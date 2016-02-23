
import gr.ntua.ivml.mint.concurrent.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.util.ApplyI
import gr.ntua.ivml.mint.util.Config
import gr.ntua.ivml.mint.util.StringUtils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger

import com.opensymphony.xwork2.util.TextParseUtil

// sync report with Noteriks euscreen portal

itemDumpName = "itemInfo.txt"

File itemDumpFile = new File( System.properties.get( "java.io.tmpdir" ), itemDumpName )
// set force to true to force recalculation!
force = false;

if( ! force ) {
	if( itemDumpFile.exists() ) {
		// if its empty its a lock
		// if its old, ignore it
		Date now = new Date()

		if( itemDumpFile.lastModified() - now.getTime() > 7200000 ) {
			// ignore more than 2 hour old file
			// Create a new dump
		} else {
			println( "The output file already exists. Force a rerun or delete existing file. Maybe a dump is in progress?")
			def when = new Date( itemDumpFile.lastModified())
			println( "Started " + when )
			return
		}
	}
}


// rest needs to go in a thread

def runnable = {
	Logger log = Logger.getLogger( "gr.ntua.ivml.mint.ExtractItemInfo")

	def fileOut =  itemDumpFile.newOutputStream()
	def out = new PrintWriter( fileOut )

	try {
		// now get all the items we want in there
		// tag them with schema name so we can find out where they are from
		Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));

		def prs = DB.publicationRecordDAO.findAll()
		for( pr in prs ) {
			if( schemas.contains( pr.publishedDataset.schema.name)) {
				boolean isOldEuscreen = ( pr.organization.dbID == 1025 )
				pr.publishedDataset.processAllValidItems(new ApplyI<Item>() {
							public void apply( Item item ) {
								try {
									String eusId = item.getValue( "//*[local-name()='identifier']")
									String provider = item.getValue("//*[local-name()='provider']" )
									String url1 =  item.getValue( "//*[local-name()='landingPageURL']")
									String url2 =  item.getValue( "//*[local-name()='digitalItemURL']")
									String filename =  item.getValue( "//*[local-name()='filename']")
									out.println( "$isOldEuscreen\t$eusId\t$provider\t$filename\t$url1\t$url2")
								}  catch( Exception e ) {
									log.error( "Exception during item $item.dbID")
									out.println( "Didnt process item $item.dbID!")
								}
							}
				}
							, false)
							log.info( "Processed $pr.publishedItemCount from $pr.organization.englishName:$pr.originalDataset.name")
							out.flush()
						}
			}
			log.info( "Regular finish")
		} catch(Exception e ) {
			log.error( "Something went wrong!!", e )
		} finally {
			DB.closeSession()
			DB.closeStatelessSession()
			fileOut.close()
		}
	}

	Queues.queue( runnable, "now" )