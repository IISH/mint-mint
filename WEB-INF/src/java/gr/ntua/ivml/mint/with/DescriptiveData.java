package gr.ntua.ivml.mint.with;

import gr.ntua.ivml.mint.persistent.Item;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;

public class DescriptiveData extends HashMap<String, MultiLiteralOrResource>{
	Edm edm;
	
	protected final Logger log = Logger.getLogger(getClass());
	
//	public HashMap<String,ArrayList<String>> listOfSimpleLiterals = new HashMap<String, ArrayList<String>>(); 

	private Dates dates;
	private SingleGroup uris;
	
	
	
	public DescriptiveData(){
		
	}
	
	public  DescriptiveData(Item item){
		for (String label:Edm.getXpaths().keySet()){
			String path = Edm.getXpaths().get(label);			
			this.put(label, parseItem(path, item));		
		}
		
		dates = new Dates(item);
		uris = new SingleGroup(item);
	}
	
	public void parseUris(){
		
	}

	public MultiLiteralOrResource parseItem(String path,Item item){
	
		
		MultiLiteralOrResource result = new MultiLiteralOrResource(); 		
		nu.xom.Nodes nods = item.getNodes(path);
		if (nods != null) {
			for (int i = 0; i < nods.size(); i++) {
				parseNode(nods.get(i),result);
			}
		}
		return result;
	}
	
	public void parseNode(Node temp,MultiLiteralOrResource mlor){
		 Element node = (Element) temp;

		for (int i = 0; i < node.getAttributeCount(); i++) {
		
			String type;
			Attribute atr = node.getAttribute(i);
			type = atr.getQualifiedName();
			if (type.contains("rdf:resource")) {
						mlor.addResource(atr.getValue());
				break;
			} else if (type.contains("xml:lang")) {
				mlor.setMultiLiteral(atr.getValue(), node.getValue());
				break;
			}
		
		}
		if (node.getAttributeCount() == 0) {
			mlor.setMultiLiteral("unknown", node.getValue());
		}
	

	}
		
	
	
	
	public net.sf.json.JSONObject toJson() {
		net.sf.json.JSONObject result = new  net.sf.json.JSONObject();
		for (String label : this.keySet()) {
			MultiLiteralOrResource mlor = this.get(label);
			net.sf.json.JSONObject entry = mlor.toJson();
			result.put(label, mlor.toJson());
		
		}
		result.put("rdfType", "http://www.europeana.eu/schemas/edm/ProvidedCHO");
		result.put("metadataRights", "http://creativecommons.org/publicdomain/zero/1.0/");
		result.put("dates",dates.toJson());
	
		for (Single sin:uris){
			result.put(sin.label, sin.uri);
		}
		

		return result;
	}
	
	
}
