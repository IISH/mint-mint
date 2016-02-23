
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.concurrent.*

// sync report with Noteriks euscreen portal

reportFilename = "sync_report.txt"

File reportFile = new File( System.properties.get( "java.io.tmpdir" ), reportFilename )
// set force to true to force recalculation!
force = false;

if( ! force ) {
  if( reportFile.exists() ) {
    // if its empty its a lock
	// if its old, ignore it
	Date now = new Date()
	
	if( reportFile.lastModified() - now.getTime() > 7200000 ) {
	  // ignore more than 2 hour old file 
	 } else {
	  if( reportFile.length() == 0 ) {
	   // its a lock
	   println( "The process is currently running. Try again later or force a rerun. ")
       def when = new Date( reportFile.lastModified())
       println( "Started " + when )
	   return
	  } else {
	    // print the contents
	    reportFile.eachLine{ println it }
		println( "!!!! Report from existing file !!! " )
        def when = new Date( reportFile.lastModified())
		println( "Created " + when )
	    return
	  }
	}
  }
}

// rest needs to go in a thread

def runnable = {
 
  def fileOut =  reportFile.newPrintWriter( "UTF8" )
  try {    
  def noterick = "http://c6.noterik.com/domain/euscreen/user/"
  def providers = new URL( noterick ).readLines()

  def allIds = [] as Set

  for( provider in providers ) {
     if( !provider.startsWith( "eu") ) continue;
     def url = noterick+provider+"/"
     def ids = new URL(url).readLines()
     for( id in ids ) {
        allIds += id
     }
  }

  def publishSchema = [ 1008l, 1009l, 1010l ]
  def now = new Date()

  def prs = DB.publicationRecordDAO.findAll()
  for( pr in prs ) {    
     def schemaId = pr.publishedDataset.schema.dbID
     if( !publishSchema.contains( schemaId)) continue
     int when = (now.time - pr.endDate.time) /1000
     if( when < 86400 ) {
        fileOut.println( "Freshly published ${pr.publishedDataset.origin.name} ignored.")
     } else {
        def missing = missingFromPortal( pr.publishedDataset, allIds )
        if( missing.size() > 0 ) {
                fileOut.println( "Dataset ${pr.publishedDataset.origin.name} in Organization ${pr.publishedDataset.organization.englishName}" )
                fileOut.println( "Published ${pr.publishedItemCount} items, ${missing.size()} not on portal!")
                fileOut.println( missing.join( ", ") )
        }
    }   
  }
  } finally {
  fileOut.close()
  }
}

println( "Generating new report!")
Queues.queue( runnable, "now" )

def missingFromPortal( Dataset ds, allIds ) {
    def ids = DB.getSession().createQuery( "select persistentId from Item where dataset_id = $ds.dbID and valid=true").list()
    def missing = ids.findAll{ !allIds.contains( it )}    
    return missing
}

//println "Read ${allIds.size()} ids"