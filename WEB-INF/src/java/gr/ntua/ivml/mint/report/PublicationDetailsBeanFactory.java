package gr.ntua.ivml.mint.report;

import gr.ntua.ivml.mint.actions.UrlApi;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.DataUpload;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.PublicationRecord;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.util.Config;
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

public class PublicationDetailsBeanFactory {

	String organizationId = null;

	private Date startDate;
	private Date endDate;

	public PublicationDetailsBeanFactory(String id, Date startDate, Date endDate) {
		super();
		this.organizationId = id;

		this.startDate = startDate;
		this.endDate = endDate;

	}
	public PublicationDetailsBeanFactory(Date startDate, Date endDate) {
		super();

		this.startDate = startDate;
		this.endDate = endDate;

	}
	
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public List<PublicationDetailsBean> getPublications() {
		UrlApi api = new UrlApi();
		api.setOrganizationId(this.organizationId);
		JSONObject json = api.listOrganizations();
		JSONArray result = (JSONArray) json.get("result");
		List<PublicationDetailsBean> all = new ArrayList<PublicationDetailsBean>();
		Iterator it = result.iterator();
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();
			String organizationId = jsonObject.get("dbID").toString();
			String orgname = jsonObject.getString("englishName").toString();
			if (orgname.equals("Old euscreen data")) continue;
			// PublicationDetailsBeanFactory lala = new
			// PublicationDetailsBeanFactory(organizationId, startDate,
			// endDate);
			// this.organizationId = organizationId;
			List<PublicationDetailsBean> pubs = new ArrayList<PublicationDetailsBean>();
			// pubs = lala.getPublications(organizationId);
			pubs = getPublications(organizationId);
			all.addAll(pubs);
		}

