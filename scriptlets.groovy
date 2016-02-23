import java.io.PrintWriter
import java.io.StringWriter

du = DB.getDataUploadDAO().getById( 1252l, false )

itemRoot = du.getItemXpath()
item = itemRoot.getNodes( 0,1).get(0)

sw = new StringWriter()
out = new PrintWriter( sw )

item.toXmlWrapped( out )


===== create the prefixes for existing db ===


import gr.ntua.ivml.athena.db.GlobalPrefixStore
import gr.ntua.ivml.athena.util.StringUtils

sr = DB.getXpathHolderDAO().scrollAll()

while( sr.next() ) {
  xp = sr.get(0)
  if( ! StringUtils.empty( (String) xp.getUri() )) {
   GlobalPrefixStore.createPrefix( (String) xp.getUri(), (String) xp.getUriPrefix() )
  }
  DB.getSession().clear()
}
sr.close()


========= check the XOM stuff

import gr.ntua.ivml.athena.persistent.*
import nu.xom.*

xo = DB.getXmlObjectDAO().findAll().find{ it.getRoot() != null }
xp = xo.getRoot().getByRelativePath( "/OAI-PMH/GetRecord/record" );
item = xp.getNodes( 10, 1).get(0)
tree = XMLNode.buildItemWrapTree( item )
elem = tree.toXOMElement()
 context = new XPathContext("oai", "http://www.openarchives.org/OAI/2.0/")

elem.query( "//oai:record", context ).size()
//elem.toXML()

==== transfer mappings from brain to nitro

import groovy.sql.Sql
import gr.ntua.ivml.athena.persistent.Mapping;

// connection to brain
sql = Sql.newInstance( "jdbc:postgresql://brain/athena", "athena", "athena",
 "org.postgresql.Driver" );
 
 
ids = [ 1297, 1312 ];
targetOrgId = 1044l;
//targetOrgId = 1001;

targetOrg = DB.getOrganizationDAO().getById( targetOrgId, false )
if( targetOrg == null ) throw new Exception( "Couldnt retreive target org" );
for( mappingId in ids ) {
   row = sql.firstRow( """select name, json from mapping  where mapping_id = $mappingId""" );
   if( row != null ) {
     Mapping m = new Mapping();
	m.setCreationDate( new Date() );
	m.setName( row.name );
	m.setJsonString( row.json )
	m.setOrganization( targetOrg )
	
	DB.getMappingDAO().makePersistent( m )
    println( "Mapping created " );
    println( m );
    println( "Json " + m.getJsonString().size() + " characters." );
    }	     
}

================= fix up mappings with event types ===========

====== redo all successfull transformations =================


import gr.ntua.ivml.athena.persistent.Transformation
import gr.ntua.ivml.athena.concurrent.Queues;

for( tr in DB.getTransformationDAO().findAll() ) {
  if( tr.statusCode == Transformation.OK ) {
       tr.setBeginTransform(new Date());
       tr.setStatusCode(Transformation.IDLE);
       tr.setStatusMessage( "" );
       tr.setJsonMapping(tr.getMapping().getJsonString());
	   tr.setEndTransform( null );
       DB.commit();
       Queues.queueTransformation(tr);
       println( "Queued Transformation " + tr.dbID )
       DB.getSession().evict( tr );
  }
}

=========== system commands on server: example df -h ================

import org.apache.commons.io.CopyUtils
pr = Runtime.getRuntime().exec( "df -h " )
CopyUtils.copy( pr.getInputStream(), System.out )
pr.waitFor()

===== which orgs have idle transformations =======================
def  orgs = [: ];

for( tr in DB.getTransformationDAO().findAll() ) {
  if( tr.statusCode == tr.ERROR ) {
	name = tr.dataUpload.organization.englishName
	if( orgs[name] ) orgs[name] += 1
        else  orgs[ name ] = 1  
  }
}

orgs.each{ println it.key+" " + it.value }

""

======= last 100 lines of log file ==================

import org.apache.commons.io.CopyUtils
pr = Runtime.getRuntime().exec( "tail -1000 ../../../../logs/catalina.out " )
CopyUtils.copy( pr.getInputStream(), System.out )
pr.waitFor()


