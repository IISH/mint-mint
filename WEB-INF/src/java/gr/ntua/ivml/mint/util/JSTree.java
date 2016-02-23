package gr.ntua.ivml.mint.util;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.mapping.model.Element;
import gr.ntua.ivml.mint.mapping.model.Mappings;
import gr.ntua.ivml.mint.mapping.model.SimpleMapping;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.XmlSchema;
import gr.ntua.ivml.mint.persistent.XpathHolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSTree {
		
	public JSTree() {
	}
	
	public static JSONArray getJSONObject(Dataset ds) {
		JSONArray result = new JSONArray();
		List<? extends TraversableI> children = ds.getRootHolder().getChildren();
		result = JSTree.getJSONObject(children);
		return result;
	}
	
	
	public static JSONArray getJSONObjectFromSchema(Dataset ds) {
		JSONArray result = new JSONArray();
		Element schemaMapRoot = ds.getSchema().getTemplate().getTemplate();
		ArrayList<Element> rootEls = new ArrayList<Element>();
		rootEls.add(schemaMapRoot);
		result = JSTree.getJSONObjectFromChildrenEls(rootEls, "", ds);
		return result;
	}
	
	public static JSONArray getJSONObjectFromChildrenEls(List<Element> elements, String ancestors, Dataset ds) {
		JSONArray result = new JSONArray();
		for (Element el: elements) {
			JSONObject child = getJSONObject(el, ancestors, ds);
			result.add(child);
		}
		return result;
	}

	
	public static JSONArray getJSONObject(List<? extends TraversableI> children) {
		JSONArray result = new JSONArray();
		for(TraversableI t : children) {
			XpathHolder xp = (XpathHolder) t;
			if(!xp.isTextNode()) {
				JSONObject child = JSTree.getJSONObject(xp);
				result.add(child);
			}
		}
		return result;
	}
	
	public static JSONObject getJSONObject(XpathHolder xp) {
		JSONObject result = new JSONObject();
		result.element("text", xp.getNameWithPrefix(true));
		result.element("id", "schema-tree-" + xp.getDbID());
		JSONObject unique = new JSONObject();
		unique.element("unique", "true");
		result.element("metadata", new JSONObject()
			.element("distinct", xp.getDistinctCount())
			.element("count", xp.getCount())
			.element("depth", xp.getDepth())
			.element("xpath", xp.getXpathWithPrefix(true))
			.element("xpathHolderId", xp.getDbID()));
		if ((xp.getTextNode() != null && xp.getTextNode().isUnique()) || (xp.isAttributeNode() && xp.isUnique())) {
			result.element("li_attr", unique);
			((JSONObject) result.get("metadata")).element("unique", "true");
		}
		List<? extends TraversableI> children = xp.getChildren();
		result.element("children", JSTree.getJSONObject(children));
		return result;
	}

	public static JSONObject getJSONObject(Element el, String ancestors, Dataset ds) {
		String treeId = el.getId();
		Long xpathHolderId = new Long(treeId);
		XpathHolder h = ds.getByPath(ancestors + "/" + el.getFullName());
		if (h != null)
		  xpathHolderId = h.getDbID();
		JSONObject result = new JSONObject();
		List<Element> attrAndChildren = el.getAttributes();
		attrAndChildren.addAll(el.getChildren());
		JSONArray childrenArray = getJSONObjectFromChildrenEls(attrAndChildren, ancestors + "/" + el.getFullName(), ds);
		result.element("text", el.getFullName());
		result.element("id", "schema-tree-" + treeId);
		result.element("data", new JSONObject()
			.element("xpath", ancestors + "/" + el.getFullName())
			.element("treeId", treeId)
			.element("xpathHolderId", xpathHolderId)
			.element("minOccurs", el.getMinOccurs())
			.element("maxOccurs", el.getMaxOccurs())
			.element("simpleContentType", el.isSimpleContentType()));
		net.minidev.json.JSONArray enumerations = el.getEnumerations();
		net.minidev.json.JSONObject thesaurus = el.getThesaurus();
		if (enumerations != null) {
			((JSONObject) result.get("data")).element("enumerations", enumerations);
		}
		if (thesaurus != null) {
			((JSONObject) result.get("data")).element("thesaurus", thesaurus);
		}
		result.element("children",childrenArray);
		/*try {
		 File f = new File("/home/eirini/Desktop/tree.txt");
		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		output.write(result.toString());
		output.close();
	} catch (IOException e) {
		System.out.println(e);
	} */
		return result;
	}
}