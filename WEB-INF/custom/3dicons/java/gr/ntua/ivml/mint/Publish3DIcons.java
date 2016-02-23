package gr.ntua.ivml.mint;

import gr.ntua.ivml.mint.actions.PublishPrepare;
import gr.ntua.ivml.mint.concurrent.Ticker;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.persistent.Transformation;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.Config;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

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
public class Publish3DIcons implements Runnable{
	private static final String DUMMY_ID = "carare:000000";
	
	public static final Logger log = Logger.getLogger( Publish3DIcons.class );
	
	private String pubname;
	long currentItemNo = 0l;
	long totalItemCount = 0l;

	int localItemCount = 0;
	
	Organization organization;
	String currentXsl;
	
	Writer pkgInfo;
	Dataset currentDataset;
	
	private static class ItemId {
		public String name;
		public String id;
		
		public String toXml() {
			if( name == null ) 
				return "<item id=\""+StringEscapeUtils.escapeXml( id )+"\"/>";
			else
				return "<item id=\""+StringEscapeUtils.escapeXml( id )+
				"\" name=\"" + StringEscapeUtils.escapeXml( name ) + "\"/>";
		}
	}
	
	public Publish3DIcons( Organization org ) {
		this.organization = org;
		pubname = "Publication_"+ organization.getDbID();
	}
	
