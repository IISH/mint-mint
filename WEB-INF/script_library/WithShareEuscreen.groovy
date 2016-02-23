import gr.ntua.ivml.mint.util.Config
import net.minidev.json.JSONNavi

import org.apache.commons.io.IOUtils
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.log4j.Logger


// run the runnable
// actually not a mint script

new ShareStuff().run()

public class ShareStuff implements Runnable {
	//	def log = Logger.getLogger( "gr.ivml.mint.ShareStuff");
	HttpClient hc =  new DefaultHttpClient()

	String projectId;
	String withUrl = "http://www.with.image.ntua.gr"

	Map someCol = [
//		"565b1ddae4b06a7f4c0b9a28":"LCA-LCVA_EUROPEANA_PILOT.zip",
//		"565b1ddfe4b06a7f4c0b9a9c":"LCA-LCVA_EUROPEANA_1.zip",
//		"565b1df8e4b06a7f4c0b9e8b":"LCA-LCVA_EUROPEANA_SECOND.zip",
//		"565b26b2e4b06a7f4c0c335b":"DW-spanish_audio_dw_mediacenter_export.json",
//		"565b26b2e4b06a7f4c0c3358":"DW-portuguese_audio_dw_mediacenter_export.json",
//		"565b26b2e4b06a7f4c0c335c":"DW-hausa_audio_dw_mediacenter_export.json",
//		"565b26b2e4b06a7f4c0c335d":"DW-french_audio_dw_mediacenter_export.json",
//		"565b26b2e4b06a7f4c0c3357":"DW-turkish_audio_dw_mediacenter_export.json",
//		"565b26b2e4b06a7f4c0c3356":"DW-2014-10-09_07-02-39_dw_mediacenter_export.json.zip",
//		"565b26b2e4b06a7f4c0c3359":"DW-persian_audio_dw_mediacenter_export.json",
//		"565b26b2e4b06a7f4c0c335a":"DW-urdu_audio_dw_mediacenter_export.json",
//		"565b26bbe4b06a7f4c0c3474":"DW-chinese_audio_dw_mediacenter_export.json",
//		"565b26f0e4b06a7f4c0c3bdb":"DW-english_audio_dw_mediacenter_export.json",
//		"565b26fce4b06a7f4c0c3d79":"DW-greek_audio_dw_mediacenter_export.json",
//		"565b270fe4b06a7f4c0c3ffa":"DW-amharic_audio_dw_mediacenter_export.json",
//		"565b2740e4b06a7f4c0c467c":"DW-german_audio_dw_mediacenter_export.json",
//		"565b27d3e4b06a7f4c0c5a1a":"NAVA-eu2.zip",
//		"565b27dee4b06a7f4c0c5b8e":"NAVA-eu3.zip",
//		"565b2842e4b06a7f4c0c68ad":"NAVA-eu4.zip",
//		"565b28afe4b06a7f4c0c76b3":"NAVA-eu5.zip",
//		"565b293ee4b06a7f4c0c8909":"NAVA-eu6.zip",
//		"565b2acee4b06a7f4c0cba99":"NAVA-aggregation_3_nava.zip",
//		"565b319ae4b06a7f4c0d7c2d":"NAVA-eu7.zip",
//		"565b36b3e4b06a7f4c0dfe6d":"NAVA-zip_all_unpack1.zip",
//		"565b380ce4b06a7f4c0e1ecb":"NAVA-zip_all2_unpack2.zip",
//		"565b3857e4b06a7f4c0e25d8":"NAVA-eu1.zip",
//		"565b458fe4b06a7f4c0f47e5":"?T-03_iVysilaniExport_2012.zip",
//		"565b5bcae4b06a7f4c10df59":"?T-02_iVysilaniExport_2011.zip",
//		"565b60abe4b06a7f4c113483":"?T-04_iVysilaniExport_2013.zip",
//		"565b6ad8e4b06a7f4c122aef":"?T-05_iVysilaniExport_2014.zip",
//		"565b6bbae4b06a7f4c1293ec":"?T-01_iVysilaniExport_2010.zip",
//		"565b6ff8e4b06a7f4c1485f6":"?T-06_iVysilaniExport_2015 (01.-10, missing Feb.).zip",
//		"565b70a9e4b06a7f4c14d6fc":"INA-ina_export_europeana_1.xml",
//		"565b70e1e4b06a7f4c14f119":"INA-ina_export_europeana_4.xml",
//		"565b7154e4b06a7f4c152614":"INA-ina_export_europeana_2_final.xml",
//		"565b717ee4b06a7f4c153a31":"INA-ina_export_europeana_3_final_corrected.xml",
//		"565b7382e4b06a7f4c16314c":"RTVSLO-Slovenska kronika_preostalo nad 1k.xml",
//		"565b739ae4b06a7f4c163be1":"RTVSLO-Tednik.xml",
//		"565b73aae4b06a7f4c1643bc":"RTVSLO-Slovenska kronika_prvih 1k.xml",
//		"565b73e2e4b06a7f4c165f55":"RTVSLO-Slovenski_magazin.xml",
//		"565b73eae4b06a7f4c1662e6":"RTVSLO-EUscreenXLEuropeanaOsamosvojitevTVD.csv",
//		"565b73f7e4b06a7f4c166954":"RTVSLO-Infodrom.xml",
//		"565b741ae4b06a7f4c1679dd":"RTVSLO-Opus.xml",
//		"565b741ee4b06a7f4c167bf5":"RTVSLO-Obzorja duha.xml",
//		"565b7436e4b06a7f4c168788":"RTVSLO-Kultura.xml",
//		"565b74a6e4b06a7f4c16bd37":"RTVSLO-Osmi dan.xml",
//		"565b74bde4b06a7f4c16c8b1":"RTVSLO-Podoba podobe.xml",
//		"565b74c1e4b06a7f4c16caf9":"Luce-Radar.xml",
//		"565b754ae4b06a7f4c170c4f":"Luce-incom.xml.zip",
//		"565b7581e4b06a7f4c1726c1":"Luce-mondolibero.xml",
//		"565b75ade4b06a7f4c173ab1":"Luce-europeociac.xml",
//		"565b75cde4b06a7f4c174905":"Luce-setteg.xml",
//		"565b75efe4b06a7f4c1758d1":"Luce-cronache.xml",
//		"565b7623e4b06a7f4c17723b":"Luce-ciac.xml",
//		"565b7638e4b06a7f4c177be8":"Luce-caleidoscopiociac.xml.zip",
//		"565b4233e4b06a7f4c0f01df":"TV3-exportacIo EUROPEANA programes.xlsx - Hoja1.csv",
//		"565b4254e4b06a7f4c0f048e":"TV3-exportacio EUROPEANA informatius.xlsx - Full1.csv",
//		"565b2754e4b06a7f4c0c4927":"RTBF-euscreen_videos-csv.csv",
//		"565b77aee4b06a7f4c1832eb":"HPRT-AGGREGATION VIDEOfinal.csv",
//		"565b77d0e4b06a7f4c1843d3":"HPRT-ERT Photos aggregationFINAL.csv",
//		"565b77d6e4b06a7f4c184690":"HPRT-video2.csv"
//		"565b1ddae4b06a7f4c0b9a2b":"SASE-europeana_export3_items444.xml",
//		"565b1ddae4b06a7f4c0b9a29":"NInA-Aggregation__NInA__PILOT__(140)__2015_07_02 - Arkusz1.csv",
//		"565b1ddae4b06a7f4c0b9a2c":"NInA-Aggregation__(6860)__NInA__SLOT_1 and _SLOT_2__2015_11_12 (csv).csv",
//		"565b1ddae4b06a7f4c0b9a26":"DR-europeana.xml",
//		"565b1ddae4b06a7f4c0b9a2d":"DR-europeana_02.xml",
//		"565b1ddae4b06a7f4c0b9a2a":"DR-europeana_03.xml",
//		"565b3936e4b06a7f4c0e3a81":"NISV-Aggregated_video_20-11-2015.zip",
//		"565b3c79e4b06a7f4c0e84f1":"NISV-Aggregated_foto_20-11-2015.zip",
//		"565b1ddae4b06a7f4c0b9a27":"KB-edm_v1_CN.xml",
		"565b2745e4b06a7f4c0c4733":"BUFVC-newsonscreen-roundabout-20140116.xml",
		
	]

