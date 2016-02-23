package gr.ntua.ivml.mint.test;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gr.ntua.ivml.mint.annotator.GroupAnnotator;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.util.Triple;
import gr.ntua.ivml.mint.util.Tuple;
import junit.framework.TestCase;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;


public class GroupAnnotatorTest extends TestCase {
	public static final Logger log = Logger.getLogger(GroupAnnotatorTest.class ) ;
	
	
	public void setUp() {
	}
	
	public void testGetDataUploads() {//throws SAXNotRecognizedException, SAXNotSupportedException  {
		//Dataset dataset1 = DB.getDatasetDAO().findByName("FashionV2 Transformation");
		Long dsId = Long.parseLong("1015");
		GroupAnnotator ga = new GroupAnnotator(dsId, new User());
		try {
			Dataset ds = DB.getDatasetDAO().findById(dsId, false);
		    List<Item> items = DB.getItemDAO().getInvalidItemsByDataset(ds, 0, 10000000);
		    JSONArray itemsXpathActions = new JSONArray();
		    JSONObject itemsXpathAction = new JSONObject();
		    itemsXpathAction.put("itemIds", items);
			itemsXpathAction.put("actionList", new JSONArray());
			itemsXpathActions.add(itemsXpathAction);
			JSONObject xpathAction = new JSONObject();
			xpathAction.put("type", "editValue");
			xpathAction.put("xpath", "/rdf:RDF/edm:ProvidedCHO/gr:color/skos:Concept/@rdf:about");
			JSONArray args = new JSONArray();
			args.add("yellow");
			xpathAction.put("argumets", args);
			((JSONArray) ((JSONObject) itemsXpathActions.get(itemsXpathActions.size()-1))
			.get("actionList")).add(xpathAction);
		    //xpathValue.add(new Triple("/rdf:RDF/edm:ProvidedCHO/dc:description", "Some other description", false));
            long time = System.currentTimeMillis();
		    ga.modifyElementsInItemsList(itemsXpathActions);
		    time = System.currentTimeMillis() - time;
		    File f = new File("/home/eirini/Desktop/time.txt");
		    BufferedWriter output = new BufferedWriter(new FileWriter(f));
	        output.write("Time = " + time);
	        output.close();
		    Thread.sleep(100000);
		}
		catch (IllegalArgumentException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}
