import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.db.*
import groovy.json.*

def map = DB.mappingDAO.simpleGet( "name='arneProvider3'")
def providers = map.mappings.findFirst("//edm:dataProvider").getEnumerations()

println providers

//DB.xmlSchemaDAO.findAll().each{ println "$it.name"}
maps = DB.mappingDAO.simpleList( "targetSchema.name='EUscreenXL-EDM'")


//maps.each{ println "$it.name $it.organization.englishName"}
//  maps.each{ changeEnum( it ) }proc
changeEnum(maps[0])

def changeEnum( Mapping mp ) {
	// eus:provider
	def mappings = mp.mappings
	def elem = mappings.findFirst("//edm:dataProvider")
	def enumProvider = elem.getEnumerations()
	println JsonOutput.prettyPrint(elem.toString())

	// remove whats there
	enumProvider.clear();
	enumProvider.addAll( providers )
	   
   //  mp.setJsonString( mappings.asJSONObject().toString())
  println JsonOutput.prettyPrint(elem.toString())
}


