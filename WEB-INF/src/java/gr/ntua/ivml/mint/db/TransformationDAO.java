package gr.ntua.ivml.mint.db;


import gr.ntua.ivml.mint.concurrent.Solarizer;
import gr.ntua.ivml.mint.persistent.DataUpload;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.Transformation;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.Counter;
import gr.ntua.ivml.mint.util.StringUtils;

import java.util.List;

import org.apache.log4j.Logger;

public class TransformationDAO extends DAO<Transformation, Long> {
	public static final Logger log = Logger.getLogger(TransformationDAO.class);
	public List<Transformation> findByUpload( DataUpload du ) {
		return getSession().createQuery("from Transformation where parentDataset=:du")
		.setEntity("du", du)
		.list();
	}
	

	public List<Transformation> getByParent( Dataset ds ) {
		return getSession().createQuery( "from Transformation where parentDataset=:ds order by created DESC")
			.setEntity("ds", ds)
			.list();
	}
	
	public void cleanup() {
		final Counter c = new Counter().set(0);
		
		ApplyI<Transformation> ap = new ApplyI<Transformation>() {
			
			@Override
			public void apply(Transformation tr) throws Exception {
				
				try {
					boolean modified = false;
					if( StringUtils.isIn( tr.getLoadingStatus(), Dataset.LOADING_HARVEST, Dataset.LOADING_UPLOAD )) {
						tr.setLoadingStatus(Dataset.LOADING_FAILED);
						tr.logEvent( "Loading interrupted, set to FAILED!");
						modified = true;
					}
					if( tr.getItemizerStatus().equals( Dataset.ITEMS_RUNNING )) {
						tr.setItemizerStatus(Dataset.ITEMS_FAILED);
						tr.logEvent( "Transformation interrupted, set to FAILED!" );
						DB.getItemDAO().delete("datasetId="+tr.getDbID());
						modified = true;
					}
					if( tr.getSchemaStatus().equals( Dataset.SCHEMA_RUNNING ))  {
						tr.setSchemaStatus(Dataset.SCHEMA_FAILED);
						tr.logEvent( "Schema validation interrupted, set to FAILED!" );
						modified = true;
					}
					if( tr.getStatisticStatus().equals( Dataset.STATS_RUNNING)) {
						tr.setStatisticStatus(Dataset.STATS_FAILED);
						tr.logEvent( "Stats building interrupted, set to FAILED!" );
						DB.getXpathStatsValuesDAO().delete("dataset="+tr.getDbID());
						DB.getXpathHolderDAO().delete("dataset="+tr.getDbID());
						modified = true;
					}
					if( modified ) c.inc();
					DB.commit();
				} catch( Exception e ) {
					tr.logEvent("General cleanup problem: " + e.getMessage(), StringUtils.stackTrace(e, null));
					log.error( "General cleanup problem!", e );
				}
			}
		};
		try {
			onAll( ap, null, true );
			log.info( "Cleaned " + c.get() + " Transformations.");
		} catch( Exception e ) {
			log.error( "Unhandled Exception " , e);
		}
	}
	
	/**
	 * This method overrides method from  parent class
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public boolean makeTransient(Transformation tr) {
		String dsId = tr.getDbID().toString();
		if(super.makeTransient(tr)) {
			if( Solarizer.isEnabled()) {
				try {
				Solarizer.delete("dataset_id:"+dsId);
				Solarizer.commit();
				} catch( Exception e ) {
					log.error( "Solr delete transformtion #"+dsId+ " failed!");
				}
			}
			return true;
		}
		else
			return false;
	}
}
