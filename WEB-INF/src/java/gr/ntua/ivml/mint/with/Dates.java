package gr.ntua.ivml.mint.with;

import gr.ntua.ivml.mint.persistent.Item;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;

import org.apache.log4j.Logger;

public class Dates extends WithSimpleGroup{
	



protected final Logger log = Logger.getLogger(getClass());


public Dates(Item item){
	super(item);
	for (String label:Edm.getDatepaths().keySet()){
		String path = Edm.getDatepaths().get(label);			
		parseItem(path, item,label);
	}
	
}


public void parseItem(String path, Item item,String label) {
	nu.xom.Nodes nods = item.getNodes(path);
	if (nods != null) {
		for (int i = 0; i < nods.size(); i++) {	
			 this.add(parseNode(nods.get(i),item,label));
		}
	}
	

}



public DateObject parseNode(Node node,Item item,String label) {
	 Element element = (Element) node;
	 
	 DateObject result = new DateObject();
	 for (int i = 0; i < element.getAttributeCount(); i++) {
			String type;
			Attribute atr = element.getAttribute(i);
			type = atr.getQualifiedName();
			if (type.contains("rdf:resource")) {
				result.put("free" , label +" "+ atr.getValue()); //to EDM type

				break;
			}
	 }
	 if (element.getValue()!=null){
		result.put("free",label +" "+ element.getValue()); 

	 }
	return result;
}

}
