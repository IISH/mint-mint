package gr.ntua.ivml.mint;

import gr.ntua.ivml.mint.actions.PublishPrepare;
import gr.ntua.ivml.mint.concurrent.Ticker;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.Config;
import gr.ntua.ivml.mint.util.CSVParser;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.XPathContext;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;


/**
 * What is done in Carare project during a publication.
 * @author Arne Stabenau 
 *
 */
public class PublishCarareDsi implements Runnable{
	private static final String DUMMY_ID = "carare:000000";
	
	public static final Logger log = Logger.getLogger( PublishCarareDsi.class );
	
	private String pubname;
	long currentItemNo = 0l;
	
	Writer pkgInfo;
	Dataset dataset;
	PublicationRecord pr;
	String schemaName;
	
	private static class ItemId {
		public String name;
		public String id;
		public String filename;
				
		// propably we need to add the direcrory here as attribute 'dir'
		public String toXml() {
			StringBuilder sb = new StringBuilder();
			sb.append("<item id=\""+StringEscapeUtils.escapeXml( id ) +"\"");
			if( name != null )
				sb.append(" name=\""+StringEscapeUtils.escapeXml( name ) +"\"");
			if( filename != null )
				sb.append(" filename=\""+StringEscapeUtils.escapeXml( filename ) +"\"");
			sb.append( "/>");
			return sb.toString();
		}
	}
	
	public String[] extractValues( Document doc, String path ) {
		Nodes noderes = doc.query( path );
		int size = noderes.size();
		String[] res = new String[size];
		for( int i=0; i<size; i++ ) res[i] = noderes.get(i).getValue();
		return res;
	}
	
	public String[] extractValues( Document doc, String path, XPathContext namespaces ) {
		Nodes noderes = doc.query( path, namespaces );
		int size = noderes.size();
		String[] res = new String[size];
		for( int i=0; i<size; i++ ) res[i] = noderes.get(i).getValue();
		return res;
	}
	
	public static boolean hasPath( Document doc, String path, XPathContext namespaces  ) {
		Nodes nodes = doc.query(path, namespaces );
		return ( nodes.size() != 0 );
	}
	
	public interface ItemResolve {
		// throw when you think this item cannot be processed
		public ItemId resolve( Item item, Document doc ) throws Exception;
	}
	
