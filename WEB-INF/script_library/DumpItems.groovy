
import gr.ntua.ivml.mint.concurrent.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.util.ApplyI
import gr.ntua.ivml.mint.util.Config

import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger

import com.opensymphony.xwork2.util.TextParseUtil

// sync report with Noteriks euscreen portal

itemDumpName = "itemDump.tgz"

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
	Logger log = Logger.getLogger( "gr.ntua.ivml.mint.ItemDump")
	def archiver = { TarArchiveOutputStream tos, Item item ->
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String itemXml = item.getXml();
			if(!itemXml.startsWith("<?xml"))
				itemXml = "<?xml version=\"1.0\"  encoding=\"UTF-8\" ?>\n" + itemXml;
			IOUtils.write(itemXml, baos, "UTF-8");
			baos.close();
			byte[] data = baos.toByteArray()
			TarArchiveEntry entry = new TarArchiveEntry("itemDump/Item_" + item.getDbID() + ".xml");
			entry.setSize((long) data.length );
			tos.putArchiveEntry(entry);
			tos.write(data, 0, data.length);
			tos.closeArchiveEntry();
		} catch( Exception e ) {
			log.error( "Failed to archive item ", e );
		}
	}
	
	def fileOut =  itemDumpFile.newOutputStream()
	GzipCompressorOutputStream gz = new GzipCompressorOutputStream(fileOut);
	def final tos = new TarArchiveOutputStream(gz);
	try {
		tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
		tos.putArchiveEntry(new TarArchiveEntry("itemDump/"));
		tos.closeArchiveEntry();

		// now get all the items we want in there
		// tag them with schema name so we can find out where they are from
		Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));

		def prs = DB.publicationRecordDAO.findAll()
		for( pr in prs ) {
			if( schemas.contains( pr.publishedDataset.schema.name)) {
				log.info( "Archive dataset started")
				pr.publishedDataset.processAllValidItems(new ApplyI<Item>() {
							public void apply( Item item ) {
								archiver( tos, item )
								// log.info( "Archive item called" )
							}
						}
						, false)
			}
		}
		log.info( "Regular finish")
	} finally {
		tos.finish();
		tos.close();
		fileOut.close()
	}
}
// EUscreenXL Series v2
Queues.queue( runnable, "now" )