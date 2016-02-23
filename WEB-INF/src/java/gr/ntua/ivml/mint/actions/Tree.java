package gr.ntua.ivml.mint.actions;

import gr.ntua.ivml.mint.db.DB;

import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.util.JSTree;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({
	@Result(name="error", location="json.jsp"),
	@Result(name="success", location="json.jsp")
})

public class Tree extends GeneralAction {

	public static final Logger log = Logger.getLogger(Tree.class ); 
	public JSONObject json;
	public long dataUploadId;
	public boolean annotatorMode = false;

	@Action(value="Tree")
	public String execute() {
		json = new JSONObject();
		try {
			Dataset du = DB.getDatasetDAO().findById(this.getDataUploadId(), false);
			if(du != null) {
				if (!annotatorMode) {
					json.element("tree", JSTree.getJSONObject(du));
				}
				else {
					json.element("tree", JSTree.getJSONObjectFromSchema(du));
					//json.element("tree", JSTree.getJSONObjectEnriched2(du));
				}
			}
		} catch( Exception e ) {
			json.element( "error", e.getMessage());
			log.error( "No values", e );
		}
		return SUCCESS;
	}
	
	public long getDataUploadId() {
		return dataUploadId;
	}

	public void setDataUploadId(long dataUploadId) {
		this.dataUploadId = dataUploadId;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public JSONObject getJson() {
		return json;
	}
}
