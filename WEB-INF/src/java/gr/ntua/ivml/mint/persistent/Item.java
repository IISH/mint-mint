package gr.ntua.ivml.mint.persistent;

import java.io.ByteArrayInputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.Text;
import nux.xom.xquery.XQueryUtil;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import org.xml.sax.XMLReader;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.util.StringUtils;


public class Item {
	private static Builder builder;
	
	private final static Logger log = Logger.getLogger( Item.class );
	
	private Long dbID;
	// this one might be better lazy, so we can have lots of items
	// loaded 
	private byte[] gzippedXml;
	private Long datasetId;
	private String persistentId;
	private Item sourceItem;
	private Date lastModified;
	private String label;
	private boolean valid = false;
	
	// transient
	private String xml = null;
	private Document dom;
	
	public List<Item> getDerived() {
		return DB.getItemDAO().getDerived(this);
	}
	
	/**
	 * Is there an item in the given dataset that is derived from this item?
	 * @param ds
	 * @return
	 */
	public Item getDerived( Dataset ds ) {
		return DB.getItemDAO().getDerived( this, ds );
	}
	
	/**
	 * Provide an empty list and retrieve all Items that are derived from this one.
	 * @param result
	 */
	public void getDerivedRecursive( List<Item> result ) {
		List<Item> l = getDerived();
		result.addAll(l);
		for( Item i: l ) i.getDerivedRecursive( result );
	}
	
	//
	// magic gzipping of xml strings
	//
	
	public void setXml( String xml) {
		ByteArrayOutputStream baos = null;
		GzipCompressorOutputStream gz = null;
		OutputStreamWriter osw = null;
		
		try {
			baos = new ByteArrayOutputStream();
			gz = new GzipCompressorOutputStream(baos);
			osw = new OutputStreamWriter( gz, "UTF8" );
			osw.append( xml );
			osw.flush();
			osw.close();
			setGzippedXml(baos.toByteArray());
		} catch( Exception e ) {
			// uhh shouldnt really happen!!
			log.error( "Unexpected Error on gzipping content", e );
		} finally {
			try {if( osw != null ) osw.close();} catch( Exception e ) {}
			try {if( gz!= null ) gz.close();} catch( Exception e ){}
			try {if( baos!= null ) baos.close();} catch( Exception e ){}
			
		}
		this.xml = xml;
	}
	
	public String getXml() {
		if( xml == null ) {
			xml = "";
			ByteArrayInputStream bais = null;
			GzipCompressorInputStream gz = null;
			StringWriter sw = new StringWriter();
			try {
				bais = new ByteArrayInputStream(getGzippedXml());
				gz = new GzipCompressorInputStream(bais);
				IOUtils.copy( gz, sw, "UTF8" );
				xml = sw.toString();
			} catch( Exception e ) {
				log.error( "Unexpected Error on gzipping content", e );		
			} finally {
				try { if( gz != null ) gz.close(); } catch( Exception e ) {}
				try { if( bais != null) bais.close(); } catch( Exception e ) {}
			}
		}
		return xml;
	}
	
	public boolean isEmpty(Element el) {
		Text text = (Text) el.getChild(0);
		return (text == null || text.getValue() == "" ||
				el.getAttributeCount() == 0);

	}
	
	public Element removeEmptyElements(Element el) {
		 for (int i=0; i < el.getChildCount(); i++) {
		      Node node = el.getChild(i);
		      if (node instanceof Element) {
		        if (isEmpty((Element) node)) 
		        	el.removeChild(i);
		        else 
		        	removeEmptyElements((Element) node);
		      }
		 }
		 return el;
	}
	
	public Document getDocument() {
		if( dom == null ) {
			try {
				Builder builder = getBuilder();
				synchronized( builder ) {
					dom = builder.build( getXml(), null );
				}
			} catch(Exception e ) {
				log.warn( "Item " + getDbID() + " has problems!",e );
			}
		}
		return dom;
	}

	
	
