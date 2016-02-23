package gr.ntua.ivml.mint.actions;

import gr.ntua.ivml.mint.PublishCarareDsi;
import gr.ntua.ivml.mint.concurrent.Queues;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.persistent.XmlSchema;
import gr.ntua.ivml.mint.util.Config;


import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({
	@Result(name="download", type="stream", params={"inputName", "inputStream", "contentType","${contentType}",
			"contentDisposition", "attachment; filename=${filename}", "contentLength", "${filesize}"}),
	@Result(name="input", location="modalResponse.jsp"),
	@Result(name="error", location="modalResponse.jsp"),
	@Result(name="success", location="modalResponse.jsp")
})


public class PublishPrepare extends GeneralAction{
	public static final Logger log = Logger.getLogger( PublishPrepare.class );

	private Organization organization;
	
	private String datasetId;
	private Dataset ds;
	
	private String title;
	private String message;
	private String pub;
	
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

		try {
			Dataset ds = getDs();
			Dataset published = latestWithValidItems(ds);

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

			PublishCarareDsi pubLo = new PublishCarareDsi(published);
			Queues.queue(pubLo, "db");
		} catch(Exception e ) {
			title = "Error";
			message = "There was an Exception\n"+e.getMessage();
			log.error( "Exception during Publication", e );
			return ERROR;
		}
		
		title = "Queued";
		message = "Dataset in process of publishing.";
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

		try {
			// no MORE protocol given yet, just set to unpublished
			Dataset origin = getDs().getOrigin();
			List<PublicationRecord> lpr = DB.getPublicationRecordDAO().findByOriginalDataset(origin);

			if( lpr.size() == 1 ) {
				DB.getPublicationRecordDAO().makeTransient(lpr.get(0));
			} else if( lpr.size()>1 ) {
				// unexpected 
				log.info( "More than one published set in here?" );
				for( PublicationRecord pr: lpr ) {
					DB.getPublicationRecordDAO().makeTransient(pr);
				}
			} else {
				log.error( "No Publication during unpublishing of Dataset #" + origin.getDbID());
				title = "Error";
				message = "No actual Publication was found";
				return ERROR;
			}
			title = "Unpublished";
			message = "Dataset unpublished";			
		} catch( Exception e ) {
			log.error( "Problem during unpublishing.", e );
			title = "Error";
			message = "There was an Exception during unpublish!\n"+e.getMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	public static synchronized void addtoPublicationFilesMap(String pubname,String location){
		PublishPrepare.publicationFiles.put(pubname, location);
	}
	
	private boolean getDownload() {
		if( is== null ) {
			try {		
				String key = getPub();
				String path = PublishPrepare.publicationFiles.get( key );
				if( path == null ) return false;
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
	
	/**
	 * Find the latest dataset in allPublishSchemas with valid items
	 * @param allSchemas
	 * @return
	 */
	public static Dataset latestWithValidItems( Dataset origin ) {
		String allPublishSchemas = Config.get("cararedsi.publish.schemas");
		
		ArrayList<Dataset> dsl = new ArrayList<Dataset>();
		dsl.add(origin);
		dsl.addAll( origin.getDerived());
		String[] publishSchemas = allPublishSchemas.split( "," );
		// sort reverse by lastModified
		Collections.sort( dsl, new Comparator<Dataset>() {
			@Override
			public int compare(gr.ntua.ivml.mint.persistent.Dataset o1,
					gr.ntua.ivml.mint.persistent.Dataset o2) {
				return o1.getLastModified().compareTo( o2.getLastModified())*-1;
			}
		} );
		// any valid items in publish schemas?
		for( Dataset ds: dsl ) {
			if( ds.getValidItemCount() > 0 ) {
				XmlSchema schema = ds.getSchema();
				if( schema != null ) {
					String name = schema.getName();
					for( String schemaName: publishSchemas ) {
						if( name.equals( schemaName.trim())) return ds; 
					}
				}
			}
		}
		return null;
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
		return getPub()+".tgz";
	}	
	
	public String getContentType(){
		return(contentType);
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public String getPub() {
		return pub;
	}

	public void setPub(String pub) {
		this.pub = pub;
	}
	
}
