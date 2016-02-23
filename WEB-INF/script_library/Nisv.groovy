import gr.ntua.ivml.mint.util.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.db.*

prs = DB.publicationRecordDAO.simpleList( "organization = 1025")
prs = prs.findAll{
    pr ->
	for( folder in pr.originalDataset.folders )
	  if( folder =~ /Sound and Vision/ ) return true
	return false
 }
 
class ItemMonger implements ApplyI<Item> {
	def out;
	public ItemMonger( out ) {
		this.out = out;
	}
	public void apply( Item item ) {
//		def type = item.getValue( "//*[local-name()='materialType']")
//		if( type == "VIDEO") {
//			String id = item.getValue( "//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']")
//			// out.println( "Filename " + item.getValue("//*[local-name()='filename']") + "\n" )
//           if( item.getValue("//*[local-name()='filename']") != null ) {
//                item.setValue("//*[local-name()='filename']", id+".mp4" ) 
//            }
//		}
//		item.setValue( "//*[local-name()='provider']", "Netherlands Institute for Sound and Vision" )
		// out.println( "Provider " + item.getValue( "//*[local-name()='provider']"))
		
		// resend to noterick
		PublishQueue.queueItem( item, false )
	}
}

def mon = new ItemMonger( out )
prs.each{
	PublicationRecord pr ->
	def ds = pr.publishedDataset
	// if( pr.originalDataset.dbID != 2138 ) return;
	ds.processAllValidItems( mon, true )
}


