package gr.ntua.ivml.mint.util;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.db.Meta;
import gr.ntua.ivml.mint.persistent.Organization;
import net.minidev.json.parser.JSONParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class OrganizationList {

	
	//reads orgtargets config line if set
	public net.minidev.json.JSONArray getGoalTargets() throws ParseException, net.minidev.json.parser.ParseException{
		net.minidev.json.JSONArray jsontargets = null;

		if (Config.has("orgtargets")) {

			String targets = Config.get("orgtargets");

			JSONObject thejson = null;

			thejson = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(targets);
			jsontargets = (net.minidev.json.JSONArray) thejson
					.get("targetorglist");

			return jsontargets;
		}
		else return(new JSONArray());
	}

	
	//returns list of Organizations to report
	public List<Organization> listOrganizationToReport() throws ParseException, net.minidev.json.parser.ParseException{
		
		 	List<Organization> orgReportList  = new ArrayList<Organization>();
			// net.minidev.json.JSONArray array = new net.minidev.json.JSONArray();
			List<Organization> orgs = DB.getOrganizationDAO().findAll();
			
			List<String> ignoreList = null ;
			if (Config.has("orgtargets")) {
				net.minidev.json.JSONArray jsontargets = getGoalTargets();
				if (jsontargets != null) {
					Iterator it = jsontargets.iterator();

					while (it.hasNext()) {
						JSONObject orgtarget = (JSONObject) it.next();
						Integer orgid = (Integer) orgtarget.get("orgID");
						orgReportList.add(DB.getOrganizationDAO().getById(orgid.longValue() , false));

					}
				}
				return orgReportList;
			}

			else {
				if (Config.has("orgstatsignore")) {

					String str = Config.get("orgstatsignore");
					ignoreList = Arrays.asList(str.split(","));
					for (Organization org : orgs) {
						if (ignoreList.indexOf(Long.toString(org.getDbID())) == -1) {
							orgReportList.add(org);
						}
					}
					return orgReportList;
				}
				else {
					//checks for targets set organization in the meta table 
					List<Organization> orglist = new ArrayList<Organization>();
					for (Organization org : orgs) {

						String targetJson = Meta.get(org, "targets");
						if (targetJson != null) {
							orglist.add(org);
							// list with orgs with target meta json
						}
					}
					
					if (orglist.isEmpty()) {
						return orgs;
					}
					else if (!orglist.isEmpty()) 
					{

						return orglist;
					}
					

					}
		
			
			return orgs;
	}
	}
}
