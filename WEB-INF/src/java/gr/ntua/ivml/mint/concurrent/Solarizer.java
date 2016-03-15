package gr.ntua.ivml.mint.concurrent;

import java.io.StringReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;
import org.ocpsoft.prettytime.PrettyTime;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import gr.ntua.ivml.mint.Custom;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.persistent.XMLNode;
import gr.ntua.ivml.mint.persistent.XpathHolder;
import gr.ntua.ivml.mint.persistent.XpathStatsValues;
import gr.ntua.ivml.mint.persistent.XpathStatsValues.ValueStat;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.Config;
import gr.ntua.ivml.mint.util.StringUtils;
import gr.ntua.ivml.mint.xml.util.SchemaExtractorHandler;


/**
 * This class does all activities that involve the Solr index. Mostly it is used when datasets (or items)
 * are created, like after upload and after transformation.
 * 
 * If the index needs to be modified (like for euscreenxl) you can set a modfier on the solr. This will allow you
 * to modify the indexing activity per run. 
 * 
 * There are many hooks into this, that allow customization on a per project basis.
 * eg:
 *  - how to create fieldnames out of xpaths
 *  - modify the solrinputdocument just before indexing
 *  - testing a dataset, if its allowed for indexing
 *   
 * @author Arne Stabenau 
 *
 */
public class Solarizer implements Runnable {
	private Dataset ds;
	private Item item;
	private XMLReader parser;
	
	public ApplyI<SolrInputDocument> modifier = null;
	
	private static final Logger log = Logger.getLogger(Solarizer.class );
	public SolrInputDocument currentSid = null;
	public static SolrClient solrClient = null;

	private int itemCounter = 0;
	
	public Solarizer( Dataset ds ) {
		this.ds = ds;
	}

	public Solarizer( Item item) {
		this.item = item;
	}
	
	public static final void queuedIndex( Dataset ds ) {
		Solarizer s  = new Solarizer( ds );
		ds.logEvent("Queued for Full Text Index.");
		DB.commit();
		Queues.queue(s, "db" );
	}
	
	private void parserSetup() throws Exception {
		parser = org.xml.sax.helpers.XMLReaderFactory.createXMLReader(); 
		parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		XpathHolder root = ds.getRootHolder();
		if( root ==  null) {
			root = new XpathHolder();
			root.setName("");
			root.setParent( null );
			root.setXpath( "" );
			root.setDataset(ds);
			DB.getXpathHolderDAO().makePersistent(root);			
			ds.setRootHolder(root);
			DB.commit();
		}
		
		SchemaExtractorHandler statsHandler = new SchemaExtractorHandler(root);

		// repurpose the statsCollector
		statsHandler.setStatsCollector( new ApplyI<XMLNode>() {
			public void apply(XMLNode node) throws Exception {
				buildSid(node);
			}
		});
		
		parser.setContentHandler(statsHandler);
	}
	
	public void run() {
		try {
			DB.getSession().beginTransaction();
			// refresh ds for this session
			if( ds != null )
				ds = DB.getDatasetDAO().getById(ds.getDbID(), false);
			else if( item != null ) 
				item = DB.getItemDAO().getById(item.getDbID(), false);
			
			runInThread();
		} finally {
			DB.commit();
			DB.closeSession();
			DB.closeStatelessSession();
		}		
	}

