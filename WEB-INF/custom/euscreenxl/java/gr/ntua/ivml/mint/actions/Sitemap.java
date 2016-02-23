package gr.ntua.ivml.mint.actions;


import gr.ntua.ivml.mint.util.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;


@Results({
	  @Result(name="error", location="xom_output.jsp"),
	  @Result(name="success", location="xom_output.jsp")
	})

public class Sitemap extends GeneralAction {
	
	public static Logger log = Logger.getLogger( Sitemap.class );

	public int page = -1;
	
	Element res = null;
	static final String SITEMAP = "http://www.sitemaps.org/schemas/sitemap/0.9";
	static final String BASE = "http://www.euscreen.eu/";
	static final String MAPBASE = "http://mint-projects.image.ntua.gr/euscreenxl/";
	static final int PAGESIZE = 10000;
	static final long HOUR = 3600*1000;
	
    @Action(value="Sitemap",interceptorRefs=@InterceptorRef("defaultStack"))
    public String execute() throws Exception {
    	   	
    	// retrieve the current list of published euscreen ids
    	List<String> publishedIds = new ArrayList<String>();
    	readPublishedIds( publishedIds );
    	
    	if( page == -1 ) {
    		// create sitemap index
    		int pageCount = (publishedIds.size() / PAGESIZE ) + 1;
    		res = new Element( "sitemapindex",SITEMAP);
    		for( int i=0; i<pageCount; i++ ) {
        		Element tmp = new Element( "sitemap", SITEMAP );
        		res.appendChild(tmp);
    			Element loc = new Element( "loc", SITEMAP );
    			loc.appendChild( MAPBASE + "Sitemap?page=" + i );
    			tmp.appendChild( loc );
    		}
    	} else {
    		res = new Element( "urlset",SITEMAP);
    		
    		for( int i=PAGESIZE*page; i<PAGESIZE*(page+1); i++ ) {
    			if( i<publishedIds.size()) {
    				Element tmp = new Element( "url", SITEMAP );
    				res.appendChild( tmp );
    				Element loc = new Element( "loc", SITEMAP );
    				tmp.appendChild( loc );
    				loc.appendChild( BASE + "item.html?id=" + publishedIds.get( i ));
    			}
    		}
    	}
    	
    	return SUCCESS;
    }
    
    public String getXml() {
    	return res.toXML();
    }
    
    public void readPublishedIds( List<String> res ) {
    	// String url = "http://147.102.11.221/~stabenau/noterick.txt";
    	InputStream is = null;
    	OutputStream os = null;
   		String url = "http://player7.noterik.com:8081/uter";
		String cacheName = "WEB-INF/publishedIds.txt";
		File cacheFile = Config.getProjectFile( cacheName );
		if( !cacheFile.exists() || 
     				((System.currentTimeMillis() - cacheFile.lastModified()) > HOUR )) {
			try {
    			if( cacheFile.exists()) cacheFile.delete();
    			// copy from net
        		is = new URL( url).openStream();
        		os = new FileOutputStream( cacheFile );
        		IOUtils.copy(is, os );
    		} catch( Exception e ) {
    			log.error( "Network copy failed");
    			return;
    		} finally {
    			IOUtils.closeQuietly(is);
    			IOUtils.closeQuietly(os);
    		}
		}
		try {
			is = new FileInputStream( cacheFile );
			List<String> ids = IOUtils.readLines(is, "UTF8");
			// match check
			for( String id: ids ) {
				if( id.matches("EUS_.*" ))
					res.add( id.trim());
			}
			log.info( "Added " + res.size() + " published ids to published list");
		} catch( Exception e ) {
			log.error( "Cannot read published ids from Noterick");
		} finally {
			try {
				if( is != null) is.close();
			} catch( Exception e  ){}
		}
    }
}