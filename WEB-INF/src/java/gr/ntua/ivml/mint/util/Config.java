package gr.ntua.ivml.mint.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import gr.ntua.ivml.mint.db.Meta;

/**
 * Class to read and reread a property file. Is static unsynced and stupid, 
 * but easy to use.
 * 
 * @author Arne Stabenau
 *
 */
public class Config {
	private static final String PROPS = "mint.properties";
	private static final String LOCAL = "local.properties";
	private static final String CUSTOM = "custom.properties";

	public static Properties properties = new Properties( System.getProperties());
	public static Properties custom = new Properties( properties );
	public static Properties local = new Properties( custom);
	public static Properties live = new Properties( local);
		
	public static final Logger log = Logger.getLogger( Config.class );
	public static ServletContext context;
	public static File projectRoot = null;
	
	public static String get(String key) {
		return Config.getWithDefault(key, null);
	}

	public static String getWithDefault( String key, String defaultValue ) {
		
		if(live.containsKey(key))
			return live.getProperty(key);
		if(local.containsKey(key)) 
			return local.getProperty(key);
		if(custom.containsKey(key)) 
			return custom.getProperty(key);
		if(properties.containsKey(key)) 			
			return properties.getProperty(key);
				
		return defaultValue;
	}
	
	public static boolean debugEnabled()
	{
		return Config.getBoolean("debug");
	}
	
	public static boolean getBoolean( String key, boolean defaultValue ) {
		String result = Config.get(key);
		
		if(result != null) {
			if(result.equalsIgnoreCase("true") || result.equalsIgnoreCase("yes") || result.equalsIgnoreCase("1")) {
				return true;
			}
		}
		
		return defaultValue;
	}

	public static boolean getBoolean( String key ) {
		return Config.getBoolean(key, false);
	}


	public static boolean has( String key ) {
		return local.contains(key) || custom.containsKey(key) || properties.containsKey(key);
	}
	
	public static String get( String key, String defaultValue ) {
		return properties.getProperty( key, defaultValue );
	}
	
	public static void readProps() {

		read(properties, PROPS);
		read(custom, CUSTOM);
		read(local, LOCAL);
		System.currentTimeMillis();

	}

	private static void read(Properties properties, String resource) {
		final String file = System.getProperty(resource);
		if ( file == null ) {
			InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(resource);
			if (inputStream != null) try {
				properties.load(inputStream);
			} catch (IOException e) {
				log.error( "Can't read properties", e );
				throw new Error( "Configuration file " + PROPS + " not found in CLASSPATH", e);
			}
		} else {
			final InputStream inputStream;
			try {
				inputStream = new FileInputStream(file);
				properties.load(inputStream);
			} catch (IOException e) {
				throw new Error( "Configuration file " + file + " not loaded.", e);
			}
		}
	}
	
	/** Set a live property in the database
	 * 
	 * @param key		
	 * @param value		
	 */
	public static void setLive( String key, String value ) {
		Meta.put("Config["+key+"]", value);
		live.setProperty(key,  value );
	}
	
	
	/** Read all live properties from database
	 */
	public static void updateFromMeta() {
		List<Tuple<String, String>> prop_list = Meta.getLike("Config%");
		for (Tuple<String, String> t : prop_list ) {
			String keyWithPrefix = t.first();
			String value = t.second();
			String key = keyWithPrefix
					.substring("Config[".length(), 
							keyWithPrefix.length()-1);
			live.setProperty(key, value);
		}
	}
	
	public static void setContext( ServletContext sc ) {
		context = sc;
	}
	
	public static ServletContext getContext( ) {
		return context;
	}

	public static String getRealPath( String path  ) {
		if( context == null ) {
			log.warn("Calling getRealPath( path )  with no context set.");
			return path;
		}
		return context.getRealPath( path );
	}

	/**
	 * Go up a couple of dirs from mint.properties and return that as project root.
	 * @return
	 */
	public static File getProjectRoot() {
		if( projectRoot == null ) {
			try {
				String resultPath = Config.class.getResource("/mint.properties").getPath();
				resultPath = resultPath.replaceFirst("/WEB-INF.*", "");
				// sometimes this comes from classes sometimes from src ...
				// it should always be in the WEB-INF folder, so cut that of

				projectRoot = new File( resultPath );
				log.info( "project root at '" + projectRoot + "'");
				if( !projectRoot.canRead()) projectRoot = null;
			} catch( Exception e) {
				log.error( "Cant find project root", e );
			}
		}
		return projectRoot;
	}

	public static String getSchemaPath(String xsd) {
		return new File( getSchemaDir(), xsd ).getAbsolutePath();
	}
	
	public static String getXSLPath(String xsl) {
		return context.getRealPath(Config.getWithDefault("paths.xsl", "xsl") + System.getProperty("file.separator") + xsl);
	}

	public static String getScriptPath(String script) {
		return context.getRealPath(Config.getWithDefault("paths.scripts", "scripts") + System.getProperty("file.separator") + script);
	}
	
	public static File getSchemaDir() {
		return getProjectFile( Config.getWithDefault("paths.schemas", "schemas") );
	}
	
	public static File getXSLDir() {
		return getProjectFile( Config.getWithDefault("paths.xsl", "xsl") );
	}
	
	public static File getViewsDir() {
		return getProjectFile( Config.getWithDefault("paths.views", "views") );
	}

	public static File getPluginsDir() {
		return getProjectFile( Config.getWithDefault("paths.plugins", "plugins") );
	}

	public static String getViewsPath(String file) {
		return new File( getSchemaDir(), file ).getAbsolutePath();
	}
	
	public static File getUploadedSchemaDir() {
		String schemas = Config.getWithDefault("paths.schemas", "schemas");
		String uploaded = Config.getWithDefault("paths.schemas.uploaded", "uploaded");
		
		return getProjectFile( schemas + System.getProperty("file.separator") + uploaded );
	}
	
	public static File getTestRoot() {
		return getProjectFile( "WEB-INF/src/test" );
	}
	
	public static File getTestFile( String path ) {
		return new File( getTestRoot(), path );
	}
	
	public static File getProjectFile( String relativePath ) {
		return new File( getProjectRoot(), relativePath );
	}

	public static File getCustomJsp(String jsp) {
		String subdir = get("custom.name" );
		File jspFile = getProjectFile( "WEB-INF/custom/"+subdir+"/jsp/" + jsp);
		if( jspFile.exists() && jspFile.canRead()) return jspFile;
		return null;
	}

	public static String getViewsPath() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
 