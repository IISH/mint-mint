
package gr.ntua.ivml.mint.actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.AnnotatedDataset;
import gr.ntua.ivml.mint.persistent.DataUpload;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.persistent.Transformation;
import gr.ntua.ivml.mint.view.Import;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({
	@Result(name="error", location="datasetoptions.jsp"),
	@Result(name="input", location="datasetoptions.jsp"),
	@Result(name="success", location="datasetoptions.jsp")
})

public class DatasetOptions extends GeneralAction{

	protected final Logger log = Logger.getLogger(getClass());
	private long organizationId;
	private Organization o;
	private String action="";
	private String actionmessage="";
	private long uploadId=-1;
	private long userId=-1;
	private User u=null;
	private List<Dataset> transformations;
	
	public boolean transformed=false;
	
	public gr.ntua.ivml.mint.view.Import current;
	
	@Action(value="DatasetOptions")
	public String execute() throws Exception {
		log.debug("DatasetOptions controller");
		setCurrent();
	    boolean success=false;
		if (this.action.equalsIgnoreCase("delete")){
			if (this.current.isLocked(getUser(), getSessionId())) {
				addActionError("The dataset is currently in use and cannot be deleted.");
				return ERROR;
			}
			if (current.getDu() instanceof AnnotatedDataset) {
				boolean successAnnDs = false;
				Dataset parent = ((AnnotatedDataset) current.getDu()).getParentDataset();
				successAnnDs = DB.getDatasetDAO().makeTransient(current.getDu());
				if (successAnnDs) {//delete parent Dataset
					success = DB.getDatasetDAO().makeTransient(parent);
				}
			} else
				success = DB.getDatasetDAO().makeTransient(current.getDu());
				DB.commit();
			if (success) {
				return SUCCESS;
			} else {
				refreshUser();	
				addActionError("Unable to delete selected dataset. Dataset is in use!");
				return ERROR;
			}
		}
		else if (this.action.equalsIgnoreCase("undoAnnotations")){
			refreshUser();	
			if (current.getDu() instanceof AnnotatedDataset)
				System.out.println("AnnotatedDataset");
			else if (current.getDu() instanceof Transformation)
				System.out.println("Transformation");
			else 
				System.out.println("Upload");
			if (this.current.isLocked(getUser(), getSessionId())) {
				addActionError("The dataset is currently in use and annotations cannot be undone.");
				return ERROR;
			}
			if (!(current.getDu() instanceof AnnotatedDataset)) {
				addActionError("Something went wrong. The dataset has not been annotated.");
				return ERROR;
			}
			else {//delete AnnotatedDataset
				success = (DB.getDatasetDAO().makeTransient(current.getDu()));
				DB.commit();
				if (success)
					return SUCCESS;
				else {
					addActionError("Unable to delete AnnotatedDataset.");
					return ERROR;
				}
			}
		}
		else {
			addActionError("Action " + this.action + " is not supported.");
			return ERROR;
		}
	}
	
	@Action("DatasetOptions_input")
	@Override
	public String input() throws Exception {
	
		log.debug("DatasetOptions input controller");
		setCurrent();
		System.out.println(this.getActionErrors());
		this.setActionErrors(this.getActionErrors());
	
		return "input";
	}
	
	public void setTransformed(boolean transformed){
		this.transformed=transformed;
	}
	
	public boolean getTransformed(){
		return transformed;
	}
	
	
	
    public String getActionmessage(){
		  return(actionmessage);
	}
    
    public void setAction(String action){
		this.action=action;
	}
		  
	public void setActionmessage(String message){
		  this.actionmessage=message;
	}
	
	public List<Dataset> getTransformations(){
		Dataset du=DB.getDatasetDAO().findById(this.getUploadId(), false);
		List<Dataset> templist=new ArrayList<Dataset>();
		for(Dataset ds:du.getDerivedTransfLeaves()) {
			if(ds instanceof gr.ntua.ivml.mint.persistent.Transformation || 
					ds instanceof AnnotatedDataset) {
				templist.add(ds);
			}
		}
		transformations=templist;
			
		return transformations;
	}
	
	public void setCurrent(){
		Dataset du=DB.getDatasetDAO().findById(this.getUploadId(), false);		
		current=new Import(du);
	}
	  
	public String getDatasetType(){
		Dataset ds=DB.getDatasetDAO().getById(this.uploadId, false);
		 if(ds instanceof DataUpload){
	    	return "Data Upload";
	    } else if (ds instanceof Transformation){
	    	return "Transformation";
	    } else if (ds instanceof AnnotatedDataset){
	    	//Dataset ps = ((AnnotatedDataset) ds).getParentDataset();
	    	return "AnnotatedDataset";
	    } else
	    	return "Unknown";
	}
	
	
	public String getNotAnnDatasetType(){
		Dataset ds=DB.getDatasetDAO().getById(this.uploadId, false);
		 if(ds instanceof DataUpload){
	    	return "Data Upload";
	    } else if(ds instanceof Transformation){
	    	return "Transformation";
	    } else if(ds instanceof AnnotatedDataset){
	    	Dataset ps = ((AnnotatedDataset) ds).getParentDataset();
	    	if (ps instanceof Transformation)
	    		return "Transformation";
	    	else if (ps instanceof DataUpload)
	    		return "Data Upload";
	    	else
	    		return "Unknown";
	    } else
	    	return "Unknown";
	}
	
	public boolean hasStats(){
		Dataset du=DB.getDatasetDAO().findById(this.getUploadId(), false);
		return (( du != null ) && du.getStatisticStatus().equals( Dataset.STATS_OK));
	}
	
	public Import getCurrent(){
		return current;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
		this.o=DB.getOrganizationDAO().findById(organizationId, false);
	}

	public Organization getO(){
		return this.o;
	}

	public long getUploadId() {
		return uploadId;
	}

	public void setUploadId(long uploadId) {
		this.uploadId = uploadId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
		this.u=DB.getUserDAO().findById(userId, false);
		
	}
	
	public User getU(){
		return this.u;
	}
	
	public Dataset getDu(){
		Dataset du;
		
		return DB.getDatasetDAO().getById(this.getUploadId(), false);
	}
		
}