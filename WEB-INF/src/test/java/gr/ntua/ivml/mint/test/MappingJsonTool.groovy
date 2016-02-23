#!/usr/bin/env groovy
@GrabConfig(systemClassLoader=true) 
@Grapes([
	@Grab(group='commons-io', module='commons-io', version='2.4'),
    @Grab(group='org.postgresql', module='postgresql', version='9.4-1201-jdbc41'),
	@Grab(group='net.minidev', module='json-smart', version='2.1.1')
])

import groovy.sql.Sql
import groovy.json.*

import net.minidev.json.*
import org.apache.commons.io.FileUtils

def cli = new CliBuilder()

cli.h('database hostname', required:true, args:1 )
cli.p('database port, default 5432', args:1)
cli.u( 'user and password', required:true, args:1)
cli.d( 'if given its the db name, otherwise use user', args:1 )
cli.w( 'write back (default read)')
cli.m( 'mapping id to handle, without, list them', args:1)
def options = cli.parse(args)

// see if we can conect to database


String port = 5432 
if( options.p ) port = options.p

try {
	if( !options.h && !options.u) {
		println ("Missing database connection")
		return
	} 
	String database= options.u
	if( options.d ) database = options.d
	
	String url = "jdbc:postgresql://${options.h}:${port}/$database"
	def sql =  Sql.newInstance( url, options.u, options.u, "org.postgresql.Driver" )

	if( ! sql ) {
		println( "Cannot make db connection")
		return
	}
	
	if( !options.m) {
		sql.eachRow("select * from mapping"){
			row ->
			println "$row.name $row.mapping_id"
		}
		return
	} else {
		if( options.w && !options.arguments() ) {
			println "Need filename with mapping"
			return
		}
		long mappingId = options.m as long
		def row = sql.firstRow("select * from mapping where mapping_id = $mappingId")
		String filename = options.arguments()[0]
		if( options.w ) {
			installMapping( sql, options.m as long, filename)
		} else {
			showMapping( row, filename )
		}
	}	
} catch ( Exception e) {
	println "Something went wrong " + e;
}

def showMapping( row, file  ) {
	def obj = JSONValue.parse( row.json );
	println "$row.mapping_id $row.name"
	if( file ) 
		FileUtils.writeStringToFile( new File( file ), JsonOutput.prettyPrint( row.json ), "UTF8")	
	else 
		println JsonOutput.prettyPrint( row.json )
}

def installMapping( sql, mappingId, file ) {
	try {
		String json = FileUtils.readFileToString(new File( file), "UTF8");
		def obj = JSONValue.parse( json )
		if( obj == null ) throw new Exception()
 		sql.executeUpdate( "update mapping set json= ? where mapping_id = ?", [obj.toString(),mappingId])
		
	} catch( Exception e ) {
	 	println ("Json in file invalid?" + e )
	}
}

//String json = FileUtils.readFileToString(new File( filename), "UTF8");
//JSONObject obj = JSONValue.parse(json);
//long mappingId = 1330
//	sql.executeUpdate( "update mapping set json= ? where mapping_id = ?", [obj.toString(),mappingId])