============ simple db size report ==================

import groovy.sql.*

c = DB.getStatelessSession().connection()
md = c.getMetaData();
uname = md.getUserName()

sql = new Sql( c )
uid = sql.firstRow( "select usesysid from pg_user where usename = '$uname'" ).usesysid
tables = sql.rows( "select relname, reltuples, relpages from pg_class where relowner = $uid order by relpages desc" )
println "table rows pages"
tables.each{if( it.relpages > 5 ) {it.each{ print it.value+" " };println()}}
""
======== db size with easy read numbers =================

import groovy.sql.*

def prettyNum( num ) {
  def suf = [ "","k", "M", "G", "T", "P", "E"  ];
  def ind = 0;
  while( num > 1000.0 ) {
    ind +=1
    num /= 1000.0
  }
  return sprintf( "%1.3g%S",[num, suf[ind]]);
}

c = DB.getStatelessSession().connection()
md = c.getMetaData();
uname = md.getUserName()

sql = new Sql( c )
uid = sql.firstRow( "select usesysid from pg_user where usename = '$uname'" ).usesysid
tables = sql.rows( "select relname, reltuples, relpages from pg_class where relowner = $uid order by relpages desc" )
tables.each{if( it.relpages > 5 ) {println "Table/Index ${it.relname} - rows ${prettyNum(it.reltuples)} - " + prettyNum( it.relpages*8000.0)}}
pages = 0
tables.each{ pages += it.relpages }

"Total space " + prettyNum(pages*8000.0)

========== redo single transformation =================

import gr.ntua.ivml.athena.persistent.Transformation
import gr.ntua.ivml.athena.concurrent.Queues;

tr = DB.getTransformationDAO().getById( 1156l, false  ) 
tr.setBeginTransform(new Date());
tr.setStatusCode(Transformation.IDLE);
tr.setStatusMessage( "" );
tr.setJsonMapping(tr.getMapping().getJsonString());
tr.setEndTransform( null );
DB.commit();
Queues.queueTransformation(tr);
println( "Queued Transformation " + tr.dbID )

=========== Queue information =======================

import gr.ntua.ivml.athena.persistent.Transformation
import gr.ntua.ivml.athena.concurrent.Queues;

dbe = Queues.queues["db"]
dbq = dbe.getQueue()
nete = Queues.queues["net"]
netq = nete.getQueue()

printf( "%10s%10s%10s\n",["Name","pending","running"] )
printf( "%10s%10d%10d\n",["db",dbq.size(), dbe.getActiveCount() ] )
printf( "%10s%10d%10d\n",["net",netq.size(), nete.getActiveCount() ] )

============= log file again ======================
import org.apache.commons.io.CopyUtils
pr = Runtime.getRuntime().exec( "tail -1000 /usr/local/apache-tomcat-6.0.24/logs/catalina.out" )
CopyUtils.copy( pr.getInputStream(), System.out )
pr.waitFor()


===== failed transformations ===================


import gr.ntua.ivml.athena.persistent.Transformation

for( tr in DB.getTransformationDAO().findAll() ) {
  if( tr.statusCode != Transformation.OK ) {
       printf( "Transformation %6d Code %2d Message %s\n", [tr.dbID, tr.statusCode, tr.statusMessage] )
  }
       DB.getSession().evict( tr );
}
========== Fill a crosswalk with xsl ========================

import org.apache.commons.io.FileUtils
import gr.ntua.ivml.mint.util.Config

cw = DB.getCrosswalkDAO().getById( 1l, false )
File cwXsl = new File( Config.getProjectRoot(), "xsl/lido-v1.0-to-ese-v3.4-transform-v1.xsl" )
xsl = FileUtils.readFileToString(cwXsl, "utf-8")

cw.setXsl( xsl )

====== crosswalking some imports ====================

import gr.ntua.ivml.mint.concurrent.*
import gr.ntua.ivml.mint.persistent.Transformation

dsl = DB.datasetDAO.findAll()