	// get through all of those and when it applies return the info needed (and or modify the item)
	public ItemResolve[] resolvers = { new ItemResolve() {
			// carare
			public ItemId resolve(Item item, Document doc) throws Exception {
				ItemId res = new ItemId();
				XPathContext namespaces = new XPathContext( "car", "http://www.carare.eu/carareSchema" );
				
				if( !hasPath( doc, "/car:carareWrap/car:carare",namespaces )) return null;
				res.id = generateCarareId(doc);
				if( res.id == null ) return null;
				// now the carare id in the doc should be set as well
				
				String[] labels = extractValues(doc, "//car:heritageAssetIdentification/car:appellation/car:name", namespaces );
				if( labels.length > 0 ) res.name = labels[0];
				else {
					labels = extractValues( doc, "//car:digitalResource/car:appellation/car:name", namespaces );
					if( labels.length > 0 ) res.name = labels[0];
				}

				// fix the item ...
				item.setPersistentId(res.id);
				if( res.name != null) item.setLabel(res.name );
				item.setXml(doc.toXML());
				return res;
			}
		}, new ItemResolve() {
			// EDM
			public ItemId resolve(Item item, Document doc) throws Exception {
				ItemId res = new ItemId();
				XPathContext namespaces = new XPathContext( "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
				namespaces.addNamespace( "edm", "http://www.europeana.eu/schemas/edm/");
				String[] ids = extractValues( doc, "rdf:RDF/edm:ProvidedCHO/@rdf:about", namespaces );
				if( ids.length == 0 ) return null;
				res.id = ids[0];
				
				return res;
			}
		}, new ItemResolve() {
			// lido
			public ItemId resolve(Item item, Document doc) throws Exception {
				ItemId res = new ItemId();
				XPathContext namespaces = new XPathContext( "lido", "http://www.lido-schema.org");
				String[] ids = extractValues( doc, "lido:lidoWrap/lido:lido/lido:administrativeMetadata/lido:recordWrap/lido:recordID", namespaces );
				if( ids.length == 0 ) return null;
				res.id = ids[0];
				
				return res;
			}
			
		}
	};
	
	public PublishCarareDsi( Dataset ds ) {
		this.dataset = ds;
		pr = ds.getPublicationRecord();
		schemaName = ds.getSchema().getName();
		pubname = "Publication_"+ pr.getDbID();
	}
	
	public void run() {
		try {
			DB.getSession().beginTransaction();
			DB.getStatelessSession().beginTransaction();
			// refresh ds for this session
			dataset = DB.getDatasetDAO().getById(dataset.getDbID(), false);
			pr = dataset.getPublicationRecord();
			runInThread();
		} finally {
			DB.commit();
			DB.closeSession();
			DB.closeStatelessSession();
		}		
	}
	
	/**
	 * Iterate over involved uploads and
	 *  - collect all items in a tgz together xsl and output.
	 */		
	
	public void runInThread() {
		final Ticker t = new Ticker(30);
		
		// only one dataset will get published
		// pkgInfo will still be collected
		// prepare tar archive 
		
		// carare schema pub is slightly different from other schemas, needs to generate ids?
		// do other schemas need id generation??
		
		String tmpFileLocation  = null;
		try {
			// tar and item file creation
			File tmpFile = File.createTempFile("cararedsi_" + pubname+"_", ".tgz" );
			tmpFileLocation = tmpFile.getAbsolutePath();
			File pkgInfoFile = File.createTempFile("itemNames", ".txt");
			pkgInfo = new OutputStreamWriter( new FileOutputStream( pkgInfoFile ), "UTF8");

			log.debug( "Publication tmpfile is :" + tmpFile.getAbsolutePath());
			final TarArchiveOutputStream tos = 
				new TarArchiveOutputStream( new GzipCompressorOutputStream(
						new BufferedOutputStream( new FileOutputStream(tmpFile))));


			TarArchiveEntry tae = new TarArchiveEntry( pubname + "/" );
			tos.putArchiveEntry(tae);
			tos.closeArchiveEntry();

			
			//get publication records and published sets
			ApplyI<Item> itemPublish = new ApplyI<Item>() {
				public void apply(Item publishedItem) throws Exception {
					Document doc = publishedItem.getDocument();
					ItemId itemId=null;
					// go through the resolvers
					for( ItemResolve resolve: resolvers ) {
						itemId = resolve.resolve(publishedItem, doc);
						if( itemId != null ) break;
					}
					
					if( itemId == null ) {
						// not processable
						return;
					} else {
						itemId.name = publishedItem.getLabel();
						itemId.filename = pubname+"/item_"+currentItemNo+"/"+schemaName+".xml";
					}
					
					// maybe itemResolve has already modified item, maybe not?
					addItem( dataset.getCreator(), currentItemNo, doc, itemId, tos);

					if( t.isSet()) {
						t.reset();
						//	setStatusMessage( "Processed " + currentItemNo + "/" + totalItemCount );
						log.debug( pubname + " Items:" + currentItemNo + "/" + dataset.getValidItemCount() );
						DB.commit();
					}
					currentItemNo+=1;
				}
			};

			dataset.processAllValidItems(itemPublish, true );
			dataset.logEvent("Publication file created", 
					"Published " + currentItemNo + " out of " + dataset.getValidItemCount() + " valid Items.");
			DB.commit();

			writePackageInfo(pkgInfoFile, tos);
			tos.close();
			log.info( pubname + " published " + currentItemNo + " Items");
			DB.commit();

			// note the publication file with the download action
			PublishPrepare.addtoPublicationFilesMap(pubname, tmpFileLocation);

			// announce it to MORE service
			String publishUrl = Config.get( "publishURL") + "?package=" +
					URLEncoder.encode(downloadUrl(), "UTF-8");
			log.debug( "Publish URL: " + publishUrl );
			InputStream is=null;
			try {
				URLConnection uc = new URL( publishUrl).openConnection();
				uc.setConnectTimeout(10000);
				uc.connect();
				is = uc.getInputStream();
				log.info( "Download from " + downloadUrl() );
				// now we sit and wait until their server has taken the file
				// and then get the answer ..
				String response = IOUtils.toString(is, "UTF-8");
				Thread.sleep( 10000l );
				// We could put the answer on the report ...
			} catch( Exception e2) {
				log.error( "Problem while sending to MORE", e2 );
				throw e2;
			}	finally {
				if( is != null ) is.close();
				if( pkgInfoFile != null ) pkgInfoFile.delete();
			}
			pr.setStatus(PublicationRecord.PUBLICATION_OK);
			pr.setPublishedItemCount(dataset.getValidItemCount());
			dataset.logEvent( "Finished publishing to MORE","");
		} catch( Exception e ) {
			log.error( pubname + " CurrentItemNo:" + currentItemNo + "\nError: " , e );
			pr.setStatus(PublicationRecord.PUBLICATION_FAILED);
			dataset.logEvent( "There was a problem during publication to MORE!");
		} finally {
			t.cancel();
			pr.setEndDate( new Date());
		}
	}

	/**
	 * If the carareId is not present, generate it with the following logic:
	 *  - If there is a heritage asset, use that Id prefixed with "HA"
	 *  - If there is a single digital resource, use that id prefixed with "DR"
	 * return false if you don't want to publish this item, due to problems.
	 * 
	 * @param wrappedNode
	 */
	private String generateCarareId(Document doc) {
		XPathContext namespaces = new XPathContext( "car", "http://www.carare.eu/carareSchema" );

		Nodes carareNodes = doc.query( "/car:carareWrap/car:carare",namespaces );
		Node carare = null;
		Attribute carareId = null;
		
		if( carareNodes.size()==1) {
			carare = carareNodes.get(0);
			carareId = ((Element) carare).getAttribute("id");
			if( carareId != null) {
				// there is an id,
				if( ! DUMMY_ID.equals( carareId.getValue())) return carareId.getValue();
				// else there is a dummy, so we need to find the real thing
			} else {
				log.debug( "Missing carae@id attribute");
				// there should be a dummy id attribute
				return null;
			}
		} else {
			log.debug( "No root node in carare file on publication? ");
			// failure, no root node for carare Item
			return null;
		}
		
		// do we have heritage asset id? 
		Nodes heritageAssetIds = doc.query( "//car:heritageAssetIdentification/car:recordInformation/car:id", namespaces );
		if( heritageAssetIds.size() > 0 ) {
			if( heritageAssetIds.size() == 1 ) {
				// bingo
				String id = heritageAssetIds.get(0).getValue();
				id = "HA"+id;
				// log.debug( "Created CarareId " + id );
				carareId.setValue(id);
				return id;
			} else {
				// too many heritageAsset Ids to handle
				log.debug( "Multiple HertageAssets in Carare record.");
				//appendReport("Multiple HeritageAssets in carare record.\n", 20000 );
				return null;
			}
		} else {
			// need to check the digitalResources
			Nodes digitalResourceIds = doc.query( "//car:digitalResource/car:recordInformation/car:id", namespaces );
			if( digitalResourceIds.size() == 1 ) {
				// bingo
				String id = digitalResourceIds.get(0).getValue();
				id = "DR" + id;
				carareId.setValue(id);
				return id;
			} else {
				// need exactly one digital resource
				if( digitalResourceIds.size() > 1 ) {
					log.debug( "Multiple DigitalResources and no HeritageAsset." );
					//appendReport("Multiple DigitalResources and no HeritageAsset.\n", 20000 );
				} else {
					log.debug( "No DigitalResources and no HeritageAsset.");
					//appendReport("No DigitalResources and no HeritageAsset.\n", 20000 );					
				}
				log.debug( "Not single DigitalResource in record.");
				return null;
			}
		}
	}

	
	/**
	 * Copy the info file into the result tar archive. Item data is taken from pkgData file.
	 * @param pkgData
	 * @param output
	 * @throws Exception
	 */
	private void writePackageInfo( File pkgData, TarArchiveOutputStream output ) throws Exception {
		File tmpFile = File.createTempFile("pkginfo", ".xml");
		java.io.FileOutputStream fos = new java.io.FileOutputStream( tmpFile );
		
		OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF8" );
		pkgInfo.flush();
		writer.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );
		writer.write( "<package timestamp=\"" );
		writer.write( new Date().toString() );
		writer.write( "\" size=\"" + currentItemNo );
		writer.write( "\" schema_id=\"" + 	resolveSchemaName(schemaName)	+ "\">");
		writer.write( "<project name=\"carare\" />" );
		writer.write( "<provider id=\"" + dataset.getOrganization().getDbID());
		//writer.write( "<provider id=\"" + parseProviderCsv(dataset.getOrganization()));
		writer.write( "\" name=\"" + dataset.getOrganization().getOriginalName() +"\" />");
		//writer.write( "<user id=\"" + parseUserCsv(dataset.getCreator().getLogin()) "\"");
		//writer.write( " name=\"" + StringEscapeUtils.escapeXml( dataset.getCreator().getName()) +"\" />");		
		writer.write( "  <items>\n" );
		writer.flush();
		// copy the pkgdata into the tgz
		FileInputStream fis =  new FileInputStream( pkgData);
		IOUtils.copy(fis, fos );
		fis.close();
		writer.write("  </items>\n" );
		writer.write( "</package>");
		writer.flush();
		writer.close();
		fos.close();
		
		TarArchiveEntry tae = new TarArchiveEntry(tmpFile, pubname+"/info.xml");
		output.putArchiveEntry(tae);
		fis = new FileInputStream(tmpFile);
		IOUtils.copy( fis , output);
		fis.close();
		tmpFile.delete();
		output.closeArchiveEntry();
	}

