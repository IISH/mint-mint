package gr.ntua.ivml.mint.with;

import java.util.Iterator;
import java.util.Map;

public class DateObject extends WithSimpleObject{
	public DateObject(){
		
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
