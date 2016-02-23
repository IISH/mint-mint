import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import gr.ntua.ivml.mint.Custom;
import gr.ntua.ivml.mint.actions.ItemPreview;
import gr.ntua.ivml.mint.actions.ItemPreview.View;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.db.Meta;
import gr.ntua.ivml.mint.persistent.AnnotatedDataset;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.util.EuscreenIndexHelper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.compass.retrotranslator.edu.emory.mathcs.backport.java.util.Arrays;

import gr.ntua.ivml.mint.util.Config;


public class CustomBehaviour extends Custom {
	@Override
	public void customModifySolarizedItem( Item item, SolrInputDocument sid ) {
		EuscreenIndexHelper.modifyDocument(item, sid);
	}
	public static String solrFieldNameEncode(String s) {
		String solrOut = "";
		try {
			byte[] bytes = s.getBytes("UTF-8");
			for (byte b : bytes) {
			    solrOut = solrOut + "0x" + Integer.toHexString(b & 0xFF);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return solrOut;
	}
	public String customSanitizeSolrXpath( String xpath, boolean withAt ) {
		// remove namespace
		// replace / with _
		// remove text() node marker
		// remove the wrapper element name
		xpath = xpath.replaceAll("/(@?)[^:]+:", "/$1").replace("/", "_").replaceAll("_text\\(\\)", "");
		xpath = xpath.replaceAll("_EUScreen_metadata", "" );
		String solrName = "";
		for (int i=0; i<xpath.length(); i++) {
			String s = xpath.substring(i, i+1);
			if (StringUtils.isAlphanumeric(s) || s.equals("_"))
				solrName = solrName + s;
			else
				solrName = solrName + solrFieldNameEncode(s);
		}
		return solrName;
	}

	public void customOrganizationStats( JSONObject ajson ) {
		long orgId = (Long) ajson.get( "organizationId");
		Organization org = DB.getOrganizationDAO().findById(orgId, false);
		ajson.put("target", org.getType());
	}
	
	public void customAddViews(ItemPreview itemPreview) {
		// Check if dataset is published on portal
		Dataset originalDS = itemPreview.getDataset();
		List<PublicationRecord> pub_rec = DB.getPublicationRecordDAO().findByOriginalDataset(originalDS);
		if(pub_rec.isEmpty())
			return;
		Dataset publishedDS = pub_rec.get(0).getPublishedDataset();
		JSONObject json = itemPreview.getJson();
		JSONArray views = (JSONArray) json.get("views");
		if(itemPreview.getItemId()==0) {
			String[] portal_schemas = Config.get("euscreen.portal.schema").split(",");
			if( !Arrays.asList(portal_schemas).contains(publishedDS.getSchema().getName()) )
				return;
			View view = new View(View.key(View.GROUP_PUBLISH, View.RESOURCE_ITEM),"Item on Portal",View.TYPE_LINK);
			if(views==null)
				views = new JSONArray();
			views.add(view.toJSON());
			json.put("views", views);
		}
		else {
			//Get requested views for column 0 and 1
			String[] views0 = itemPreview.getViews()[0].split("[.,]");
			String[] views1 = itemPreview.getViews()[1].split("[.,]");
			if( !Arrays.asList(views0).contains(View.GROUP_PUBLISH)
					&& !Arrays.asList(views1).contains(View.GROUP_PUBLISH) )
				return;
			// Get url for this published item
			if(publishedDS instanceof AnnotatedDataset) {
				publishedDS = ((AnnotatedDataset) publishedDS).getParentDataset();
			}
			String itemId = DB.getItemDAO().getDerived(itemPreview.getItem(),publishedDS).getPersistentId();
			String url;
			try {
				url = "http://preview.euscreen.eu/itempage.html?ID=" +
						URLEncoder.encode(itemId+"", "UTF-8");
				View view = new View(View.key(View.GROUP_PUBLISH, View.RESOURCE_ITEM)
						,"Item on Portal", View.TYPE_LINK, "", url);
				if( Arrays.asList(views0).contains(View.GROUP_PUBLISH) ) {
					JSONArray set = (JSONArray) views.get(0);
					set.add(view.toJSON());
					views.set(0,set);
				}
				if( Arrays.asList(views1).contains(View.GROUP_PUBLISH) ) {
					JSONArray set = (JSONArray) views.get(1);
					set.add(view.toJSON());
					views.set(1,set);
				}
				json.put("views", views);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

}
