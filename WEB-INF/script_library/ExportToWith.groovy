
import gr.ntua.ivml.mint.concurrent.Queues
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.mapping.MappingConverter;
import gr.ntua.ivml.mint.mapping.model.Mappings;
import gr.ntua.ivml.mint.persistent.*;
import gr.ntua.ivml.mint.util.*
import gr.ntua.ivml.mint.xml.transform.XMLFormatter;
import gr.ntua.ivml.mint.xml.transform.XSLTGenerator;
import gr.ntua.ivml.mint.xml.transform.XSLTransform
import net.minidev.json.JSONNavi
import net.minidev.json.JSONObject
import net.minidev.json.JSONValue

import org.apache.commons.io.IOUtils
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.apache.log4j.Logger

// Make a duplication report for aggregation

outputName = "withExport.txt"

File outputFile = new File( System.properties.get( "java.io.tmpdir" ), outputName )
// set force to true to force recalculation!
force = true;

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
def runnable = new ExportToWithImpl( fileOut )
Queues.queue( runnable, "now" )

// rest needs to go in a thread
class ExportToWithImpl implements Runnable {

	Logger log = Logger.getLogger( "gr.ntua.ivml.mint.LongScript")
	PrintWriter output

	static class WithExporter implements Runnable {
		Dataset ds;
		Mapping mp;
		HttpClient hc;

		XSLTransform tr;
		int failedItems, exportedItems
		int counter =1
		String collectionId;
		Logger log = Logger.getLogger( "gr.ntua.ivml.mint.LongWithExporter")
		PrintWriter output


		public void run() {
			try {
				// reget the params
				mp = DB.mappingDAO.getById( mp.dbID, false )
				ds = DB.datasetDAO.getById( ds.dbID, false )
				hc = new DefaultHttpClient()
				tr = new XSLTransform()

				tr.setXSL( getXsl() );
				if( !loginToWith()) {
					log.error( "Cannot proceed without login" );
					return
				}
				collectionId = createCollection()

				def perItem = new ApplyI<Item>() {
							void apply( Item edmItem ) {
								String originalXml = edmItem.getImportItem().getXml();
								String beforeMap = edmItem.getXml()
								String withXml = tr.transform(beforeMap)

								Item item =  new Item();
								item.setXml( withXml );
								// we could check here, but we could skip that as well :-)

								JSONNavi res = new JSONNavi()
										.set( "source",  "Mint - EUscreenXL")
										.set( "sourceId", item.getValue("//*[local-name()='sourceId']") as String )
										.set( "title",  item.getValue("//*[local-name()='title']") as String )
										.set( "externalId",  StringUtils.md5Utf8(item.getValue("//*[local-name()='externalId']")) as String )
										.set( "description",  item.getValue("//*[local-name()='description']") as String )
										.set( "thumbnailUrl",  item.getValue("//*[local-name()='thumbnailUrl']") as String )
										.set( "sourceUrl",  item.getValue("//*[local-name()='sourceUrl']") as String )
										.set( "isShownAt", item.getValue("//*[local-name()='isShownAt']") as String )
										.set( "isShownBy", item.getValue("//*[local-name()='isShownBy']") as String )
										.set( "itemRights",  item.getValue("//*[local-name()='rights']") as String)
										.set( "provider",  item.getValue("//*[local-name()='provider']") as String )
										.set( "type",  item.getValue("//*[local-name()='type']" as String ))
										.set( "dataProvider",  item.getValue("//*[local-name()='dataProvider']" as String ))
										 
								res.at( "content" )
								res.set( "XML-UNKNOWN", originalXml )
								if( ds.schema.name =~ /EDM/ ) {
									res.set( "XML-EDM", beforeMap )
								}
								if( ds.schema.name =~ /EUscreen/ ) {
									// res.set( "XML-EUSCREEN", beforeMap )
								}

								res.up()

								HttpPost createItem = new HttpPost( Config.get( "with.url" )
										+ "/collection/" + collectionId + "/addRecord" );

								StringEntity se = new StringEntity( res.toString(), "UTF8" );
								createItem.setEntity( se );
								createItem.addHeader( "Content-type", "text/json");

								//	log.debug(createItem.toString());
								HttpResponse httpRes = hc.execute( createItem );
								String jsonResponse = EntityUtils.toString( httpRes.getEntity(), "UTF8" );
								//log.debug("response is: "+ jsonResponse);
								createItem.releaseConnection();
								createItem.abort();
								counter++
								// EntityUtils.consumeQuietly( res.getEntity());
								if( httpRes.getStatusLine().getStatusCode() != 200 ) {
									log.debug( "Record xx export to WITH respond with " + httpRes.getStatusLine());
									failedItems++;
								} else {
									if(( counter % 100) == 0 ) {
										log.info( exportedItems + " records from Dataset #" + ds.getDbID() + " exported to WITH.");
									}
									exportedItems++;
								}

							}
						}

				if( collectionId != null) {
					if( ds.validItemCount > 0 ) {
						log.info( "Export ds #$ds.dbID with $ds.validItemCount Items.")
						ds.processAllValidItems( perItem, true )
					} else 
						log.info("Dataset #$ds.dbID has no valid Items.")	
				} else {
					log.error( "There is no collection for DS #$ds.dbID" )
				}

			} catch( Exception e)  {
				log.error( "Exception during item send", e )
			} finally {
				DB.closeSession()
				DB.closeStatelessSession()
				hc.getConnectionManager().shutdown();
			}


		}