	/**
	 * Gets the Value if there is one node as the query result. 
	 * Otherwise returns null.
	 * @param query
	 * @return
	 */
	public String getValue( String query ) {
		nu.xom.Nodes nodes = getDocument().query( query );
		log.debug("Nodes founds after getValue are "+ nodes.size());
		if( nodes.size() != 1 ) return null;
		String res = nodes.get(0).getValue();
		if( res == null ) res = "";
		return res;
	}
	
	
/*	public Nodes getNodes(String query){
		nu.xom.Nodes nodes = getDocument().query( query );
		if (nodes!= null){
			return nodes;
		}
		else return null;
				
	}*/
	
	public Nodes getNodes(String query){
		//nu.xom.Nodes nodes = getDocument().query( query );
		nu.xom.Nodes nodes = XQueryUtil.xquery( getDocument(),query);
		if (nodes!= null){
			return nodes;
		}
		else return null;
				
	}
	
	public Nodes getNodesProvidedCHO(String query){
	//	Node root = getDocument().query("//*/ProvidedCHO").get(0);
		nu.xom.Nodes nodes = getDocument().query( query );
	//	nu.xom.Nodes nodes = XQueryUtil.xquery( root,query);
		log.debug("result size is :" + nodes.size());
		if (nodes!= null){
			return nodes;
		}
		else return null;
				
	}
	
	
	public Nodes getNodesAggregation(String query){
	//	Node root = getDocument().query("//*/Aggregation").get(0);
		nu.xom.Nodes nodes = getDocument().query( query );
	//	nu.xom.Nodes nodes = XQueryUtil.xquery( root,query);
		log.debug("result size is :" + nodes.size());
		if (nodes!= null){
			return nodes;
		}
		else return null;
				
	}
	
//	/RDF/Aggregation/rights/@resource -->originalRights --> withRights
	
//	/RDF/ProvidedCHO/type  -->mimeType
	
// 	/RDF/Aggregation/dataProvider	
	
//	/RDF/Aggregation/provider	
	
 	/**
	 * Set node value to given. Returns false if there is not exactly one and does nothing then.
	 * @param query
	 * @param value
	 */
	public boolean setValue( String query, String value ) {
		nu.xom.Nodes nodes = getDocument().query( query );
		if( nodes.size() != 1 ) return false;
		Node modify = nodes.get(0);
		if( modify instanceof Element ) {
			Element elem = (Element) modify;
			// replace first Text node 
			// and delete the others
			int length = elem.getChildCount();
			int i=0;
			boolean replaced = false;
			while( length > 0 ) {
				Node n = elem.getChild(i);
				if( n instanceof Text ) {
					if( replaced ) {
						elem.removeChild(i);
						i--;
					} else {
						((Text) n).setValue(value);
						replaced = true;
					}
				}
				i++;
				length--;
			}
			if( ! replaced) elem.appendChild(value);
		} else if( modify instanceof Text ) {
			Text txt = (Text) modify;
			txt.setValue(value);
		} else if( modify instanceof Attribute ) {
			Attribute attr = (Attribute) modify;
			attr.setValue(value);
		} else { return false; }
		
		// update xml
		setXml( dom.toXML());
		return true;
	}
	
	private static synchronized Builder getBuilder() {
		if( builder == null ) {
			try {
				XMLReader parser = org.xml.sax.helpers.XMLReaderFactory.createXMLReader(); 
				parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

				builder = new Builder(parser);
			} catch( Exception e ) {
				log.error( "Cannot build xml parser.", e );
			}
		}
		return builder;
	}

	
	
