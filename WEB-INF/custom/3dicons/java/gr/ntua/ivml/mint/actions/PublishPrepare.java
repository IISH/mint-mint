package gr.ntua.ivml.mint.actions;

import gr.ntua.ivml.mint.Publish3DIcons;
import gr.ntua.ivml.mint.concurrent.Queues;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.util.Config;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.convention.annotation.InterceptorRef;

@Results({
	@Result(name="download", type="stream", params={"inputName", "inputStream", "contentType","${contentType}",
			"contentDisposition", "attachment; filename=${filename}", "contentLength", "${filesize}"}),
	@Result(name="input", location="modalResponse.jsp"),
	@Result(name="error", location="modalResponse.jsp"),
	@Result(name="success", location="modalResponse.jsp")
})


public class PublishPrepare extends GeneralAction{
	public static final Logger log = Logger.getLogger( Publish3DIcons.class );

	private String orgId;
	private Organization organization;
	
	private String datasetId;
	private Dataset ds;
	
	private String title;
	private String message;
	
	private String contentType;
	private int filesize = -1;
	private InputStream is;

	 
	static Map<String, String> publicationFiles = new HashMap<String, String>();
	
	@Action("PublishMore")
	  public String execute() throws Exception {
		if (!user.can("publish data", getDs().getOrganization())||!getDs().getOrganization().isPublishAllowed()){
			log.debug("No publishing rights");
			throw new IllegalAccessException( "No publishing rights!" );
		}

		List<PublicationRecord> prlist = Collections.emptyList();
		try {
			Dataset ds = getDs();
			Dataset published = ds.getBySchemaName(Config.get("3dicons.publish.schema"));

			PublicationRecord pr = published.getPublicationRecord();
			if( pr == null ) {
			//Creating a new publication Record
				pr = new PublicationRecord();
			}
			pr.setStartDate(new Date());
			pr.setEndDate(null);
			pr.setOrganization(ds.getOrganization());
			pr.setPublisher(getUser());
			pr.setStatus(PublicationRecord.PUBLICATION_RUNNING);
			pr.setPublishedDataset(published);
			pr.setOriginalDataset(published.getOrigin());
			DB.getPublicationRecordDAO().makePersistent(pr);
			DB.commit();

			prlist = DB.getPublicationRecordDAO().findByOrganization(ds.getOrganization());
			for (PublicationRecord precord : prlist){
				precord.setStatus(PublicationRecord.PUBLICATION_RUNNING);
			}

			DB.commit();
			Publish3DIcons p3d = new Publish3DIcons(ds.getOrganization());
			Queues.queue(p3d, "db");
		} catch(Exception e ) {
			title = "Error";
			message = "There was an Exception\n"+e.getMessage();
			log.error( "Exception during Publication", e );
			return ERROR;
		}
		
		title = "Queued";
		message = prlist.size() + " datasets in process of publishing.";
		return SUCCESS;
	}
	
	@Action(value="DownloadPublicationMore", interceptorRefs= {
		@InterceptorRef(value="ipFilter", params={"configIpPatterns", "more.ipPattern"}),
	    @InterceptorRef("myStack") })
	public String Download() throws Exception {
		if( getDownload())
			return "download";
		else
			return NONE;
	}
	
	@Action("UnpublishMore")
	public String remove() throws Exception {
		//execute un-publication
		if (!user.can("publish data", getDs().getOrganization())||!getDs().getOrganization().isPublishAllowed()){
			log.debug("No publishing rights");
			throw new IllegalAccessException( "No publishing rights!" );
		}

		List<PublicationRecord> prlist = Collections.emptyList();
		try {
			//Dataset ds = DB.getDatasetDAO().findById(Long.parseLong(datasetId), false);		
			
			Dataset ds = getDs();
			//Dataset published = ds.getBySchemaName(Config.get("3dicons.publish.schema"));
			
		//	Dataset published = ds.getBySchemaName(Config.get("3dicons.publish.schema"));
			Dataset published = null;

			
				
			published = ds.getBySchemaName(Config.get("3dicons.old.publish.schema"));	
			
	//		if ((published == null) && ds.getBySchemaName(Config.get("3dicons.publish.schema")) != null){
			if (published == null){
				 published = ds.getBySchemaName(Config.get("3dicons.publish.schema"));
			}
			
			
			
			
			PublicationRecord pr = published.getPublicationRecord();	
					if( pr != null  ) {
						DB.getPublicationRecordDAO().makeTransient(pr);
						DB.commit();
					} else {
						log.info( "Unpublish " + ds.getDbID() + " failed.");
						throw new Exception("The dataset doesn't seem to be published");
					}
		/*	
			Dataset published = null;
			Collection<Dataset> derived  = new ArrayList<Dataset>();
			derived  = ds.getDirectlyDerived();
			//derived.add(ds);
			for (Dataset dset:derived){
				PublicationRecord pr = dset.getPublicationRecord();
				if (pr!=null){
					DB.getPublicationRecordDAO().makeTransient(pr);
					DB.commit();
					published = dset;
					break;
				} else {
				log.info( "Unpublish " + ds.getDbID() + " failed.");
				throw new Exception("The dataset doesn't seem to be published");
				}
			}
*/

		//	PublicationRecord pr = published.getPublicationRecord();	
			/*if( pr != null  ) {
				DB.getPublicationRecordDAO().makeTransient(pr);
				DB.commit();
			} else {
				log.info( "Unpublish " + ds.getDbID() + " failed.");
				throw new Exception("The dataset doesn't seem to be published");
			}*/
			prlist = DB.getPublicationRecordDAO().findByOrganization(ds.getOrganization());
			for (PublicationRecord precord : prlist){
				precord.setStatus(Dataset.PUBLICATION_RUNNING);
			}
			DB.commit();

			Publish3DIcons p3d = new Publish3DIcons(published.getOrganization());
			Queues.queue(p3d, "db");
			title = "Unpublish queued";
			message = "Unpublished dataset by republishing remaining "+ prlist.size() + " datasets queued.";
		} catch( Exception e ) {
			log.error( "Problem during unpublishing.", e );
			title = "Error";
			message = "There was an Exception during unpublish!\n"+e.getMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	public static synchronized void addtoPublicationFilesMap(String orgid,String location){
		PublishPrepare.publicationFiles.put(orgid, location);
	}
	
	private boolean getDownload() {
		if( is== null ) {
			try {		
				String key = Long.toString( getOrg().getDbID());
				String path = PublishPrepare.publicationFiles.get( key );
				File publicationFile = new File(path);

				is = new java.io.FileInputStream(publicationFile) ;
				if( is == null ) return false;
				filesize = (int) publicationFile.length();
			} catch( Exception e ) {
				log.error( "Problem", e );
				return false;
			}
		}
		return true;
	}
	
	private Organization getOrg() throws NumberFormatException{
		if( organization == null ) {
			long id = Long.parseLong(getOrgId());
			organization = DB.getOrganizationDAO().getById(id, false);
		}
		return organization;
	}

	private Dataset getDs( ) throws NumberFormatException {
		if( ds == null ) {
			long id = Long.parseLong(getDatasetId());
			ds = DB.getDatasetDAO().getById(id, false);
		}
		return ds;
	}
	
	public int getFilesize() {
		return filesize;
	}

	public InputStream getInputStream()	{
		return is;
	}
	
	public String getFilename() {
		return "Publication_"+orgId+".tgz";
	}	
	
	public String getContentType(){
		return(contentType);
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
