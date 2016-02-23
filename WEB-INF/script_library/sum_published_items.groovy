import gr.ntua.ivml.mint.util.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*

// Published Item table 
// Sum the published items per organization and per schema

def records = DB.publicationRecordDAO.findAll()
def orgs = [:]
def allSchema = [:]
def orgInvalids = [:]
    	
for( def rec: records ) {
    if( rec.status != PublicationRecord.PUBLICATION_OK ) continue;
    def schemas = orgs[rec.organization.englishName]
    if( schemas == null ) {
        schemas = [:];
    }
    def schema = rec.publishedDataset.schema;
    if( schema == null ) continue;
    def count = schemas[schema.name];
    if( count == null ) count = rec.publishedItemCount;
    else count = count + rec.publishedItemCount;
    schemas[schema.name] = count;
    
    if( rec.publishedDataset.validItemCount != rec.publishedDataset.itemCount ) {
    	List invalids = orgInvalids[rec.organization.englishName]
    			if( invalids == null ) {
    				invalids = []
    				orgInvalids[rec.organization.englishName] = invalids	
    			}
    	String inv = sprintf( "%15s %15s %4d",
    		[ schema.name, rec.publishedDataset.origin.name, (rec.publishedDataset.itemCount - rec.publishedDataset.validItemCount)] );
    	invalids.add( inv )
    }
    
    orgs[rec.organization.englishName] = schemas
    if( ! allSchema[schema.name]) allSchema[schema.name] = rec.publishedItemCount
    else allSchema[schema.name] += rec.publishedItemCount
		
}

// print it 
orgs.each{ k,v -> 
    if( v != null ) { 
        println k
         v.each{ k2,v2 -> 
            println "  "+k2 +" ==> " + v2
         }
    }
}

println "\nInvalids in published sets\n"
orgInvalids.each{ k,v ->
  println k
  v.each{ l -> println "  " + l }
  println ("")
}

println "\nTotal Summary:"
allSchema.each{ k,v ->
  println "$k ==> $v"
}

""


