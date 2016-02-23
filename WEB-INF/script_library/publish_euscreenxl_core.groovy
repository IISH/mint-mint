import gr.ntua.ivml.mint.*
import gr.ntua.ivml.mint.concurrent.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.util.*

import org.apache.log4j.Logger

import com.opensymphony.xwork2.util.TextParseUtil

// if you have already run this below, you need to clean
// on a fresh db ... CoreToEuropeana.cleanAnnotated()
// CoreToEuropeana.removeOldEdmPubRecords()
// CoreToEuropeana.cleanPublishCoreSets()()


Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));
String oaischema = Config.get( "oai.schema" );

seriesInfo = CoreToEuropeana.buildSeriesInfo()
Map noterik = CoreToEuropeana.readNoterikInfoResource( "/noterikVideos.txt" )

def seriesTitleIdx = [:]
def idIdx = [:]
	
CoreToEuropeana.buildSeriesIndex( seriesInfo, idIdx, seriesTitleIdx, noterik )

	
prs = DB.publicationRecordDAO.findAll()

for( pr in prs ) {
	if( schemas.contains( pr.getPublishedDataset().getSchema().getName())) {
		Dataset publishedDataset = pr.getPublishedDataset();
		Dataset ds2 = publishedDataset.getBySchemaName(oaischema);

		if( ds2 != null ) {
			def wrap = new Wrap( ds2, noterik, idIdx, seriesTitleIdx )
			Queues.queue( wrap, "db" )
		}
	}
}


// call this to publish everything
def publish() {
	Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));
	String oaischema = Config.get( "oai.schema" );
		
	def prs = DB.publicationRecordDAO.findAll()
	
	for( pr in prs ) {
		if( schemas.contains( pr.getPublishedDataset().getSchema().getName())) {
			Dataset publishedDataset = pr.getPublishedDataset();
			Dataset ds2 = publishedDataset.getBySchemaName(oaischema);
	
			if( ds2 != null ) {
				def publication = new BasePublication( ds2.organization )
				publication.publish( ds2, ds2.creator )
			}
		}
	}
}

class Wrap implements Runnable {

	Logger log = Logger.getLogger( Custom.class );
	Dataset ds;
	Map<String, ?> noterik
	Map<String, ?> eusIdIdx
	Map<String, ?> seriesTitleIdx
	
	public Wrap( Dataset ds, Map noterik, Map eusIdIdx, Map seriesTitleIdx ) {
		this.ds = ds;
		this.noterik = noterik
		this.seriesTitleIdx = seriesTitleIdx
		this.eusIdIdx = eusIdIdx
	}


	public void run() {
		// do my shit, transform it that is
		try {
			// throw new Exception( "")
			log.info( "Processing ds#$ds.dbID ")
			DB.getSession().beginTransaction()
			ds = DB.datasetDAO.getById( ds.dbID, false )
		
			CoreToEuropeana.invalidateNotApprovedEdm(ds, noterik )
			CoreToEuropeana.installSeriesLinks( ds, eusIdIdx, seriesTitleIdx )
			CoreToEuropeana.installScreenShots(ds, noterik )
			
		} catch( Exception e ) {
			log.error( e )
		} finally{
			DB.closeSession()
			DB.closeStatelessSession()
		}
	}
}