		public String getXsl() {
			XSLTGenerator xslt = new XSLTGenerator();

			xslt.setItemXPath(ds.getItemRootXpath().getXpathWithPrefix(true));
			xslt.setImportNamespaces(ds.getRootHolder().getNamespaces(true));
			xslt.setOption(XSLTGenerator.OPTION_OMIT_XML_DECLARATION, true);
			//xslt.setNamespaces(ftr.getDataUpload().getRootXpath().getNamespaces(true));

			Mappings mappings = mp.getMappings();
			String xsl = XMLFormatter.format(xslt.generateFromMappings(mappings));

			return xsl;
		}


		public String 	createCollection() {
			// hc is now authorized to publish stuff
			HttpPost createCollection = new HttpPost( Config.get( "with.url" )
					+ "/collection/create");

			String collectionName = ds.organization.shortName+"-"+ ds.origin.name

			String jsonBody = JSONNavi.newInstance()
					.set("title", collectionName)
					.set("description", "Mint datasetId #"+ds.origin.dbID)
					.set("isPublic", "true")
					.toString();

			log.debug("Json entity is : " + jsonBody);
			createCollection.setEntity(new StringEntity(jsonBody));
			createCollection.addHeader("Content-type", "text/json");

			HttpResponse response = hc.execute(createCollection);

			String jsonResponse = EntityUtils.toString(
					response.getEntity(), "UTF8");
			createCollection.releaseConnection();
			createCollection.abort();

			log.debug("Collection created response: " + jsonResponse);
			if (response.getStatusLine().getStatusCode() != 200) {
				//return error message
				log.info("Dataset #"+ds.origin.dbID + " not created collection!" );
				return null;
			} else {
				JSONObject obj = (JSONObject) JSONValue.parse(jsonResponse);
				log.info("Dataset #"+ds.origin.dbID + " created collection " +  obj.get( "dbId").toString());
				return obj.get( "dbId").toString();
			}
		}

		public boolean loginToWith() {
			HttpPost loginToWith = new HttpPost( Config.get( "with.url" )
					+ "/user/login");
			String jsonBody = new JSONNavi()
					.set( "email", "euscreen" )
					.set( "password", "123456" )
					.toString()

			loginToWith.setEntity(new StringEntity(jsonBody));
			loginToWith.addHeader("Content-type", "text/json");

			HttpResponse response = hc.execute(loginToWith);
			if (response.getStatusLine().getStatusCode() == 200) {
				// hopefully handles the connection ...
				loginToWith.releaseConnection()
				loginToWith.abort();
				return true;
			} else {
				log.error( "login as user euscreen failed")
				String resString = IOUtils.toString( response.entity.content , "UTF8")
				log.error( "Response was \n" + resString );
				return false;
			}
		}

		public WithExporter( Dataset ds, Mapping mp, PrintWriter output ) {
			this.ds = ds;
			this.mp = mp
			this.output = output
		}
	}


	public void run() {
		try {
			def prs = DB.publicationRecordDAO.findAll()
			  .sort{ a,b ->
				  a.publishedDataset.organization.dbID <=> b.publishedDataset.organization.dbID
			  }
			int limit = -1
			// Config.setLive("with.url", "147.102.11.186:9000" )
			Mapping mp = DB.mappingDAO.simpleGet( "name='WITH-EDM'")
			// find all published sets
			// create runnables for the exported dataset
			// queue them

			if( mp != null ) {
				for( PublicationRecord pr: prs ) {
					if( pr.publishedDataset.schema.name =~ /EDM/ ) {
						def we = new WithExporter( pr.publishedDataset, mp, output )
						Queues.queue( we, "db" )
						limit--
					} else if( pr.publishedDataset.schema.name =~ /EUscreen/ ){
						// do nothing for now :-)
					}
					if( limit ==0 ) break;
				}
			} else {
				log.error( "Mapping  WITH-EDM not found" )
			}
		} catch( Exception e ) {
			log.error( "Problem in script", e );
		} finally {
			DB.closeSession()
			DB.closeStatelessSession()
			output.close()
		}
	}

	public ExportToWithImpl( PrintWriter output ) {
		this.output = output
	}
}





