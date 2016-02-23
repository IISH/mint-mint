package gr.ntua.ivml.mint.with;

import java.util.ArrayList;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import gr.ntua.ivml.mint.persistent.Item;

public class SingleGroup extends ArrayList<Single>{

	public SingleGroup(Item item) {
		for (String label : Edm.getURIpaths().keySet()) {
			String path = Edm.getURIpaths().get(label);
			parseItem(path, item, label);
		}
	}

	public void parseItem(String path, Item item, String label) {
		nu.xom.Nodes nods = item.getNodes(path);
		if (nods != null) {
			for (int i = 0; i < nods.size(); i++) {
				this.add(parseNode(nods.get(i), item, label));
			}
		}
	}

	public Single parseNode(Node node, Item item, String label) {
		Element element = (Element) node;

		Single result = new Single();
		for (int i = 0; i < element.getAttributeCount(); i++) {
			String type;
			Attribute atr = element.getAttribute(i);
			type = atr.getQualifiedName();
			if (type.contains("rdf:resource")) {
				result.label = label;
				result.uri = atr.getValue(); //(label, atr.getValue()); // to EDM type
				break;
			}
		}
		// if (element.getValue()!=null){
		// result.put("free",label +" "+ element.getValue());
		//
		// }
		return result;
	}

}
