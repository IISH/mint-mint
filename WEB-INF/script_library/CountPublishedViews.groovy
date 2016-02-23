import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.util.*
import nu.xom.Nodes

import com.opensymphony.xwork2.util.TextParseUtil

def prs = DB.publicationRecordDAO.findAll()
def publishedSchemaNames= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.aggregate.schema"));

Map orgPrs = prs.findAll{ publishedSchemaNames.contains( it.publishedDataset.schema.name ) }.groupBy{ it.organization }

Map allIds = [:]


orgPrs.findAll{ k,v ->
	print k.englishName + " "
	println  v.inject( 0, {i,o -> i+o.publishedItemCount})
}

// def orgId = 1000

for( Organization org in orgPrs.keySet()) {
	if( org.englishName =~ /Netherlands/ ) {
		allIds.clear();
		println( "Organization $org.englishName")
		for( PublicationRecord pr in orgPrs[ org] ) {
			if( publishedSchemaNames.contains( pr.publishedDataset.schema.name ) ) {
				analyze( pr.publishedDataset, allIds )
			}
		}
		println( "Found ${allIds.size()} different URLs in hasView,webResource and edm:object fields.")
		println( "----- finished $org.englishName")
		println()
	}
}

	
	
def analyze( Dataset ds, Map allIds ) {
	final Map ids = allIds
	ds.processAllValidItems( new ApplyI<Item>() {
		public void apply( Item item ) {
			// count all webresources, views and edm:object
			String url = null;
			Nodes n = item.getDocument().query( "//*[local-name()='WebResource']/@*[local-name()='about']" );
			for( int i=0; i<n.size(); i++ ) {
				url = n.get(i).getValue()
				ids.put( url, 1 )
			}
			
			n = item.getDocument().query( "//*[local-name()='Aggregation']/*[local-name()='hasView']/@*[local-name()='resource']" );
			for( int i=0; i<n.size(); i++ ) {
				url = n.get(i).getValue()
				ids.put( url, 1 )
			}

			n = item.getDocument().query( "//*[local-name()='Aggregation']/*[local-name()='object']/@*[local-name()='resource']" );
			for( int i=0; i<n.size(); i++ ) {
				url = n.get(i).getValue()
				ids.put( url, 1 )
			}

			n = item.getDocument().query( "//*[local-name()='Aggregation']/*[local-name()='isShownBy']/@*[local-name()='resource']" );
			 for( int i=0; i<n.size(); i++ ) {
				 url = n.get(i).getValue()
				 ids.put( url, 1 )
			 }
 
		}
	}, false)
}