dsl9proxy = dsl.findAll{ (it.schema != null) && (it.schema.name.equals( "LIDO 0.9 PROXY" )) }
dsl9 = dsl.findAll{  (it.schema != null) && (it.schema.name.equals( "LIDO 0.9" )) }

cw = DB.crosswalkDAO.getById( 2l, false )
dsl9.each {
  Transformation tr = Transformation.fromDataset( it, cw )
  DB.transformationDAO.makePersistent( tr )
  DB.commit()
   XSLTransform xslt = new XSLTransform( tr )
  Queues.queue( xslt, "db" )
  println "Queued ${it.name}"
}
===== load server files for euscreenxl  ========================================

import org.apache.commons.io.FileUtils
import gr.ntua.ivml.mint.util.Config
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.concurrent.*
import java.io.*

dir = new File( "/home/arne/euscreenOriginalData")
org = DB.organizationDAO.findAll().find{ it.originalName =~ "euscreen" }
    	

		
if( org == null) {
	println( "Org 'Euscreen old data' not found");
	return
}

user = org.getPrimaryContact();
schema = DB.xmlSchemaDAO.getByName( "EUScreen_valid");

def map = readMapFile( new File(dir,"annInfo.txt"))

// iterate through the map and see if the files exist
map.each{ key,val ->
  File file = new File( dir, "Annotation_"+key+".zip" )
  if( file.exists() && file.length() > 0 ) {
	  uploadFile( file, map[key][1], map[key][0])
	  return
  } else {
	  println( "No data for ${file.name}")
  }
}

// upload the file to 
def uploadFile( File data, String tag, String name ) {
	DataUpload du = new DataUpload();
	du.init( user );
	du.setName( name );
	du.setUploadMethod DataUpload.METHOD_SERVER
	du.setOrganization( org )
	du.addFolder( tag+"#8080ff" );
	du.setSchema( schema )
	println( "Name file '${data.name}' with '$name', tag '$tag'")
	// safe this
	DB.dataUploadDAO.makePersistent( du )
	DB.commit()
	UploadIndexer ui = new UploadIndexer( du, UploadIndexer.SERVERFILE );
	ui.setServerFile(data.getAbsolutePath())
	Queues.queue( ui, "single" );
}


def readMapFile( File mapFile ) {
	// num, filename, org/tag
	def res = [:]
	mapFile.eachLine( "UTF8", {
		String[] line = it.split("\t")
		res[line[0]] = [line[1], line[2]]		
	})
	return res
}

===== deal with mappings and the json in it =====

import groovy.json.*

schema = "EUscreenXL ITEM/CLIP v2"
maps = DB.mappingDAO.simpleList( "targetSchema.name = '$schema' ")
maps.each{ println it.targetSchema.name }

json = new JsonSlurper().parseText( maps[0].jsonString )

builder = new JsonBuilder( json )
JsonOutput.prettyPrint( builder.toString() )


=== list paths ====

// process and amend mappings (bookmarks, constraints)

import groovy.json.*
import gr.ntua.ivml.mint.mapping.model.*
import net.minidev.json.*

schema = "EUscreenXL ITEM/CLIP v2"
// xs = DB.xmlSchemaDAO.simpleGet( "name='$schema'")

 maps = DB.mappingDAO.simpleList( "targetSchema.name = '$schema' ")
// maps.each{ println it.targetSchema.name }

json = new JsonSlurper().parseText( maps[0].jsonString )
// extract the path ids
res = [:];
xpaths( "/", json.template, res )

res.each{
    println it
}

// try sorting the bookmarks
addBookmark(json, xpaths, "Series/Collection title", "/metadata/ContentDescriptiveMetadata/TitleSet/TitleSetInOriginalLanguage/seriesOrCollectionTitle");
addBookmark(json, xpaths, "Series/Collection title in English", "/metadata/ContentDescriptiveMetadata/TitleSet/TitleSetInEnglish/seriesOrCollectionTitle");
addBookmark(json, xpaths, "Series/Season number", "/metadata/ContentDescriptiveMetadata/seriesSeasonNumber");
addBookmark(json, xpaths, "Episode number", "/metadata/ContentDescriptiveMetadata/episodeNumber");