	/**
	 * Do the actual work here, run wraps the transaction around it
	 * 
	 */
	public void runInThread() {
		try {
			final HashMap<String, String> extraItemData = new HashMap<String, String>();
			if( item != null ) {
				ds = item.getDataset();
			}
			parserSetup();


			// return if we dont have items
			if( (item==null) && !ds.getItemizerStatus().equals( Dataset.ITEMS_OK)) {
				if( ds != null ) {
					ds.logEvent( "Cant solrize, ITEMS not OK." );
					DB.commit();
				}
				return;
			}

			
			// extraData comes from Dataset
			extraItemData.put( "organization_id", Long.toString( ds.getOrganization().getDbID()));
			extraItemData.put( "dataset_id", ds.getDbID().toString());
			extraItemData.put( "user_id", ds.getCreator().getDbID().toString());
			
			if( ds.getSchema() != null ) { 
				extraItemData.put("schema_id", ds.getSchema().getDbID().toString());
				extraItemData.put("schema_name_s", ds.getSchema().getName());
			}

			PublicationRecord pr = ds.getPublicationRecord();
			if(( pr != null) && ( PublicationRecord.PUBLICATION_OK.equals( pr.getStatus())))
				extraItemData.put( "published_b", "true");

			if( item != null ) {
				// this shouldn't be necessary
				// getSolrServer().deleteByQuery("item_id:"+item.getDbID().toString())
				item.getDataset().logEvent("Solarize Item["+item.getDbID()+"]");
				DB.commit();
				solarizeStatelessItem( item, extraItemData );				
				Solarizer.commit();
			} else {
				ds.logEvent("Full text index started.");
				DB.commit();
				// delete what is there, just in case

				Solarizer.delete("dataset_id:"+ds.getDbID().toString());
				Solarizer.commit();

				final int totalInputItems = ds.getItemCount();
				final long startTime = System.currentTimeMillis();
				ApplyI<Item> itemProcessor = new ApplyI<Item>() {
					@Override
					public void apply(Item item) throws Exception {
						try {
						solarizeStatelessItem( item, extraItemData );
						} catch( Exception e ) {
							ds.logEvent( "Item #" + item.getDbID() + " didn't solarize.");
							throw e;
						}
						itemCounter++;
						if( totalInputItems > 2000 ) {
							if( itemCounter == (totalInputItems/50)) {
								long usedTime = System.currentTimeMillis()-startTime;
								PrettyTime pt = new PrettyTime();
								
								String expected = pt.format( new Date( System.currentTimeMillis() + 49*usedTime )); 
								ds.logEvent( "Expect solr index finished " + expected );
								DB.commit();
							}
						}
 					}
				};
				ds.processAllItems(itemProcessor, false );
				Solarizer.commit();
				ds.logEvent("Full text index finished.");
				DB.commit();
			}
		} catch( Exception e ) {
			if( item == null ) {
				log.error( "Solarizing of Dataset[" + ds.getDbID() + "] failed!", e );
				ds.logEvent( "Solarizing failed with " + e.getMessage(), StringUtils.stackTrace(e, null));
			} else
				log.error( "Solarizing of Item[" + item.getDbID() + "] failed!" ,e );
		}
	}

	/**
	 * Consider removing prefixes, "/" and "@" and/or replace them with "_"
	 *  
	 * @param path
	 * @return
	 */
	public static String smoothXpathForSolr( String path ) {
		return Custom.sanitizeSolrXpath(path);
	}
	
	public void buildSid( XMLNode node ) {
		String path = smoothXpathForSolr(node.getXpathWithPrefix());
		if(( node.getNodeType() == XMLNode.TEXT) || 
				( node.getNodeType() == XMLNode.ATTRIBUTE )) {
			String content = node.getContent();
			currentSid.addField( path+"_tg",content);
			// exact field without tokenize only for fields lower than 10k
			// this is arbitrary, could be much shorter
			if( content.length() < 10000 )
				currentSid.addField( path+"_s",content);
		}
	}
	
	/** 
	 * Reads the xml into the currentSid
	 * @param xml
	 */
	public void parseXmlIntoSid( String itemXml ) throws Exception {
		InputSource ins = new InputSource();
		if(!itemXml.startsWith("<?xml")) itemXml =  "<?xml version=\"1.0\"  standalone=\"yes\"?>" + itemXml;
		ins.setCharacterStream(new StringReader( itemXml ));
		currentSid = new SolrInputDocument();
		parser.parse( ins );
	}

