package gr.ntua.ivml.mint.xml.transform;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import net.sf.saxon.lib.FeatureKeys;

public class XSLTransform implements ItemTransform {
	private static final Logger log = Logger.getLogger(XSLTransform.class);
	
	String xsl = null;
	Transformer tr = null;
	Map<String, String> parameters;
	
	public void setXSL(String xsl) {
		this.xsl = xsl;
		tr = null;
	}
	
	public String getXSL() {
		return this.xsl;
	}
	
	private void applyParameters(Transformer transformer) {
		if(parameters == null) return;
	    for(String parameter: parameters.keySet()) {
    			transformer.setParameter(parameter, parameters.get(parameter));
	    }
	}
	
	private Transformer getTransformer() throws TransformerConfigurationException {
		if( tr == null ) {
			System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
			System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
		    TransformerFactory tFactory = TransformerFactory.newInstance();
		    tFactory.setAttribute(  FeatureKeys.DTD_VALIDATION, false );
		    tFactory.setURIResolver(new XSLURIResolver());
		    StreamSource xslSource = new StreamSource(new StringReader(xsl));
		    try {
		    tr = tFactory.newTransformer(xslSource);
		    } catch( TransformerConfigurationException tce ) {
		    	log.error( tce.getMessageAndLocation(), tce );
		    	throw tce;
		    }
		    
		    this.applyParameters(tr);
		    
		    tr.setErrorListener(new ErrorListener() {

				@Override
				public void error(TransformerException arg0)
						throws TransformerException {
					throw arg0;
				}

				@Override
				public void fatalError(TransformerException arg0)
						throws TransformerException {
					throw arg0;					
				}

				@Override
				public void warning(TransformerException arg0)
						throws TransformerException {
					arg0.printStackTrace(new PrintStream(System.err));
				}
		    	
		    });
		}
		// dont know if this helps with anything
		tr.reset();
		return tr;
	}
	
	/**
	 * If xsl is stored in this object, use a stored transformer, don't make a new one.
	 * @param xml
	 * @param xsl
	 * @return
	 * @throws TransformerException
	 */
	public String transform(String xml, String xsl) throws TransformerException {

		if(( xsl != null ) && (!xsl.equals(this.xsl))) this.setXSL(xsl);
		Transformer transformer = getTransformer();
		
		String result = "";
	
	    StringWriter out = new StringWriter();

    
	    StreamSource xmlSource = new StreamSource(new StringReader(xml));
	    StreamResult xmlResult = new StreamResult(out);
	    
	    transformer.transform(xmlSource, xmlResult);
	    result = out.toString();
		
		return result;
	}

	
	//using DOM, disabling validation
	
	public void transform(InputStream xml, String xsl,OutputStream out ) throws Exception {
		System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
		System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setAttribute("http://xml.org/sax/features/namespaces", true);
        factory.setAttribute("http://xml.org/sax/features/validation", false);
        factory.setAttribute("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        factory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(false);
        factory.setIgnoringComments(false);
        factory.setValidating(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(xml));

        Source source = new DOMSource(document);

		TransformerFactory tFactory = TransformerFactory.newInstance();
        

	    StreamSource xslSource = new StreamSource(new StringReader(xsl));
	    StreamResult xmlResult = new StreamResult(out);
	
	    
	    Transformer transformer = tFactory.newTransformer(xslSource);
	    this.applyParameters(transformer );
	    
	    transformer.transform(source, xmlResult);
	}


	@Override
	public String transform(String input) throws Exception {
		return transform(input, null);
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	 
}
