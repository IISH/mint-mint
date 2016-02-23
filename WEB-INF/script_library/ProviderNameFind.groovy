
import gr.ntua.ivml.mint.concurrent.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.util.ApplyI
import gr.ntua.ivml.mint.util.Config

import java.util.Map;

import org.apache.log4j.Logger

import com.opensymphony.xwork2.util.TextParseUtil

// sync report with Noteriks euscreen portal

outputName = "out.txt"

File outputFile = new File( System.properties.get( "java.io.tmpdir" ), outputName )
// set force to true to force recalculation!
force = false;

if( ! force ) {
	if( outputFile.exists() ) {
		// if its empty its a lock
		// if its old, ignore it
		Date now = new Date()

		if( outputFile.lastModified() - now.getTime() > 7200000 ) {
			// ignore more than 2 hour old file
			// Create a new dump
		} else {
			println( "The output file already exists. Force a rerun or delete existing file. Maybe a dump is in progress?")
			def when = new Date( outputFile.lastModified())
			println( "Started " + when )
			return
		}
	}
}

def fileOut =  outputFile.newPrintWriter( "UTF8" )
def runnable = new ProviderChanges( fileOut )
Queues.queue( runnable, "now" )

// rest needs to go in a thread
class ProviderChanges implements Runnable {
		Logger log = Logger.getLogger( "gr.ntua.ivml.mint.LongScript")
	PrintWriter output


	public void run() {
		try {
			Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));
			def prs = DB.publicationRecordDAO.findAll()
			def publishedDs = [:]
			output.println( "Looking into ${prs.size()} datasets")
			output.flush()
			for( pr in prs ) {
				if( schemas.contains( pr.publishedDataset.schema.name)) {
					Dataset ds = pr.publishedDataset
					// first item
					def items = ds.getValidItems(0, 1)
					if(( items == null ) || (items.size()!=1)) log.info( "Dataset #$ds.dbID no valid items.")
					else {
						log.info("Checking providername in #$ds.dbID")
						def givenProvider = items[0].getValue( "//*[local-name()='provider']")
						if( givenProvider != null ) {
							List dsList = publishedDs[givenProvider]
							if( dsList == null ) {
								publishedDs[givenProvider] = [ds.dbID] as List
							} else {
								dsList.add( ds.dbID )
							}
						}
					}
				}
			}
			output.println( publishedDs )
			output.flush()
		} catch( Exception e ) {
			log.error( e );
		} finally {
			output.close()
		}
	}

	public ProviderChanges( PrintWriter output ) {
		this.output = output
	}
	
}


