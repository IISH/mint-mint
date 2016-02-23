#!/usr/bin/env groovy

@Grab(group='commons-httpclient', module='commons-httpclient', version='3.1')
@Grab(group='org.jsoup', module='jsoup', version='1.6.2')
@Grab(group='commons-io', module='commons-io', version='2.4')
@Grab(group='org.apache.commons', module='commons-lang3', version='3.4')

import org.apache.commons.httpclient.*
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.io.*
import org.apache.commons.lang3.*
import org.jsoup.Jsoup
import org.jsoup.select.*
import org.jsoup.nodes.*

import groovy.json.*

import java.nio.*
import java.nio.channels.*
import java.util.regex.Matcher
import java.util.regex.Pattern


String filename = "noterikVideos.txt"

Map idMap = readNoterickApprovedList(true)
List ids = (idMap.keySet() as List).sort()
println( "Harvested ${ids.size()} ids from Noteriks server" )
println( "Writing output to $filename")
// List noterikVideoRecords = harvestNoterikVideoPlayoutXml( idMap, ids[40001..-1] )
// List noterikVideoRecords = harvestNoterikVideoPlayoutXml( idMap, ids )

// List noterikVideoRecords = []
// noterikFile( new File(filename), noterikVideoRecords, "write" )

// noterikVideoRecords.groupBy { it.provider + " " + it.public }
//	.entrySet().sort{ a,b -> a.key<=>b.key}.each{ r -> println "$r.key ${r.value.size()}" }
 
 


// read the ids with providers and store in a map and on a file (for cache)
def readNoterickApprovedList( boolean refresh) {
	def cachefile = new File( "noterickIds.txt")
	def allIds = [:] as Map

	Authenticator.setDefault (new Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication ("admin", "ronflonflon12".toCharArray());
		}
	});
	
	if( refresh ) {
		cachefile.withPrintWriter { writer ->
			def noterickApproved = "http://player3.noterik.com/bart/domain/euscreenxl/user/"

			def providersXml = IOUtils.toString(new URL( noterickApproved ).openStream(), "UTF8" )
			def providerDoc = Jsoup.parse( providersXml )
			Elements users = providerDoc.select( "user" )
			for( Element elem: users ) {
				def provider = elem.attr( "id")
				if( !provider.startsWith( "eu_" )) continue;
				def idXml = IOUtils.toString(new URL( noterickApproved+provider+"/" ).openStream(), "UTF8" )
				def idDoc = Jsoup.parse( idXml )
				def allIdElems = idDoc.select( "*[id]" )
				for(Element idElem: allIdElems ) {
					def id = idElem.attr( "id")
					if( id =~ /EUS_/ ) {
						allIds[id] = provider
						writer.println( "$id\t$provider")
					}
				}
			}			
		}
	} else {
		cachefile.eachLine {
			line ->
			String[] elems = line.split("\\t")
			allIds[elems[0]] = elems[1]
		}
	}
	return allIds
}

def harvestNoterikVideoPlayoutXml( Map ids, List whichIds ) {
	HttpClient client = new HttpClient()
	List harvestedData = []
	String baseUrl = "http://player3.noterik.com/smithers2/domain/euscreenxl/user"
	int count =0;
	def document;
	def doctype;
	for( String id: whichIds ) {
		List types = ["video", "doc", "picture", "audio", "series" ]
		for( type in types) {
			doctype = type
			String url = baseUrl+"/" + ids[id] + "/$type/" + id +"/properties"
			def getUrl = new GetMethod( url )
			def res = client.executeMethod( getUrl )
			def statusCode = getUrl.statusCode
			if( statusCode != 200 ) {
				println( "No response for $id.")
				continue
			}
			def html = IOUtils.toString( getUrl.getResponseBodyAsStream(), "UTF8" )
			document = Jsoup.parse(html)
			def err = document.select( "error" ).first();
			// I guess this means it worked
			if( err == null ) break;
			println( "$id was no $type")
		}
		Map meta = [:]
		meta["screenShot"] = document.select("screenshot").text()
		meta["public"] = document.select("public").text()
		meta["originalIdentifier"] = noterikUnescape(document.select("originalIdentifier").text())
		meta["provider"] = noterikUnescape(document.select("provider").text())
		meta["currentimportdate"] = document.select("currentimportdate").text()
		meta["id"] = id
		meta["euProvider"] = ids[id]
		meta["type"] = doctype
		
		harvestedData.add( meta )
		count++;
		if( count % 500 == 0 ) print(".")
		if( count %10000 == 0 ) println("")
	}
	return harvestedData
}

