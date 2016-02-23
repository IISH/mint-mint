package gr.ntua.ivml.mint.with;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MultiLiteral extends HashMap<String, ArrayList<String>> {
	
	
	
	public MultiLiteral() {
	}

	public MultiLiteral(String label) {
		this.put("unknown", new ArrayList<String>(Arrays.asList(label)));
	}

	public MultiLiteral(String lang, String label) {
		if (this.containsKey(lang))
			this.get(lang).add(label);
		else
			this.put(lang, new ArrayList<String>(Arrays.asList(label)));
		
		
		if (lang.equals("en")){
			if  (!this.containsKey("def"))
				this.put("def", new ArrayList<String>(Arrays.asList(label)));		
			}
			else {
				this.get("def").add(label);
			}
	}

	
	public void setMultiLiteral(String lang, String label) {
		if (this.containsKey(lang))
			this.get(lang).add(label);
		else
			this.put(lang, new ArrayList<String>(Arrays.asList(label)));
		
		if (lang.equals("en")){
			if  (!this.containsKey("def"))		
				this.put("def", new ArrayList<String>(Arrays.asList(label)));
			else 
				this.get("def").add(label);
		}

	}
	

	public ArrayList<String> getMultiLiteral(String lang) {
		/*if(Language.ANY.equals(lang)) {
			return this.get(this.keySet().toArray()[0]);
		}
		else*/
			return get(lang.toString());
	}
}
