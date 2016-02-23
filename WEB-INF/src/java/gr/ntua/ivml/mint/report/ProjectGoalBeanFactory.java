package gr.ntua.ivml.mint.report;

import gr.ntua.ivml.mint.actions.UrlApi;
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

public class ProjectGoalBeanFactory extends OrganizationProgressBeanFactory {

	public ProjectGoalBeanFactory(Date startdate, Date enddate) {
		super(startdate, enddate);
		// TODO Auto-generated constructor stub
	}

	public List<OrganizationGoalsSummaryBean> getProjectGoalBeans() throws ParseException, net.minidev.json.parser.ParseException {		
		
		Integer totaltransformed = 0;
		Integer totalpublished = 0 ;
		Integer totaloaipublished = 0 ;
		
		Integer projectfinalTargetItems = 0;
		Integer projectCurrentTargetItems =0 ;
		List<OrganizationGoalsSummaryBean> all = new ArrayList<OrganizationGoalsSummaryBean>();
		
		
		
		
		List <Organization> orglist = new OrganizationList().listOrganizationToReport();
		
		Date finalDate = null;
		
		Date targetDate = null;
		
		Date today = endDate;
		
		Integer finaltargetItems = 0;
		Integer targetItems = 0 ;
		
		
		for (Organization org:orglist){
		
			Long orgid = org.getDbID();		
			
			
			Tuple<List<TransformationDetailsBean>, Integer> transfTuple = transformationDetailsBeanFactory.getTransformations3(orgid.toString());
			
			Integer transformed = transfTuple.v;
			Tuple<List<PublicationDetailsBean>, Integer> pubTuple = publicationDetailsBeanFactory.getPublications3(orgid.toString());
			Integer published  = pubTuple.v;
			
			
			
			totaltransformed +=transformed;	
			totalpublished += published;	
			
		}
		
	
		//get targets if exists add then beans with targets
			
		
		//class not ready 
			projectCurrentTargetItems += targetItems;
			projectfinalTargetItems+= finaltargetItems;
		
		
		
		totaloaipublished = orgoaibeanfactory.getProjectstatus().getUnique() / 2 ;
		
		
		OrganizationGoalsSummaryBean transformedgoalbean = new OrganizationGoalsSummaryBean(
				"Transformed Valid Items", today, totaltransformed, null,
				null, "transformed");
		all.add(transformedgoalbean);

		OrganizationGoalsSummaryBean publishedgoalbean = new OrganizationGoalsSummaryBean(
				"Published Valid Items", today, totalpublished, null, null,
				"published");
		all.add(publishedgoalbean);

		OrganizationGoalsSummaryBean oaipublishedgoalbean = new OrganizationGoalsSummaryBean(
				"OAI Published Valid Items", today, totaloaipublished, null,
				null, "oaipublished");
		all.add(oaipublishedgoalbean);

		OrganizationGoalsSummaryBean currentGoalbean = new OrganizationGoalsSummaryBean(
				"Current Target Number of Items", targetDate,
				projectCurrentTargetItems, null, null, "current goal");
		all.add(currentGoalbean);

		OrganizationGoalsSummaryBean finalGoalbean = new OrganizationGoalsSummaryBean(
				"Final Target Number of Items", finalDate, projectfinalTargetItems,
				null, null, "final goal");
		all.add(finalGoalbean);
		
		

		return all;	
		
	}

	
}
