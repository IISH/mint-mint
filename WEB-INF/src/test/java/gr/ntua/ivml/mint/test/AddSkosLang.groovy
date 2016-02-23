package gr.ntua.ivml.mint.test

import groovy.xml.*
import org.apache.poi.hssf.usermodel.*

File input = new File( "/Users/admin/Projects/euscreen/EditedSkosIPTCv2.xml" )
//String excel = "/Users/admin/Projects/euscreen/Thesaurus_lt_edited.xls"
String excel = "/Users/admin/Projects/euscreen/Thesaurus_pt_edited.xls"
String lang = "pt"

// slurp input xml file
def origthes = new XmlSlurper().parse( input )
def engConceptMap = [:]

		
def concepts = origthes
 .'Description'
 .findAll{ it.type.'@resource' == "http://www.w3.org/2004/02/skos/core#Concept" }
 .each{
	 def url = it.'@about'
	 def label = it.'prefLabel'.find{ it.'@lang' == "en" }.text()
	 engConceptMap[label] = url
 }

wb = new HSSFWorkbook(new FileInputStream(excel))
sheet = wb.getSheetAt(0)

result = []
		
for( row in sheet.rowIterator()) {
	def labels = []
	for( cell in row.cellIterator()) {
		if( cell.CELL_TYPE_STRING == cell.getCellType()) {
			def content = cell.getStringCellValue();
			if( content =~ /\s+/ ) {
				labels.add( content )
			}
		}
	}
	if( labels.size() == 2 ) {
		def eng = labels[0]
		def trans = labels[1]
		if( engConceptMap.containsKey(eng)) {
			result.add( [ engConceptMap[eng], trans] )
		} else {
			println( "'$eng' not found.")
		}
		// find english concept
		// add to result
	} else if( labels.size() >0 ) {
		// println( "Only found - [$labels]" );
	}
}
//println engConceptMap
//  <rdf:Description rdf:about="http://thesaurus.euscreen.eu/EUscreenXL/v1/490">
//<skos:prefLabel xml:lang="en">Electricity Production &amp; Distribution</skos:prefLabel>
//</rdf:Description>

new File( "/tmp/Output.xml").withWriter( "UTF-8") {
	w -> 
	def builder = new MarkupBuilder( w )
	builder.setDoubleQuotes( true )
	builder.mkp.xmlDeclaration(version:'1.0')
	builder.'rdf:RDF'( 'xmlns:xsi':"http://www.w3.org/2001/XMLSchema-instance",
			'xmlns:rdf':"http://www.w3.org/1999/02/22-rdf-syntax-ns#",
			'xmlns:skos':"http://www.w3.org/2004/02/skos/core#"
		) {
			for( tuple in result ) {
				builder.'rdf:Description'( 'rdf:about': tuple[0]) {
					builder.'skos:prefLabel'( 'xml:lang':lang, tuple[1])
				}
			}
		}
}


// now read the excel sheet


System.exit(0);

baseUrl = "http://thesaurus.euscreen.eu/EUscreenXL/v1/"

def buildSkos( concept, builder ) {
	builder.'rdf:Description'( 'rdf:about': baseUrl + concept.@hierarchyId ) {
		builder.'rdf:type'( 'rdf:resource':"http://www.w3.org/2004/02/skos/core#Concept" )
		concept.Translations.Term*.each{
			if( it.GeneratedTranslation.text() != "true" ) {
				builder.'skos:prefLabel'('xml:lang':it.@lang, it.Name.text())
			}
			if( it.@lang == "en" && (it.ScopeNote != null) && it.ScopeNote.text()  ) {
				builder.'skos:scopeNote'( it.ScopeNote.text())
			}
		}
		if( concept.parent().name() == "Concept" ) {
			builder.'skos:broader'( 'rdf:resource':baseUrl + concept.parent().@hierarchyId)
		}
		builder.'skos:inScheme'( 'rdf:resource':baseUrl+"ConceptScheme" )
	}
}


//def writer = new FileWriter( "/tmp/output.xml", "UTF-8")
new File( "/tmp/Output.xml").withWriter( "UTF-8") {
	w -> 
	def builder = new MarkupBuilder( w )
	builder.setDoubleQuotes( true )
	builder.mkp.xmlDeclaration(version:'1.0')
	builder.'rdf:RDF'( 'xmlns:xsi':"http://www.w3.org/2001/XMLSchema-instance",
			'xmlns:rdf':"http://www.w3.org/1999/02/22-rdf-syntax-ns#",
			'xmlns:dcterms':"http://purl.org/dc/terms/",
			'xmlns:owl':"http://www.w3.org/2002/07/owl#",
			'xmlns:skos':"http://www.w3.org/2004/02/skos/core#"
		) {
			builder.'rdf:Description'( 'rdf:about': baseUrl+"ConceptScheme") {
				builder.'skos:prefLabel'( 'xml:lang':"en", "Subject ")
				builder.'rdf:type'( 'rdf:resource':"http://www.w3.org/2004/02/skos/core#ConceptScheme")
			}
			thes.'**'.findAll{it.name()=="Concept"}.each{ buildSkos( it, builder ) }
		}
}



