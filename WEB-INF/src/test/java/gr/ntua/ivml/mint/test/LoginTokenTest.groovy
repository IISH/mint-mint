package gr.ntua.ivml.mint.test

import junit.extensions.TestSetup
import junit.framework.Test
import junit.framework.TestSuite

import org.apache.commons.io.IOUtils
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair

import net.minidev.json.*

class LoginTokenTest extends GroovyTestCase {
	HttpClient httpclient;
	
	public void testARequest() {
		ArrayList<NameValuePair> postParameters;
		HttpPost httppost = new HttpPost("http://localhost:8080/mint2/Login.action");
		postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("user", "admin"));
		postParameters.add(new BasicNameValuePair("password", "mt1k0"));
	
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));	
		HttpResponse response = httpclient.execute(httppost);
		
		httppost.releaseConnection();
		// lets assume we are logged in

		HttpGet tokenGet = new HttpGet( "http://localhost:8080/mint2/LoginToken");
		response = httpclient.execute( tokenGet );
		if( response.statusLine.statusCode != 200 ) {
			println "Login failed"
		} else {
			String json  = IOUtils.toString( response.entity.content , "UTF8")

			JSONObject jo = JSONValue.parse( json );
			String token = jo.get( "token");
		}
		
		

	}

	public void setUp() {
		httpclient = new DefaultHttpClient();
	}

	public void tearDown() {
		httpclient.getConnectionManager().shutdown();	
	}
	
	
	public static Test suite() {
		return new TestSetup( new TestSuite(LoginTokenTest.class )) {
			protected void setUp() throws Exception {
				
			}
		}
	}

}