	Map orgs = [
		"tv3.cat":"5613ebcfe4b0eb60020c8405",
		"drdk":"5613eceee4b0eb60020c840f",
		"nava":"5613ef52e4b0eb60020c8428",
		"ina":"561b8ce9e4b01881c95b6e02",
		"ntua":"5620e9f8e4b0857763602205",
		"luce":"5617be4ce4b01881c95b6d36",
		"sase":"5617c111e4b01881c95b6d4b",
		"tvr.ro":"5617c1c1e4b01881c95b6d54",
		"lca":"5617c35be4b01881c95b6d5f",
		"nina":"5617c72ce4b01881c95b6d68",
		"rte":"5617d57fe4b01881c95b6d91",
		"orf":"5617d7b4e4b01881c95b6d9a",
		"bufvc":"5617d3f3e4b01881c95b6d88",
		"rtp":"5617dc32e4b01881c95b6dae",
		"nisv":"5617dd3fe4b01881c95b6db7",
		"rtbf":"561b8f09e4b01881c95b6e15",
		"polishtv":"561b90c3e4b01881c95b6e23",
		"kb":"561b9258e4b01881c95b6e2d",
		"ct":"561b9386e4b01881c95b6e36",
		"rtvslo":"561b9b23e4b01881c95b6e41",
		"dw":"561e46ade4b01881c95b6e91",
		"ert":"565839afe4b06a7f4bfe89da"
	]