	private String parseProviderCsv( Organization org ) throws Exception {

		CSVParser parser = new CSVParser(',', '\"');
		BufferedReader br = new BufferedReader(new InputStreamReader
				(this.getClass().getResourceAsStream("providers.csv")));
		String[] header = null;
		boolean hasHeader = true;
		if( hasHeader ) {
			header = readNext( parser, br );
			if(( header == null ) || ( header.length == 0 )) throw new Exception( "No header found" );
		}

		String[] tokens = readNext( parser, br );
		Map<String, String> orgs = new HashMap<String, String>();
		while( tokens != null ) {
			orgs.put(tokens[2], tokens[0]);
			tokens = readNext( parser, br );
		}
		if(orgs.containsKey(org.getShortName()))
			return orgs.get(org.getShortName());
		else
			return "db_" + String.valueOf(org.getDbID());
	}
	
	private String parseUserCsv( String user ) throws Exception {

		CSVParser parser = new CSVParser(',', '\"');
		BufferedReader br = new BufferedReader(new InputStreamReader
				(this.getClass().getResourceAsStream("users.csv")));
		String[] header = null;
		boolean hasHeader = true;
		if( hasHeader ) {
			header = readNext( parser, br );
			if(( header == null ) || ( header.length == 0 )) throw new Exception( "No header found" );
		}

		String[] tokens = readNext( parser, br );
		Map<String, String> users = new HashMap<String, String>();
		while( tokens != null ) {
			users.put(tokens[2], tokens[0]);
			tokens = readNext( parser, br );
		}
		if(users.containsKey(user))
			return users.get(user);
		else
			return null;
	}

