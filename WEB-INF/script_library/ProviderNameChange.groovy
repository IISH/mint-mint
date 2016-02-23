
import gr.ntua.ivml.mint.actions.EuscreenPublish
import gr.ntua.ivml.mint.concurrent.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.util.ApplyI

import org.apache.log4j.Logger
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
def runnable = new ProviderNameChanges( fileOut )
Queues.queue( runnable, "now" )

// rest needs to go in a thread
class ProviderNameChanges implements Runnable {
	Map changes = [ 
		"ERT" : "ERT SA",
		"HeNAA" : "ERT SA",
		"ERT S.A.": "ERT SA"]
		
	Logger log = Logger.getLogger( "gr.ntua.ivml.mint.LongScript")
	PrintWriter output

	List dsIds = [4598, 4577, 5024, 4766, 5034, 4416, 4603, 5005, 4587, 4798, 4552, 4589, 5093, 4596, 4914, 4924,  // HeNAA
		4682, 8258, 4658, 4685, 4819, 4670, 4740, 4688, 4665, 4560, 4687, 4689, 4553, 8149, 8151, 4673, 4661, 4746] //ERT

	def providerChange =  new ApplyI<Item>() {
		public void apply( Item item ) {
			def givenProvider = item.getValue( "//*[local-name()='provider']")
			def changedProvider = changes[givenProvider]
			if( changedProvider != null ) {
				item.setValue( "//*[local-name()='provider']", changedProvider )
			}
		}
	}

	public void run() {
		try {
			
			// Make sure we have transaction
			DB.getSession().beginTransaction()
			
			// unpublish them
			output.println( "Processing ${dsIds.size()} datasets.")
			for( dsid in dsIds ) {
				Dataset ds = DB.datasetDAO.getById( dsid as Long, false);
				unpublish( ds )
			}
			output.println( "Unpublished Datasets")
			output.flush()
			for( dsid in dsIds ) {
				Dataset ds = DB.datasetDAO.getById( dsid as Long, false);
				log.info("Changeing ${ds.itemCount} items in #$ds.dbID")
				output.println( "Changeing ${ds.itemCount} items in #$ds.dbID")
				output.flush()
				ds.processAllItems(providerChange, true)
			}
			output.flush()
			for( dsid in dsIds ) {
				Dataset ds = DB.datasetDAO.getById( dsid as Long, false);
				publish( ds )
			}

		} catch( Exception e ) {
			log.error( e );
		} finally {
			output.println( "Finished!")
			output.close()
			DB.closeSession()
		}
	}

	public ProviderNameChanges( PrintWriter output ) {
		this.output = output
	}
	
	public unpublish( Dataset du ) {
		def publisher = new EuscreenPublish()
		publisher.setCmd( "portalUnpublish")
		publisher.setDatasetId( du.dbID as String )
		def result = publisher.execute()
		output.println( "$du.dbID unpublish: $result" )
	}
	
	def publish( Dataset du ) {
		def publisher = new EuscreenPublish()
		publisher.setCmd( "portalPublish")
		publisher.setDatasetId( du.dbID as String )
		publisher.setUser( du.getCreator() )
		def result = publisher.execute()
		output.println( "$du.dbID Publish: $result" )
	}
}


