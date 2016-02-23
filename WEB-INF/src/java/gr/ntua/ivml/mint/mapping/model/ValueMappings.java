package gr.ntua.ivml.mint.mapping.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import gr.ntua.ivml.mint.mapping.JSONHandler;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import gr.ntua.ivml.mint.rdf.thesauri.SKOSThesaurus;
import gr.ntua.ivml.mint.util.Config;

public class ValueMappings extends JSONHandler {
	/** 
	 * Create an empty value mappings handler.
	 */
	public ValueMappings() {
		super(new JSONArray());
	}
	
	/**
	 * Create a value mappings handler with contents of provided value mappings json object.
	 * @param mappings value mappings json object.
	 */
	public ValueMappings(JSONArray mappings) {
		super(mappings);
	}

	public static final String VALUE_MAPPINGS_INPUT = "input";
	public static final String VALUE_MAPPINGS_OUTPUT = "output";
	
	public void set(String input, String output, String thesLang) {
		JSONObject map = null;
		
		Iterator<?> i = this.array.iterator();
		while(i.hasNext()) {
			JSONObject m = (JSONObject) i.next();
			if(m.get("input").toString().trim().equals(input.trim())) {
				map = m;
				break;
			}
		}
		
		if(map == null) {
			map = new JSONObject();
			map.put("input", input.trim());
			map.put("output", output);
			if (!thesLang.equals("noThesaurus")) {
				String repository = "";
				if(Config.has("mint.rdf.thesauri"))
				    repository = Config.get("mint.rdf.thesauri");
				else
				    repository = Config.get("mint.rdf.repository");

				SKOSThesaurus skos = new SKOSThesaurus(repository,null,null,null,null,true);
				String englishLabel = skos.getPrefLabel(output, thesLang);

				map.put("thesPrefLabel", englishLabel);
			}
			this.array.add(map);
		} else {
			map.put("output", output);
		}
		sortValues();
	}

	public void sortValues() {
		// sorting json_array of valueMappings
		Comparator<Object> c = new Comparator<Object>() {

         	@Override
			public int compare(Object arg0, Object arg1) {
				return ((JSONObject)arg0).get("input").toString().toLowerCase()
						.compareTo(((JSONObject)arg1).get("input").toString().toLowerCase());
			}
        };
		Collections.sort(this.array, c);
	}
	
	public void remove(String input) {
		JSONObject map = null;
		Iterator<?> i = this.array.iterator();
		while(i.hasNext()) {
			JSONObject m = (JSONObject) i.next();
			if(m.get("input").toString().equals(input)) {
				map = m;
				break;
			}
		}
		
		if(map != null) {
			this.array.remove(map);
		}
	}
	
	public void trimValues() {
		Iterator<?> it = this.array.iterator();
		while(it.hasNext()) {
			JSONObject m = (JSONObject) it.next();
			String inputNew = m.get("input").toString().trim();
			m.put("input", inputNew);
		}
	}
	
	public void removeDuplicates() {
		Set<String> valuesSet = new HashSet<String>();
		List<JSONObject> toRemove = new ArrayList<JSONObject>();
		Iterator<?> it = this.array.iterator();
		if(hasDuplicates(valuesSet, it)) {
			it = this.array.iterator();
			while(it.hasNext()) {
				JSONObject m = (JSONObject)it.next();
				String inputKey = m.get("input").toString();
				if(valuesSet.contains(inputKey)) {
					valuesSet.remove(inputKey);
				} else {
					toRemove.add(m);
				}
			}
			for(JSONObject jo : toRemove) {
				this.array.remove(jo);
			}
		}
	}
	
	public boolean hasDuplicates(Set<String> valuesSet, Iterator<?> it) {
		while(it.hasNext())
			valuesSet.add(((JSONObject)it.next()).get("input").toString());
		
		if( valuesSet.size() != this.array.size())
			return true;
		return false;
	}
}