json.bookmarks.sort{ it.title.toUpperCase }

// mps = new Mappings( JSONValue.parse( maps[0].jsonString ))
// mps.find( "//*").collect{ it.name }

//mps.getHandlerForPath( "/metadata/ObjectDescriptiveMetadata/relationIdentifier")
builder = new JsonBuilder( json )
JsonOutput.prettyPrint( builder.toString() )

// only thing left is put it back into the mapping

public void xpaths( String prefix, Map elem, Map result ) {
    if( elem['name'] != null ) {
    	id = elem['id']
    	if( id != null ) {
    		result[id] =  prefix+elem['name']
    	}
        if( elem['attributes'] != null ) {
            elem['attributes'].each{
                xpaths( prefix+elem['name']+"/", it, result )
            }

        }
        if( elem['children'] != null ) {
            elem['children'].each{
                xpaths( prefix+elem['name']+"/", it, result )
            }
        }
    }
}

public String findId( String pattern, Map xpaths  ) {
	def entry = xpaths.find{ 
		it.value =~ pattern;
	}
	return entry.key
}

public void addBookmark( json, xpaths, title, path ) {
	def id = findId( path, xpaths )
	json.bookmarks.add( [
	         id: id,
	         title: title
	      ])
}

======= useful mapping stuff ===========

======= check XML from all items if parses ==========

import gr.ntua.ivml.mint.util.*
import gr.ntua.ivml.mint.persistent.*
import org.xml.sax.*
import org.xml.sax.helpers.*;
import java.io.*

someDatasetId = 1000l

ds = DB.datasetDAO.getById( someDatasetId, false )
XMLReader parser
parser = org.xml.sax.helpers.XMLReaderFactory.createXMLReader(); 
parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
parser.setContentHandler( new DefaultHandler())
StringBuilder res = new StringBuilder()
itemOp = new ApplyI<Item>() {
    public void apply( Item i ) {
        try {
        InputSource ins = new InputSource();
        ins.setCharacterStream(new StringReader( i.getXml() ))
            parser.parse( ins )
        }    
     catch( Exception e ) {
        res.append( "Item $i.dbID failed")
    }
    }
}

ds.processAllItems( itemOp, false  )
println res

====== schema checking ======

import gr.ntua.ivml.mint.concurrent.*

dss = DB.datasetDAO.simpleList( "schema.name = 'EUscreenXL ITEM/CLIP v2'")
dss.size()

count = 0 
for( ds in dss ) {
    val = new Validator( ds );
    orig = ds.getOrigin()
    println( "Validated $orig.name in $ds.organization.englishName")
    Queues.queue( val, "single")
    count++
    if( count == 10 ) break
}


====== remove the fixed mapping thing ===============

import groovy.json.*
import gr.ntua.ivml.mint.util.*
import gr.ntua.ivml.mint.mapping.model.*

map = DB.mappingDAO.simpleGet( "name like 'transfer%'")
obj = JSONUtils.parse( map.jsonString )
map1 = new Mappings( obj );
template = map1.getTemplate();
elem= template.findFirst("//eus:identifier" )
elem.setFixed( false )
map.setJsonString( obj.toString())

====== transform a lot of euscreen stuff ==========

import gr.ntua.ivml.mint.persistent.*;
import gr.ntua.ivml.mint.concurrent.*;
// org = 1025
// schema should be EUScreen_valid

dus = DB.dataUploadDAO.simpleList( "organization = 1025")
usr = DB.userDAO.simpleGet( "login ='arneOld'")

for( DataUpload du:dus ) {
	if( du.getBySchemaName("EUscreen OLD") != null ) continue;
	
}

def unpublish( du ) {
	def publisher = new EuscreenPublish()
	publisher.setCmd( "portalUnpublish")
	publisher.setDatasetId( du.dbID as String )
	def result = publisher.execute()
	println "$du.dbID unpublish: $result"
}



import gr.ntua.ivml.mint.persistent.*;
import gr.ntua.ivml.mint.concurrent.*;
// org = 1025
// schema should be EUScreen_valid

