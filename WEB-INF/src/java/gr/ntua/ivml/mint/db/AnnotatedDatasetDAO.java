package gr.ntua.ivml.mint.db;


import gr.ntua.ivml.mint.concurrent.Solarizer;
import gr.ntua.ivml.mint.persistent.AnnotatedDataset;
import gr.ntua.ivml.mint.persistent.DataUpload;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.Counter;
import gr.ntua.ivml.mint.util.StringUtils;

import org.apache.log4j.Logger;

public class AnnotatedDatasetDAO extends DAO<AnnotatedDataset, Long> {
	public static final Logger log = Logger.getLogger(AnnotatedDatasetDAO.class);
	
	public AnnotatedDataset getByParent(Dataset ds) {
		return (AnnotatedDataset) getSession().createQuery( "from AnnotatedDataset where parentDataset=:ds order by created DESC")
			.setEntity("ds", ds)
			.uniqueResult();
	}
	
	public void cleanup() {
		final Counter c = new Counter().set(0);
		
		ApplyI<AnnotatedDataset> ap = new ApplyI<AnnotatedDataset>() {
			
			@Override
			public void apply(AnnotatedDataset ann) throws Exception {
				
				try {
					boolean modified = false;
					if( StringUtils.isIn( ann.getLoadingStatus(), Dataset.LOADING_HARVEST, Dataset.LOADING_UPLOAD )) {
						ann.setLoadingStatus(Dataset.LOADING_FAILED);
						ann.logEvent( "Loading interrupted, set to FAILED!");
						modified = true;
					}
					if( ann.getItemizerStatus().equals( Dataset.ITEMS_RUNNING )) {
						ann.setItemizerStatus(Dataset.ITEMS_FAILED);
						ann.logEvent( "Annotation interrupted, set to FAILED!" );
						DB.getItemDAO().delete("datasetId="+ann.getDbID());
						modified = true;
					}
					if( ann.getSchemaStatus().equals( Dataset.SCHEMA_RUNNING ))  {
						ann.setSchemaStatus(Dataset.SCHEMA_FAILED);
						ann.logEvent( "Schema validation interrupted, set to FAILED!" );
						modified = true;
					}
					if( ann.getStatisticStatus().equals( Dataset.STATS_RUNNING)) {
						ann.setStatisticStatus(Dataset.STATS_FAILED);
						ann.logEvent( "Stats building interrupted, set to FAILED!" );
						DB.getXpathStatsValuesDAO().delete("dataset="+ann.getDbID());
						DB.getXpathHolderDAO().delete("dataset="+ann.getDbID());
						modified = true;
					}
					if( modified ) c.inc();
					DB.commit();
				} catch( Exception e ) {
					ann.logEvent("General cleanup problem: " + e.getMessage(), StringUtils.stackTrace(e, null));
					log.error( "General cleanup problem!", e );
				}
			}
		};
		try {
			onAll(ap, null, true);
			log.info( "Cleaned " + c.get() + " AnnotatedDatasets.");
		} catch( Exception e ) {
			log.error( "Unhandled Exception " , e);
		}
	}
	
	@Override
	public boolean makeTransient( AnnotatedDataset ad ) {
		String dsId = ad.getDbID().toString();
		if(super.makeTransient(ad)) {
			if( Solarizer.isEnabled()) {
				try {
				Solarizer.delete("dataset_id:"+dsId);
				Solarizer.commit();
				} catch( Exception e ) {
					log.error( "Solr delete annotatedDataset #"+dsId+ " failed!");
				}
			}
			return true;
		}
		else
			return false;
	}

}
