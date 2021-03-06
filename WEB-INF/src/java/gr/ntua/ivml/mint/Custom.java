package gr.ntua.ivml.mint;

import java.io.UnsupportedEncodingException;
import java.util.List;

import gr.ntua.ivml.mint.actions.ItemPreview;
import gr.ntua.ivml.mint.actions.ItemPreview.View;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.User;

import javax.servlet.http.HttpServletRequest;

import net.minidev.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrInputDocument;

/**
 * How to customize behaviour in Mint2?
 * 
 * At the place in the base project where you want customized behaviour, insert the standard behaviour
 * into a method in this object, call it customDoSomething( args ).
 * 
 * Create a static way of calling it without the custom... so static doSomething( args ). This is for easier use of the 
 * custom system. You don't need to do it and you can call the customized method with Custom.getInstance().someOtherName()
 *  
 * 
 * In your custom/java dir create the class CustomBehaviour that derives from Custom 
 * CustomBehaviour extends Custom {
 * }
 * 
 * and overwrite the customDoSomething( args ) method.
 * you can create other custom behaviours for existing methods if you like.
 * 
 * 
 * 
 * @author Arne Stabenau 
 *
 */
public class Custom {
	private static final Logger log = Logger.getLogger( Custom.class );
	
	private static Custom instance = null;
	
	public static Custom getInstance() {
		if( instance == null ) {
			try {
				Class clazz  =  Custom.class.getClassLoader().loadClass( "CustomBehaviour" );
				instance = (Custom) clazz.newInstance();
			} catch( Exception e ) {
				log.info( "No CustomBehaviour found" );
				instance = new Custom();
			}
		}
		return instance;
	}
	

	//
	// Customize Solrize behaviour
	//
	
	public static void modifySolarizedItem( Item item, SolrInputDocument sid ) {
		getInstance().customModifySolarizedItem(item, sid);
	}
	
	/**
	 * Custom creation of extra fields or modification of existing fields in the index.
	 * @param item
	 * @param sid
	 */
	public void customModifySolarizedItem( Item item, SolrInputDocument sid ) {
		
	}


	public static boolean allowSolarize(Dataset ds) {
		return getInstance().customAllowSolarize(ds);
	}
	
	/**
	 * Customize decision, which datasets need to be solarized.
	 * Default solarize everything.
	 * @param ds
	 * @return
	 */
	public boolean customAllowSolarize( Dataset ds ) {
		return true;
	}

	public static void rightsFilter(SolrQuery query, User user ) {
		getInstance().customRightsFilter( query, user );
	}
	
	/**
	 * You want different filter for searchable stuff than the default?
	 * Modify the filterQuerys in the solr query.
	 * @param query
	 */
	public void customRightsFilter( SolrQuery query, User user  ) {
		
	}
	
	/**
	 * If you want to modify the conversion of xpaths to fieldnames, here is the place!
	 */
	
	public static String sanitizeSolrXpath( String xpath) {
		return customSanitizeSolrXpath(xpath);
	}
	
	public static String solrFieldNameEncode(String s) {
		String solrOut = "";
		try {
			byte[] bytes = s.getBytes("UTF-8");
			for (byte b : bytes) {
			    solrOut = solrOut + Integer.toHexString(b & 0xFF);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return solrOut;
	}
		
	public static String customSanitizeSolrXpath( String xpath) {
		// remove namespace
		// replace / with _
		// remove text() node marker
		xpath = xpath.replaceAll("/(@?)[^:]+:", "/$1").replace("/", "_").replaceAll("_text\\(\\)", "");
		//substitute all non-alphanumeric characters by their UTF-8 hex encoding
		//(because Solr only accepts alphanumerics and "_" in field names
		String solrName = "";
		for (int i=0; i<xpath.length(); i++) {
			String s = xpath.substring(i, i+1);
			if (StringUtils.isAlphanumeric(s) || s.equals("_"))
				solrName = solrName + s;
			else
				solrName = solrName + solrFieldNameEncode(s);
		}
		return solrName;
	}
	
	public static void login( HttpServletRequest req ) {
		getInstance().customLogin( req );
	}
	
	public void customLogin( HttpServletRequest req) {
	}


	public static void organizationStats(JSONObject ajson) {
		getInstance().customOrganizationStats( ajson );
	}
	
	public void customOrganizationStats( JSONObject ajson ) {
		
	}

	/*
	 * Look into ItemPreview and create custom views based on what you find
	 */
	public static void addViews(ItemPreview itemPreview) {
		getInstance().customAddViews( itemPreview );
	}

	public void customAddViews(ItemPreview itemPreview) {
	}
	
}
