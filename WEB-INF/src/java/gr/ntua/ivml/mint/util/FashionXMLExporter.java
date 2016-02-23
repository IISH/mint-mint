package gr.ntua.ivml.mint.util;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FashionXMLExporter {
	
	//Database properties
	String url = "jdbc:mysql://panic.image.ntua.gr:3306/";  //the portal database server
	String dbName = "portal";								//the DB name
	String driver = "com.mysql.jdbc.Driver";                //the DB driver
	String userName = "fashion";                            //the portal username
	String password = "portal";								//the portal password


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FashionXMLExporter export = new FashionXMLExporter();
		String path = "/Users/nsimou/Desktop/FashionExport2/";
		export.export(1034,-1,path);
		

	}
	
	private Connection getConnection(){
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	private ArrayList<Integer> getOrgs(){
		Connection conn = null;
		try {
			conn = getConnection();
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery("select distinct mint_org_id from record");
			ArrayList<Integer> orgs = new ArrayList<Integer>();

			while (res.next()) {
				int mintOrgID = res.getInt("mint_org_id");
//				if(mintOrgID != 1 && mintOrgID != 1002 && mintOrgID != 1008 
//						&& mintOrgID != 1014 && mintOrgID != 1016 && mintOrgID != 1018)
//					orgs.add(new Integer(mintOrgID));
//				if(mintOrgID == 1015)
//					orgs.add(new Integer(mintOrgID));
				if(mintOrgID != 1)
					orgs.add(new Integer(mintOrgID));
				
			}
			return orgs;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void export(int orgId,int recordsFromEachOrg, String path){
		Connection conn = null;
		try {
			conn = getConnection();
			ArrayList<Integer> orgs;
			
			if(orgId < 0)
				orgs = getOrgs();
			else{ 
				orgs = new ArrayList<Integer>();
				orgs.add(new Integer(orgId));
			}

			for (int i =0; i < orgs.size(); i++){
				File folder = new File(path+"/"+orgs.get(i));
				folder.mkdirs();
				System.out.println("Exporting from "+orgs.get(i)+" "+i+"/"+orgs.size());
				String query;
				if (recordsFromEachOrg > 0 )
					query = "select xml, mint_dataset_id from record where mint_org_id="+orgs.get(i)+" limit "+recordsFromEachOrg+"";
				else 
					query = "select xml, mint_dataset_id from record where mint_org_id="+orgs.get(i);
				
				Statement st = conn.createStatement();
				ResultSet res = st.executeQuery(query);
				
				int countRecords = 0;
				while (res.next()) {
					countRecords++;
					String xml = res.getString("xml");
					int mint_dataset_id = res.getInt("mint_dataset_id");
					PrintWriter out = new PrintWriter(path+"/"+orgs.get(i)+"/"+mint_dataset_id+"_"+countRecords+".xml");
					out.println(xml);
					out.close();
					
				}
			}
		}catch (Exception e) {
	      e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getIsShownByFromJSON(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}