dus = DB.dataUploadDAO.simpleList( "organization = 1025")
//usr = DB.userDAO.simpleGet( "login ='arneOld'")
mapping = DB.mappingDAO.simpleGet( "name like 'transfer%'")
println mapping.name

// trs= dus.findAll().findAll{ it instanceof Transformation }
//    if( du.getBySchemaName("EUscreen OLD") != null ) continue;
//    derived  = du.getBySchemaName("EUscreenXL ITEM/CLIP v2")

// trs.each{ println "$it.parentDataset.name"}
int i=40;
for( du in dus ) {
    if( du.getBySchemaName("EUscreen OLD") != null ) continue;
    Transformation tr = Transformation.fromDataset( du, mapping) 
    DB.transformationDAO.makePersistent( tr )
    DB.commit()
    trans = new XSLTransform( tr )
    Queues.queue( trans, "db")
    println "Queued $du.name with $du.itemCount items"
    i--
    if( i==0 ) break;
}
=================inspect the euscreen old transform ===============

import gr.ntua.ivml.mint.util.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*

// per schema valid and invalid counts
dss = DB.datasetDAO.findAll().findAll{ it?.schema?.name == "EUscreen OLD" }

/*
int valid = 0, invalid = 0; invalidDs = 0
for( ds in dss ) {
    int total = ds.itemCount
    valid += ds.validItemCount
    invalid += (total - ds.validItemCount)
    if( ds.validItemCount == 0 ) invalidDs++;
}
*/
perFolder = [:]
for( ds in dss ) {
   String folder = ( ds.parentDataset.getFolders() as List)[0]
   if( folder == null ) folder = "No Org"
   def report = perFolder[folder]
   if( report == null ) report = []
   report.add( ["$ds.parentDataset.name", ds.validItemCount, ds.itemCount ] )
   perFolder[folder] = report
}

perFolder.each{ k,v ->
    int invalid = 0
    for( ds in v ) {
        invalid += (ds[2] - ds[1])
    }
    println( "'$k' $invalid ")

}

=========================================

import gr.ntua.ivml.mint.util.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*

// per schema valid and invalid counts
dss = DB.datasetDAO.simpleList( "organization_id = 1025").findAll{ 
     
    (it.folders as List)[0] =~ /RAI/ 
}

/*
int valid = 0, invalid = 0; invalidDs = 0
for( ds in dss ) {
    int total = ds.itemCount
    valid += ds.validItemCount
    invalid += (total - ds.validItemCount)
    if( ds.validItemCount == 0 ) invalidDs++;
}
*/
for( ds in dss ) {
    tr = ds.getBySchemaName( "EUscreen OLD")
        println( "$ds.name ${(ds.folders as List)[0]}")
    // remove this
    if( tr != null ) {
        // DB.transformationDAO.makeTransient( tr )
        DB.commit()
    }
}

====================================
import gr.ntua.ivml.mint.util.*
import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.concurrent.*

// per schema valid and invalid counts
dss = DB.datasetDAO
    .simpleList( "organization_id = 1025 and schema.name = 'EUscreen OLD'")

int queued = 0;
for( ds in dss ) {
    // make a Validator and queue it
    Validator val = new Validator( ds )
    Queues.queue( val, "db")
    println( "Queued Dataset[$ds.dbID].")
    queued++
    break
}
println( "Queued $queued datasets.")

======== republish euscreen =================

dss = DB.datasetDAO
.simpleList( "organization_id = 1025 and schema.name = 'EUscreen OLD'")

usr = DB.userDAO.simpleGet( "login ='arneOld'")

def publish( du ) {
	def publisher = new EuscreenPublish()
	publisher.setCmd( "portalPublish")
	publisher.setDatasetId( du.dbID as String )
    publisher.setUser( usr )
	def result = publisher.execute()
	println "$du.dbID Publish: $result"
}


=== revalidate schema (maybe after upload) ============
import gr.ntua.ivml.mint.concurrent.*

//DB.session.createQuery( "select distinct name from XmlSchema").list()
dss  = DB.datasetDAO.simpleList( "schema.name = 'EUscreenXL ITEM/CLIP v2'")

