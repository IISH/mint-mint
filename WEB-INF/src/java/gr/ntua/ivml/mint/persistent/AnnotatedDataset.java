package gr.ntua.ivml.mint.persistent;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

public class AnnotatedDataset extends Dataset implements Lockable {
public static final Logger log = Logger.getLogger(AnnotatedDataset.class);

	//parent is usually a transformation, but could also 
	//be a dataUpload with a defined schema.
	private Dataset parentDataset;
	

	public static AnnotatedDataset fromDataset(Dataset source ) {
		AnnotatedDataset ann = new AnnotatedDataset();
		ann.init(source.getCreator());
		ann.setName(source.getName() + "_annotated");
		ann.setParentDataset(source);
		ann.setCreated(new Date());
		ann.setOrganization(source.getOrganization());
		ann.setSchema(source.getSchema());
		ann.setItemizerStatus(source.getItemizerStatus());
		return ann;		
	}
	
	/**
	 * If the json string or xsl changed, the annotated dataset is stale ...
	 * @return
	 */
	public boolean isStale() {
		if( getMapping() != null ) {
			String newMapping = null;
			if(getMapping().isXsl()) {
				newMapping = getMapping().getXsl(); 
			} else {
				newMapping = getMapping().getJsonString();
			}
			
			if(newMapping !=null ){
				try {
					return !newMapping.equals(getJsonMapping());
				} catch( Exception e ) {
					log.error( "on is stale check", e );
				}
			}
		}
		return false;
	}
	
	
	@Override
	public String getLockname() {
		// TODO Auto-generated method stub
		String result = "";
		if( getParentDataset() != null ) result += getParentDataset().getCreated().toString() + " Dataset.";
		return result;
	}
	
	
	@Override
	public void derivedFrom( List<Dataset> result ) {
		result.add( getParentDataset() );
		getParentDataset().derivedFrom(result);
	}
	
	@Override
	public Dataset getOrigin( ) {
		return getParentDataset().getOrigin();
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject res = super.toJSON();	
		res.put("invalidItems",getInvalidItemCount());			
		res.put("validItems", getValidItemCount());

		if (parentDataset !=null){
			res.put("parentDataset",parentDataset.getName());
			
			if (parentDataset instanceof Transformation)   {
				res.put("targetSchema",((Transformation) parentDataset).getMapping().getTargetSchema().getName());
				
			}
			if (parentDataset instanceof DataUpload)   {
				if ( ((DataUpload) parentDataset).getSchema().getName() != null)
				res.put("targetSchema",((DataUpload) parentDataset).getSchema().getName());
			}
		
			JSONObject org = new JSONObject();
			org.put( "dbID", parentDataset.getOrganization().getDbID());
			org.put( "name", parentDataset.getOrganization().getName());
			res.put( "organization", org);
		}
		else {
			JSONObject org = new JSONObject();
			org.put( "dbID", getOrganization().getDbID());
			org.put( "name", getOrganization().getName());
			res.put( "organization", org);
			res.put("parentDataset", getName());
		}

		
		res.put( "type", "AnnotatedDataset");
		return res;
	
	}

	//  AnnotatedDataset state is stored in itemizer state 

	public String getAnnotatedStatus() {
		return getItemizerStatus();
	}
	
	public void setAnnotatedStatus( String state ) {
		setItemizerStatus(state);
	}
	
	public Dataset getParentDataset() {
		return parentDataset;
	}
	
	public void setParentDataset(Dataset parentDataset) {
		this.parentDataset = parentDataset;
	}
    
	public Mapping getMapping() {
		if (parentDataset instanceof Transformation) {
			Transformation t = (Transformation) parentDataset;
			return t.getMapping();
		}
		else return null;
	}
	
	public String getJsonMapping() {
		if (parentDataset instanceof Transformation) {
			Transformation t = (Transformation) parentDataset;
			return t.getJsonMapping();
		}
		else return null;
	}
}
