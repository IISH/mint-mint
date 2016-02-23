
package gr.ntua.ivml.mint.actions;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.Organization;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({
	  @Result(name="input", location="home.jsp"),
	  @Result(name="error", location="home.jsp"),
	  @Result(name="success", location="home.jsp")
	})

public class Home extends GeneralAction{

	protected final Logger log = Logger.getLogger(getClass());
	
      private List<Organization> allOrgs;
      private List<String> countries=new ArrayList<String>();
      private String linkDs;
      private String linkItem;
      
      
      @Action(value="Home")
      public String execute() throws Exception {
    	  log.debug("Home controller");
  		return SUCCESS;
      }
 	
      
      public List<Organization> getAllOrgs(){
      	allOrgs =DB.getOrganizationDAO().findAll();
      	return allOrgs;
      } 
      
      public List<String> getCountries(){
    		countries = new ArrayList<String>(java.util.Arrays.asList("Austria", "Belgium", "Bulgaria","Cyprus", "Czech Rep.", "Denmark", "Estonia",
    				"Finland", "France", "Germany", "Greece","Hungary", "Ireland", "Italy", "Israel", "Latvia",
    				  "Lithuania","Luxembourg","Malta","Netherlands","Poland","Portugal",
    				"Romania","Russia","Slovakia","Slovenia",
    				"Spain","Sweden", "Switzerland", "United Kingdom", "Europe", "International"));
    		return countries;
        }
      
  	
  	String orgId;
  	
  	public String getOrgId() {
  		return orgId;
  	}

  	public void setOrgId(String orgId) {
  		this.orgId = orgId;
  	}

  	public List<Organization> getOrganizations() {
  		return  user.getAccessibleOrganizations();
  	}
  	
  	public void setLinkDs( String id ) {
  		this.linkDs = id;
  	}
  	
  	public void setLinkItem( String linkItem ) {
  		this.linkItem = linkItem;
  	}
  	
  	private String dsLink() {
  		try {
  			Dataset d = DB.getDatasetDAO().getById(Long.parseLong(linkDs), false);
  			Dataset importDs = d.getOrigin();
  			Organization org = d.getOrganization();
  			if( getUser().can("view data", org )) {
  				return "[{url:'ImportSummary.action?orgId=" + org.getDbID() + "&linkDs="+ importDs.getDbID()+"', title:'My workspace'},"
  						+ "{url:'DatasetOptions_input?uploadId=" + importDs.getDbID() +"', title:'Dataset Options'}]";
  			}
  		} catch( Exception e ) {
  			log.error( "Deep link problem", e );
  		}
  		return "[]";
  	}
  	
  	private String itemLink() {
  		try {
  			Item item = DB.getItemDAO().getById(Long.parseLong(linkItem), false);
  			if( item == null ) return "[]";
  			Dataset d = item.getDataset();
  			Dataset importDs = d.getOrigin();
  			Organization org = d.getOrganization();
  			if( getUser().can("view data", org )) {
  				StringBuilder res = new StringBuilder();
  				res.append("[{url:'ImportSummary.action?orgId=" + org.getDbID() + "&linkDs="+ importDs.getDbID()+"', title:'My workspace'},"
  						+ "{url:'DatasetOptions_input?uploadId=" + importDs.getDbID() +"', title:'Dataset Options'},"
  						);
 				if( d.getDbID() != importDs.getDbID()) {
 					res.append( "{url:'DatasetOptions_input?uploadId=" + d.getDbID() +"', title:'Dataset Options'}," );
 	 				res.append( "{url:'ItemView?uploadId=" +d.getDbID() + "&itemId=" + linkItem +
 	 						"', title:'DataUpload Items' }" +
 	 						"]" );
 				} else {
 				res.append( "{url:'ItemView?uploadId=" +importDs.getDbID() + "&itemId=" + linkItem +
 						"', title:'DataUpload Items' }" +
 						"]" );
 				}
 				return res.toString();
  			}
  		} catch( Exception e ) {
  			log.error( "Deep link problem", e );
  		}
  		return "[]";
  	}
  	
  	
  	
  	
  	// json with [ { url:.. title: }]
  	public String getDeepLink() {
  		//  url:"ImportSummary.action?orgId=<%=orgid%>", kTitle:"My workspace"
  		if( linkDs != null ) return dsLink();
  		if( linkItem != null ) return itemLink();
  		return "[]";
  	}
}