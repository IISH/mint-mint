package gr.ntua.ivml.mint.report;

import gr.ntua.ivml.mint.actions.UrlApi;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.DataUpload;
import gr.ntua.ivml.mint.persistent.Organization;
import gr.ntua.ivml.mint.persistent.Transformation;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.util.OrganizationList;
import gr.ntua.ivml.mint.util.Tuple;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.saxon.expr.JPConverter.FromDate;

public class DataUploadDetailsBeanFactory {

	String organizationId;
	private TransformationDetailsBeanFactory derivedTransformationfactory;
	private Date startDate;
	private Date endDate;

	protected final Logger log = Logger.getLogger(getClass());

	public DataUploadDetailsBeanFactory(String organizationId, Date startDate,
			Date endDate) {
		super();

		this.organizationId = organizationId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.derivedTransformationfactory = new TransformationDetailsBeanFactory(
				organizationId, startDate, endDate);
	}

	public DataUploadDetailsBeanFactory(Date startDate, Date endDate) {
		super();

		this.startDate = startDate;
		this.endDate = endDate;
		this.derivedTransformationfactory = new TransformationDetailsBeanFactory(
				startDate, endDate);
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	/*
	 * public List<DataUploadDetailsBean> getUploads() { commented this and
	 * added 2 methods below to allow grouping per organization needed for
	 * jasper result UrlApi api = new UrlApi();
	 * api.setOrganizationId(organizationId); JSONObject json =
	 * api.listDataUploads(); return getUploads(json);
	 * 
	 * }
	 */

	public List<DataUploadDetailsBean> getUploads() {
		// JSONObject json = jsFetcher.getJson();
		UrlApi api = new UrlApi();
		api.setOrganizationId(this.organizationId);
		JSONObject json = api.listOrganizations();
		JSONArray result = (JSONArray) json.get("result");
		List<DataUploadDetailsBean> all = new ArrayList<DataUploadDetailsBean>();
		Iterator it = result.iterator();
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();
			String organizationId = jsonObject.get("dbID").toString();
			String orgname = jsonObject.getString("englishName").toString();
			if (orgname.equals("Old euscreen data")) continue;
			// DataUploadDetailsBeanFactory lala = new
			// DataUploadDetailsBeanFactory(organizationId, startDate, endDate);
			// this.organizationId = organizationId;
			List<DataUploadDetailsBean> ups = new ArrayList<DataUploadDetailsBean>();
			// ups = lala.getUploads(organizationId);
			ups = getUploads(organizationId);
			all.addAll(ups);
		}

		return all;

	}

	public List<DataUploadDetailsBean> getUploads(String orgid) {
		UrlApi api = new UrlApi();
		api.setOrganizationId(orgid);
		JSONObject json = api.listDataUploads();

		return getUploads(json);

	}

	public List<DataUploadDetailsBean> getUploads(JSONObject json) {
		List<DataUploadDetailsBean> uploads = new ArrayList<DataUploadDetailsBean>();
		JSONArray result = (JSONArray) json.get("result");
		Iterator<?> it = result.iterator();

		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();
			String name = jsonObject.get("name").toString();
			Date created = new Date(0);
			;
			Date lastModified = new Date(0);
			try {
				String dateCreated = (String) jsonObject.get("created");
				// created = new
				// SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS").parse(dateCreated);
				created = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.parse(dateCreated);

				String dateModified = (String) jsonObject.get("lastModified");
				// lastModified = new
				// SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS").parse(dateModified);
				lastModified = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.parse(dateModified);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!(created.compareTo(startDate) > 0 && created
					.compareTo(endDate) < 0)
					&& !(lastModified.compareTo(startDate) > 0 && lastModified
							.compareTo(endDate) < 0)) {
				continue;
			}

			Integer itemCount = (Integer) jsonObject.get("itemCount");
			if (itemCount == -1)
				itemCount = 0;

			Integer validItems = (Integer) jsonObject.get("validItems");
			if (validItems == -1)
				validItems = 0;
			Integer invalidItems = (Integer) jsonObject.get("invalidItems");
			if (invalidItems == -1) {
				invalidItems = 0;
				validItems = itemCount;
			}

			String itemizerStatus = jsonObject.get("itemizerStatus").toString();
			String datasetId = jsonObject.get("dbID").toString();
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

			/*
			 * List<TransformationDetailsBean> transformationsderivedCollection
			 * = null; populateFactories(datasetId);
			 * transformationsderivedCollection = derivedBeanFactory
			 * .getTransformations();
			 */

			JSONArray derived = (JSONArray) jsonObject.get("derived");

			JSONObject tempobject = new JSONObject();
			tempobject.element("result", derived);

			List<TransformationDetailsBean> transformationsderivedCollection = null;
			derivedTransformationfactory.setOrganizationId(organizationId);
			transformationsderivedCollection = derivedTransformationfactory
					.getTransformations(tempobject, organizationId);

			DataUploadDetailsBean dataUploadDetailsBean = new DataUploadDetailsBean(
					name, created, lastModified, creatorId, creatorName,
					itemCount, validItems, invalidItems, itemizerStatus,
					organizationId, organizationName,
					transformationsderivedCollection);
			uploads.add(dataUploadDetailsBean);
		}

