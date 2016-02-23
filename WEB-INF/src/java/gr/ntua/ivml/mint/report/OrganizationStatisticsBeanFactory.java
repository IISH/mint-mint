package gr.ntua.ivml.mint.report;

import gr.ntua.ivml.mint.actions.UrlApi;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.util.Config;
import gr.ntua.ivml.mint.util.OrganizationList;
import gr.ntua.ivml.mint.util.Tuple;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class OrganizationStatisticsBeanFactory {

//	DataUploadDetailsBeanFactory datauploadDetailsFactory;
	TransformationDetailsBeanFactory transformationDetailsBeanFactory;
	PublicationDetailsBeanFactory publicationDetailsBeanFactory;
	OrgOAIBeanFactory orgOaiBeanFactory;



	private Date startDate;
	private Date endDate;

	public OrganizationStatisticsBeanFactory(Date startDate, Date endDate) {
		super();

		this.startDate = startDate;
		this.endDate = endDate;
		
	//	this.datauploadDetailsFactory = new DataUploadDetailsBeanFactory(startDate, endDate);
		this.transformationDetailsBeanFactory = new TransformationDetailsBeanFactory(startDate, endDate);
		this.publicationDetailsBeanFactory = new PublicationDetailsBeanFactory(startDate, endDate);
		this.orgOaiBeanFactory = new OrgOAIBeanFactory();
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

	/*public List<OrganizationStatisticsBean> getOrgStatisticsBeans() {

		
		
		UrlApi api = new UrlApi();
		JSONObject json = api.listOrganizations();
		return getOrgStatisticsBeans(json);

	}*/
	
	public List<OrganizationStatisticsBean> getOrgStatisticsBeans3() throws ParseException, net.minidev.json.parser.ParseException {
		List<OrganizationStatisticsBean> all = new ArrayList<OrganizationStatisticsBean>();
		List <Organization> orglist = new OrganizationList().listOrganizationToReport();
		for (Organization org:orglist){
		
			List<OrganizationStatisticsBean> beanCollection = new ArrayList<OrganizationStatisticsBean>();		
			Long orgid = org.getDbID();		
			/*Tuple<List<PublicationDetailsBean>, Integer> pubTuple = getPublications3(orgid.toString());	
			publicationsbeanCollection  = pubTuple.u;*/
			beanCollection = getOrgStatisticsBeans3(orgid.toString());
			all.addAll(beanCollection);
		}
		
		return all;
		
	}

	public List<OrganizationStatisticsBean> getOrgStatisticsBeans3(String orgid){
		
		
		List<OrganizationStatisticsBean> organizations = new ArrayList<OrganizationStatisticsBean>();
		
		Organization org = DB.getOrganizationDAO().findById(Long.parseLong(orgid), false);
		String name = org.getEnglishName();
		String country = org.getCountry();
		
		
		/*List<DataUploadDetailsBean> uploadsbeanCollection = null;
		List<TransformationDetailsBean> transformationsbeanCollection = null;
		List<MappingDetailsBean> mappingsbeanCollection = null;
		List<PublicationDetailsBean> publicationsbeanCollection = null;
		List<OaiPublicationDetailsBean> oaipublicationbeanCollection = null;
		List<OrgOAIBean> orgoaibeanCollection = null;
		*/
		/*
		Tuple<List<DataUploadDetailsBean>, Integer> importsTuple =  datauploadDetailsFactory.getDataUploads3(orgid);
	//	uploadsbeanCollection = importsTuple.u;
		Integer imported = importsTuple.v;

		Tuple<List<TransformationDetailsBean>, Integer> transfTuple = transformationDetailsBeanFactory.getTransformations3(orgid);
	//	transformationsbeanCollection = transfTuple.u;
		Integer transformed = transfTuple.v;*/

		
		Tuple<Tuple, Tuple> tuple =  transformationDetailsBeanFactory.getUplodsTransformations(orgid);
		
		Tuple<List<DataUploadDetailsBean>, Integer> importsTuple = tuple.u;
		//uploadsbeanCollection = importsTuple.u;
		Integer imported = importsTuple.v;
		
		Tuple<List<TransformationDetailsBean>, Integer> transfTuple = tuple.v;
	//	transformationsbeanCollection = transfTuple.u;
		Integer transformed = transfTuple.v;
		
		Tuple<List<PublicationDetailsBean>, Integer> pubTuple = publicationDetailsBeanFactory.getPublications3(orgid);
	//	publicationsbeanCollection  = pubTuple.u;
		Integer published  = pubTuple.v;
	
		
		//orgoaibeanCollection = orgOaiBeanFactory.getOrgOAIBeans2(orgid);
		Integer oaipublished = orgOaiBeanFactory.getItemCount(orgid);
		
		OrganizationStatisticsBean organizationStatisticsbean = new OrganizationStatisticsBean(
				name, country, imported, transformed,
				published, oaipublished);

		organizations.add(organizationStatisticsbean);
		return organizations;
		
	}

	/*public List<OrganizationStatisticsBean> getOrgStatisticsBeans(
			JSONObject json) {
		List<OrganizationStatisticsBean> organizations = new ArrayList<OrganizationStatisticsBean>();
		JSONArray result = (JSONArray) json.get("result");

		Iterator it = result.iterator();
		int uploadedItems = 0;
		int transformedItems = 0;
		int publishedItems = 0 ;
		int oaicommited = 0 ;
		
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();
			String organizationId = jsonObject.get("dbID").toString();
			String name = jsonObject.get("englishName").toString();
			String country = jsonObject.get("country").toString();
		
			 uploadedItems = 0;
			 transformedItems = 0;
			 publishedItems = 0 ;
			 oaicommited = 0 ;
			
			List<String> ignoreList = null ;
			if (name.equals("NTUA"))
				continue;
			
			if (Config.has("orgstatsignore")) {
				String str = Config.get("orgstatsignore");
				ignoreList = Arrays.asList(str.split(","));
			}
			if (ignoreList.indexOf(organizationId) == -1){
				continue;
			}

			 uploadedItems = datauploadDetailsFactory.getUploadedItems(organizationId);
			
			
			 transformedItems = transformationDetailsBeanFactory.getTransformedItems(organizationId);
			
			 publishedItems = publicationDetailsBeanFactory.getPublishedItems(organizationId);
			
			 oaicommited = orgOaiBeanFactory.getItemCount(organizationId);


			OrganizationStatisticsBean organizationStatisticsbean = new OrganizationStatisticsBean(
					name, country, uploadedItems, transformedItems,
					publishedItems, oaicommited);

			organizations.add(organizationStatisticsbean);
		}

		//Collections.sort(organizations);
		return organizations;

	}*/

}
