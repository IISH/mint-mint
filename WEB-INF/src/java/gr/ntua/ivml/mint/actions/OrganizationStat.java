package gr.ntua.ivml.mint.actions;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.db.Meta;
import gr.ntua.ivml.mint.persistent.AnnotatedDataset;
import gr.ntua.ivml.mint.persistent.DataUpload;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.persistent.Transformation;
import gr.ntua.ivml.mint.report.OrgOAIBeanFactory;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.Config;
import gr.ntua.ivml.mint.util.Tuple;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "error", location = "json.jsp"),
		@Result(name = "success", location = "json.jsp") })
public class OrganizationStat extends GeneralAction {

	protected final Logger log = Logger.getLogger(getClass());
	public JSONObject json;
	public String organizationId;
	public List<String> schemaList;

	private int itemsTransformed, itemsImported, itemsPublished ;
	private Map<String, Integer> countedItemsperSchema = new HashMap<String, Integer>();

	
	private Map<java.lang.Long, List<Integer>> countedPerOrganization = new HashMap<java.lang.Long, List<Integer>>();
		
	
	
	@Action(value = "OrganizationStat")
	public String execute() throws ParseException {
		log.debug("Stats counting started");
		schemaList = new ArrayList<String>();
		if (Config.get("schema.filter") != null) {
			String schemas = Config.get("schema.filter");
			schemaList = Arrays.asList(schemas.split(","));

		}

		json = new JSONObject();
		net.minidev.json.JSONArray array = new net.minidev.json.JSONArray();

		if (this.organizationId != null) {
			Long orgid = Long.parseLong(organizationId);
			array.add(listOrganizationOverall(orgid));
			// json.put("result", listOrganizationOverall(orgid));
			json.put("result", array);

			return SUCCESS;
		}
		
		else {
	
			List<Organization> orgs = DB.getOrganizationDAO().findAll();		
			if (Config.get("orgtargets") != null) {
				log.debug("orgtargets ");

				net.minidev.json.JSONArray jsontargets = getGoalTargets();
				if (jsontargets != null) {
					Iterator it = jsontargets.iterator();

					while (it.hasNext()) {
						JSONObject orgtarget = (JSONObject) it.next();
						Integer orgid = (Integer) orgtarget.get("orgID");
						array.add(listOrganizationOverall(orgid.longValue()));

					}
				}
				json.put("result", array);

				return SUCCESS;
			}

			else if (Config.get("orgstatsignore") != null) {
				log.debug("orgstatsignore ");

				String str = Config.get("orgstatsignore");
				List<String> ignoreList = Arrays.asList(str.split(","));
				for (Organization org : orgs) {
					if (ignoreList.indexOf(Long.toString(org.getDbID())) == -1) {
						array.add(listOrganizationOverall(org.getDbID()));
					}
				}
				json.put("result", array);

				return SUCCESS;
			}
			else if (Config.get("meta.goals") != null) {
				log.debug("Meta Goals !!!! ");
				// List<Organization> orglist = new ArrayList<Organization>();
				List<Tuple<String, String>> properties = new ArrayList<Tuple<String, String>>();
				for (Organization org : orgs) {
					log.debug(org.getEnglishName());
					properties = Meta.getAllProperties(org);
					//if (org.getCurrentTarget() == null){
					if (properties.isEmpty()) {
						log.debug("Skiped organizaion: "+ org.getEnglishName());
						continue;
					}
					else {	log.debug("Started collecting stats for "
								+ org.getEnglishName());
						array.add(listOrganizationOverall(org.getDbID()));
					}
					
				}
				json.put("result", array);

				return SUCCESS;
			} else {
				log.debug("ELSE!!!! ");

				for (Organization org : orgs) {
					array.add(listOrganizationOverall(org.getDbID()));
				}
				if ((Config.get("custom.name") != null)
						&& (Config.get("custom.name").equals("fashion"))) {
					log.debug("fashion ");

					OrgOAIBeanFactory orgoaibeanfactory = new OrgOAIBeanFactory();
					HashMap<String, HashMap<String, String>> bigMap = orgoaibeanfactory
							.getOrganizationitemnumberpernamespace();
					Iterator it = bigMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						String namespace = pairs.getKey().toString();
						Map itemCountmap = (Map) pairs.getValue();

						Iterator<Object> orgit = array.iterator();
						while (orgit.hasNext()) {
							net.minidev.json.JSONArray jsOrg = (net.minidev.json.JSONArray) orgit
									.next();
							net.minidev.json.JSONObject orgN = (JSONObject) jsOrg
									.get(0);

							String orgname = (String) orgN.get("Name");
							Organization org = DB.getOrganizationDAO()
									.findByFName(orgname);
							Long orgid = org.getDbID();
							if (itemCountmap.containsKey(orgid.toString())) {
								JSONObject js = new JSONObject();
								js.put("OAI commited in " + namespace, Long
										.parseLong((String) itemCountmap
												.get(orgid.toString())));
								jsOrg.add(js);
							} else {
								JSONObject js = new JSONObject();
								js.put("OAI commited in " + namespace, 0);
								jsOrg.add(js);
							}

						}
					}
				}
				json.put("result", array);

				return SUCCESS;
			}
		}
	}

	
	public net.minidev.json.JSONArray listProjectOverall() throws ParseException{
		
		itemsImported= 0;
		itemsTransformed = 0;
		itemsPublished = 0;

		countedPerOrganization.clear();
		
		List<Organization> orgs =  DB.getOrganizationDAO().findAll();
		for (Organization org:orgs){
			List lista = new ArrayList<Integer>();
			lista.add(0);
			lista.add(0);
			lista.add(0);
			countedPerOrganization.put(org.getDbID(), lista);
		}
		try {
			DB.getDatasetDAO().onAll(new ApplyI<Dataset> () {
			public void apply(Dataset ds) throws Exception {
				int transformed = 0;
				int imported = 0;
					if (!(ds instanceof Transformation) && !(ds instanceof AnnotatedDataset)) {
						if (ds.getItemCount() != -1) {
							List<Integer> list = countedPerOrganization.get(ds.getOrganization().getDbID());
							imported  = ds.getValidItemCount() + list.get(0);
							list.set(0,imported);
							countedPerOrganization.put(ds.getOrganization().getDbID(), list);
						}
					}
					if ((ds instanceof Transformation))	
						if (ds.getItemCount() != -1) {
							List<Integer> list = countedPerOrganization.get(ds.getOrganization().getDbID());
							transformed  = ds.getValidItemCount() + list.get(1);
							list.set(1,transformed);
							countedPerOrganization.put(ds.getOrganization().getDbID(), list);
						}						
			}
		},"", true );
		} catch( Exception e ) {}
		
		try {
			DB.getPublicationRecordDAO().onAll(new ApplyI<PublicationRecord> () {
				public void apply(PublicationRecord pr) throws Exception {
					int published;
					String targetSchemaName = "";
					
						if ((Config.get("custom.name") != null)
								&& (Config.get("custom.name").equals("euscreenxl"))) {
							if (pr.getStatus().equals("OK")) {
							List<Integer> list = countedPerOrganization.get(pr.getOrganization().getDbID());
							published  = pr.getPublishedItemCount() + list.get(2);
							list.set(2,published);
							countedPerOrganization.put(pr.getOrganization().getDbID(), list);
							
							if (pr.getPublishedDataset().getSchema() != null) {
								targetSchemaName = pr.getPublishedDataset().getSchema()
										.getName();
								if (countedItemsperSchema.containsKey(targetSchemaName)) {
									countedItemsperSchema.put(targetSchemaName,
											countedItemsperSchema.get(targetSchemaName)
													+ pr.getPublishedItemCount());
								}
							}
						}
						}
						else if (!((Config.get("custom.name") != null) && (Config
								.get("custom.name").equals("euscreenxl")))) {
							if (pr.getPublishedDataset().isOk()){

							List<Integer> list = countedPerOrganization.get(pr.getOrganization().getDbID());
							published  = pr.getPublishedDataset().getValidItemCount() + list.get(2);
							list.set(2,published);
							countedPerOrganization.put(pr.getOrganization().getDbID(), list);
							
							if (pr.getPublishedDataset().getSchema() != null) {
								targetSchemaName = pr.getPublishedDataset().getSchema()
										.getName();
								if (countedItemsperSchema.containsKey(targetSchemaName)) {
									countedItemsperSchema.put(targetSchemaName,
											countedItemsperSchema.get(targetSchemaName)
													+ pr.getPublishedDataset()
															.getValidItemCount());
								}
							}
						}

					}

				
							
				}
			},"", true );

			} catch( Exception e ) {} 
		
		
		
		return null;
	}
	
	public net.minidev.json.JSONArray listOrganizationOverall(
			Long organizationId) throws ParseException {

		itemsImported= 0;
		itemsTransformed = 0;
		try {

		//	DB.getDatasetDAO().onAll(new ApplyI<Dataset> () {
			DB.getDatasetDAO().onAllStateless(new ApplyI<Dataset> () {
			public void apply(Dataset ds) throws Exception {
					if (!(ds instanceof Transformation) && !(ds instanceof AnnotatedDataset)) {
						if (ds.getItemCount() != -1) {
							itemsImported += ds.getItemCount();
						}
					}
					if ((ds instanceof Transformation))	
						itemsTransformed +=    ds.getValidItemCount();
					
							
			}
		}, "organization = " + organizationId);

		} catch( Exception e ) {} 
		
		log.debug(DB.getOrganizationDAO()
				.getById(organizationId, false).getEnglishName() + "  imported and transformed counted  ");
	
		
		countedItemsperSchema.clear();
		if (!schemaList.isEmpty()) {
			for (String str : schemaList) {
				countedItemsperSchema.put(str, 0);

			}
		}

		itemsPublished = 0;

		
		
		
		/*List<PublicationRecord> prs = new ArrayList<PublicationRecord>();
		prs = DB.getPublicationRecordDAO().findByOrganization(
				DB.getOrganizationDAO().getById(organizationId, false));*/
		
		try {
			DB.getPublicationRecordDAO().onAll(new ApplyI<PublicationRecord> () {
				public void apply(PublicationRecord pr) throws Exception {

					String targetSchemaName = "";
					
						if ((Config.get("custom.name") != null)
								&& (Config.get("custom.name").equals("euscreenxl"))) {
							if (pr.getStatus().equals("OK")) {
							itemsPublished += pr.getPublishedItemCount();
							if (pr.getPublishedDataset().getSchema() != null) {
								targetSchemaName = pr.getPublishedDataset().getSchema()
										.getName();
								if (countedItemsperSchema.containsKey(targetSchemaName)) {
									countedItemsperSchema.put(targetSchemaName,
											countedItemsperSchema.get(targetSchemaName)
													+ pr.getPublishedItemCount());
								}
							}
						}
						}
						else if (!((Config.get("custom.name") != null) && (Config
								.get("custom.name").equals("euscreenxl")))) {
							if (pr.getPublishedDataset().isOk()){
							itemsPublished += pr.getPublishedDataset()
									.getValidItemCount();

							if (pr.getPublishedDataset().getSchema() != null) {
								targetSchemaName = pr.getPublishedDataset().getSchema()
										.getName();
								if (countedItemsperSchema.containsKey(targetSchemaName)) {
									countedItemsperSchema.put(targetSchemaName,
											countedItemsperSchema.get(targetSchemaName)
													+ pr.getPublishedDataset()
															.getValidItemCount());
								}
							}
						}

					}

				
							
				}
			}, "organization = " + organizationId, false );

			} catch( Exception e ) {} 
		
		
		
		

		/*for (PublicationRecord pr : prs) {
			String targetSchemaName = "";
			
				if ((Config.get("custom.name") != null)
						&& (Config.get("custom.name").equals("euscreenxl"))) {
					if (pr.getStatus().equals("OK")) {
					itemsPublished += pr.getPublishedItemCount();
					if (pr.getPublishedDataset().getSchema() != null) {
						targetSchemaName = pr.getPublishedDataset().getSchema()
								.getName();
						if (countedItemsperSchema.containsKey(targetSchemaName)) {
							countedItemsperSchema.put(targetSchemaName,
									countedItemsperSchema.get(targetSchemaName)
											+ pr.getPublishedItemCount());
						}
					}
				}
				}
				else if (!((Config.get("custom.name") != null) && (Config
						.get("custom.name").equals("euscreenxl")))) {
					if (pr.getPublishedDataset().isOk()){
					itemsPublished += pr.getPublishedDataset()
							.getValidItemCount();

					if (pr.getPublishedDataset().getSchema() != null) {
						targetSchemaName = pr.getPublishedDataset().getSchema()
								.getName();
						if (countedItemsperSchema.containsKey(targetSchemaName)) {
							countedItemsperSchema.put(targetSchemaName,
									countedItemsperSchema.get(targetSchemaName)
											+ pr.getPublishedDataset()
													.getValidItemCount());
						}
					}
				}

			}

		}*/
		
		//DB.getSession().clear();
		log.debug(DB.getOrganizationDAO()
				.getById(organizationId, false).getEnglishName() + "  published  counted  ");


		net.minidev.json.JSONArray jsonAr = new net.minidev.json.JSONArray();

		JSONObject jName = new JSONObject();
		jName.put("Name", DB.getOrganizationDAO()
				.getById(organizationId, false).getEnglishName());
		jsonAr.add(jName);

		JSONObject jImported = new JSONObject();
		jImported.put("Imported", itemsImported);
		jsonAr.add(jImported);

		JSONObject jTransformed = new JSONObject();
		jTransformed.put("Transformed", itemsTransformed);
		jsonAr.add(jTransformed);

		JSONObject jPulibshed = new JSONObject();
		jPulibshed.put("Published", itemsPublished);
		jsonAr.add(jPulibshed);

		if (!countedItemsperSchema.isEmpty()) {
			Set<String> schemaKeys = countedItemsperSchema.keySet();
			Iterator sit = schemaKeys.iterator();
			JSONObject jPulibshedTo = new JSONObject();
			while (sit.hasNext()) {
				String schema = (String) sit.next();
				jPulibshedTo = new JSONObject();
				jPulibshedTo.put("Published to " + schema,
						countedItemsperSchema.get(schema));
				jsonAr.add(jPulibshedTo);
			}
		}
		Organization org = DB.getOrganizationDAO().getById(organizationId,
				false);
		if (Config.get("meta.goals") != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			if (org.getFinalTarget() != null) {
				Tuple<Date, Integer> ftp = org.getFinalTarget();
				JSONObject jftarget = new JSONObject();
				jftarget.put("Final goal " + sdf.format(ftp.u), ftp.v);
				jsonAr.add(jftarget);
			}
			if (org.getCurrentTarget() != null) {
				Tuple<Date, Integer> ctp = org.getCurrentTarget();

				JSONObject jctarget = new JSONObject();
				jctarget.put("Current goal " + sdf.format(ctp.u), ctp.v);
				jsonAr.add(jctarget);

			}
			log.debug(DB.getOrganizationDAO()
					.getById(organizationId, false).getEnglishName() + "meta goal added  ");

		}
		if (Config.has("orgtargets")) {

			net.minidev.json.JSONArray jsontargets = getGoalTargets();

			if (jsontargets != null) {
				Iterator it = jsontargets.iterator();

				while (it.hasNext()) {
					JSONObject orgtarget = (JSONObject) it.next();
					Integer orgid = (Integer) orgtarget.get("orgID");

					JSONObject aggregationJson = new JSONObject();
					JSONObject coreJson = new JSONObject();

					if (orgid.longValue() == organizationId) {

						aggregationJson.put("Aggregation goal ",
								orgtarget.get("aggregation"));
						coreJson.put("Core goal ", orgtarget.get("core"));

						jsonAr.add(aggregationJson);
						
						jsonAr.add(coreJson);

						break;
					}
				}
			}
		}

		log.debug(org.getName() + " Stat made");
		return jsonAr;
	}

	public net.minidev.json.JSONArray getGoalTargets() throws ParseException {

		net.minidev.json.JSONArray jsontargets = null;

		if (Config.has("orgtargets")) {

			String targets = Config.get("orgtargets");

			JSONObject thejson = null;

			thejson = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE)
					.parse(targets);
			jsontargets = (net.minidev.json.JSONArray) thejson
					.get("targetorglist");

		}
		return jsontargets;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public JSONObject getJson() {
		return json;
	}

}