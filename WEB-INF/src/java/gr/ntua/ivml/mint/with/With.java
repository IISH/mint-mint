package gr.ntua.ivml.mint.with;

import gr.ntua.ivml.mint.persistent.Item;



import org.apache.log4j.Logger;

public class With {

	protected final Logger log = Logger.getLogger(getClass());
 

	
	   Provenance provenance;
	   Media media;
	   DescriptiveData descriptiveData;
	

	
	public  With(Item item){
		this.descriptiveData = new DescriptiveData(item);
		this.provenance = new Provenance(item);
		this.media = new Media(item);
		

	}
	

	
	
	public net.sf.json.JSONObject toJson() {
		net.sf.json.JSONObject result = new  net.sf.json.JSONObject();
		
		result.put("descriptiveData", this.descriptiveData.toJson());
	
		result.put("media",this.media.toJson());
		
		result.put("provenance",this.provenance.toJson());
		
		
		log.debug("descriptive is : ");
		log.debug(this.descriptiveData.toJson());
		log.debug("media is: ");
		log.debug(this.media.toJson());
		log.debug("provenance is : ");
		log.debug(this.provenance.toJson());
		return result;
	}
	

	
	
	
	
	
	
	
	
	
	
}