	/**
	 * This function adds the extra data to the sid. Extra is Dataset related and not item related.
	 * @param item
	 * @throws Exception
	 */
	public void solarizeStatelessItem( Item item, Map<String, String> extra ) throws Exception {
		Thread.sleep(0);
		
		parseXmlIntoSid( item.getXml());
		for( Map.Entry<String,String> e: extra.entrySet()) {
			currentSid.addField(e.getKey(), e.getValue());
		}
		
		// here some magic fields
		currentSid.addField("id", item.getDbID());
		if( extra.containsKey("schema_name_s")) {
			currentSid.addField( "valid_b", item.isValid());
		}
		currentSid.addField("item_id", item.getDbID().toString());
		if( item.getLabel() != null ) {
			currentSid.addField( "label_tg", item.getLabel());
			currentSid.addField( "label_s", item.getLabel());
		}
		
		if( item.getPersistentId() != null ) {
			currentSid.addField( "native_id_tg", item.getPersistentId());
			currentSid.addField( "native_id_s", item.getPersistentId());
		}
		if( item.getSourceItem() != null ) {
			currentSid.addField( "source_item_id", item.getSourceItem().getDbID().toString());
		}
		
		currentSid.addField( "last_modified_tdt", item.getLastModified());
		
		// hook into putting extra fields into the index
		Custom.modifySolarizedItem(item, currentSid );
		if( modifier != null ) modifier.apply(currentSid );
		
		Solarizer.addDocument( currentSid );
	}		

	public static SolrClient getSolrClient() {
		if (solrClient == null) {
			if( Config.get("solr.url") != null ) {
				solrClient = new HttpSolrClient(Config.get("solr.url"));
			} else if( Config.get("solr.directory") != null ) {
				// setup embedded access
				// modify solr core config to point to the given data dir
				System.setProperty("solr.data.dir", Config.get("solr.directory" ));
				Path solrHome = Config.getProjectFile("WEB-INF/solr_home").toPath();
				solrClient = new EmbeddedSolrServer(solrHome, "mint2");
			}
		}
		return solrClient;
	}	
	
	/**
	 * For safety I think its better to synchronize on writing to the solr server
	 * @return
	 */
	
	public static synchronized void addDocument( SolrInputDocument sid) throws Exception {
		getSolrClient().add( sid );
	}
	
	public static synchronized void commit() throws Exception {
		getSolrClient().commit();
	}
	
	public static synchronized void delete(String query) throws Exception {
		getSolrClient().deleteByQuery( query );
	}
	
	/**
	 * Get content / count pairs for a facet field
	 * @param query 	main query for solr
	 * @param field 	facet field for count 
	 * @param offset	
	 * @param limit
	 * @return	list of content/count pairs 
	 */
	public static List<XpathStatsValues.ValueStat> solrFacetQuery(
			String query, String field, long offset, int limit) {
		SolrQuery q = new SolrQuery(query);
		q.setRows(0);
		q.add("facet.offset",Long.toString(offset));
		q.setFacetLimit(limit);
		q.setFacetMinCount(1);	//no zero counts
		q.addFacetField(field);
		try {
			FacetField f = getSolrClient().query(q).getFacetField(field);
			/* Create List with content/count pairs */
			ArrayList<ValueStat> values = new ArrayList<XpathStatsValues.ValueStat>();
			for (Count val : f.getValues()) {
				ValueStat vs = new ValueStat(val.getName(),
						(int) val.getCount());
				values.add(vs);
			}
			return values;

		} catch (Exception e) {
			/* In case the field dosen't exist 
			 * return empty array */
			log.error( "Solr error", e );
			return new ArrayList<ValueStat>();
		}	
	}
	
	/**
	 * Get content / count pairs from this holder. First value is 0.
	 * @param holder
	 * @param start
	 * @param maxCount
	 * @return	list of content/count pairs 
	 */
	public static List<XpathStatsValues.ValueStat> getValues(
			XpathHolder holder, long start, int maxCount) {

		/* Get dataset_id and xpath from XpathHolder */
		String xpath = smoothXpathForSolr(holder.getXpath());
		String dataset_id = holder.getDataset().getDbID().toString();
		List<ValueStat> vals = solrFacetQuery("dataset_id:"+dataset_id, xpath+"_s", start, maxCount);
		return vals;
	}
	
	public static boolean isEnabled() {
		return ((Config.get("solr.url") != null ) ||
				(Config.get("solr.directory") != null ));
	}
}
