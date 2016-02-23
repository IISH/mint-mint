package gr.ntua.ivml.mint.with;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minidev.json.JSONObject;

public class Literal extends HashMap<String, String> {
	
	public Literal(){
		
	}

	public Literal(String lang,String label){
		this.put(lang, label );
	}
	
	
	public Literal(String label){
		this.put("unknown",label);
	}
	
	
	public String getLiteral(String lang){
		return this.get(lang);
	}
	
	public void setLiteral(String lang,String label){
		this.put(lang,label);
	}
	
	
	public net.sf.json.JSONObject toJson(){
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		
		Iterator it  = this.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry pair  = (java.util.Map.Entry) it.next();
			result.put((String) pair.getKey(),pair.getValue() );	
		}
		return result;
	}
			
}
