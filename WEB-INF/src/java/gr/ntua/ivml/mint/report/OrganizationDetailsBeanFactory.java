package gr.ntua.ivml.mint.report;

import gr.ntua.ivml.mint.actions.UrlApi;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.util.OrganizationList;
import gr.ntua.ivml.mint.util.Tuple;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class OrganizationDetailsBeanFactory {

	String organizationId;
	Date startDate;
	Date endDate;

	PublicationDetailsBeanFactory publicationDetailsBeanFactory;
	TransformationDetailsBeanFactory transformationDetailsBeanFactory;
	MappingDetailsBeanFactory mappingDetailsBeanFactory;
	OaiPublicationDetailsBeanFactory oaipublicationbeanFactory;
	DataUploadDetailsBeanFactory datauploadDetailsFactory;
	OrgOAIBeanFactory orgoaibeanfactory;

	public OrganizationDetailsBeanFactory(String id, Date startdate,
			Date enddate) {
		super();

		this.organizationId = id;
		this.startDate = startdate;
		this.endDate = enddate;

		this.publicationDetailsBeanFactory = new PublicationDetailsBeanFactory(
				id, startDate, endDate);
		this.transformationDetailsBeanFactory = new TransformationDetailsBeanFactory(
				id, startDate, endDate);
		this.mappingDetailsBeanFactory = new MappingDetailsBeanFactory(id,
				startDate, endDate);
		this.oaipublicationbeanFactory = new OaiPublicationDetailsBeanFactory(
				id, startDate, endDate);
		this.datauploadDetailsFactory = new DataUploadDetailsBeanFactory(
				organizationId, startDate, endDate);
		this.orgoaibeanfactory = new OrgOAIBeanFactory();
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

	public List<OrganizationDetailsBean> getOrgDetailsBeans() throws ParseException, net.minidev.json.parser.ParseException {

		UrlApi api = new UrlApi();
		api.setOrganizationId(organizationId);
		JSONObject json = api.listOrganizations();
		JSONArray result = (JSONArray) json.get("result");
		JSONObject jsonObject = result.getJSONObject(0);
		return this.getOrgDetailsBeans(jsonObject);

	}
	
	public List<OrganizationDetailsBean> getOrgDetailsBeans3() throws ParseException, net.minidev.json.parser.ParseException {

		
		
		
		List<OrganizationDetailsBean> all = new ArrayList<OrganizationDetailsBean>();
		List <Organization> orglist = new OrganizationList().listOrganizationToReport();
		for (Organization org:orglist){
		
			List<OrganizationDetailsBean> beanCollection = new ArrayList<OrganizationDetailsBean>();		
			Long orgid = org.getDbID();		
			/*Tuple<List<PublicationDetailsBean>, Integer> pubTuple = getPublications3(orgid.toString());	
			publicationsbeanCollection  = pubTuple.u;*/
			beanCollection = getOrgDetailsBeans3(orgid.toString());
			all.addAll(beanCollection);
		}
		
		return all;

	}
	
	public List<OrganizationDetailsBean> getOrgDetailsBeans3(String orgid) throws ParseException, net.minidev.json.parser.ParseException{
		List<OrganizationDetailsBean> organizations = new ArrayList<OrganizationDetailsBean>();

		Organization org = DB.getOrganizationDAO().findById(Long.parseLong(orgid), false);
		String name = org.getEnglishName();
		String country = org.getCountry();

		List<DataUploadDetailsBean> uploadsbeanCollection = null;
		List<TransformationDetailsBean> transformationsbeanCollection = null;
		List<MappingDetailsBean> mappingsbeanCollection = null;
		List<PublicationDetailsBean> publicationsbeanCollection = null;
		List<OaiPublicationDetailsBean> oaipublicationbeanCollection = null;
		List<OrgOAIBean> orgoaibeanCollection = null;
		
		
		Tuple<List<DataUploadDetailsBean>, Integer> importsTuple =  datauploadDetailsFactory.getDataUploads3(orgid);
		uploadsbeanCollection = importsTuple.u;
		Integer imported = importsTuple.v;

		Tuple<List<TransformationDetailsBean>, Integer> transfTuple = transformationDetailsBeanFactory.getTransformations3(orgid);
		transformationsbeanCollection = transfTuple.u;
		Integer transformed = transfTuple.v;

		
		Tuple<List<PublicationDetailsBean>, Integer> pubTuple = publicationDetailsBeanFactory.getPublications3(orgid);
		publicationsbeanCollection  = pubTuple.u;
		Integer published  = pubTuple.v;
		
		mappingsbeanCollection = mappingDetailsBeanFactory.getMappings();

		
		oaipublicationbeanCollection = oaipublicationbeanFactory.getOaiPublicationDetailsBeans3(orgid);
		
		
		orgoaibeanCollection = orgoaibeanfactory.getOrgOAIBeans2(orgid);
		
		

		OrganizationDetailsBean organizationdetailsbean = new OrganizationDetailsBean(
				name, country, uploadsbeanCollection, mappingsbeanCollection,
				transformationsbeanCollection, publicationsbeanCollection,
				oaipublicationbeanCollection, orgoaibeanCollection);
		organizations.add(organizationdetailsbean);
		return organizations;
	}

		
		
		
		
		


	
	
	public List<OrganizationDetailsBean> getOrgDetailsBeans(
			JSONObject jsonObject) throws ParseException, net.minidev.json.parser.ParseException {

		List<OrganizationDetailsBean> organizations = new ArrayList<OrganizationDetailsBean>();

		String name = jsonObject.get("englishName").toString();
		String country = jsonObject.get("country").toString();
		String publishAllowed = jsonObject.get("publishAllowed").toString();

		List<DataUploadDetailsBean> uploadsbeanCollection = null;
		List<PublicationDetailsBean> publicationsbeanCollection = null;
		List<TransformationDetailsBean> transformationsbeanCollection = null;
		List<MappingDetailsBean> mappingsbeanCollection = null;
		List<OaiPublicationDetailsBean> oaipublicationbeanCollection = null;
		List<OrgOAIBean> orgoaibeanCollection = null;
		
		/*System.out.println("DEBUG,lists starting" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		uploadsbeanCollection = datauploadDetailsFactory.getUploads(organizationId);
		System.out.println("DEBUG,uploadslist done" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		transformationsbeanCollection = transformationDetailsBeanFactory
				.getTransformations(organizationId);
		System.out.println("DEBUG,transformations done" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		publicationsbeanCollection = publicationDetailsBeanFactory
				.getPublications(organizationId);
		System.out.println("DEBUG,publications done" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));*/
		
		
		
		/*Tuple<List<DataUploadDetailsBean>, Integer> importsTuple =  datauploadDetailsFactory.getDataUploads3(organizationId);
		uploadsbeanCollection = importsTuple.u;
		Integer imported = importsTuple.v;*/

		
		/*Tuple<List<TransformationDetailsBean>, Integer> transfTuple = transformationDetailsBeanFactory.getTransformations3(organizationId);
		transformationsbeanCollection = transfTuple.u;
		Integer transformed = transfTuple.v;*/

		Tuple<Tuple,Tuple> tuple =  transformationDetailsBeanFactory.getUplodsTransformations(organizationId);
		
		Tuple<List<DataUploadDetailsBean>, Integer> importsTuple = tuple.u;
		uploadsbeanCollection = importsTuple.u;
		Integer imported = importsTuple.v;
		
		Tuple<List<TransformationDetailsBean>, Integer> transfTuple = tuple.v;
		transformationsbeanCollection = transfTuple.u;
		Integer transformed = transfTuple.v;
		
		Tuple<List<PublicationDetailsBean>, Integer> pubTuple = publicationDetailsBeanFactory.getPublications3(organizationId);
		publicationsbeanCollection  = pubTuple.u;
		Integer published  = pubTuple.v;
		
		
		
		mappingsbeanCollection = mappingDetailsBeanFactory.getMappings();
		System.out.println("DEBUG,mappings done" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		oaipublicationbeanCollection = oaipublicationbeanFactory
				.getOaiPublicationDetailsBeans3();
		System.out.println("DEBUG,oai commits done" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
	//	orgoaibeanCollection = orgoaibeanfactory.getOrgOAIBeans();
		orgoaibeanCollection = orgoaibeanfactory.getOrgOAIBeans2(organizationId);
		System.out.println("DEBUG,oai status done" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		OrganizationDetailsBean organizationdetailsbean = new OrganizationDetailsBean(
				name, country, uploadsbeanCollection, mappingsbeanCollection,
				transformationsbeanCollection, publicationsbeanCollection,
				oaipublicationbeanCollection, orgoaibeanCollection);
		organizations.add(organizationdetailsbean);
		return organizations;
	}

}
