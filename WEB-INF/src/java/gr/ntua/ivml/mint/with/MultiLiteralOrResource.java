package gr.ntua.ivml.mint.with;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.*;


public class MultiLiteralOrResource extends MultiLiteral {
	

	public ArrayList<String> resources;

	public ArrayList<String> getResources(){
		return this.resources;
	}
	
	
	
	public void setResources(ArrayList<String> resources) {
		this.resources = resources;
	}

	
	public void addResource(String resource){
		getResources().add(resource);
	}

	public MultiLiteralOrResource() {
		super();
		this.resources = new ArrayList<String>();
	}

	
	public net.sf.json.JSONObject toJson() {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		if (resources.size()!=0) {
			JSONArray array = new JSONArray();
			for (String uri : resources) {
				array.add(uri);

			}
			result.put("uri", array);
		}
		
		if (this.size() != 0) {
			if (this.containsKey("def")) {
				Iterator it = this.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (java.util.Map.Entry) it.next();
					JSONArray jsarray = new JSONArray();
					String label = (String) pair.getKey();
					ArrayList<String> list = (ArrayList<String>) pair
							.getValue();
					for (String entry : list) {
						// jsarray.add(new JSONObject().put(label, entry));
						jsarray.add(entry);
					}
					result.put(label, jsarray);
				}
			}
			else {
				Map.Entry<String, ArrayList<String>> maxEntry = null;

				for (Map.Entry<String, ArrayList<String>> entry : this.entrySet())
				{
				    /*if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
				    {
				        maxEntry = entry;
				    }*/
					if (maxEntry == null){
					maxEntry = entry;
					}
					
					ArrayList<String> list = entry.getValue();
					ArrayList<String> maxlist = maxEntry.getValue();
					if (list.size() > maxlist.size()){ 
						maxEntry = entry;
					}
				}
								
				Iterator it = this.entrySet().iterator();
				while (it.hasNext()) {					
					Map.Entry pair = (java.util.Map.Entry) it.next();
					JSONArray jsarray = new JSONArray();
					String label = (String) pair.getKey();
					ArrayList<String> list = (ArrayList<String>) pair
							.getValue();
					for (String entry : list) {
						// jsarray.add(new JSONObject().put(label, entry));
						jsarray.add(entry);
					}
					result.put(label, jsarray);
				}

				result.put("default",maxEntry.getValue());
			}
		}
		return result;
	}

}

