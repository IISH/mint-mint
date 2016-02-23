package gr.ntua.ivml.mint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.persistent.XpathHolder;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.Config;
import gr.ntua.ivml.mint.util.Counter;
import gr.ntua.ivml.mint.util.StringUtils;
import net.minidev.json.JSONNavi;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class FashionPublication extends BasePublication {
	
	String portalUrl = "";
	private DefaultHttpClient httpclient;

	public FashionPublication(Organization org) {
		super(org);
		portalUrl = Config.get( "portal.url");
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setDefaultMaxPerRoute(8);
		httpclient = new DefaultHttpClient(cm);
	}

	
	/**
	 * Send the data away. If you want all the Transformation magic, you can just overwrite this one.
	 * @param ds
	 * @return
	 */
	public void externalPublish( Dataset ds, User publisher ) {
		String oaiSchema = Config.get( "oai.schema" );
		final Dataset originalDataset = ds.getOrigin();
		
		log.debug( "Portal publish " + originalDataset.getName() );
		final Dataset derivedItemsDataset = ds.getBySchemaName(oaiSchema);
		originalDataset.logEvent("Sending to Portal.", "Use schema " + oaiSchema + " actual Dataset #" + derivedItemsDataset.getDbID() );
		
		// get or make a publication record
		
		PublicationRecord pr;
		pr = DB.getPublicationRecordDAO().getByPublishedDataset(derivedItemsDataset);
		if( pr == null ) {

			pr = new PublicationRecord();

			pr.setStartDate(new Date());
			pr.setPublisher(publisher);
			pr.setOriginalDataset(originalDataset);
			pr.setPublishedDataset(derivedItemsDataset );
			pr.setStatus(Dataset.PUBLICATION_RUNNING);
			pr.setOrganization(ds.getOrganization());
			DB.getPublicationRecordDAO().makePersistent(pr);
			DB.commit();
		} else {
			pr.setStatus(Dataset.PUBLICATION_RUNNING);
			DB.commit();			
		}
		
		final Counter itemCounter = new Counter(0);
		final Counter problemCounter = new Counter(0);
		HttpContext httpContext = new BasicHttpContext();
		
		String report = null;
		try {
		    report = createReport( originalDataset, httpContext );
			final JSONObject template = portalTemplate( derivedItemsDataset );
			template.put( "reportId",  report );
			
			
			ApplyI<Item> itemSender = new ApplyI<Item>() {
				@Override
				public void apply(Item item) throws Exception {
						String json = itemPortalJson(item, template);
						if( insertRecord( json )) {
							itemCounter.inc();
						} else {
							problemCounter.inc();
						}
				}
			};
			
			derivedItemsDataset.processAllValidItems(itemSender, false);
			
			// put the report in the log message!
			ds.logEvent( "Finished publishing. " + itemCounter.get() + " items send.", 
					problemCounter.get()>0
						? ( "There were problems with " + problemCounter.get() + " items.")
					    : "" );
			
			pr.setPublishedItemCount(itemCounter.get());
			pr.setEndDate(new Date());
			pr.setStatus(Dataset.PUBLICATION_OK);
			DB.commit();
		} catch( Exception e ) {
			log.warn( "Item publication went wrong", e );
			ds.logEvent( "Publication went wrong. " + e.getMessage());
			pr.setStatus(Dataset.PUBLICATION_FAILED);
			pr.setEndDate(new Date());
			pr.setReport(e.getMessage());
			pr.setPublishedItemCount(itemCounter.get());
			DB.commit();
		} finally {
			if( report != null ) {
				try {
					closeReport( report, httpContext );
				} catch( Exception e ) {
					log.error( "Report closing problem", e );
				}
			}
		}
		
	}

	public boolean externalUnpublish( Dataset ds  ) {
		HttpContext httpContext = new BasicHttpContext();

		try {
			// not sure do we need the original or the published dataset id here
			simpleGet( httpContext, "api/deleteByDatasetId", "datasetId", ds.getOrigin().getDbID().toString());
			ds.logEvent("Unpublished from Portal.", "Dataset #"+ds.getDbID().toString() + " removed from Portal.");
			return true;
		} catch( Exception e ) {
			log.error( "Error during unpublish", e );
			ds.logEvent("Error during unpublish", e.toString() );
			return false;
		} finally {
			DB.commit();
		}
	}
	
	private String itemPortalJson( Item item, JSONObject template ) {
		String res = JSONNavi.newInstance()	
			.set( "record", item.getXml())
			.set( "hash", StringUtils.sha1Utf8( item.getXml()))
			.at( "sets")
				.add( template.get("datasetId"), template.get( "orgId"), template.get( "userId")).up()
			.set( "datasetId", (Long) template.get( "datasetId"))
			.set( "mintOrgId", (Long) template.get( "orgId"))
			.set( "userId",  (Long) template.get( "userId"))
			.set( "recordId", item.getDbID())
			.set( "reportId",  template.get( "reportId").toString())
			.set( "sourceItemId", item.getSourceItem().getDbID())
			.set( "sourceDatasetId", (Long) template.get( "sourceDatasetId"))
			.set( "datestamp", System.currentTimeMillis())
			.at( "namespace")
				.set( "prefix", (String) template.get( "uriPrefix "))
				.set( "uri", (String) template.get( "uri" ))
				.up()
			.set( "schemaId", (Long) template.get( "schemaId"))
			.asString();
			
		return res;
	}
	
	/**
	 * Accumulate dataset dependend attributes in a JSONObject
	 * @param ds
	 * @return
	 */
	private JSONObject portalTemplate( Dataset ds) {
		XpathHolder xmlRoot = ds.getRootHolder().getChildren().get(0);

		JSONNavi nav = JSONNavi.newInstance()
		.set( "datasetId", ds.getDbID())
		.set( "orgId", ds.getOrganization().getDbID())
		.set( "userId", ds.getCreator().getDbID())
		.set( "sourceDatasetId", ds.getOrigin().getDbID())
		.set( "uri", xmlRoot.getUri())
		.set( "uriPrefix", xmlRoot.getUriPrefix());
		if( ds.getSchema() != null ) nav.set( "schemaId", ds.getSchema().getDbID());
		return (JSONObject) nav.root().getCurrentObject();
	}
	
	
	/**
	 * Sends the given String to the portal as one record.
	 * @param item
	 * @return
	 */
	public boolean insertRecord( String item ) {
		HttpPost post = null;
	
		try {
			post = new HttpPost( portalUrl + "api/insertBlocking" );
			StringEntity ent = new StringEntity( item, "UTF-8");
			
			ent.setContentType("application/json");
			post.setEntity(ent);
			httpclient.execute(post);
			return true;
		} catch( Exception e ) {
			log.error( "Exception during insert of Record", e );
			return false;
		} finally {
			if( post != null )
				post.releaseConnection();
		}
	}
	
	public String createReport(Dataset ds, HttpContext httpContext) throws Exception {
		try {
			String resp = simpleGet( httpContext, "api/createReport", "datasetId", ds.getDbID().toString(), "orgId", ds.getOrganization().getDbID()+"" );
			JSONObject res = (JSONObject) JSONValue.parse( resp );
			return  res.get( "reportId" ).toString();
		} catch( Exception e ) {
			log.error( "Error during create report!", e );
			throw e;
		}
	}
 	
	public void closeReport( String reportId, HttpContext httpContext  ) throws Exception {
		try {
			String resp = simpleGet( httpContext, "api/closeReport", "reportId", reportId );
		} catch( Exception e ) {
			log.error( "Error during close report!", e );
			throw e;
		}
	}
	
	private String simpleGet( HttpContext httpContext, String verb, String... params ) throws Exception {
		HttpGet get = new HttpGet(portalUrl+"/"+verb );
		try {
			URIBuilder builder = new URIBuilder( get.getURI());
			if( params != null ) {
				for( int i=0; i<params.length; ) {
					String key = params[i];
					i++;
					if( i<params.length ) {
						String val = params[i];
						builder.addParameter(key, val);
					}
					i++;
				}
			}
			URI uri = builder.build();
			get.setURI(uri);
			HttpResponse response = httpclient.execute(get, httpContext );
			BufferedReader rd = new BufferedReader
					(new InputStreamReader(response.getEntity().getContent()));
			String resp = org.apache.commons.io.IOUtils.toString(rd);	
			return resp;
		} finally {
			get.releaseConnection();
		}
	}
	

}
