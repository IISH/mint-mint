package gr.ntua.ivml.mint.report;

import gr.ntua.ivml.mint.actions.UrlApi;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.util.Config;
import gr.ntua.ivml.mint.util.OrganizationList;
import gr.ntua.ivml.mint.util.Tuple;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class OrganizationProgressBeanFactory {
	String organizationId;
	Date startDate;
	Date endDate;

	  PublicationDetailsBeanFactory publicationDetailsBeanFactory;
	  TransformationDetailsBeanFactory transformationDetailsBeanFactory;
	  OrgOAIBeanFactory orgoaibeanfactory;
	  OrganizationGoalsSummaryBeanFactory orggoalsFactory;

	public OrganizationProgressBeanFactory(String organizationId,Date startdate,
			Date enddate) {
		//	System.out.println("DEBUG, BEAN MADE  " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		super();
		this.organizationId = organizationId;
		this.startDate = startdate;
		this.endDate = enddate;

		this.transformationDetailsBeanFactory = new TransformationDetailsBeanFactory(
			startDate, endDate);
		this.publicationDetailsBeanFactory = new PublicationDetailsBeanFactory(
			 startDate, endDate);
		this.orggoalsFactory = new OrganizationGoalsSummaryBeanFactory(
				startDate, endDate);
		this.orgoaibeanfactory = new OrgOAIBeanFactory();
	}
	
	
	public OrganizationProgressBeanFactory(Date startdate,
			Date enddate) {

		super();
		System.out.println("DEBUG, CREATED FACTORY  " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		this.startDate = startdate;
		this.endDate = enddate;

		this.transformationDetailsBeanFactory = new TransformationDetailsBeanFactory(
			startDate, endDate);
		System.out.println("DEBUG, CREATED trans FACTORies  " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		this.publicationDetailsBeanFactory = new PublicationDetailsBeanFactory(
			 startDate, endDate);
		System.out.println("DEBUG, CREATED pubs FACTORies  " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		this.orggoalsFactory = new OrganizationGoalsSummaryBeanFactory(
				startDate, endDate);
		System.out.println("DEBUG, CREATED goals FACTORies  " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		this.orgoaibeanfactory = new OrgOAIBeanFactory();
		System.out.println("DEBUG, CREATED oai FACTORies  " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		System.out.println("DEBUG, CREATED smallest FACTORies  " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationid(String organizationId) {
		this.organizationId = organizationId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	

	public List<OrganizationProgressDetailsBean> getOrgProgressbeans() {
		UrlApi api = new UrlApi();
		api.setOrganizationId(this.organizationId);
		JSONObject json = api.listOrganizations();
		JSONArray result = (JSONArray) json.get("result");
		List<OrganizationProgressDetailsBean> all = new ArrayList<OrganizationProgressDetailsBean>();
		Iterator it = result.iterator();
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();
			String organizationId = jsonObject.get("dbID").toString();
			
			List<String> ignoreList = new ArrayList<String>();
			if (Config.has("orgstatsignore")) {
				String str = Config.get("orgstatsignore");
				ignoreList = Arrays.asList(str.split(","));
				if (ignoreList.indexOf(organizationId) != -1){
					continue;
				}
			}
			String organizationName = jsonObject.getString("englishName");
			if (organizationName.equals("NTUA"))
				continue;
			if (organizationName.equals("Old euscreen data")) continue;			
			List<OrganizationProgressDetailsBean> progbeans = new ArrayList<OrganizationProgressDetailsBean>();
			progbeans = getOrgProgressbeans(organizationId);
			all.addAll(progbeans);
		}

		return all;

		
		
		
	}
	
	public List<OrganizationProgressDetailsBean> getOrgProgressbeans3() throws ParseException, net.minidev.json.parser.ParseException {
		List<OrganizationProgressDetailsBean> all = new ArrayList<OrganizationProgressDetailsBean>();
		List <Organization> orglist = new OrganizationList().listOrganizationToReport();
		for (Organization org:orglist){
		
			List<OrganizationProgressDetailsBean> beanCollection = new ArrayList<OrganizationProgressDetailsBean>();		
			Long orgid = org.getDbID();		
			/*Tuple<List<PublicationDetailsBean>, Integer> pubTuple = getPublications3(orgid.toString());	
			publicationsbeanCollection  = pubTuple.u;*/
			beanCollection = getOrgProgressbeans3(orgid.toString());
			all.addAll(beanCollection);
		}
		
		return all;
	}

	
	public List<OrganizationProgressDetailsBean> getOrgProgressbeans3(String orgid){
		
		List<OrganizationProgressDetailsBean> organizations = new ArrayList<OrganizationProgressDetailsBean>();

		Organization org = DB.getOrganizationDAO().findById(Long.parseLong(orgid), false);
		String name = org.getEnglishName();
		String country = org.getCountry();
	
	//	System.out.println("DEBUG, STARTED  " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		List<PublicationDetailsBean> publicationsbeanCollection = null;
		List<TransformationDetailsBean> transformationsbeanCollection = null;
		List<OrganizationGoalsSummaryBean> goalsbeanColleection = null;
		List<OrgOAIBean> orgoaibeanCollection = null;
		
		
		Tuple<List<TransformationDetailsBean>, Integer> transfTuple = transformationDetailsBeanFactory.getTransformations3(orgid);
		transformationsbeanCollection = transfTuple.u;
		Integer transformed = transfTuple.v;

	//	System.out.println("DEBUG, TRANSFORMATIONS DONE  " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		Tuple<List<PublicationDetailsBean>, Integer> pubTuple = publicationDetailsBeanFactory.getPublications3(orgid);
		publicationsbeanCollection  = pubTuple.u;
		Integer published  = pubTuple.v;
	//	System.out.println("DEBUG, PUBLICATIONS DONE  " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		orgoaibeanCollection = orgoaibeanfactory.getOrgOAIBeans2(orgid);
//		System.out.println("DEBUG, OAI HISTORY DONE  " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));					
//		Integer oaipublished = orgoaibeanfactory.getItemCount(orgid);
		Integer oaipublished;
		if (orgoaibeanCollection.isEmpty()) oaipublished = 0 ;
		else 
		oaipublished = orgoaibeanCollection.get(0).uniqueItems;
//		System.out.println("DEBUG, OAI COUNTS CURRENT DONE " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));				
		goalsbeanColleection = orggoalsFactory.getOrgGoalBeans(orgid,transformed,published,oaipublished);
//		System.out.println("DEBUG, GOALS DONE  " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		OrganizationProgressDetailsBean orgBean = new OrganizationProgressDetailsBean(
				name, country, transformationsbeanCollection,
				publicationsbeanCollection, goalsbeanColleection,
				orgoaibeanCollection);
		organizations.add(orgBean);
	//	System.out.println("DEBUG, BEAN MADE  " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		return organizations;
	}
	
	
	public List<OrganizationProgressDetailsBean> getOrgProgressbeans(
			String orgid) {
		UrlApi api = new UrlApi();
		api.setOrganizationId(orgid);
		JSONObject json = api.listOrganizations();
		JSONArray result = (JSONArray) json.get("result");
		JSONObject jsonObject = result.getJSONObject(0);
		return getOrgProgressbeans(jsonObject);

		
		
	}

	public List<OrganizationProgressDetailsBean> getOrgProgressbeans(
			JSONObject jsonObject) {

		List<OrganizationProgressDetailsBean> organizations = new ArrayList<OrganizationProgressDetailsBean>();

		String name = jsonObject.get("englishName").toString();
		String country = jsonObject.get("country").toString();
		String organizationId = jsonObject.get("dbID").toString();
		
	//	this.populateFactories(organizationId);

		List<PublicationDetailsBean> publicationsbeanCollection = null;
		List<TransformationDetailsBean> transformationsbeanCollection = null;
		List<OrganizationGoalsSummaryBean> goalsbeanColleection = null;
		List<OrgOAIBean> orgoaibeanCollection = null;
		
		Tuple<List<TransformationDetailsBean>, Integer> transfTuple = transformationDetailsBeanFactory.getTransformations3(organizationId);
		transformationsbeanCollection = transfTuple.u;
		Integer transformed = transfTuple.v;

		
		Tuple<List<PublicationDetailsBean>, Integer> pubTuple = publicationDetailsBeanFactory.getPublications3(organizationId);
		publicationsbeanCollection  = pubTuple.u;
		Integer published  = pubTuple.v;
		
		
		//System.out.println("DEBUG, finished making published lists  " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		orgoaibeanCollection = orgoaibeanfactory.getOrgOAIBeans2(organizationId);
		//System.out.println("DEBUG, finished making oai published lists  " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		//Integer transformed = transformationDetailsBeanFactory.getTransformedItems(organizationId);
		//Integer published = publicationDetailsBeanFactory.getPublishedItems(organizationId);
		
		Integer oaipublished = orgoaibeanfactory.getItemCount(organizationId);
		
		//System.out.println("DEBUG, passing "+" "+transformed+" "+ published+" "+ oaipublished);

		goalsbeanColleection = orggoalsFactory.getOrgGoalBeans(organizationId,transformed,published,oaipublished);

		//System.out.println("DEBUG, finished making goals  lists  " + "for organization "+name+" "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		OrganizationProgressDetailsBean orgBean = new OrganizationProgressDetailsBean(
				name, country, transformationsbeanCollection,
				publicationsbeanCollection, goalsbeanColleection,
				orgoaibeanCollection);

		organizations.add(orgBean);
		return organizations;
	}
	
	/*protected void populateFactories(String organizationId) {
		transformationDetailsBeanFactory.setOrganizationId(organizationId);
		publicationDetailsBeanFactory.setOrganizationId(organizationId);
		orgoaibeanfactory.setOrganizationId(organizationId);
	//	orggoalsFactory.setOrganizationid(organizationId);	
	}
*/

}