def noterikUnescape( String input ) {
    Pattern p = Pattern.compile( /\\\d{3}/ )
	StringBuffer res = new StringBuffer()
	Matcher m = p.matcher( input )
	while( m.find()) {
		String numS = m.group().substring(1)
		int num = Integer.parseInt(numS)
		m.appendReplacement(res, new String( Character.toChars(num )))
	}
	m.appendTail( res )
	return res.toString()
}

def noterikFile( File f, List records, String command ) {
	if( command == "read" ) {
		f.eachLine( "UTF8", 0, {
			line ->
			String[] elems = line.split("\\t")
			Map record = [:]
			record["screenShot"] = elems[0]
			record["public"] = elems[1]
			record["originalIdentifier"] = elems[2]
			record["provider"] = elems[3]
			record["currentimportdate"] = elems[4]
			record["id"] = elems[5]
			record["euProvider"] = elems[6]
			records.add( record )
		} )
	} else if( command == "write" ) {
		f.withPrintWriter( "UTF8", {
			writer ->

			for( Map record: records ) {
				String line = [ record["id"],record["type"],
					record["provider"],record["screenShot"], record["public"], 
					record["originalIdentifier"],
					record["currentimportdate"], 
					record["euProvider"]].join("\t")
				writer.println( line )
			}
		} )
	} else {
		// append ?
		f.withWriterAppend( "UTF8", {
			writer ->

			for( Map record: records ) {
				String line = [ record["id"],record["type"],
					record["provider"],record["screenShot"], record["public"], 
					record["originalIdentifier"],
					record["currentimportdate"], 
					record["euProvider"]].join("\t")
				writer.println( line )
			}
		} )
	}
}

/**
 new File( "/Users/stabenau/Sites/noterick.txt").eachLine {
 line ->
 if(( current >= offset) && (current < offset+count)) {
 current++
 def id = line.trim()
 if( id =~ /EUS_/ ) {
 // screenshotUrl( client, id )
 urlMap[id] =  screenshotUrl( client, id )
 }
 }
 }
 new File("outputMap.txt").withPrintWriter {
 writer ->
 urlMap.each{ k,v ->
 writer.println( "$k $v")
 }
 }
 // http://c6.noterik.com:8080/smithers2/domain/euscreenxl/user/eu_nisv/video/EUS_01271DA2F771FBAAF532226C6309D173
 def getDoc(String url ) {
 String base = "http://player3.noterik.com/smithers2/domain/euscreenxl/user"
 // eu_kb/$eusid/properties
 // screenshot
 // public
 // provider
 // originalIdentifier
 // 	
 //	/eu_nisv/video/EUS_772ADB776A421A8F1E72DC2D2E69FBF3/properties
 // http://c6.noterik.com:8080/smithers2/domain/euscreenxl/user/eu_nisv/video/EUS_01271DA2F771FBAAF532226C6309D173
 }
 def screenshotUrl( HttpClient client, String euscreenId ) {
 String res = null
 try {
 def url = new GetMethod("http://www.euscreen.eu/item.html?id="+euscreenId)
 url.setRequestHeader( "Cookie", "smt_browserid=Fri%2C%2008%20May%202015%2006%3A41%3A08%20GMT")
 client.executeMethod( url )
 String outputUrl = null
 def html = url.getResponseBodyAsString()
 def document = Jsoup.parse(html)
 res = document.select("meta[property=og:image]").first().attr("content")
 if(( res != null ) && ( res!="null"))
 return res;
 if( false){
 URL website = new URL(res+"?script=euscreen640t");
 ReadableByteChannel rbc = Channels.newChannel(website.openStream());
 FileOutputStream fos = new FileOutputStream(euscreenId+".jpg");
 int offset = 0
 int count =1
 while( count > 0  ) {
 count = fos.getChannel().transferFrom(rbc, offset, Long.MAX_VALUE);
 offset += count
 }
 }
 } catch(Exception e ) {
 e.printStackTrace()
 } finally {
 //probably need to close resources
 }
 }
 **/
