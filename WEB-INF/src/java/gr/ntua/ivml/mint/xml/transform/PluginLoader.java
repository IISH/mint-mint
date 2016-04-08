package gr.ntua.ivml.mint.xml.transform;

import gr.ntua.ivml.mint.util.Config;
import net.sf.saxon.Configuration;
import net.sf.saxon.TransformerFactoryImpl;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import org.apache.log4j.Logger;

import javax.xml.transform.TransformerFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * PluginLoader
 * <p/>
 * This static utility class loads and detects the classes defined in a plugin folder list.
 * It will return a TransformerFactory with extended functions ( if any are found ).
 *
 * @author Lucien van Wouw <lwo@iisg.nl>
 */
final class PluginLoader {
	protected final static Logger log = Logger.getLogger("gr.ntua.ivml.mint.xml.transform.PluginLoader");

	private static List<Class<ExtensionFunctionDefinition>> extensionFunctions = new ArrayList<>();
	private static List<String> xmlnsList = new ArrayList<>();


	/**
	 * scan
	 * <p/>
	 * Iterate over the plugin folders and make a list of the classes that extend the desired class.
	 */
	private static synchronized void scan() {

		if (extensionFunctions.isEmpty()) {

			File pluginFolder = Config.getPluginsDir();

			final List<String> jarFiles = new ArrayList<>();
			scanDirectory(pluginFolder, jarFiles);

			for (String jarFile : jarFiles) {
				try {
					loadClass(jarFile);
				} catch (ClassNotFoundException e) {
					log.error("Failed to find class in plugin: " + jarFile);
					log.error(e);
				} catch (InstantiationException e) {
					log.error("Failed to instantiate class in plugin: " + jarFile);
					log.error(e);
				} catch (IllegalAccessException e) {
					log.error("Not allowed to load classes from plugins.");
					log.error(e);
				} catch (NoSuchMethodException | InvocationTargetException e) {
					log.error("Cannot find an empty constructor in the plugin.");
					log.error(e);
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * scanDirectory
	 * <p/>
	 * Recursive method. Lists content of directory. If it finds a jar, it adds it to the list, if is finds
	 * a directory, it calls itself with that directory as argument
	 *
	 * @param f        - file handle representing a directory
	 * @param jarFiles - List to which we're adding Jar names
	 */
	private static void scanDirectory(File f, List<String> jarFiles) {

		final File[] children = f.listFiles();
		if (children == null || children.length == 0) {
			log.info("No plugin detected in " + f.getAbsolutePath());
		} else {
			for (File aChildren : children) {

				if (aChildren.isFile() && aChildren.getName().endsWith(".jar")) {
					String canonicalPath;
					try {
						canonicalPath = aChildren.getCanonicalPath();
					} catch (IOException e) {
						log.info("Unable to read plugin.");
						log.error(e);
						continue;
					}
					jarFiles.add(canonicalPath);
					log.info("Scan found plugin: " + canonicalPath);
				} else if (aChildren.isDirectory()) {
					scanDirectory(aChildren, jarFiles);
				} else {
					log.debug("Ignore: " + aChildren.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * loadClass
	 * <p/>
	 * Load the ExtensionFunctionDefinition class and register it.
	 *
	 * @param pathToJar location of the jar
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	private static void loadClass(String pathToJar) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

		final JarFile jarFile = new JarFile(pathToJar);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
		URLClassLoader cl = URLClassLoader.newInstance(urls, PluginLoader.class.getClassLoader());

		while (e.hasMoreElements()) {
			JarEntry je = e.nextElement();
			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				log.debug("Not a class, ignoring: " + je.getName());
				continue;
			}
			// -6 because of .class
			String className = je.getName().substring(0, je.getName().length() - 6).replace('/', '.');
			final Class clazz = cl.loadClass(className);
			if (ExtensionFunctionDefinition.class.isAssignableFrom(clazz)) {
				extensionFunctions.add(clazz);
			}
		}
	}

	/**
	 * register
	 * <p/>
	 * Instantiate the extended function class and register it.
	 *
	 * @param saxonConfig
	 * @param clazz
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static void register(Configuration saxonConfig, Class<ExtensionFunctionDefinition> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		final Constructor<ExtensionFunctionDefinition> constructor = clazz.getConstructor();
		final ExtensionFunctionDefinition extensionFunctionDefinition = constructor.newInstance();
		saxonConfig.registerExtensionFunction(extensionFunctionDefinition);

		addPrefix(extensionFunctionDefinition);
	}

	private static void addPrefix(ExtensionFunctionDefinition extensionFunctionDefinition) {
		final StructuredQName functionQName = extensionFunctionDefinition.getFunctionQName();
		final String xmlns = "xmlns:" + functionQName.getPrefix() + "=\"" + functionQName.getURI() + "\"";
		if (!xmlnsList.contains(xmlns)) {
			xmlnsList.add(xmlns);
		}
	}

	/**
	 * getTransformerFactory
	 *
	 * @return The factory with the extended functions.
	 */
	public static TransformerFactory getTransformerFactory() {

		scan();

		System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
		System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");

		final TransformerFactory tFactory = TransformerFactory.newInstance();

		if (tFactory instanceof TransformerFactoryImpl) {
			TransformerFactoryImpl tFactoryImpl = (TransformerFactoryImpl) tFactory;
			Configuration saxonConfig = tFactoryImpl.getConfiguration();
			for (Class<ExtensionFunctionDefinition> clazz : extensionFunctions) {
				try {
					register(saxonConfig, clazz);
				} catch (Exception e) { // we are more greedy here because of our earlier error handling in the scan method.
					log.error(e);
				}
			}
		} else {
			log.warn("TransformerFactory is not of type TransformerFactoryImpl");
		}

		return tFactory;
	}

	public static List<String> getPrefixNamespace() {
		return xmlnsList;
	}
}
