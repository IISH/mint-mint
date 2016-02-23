package gr.ntua.ivml.mint.with;

import java.util.HashMap;

import gr.ntua.ivml.mint.persistent.Item;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;

public class Media extends HashMap<String, MediaObject> {
	protected final Logger log = Logger.getLogger(getClass());

	public Media(Item item) {
		for (String label : Edm.getMediapaths().keySet()) {
			String path = Edm.getMediapaths().get(label);
			parseItem(path, item, label);
		}

	}

	public void parseItem(String path, Item item, String label) {
		nu.xom.Nodes nods = item.getNodes(path);
		if (nods != null) {
			for (int i = 0; i < nods.size(); i++) {
				if (parseNode(nods.get(i), item, label)!= null)
				this.put(label, parseNode(nods.get(i), item, label));
			}
		}

	}

	public String getType(Item item) {
		String path = Edm.getProvenancepaths().get("edmtype");
		nu.xom.Nodes nods = item.getNodes(path);
		String s = null;

		if (nods.size() >= 0) {
			s = nods.get(0).getValue();

		}
		return s;
	}

	public String getRights(Item item) {
		String path = Edm.getProvenancepaths().get("edmrights");
		nu.xom.Nodes nods = item.getNodes(path);
		String s = null;

		if (nods.size() >= 0) {
			Element el = (Element) nods.get(0);
			Attribute atr = el.getAttribute(0);
			s = atr.getValue();
		}
		return s;
	}

	public String getIsShownAt(Item item) {
		String path = Edm.getProvenancepaths().get("isShownAt");
		nu.xom.Nodes nods = item.getNodes(path);
		String s = null;

		if (nods.size() >= 0) {
			Element el = (Element) nods.get(0);
			Attribute atr = el.getAttribute(0);
			s = atr.getValue();
		}
		return s;
	}

	public MediaObject parseNode(Node node, Item item, String label) {
		Element element = (Element) node;

		MediaObject result = new MediaObject();
		for (int i = 0; i < element.getAttributeCount(); i++) {
		//	log.debug("Media is " + element.toString());
			String type;
			Attribute atr = element.getAttribute(i);

			type = atr.getQualifiedName();
		//	log.debug("media attribute is" + type);
			if (type.contains("rdf:resource")) {
				if (label.equals("isShownBy") || label.equals("hasView")) {
					result.kind = "Original";
					result.parentId = "self";
					
				}
				if (label.equals("object")) {
					if (this.get("isShownBy") != null) {
						MediaObject thumb = new MediaObject();
						MediaObject isSb = this.get("isShownBy");
						thumb.kind = "Thumbnail";
						thumb.parentId = isSb.url;
						thumb.type = this.getType(item);
						thumb.url = atr.getValue();
						thumb.originalRights = this.getRights(item);
						thumb.withRights = this.getRights(item);
						isSb.thumbnail = thumb;
						//this.remove("object");
						return null;
						
					}	
					result.kind = "Original";
					result.parentId = "self";
				}
				result.type = this.getType(item);
				result.url = atr.getValue();
				result.originalRights = this.getRights(item);
				result.withRights = this.getRights(item);

		}
		}
		return result;
	}

	public net.sf.json.JSONArray toJson() {
		net.sf.json.JSONArray result = new JSONArray();
		for (String label : this.keySet()) {
			//log.debug(label);
			MediaObject temp = this.get(label);
		//	log.debug(temp.kind);
			JSONObject media = new JSONObject();
			media.put(temp.kind, temp.toJson());
			if (temp.thumbnail!=null){
				media.put(temp.thumbnail.kind,temp.thumbnail.toJson());
			}
			result.add(media);
		}

		return result;
	}

}
