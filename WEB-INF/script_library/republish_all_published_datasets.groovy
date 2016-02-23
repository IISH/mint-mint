import java.util.HashMap;
import gr.ntua.ivml.mint.Publication;
import gr.ntua.ivml.mint.util.Config;
import gr.ntua.ivml.mint.util.Tuple;
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.concurrent.*;


// Republish all EDM published datasets
/* This script 
	a) unpublishes,
	b) removes the target schema dataset,
	c) re-transforms to target, 
	d) does the prepare for publish process,
	e) re-publishes.
*/
class MyRunnable implements Runnable, Queues.ConditionedRunnable {
    Dataset ds;
    public MyRunnable( Dataset ds ) {
        this.ds = ds     
    }

    public boolean isRunnable() {
        Dataset localds = DB.datasetDAO.getById( ds.getDbID(), false );
        Dataset derivedDs = localds.getBySchemaName("EDM");

        if( derivedDs == null ) return false;

        return( derivedDs.getSchemaStatus().equals( Dataset.SCHEMA_OK))
    }

    public boolean isUnRunnable() {
        Dataset localds = DB.datasetDAO.getById( ds.getDbID(), false );
        Dataset derivedDs = localds.getBySchemaName("EDM");

        if( derivedDs == null ) return false;

        return( derivedDs.getSchemaStatus().equals( Dataset.SCHEMA_FAILED))
    };

    public void run() {
        ds = DB.datasetDAO.getById( ds.getDbID(), false );
        ds.publish();
    }
}


targetSchema = "EDM";
for( DataUpload du: DB.dataUploadDAO.findAll() ) {

    if( du.isPublished()) {
        // unpublish
        // remove target schema dataset
        // prepare for publication
        // publish

        HashMap<String, String> map = new HashMap<String, String>();
        map.put( "provider", Config.get("mint.provider"));
        du.unpublish();
        DB.commit();
        Dataset ds = du.getBySchemaName(targetSchema)
        DB.datasetDAO.makeTransient(ds)
        DB.commit()
        Publication p = du.getOrganization().getPublication();

        p.prepareForPublication(du, map, du.getCreator());
        // and now  conditioned runnable    
        def myRunnable = new MyRunnable( du );
        Queues.queue(myRunnable, "db");
    }

}