    private String[] readNext( CSVParser parser, BufferedReader reader ) throws Exception {
    	String[] result = null;
    	do {
    		
    		String nextLine;
    		// skip empty lines if they are there
    		do {
    			nextLine = reader.readLine();
    			if( nextLine == null ) break;
    			if( parser.isPending() ) break;    			
    		} while( nextLine.trim().length() == 0 );
    		
    		if( nextLine == null ) {
    			if( parser.isPending()) throw new Exception( "Quotes not matching, missing input!");
    			else return null;
    		}
    		// skip empty lines if we are not pending
    		
    		String[] r = parser.parseLineMulti(nextLine);
    		if (r.length > 0) {
    			if (result == null) {
    				result = r;
    			} else {
    				String[] t = new String[result.length+r.length];
    				System.arraycopy(result, 0, t, 0, result.length);
    				System.arraycopy(r, 0, t, result.length, r.length);
    				result = t;
    			}
    		}
    	} while (parser.isPending());
    	return result;
    }

	
	private String resolveSchemaName(String schemaName) {
		if (schemaName.toLowerCase().contains("carare"))
			return "CARARE2";
		if (schemaName.toLowerCase().contains("lido"))
			return "LIDO";
		if (schemaName.toLowerCase().contains("edm"))
			return "EDM";
		else
			return schemaName;
	}

	//No need for xmlnode sourceitemtree 
	//carate document doc will be 
	
	// add dir and files to the output zip
	private void addItem(User uploader, long itemNo, Document doc,
			ItemId id, TarArchiveOutputStream output ) throws Exception {
		
		String path = pubname + "/" + "item_" + itemNo + "/";
		
		// create item_"id" entry	
		output.putArchiveEntry( new TarArchiveEntry( path ));
		output.closeArchiveEntry();

		// add output xml
		addStringToTar( doc.toXML(), id.filename, output );
		
		// add to info file
		pkgInfo.write( "    " + id.toXml() + "\n" );
	}	
	
	private void addStringToTar( String data, String path, TarArchiveOutputStream tos ) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(bos, "UTF8" );

		writer.write( data );
		writer.flush();
		TarArchiveEntry tae = new TarArchiveEntry( path );
		tae.setSize( bos.size() );
		tos.putArchiveEntry( tae );
		bos.writeTo(tos);
		tos.flush();
		tos.closeArchiveEntry();
	}
	
	
	private String downloadUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append( Config.get( "publicationDownloadURL" ));
		sb.append( "?pub=");
		sb.append( pubname );

		return sb.toString();
	}
	
	
}

