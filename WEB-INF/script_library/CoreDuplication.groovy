
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

outputName = "coredup.txt"

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
def runnable = new CoreDuplicationImpl( fileOut )
Queues.queue( runnable, "now" )

// rest needs to go in a thread
class CoreDuplicationImpl implements Runnable {
	Logger log = Logger.getLogger( "gr.ntua.ivml.mint.LongScript")
	PrintWriter output
	Map allIds = [:]

	public void run() {
		try {
			def prs = DB.publicationRecordDAO.findAll()
			def publishedSchemaNames= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));

			for( pr in prs ) {
				if( publishedSchemaNames.contains( pr.publishedDataset.schema.name ) ) {
					analyze( pr.publishedDataset )
				}
			}
			
			output.println( "Found ${allIds.size()} ids in datasets")
			def dss = [] as Set
			allIds.each{ k,v ->
				if( v.size() > 1 ) {
					output.println "$k, $v"
					v.each{ dss.add( it )}
				}
			}

			dss.each{
				def ds = DB.datasetDAO.getById( it, false )
				output.println "DS:$ds.dbID ($ds.origin.name:$ds.organization.englishName)"
			}
		} catch( Exception e ) {
			log.error( "Problem in script", e ); 
		} finally {
			DB.closeSession()
			DB.closeStatelessSession()
			output.close()
		}
	}
	
	def analyze( Dataset ds ) {
		final Map invalids = [:]
		log.info("Analysing $ds.validItemCount items in #$ds.dbID!")
		
		ds.processAllValidItems( new ApplyI<Item>() {
					public void apply( Item item ) {
						if( ds.organization.englishName != "Old euscreen data" ) {
							String provider = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='provider']");
							String originalId = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='originalIdentifier']");
							String id = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']");

							if(!( StringUtils.empty(originalId) || StringUtils.empty( provider))) {
								String eusId = "EUS_"+StringUtils.md5Utf8(StringUtils.join( provider,":",originalId));
								// find datasets where id needs updateing NOT in old euscreen (there it wouldnt work)
								if( eusId != id ) {
									invalids["update"] = 1;
								}
							}
						}
						String id = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']" )
						if( id == null ) {
							if( invalids.containsKey( "invalidCount")) {
								invalids["invalidCount"] = invalids["invalidCount"] + 1
							}  else

								invalids["invalidCount"] = 1;
						} else {
							if( allIds.containsKey( id )) {
								allIds[id].add( ds.dbID )
							} else {
								allIds[id] = [ ds.dbID ] as List
							}
						}
					}
				}, false)

		if( invalids.containsKey("invalidCount") ) {
			output.println "DS:$ds.dbID ($ds.origin.name:$ds.organization.englishName) contains $invalids"
			output.flush()
		}
		if( invalids.containsKey( "update")) {
			output.println "DS:$ds.dbID ($ds.origin.name:$ds.organization.englishName) is updateable"
			output.flush()
		}
	}

	public CoreDuplicationImpl( PrintWriter output ) {
		this.output = output
	}
}



