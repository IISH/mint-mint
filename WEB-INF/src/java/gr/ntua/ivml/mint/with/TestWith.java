package gr.ntua.ivml.mint.with;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.Config;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class TestWith implements Runnable{
	public static final Logger log = Logger.getLogger( TestWith.class );
	Dataset dataset;
	PublicationRecord pr;

	String collectionId;
	private HttpClient hc;
	
	
	public TestWith( Dataset ds , PublicationRecord pr, String collectionId,HttpClient hc) throws Exception {
		this.collectionId = collectionId;
		this.dataset = ds;
		this.pr = pr;
		this.hc = hc;
	}
	
	@Override
	public void run() {
		log.debug("with export started");
		try {
			DB.getSession().beginTransaction();
			DB.getStatelessSession().beginTransaction();
			dataset = DB.getDatasetDAO().getById(dataset.getDbID(), false);
			runInThread();
		} catch( Exception e ) {
			log.info( "Error while exporting ", e );
		} finally {
			DB.commit();
			DB.closeSession();
			DB.closeStatelessSession();
		}
		
	}

	private void runInThread() throws Exception {
		
		try {
			final Dataset publishDataset = dataset.getBySchemaName("EDM");
			if( publishDataset == null ) {
				throw new Exception( "Non WITH schema export not supported");
			}

			
			ApplyI<Item> itemSender = new ApplyI<Item>() {
				int counter = 1;
				
				public void apply(Item item) throws Exception {
				
					With test  = new With(item);
										
					net.sf.json.JSONObject result = test.toJson(); 
					
				//	result.put("content", item.getXml());
					
					log.debug("Item result is : ");
	
					log.debug(result.toString());
					String jsonBody = result.toString();

					//Todo: Change the call to with to add new record
					
					HttpPost createItem = new HttpPost( Config.get( "with.url") 
							+ "/collection/" + collectionId + "/addRecord" );
					StringEntity se = new StringEntity( jsonBody, "UTF8" );
					createItem.setEntity( se );
					createItem.addHeader( "Content-type", "text/json");
					HttpResponse res = hc.execute( createItem );
					String jsonResponse = EntityUtils.toString( res.getEntity(), "UTF8" );
					createItem.releaseConnection();
					createItem.abort();
					if( res.getStatusLine().getStatusCode() != 200 ) {
						log.debug( "Record xx export to WITH respond with " + res.getStatusLine());
					} else {
						if(( counter % 100) == 0 ) {
							log.info( counter + " records from Dataset #" + publishDataset.getDbID() + "exported to WITH.");
						}
						counter++;
					}
					
				}
			};
			
			publishDataset.processAllValidItems(itemSender, true );
			
			
			pr.setStatus(PublicationRecord.PUBLICATION_OK);
			pr.setPublishedItemCount(dataset.getValidItemCount());
			dataset.logEvent( "Finished export to With","");
			DB.commit();
		} catch (Exception e) {
			pr.setStatus(PublicationRecord.PUBLICATION_FAILED);
			log.error( "There was a problem during export of the  dataset to With!", e );
			dataset.logEvent( "There was a problem while during export of the dataset to With!" );
			DB.commit();
		}  finally {
             hc.getConnectionManager().shutdown();
             DB.getPublicationRecordDAO().makeTransient(pr);
		}
	}
	
}
