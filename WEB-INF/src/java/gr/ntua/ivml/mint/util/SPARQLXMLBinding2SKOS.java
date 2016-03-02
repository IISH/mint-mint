package gr.ntua.ivml.mint.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class SPARQLXMLBinding2SKOS {
	
	static String path = "/Users/nsimou/Desktop/AthenaPlus Portal/";
	
	public static void main(String args[]){
		
//		String file = path+"Cultural context/Cultural Broader";
//		String file = path+"TGN Nations/TGNNationsEnglish";
//		String file = path+"Materials/MaterialsBroader";
//		String file = path+"Technique/TechniqueBroader";
//		String file = path+"ObjectsFacet/ObjectsFacet";
		String file = path+"Cultural Content/CulturalType";
		
		ArrayList<String> parseResults = readXMLFile(file+".xml");
		convertToSKOS(parseResults,"http://www.culturalType.gr/ConceptScheme", file+".rdf");
	}
	
	public static ArrayList<String> readXMLFile(String file) {
		ArrayList<String> results = new ArrayList<String>();
		File xmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);

			NodeList nodes = doc.getElementsByTagName("result");
			for(int i = 0; i < nodes.getLength() ; i++) {
				Element result = (Element) nodes.item(i);
				NodeList resultNodes = result.getElementsByTagName("binding");
				
				String x="",l="",broader="";

				for (int j = 0; j < resultNodes.getLength() ; j ++) {
					String key = resultNodes.item(j).getAttributes().getNamedItem("name").getNodeValue();
					Element resultElement = (Element) resultNodes.item(j);
					
					if(resultElement.getElementsByTagName("uri").getLength() > 0) {
						String value = resultElement.getElementsByTagName("uri").item(0).getChildNodes().item(0).getNodeValue();
						if(key.matches("x"))
							x = value;
						if(key.matches("broader"))
							broader = value;
					}
					
					if(resultElement.getElementsByTagName("literal").getLength() > 0) {
						String value = resultElement.getElementsByTagName("literal").item(0).getChildNodes().item(0).getNodeValue();
						if(key.matches("l"))
							l = value;
					}
				}
				results.add(x+"*"+l+"*"+broader);
			}
			return results;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void convertToSKOS(ArrayList<String> results, String conceptSchemeStr, String outputFile){
		String skos="http://www.w3.org/2004/02/skos/core#" ;
		
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix( "skos" , skos);
		
		Resource conceptScheme = model.createResource(conceptSchemeStr);
		conceptScheme.addProperty(RDF.type,model.createResource(skos+"ConceptScheme"));
		conceptScheme.addProperty(model.createProperty(skos+"prefLabel"),"A concept scheme","en");
		
		
		
		for(int i=0; i< results.size(); i++){
			StringTokenizer st = new StringTokenizer(results.get(i),"*");
			String conceptStr = st.nextToken();
			String prefLabel = st.nextToken();
//			String broaderStr = st.nextToken();
			
			Resource concept = model.createResource(conceptStr);
//			Resource broader = model.createResource(broaderStr);
			concept.addProperty(RDF.type,model.createResource(skos+"Concept"));
			concept.addProperty(model.createProperty(skos+"prefLabel"), prefLabel, "en");
//			concept.addProperty(model.createProperty(skos+"broader"), broader);
			concept.addProperty(model.createProperty(skos+"inScheme"), conceptScheme);
		}
		
		//Write to file
		try{
			File rdfFile = new File(outputFile);
			FileOutputStream outputRDFJena = new FileOutputStream(rdfFile);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			model.write(outStream,"RDF/XML");
	//		model.write(outStream,"N-TRIPLE");
			outStream.writeTo(outputRDFJena);
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
		
	}

}
