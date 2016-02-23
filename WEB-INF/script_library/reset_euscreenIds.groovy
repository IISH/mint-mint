import java.util.Set;

import com.opensymphony.xwork2.util.TextParseUtil;

import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.util.*
import nu.xom.Document
import nu.xom.Element


def dsIds = [8419] as List

Set<String> publishedSchemaNames= TextParseUtil.commaDelimitedStringToSet(Config.get("euscreen.portal.schema"));

for( Long dsId: dsIds ) {
    def ds = DB.datasetDAO.getById(dsId, false)
	// find derived in right schema
		if( publishedSchemaNames.contains( ds.schema.name )) {
			//ok reset 
            println( "Processing $ds.dbID" )
			ds.processAllItems(new ApplyI<Item>() {
						public void apply( Item item ) {
							// modify item
							// reset the id
							Document doc = item.document
							String originalId = item.getValue( "//*[local-name()='AdministrativeMetadata']/*[local-name()='originalIdentifier']" )
							String provider = "SASE";
							String eusId = "EUS_"+StringUtils.md5Utf8(StringUtils.join( provider,":",originalId));
							 item.setValue( "//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']", 
								eusId )
							 
                            //item.setValue( "//*[local-name()='AdministrativeMetadata']/*[local-name()='identifier']", "EUS_00000000000000000000000000000000" )
						}
					}
					, true )
		}
}



