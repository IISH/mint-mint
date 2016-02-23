package gr.ntua.ivml.mint.util;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.FlushMode;

import com.opensymphony.xwork2.util.TextParseUtil;

import gr.ntua.ivml.mint.BasePublication;
import gr.ntua.ivml.mint.concurrent.Queues;
import gr.ntua.ivml.mint.concurrent.XSLTransform;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.AnnotatedDataset;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.Mapping;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.persistent.Transformation;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.persistent.XpathHolder;
import nu.xom.Attribute;
import nu.xom.Element;

public class CoreToEuropeana {

	static Logger log = Logger.getLogger( CoreToEuropeana.class );

	static String coreSchema = "EUscreenXL ITEM/CLIP v2";
		
	static String[] normOrgInit = new String[] { "DW", "Deutsche Welle",
				"KB" ,"Kungliga biblioteket",
				"TVC", "TV3 Televisió de Catalunya (TVC)",
				"NISV", "Netherlands Institute for Sound and Vision",
				"NINA", "Narodowy Instytut Audiowizualny",
				"SASE", "Screen Archive South East",
				"ERT", "ERT SA",
				"HeNAA" ,"ERT SA"
	};

	static HashMap<String,String> normalizeOrg = new HashMap<String, String>();
	static {
		for( int i=0; i<normOrgInit.length; i+= 2 ) 
			normalizeOrg.put( normOrgInit[i], normOrgInit[i+1]);
	};

	static List<String> screenshotInsertPos = Arrays.asList( 
			"{http://www.europeana.eu/schemas/edm/}:aggregatedCHO" ,
			"{http://www.europeana.eu/schemas/edm/}:dataProvider",
			"{http://www.europeana.eu/schemas/edm/}:hasView",
			"{http://www.europeana.eu/schemas/edm/}:isShownAt",
			"{http://www.europeana.eu/schemas/edm/}:isShownBy",
			"{http://www.europeana.eu/schemas/edm/}:object",
			"{http://www.europeana.eu/schemas/edm/}:preview",
			"{http://www.europeana.eu/schemas/edm/}:provider",
			"{http://purl.org/dc/elements/1.1/}:rights" ,
			"{http://www.europeana.eu/schemas/edm/}:rights",
			"{http://www.europeana.eu/schemas/edm/}:ugc"
	);
	
	//   xmlns:edm="http://www.europeana.eu/schemas/edm/"
	// rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#" local-name="Resource" attribute to the edm:object


	static List<String> linkInsertPos = Arrays.asList(
				// basetype in ProvidedCHO is unordered
				"{http://purl.org/dc/terms/}:hasPart",
				"{http://purl.org/dc/terms/}:isPartOf",

				// extension is ordered but has to come after base type anyway
				"{http://www.europeana.eu/schemas/edm/}:currentLocation",
				"{http://www.europeana.eu/schemas/edm/}:hasMet" ,
				"{http://www.europeana.eu/schemas/edm/}:hasType" ,
				"{http://www.europeana.eu/schemas/edm/}:incorporates",
				"{http://www.europeana.eu/schemas/edm/}:isDerivativeOf",
				"{http://www.europeana.eu/schemas/edm/}:isNextInSequence" ,
				"{http://www.europeana.eu/schemas/edm/}:isRelatedTo",
				"{http://www.europeana.eu/schemas/edm/}:isRepresentationOf",
				"{http://www.europeana.eu/schemas/edm/}:isSimilarTo",
				"{http://www.europeana.eu/schemas/edm/}:isSuccessorOf",
				"{http://www.europeana.eu/schemas/edm/}:realizes",

				
				"{http://www.europeana.eu/schemas/edm/}:type"
				
			);

	public static class NoterikInfo {
		public String eusId;
		public boolean published;
		public String screenshot;
		public String provider;
		public String nativeId;
		public String type;
	}
	

