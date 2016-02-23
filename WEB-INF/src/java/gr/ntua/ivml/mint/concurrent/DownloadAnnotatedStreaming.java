package gr.ntua.ivml.mint.concurrent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.Collections;
import java.util.List;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.db.LockManager;
import gr.ntua.ivml.mint.persistent.AnnotatedDataset;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.Lock;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


public class DownloadAnnotatedStreaming implements Runnable {
	private Dataset ds;
	private TarArchiveOutputStream tos = null;
	private int filesize = 0;
	private List<Lock> aquiredLocks = Collections.emptyList();
	
	public static final Logger log = Logger.getLogger( DownloadAnnotatedStreaming.class );
	

	public DownloadAnnotatedStreaming(Dataset ds, PipedOutputStream pos) {
		this.ds = ds;
		try {	
			GzipCompressorOutputStream gz = new GzipCompressorOutputStream(pos);
			tos = new TarArchiveOutputStream(gz);
			tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
			tos.putArchiveEntry(new TarArchiveEntry(datasetSubdir(ds)+"/"));
			tos.closeArchiveEntry();
		} catch( Exception e ) {
			log.error( "Cannot create tar archive output stream.", e );
		}
	}
	
	public void run() {
		try {
			DB.getSession().beginTransaction();
			ds = DB.getDatasetDAO().getById(ds.getDbID(), false);	
			int itemCount = ds.getItemCount();
			if( ds instanceof AnnotatedDataset) {
				for (Item item: ds.getItems(0, itemCount)) {
					archiveItem(item);
					filesize = (int) tos.getBytesWritten();
				}
			}
			tos.finish();
			tos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			releaseLocks();
			DB.closeSession();
			DB.closeStatelessSession();
			DB.commit();
		}
	}
	
	
	private void archiveItem(Item item) {
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String itemXml = item.getXml();
			if(!itemXml.startsWith("<?xml")) 
				itemXml = "<?xml version=\"1.0\"  encoding=\"UTF-8\" ?>\n" + itemXml;
			IOUtils.write(itemXml, baos, "UTF-8");
			baos.close();
			write(baos.toByteArray(), datasetSubdir(ds)+"/Item_" + item.getDbID() + ".xml");
		} catch( Exception e ) {
			log.error( "Failed to archive item ", e );
		}
	}
	
	private void write( byte[] data, String name) throws IOException  {
		TarArchiveEntry entry = new TarArchiveEntry(name);
		entry.setSize((long) data.length );
		tos.putArchiveEntry(entry);
		tos.write(data, 0, data.length);
		tos.closeArchiveEntry();
	}	
	
	private String datasetSubdir( Dataset ds ) {
		return String.format("%1$ty-%1$tm-%1$td_%1$tH:%1$tM:%1$tS", ds.getCreated());
	}
	
	public int getFilesize() {
		return filesize;
	}
	
	private void releaseLocks() {
		LockManager lm = DB.getLockManager();
		for( Lock l: aquiredLocks)
			lm.releaseLock(l);
	}
	
	public void setAquiredLocks( List<Lock> locks ) {
		this.aquiredLocks = locks;
	}
	
}
