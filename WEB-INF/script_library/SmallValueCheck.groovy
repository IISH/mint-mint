
import gr.ntua.ivml.mint.concurrent.Queues
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.util.*

import java.io.File
import java.io.PrintWriter
import java.util.Date
import java.util.List
import java.util.Map
import java.util.Set

import nu.xom.*

import org.apache.log4j.Logger

import com.opensymphony.xwork2.util.TextParseUtil

// Make a duplication report for aggregation

outputName = "aggdup.txt"

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
			println( "The output file already exists. Force a rerun or delete existing file. Maybe an analysis is in progress?")
			def when = new Date( outputFile.lastModified())
			println( "Started " + when )
			
			outputFile.eachLine{ println it }
			
			return
		}
	}
}

def fileOut =  outputFile.newPrintWriter( "UTF8" )
def runnable = new Duplication( fileOut )
Queues.queue( runnable, "now" )

// rest needs to go in a thread
class Duplication implements Runnable {
	Logger log = Logger.getLogger( "gr.ntua.ivml.mint.LongScript")
	PrintWriter output
	Map allIds = [:]

	public void run() {
		try {
			def prs = DB.publicationRecordDAO.findAll()
			def publishedSchemaNames= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.aggregate.schema"));

			Map orgPrs = prs.findAll{ publishedSchemaNames.contains( it.publishedDataset.schema.name ) }.groupBy{ it.organization }

			orgPrs.findAll{ k,v ->
				output.print k.englishName + " "
				output.println  v.inject( 0, {i,o -> i+o.publishedItemCount})
			}
			output.flush()

			int count = 2
			for( Organization org in orgPrs.keySet()) {
				count--
				if( count ==0 ) break;
				allIds.clear();
				output.println( "Organization $org.englishName")
				output.flush()
				for( PublicationRecord pr in orgPrs[ org] ) {
					if( publishedSchemaNames.contains( pr.publishedDataset.schema.name ) ) {
						output.println "Analyze $pr.publishedDataset.dbID with $pr.publishedItemCount items."
						output.flush()
						log.info "Analyze $pr.publishedDataset.dbID with $pr.publishedItemCount items."
						
						analyze( pr.publishedDataset )
					}
				}

				output.println( "Found ${allIds.size()} ids in datasets")
				Set dss = [] as Set
				allIds.each{ k,v ->
					if( v.size() > 1 ) {
						output.println "$k, $v"
						v.each{ dss.add( it )}
					}
				}
				output.flush()
				dss.each{
					def ds = DB.datasetDAO.getById( it, false )
					output.println "DS:$ds.dbID ($ds.origin.name:$ds.organization.englishName)"
				}
				output.println( "----- finished $org.englishName")
				output.println()
				output.flush()
			}
		} catch( Exception e ) {
			log.error( e )
		} finally {
			output.close()
		}
	}

	public void  analyze( Dataset ds ) {
		final Map ids = allIds
		final Map invalids = [:]
		ds.processAllValidItems( new ApplyI<Item>() {
			public void apply( Item item ) {
				String id = item.getValue( "//*[local-name()='Aggregation']/@*[local-name()='about']")
				if( id == null ) {
					if( invalids.containsKey( "invalidCount")) {
						invalids["invalidCount"] = invalids["invalidCount"] + 1
					}  else
					invalids["invalidCount"] = 1;
				} else {
					if( ids.containsKey( id )) {
						ids[id].add( ds.dbID )
					} else {
						ids[id] = [ ds.dbID ] as List
							}
						}
					}
				}, false)
		if( invalids.containsKey("invalidCount") ) {
			output.println "DS:$ds.dbID ($ds.origin.name:$ds.organization.englishName) contains $invalids"
		}
	}

	public Duplication( PrintWriter output ) {
		this.output = output
	}
}



