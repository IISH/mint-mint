package gr.ntua.ivml.mint.actions;
import gr.ntua.ivml.mint.concurrent.DownloadAnnotatedStreaming;
import gr.ntua.ivml.mint.concurrent.Queues;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.AnnotatedDataset;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.ocpsoft.pretty.time.PrettyTime;

/**
 * Action for download page.
 
 */

@Results({
	  @Result(name="download", type="stream", params={"inputName", "inputStream", "contentType", "application/x-tar", 
			  "contentDisposition", "attachment; filename=${filename}"})
	  })



public class DownloadAnnotated extends GeneralAction {

	protected final Logger log = Logger.getLogger(getClass());
	private String contentType = "application/x-tar";
	private InputStream inputStream;
	private Dataset dataset;
	private long datasetId;
	private int filesize = -1;
	private PipedOutputStream pos;
	private String filename="";
	
	/**
	 * 
	 */
	private boolean getDownload() {
		Dataset ds = getDataset();
		try {
			pos = new PipedOutputStream();
			inputStream = new PipedInputStream(pos);
			DownloadAnnotatedStreaming das = new DownloadAnnotatedStreaming(ds, pos);
			Queues.queue(das, "now");
		} catch(Exception e) {
			log.error("Problem", e);
			return false;
		}
		return true;
	}
	

	public InputStream getInputStream()	{
		if (inputStream == null) getDownload();
		return inputStream;
	}
	
	/*public int getFilesize() {
		if (filesize == -1) 
			getDownload();
		return das.getFilesize();
	}*/
	
	public String getFilename(){
		if (filename.isEmpty()) {
			Dataset ds = getDataset();
			String name = StringUtils.getDefault(ds.getName(), ds.getDbID());
			name = name.replace(' ' , '_');
			filename = name + ".tgz";
		}
		return filename;
	}

	
	public void setContentType(String ct) {
	   this.contentType = ct;	
	}
	
	public String getContentType() {
		return contentType;
	}

	/**
	 * Use this as the parameter of the dataset where you want to download from.
	 * GzipCompressorOutputStream
	 * @param id
	 */
	public void setDatasetId(String id) {
		datasetId = -1l;
		try {
			datasetId = Long.parseLong(id);
		} catch( Exception e) {}
	}
	
	public String getDatatsetId() {
		return Long.toString( datasetId );
	}
	
	public Dataset getDataset() {
		if( dataset == null ) {
			dataset = DB.getDatasetDAO().getById(datasetId, false);
		}
		return dataset;
	}

	public boolean hasDownload() {
		return getDownload();
	}
		
	@Action(value="DownloadAnnotated")
	public String execute() throws Exception {
		Dataset ds = getDataset();
		// check reading rights
		if( ds != null ) {
			if( getUser().can( "view data", ds.getOrganization() )) {
				return "download";
			}
			else return NONE;
		}
		return ERROR;
	}
	
	

}
	  
