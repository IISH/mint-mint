import gr.ntua.ivml.mint.concurrent.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.util.*

import org.apache.log4j.Logger


DB.publicationRecordDAO.findAll().each{
    def pub = new MyPub( it.publishedDataset.dbID )
	Queues.queue(pub,"db")
}

class MyPub implements Runnable  {
	Long dsId
	public MyPub( long dsId ) {
		this.dsId = dsId
	}
	
	public void run() {
		Logger log = Logger.getLogger( "gr.ntua.ivml.mint.Script")
	  try {
		  DB.getSession().beginTransaction()
		  log.info( "retreive DS #$dsId")
		def ds = DB.datasetDAO.getById( dsId, false )
        log.info( "Unpublish #$dsId")
		 ds.unpublish()
        log.info( "Publish #$dsId") 
		ds.publish( ds.creator )
        log.info( "Republished #$dsId")
	  } catch( Exception e ) {
	    log.error( "Repub #$dsId failed!", e ) 
	  } finally {
		  DB.closeSession()
		  DB.closeStatelessSession()
	  }
	}
}