	public void run() {
		try {
			DB.getSession().beginTransaction();
			DB.getStatelessSession().beginTransaction();
			// refresh ds for this session
			organization = DB.getOrganizationDAO().getById(organization.getDbID(), false);
			
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
		String tmpFileLocation  = null;
		try {
			List<PublicationRecord> prlist = DB.getPublicationRecordDAO().findByOrganization(organization);
			// potential item count
			for( PublicationRecord pr: prlist ) 
				totalItemCount += pr.getPublishedDataset().getValidItemCount();
			
			File tmpFile = File.createTempFile("3dicons_pub_"+organization.getDbID()+"_", ".tgz" );
			tmpFileLocation = tmpFile.getAbsolutePath();
			File pkgInfoFile = File.createTempFile("itemNames", ".txt");
			pkgInfo = new OutputStreamWriter( new FileOutputStream( pkgInfoFile ), "UTF8");

			// String targetSchemaName = Config.get( "3dicons.publish.schema" );
			
			log.debug( "Publication tmpfile is :" + tmpFile.getAbsolutePath());
			final TarArchiveOutputStream tos = 
				new TarArchiveOutputStream( new GzipCompressorOutputStream(
						new BufferedOutputStream( new FileOutputStream(tmpFile))));


			TarArchiveEntry tae = new TarArchiveEntry( pubname + "/" );
			tos.putArchiveEntry(tae);
			tos.closeArchiveEntry();

			//get publication records and published sets
			for (PublicationRecord pr:prlist){
				final Dataset publishedDs = pr.getPublishedDataset();
				currentXsl = null;
				localItemCount = 0;
				if( pr.getPublishedDataset() != pr.getOriginalDataset()) {
					// There should be some xsl involved
					currentXsl = ((Transformation) publishedDs).getXsl();
				}
				currentDataset = pr.getOriginalDataset();
				ApplyI<Item> itemPublish = new ApplyI<Item>() {
					public void apply(Item carareItem) throws Exception {
						Item sourceItem = carareItem.getSourceItem();
						Document doc = carareItem.getDocument();
						
						// now modify doc to contain the id!
						if(	! generateCarareId(doc)) return;
						addItem( publishedDs.getCreator(),  currentItemNo, doc,
								sourceItem, carareItem, currentXsl, tos);

						if( t.isSet()) {
							t.reset();
							//	setStatusMessage( "Processed " + currentItemNo + "/" + totalItemCount );
							log.debug( "Publication: " + organization.getDbID() + " Items:" + currentItemNo + "/" + totalItemCount );
							DB.commit();
						}
						currentItemNo+=1;
						localItemCount++;
					}
				};
				publishedDs.processAllValidItems(itemPublish, true );
				currentDataset.logEvent("Publication for this Dataset finished", 
					"Published " + localItemCount + " out of " + publishedDs.getValidItemCount() + " valid Items.");
				pr.setStatus(PublicationRecord.PUBLICATION_OK);
				pr.setPublishedItemCount(publishedDs.getValidItemCount());
				DB.commit();
			}

			writePackageInfo(pkgInfoFile, tos);
			tos.close();
			log.info( "Publication " +organization.getDbID() + " published " + currentItemNo + " Items");
			DB.commit();
			// note the publication file with the download action
			PublishPrepare.addtoPublicationFilesMap(Long.toString( organization.getDbID()), tmpFileLocation);

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
				log.error( "Problem while publishing", e2 );
				throw e2;
			}	finally {
				if( is != null ) is.close();
				if( pkgInfoFile != null ) pkgInfoFile.delete();
			}
			setPublicationStatus(PublicationRecord.PUBLICATION_OK);
			logAll( "Finished publishing to MORE","");
		} catch( Exception e ) {
			log.error( "Publication: " + organization.getDbID() + " CurrentItemNo:" + currentItemNo + "\nError: " , e );
			setPublicationStatus(PublicationRecord.PUBLICATION_FAILED);
			logAll( "There was a problem during publication to MORE!", "");
		} finally {
			t.cancel();
			finishPublication();
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
	private boolean generateCarareId(Document doc) {
		XPathContext namespaces = new XPathContext( "car", "http://www.carare.eu/carareSchema" );

		Nodes carareNodes = doc.query( "/car:carareWrap/car:carare",namespaces );
		Node carare = null;
		Attribute carareId = null;
		
		if( carareNodes.size()==1) {
			carare = carareNodes.get(0);
			carareId = ((Element) carare).getAttribute("id");
			if( carareId != null) {
				// there is an id,
				if( ! DUMMY_ID.equals( carareId.getValue())) return true;
				// else there is a dummy, so we need to find the real thing
			} else {
				log.debug( "Missing carae@id attribute");
				// there should be a dummy id attribute
				return false;
			}
		} else {
			log.debug( "No root node in carare file on publication? ");
			// failure, no root node for carare Item
			return false;
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
				return true;
			} else {
				// too many heritageAsset Ids to handle
				log.debug( "Multiple HertageAssets in Carare record.");
				//appendReport("Multiple HeritageAssets in carare record.\n", 20000 );
				return false;
			}
		} else {
			// need to check the digitalResources
			Nodes digitalResourceIds = doc.query( "//car:digitalResource/car:recordInformation/car:id", namespaces );
			if( digitalResourceIds.size() == 1 ) {
				// bingo
				String id = digitalResourceIds.get(0).getValue();
				id = "DR" + id;
				carareId.setValue(id);
				return true;
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
				return false;
			}
		}
	}

	private void writePackageInfo( File pkgData, TarArchiveOutputStream output ) throws Exception {
		File tmpFile = File.createTempFile("pkginfo", ".xml");
		java.io.FileOutputStream fos = new java.io.FileOutputStream( tmpFile );
		
		OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF8" );
		pkgInfo.flush();
		writer.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );
		writer.write( "<package timestamp=\"" );
		writer.write( new Date().toString() );
		writer.write( "\" size=\"" + currentItemNo + "\">\n" );
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
	
	//No need for xmlnode sourceitemtree 
	//carate document doc will be 
	
	// add dir and files to the output zip
	private void addItem(User uploader, long itemNo, Document carareDoc,
			Item sourceItem, Item carareItem, String xsl, TarArchiveOutputStream output ) throws Exception {
		
		String path = pubname + "/" + "item_" + itemNo + "/";
		
		// create item_"id" entry	
		output.putArchiveEntry( new TarArchiveEntry( path ));
		output.closeArchiveEntry();
		
		// add input xml file
		if( sourceItem != null )
			addStringToTar(sourceItem.getXml(), path + "native.xml", output );
		
		// add xsl as file
		// output.putNextEntry(new ZipEntry( path + "mapping.xsl" ));
		// writer.write( xsl);
		// writer.flush();
		// output.closeEntry();		
		
		// add output xml
		String outputXml;
		outputXml = carareDoc.toXML();
		
		addStringToTar( outputXml, path+"carare.xml", output );
		
		// add info file
		// extract native id and label from carare xml
		XPathContext namespaces = new XPathContext( "car", "http://www.carare.eu/carareSchema" );
		
		Nodes tmpNodes = carareDoc.query( "/car:carareWrap/car:carare/@id", namespaces );
		if( tmpNodes.size() != 1 ) {
			// crap, how do we abort cleanly from here ?
			// should not happen anyway
			log.warn( "No CarareId where there should be one!!!");
		}
		Node nativeCarareId = tmpNodes.get(0);
		Node nativeLabel = null;

		// fix the carareItem in the db
		carareItem.setXml(carareDoc.toXML());
		carareItem.setPersistentId(nativeCarareId.getValue());
		
		Nodes labelNodes = carareDoc.query( "//car:heritageAssetIdentification/car:appellation/car:name", namespaces );
		
		if( labelNodes != null && (labelNodes.size() > 0 )) {
			nativeLabel = labelNodes.get(0);
		} else {
			labelNodes = carareDoc.query( "//car:digitalResource/car:appellation/car:name", namespaces );
			if( labelNodes != null && (labelNodes.size() > 0 )) 
				nativeLabel = labelNodes.get(0);
		}

		StringBuilder sb = new StringBuilder();
		
		sb.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );
		sb.append( "<record>\n");

		sb.append( "  <provider id=\"") ;
		sb.append( Long.toString( organization.getDbID()));
		sb.append( "\" name=\"" );
		sb.append( StringEscapeUtils.escapeXml(organization.getOriginalName()));
		sb.append( "\"/>\n" );

		sb.append( "  <user id=\"" + uploader.getLogin() + 
						"\" name=\"" + uploader.getName() + "\"/>\n");
		ItemId itemId = new ItemId();
		itemId.id = nativeCarareId.getValue();
		if( nativeLabel!=null ) {
		   itemId.name = nativeLabel.getValue();
		}
		sb.append( "  " + itemId.toXml() + "\n" );
		pkgInfo.write( "    " + itemId.toXml() + "\n" );
		sb.append( "</record>\n" );
		addStringToTar(sb.toString(), path+"info.xml", output );
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
		sb.append( "?orgId=");
		sb.append( organization.getDbID() );

		return sb.toString();
	}
	
	private void setPublicationStatus( String status ) {
		for( PublicationRecord pr: DB.getPublicationRecordDAO().findByOrganization(organization)) {
			pr.setStatus(status);
		}
		DB.commit();
	}
	
	private void finishPublication() {
		Date now = new Date();
		for( PublicationRecord pr: DB.getPublicationRecordDAO().findByOrganization(organization)) {
			pr.setEndDate(now);
		}
		DB.commit();
	}
	private void logAll( String mesg, String detail ) {
		for( PublicationRecord pr: DB.getPublicationRecordDAO().findByOrganization(organization)) {
			pr.getOriginalDataset().logEvent(mesg,  detail );
		}
		DB.commit();
	}
	
}

