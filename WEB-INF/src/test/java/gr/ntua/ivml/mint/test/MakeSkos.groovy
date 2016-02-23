package gr.ntua.ivml.mint.test

import groovy.xml.*

File input = new File( "/Users/admin/Projects/euscreen/Thesaurus_Euscreen.xml" )

// slurp input xml file
def thes = new XmlSlurper().parse( input )

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



