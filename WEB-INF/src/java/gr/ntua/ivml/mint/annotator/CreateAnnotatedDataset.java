package gr.ntua.ivml.mint.annotator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gr.ntua.ivml.mint.Custom;
import gr.ntua.ivml.mint.concurrent.Solarizer;
import gr.ntua.ivml.mint.concurrent.Queues.ConditionedRunnable;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.db.LockManager;
import gr.ntua.ivml.mint.persistent.AnnotatedDataset;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.DatasetLog;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.Lock;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.persistent.XmlSchema;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.StringUtils;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;


public class CreateAnnotatedDataset implements Runnable, ConditionedRunnable {

	protected final Logger log = Logger.getLogger(getClass());
	private AnnotatedDataset annotatedDS;
	private List<Lock> aquiredLocks = Collections.emptyList();
	private User user;
	private int annotatedItemsCount = 0;
	
	public CreateAnnotatedDataset(AnnotatedDataset annotatedDS, User user) {
		this.annotatedDS = annotatedDS;
		this.user = user;
	}
	
	public void run() {
		//refresh
		DB.getSession().beginTransaction();
		annotatedDS = (AnnotatedDataset) DB.getDatasetDAO().getById(annotatedDS.getDbID(), false);
		annotatedDS.setItemizerStatus(Dataset.ITEMS_RUNNING);
		DB.commit();
		try {
			copyItemsFromParent();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			indexItems();
			annotatedDS.logEvent("Indexing of items finished");
			releaseLock();
			//output3.write("released?");
			DB.commit();
			DB.closeStatelessSession();
			DB.closeSession();
			DB.commit();
		}
    	log.info("Creation of AnnotatedDataset finished");
	}
	
	private void copyItemsFromParent() {
		final Dataset parentDS = annotatedDS.getParentDataset();
		//copy history of parent to annotatedDS log
		List<DatasetLog> parentLogs = parentDS.getLogs();
		for (DatasetLog parentLog: parentLogs) {
			String parentMsg = parentLog.getMessage();
			User parentUser = parentLog.getUser();
			annotatedDS.logEvent(parentMsg, parentUser);
		}
		annotatedDS.logEvent("Creation of AnnotatedDataset started.");
		annotatedDS.setItemizerStatus(Dataset.ITEMS_RUNNING);
		String oldParentItemizerStatus = parentDS.getItemizerStatus();
		parentDS.setItemizerStatus(Dataset.ITEMS_RUNNING);
		DB.commit();
		//final int parentItemsCount = parentDS.getItemCount();
		ApplyI<Item> annotateItem = new ApplyI<Item>() {
			@Override
			public void apply(Item parentItem) throws Exception {
				Item outputItem = new Item();
				outputItem.setSourceItem(parentItem);
				outputItem.setXml(parentItem.getXml());
				outputItem.setLabel(parentItem.getLabel());
				outputItem.setPersistentId(parentItem.getPersistentId());
				outputItem.setDataset(annotatedDS);
				outputItem.setValid(parentItem.isValid());
				DB.getSession().save(outputItem);
				DB.getSession().flush();
				DB.getSession().evict(outputItem);
				annotatedItemsCount++;
			}	
		};
		try {
			DB.getItemDAO().applyForDataset(parentDS, annotateItem, true);
			annotatedDS.setItemCount(annotatedItemsCount);
			annotatedDS.setItemizerStatus(Dataset.ITEMS_OK);
			annotatedDS.setValidItemCount(parentDS.getValidItemCount());
			parentDS.setItemizerStatus(oldParentItemizerStatus);
			DB.commit();
			annotatedDS.logEvent("Created AnnotatedDataset from " + annotatedDS.getParentDataset().getName());
		} catch( Exception e ) {
			annotatedDS.setItemizerStatus(Dataset.ITEMS_FAILED);
			annotatedDS.logEvent("Creating AnnotatedDataset failed. " + e.getMessage(), StringUtils.stackTrace(e, null));
			parentDS.setItemizerStatus(oldParentItemizerStatus);
			DB.commit();
		}
	}
	
	private void indexItems() {
		//annotatedDS = (AnnotatedDataset) DB.getDatasetDAO().getById(annotatedDS.getDbID(), false);
		int itemsCount = annotatedDS.getItemCount();
		annotatedDS.logEvent("Indexing items for newly created AnnotatedDataset");
		if (itemsCount > 0 && annotatedDS.getItemizerStatus().equals(Dataset.ITEMS_OK)) {
			if( Solarizer.isEnabled()) {
				if( Custom.allowSolarize(annotatedDS)) {
					if (itemsCount <= 1000) {
						List<Item> items = annotatedDS.getItems(0, annotatedDS.getItemCount());
						for (Item item: items) {
							Solarizer sl = new Solarizer(item);
							sl.run();
						}
					}
					else {
						Solarizer sl = new Solarizer(annotatedDS);
						sl.run();
					}
				}
			}
		}
	}
	
	public void releaseLock()  {
		LockManager lm = DB.getLockManager();
		for( Lock l: aquiredLocks) {
			lm.releaseLock(l);
		}
	}
	
	public void setAcquiredLocks(List<Lock> locks ) {
		this.aquiredLocks = locks;
	}

	@Override
	public boolean isRunnable() {
		LockManager lm = DB.getLockManager();
		if ((lm.isLocked(annotatedDS) != null)) 
			return false;
		else {
			Lock l = lm.directLock(user, "createAnnotatedDataset", annotatedDS);
			setAcquiredLocks(Arrays.asList(l));
			return true;
		}
	}

	@Override
	public boolean isUnRunnable() {		
		return false;
	}

	
}
