package gr.ntua.ivml.mint.test
import groovy.sql.Sql

import org.apache.commons.io.FileUtils;
import net.minidev.json.*;

String host = "panic.image.ece.ntua.gr"
String user = "euscreenxl"
String pass = "euscreenxl"
String port = "5433"
String db = "euscreenxl"

if( port != null ) port = ":"+port;

String filename = "/Users/admin/git/mint2/js/arneValidToV2.json"

String json = FileUtils.readFileToString(new File( filename), "UTF8");
JSONObject obj = JSONValue.parse(json);
long mappingId = 1330


try {
	String url = "jdbc:postgresql://${host}${port}/${db}"
	sql =  Sql.newInstance( url, user, pass, "org.postgresql.Driver" )
	sql.executeUpdate( "update mapping set json= ? where mapping_id = ?", [obj.toString(),mappingId])
} finally {
}
