import gr.ntua.ivml.mint.concurrent.*
import gr.ntua.ivml.mint.persistent.*
import org.apache.log4j.Logger;
import gr.ntua.ivml.mint.concurrent.Solarizer;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.client.solrj.response.FacetField.Count
import org.apache.solr.client.solrj.response.FacetField

// Re-sync the solr index

SolrQuery sq = new SolrQuery()
sq.setStart(1)
sq.setQuery("item_id:*")
sq.setFacet(true)
sq.addFacetField("dataset_id")
sq.setParam( "facet.limit", "-1" )
QueryResponse resp = Solarizer.getSolrServer().query(sq)

// We can do that because we used just one facet field 
FacetField ff = resp.getFacetFields().get(0)
List<Count> counts = ff.getValues()
List<Dataset> datasetsInDb = DB.datasetDAO.findAll()
// Dataset ds
counts.sort{ a,b -> b.getCount() <=> a.getCount() }

for( Count c : counts) {
//    println c.getName() + " " + c.getCount()
     long ds_id = Long.parseLong(c.getName())
      ds = DB.datasetDAO.getById(ds_id, false)
      if( ds != null ) {
    	datasetsInDb.remove(ds)
		if( ds.getItemCount() != (int)c.getCount() ) {
            printf( "Dataset %d has %d items in db, %d in index.\n", [ds.dbID, ds.itemCount, c.count])
//			Solarizer.getSolrServer().deleteByQuery("dataset_id:"+ds.getDbID())
//			Solarizer si = new Solarizer(ds)
//			Queues.queue( si, "single" )
//			count++
		}
	} else {
        println( "ds #$ds_id in index, not in db.")
//		Solarizer.getSolrServer().deleteByQuery("dataset_id:" + ds_id)
  //      Solarizer.getSolrServer().commit()
//        count++

	}
}

// store in index datasets that exist only in the DB
for(Dataset dsi : datasetsInDb) {
	Solarizer si = new Solarizer(dsi);
	Queues.queue( si, "single" );
	count++
}

//println "Queued $count datasets for indexing!"

 
  
