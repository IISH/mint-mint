package gr.ntua.ivml.mint.actions;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.util.StringUtils;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * pass in the EUscreen id and get forwarded to the right item.
 * @author stabenau
 *
 */

@Results({
	  @Result(name="success", type="redirect", location="${url}"),
})

public class LinkItem extends GeneralAction {
	public String itemId;
	public String euscreenId;
	public String datasetId;
	public String url;

	@Action(value="Link")
	public String execute() {
		if( !StringUtils.empty( euscreenId)) {
			// find the itemId 
			// construct the location Home?itemId=...
			List<Item> items = DB.getItemDAO().findAll();
		}
		return SUCCESS;
	}
	
	public String getUrl() {
		return url;
	}
	
//
//  Getter setter
//
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getEuscreenId() {
		return euscreenId;
	}
	public void setEuscreenId(String euscreenId) {
		this.euscreenId = euscreenId;
	}
	public String getDatasetId() {
		return datasetId;
	}
	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}
	
	
}
