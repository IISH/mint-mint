package gr.ntua.ivml.mint.actions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.util.TextParseUtil;

import gr.ntua.ivml.mint.BasePublication;
import gr.ntua.ivml.mint.concurrent.Queues;
import gr.ntua.ivml.mint.concurrent.Solarizer;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.Config;
import gr.ntua.ivml.mint.util.PublishQueue;
import gr.ntua.ivml.mint.util.StringUtils;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;

/**
 * All activities around publishing in Euscreenxl should happen here.
 * publish on portal, unpublish portal,
 * publish / unpublish europeana
 * item production status change 
 * 
 * @author Arne Stabenau 
 *
 */
@Results({	  
	  @Result(name="input", location="/WEB-INF/custom/euscreenxl/jsp/publishResponse.jsp"),
	  @Result(name="error", location="/WEB-INF/custom/euscreenxl/jsp/publishResponse.jsp"),
	  @Result(name="success", location="/WEB-INF/custom/euscreenxl/jsp/publishResponse.jsp")

})



public class EuscreenPublish extends GeneralAction {
	public static final String EUSCREEN_DEFAULT_ID = "EUS_00000000000000000000000000000000";
	
	public static class PortalPublish implements Runnable {
		private Dataset ds;
		private boolean publish;
		public PublicationRecord pr;
		
		private int problemCounter = 3;
		
		public static HashMap<String, String> thesaurusLiteralToUrl = new HashMap<String, String>();
		public static HashMap<String, String> thesaurusAmbiguousToUrl = new HashMap<String, String>();
		public static HashSet<String> thesaurusBadLiteral = new HashSet<String>();
		
		public static boolean forceThesaurusOverwrite = true;

		public static List<String> providerNameChanges =
				Arrays.asList(
						// ERT is already correct
				// "HeNAA", "ERT SA",
				// "ERT", "ERT SA",
				// "DW", "Deutsche Welle",
				"NINA", "Narodowy Instytut Audiowizualny",
				"KB", "Kungliga biblioteket",
				"TVC", "TV3 Televisió de Catalunya (TVC)",
				"SASE", "Screen Archive South East",
				"NISV", "Netherlands Institute for Sound and Vision" );
		
		public static HashMap<String,String> noterikKnownIds = new HashMap<String,String>();
		
		public PortalPublish( Dataset ds, boolean publish, PublicationRecord pr ) {
			this.ds = ds;
			this.publish =  publish;
			this.pr = pr;
		}

		static {
			readThesaurus();
			readThesaurusExtension();
			readNoterikIds();
		}
		
		/**
		 * Make sure the new providername is in the item. 
		 *  if there is an id set in here, leave it
		 *  if there is no id, check if old or new id is known to Noterik. If both, flag a problem and set id from new provider
		 *  if one, use that id. If there is none, use the new provider name derived id.
		 * @param item
		 */
		public static void adjustProvider( Item item ) {
			String providername = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='provider']" );
			if( providerNameChanges.contains( providername )) {
				String originalId = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='originalIdentifier']");

				String currentId = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']");
				String oldProviderName, newProviderName;
				
				int i=0;
				for( ;i<=providerNameChanges.size(); i++ ) {
					if( providerNameChanges.get(i).equals(providername)) break;
				}
				if( i%2 == 0 ) {
					oldProviderName = providerNameChanges.get(i);
					newProviderName = providerNameChanges.get(i+1);
				} else {
					oldProviderName = providerNameChanges.get(i-1);
					newProviderName = providerNameChanges.get(i);					
				}
				
				// these are the possible ids that the item can have
				String oldId = "EUS_"+StringUtils.md5Utf8(StringUtils.join(oldProviderName,":",originalId));
				String newId = "EUS_"+StringUtils.md5Utf8(StringUtils.join(newProviderName,":",originalId));
				
				if( currentId == null ) { // should not happen 
					log.info( "There should be an euscreen id element in the xml. Missing in item #" + item.getDbID());
					return;
				}
				
				// adjust providername 
				if( !providername.equals( newProviderName)) {
					item.setValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='provider']", newProviderName );
					log.info( "Adjusted providername to " + newProviderName  + " in " + currentId );
				}
				
				// bail for the euscreen-ids that are not providername / local-id based 
				if( !( currentId.equals( oldId ) || currentId.equals( newId ))) return;
				