	/**
	 * Go over all core records and transform to edm with the given mapping.
	 * Wait after this for all transformations to happen (can't be very long ...60k items)
	 */
	public static void transformNotTransformedCore( Mapping mapping ) {
		Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));
		String oaischema = Config.get( "oai.schema" );

		DB.getSession().setFlushMode(FlushMode.COMMIT);
		for( PublicationRecord pr: DB.getPublicationRecordDAO().findAll() ) {
			if( schemas.contains( pr.getPublishedDataset().getSchema().getName())) {
				// is it transformed
				Dataset ds = pr.getPublishedDataset();
				Dataset ds2 = ds.getBySchemaName( oaischema );
				if( ds2 == null ) {
					// ok, there is no edm version yet
					Transformation tr = Transformation.fromDataset( ds, mapping);
					DB.getTransformationDAO().makePersistent( tr );
					DB.commit();
					log.info( "Created Transformation " + tr.getDbID());
					XSLTransform trans = new XSLTransform( tr );
					Queues.queue( trans, "db");
					log.info( "Queued " + ds.getName()+ " with " + ds.getItemCount() + " items" );
				}
			}
		}
		DB.getSession().setFlushMode(FlushMode.AUTO);
		log.info( "All datasets queued in db for transform.");
	}
	
	public static void invalidateNotApprovedEdm( Dataset ds, final Map<String, NoterikInfo> noterik  ) {
		try {
			ds.processAllItems( new ApplyI<Item>() {

				@Override
				public void apply(Item i) throws Exception {
					Item source = i.getSourceItem();
					String id = source.getPersistentId();
					NoterikInfo ni = noterik.get(id);
					if( (ni == null ) || (!ni.published)) {
						if( i.isValid()) {
							i.setValid( false );
							log.debug(id + " is not in approved list.");
						}
					}
				}						
			}, true );
		} catch( Exception e ) {
			log.error( "Problem invalidating in ds #" + ds.getDbID());
		}
	}
	
	// wait until db queue is empty, check every 20 seconds
	// probably useless, since we dont want to keep the web response waiting for this
	public static boolean waitForDb( int seconds ) {
		boolean empty = false;
		int start = (int)(System.currentTimeMillis() / 1000l);
		int current = start;
		ThreadPoolExecutor dbe = (ThreadPoolExecutor) Queues.queues.get("db");
		empty = (dbe.getQueue().size() == 0 ) && 
				( dbe.getActiveCount() == 0 );
		do  {
			if( ! empty ) {
				try {
					Thread.sleep( 10000 );
				} catch( Exception e ) {}
			}
			empty = (dbe.getQueue().size() == 0 ) && 
					( dbe.getActiveCount() == 0 );
			current = (int)(System.currentTimeMillis() / 1000l);
		} while( !empty && (current<start+seconds));
		return empty;
	}
	
	
	
	// set missing item root path on annotated datasets, so they can be transformed
	public static void cleanAnnotated() {
		List<AnnotatedDataset> annotatedDatasets = DB.getAnnotatedDatasetDAO().findAll();
		for( Dataset ds: annotatedDatasets) {
			if( ds instanceof AnnotatedDataset ) {
				Dataset parent = ((AnnotatedDataset)ds).getParentDataset();
				XpathHolder xpath = ds.getItemRootXpath();
				if( xpath == null ) {
					if( parent != null ) {
						if( parent.getItemRootXpath() != null ) {
							XpathHolder alt = ds.getByPath( parent.getItemRootXpath().getQueryPath());
							if( alt != null ) {
								ds.setItemRootXpath( alt );
								log.info( "Fixed " + ds.getDbID() + " with itemRootPath");
							}
						}
					}
				}
			}
		}
	}
	
	public static void removeOldEdmPubRecords() {
		String oaischema = Config.get( "oai.schema" );
		for( PublicationRecord pr: DB.getPublicationRecordDAO().findAll()) {
			if( pr.getPublishedDataset().getSchema().getName().equals(oaischema)) {
				DB.getPublicationRecordDAO().makeTransient(pr);
			}
		}
		DB.commit();
	}

	
	// remove the edm transformed version of core records
	public static void cleanPublishCoreSets() {
		String oaischema = Config.get( "oai.schema" );
		Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));

		DB.getSession().setFlushMode(FlushMode.COMMIT);
		for( PublicationRecord pr: DB.getPublicationRecordDAO().findAll() ) {
			if( schemas.contains( pr.getPublishedDataset().getSchema().getName())) {
				Dataset publishedDataset = pr.getPublishedDataset();
				Dataset ds2 = publishedDataset.getBySchemaName(oaischema);
				if( ds2 != null ) {
					DB.getTransformationDAO().makeTransient((Transformation)ds2 );
					log.info( "Removed " + ds2.getDbID());
				}
			}
		}
		DB.commit();
		DB.getSession().setFlushMode(FlushMode.AUTO);
	}

	// build series info for all core records (old, new and series records)
	public static List<Map<String,?>> buildSeriesInfo () {
		final List allRecords = new ArrayList<Map<String,?>>();

		// collect series information
		Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));
		for( PublicationRecord pr: DB.getPublicationRecordDAO().findAll() ) {
			if( schemas.contains( pr.getPublishedDataset().getSchema().getName())) {
				try {
					pr.getPublishedDataset().processAllValidItems(new ApplyI<Item>() {
						public void apply( Item item ) {
							try {
								String seriesTitle= item.getValue( "//*[local-name()='TitleSetInEnglish']/*[local-name()='seriesOrCollectionTitle']");
								String provider = item.getValue("//*[local-name()='provider']" );
								String series = item.getValue( "//*[local-name()='recordType']");
								String eusId = item.getValue( "//*[local-name()='identifier']");
								boolean isSeries = "SERIES/COLLECTION".equals( series );
								Map<String, Object> info = new HashMap<String, Object>();
								if( normalizeOrg.containsKey( provider ))
									provider = normalizeOrg.get( provider );
								info.put( "itemId",  item.getDbID());
								info.put("provider",  provider);
								info.put("seriesTitle", seriesTitle);
								info.put( "id", eusId);
								// is it a clip or a series record
								info.put( "isSeries", isSeries );

								allRecords.add( info );
							}  catch( Exception e ) {
								log.error( "Exception during reading item #" + item.getDbID() );
							}
						}
					}
					, false);
				} catch( Exception e ) {
					log.error( "Not expecting exception in processing ds #" + pr.getPublishedDataset().getDbID());
				}
			}
		}
		return allRecords;
	}

	// fill the eusIdIndex, and seriesTitleIndex and allow only approved entries
	// you need to provide empty maps
	public static void  buildSeriesIndex( List<Map<String,?>> seriesInfo, Map<String, Map> eusIdIndex, Map<String, Map> seriesTitleIndex, Map<String, NoterikInfo> noterik ) {
		
		// filter out entries that are not approved
		Iterator<Map<String,?>> listIter = seriesInfo.iterator();
		while( listIter.hasNext() ) {
			Map<String, ?> info = listIter.next();
			String id = (String) info.get("id");
			NoterikInfo ni = noterik.get(id );
			if(( ni == null) || (!ni.published)) listIter.remove();
		}
		
		// build a small index into the records
		for( Map<String, ?> info: seriesInfo ) {
			eusIdIndex.put( (String) info.get( "id" ), info );
		}
		
		for( Map<String, ?> info: seriesInfo ) {
			if( (Boolean) info.get( "isSeries")) {
				seriesTitleIndex.put( (String) info.get( "seriesTitle"), info);
			}
		}

		
		// clean the index
		// all ids that have no series title or no series title with series record go out
		Iterator<Map.Entry<String, Map>> iter = eusIdIndex.entrySet().iterator();
		while( iter.hasNext() ) {
			Map.Entry<String, Map> entry = iter.next();
			Map<String, Object> info = entry.getValue();
			
			// do nothing with series records
			if( (Boolean) info.get( "isSeries")) continue;
			
			String seriesTitle = (String) info.get( "seriesTitle" );
			if( StringUtils.empty( seriesTitle ) ||
				!seriesTitleIndex.containsKey( seriesTitle )) {
				iter.remove();
				continue;
			}
			
			// series epsiodes are added to the series record
			Map<String, Object> seriesRecord = seriesTitleIndex.get( seriesTitle );
			// should not be empty but check anyway
			if( seriesRecord == null ) {
				log.error( "There really should be a series record for " + seriesTitle );
				continue;
			}
			List episodes = (List) seriesRecord.get("allEpisodes");
			if( episodes == null ) {
			    episodes = new ArrayList<String>(); 
				seriesRecord.put( "allEpisodes",  episodes );
			}
			episodes.add( entry.getKey());
		}
	}
	
	
	
	public static void installSeriesLinks( Dataset edmSet,  
			final Map<String, Map> eusIdIndex, final Map<String,Map> seriesTitleIndex ) throws Exception {
		edmSet.processAllItems(new ApplyI<Item>() {
			public void apply( Item item ) {
				try {
					String id = item.getValue( "//*[local-name()='Aggregation']/@*[local-name()='about']");
					String cho = item.getValue( "//*[local-name()='ProvidedCHO']/@*[local-name()='about'] " );
					String eusId = id.replaceFirst( ".*(EUS_.{32}).*", "$1");

					if( eusIdIndex.containsKey( eusId )) {
						Map info = eusIdIndex.get(  eusId );
						if( (Boolean) info.get( "isSeries")  ) {
							// series record entries
							List<String> episodes  = (List<String>) info.get( "allEpisodes" );
							for( String episodeId: episodes ) {
								Element newElem = item.insertElement( "hasPart","http://purl.org/dc/terms/", linkInsertPos, "//*[local-name()='ProvidedCHO']" );
								Attribute att = new Attribute( "rdf:resource", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", cho.replace(eusId, episodeId));
								newElem.addAttribute(att);											
							}
						} else {
							String seriesTitle = (String) info.get( "seriesTitle" );
							Map seriesRecord = (Map) seriesTitleIndex.get(  seriesTitle );
							String seriesId = (String) seriesRecord.get( "id" );

							Element newElem = item.insertElement( "isPartOf","http://purl.org/dc/terms/", linkInsertPos, "//*[local-name()='ProvidedCHO']" );
							Attribute att = new Attribute( "rdf:resource", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", cho.replace(eusId, seriesId));
							newElem.addAttribute(att);
						}
						item.setXml(item.getDocument().toXML());
					}

				} catch( Exception e ) {
					log.error( "Series link install problem", e );
				}
			}
		}, true );
	}

	public static Map<String, NoterikInfo> readNoterikInfoUrl( String url ) {
		try {
			InputStream is = new URL( url).openStream();
			return readNoterikInfo( is );
		} catch( Exception e ) {
			log.error( "Couldnt open url '" + url + "' as Stream.", e );
		}
		return Collections.emptyMap();
	}
	
	
	public static Map<String,NoterikInfo> readNoterikInfoResource( String name ) {
		InputStream is = CoreToEuropeana.class.getResourceAsStream( name );
		if( is != null )
			return readNoterikInfo( is );
		else
			log.error( "Couldnt read '" + name + "' as Stream." );
		return Collections.emptyMap();
	}
	
	
	/**
	 * Read given file and return a hashmap of euscreen id and screenshot url
	 * @param filename
	 * @return
	 */
	public static Map<String,NoterikInfo> readNoterikInfo( InputStream is ) {
		HashMap<String, NoterikInfo> result = new HashMap<String, NoterikInfo>();
		
		try {
			Iterator<String> it = IOUtils.lineIterator(is, "UTF8");
			while( it.hasNext() ) {
				String line = it.next();
				String[] elems = line.split("\\t");
				if( elems.length >= 5 ) { 
					NoterikInfo ni = new NoterikInfo();
					ni.eusId = elems[0];
					if( ni.eusId.matches( "EUS_.{32}" )) {
						ni.type = elems[1];
						ni.provider = elems[2];
						ni.screenshot = elems[3]; 
						ni.published = ("true".equals( elems[4]));
						result.put( ni.eusId, ni );
					}
				} else {
					log.info( "Funny line in Noterik scrape file '" + line + "'");
				}
			}
		} catch( Exception e ) {
			log.error( "Stream couldn't be read for screenshots!", e );
		} finally {
			IOUtils.closeQuietly(is);
		}
		return result;
	} 

	public static void installScreenShots( Dataset edmSet, final Map<String, NoterikInfo> screenShotUrls ) {
		try {
			edmSet.processAllItems(new ApplyI<Item>() {
				public void apply( Item item ) {
					try {
						// rdf:about
						String id = item.getValue( "//*[local-name()='Aggregation']/@*[local-name()='about']");
						String eusId = id.replaceFirst( ".*(EUS_.{32}).*", "$1");

						if( screenShotUrls.containsKey( eusId )) {
							NoterikInfo ni = screenShotUrls.get( eusId );
							Element edmObject = item.insertElement( "object", "http://www.europeana.eu/schemas/edm/", 
									screenshotInsertPos, "//*[local-name()='Aggregation']" );
							Attribute att = new Attribute( "rdf:resource", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", ni.screenshot );
							edmObject.addAttribute( att );
							item.setXml( item.getDocument().toXML());
						}
					} catch( Exception e ) {
						log.error( "Screen shot insert problem", e );
					}
				}
			}, true );
		} catch( Exception e ) {
			log.error( "Unexpected Exception setting screenshots on ds #" + edmSet.getDbID());
		}
	}
}