	/**
	 * 
	 * @param elementName you want to create
	 * @param uri of this element. This namespace needs to be in declaration.
	 * @param order List of "{uri}:localName or just localName when no namespace"
	 * @param parent element xquery string, should yield exactly one element
	 * @return
	 */
	public Element insertElement( String elementName, String uri, List<String> order, String into ) {
		// lookup order
		HashMap<String, Integer> nameOrderIndex = new HashMap<String, Integer>();
		for( int i =0; i<order.size(); i++ ) 
			nameOrderIndex.put( order.get(i), i);
		
		String lookup = elementName;
		if( ! StringUtils.empty(uri)) 
			lookup = "{"+uri+"}:"+lookup;
		int whereInsert = 0;
		if(! nameOrderIndex.containsKey(lookup)) {
			log.info( "Element not ordered, inserted at the beginning");
		} else {
			whereInsert = nameOrderIndex.get( lookup );	
		}
		
		
		nu.xom.Nodes nodes = getDocument().query( into );
		if( nodes.size() != 1 ) {
			// parent not found, can't proceed
			log.info( "Parent not found or not unique!prinlnt");
			return null;
		}
		Element parent = (Element) nodes.get(0);
		HashMap<String, String> uris = effektiveNamespaces(parent);
		String newPrefix = uris.get(uri);
		if( newPrefix == null) {
			log.warn( "Namespace " + uri + " not declared in " + parent.getLocalName());
			return null;
		}
		
		// find all the children
		int i=0;
		for( i=0; i<parent.getChildCount();i++ ) {
			Node currentChild = parent.getChild( i );
			if(! ( currentChild instanceof Element )) continue;
			Element childElement = (Element) currentChild;
					
			String childUri = childElement.getNamespaceURI();
			String lookupLoop = childElement.getLocalName();
			if( !StringUtils.empty(childUri))
				lookupLoop = "{"+childUri+"}:"+lookupLoop;
			if( nameOrderIndex.containsKey(lookupLoop)) {
				int whereLoop = nameOrderIndex.get( lookupLoop );
				if( whereLoop >= whereInsert ) break;
			}
		}

		Element newElement = new Element( newPrefix+":"+elementName, uri );
		if( i < parent.getChildCount()) 
			parent.insertChild(newElement, i );
		else
			parent.appendChild( newElement );
		
		return newElement;
	}
	
	/**
	 * Returns hash of uris and prefixes which are valid at the given element. 
	 * @param elem
	 * @return
	 */
	public HashMap<String, String> effektiveNamespaces( Element elem ) {
		HashMap<String, String> res = new HashMap<String, String>();
		while( true ) {
			int i = elem.getNamespaceDeclarationCount();
			i--;
			while( i >= 0 ) {
				String prefix = elem.getNamespacePrefix(i);
				String uri = elem.getNamespaceURI(prefix);
				
				if( !res.containsKey(uri)) 
					res.put( uri, prefix );

				i--;
			}
			if(( elem.getParent() != null ) && ( elem.getParent() instanceof Element )) 
				elem = (Element) elem.getParent();
			else 
				break;
		}
		return res;
	}
	
	/**
	 * Gets the original item that produced this item, or "this" if it is an original item.
	 * Calls getSourceItem() recursively. 
	 * @return the original item that produced this item or the item itself, if it is an original item.
	 */
	public Item getImportItem() {
		Item source = this;
	
		while (source.getSourceItem() != null) {
			source = source.getSourceItem();
		}
		
		return source;
	}
	
	public Dataset getDataset() {
		try {
			return DB.getDatasetDAO().getById(getDatasetId(), false);
		} catch(Exception e ) {
			log.error( "Problem retrieving Dataset for item", e );
			return null;
		}
	}
	
	public void setDataset(Dataset ds) {
		setDatasetId(ds.getDbID());
	}
	//
	// Boilerplate Getter and Setters (curse of java) 
	//
	

	public Long getDbID() {
		return dbID;
	}
	public void setDbID(Long dbID) {
		this.dbID = dbID;
	}
	public byte[] getGzippedXml() {
		return gzippedXml;
	}
	public void setGzippedXml(byte[] gzippedXml) {
		this.gzippedXml = gzippedXml;
	}
	public String getPersistentId() {
		return persistentId;
	}
	public void setPersistentId(String persistentId) {
		this.persistentId = persistentId;
	}
	public Item getSourceItem() {
		return sourceItem;
	}
	public void setSourceItem(Item sourceItem) {
		this.sourceItem = sourceItem;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Long getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(Long datasetId) {
		this.datasetId = datasetId;
	}

	
}
