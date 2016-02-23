import com.opensymphony.xwork2.util.TextParseUtil;
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.actions.*
import gr.ntua.ivml.mint.util.*


// this scripts republishes, not just resends
// That means all ids are recalculated, Thesaurus realigned,
// Providers with crappy names renamed ...




Set<String> schemas= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));
def prs = DB.publicationRecordDAO.findAll()

	

for( pr in prs ) {
	if( schemas.contains( pr.getPublishedDataset().getSchema().getName())) {
		Dataset publishedDataset = pr.getPublishedDataset();
		DB.publicationRecordDAO.makeTransient( pr )
		publish( publishedDataset )
	}
}



def publish( du ) {
	def publisher = new EuscreenPublish()
	publisher.setCmd( "portalPublish")
	publisher.setDatasetId( du.dbID as String )
	publisher.setUser( du.creator )
	def result = publisher.execute()
	println "$du.dbID Publish: $result"
}