	Map allCol = [
		"565b77a6e4b06a7f4c182f26":"FMS-VIDEO_URL6.csv",
		"565b7677e4b06a7f4c179a3c":"FMS-PHOTOS_DATASET5000_.csv",
		"565b7789e4b06a7f4c1821a9":"FMS-VIDEO_URL10.csv",
		"565b778ae4b06a7f4c1821ea":"FMS-CA_URL4_1.csv",
		"565b7797e4b06a7f4c18284b":"FMS-CA_URL3.csv",
		"565b7798e4b06a7f4c1828ea":"FMS-CA_URL5.csv",
		"565b7799e4b06a7f4c18295b":"FMS-CA_URL6.csv",
		"565b779be4b06a7f4c182a11":"FMS-CA_URL2.csv",
		"565b779de4b06a7f4c182b2f":"FMS-CA_URL7.csv",
		"565b779ee4b06a7f4c182b63":"FMS-VIDEO_URL1.csv",
		"565b779ee4b06a7f4c182ba2":"FMS-CA_URL8.csv",
		"565b779ee4b06a7f4c182bc5":"FMS-VIDEO_URL5.csv",
		"565b779fe4b06a7f4c182c59":"FMS-VIDEO_URL2.csv",
		"565b77a0e4b06a7f4c182caa":"FMS-VIDEO_URL7.csv",
		"565b77a2e4b06a7f4c182dd6":"FMS-VIDEO_URL3.csv",
		"565b77a3e4b06a7f4c182e0f":"FMS-VIDEO_URL8.csv",
		"565b77a4e4b06a7f4c182ea7":"FMS-VIDEO_URL9.csv",
		"565b77a5e4b06a7f4c182ee5":"FMS-VIDEO_URL4.csv",
		"565b77a8e4b06a7f4c182ff6":"LNA LVKFFDA-Europeana_2.xml",
		"565b77aae4b06a7f4c183146":"LNA LVKFFDA-faili_ieladesanai REAL2.xml",
		"565b77abe4b06a7f4c1831a2":"LNA LVKFFDA-Europeana_3.xml",
		"565b77ace4b06a7f4c1831d6":"LSA-EDM_Data_Export_20150630_1508.tgz",
	];


	public void run() {

		if( ! loginToWith()) return
			someCol.each{ k,v ->
				shareCollection( k, orgs["bufvc"])
			}
	}

	public void deleteCollection( collId ) {
		HttpDelete del = new HttpDelete( withUrl+"/collection/"+collId )
		HttpResponse res = hc.execute( del )
		if (res.getStatusLine().getStatusCode() == 200) {
			println( "Deleted $collId" )
		} else {
			println ( "Failed to delete $collId")
		}
		del.releaseConnection()
	}

	public boolean shareCollection( String colId, String userId ) {
		HttpGet shareGet
		HttpResponse res
		try {
			shareGet = new HttpGet( withUrl+"/rights/" + colId + "/OWN?userId=" + userId )
			res = hc.execute( shareGet )

			if (res.getStatusLine().getStatusCode() == 200) {
				println( "Shared with $userId" )
			} else {
				println ( "Failed to share $colId")
				return false
			}
		} finally {
			shareGet.releaseConnection();
		}

		// and with EUscreenXl ObjectId("561033bfe4b03e1878ccaf73")
		String eus = "561033bfe4b03e1878ccaf73"
		try {
			shareGet =new HttpGet( withUrl+"/rights/" + colId + "/READ?userId=" + eus )
			res = hc.execute( shareGet )

			if (res.getStatusLine().getStatusCode() == 200) {
				println( "Shared with EUscreenXL" )
				return true
			} else {
				println ( "Failed to share $colId with EUscreenXL")
				return false
			}
		} finally {
			shareGet.releaseConnection();
		}
	}

	public boolean loginToWith() {
		HttpPost loginToWith = new HttpPost(withUrl
				+ "/user/login");
		String jsonBody = new JSONNavi()
				.set( "email", "euscreen" )
				.set( "password", "123456" )
				.toString()

		loginToWith.setEntity(new StringEntity(jsonBody));
		loginToWith.addHeader("Content-type", "text/json");

		HttpResponse response = hc.execute(loginToWith);
		if (response.getStatusLine().getStatusCode() == 200) {
			// hopefully handles the connection ...
			loginToWith.releaseConnection()
			loginToWith.abort();
			return true;
		} else {
			String resString = IOUtils.toString( response.entity.content , "UTF8")
			return false;
		}
	}

}