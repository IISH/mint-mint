// package gr.ntua.ivml.mint.test

// import groovy.util.GroovyTestCase
import gr.ntua.ivml.mint.persistent.*
import nu.xom.*

def item = new Item()
item.setXml( """<?xml version="1.0" encoding="UTF-8"?>
<rdf:RDF
  xmlns:crm="http://www.cidoc-crm.org/rdfs/cidoc_crm_v5.0.2_english_label.rdfs#"
  xmlns:dc="http://purl.org/dc/elements/1.1/"
  xmlns:dcterms="http://purl.org/dc/terms/"
  xmlns:edm="http://www.europeana.eu/schemas/edm/"
  xmlns:foaf="http://xmlns.com/foaf/0.1/"
  xmlns:ore="http://www.openarchives.org/ore/terms/"
  xmlns:owl="http://www.w3.org/2002/07/owl#"
  xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/"
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
  xmlns:skos="http://www.w3.org/2004/02/skos/core#"
  xmlns:wgs84="http://www.w3.org/2003/01/geo/wgs84_pos#" xmlns:xalan="http://xml.apache.org/xalan">
  <edm:ProvidedCHO rdf:about="http://mint-projects.image.ntua.gr/data/euscreenXL/ina_1752878009042">
    <dc:identifier>ina_1752878009042</dc:identifier>
    <dc:language>French</dc:language>
    <dc:rights>Copyright : Institut national de l'audiovisuel (www.ina.fr). All rights reserved.</dc:rights>
    <dc:subject/>
    <dc:title>Trouble Every Day</dc:title>
    <dc:type>Journal télévisé</dc:type>
    <dcterms:issued>2001-07-11</dcterms:issued>
    <edm:type>VIDEO</edm:type>
  </edm:ProvidedCHO>
  <ore:Aggregation rdf:about="http://mint-projects.image.ntua.gr/data/euscreenXL/Aggregation_ina_1752878009042">
    <edm:aggregatedCHO rdf:resource="http://mint-projects.image.ntua.gr/data/euscreenXL/ina_1752878009042"/>
    <edm:dataProvider>INA</edm:dataProvider>
    <edm:isShownAt rdf:resource="http://www.ina.fr/video/1752878009042/trouble-every-day.fr.html#xtor=AL-3"/>
    <edm:object rdf:resource="http://www.ina.fr/images_v2/320x240/1752878009042.jpeg"/>
    <edm:provider>EUscreenXL</edm:provider>
    <edm:rights rdf:resource="http://www.europeana.eu/rights/rr-f/"/>
  </ore:Aggregation>
</rdf:RDF>
	""" )

List insertOrder = [ "{http://www.europeana.eu/schemas/edm/}:aggregatedCHO" ,
	"{http://www.europeana.eu/schemas/edm/}:dataProvider",
	"{http://www.europeana.eu/schemas/edm/}:hasView",
	"{http://www.europeana.eu/schemas/edm/}:isShownAt",
	"{http://www.europeana.eu/schemas/edm/}:isShownBy",
	"{http://www.europeana.eu/schemas/edm/}:object",
	"{http://www.europeana.eu/schemas/edm/}:preview",
	"{http://www.europeana.eu/schemas/edm/}:provider",
	"{http://purl.org/dc/elements/1.1/}:rights" ,
	"{http://www.europeana.eu/schemas/edm/}:rights",
	"{http://www.europeana.eu/schemas/edm/}:ugc" ]

// def newElem = item.insertElement( "object", "http://www.europeana.eu/schemas/edm/", insertOrder, "//*[local-name()='Aggregation']" )
// def newElem = item.insertElement( "dataProvider", "http://www.europeana.eu/schemas/edm/", insertOrder, "//*[local-name()='Aggregation']" )

// def newAtt = new Attribute( "rdf:resource", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", "http://some.url.from.arne/funny.jpg" )
// newElem.insertChild( "TestProvider", 0 )
// newElem.addAttribute( newAtt )

// println( item.getDocument().toXML())
String id = item.getValue( "//*[local-name()='Aggregation']/@*[local-name()='about']")
 println( id )
 