		return all;

	}

	public List<PublicationDetailsBean> getPublications(String orgid) {
		UrlApi api = new UrlApi();
		api.setOrganizationId(orgid);
		JSONObject json = api.listPublications();

		return getPublications(json);

	}

	public List<PublicationDetailsBean> getPublications(JSONObject json) {
		List<PublicationDetailsBean> publications = new ArrayList<PublicationDetailsBean>();
		JSONArray result = (JSONArray) json.get("result");
		Iterator it = result.iterator();
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();
			String name = jsonObject.get("name").toString();
			Date created = new Date(0);
			;
			Date lastModified = new Date(0);
			try {
				String dateCreated = (String) jsonObject.get("created");
				created = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.parse(dateCreated);
				String dateModified = (String) jsonObject.get("lastModified");
				lastModified = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.parse(dateModified);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// date ccheck
			if (!(created.compareTo(startDate) > 0 && created
					.compareTo(endDate) < 0)
					&& !(lastModified.compareTo(startDate) > 0 && lastModified
							.compareTo(endDate) < 0)) {
				continue;
			}

			Integer itemCount = (Integer) jsonObject.get("itemCount");
			if (itemCount == -1)
				itemCount = 0;

			Integer publishedItems = (Integer) jsonObject.get("publishedItems");
			if (publishedItems == -1)
				publishedItems = 0;

			String itemizerStatus = jsonObject.get("itemizerStatus").toString();

			JSONObject creator = (JSONObject) jsonObject.get("creator");
			String creatorId = null;
			String creatorName = null;
			if (creator.has("dbID")) {
				creatorId = creator.getString("dbID").toString();
			}
			if (creator.has("name")) {
				creatorName = creator.getString("name").toString();
			}

			String organizationName = null;
			String organizationId = null;

			JSONObject org = (JSONObject) jsonObject.get("organization");
			if (org.has("dbID")) {
				organizationId = org.getString("dbID").toString();
			}
			if (org.has("name")) {
				organizationName = org.getString("name").toString();
			}

			PublicationDetailsBean publicationDetailsBean = new PublicationDetailsBean(
					name, created, lastModified, creatorId, creatorName,
					itemCount, publishedItems, itemizerStatus, organizationId,
					organizationName);
			publications.add(publicationDetailsBean);
		}

		return publications;
	}

	public Tuple<List<PublicationDetailsBean>, Integer> getPublications2(String orgid) {
		UrlApi api = new UrlApi();
		api.setOrganizationId(orgid);
		JSONObject json = api.listPublications();

		return getPublications2(json);
	}
	
	
	
	public Tuple<List<PublicationDetailsBean>,Integer>  getPublications2(JSONObject json){
		List<PublicationDetailsBean> publications = new ArrayList<PublicationDetailsBean>();
		JSONArray result = (JSONArray) json.get("result");
		Iterator it = result.iterator();
		Integer allpublished =0;
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();
			String name = jsonObject.get("name").toString();
			Date created = new Date(0);
			;
			Date lastModified = new Date(0);
			try {
				String dateCreated = (String) jsonObject.get("created");
				created = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.parse(dateCreated);
				String dateModified = (String) jsonObject.get("lastModified");
				lastModified = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.parse(dateModified);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// date ccheck
			if (!(created.compareTo(startDate) > 0 && created
					.compareTo(endDate) < 0)
					&& !(lastModified.compareTo(startDate) > 0 && lastModified
							.compareTo(endDate) < 0)) {
				continue;
			}

			Integer itemCount = (Integer) jsonObject.get("itemCount");
			if (itemCount == -1)
				itemCount = 0;

			Integer publishedItems = (Integer) jsonObject.get("publishedItems");
			if (publishedItems == -1)
				publishedItems = 0;


			allpublished+= itemCount;
			
			String itemizerStatus = jsonObject.get("itemizerStatus").toString();

			JSONObject creator = (JSONObject) jsonObject.get("creator");
			String creatorId = null;
			String creatorName = null;
			if (creator.has("dbID")) {
				creatorId = creator.getString("dbID").toString();
			}
			if (creator.has("name")) {
				creatorName = creator.getString("name").toString();
			}

			String organizationName = null;
			String organizationId = null;

			JSONObject org = (JSONObject) jsonObject.get("organization");
			if (org.has("dbID")) {
				organizationId = org.getString("dbID").toString();
			}
			if (org.has("name")) {
				organizationName = org.getString("name").toString();
			}

			PublicationDetailsBean publicationDetailsBean = new PublicationDetailsBean(
					name, created, lastModified, creatorId, creatorName,
					itemCount, publishedItems, itemizerStatus, organizationId,
					organizationName);
			publications.add(publicationDetailsBean);
		}
		
		return new Tuple<List<PublicationDetailsBean>,Integer>( publications, allpublished );
		
		
	}
	
	
	
	
	
	//return all publications for overall xlsx reports.
	public List<PublicationDetailsBean> getPublications3() throws ParseException, net.minidev.json.parser.ParseException{
		List<PublicationDetailsBean> all = new ArrayList<PublicationDetailsBean>();
		List <Organization> orglist = new OrganizationList().listOrganizationToReport();
		for (Organization org:orglist){
		
			List<PublicationDetailsBean> publicationsbeanCollection = new ArrayList<PublicationDetailsBean>();		
			Long orgid = org.getDbID();		
			Tuple<List<PublicationDetailsBean>, Integer> pubTuple = getPublications3(orgid.toString());	
			publicationsbeanCollection  = pubTuple.u;
			all.addAll(publicationsbeanCollection);
		}
		
		return all;
		
	}
	
	public Tuple<List<PublicationDetailsBean>,Integer>  getPublications3(String orgid){
		List<PublicationDetailsBean> publications = new ArrayList<PublicationDetailsBean>();
		Integer allpublished =0;
		//	List<DataUpload> result;
//		result = DB.getDataUploadDAO().findByOrganization(DB.getOrganizationDAO().getById(Long.parseLong(orgid),false));
		
//		for( DataUpload du: result ){
//						
//			if (du.getPublicationStatus().equals("OK")){	
//		
//				String name =  du.getName();
//				Date created = new Date(0);
//				Date lastModified = new Date(0);
//			//	try {
//					//String dateCreated = (String)  du.getCreated().toString();
//					//created = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(dateCreated);
//					 created = du.getCreated();
//					//String dateModified = (String) du.getLastModified().toString();
//					//lastModified = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(dateModified);
//					 lastModified = du.getLastModified();
//
//			//	} catch (ParseException e) {
//					// TODO Auto-generated catch block
//			//		e.printStackTrace();
//			//	}
//
//				// date ccheck
//				if (!(created.compareTo(startDate) > 0 && created
//						.compareTo(endDate) < 0)
//						&& !(lastModified.compareTo(startDate) > 0 && lastModified
//								.compareTo(endDate) < 0)) {
//					continue;
//				}
//
//				Integer itemCount = (Integer)  du.getItemCount();
//				if (itemCount == -1)
//					itemCount = 0;
//
//				Integer publishedItems = (Integer) du.getPublishedItemCount();
//				if (publishedItems == -1)
//					publishedItems = 0;
//
//
//				allpublished+= itemCount;
//				
//				String itemizerStatus =   du.getItemizerStatus();
//
//				User creator = du.getCreator();
//				String creatorId = null;
//				String creatorName = null;
//				if (creator.getName()!=null){
//					creatorName = creator.getName();
//				}
//				if (creator.getDbID()!=null){
//					creatorId = creator.getDbID().toString();
//				}
//				//String organizationName = null;
//				//String organizationId = null;
//
//				
//				
//					
//					//Long orgdbid = du.getOrganization().getDbID();
//					//organizationId = orgdbid.toString();
//				String organizationId = orgid;
//				String organizationName = DB.getOrganizationDAO().getById(Long.parseLong(orgid), false).getEnglishName();
//			
//				//organizationName = du.getOrganization().getEnglishName().toString();
//				
//
//				PublicationDetailsBean publicationDetailsBean = new PublicationDetailsBean(
//						name, created, lastModified, creatorId, creatorName,
//						itemCount, publishedItems, itemizerStatus, organizationId,
//						organizationName);
//				publications.add(publicationDetailsBean);
//				
//				
//			}
//		
//		}
//		
		
		
		List<PublicationRecord> resr;
		resr  = DB.getPublicationRecordDAO().findByOrganization(DB.getOrganizationDAO().getById(Long.parseLong(orgid),false));
		for( PublicationRecord pr: resr ){

			if (pr.getPublishedDataset().isOk()){

				String name =  pr.getOriginalDataset().getName();
				Date created = new Date(0);
				Date lastModified = new Date(0);
				created = pr.getStartDate() ;
				lastModified = pr.getEndDate();

				if (!(created.compareTo(startDate) > 0 && created
						.compareTo(endDate) < 0)
						&& !(lastModified.compareTo(startDate) > 0 && lastModified
								.compareTo(endDate) < 0)) {
					continue;
				}

				Integer itemCount = (Integer)  pr.getPublishedDataset().getItemCount();
				if (itemCount == -1)
					itemCount = 0;

				Integer publishedItems = pr.getPublishedDataset().getValidItemCount();
				if (publishedItems == -1)
					publishedItems = 0;


				allpublished+= itemCount;

				String itemizerStatus =   pr.getOriginalDataset().getItemizerStatus();

				User creator = pr.getOriginalDataset().getCreator();
				String creatorId = null;
				String creatorName = null;
				if (creator.getName()!=null){
					creatorName = creator.getName();
				}
				if (creator.getDbID()!=null){
					creatorId = creator.getDbID().toString();
				}

				String organizationId = orgid;
				String organizationName = DB.getOrganizationDAO().getById(Long.parseLong(orgid), false).getEnglishName();

			

			PublicationDetailsBean publicationDetailsBean = new PublicationDetailsBean(
					name, created, lastModified, creatorId, creatorName,
					itemCount, publishedItems, itemizerStatus, organizationId,
					organizationName);
			publications.add(publicationDetailsBean);

		}
	}
		return new Tuple<List<PublicationDetailsBean>,Integer>( publications, allpublished );
		
		
	}
	
	
	
	
	
	
	public Integer getPublishedItems(String orgid) {
		UrlApi api = new UrlApi();
		api.setOrganizationId(orgid);
		JSONObject json = api.listPublications();

		return getPublishedItems(json);
	}
	public Integer getPublishedItems(JSONObject json) {
		List<PublicationDetailsBean> publications = new ArrayList<PublicationDetailsBean>();
		Integer allpublished =0;
		JSONArray result = (JSONArray) json.get("result");
		Iterator it = result.iterator();
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();
			Date created = new Date(0);
			;
			Date lastModified = new Date(0);
			try {
				String dateCreated = (String) jsonObject.get("created");
				created = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.parse(dateCreated);
				String dateModified = (String) jsonObject.get("lastModified");
				lastModified = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.parse(dateModified);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// date ccheck
			if (!(created.compareTo(startDate) > 0 && created
					.compareTo(endDate) < 0)
					&& !(lastModified.compareTo(startDate) > 0 && lastModified
							.compareTo(endDate) < 0)) {
				continue;
			}

			Integer itemCount = (Integer) jsonObject.get("itemCount");
			if (itemCount == -1)
				itemCount = 0;

			Integer publishedItems = (Integer) jsonObject.get("publishedItems");
			if (publishedItems == -1)
				publishedItems = 0;

			allpublished+= itemCount;

			
		}

		return allpublished;
	}
	
//	public int gettotalValidItemsPublished() {
//		Integer totalValidPublished = 0;
//		List<PublicationDetailsBean> tlist = this.getPublications();
//		Iterator<PublicationDetailsBean> t = tlist.iterator();
//		while (t.hasNext()) {
//			PublicationDetailsBean tbean = (PublicationDetailsBean) t.next();
//			totalValidPublished += tbean.getItemCount();
//		}
//
//		return totalValidPublished;
//	}

}
