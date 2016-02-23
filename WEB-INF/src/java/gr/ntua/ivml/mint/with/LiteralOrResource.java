package gr.ntua.ivml.mint.with;

import java.util.Iterator;
import java.util.Map;


public class LiteralOrResource extends Literal {
	
	Resource resource;
	
	
	public LiteralOrResource(){
		super();
	}

	
	public String getResource(){
		if (this.resource != null)
		{	
		
			return this.resource.uri;
		}
		else return null;
	}


	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public net.sf.json.JSONObject toJson() {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		if (resource != null) {

			result.put("uri", getResource());

		}
		Iterator it = this.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (java.util.Map.Entry) it.next();
			result.put((String) pair.getKey(), pair.getValue());
		}

		return result;
	}
		
}