				if( noterikKnownIds.containsKey(oldId) && noterikKnownIds.containsKey( newId)) {
					// flag a problem
					log.info( "Item #"+ item.getDbID() + " has EUS id for old and new provider '"+newProviderName+"' at Noterik ( " + oldId + "/" + newId + ")" );
					log.info( "Published state is " + oldId + "=>'" + noterikKnownIds.get(oldId) +"' " + newId + "=>'" + noterikKnownIds.get( newId ) + "'");
					log.info( "Dataset #" + item.getDatasetId());
					// choose new id
					item.setValue( "//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']", newId );
					if(! currentId.equals( newId )) log.info( "Change from " + currentId + " to new provider " + newId );
				} else if( noterikKnownIds.containsKey(oldId)){
					item.setValue( "//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']", oldId );						
					if(! currentId.equals( oldId )) log.info( "Change from " + currentId + " to old provider " + oldId );
				} else {
					item.setValue( "//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']", newId );												
					if(! currentId.equals( newId )) log.info( "Change from " + currentId + " to new provider " + newId );
				}
			} 
		}
		
		public static void readNoterikIds() {
			InputStream noterikIdStream = null;
			try {
				noterikIdStream = EuscreenPublish.class.getResourceAsStream("/noterikVideos.txt");
				LineIterator lines = IOUtils.lineIterator(noterikIdStream, "UTF8" );
				noterikKnownIds.clear();
				while( lines.hasNext()) {
					String line = lines.next();
					String[] cols = line.split("\\t");
					if( cols[0].trim().length() >0 ) {
						// extract the matching eus ids
						String eusId = cols[0].trim();
						String published = cols[4].trim();
						if( eusId.matches( "EUS_.{32}")) {
							noterikKnownIds.put( eusId, published );
						}
					}
				}
				
			} catch( Exception e) {
				log.error( "Error reading Noteriks known ids.", e );
			} finally {
				if( noterikIdStream != null ) IOUtils.closeQuietly( noterikIdStream );
			}
		}
		
		/**
		 * Read a file with some typical thesaurus entries that dont exist in the thesaurus,
		 * but we provide a literal to URL translation anyway.
		 */
		public static void readThesaurusExtension() {
			try {
				InputStream thesaurus = EuscreenPublish.class.getResourceAsStream("/ExtendThesaurus.json");
				JSONObject thes = (JSONObject) JSONValue.parse(thesaurus);
				for( Entry<String,Object> entry: thes.entrySet() ) {
					String url = (String) entry.getValue();
					String literal = entry.getKey();
					thesaurusLiteralToUrl.put( literal, url );
				}
			} catch( Exception e ) {
				log.error( "Couldnt find the thesaurus Extension.", e );
			}
		}
		
		// some statics to deal with thesaurus
		public static void readThesaurus() {
			// read file from disc
			// tmp hash literal to tuple list
			HashMap<String, List<Pair<String, String>>> thesTmp = new HashMap<String, List<Pair<String,String>>>();
			try {
				InputStream thesaurus = EuscreenPublish.class.getResourceAsStream("/ThesaurusInJson.json");
				JSONObject thes = (JSONObject) JSONValue.parse(thesaurus);
				// build list of pairs
				for( Entry<String,Object> conceptObj: thes.entrySet() ) {
					String url = conceptObj.getKey();
					JSONObject thesEntry = (JSONObject) conceptObj.getValue();
					for( Entry<String,Object> langLiteral: thesEntry.entrySet() ) {
						String lang = langLiteral.getKey();
						String literal = langLiteral.getValue().toString();
					
						Pair<String, String> p = Pair.of( lang, url);
						List<Pair<String,String>> litList = thesTmp.get( literal );
						if( litList == null ) {
							litList = new ArrayList<Pair<String,String>>();
							thesTmp.put( literal, litList);
						}
						litList.add( p );
					}
				}
				// create literal lookup
				thesaurusLiteralToUrl.clear();
				thesaurusAmbiguousToUrl.clear();
				thesaurusBadLiteral.clear();
				
				for(Entry<String, List<Pair<String, String>>> literal: thesTmp.entrySet()) {
					if( literal.getValue().size() == 1 ) {
						// easy
						thesaurusLiteralToUrl.put( literal.getKey(), literal.getValue().get(0).getRight() );
					} else {
						// only one URL ?
						List<Pair<String, String>> urls = literal.getValue();
						String firstUrl = urls.get(0).getRight();
						boolean differentUrls = false;
						for( Pair<String,String> urlPair: urls ) {
							if( !urlPair.getRight().equals( firstUrl)) differentUrls = true;
						}
						if( !differentUrls ) {
							// all urls the same, easy again
							thesaurusLiteralToUrl.put( literal.getKey(), firstUrl );
						} else {
							// harder, use the english literal
							String enUrl = null;
							for( Pair<String,String> urlPair: urls ) {
								if( urlPair.getLeft().equals( "en" )) {
									if( enUrl == null ) {
										enUrl = urlPair.getRight();
										thesaurusAmbiguousToUrl.put( literal.getKey(), enUrl );
									} else {
										// multiple equal english literals
										log.warn( "Thesaurus ambiguous on literal '" + literal.getKey() + "'\n" );
									}
								}
							}
							if( enUrl == null ) {
								log.warn( literal.getKey() + " is ambiguous and not english.");
								log.debug( literal.getValue());
								thesaurusBadLiteral.add( literal.getKey() );
							}
						}
					}
					
				}
			} catch( Exception e ) {
				log.error( "Thesaurus not read.", e );
			}
			log.info( "Read Thesaurus with " + thesTmp.size() + " literals. " + thesaurusLiteralToUrl.size() + 
					" easy to map. " + thesaurusAmbiguousToUrl.size() + " map ok with english.");
		}
		
		/**
		 * Insert into the item the thestermCode attributes for given thesaurus literal terms.
		 * The item Document and XML is modified.
		 * @param item
		 */
		public static void amendThesaurus( Item item ) {
			// get doc
			// find all thesaurus literals
			Nodes terms = item.getDocument().query( "//*[local-name()='ThesaurusTerm']");
			for( int i=0; i<terms.size(); i++ ) {
				Element thes = (Element) terms.get(i);
				String literal = thes.getValue().trim();
				if( literal.length() == 0 ) {
					Attribute att = thes.getAttribute("thestermCode");
					if(( att == null) || (att.getValue() == null) || (att.getValue().length() == 0 ))
						log.debug( "Empty literal in Item #" + item.getDbID());
					continue;
				}
				String url = thesaurusLiteralToUrl.get( literal );
				if( url == null ) {
					url = thesaurusAmbiguousToUrl.get( literal );
					if( url == null ) {
						log.warn( "Thesaurus entry '" + literal + "' not found. Item #" + item.getDbID());
						if( thesaurusBadLiteral.contains(literal)) {
							log.warn( "Literal is ambiguous in thesaurus!" );							
						}
					}
				}
				Attribute att = thes.getAttribute("thestermCode");
				if( url != null ) {
					log.debug("Correct Thesaurus entry!");
					if( att != null ) {
						String currentUrl = att.getValue();
						if( !currentUrl.equals( url )) {
							log.info( "Conflicting URL entry for Thesaurus entry '" + literal + "' in item #" + item.getDbID());
							if( forceThesaurusOverwrite) {
								att.setValue(url);
							}
						}
					} else {
						Attribute thestermCode = new Attribute( "thestermCode", url);
						thes.addAttribute(thestermCode);
					}
				} else {
					log.debug("Unmatched Thesaurus entry!");
					// URL == null
					if( att != null ) {
						// we better remove this, its probably crap
						log.info( "Removed funny URL '" + att.getValue() + "'");
						thes.removeAttribute(att);
					}
				}
			}
			item.setXml( item.getDocument().toXML());
		}
		
		public void run() {
			try {
				DB.getSession().beginTransaction();

				ds = DB.getDatasetDAO().getById(ds.getDbID(), false);
				pr = DB.getPublicationRecordDAO().getById(pr.getDbID(), false);
				ds.logEvent((publish?"Publish":"Unpublish") + " started" );

				if( publish ) {
					// iterate over items, 
					// if they don't have eus id, make one
					// if they do, compare it and see if its still ok
					// make a note in log if there has been a change
					DB.commit();
					ApplyI<Item> modifyId = new ApplyI<Item>() {
						@Override
						public void apply(Item item) throws Exception {
							String id = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']");
							if( id != null ) {
								// create the id.
								String provider = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='provider']");
								String originalId = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='originalIdentifier']");
								if( StringUtils.empty(originalId) || StringUtils.empty( provider)) {
									// bad, will be bad EUS id, need to ignore this and warn here
									// set item to invalid and not publish
									item.setValid(false);
									if( problemCounter > 0 ) {
										problemCounter--;
										ds.logEvent("Provider or originalId not set!", "In Item["+item.getDbID()+"] " + item.getLabel());
									}
									
								} else {
									String eusId = "EUS_"+StringUtils.md5Utf8(StringUtils.join( provider,":",originalId));
									if( !id.equals(eusId )) {
										// for the time being only adjust if the value is initial
										if( id.equals( EUSCREEN_DEFAULT_ID)) {
											// modify the xml
											item.setValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']", eusId );
										} else {
											log.info( "Item [" + item.getDbID() + "] tried change id from " + id + " to " + eusId + ". NOT DONE!"  );
										}
									}
									amendThesaurus(item);
									adjustProvider(item);
								}
								
								id = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']");
								item.setPersistentId( id );
							} else {
								// warn the id field is empty
								if( problemCounter > 0 ) {
									problemCounter--;
									ds.logEvent("ID field not preset during publish!", "In Item["+item.getDbID()+"] " + item.getLabel());
								}
							}
						}
					};

					ds.processAllValidItems( modifyId, true );
				}
				
				Solarizer si = new Solarizer( ds );
				si.modifier = new ApplyI<SolrInputDocument>() {
					public void apply(SolrInputDocument sid) throws Exception {
						if( publish ) {
							sid.addField("published_b", "true");
						} else {
							sid.removeField("published_b");
						}
					}
				};
				si.runInThread();
				
				// and now for the queueing 
				ApplyI<Item> publishOnQueue = new ApplyI<Item>() {
					public void apply(Item item) throws Exception {
						PublishQueue.queueItem(item, !publish);
					}
				};
		
				ds.processAllValidItems( publishOnQueue, false );
				
				ds.logEvent("Finished " + (publish?"publishing to":"unpublishing from") + " portal", ds.getValidItemCount()+" items queued for process at portal.");
				if( publish ) {
					pr.setEndDate(new Date());
					pr.setPublishedItemCount(ds.getValidItemCount());
					pr.setStatus(Dataset.PUBLICATION_OK);
					DB.commit();
				} else {
					DB.getPublicationRecordDAO().makeTransient(pr);
					DB.commit();
				}
			} catch( Exception e ) {
				ds.logEvent("Error while publishing/unpublishing", e.getMessage());
				if( pr != null ) {
					pr.setEndDate(new Date());
					pr.setStatus(Dataset.PUBLICATION_FAILED);
				}
				DB.commit();
			} finally {
				DB.closeSession();
				DB.closeStatelessSession();
			}
		}
	}
	
	// which id should be scheduled for publish
	public static final Logger log = Logger.getLogger( EuscreenPublish.class );
	
	String datasetId;
	String cmd;
	
	String title;
	String message;
	
	private Dataset ds;
	
	@Action( value="EuscreenPublish" )
	public String execute() {
		log.debug( "cmd="+cmd+" datasetId="+datasetId);
		try {
			ds = DB.getDatasetDAO().getById(Long.parseLong(datasetId), false);
			// TODO: rights check
		} catch( Exception e ) {
			log.error( e );
		}
		if( "europeanaPublish".equals( cmd ))
			return europeanaPublish();
		else if( "europeanaUnpublish".equals( cmd )) 
			return europeanaUnpublish();
		else if( "portalPublish".equals( cmd ))
			return portalPublish();
		else if( "portalUnpublish".equals( cmd )) 
			return portalUnpublish();
		else {
			log.error( "cmd="+cmd+" not supported.");
			title = "Error";
			message = "Not implemented yet";
			
		}
		return SUCCESS;
	}

	private String portalUnpublish() {
		// is it successfully published on portal?
		// send of runnable that reindexes all published items from dataset
		// field published removed
		Dataset origin = ds.getOrigin();
		Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));
		Dataset published = null;
		for( String schemaName: schemas ) {
			published = ds.getBySchemaName(schemaName);
			if( published != null ) break;
		}
		
		if(( origin == null) || ( published == null )) {
			title = "Error";
			message = "Can't find Dataset to publish! Program Bug!";
			return ERROR;
		}

		PublicationRecord pr = published.getPublicationRecord();
		
		if( pr == null ) {
			title = "Error";
			message = "Is not published on portal. Possible program bug!";
			return ERROR;
		}
		if(( pr != null ) && Dataset.PUBLICATION_RUNNING.equals( pr.getStatus())) {
			title = "Busy";
			message = "There is a publishing/unpublishing still running!";
			return SUCCESS;		
		}

		pr.setStatus(Dataset.PUBLICATION_RUNNING);
		DB.commit();
		PortalPublish pp = new PortalPublish( published, false, pr );
		Queues.queue(pp, "db" );
		message= "The removal from the portal was queued!";
		title="Portal Removal";
		return SUCCESS;
	}

	private String portalPublish() {
		Dataset origin = ds.getOrigin();
		
		Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));
		Dataset published = null;
		for( String schemaName: schemas ) {
			published = ds.getBySchemaName(schemaName);
			if( published != null ) break;
		}
		
		if(( origin == null) || ( published == null )) {
			title = "Error";
			message = "Can\\'t find Dataset to publish! Program Bug!";
			return ERROR;
		}
		PublicationRecord pr = published.getPublicationRecord();
		
		// is it not allready publishing?
		if(( pr != null ) && Dataset.PUBLICATION_RUNNING.equals( pr.getStatus())) {
			title = "Error";
			message = "There is a publishing/unpublishing still running!";
			return ERROR;
		}
		if(( pr != null ) && Dataset.PUBLICATION_OK.equals( pr.getStatus())) { 
			title = "Error";
			message = "Shouldn't get here, is already published!";
			return ERROR;
		}
		if( pr != null ) {
			DB.getPublicationRecordDAO().makeTransient(pr);
		}
		
		// check correct schema only on UI is not enough
		// get origin and actual published dataset
		if(( origin == null) || ( published == null )) {
			title = "Error";
			message = "Can't find Dataset to publish! Program Bug!";
			return ERROR;
		}
				
		// runnable that reindexes all valid items from dataset with field published=yes set
		pr = new PublicationRecord();

		pr.setStartDate(new Date());
		pr.setOrganization(ds.getOrganization());
		pr.setPublisher(getUser());
		pr.setStatus(Dataset.PUBLICATION_RUNNING);
		pr.setPublishedDataset(published);
		pr.setOriginalDataset(origin);
		DB.getPublicationRecordDAO().makePersistent(pr);
		DB.commit();
		PortalPublish pp = new PortalPublish( published, true, pr );
		Queues.queue(pp, "db" );
		message= "Your publication to the portal successfully queued!";
		title="Portal Publication";
		return SUCCESS;
	}

	private String europeanaUnpublish() {
		Dataset origin = ds.getOrigin();
		Dataset published = ds.getBySchemaName(Config.get("euscreen.aggregate.schema"));

		PublicationRecord pr = published.getPublicationRecord();
		
		// is it not allready publishing?
		if(( pr != null ) && Dataset.PUBLICATION_RUNNING.equals( pr.getStatus())) {
			title="Wait please.";
			message = "There is already a publication running!";
			return "success";
		}
		
		if( pr != null ) {
			DB.getPublicationRecordDAO().makeTransient(pr);
		} else {
			title = "Error";
			message = "Dataset is not published on Europeana!";
			return ERROR;
		}
		
		BasePublication bp = new BasePublication(ds.getOrganization());
		bp.unpublish(ds);
		// TODO Auto-generated method stub
		title = "Success";
		message = "Your dataset was unpublished.";
		return SUCCESS;
	}

	private String europeanaPublish() {
		Dataset origin = ds.getOrigin();
		Dataset published = ds.getBySchemaName(Config.get("euscreen.aggregate.schema"));

		PublicationRecord pr = published.getPublicationRecord();
		
		// is it not allready publishing?
		if(( pr != null ) && Dataset.PUBLICATION_RUNNING.equals( pr.getStatus())) {
			title="Wait please.";
			message = "There is already a publication running!";
			return "success";
			
		}
		if(( pr != null ) && Dataset.PUBLICATION_OK.equals( pr.getStatus())) {
			title = "Already published!";
			message = "Please unpublish before you republish.";
			return SUCCESS;
		}
		if( pr != null ) {
			DB.getPublicationRecordDAO().makeTransient(pr);
		}
		
		// check correct schema only on UI is not enough
		if(( published.getSchema() == null) || !published.getSchema().getName().equals( Config.get( "euscreen.aggregate.schema" ))
				|| published.getValidItemCount()<=0 ) {
			// cant publish this
			title = "Error";
			message = "Shouldn't have gotten here, UI should filter this";
			log.warn( "Europeana publish called without precondition being true.");
			return ERROR;
		}
		
		
		
		pr = new PublicationRecord();

		pr.setStartDate(new Date());
		pr.setOrganization(ds.getOrganization());
		pr.setPublisher(getUser());
		pr.setStatus(Dataset.PUBLICATION_RUNNING);
		pr.setPublishedDataset(published);
		pr.setOriginalDataset(origin);
		DB.getPublicationRecordDAO().makePersistent(pr);
		DB.commit();
		
		// Here we use the BasePublication publish strategy
		BasePublication bp = new BasePublication(ds.getOrganization());
		bp.publish(origin, getUser());
		
		message= "Your publication to Europeana successfully queued!";
		title="Europeana Publication";
		return SUCCESS;
	}

	public String getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