		return uploads;
	}
	
	public Integer getUploadedItems(String orgid) {
		UrlApi api = new UrlApi();
		api.setOrganizationId(orgid);
		JSONObject json = api.listDataUploads();

		return getUploadedItems(json);
	}
	
	
	public Integer getUploadedItems(JSONObject json) {
		List<DataUploadDetailsBean> uploads = new ArrayList<DataUploadDetailsBean>();
		JSONArray result = (JSONArray) json.get("result");
		Iterator it = result.iterator();

		Integer uploadedItems = 0;

		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();
			String name = jsonObject.get("name").toString();
			Date created = new Date(0);
			;
			Date lastModified = new Date(0);
			try {
				String dateCreated = (String) jsonObject.get("created");
				// created = new
				// SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS").parse(dateCreated);
				created = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.parse(dateCreated);

				String dateModified = (String) jsonObject.get("lastModified");
				// lastModified = new
				// SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS").parse(dateModified);
				lastModified = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.parse(dateModified);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!(created.compareTo(startDate) > 0 && created
					.compareTo(endDate) < 0)
					&& !(lastModified.compareTo(startDate) > 0 && lastModified
							.compareTo(endDate) < 0)) {
				continue;
			}

			Integer itemCount = (Integer) jsonObject.get("itemCount");
			if (itemCount == -1)
				itemCount = 0;

			Integer validItems = (Integer) jsonObject.get("validItems");
			if (validItems == -1)
				validItems = 0;
			Integer invalidItems = (Integer) jsonObject.get("invalidItems");
			if (invalidItems == -1) {
				invalidItems = 0;
				validItems = itemCount;
			}

			uploadedItems += itemCount;
		}
		return uploadedItems;

	}
	
	//return all Transformations for overall xlsx reports.
			public List<DataUploadDetailsBean> getDataUploads3() throws ParseException, net.minidev.json.parser.ParseException{
				List<DataUploadDetailsBean> all = new ArrayList<DataUploadDetailsBean>();
				List <Organization> orglist = new OrganizationList().listOrganizationToReport();
				for (Organization org:orglist){
				
					List<DataUploadDetailsBean> DataUploadsbeanCollection = new ArrayList<DataUploadDetailsBean>();		
					Long orgid = org.getDbID();		
					Tuple<List<DataUploadDetailsBean>, Integer> pubTuple = getDataUploads3(orgid.toString());	
					DataUploadsbeanCollection  = pubTuple.u;
					all.addAll(DataUploadsbeanCollection);
				}
				
				return all;
				
			}
			
			public Tuple<List<DataUploadDetailsBean>,Integer>  getDataUploads3(String orgid){
				List<DataUploadDetailsBean> DataUploads = new ArrayList<DataUploadDetailsBean>();
				List<DataUpload> result;
				result = DB.getDataUploadDAO().findByOrganization(DB.getOrganizationDAO().getById(Long.parseLong(orgid), false));
				
				Integer alluploaded = 0 ;
				for (DataUpload du : result) {

					
						String name =  du.getName();
						Date created = new Date(0);
						Date lastModified = new Date(0);


						created = du.getCreated();
						lastModified = du.getLastModified();


						if (!(created.compareTo(startDate) > 0 && created
								.compareTo(endDate) < 0)
								&& !(lastModified.compareTo(startDate) > 0 && lastModified
										.compareTo(endDate) < 0)) {
							continue;
						}

						Integer itemCount = (Integer)  du.getItemCount();
						if (itemCount == -1)
							itemCount = 0;


						Integer validItems = (Integer) du.getValidItemCount();
						if (validItems == -1)
							validItems = 0;
						Integer invalidItems = (Integer) du.getInvalidItemCount();
						if (invalidItems == -1)
							invalidItems = 0;

						String itemizerStatus = du.getItemizerStatus();


						


						alluploaded+= itemCount;



						User creator = du.getCreator();
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

						List<TransformationDetailsBean> derivedTransformations = null;
							
						
						
						DataUploadDetailsBean DataUploadDetailsBean = new DataUploadDetailsBean(name, created, lastModified, creatorId, creatorName, itemCount, validItems, invalidItems, itemizerStatus, organizationId, organizationName, derivedTransformations);
						DataUploads.add(DataUploadDetailsBean);




					}

					return new Tuple<List<DataUploadDetailsBean>,Integer>( DataUploads, alluploaded );


				}
			
		
		
	
	
	
	

}