for( ds in dss ) {
def val = new Validator( ds )
Queues.queue( val, "db" )
}
println( dss.size() + " datasets queued for validation" )
=========== stuff on mappings ================

import groovy.json.*
import gr.ntua.ivml.mint.mapping.model.*
import gr.ntua.ivml.mint.mapping.*


 //                  "value": "EUS_00000000000000000000000000000000",
 //                   "type": "constant"
 
schema = "EUscreenXL ITEM/CLIP v2"
// DB.xmlSchemaDAO.findAll().each{ println it.name }

map = DB.mappingDAO.simpleGet("name like '%THIRD_VIDEO%'")
mappings = map.mappings
// println JsonOutput.prettyPrint( mappings.asJSONObject().toString())
elem = mappings.findFirst( "//eus:identifier")

mc = elem.getMappingCases()[0]

mc.setMapping( 0, JSONHandler.MAPPING_CONSTANT, "EUS_00000000000000000000000000000000", null )
 
println JsonOutput.prettyPrint( mappings.asJSONObject().toString())
// map.setJsonString( mappings.asJSONObject().toString()) 

/*
maps = DB.mappingDAO.simpleList( "targetSchema.name = '$schema'")
println maps.size()

for(map in maps ) {
    println map.dbID
mappings = map.mappings
// println JsonOutput.prettyPrint( mappings.asJSONObject().toString())
elem = mappings.findFirst( "//eus:identifier")
println JsonOutput.prettyPrint( elem.asJSONObject().toString())
}
*/

======== and another edited mapping =======

import gr.ntua.ivml.mint.mapping.*
import groovy.json.*

maps = DB.mappingDAO.simpleList( "name like '%20140522%'")
map = maps[0]

template = map.mappings

edmProvider = template.getHandlersForPrefixAndName("edm", "provider").get(0).getChild("edm:Agent");
edmProviderRes = edmProvider.getAttribute("@rdf:about");
edmProviderRes.addConstantMapping("http://www.europeanafashion.eu/")
edmProviderLabel = edmProvider.getChild("skos:prefLabel");
edmProviderLabel.addConstantMapping("Europeana Fashion");

// repairs the addConstantMapping() mistake
MappingConverter.upgrade20To21( template )
map.setJsonString( template.asJSONObject().toString())
// println JsonOutput.prettyPrint( edmProvider.asJSONObject().toString())
// this went badly ... things got screwed


==== added stuff to enum =========

import gr.ntua.ivml.mint.persistent.*
import groovy.json.*
//DB.xmlSchemaDAO.findAll().each{ println "$it.name"}
maps = DB.mappingDAO.simpleList( "targetSchema.name='EUscreenXL ITEM/CLIP v2'")
//maps.each{ println "$it.name $it.organization.englishName"}
 maps.each{ changeEnum( it ) }proc
//changeEnum(maps[0])
def changeEnum( Mapping mp ) {
    // eus:provider
    String rights = "Free Access - no-reuse"
    def mappings = mp.mappings
    def elem = mappings.findFirst("//eus:iprRestrictions")
    def enumProvider = elem.getEnumerations()
    if( ! enumProvider.contains(rights))
    	// prepend
        enumProvider.add( 0,rights)
else 
       println( "$rights already added")
       
    mp.setJsonString( mappings.asJSONObject().toString())
  //  println JsonOutput.prettyPrint(elem.toString())
}


==== list some ids, filtered by published and folder ==================

import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.util.*



prs = DB.publicationRecordDAO.simpleList( "organization = 1025")
for( pr in prs ) {
	def ds = pr.publishedDataset
	def origin = pr.originalDataset
	def folders = origin.folders
	def ids = []
	if( folders =~ /Netherlands/ ) {
		println "$origin.name"
		ds.processAllItems( new ApplyI<Item>() {
			public void apply( Item item) {
				String originalId = item.getValue("//*[local-name()='AdministrativeMetadata']/*[local-name()='originalIdentifier']");
				if( originalId != null ) ids.add( originalId )
			}
		}, false )
		ids.each{ println( "  $it" ) }
	}
}


============================
