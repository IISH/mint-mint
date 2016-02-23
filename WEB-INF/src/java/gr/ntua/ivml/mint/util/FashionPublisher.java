package gr.ntua.ivml.mint.util;


import gr.ntua.ivml.mint.RecordMessageProducer;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.XmlSchema;
import gr.ntua.ivml.mint.pi.messages.ExtendedParameter;
import gr.ntua.ivml.mint.pi.messages.ItemMessage;
import gr.ntua.ivml.mint.pi.messages.Namespace;
import gr.ntua.ivml.mint.xml.transform.XSLTransform;
import gr.ntua.ivml.mint.xsd.ReportErrorHandler;
import gr.ntua.ivml.mint.xsd.SchemaValidator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.util.TextParseUtil;


/**
 * This class publishes metadata from the Fashion portal database to the OAI. 
 * 
 * See the comments at the main class and the parameters.
 * @author nsimou
 *
 */
public class FashionPublisher
{
	private static final Logger log = Logger.getLogger(FashionPublisher.class);
	
	// Either to publish or not
	boolean publish = true;
	// Number of records to copy
	private String recordsNum = "-1";	
	// Exclude these organisations from publishing
	List<String> orgsExclude = new ArrayList<String>();
	
//	//Database properties
//	String url = "jdbc:mysql://panic.image.ntua.gr:3306/";  	//the portal database server
//	String dbName = "fashionenhancementfinal";					//the DB name
//	String driver = "com.mysql.jdbc.Driver";            	    //the DB driver
//	String userName = "fashionenha";                            //the portal username
//	String password = "fashionenha";							//the portal password
	
	//Database properties
	String url = "jdbc:mysql://panic.image.ntua.gr:3306/";  	//the portal database server
	String dbName = "portal";									//the DB name
	String driver = "com.mysql.jdbc.Driver";            	    //the DB driver
	String userName = "fashion";                    	        //the portal username
	String password = "portal";									//the portal password

	//Paths
	String xslPath;					//the path where the xsls used for the transformation are saved
	String xmlPath;					//the path uses to export DB dump and Invalid files
	String orgToPublish;
	
	
	//OAI publication parameters
	String routingKey = "fashion.oai";									//the queue routing key
	String schemaPrefix = "rdf";										//the prefix used for the OAI
	String schemaUri= "http://www.w3.org/1999/02/22-rdf-syntax-ns#";	//the uri of the schema
	String oaiServerHost = "panic.image.ece.ntua.gr";					//the oai server
	int oaiServerPort = 3009;											//the oai server port
	String queueHost = "guinness.image.ece.ntua.gr";					//the oai queue host
	XmlSchema xs = DB.getXmlSchemaDAO().getByName("EDM");               //the schema used for validation - it has to be included in the database defined in hibernate.properties
	RecordMessageProducer rmp;
	
	public void inintRMP(){
		try {
			rmp = new RecordMessageProducer(queueHost, "mint" );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		args = new String[31];		
		args[0] = "/Users/nsimou/git/mint2/WEB-INF/custom/fashion/xsl/";
		args[1] = "/Users/nsimou/Documents/Education/NTUA/Projects/Running/Fashion/FinalPublication_Update/xml/";
		args[2] = "true";
		args[3] = "-1";

		
		//Published
		args[4]  = "1011"; //171
		args[28] = "1048"; //966
		args[29] = "2015"; //680
		args[30] = "1050"; //97
		args[5]  = "1046"; //275
		args[26] = "1019"; //55691
		
		args[25] = "1015"; //36864 ok
		
		//Not published
		args[6]  = "1047"; //2508
		args[7]  = "1030"; //3555
		args[8]  = "1045"; //4741
		args[9]  = "1031"; //5585 ok
		args[10] = "1004"; //7769 
		args[11] = "1009"; //8361
		args[12] = "1023"; //9019 ok
		args[13] = "1016"; //9351
		args[14] = "1018"; //9980
		args[15] = "1034"; //9995 
		args[16] = "1012"; //10048
		args[17] = "1013"; //10084
		args[18] = "1002"; //10374
		args[19] = "1008"; //10593
		args[20] = "1029"; //13050
		args[21] = "1014"; //13492
		args[22] = "1039"; //15710
		args[23] = "1033"; //17412
		args[24] = "1040"; //25850
		
		
		args[27] = "2015"; //405050 ok
		
		

		
		
		if(args.length < 2) {
			FashionPublisher.log.error("Please specify:\n 1. XSL transformation path\n2. Path to export DB dump and Invalid files\n3,4,5... Organizations to exclude");
			System.out.println("Please specify:\n 1. XSL transformation path\n2. Path to export DB dump and Invalid files\n3,4,5... Organizations to exclude");
			System.exit(-1);
		}
		FashionPublisher fashionExporter = new FashionPublisher();
//		fashionExporter.orgToPublish = "1015";

				
//		Config.readProps();
//		// Setting db credentials and paths needed
//		fashionExporter.url = Config.get("db.url");
//		fashionExporter.dbName = Config.get("db.name");
//		fashionExporter.driver = Config.get("db.driver");
//		fashionExporter.userName = Config.get("db.username");
//		fashionExporter.password = Config.get("db.password");
		
		fashionExporter.xslPath = args[0];
		fashionExporter.xmlPath = args[1];
		fashionExporter.publish = Boolean.parseBoolean(args[2]);
		fashionExporter.recordsNum = args[3];
		for(int i=3;i<args.length;i++) 
			fashionExporter.orgsExclude.add(args[i]);
//		fashionExporter.exportXML();      			//Exports the XMLs of the DB to the 
//		fashionExporter.exportXMLFromEachOrg(5);    //Exports the specified number of records from each organization
		fashionExporter.inintRMP();					//Initializes the Record message Producer used for publishing
		
		//Publishes to OAI number of records specified in the command line from each organization 
		//- it uses getOrgs() modify depending on the organizations you want to publish.
		// By default it publishes all records.
		fashionExporter.publish(Integer.parseInt(fashionExporter.recordsNum));				
//		fashionExporter.detectOrgs(-1);             //Prints the dataProvider field for the records that are under NTUA (org_id=1). The correct ids have to be
													//found for each org and used for updating the database.
		}
	
	private Connection getConnection(){
		try {
			Class.forName(driver).newInstance();
			// Panic connection
//			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			// Amazon connection
			Connection conn = DriverManager
					.getConnection(url+dbName+"?user="+userName+"&password="+password);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	private void exportXML(){
		Connection conn = null;
		try {
			conn = getConnection();
			Statement st = conn.createStatement();

			String countQuery = "select count(*) from record";
			String query = "select mint_org_id,xml from record";
			
			ResultSet res = st.executeQuery(countQuery);
			res.next();
			int orgTotal = res.getInt("count(*)");
			res = st.executeQuery(query);
			int countRecords = 0;
			while (res.next()) {
				countRecords++;
				String xml = res.getString("xml");
				log.info("Processing record "+countRecords+"/"+orgTotal+" "+(countRecords/orgTotal * 100) );
				System.out.println("Processing record "+countRecords+"/"+orgTotal+" "+(countRecords/orgTotal * 100) );
				PrintWriter out = new PrintWriter(xmlPath+"xml/Dump/Record_"+countRecords+".xml");
				out.println(xml);
				out.close();
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
	
	private void exportXMLFromEachOrg(int recordsFromEachOrg){
		Connection conn = null;
		try {
			conn = getConnection();
			ArrayList<Integer> orgs = getOrgs();
			
			for (int i =0; i < orgs.size(); i++){
				String query;
				if (recordsFromEachOrg > 0 )
					query = "select xml from record where mint_org_id="+orgs.get(i)+" limit "+recordsFromEachOrg+"";
				else 
					query = "select xml from record where mint_org_id="+orgs.get(i);
				
				Statement st = conn.createStatement();
				ResultSet res = st.executeQuery(query);
				
				int countRecords = 0;
				while (res.next()) {
					countRecords++;
					String xml = res.getString("xml");
					PrintWriter out = new PrintWriter(xmlPath+"DataPerOrgs/Record_"+orgs.get(i)+"_"+countRecords+".xml");
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
	
	private ArrayList<Integer> getOrgs(){
		
		if(orgToPublish != null){
			ArrayList<Integer> orgs = new ArrayList<Integer>();
			orgs.add(new Integer(orgToPublish));
			return orgs;
		}
		else{
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
				// Exclude organizations specified
				for(String o : orgsExclude) 
					orgs.remove(Integer.valueOf(o));
				
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
	}
	
	
	private void publish(int recordsFromEachOrg){
		Connection conn = null;
		try {
			ArrayList<Integer> orgs = getOrgs();
			String report = "\n\nOrg_id	Records	Published	Invalid\n";
			for (int i = 0; i < orgs.size(); i++){
//			for (int i = 0; i < 1; i++){	
				conn = getConnection();

				String query;
				if( recordsFromEachOrg > 0)
					query = "select * from record where mint_org_id="+orgs.get(i)+" limit "+recordsFromEachOrg+"";
				else
					query = "select * from record where mint_org_id="+orgs.get(i);

				String countQuery = "select count(*) from record where mint_org_id="+orgs.get(i);
				
				
				//Uncomment this if only a dataset needs to be published.
//				String query;
//				if( recordsFromEachOrg > 0)
//					query = "select * from record where mint_org_id=1044";
//				else
//					query = "select * from record where mint_org_id="+orgs.get(i)+" AND mint_dataset_id=6340";
//
//				String countQuery = "select count(*) from record where mint_org_id="+orgs.get(i)+" AND mint_dataset_id=6340";
				
				
				Statement st = conn.createStatement();
				ResultSet res = st.executeQuery(countQuery);
				res.next();
				int orgTotal = res.getInt("count(*)");
			    int orgCount = 0;
			    int published = 0;
			    int invalid = 0;
				res = st.executeQuery(query);
				
				while (res.next()) {
				    orgCount++;
//				    if(orgCount > 125000  ){
				    	String xml = res.getString("xml");
				    	log.info("Publishing "+orgCount+"/"+orgTotal+" from org "+orgs.get(i)+" "+(i+1)+"/"+orgs.size());
				    	System.out.println("Publishing "+orgCount+"/"+orgTotal+" from org "+orgs.get(i)+" "+(i+1)+"/"+orgs.size());
				    	String transformed = transform(xml,res.getString("hash"),res.getInt("mint_org_id"));
				    	log.info("	Transformed "+orgCount+"/"+orgTotal+" from org "+orgs.get(i)+" "+(i+1)+"/"+orgs.size());
				    	System.out.println("	Transformed "+orgCount+"/"+orgTotal+" from org "+orgs.get(i)+" "+(i+1)+"/"+orgs.size());
				    	if (validate (transformed)){
				    		log.info("	Validated "+orgCount+"/"+orgTotal+" from org "+orgs.get(i)+" "+(i+1)+"/"+orgs.size());
				    		System.out.println("	Validated "+orgCount+"/"+orgTotal+" from org "+orgs.get(i)+" "+(i+1)+"/"+orgs.size());
				    		if(publish) {
				    			publishItem(res.getInt("mint_dataset_id"), res.getInt("report_id"), res.getInt("mint_org_id"), res.getInt("mint_record_id"), transformed);
//				    			publishItem(res.getInt("mint_dataset_id"), res.getInt("report_id"), 1020, res.getInt("mint_record_id"), transformed);
				    			log.info("	Published "+orgCount+"/"+orgTotal+" from org "+orgs.get(i)+" "+(i+1)+"/"+orgs.size());
				    			System.out.println("	Published "+orgCount+"/"+orgTotal+" from org "+orgs.get(i)+" "+(i+1)+"/"+orgs.size());
				    			published++;
				    		}
				    	}
				    	else{
				    		invalid++;
				    		File file = new File(xmlPath+"Invalid/"+orgs.get(i));
				    		FileUtils.forceMkdir(file);
				    		PrintWriter out = new PrintWriter(xmlPath+"Invalid/"+orgs.get(i)+"/"+orgs.get(i)+"_"+invalid+"_"+res.getString("hash")+".xml");
				    		out.println(xml);
				    		out.close();
				    	}
//				    }
				}
				conn.close();
				report = report+orgs.get(i)+"	"+orgTotal+"	"+published+"	"+invalid+"\n";
				
				
				
			}
			
			log.info(report);
			System.out.println(report);
			System.exit(0);
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
	
	private boolean validate (String xml){
		try {
			ReportErrorHandler report = SchemaValidator.validate(xml, xs);
			if(report.isValid())
				return true;
			else
				return false;
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}

	}
	
	private String transform(String input, String hash, int orgID){
		try {
		XSLTransform transformer = new XSLTransform();
		
		File xslPart1File = new File( xslPath + "EDMFP2EDMPart1.xsl");
		File xslPart2File = new File( xslPath + "EDMFP2EDMPart2.xsl");
		String printDescription = "true";
		if (orgID == 1003) //SPK
			printDescription = "false";
		
		String xslPart1 = FileUtils.readFileToString(xslPart1File, "UTF-8" );
		String xslPart2 = FileUtils.readFileToString(xslPart2File, "UTF-8" );
		String xsl = xslPart1 +
				"	<xsl:param name=\"var2\" select=\"'"+hash+"'\" /> \n  "+
				"	<xsl:param name=\"var3\" select=\"'"+printDescription+"'\" />  "+xslPart2;
		
		return transformer.transform(input, xsl);
		
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} 
	}
	
	private void detectOrgs(int recordsFromEachOrg){
		Connection conn = null;
		try {
			ArrayList<String> orgs = new ArrayList<String>();
			conn = getConnection();
			String query;
			if( recordsFromEachOrg > 0)
				query = "select * from record where mint_org_id=1 limit "+recordsFromEachOrg+"";
			else
				query = "select * from record where mint_org_id=1";
			String countQuery = "select count(*) from record where mint_org_id=1";


			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery(countQuery);
			res.next();
			int orgTotal = res.getInt("count(*)");
			int count=0;
			res = st.executeQuery(query);

			while (res.next()) {
				String xml = res.getString("xml");
				String dataProvider = getDataProvider(xml);
				log.info(dataProvider+" "+count++);
				System.out.println(dataProvider+" "+count++);
				if(!orgs.contains(dataProvider))
					orgs.add(dataProvider);
			}
			
			
			for(int i =0; i<orgs.size(); i++){
				log.debug(orgs.get(i));
				System.out.println(orgs.get(i));
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
	
	private String getDataProvider(String xml){
		String dataProvider = "";
		Document doc = parseXML(xml);
		NodeList properties = doc.getElementsByTagName("edm:dataProvider");
		
		Node edmDataProvider = properties.item(0);
//		System.out.println(edmDataProvider.getNodeName());
		Node skosConcept = edmDataProvider.getFirstChild();
//		System.out.println(skosConcept.getNodeName());
		NodeList skosConceptProperties = skosConcept.getChildNodes();
		for(int i=0; i < skosConceptProperties.getLength(); i++){
			Node skosProperty = skosConceptProperties.item(i);
			if(skosProperty.getNodeName().equals("skos:prefLabel"))
				dataProvider = skosProperty.getTextContent();			
		}
		
		return dataProvider;
		
	}
	
	private Document parseXML(String xml) {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new ByteArrayInputStream(xml.getBytes()));

			// normalize text representation
			doc.getDocumentElement ().normalize ();
			return doc;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void publishItem(int mintDatasetID,  int reportID,  int mintOrgID,  int mintRecordID,  String transformation){
		try {
//			RecordMessageProducer rmp =organizations you want to publish. new RecordMessageProducer(queueHost, "mint" );
			Namespace ns = new Namespace();
			int schemaId = 1003; //EDM
			String routingKeysConfig = routingKey;
			Set<String> routingKeys =  TextParseUtil.commaDelimitedStringToSet(routingKeysConfig);
			
			ns.setPrefix(schemaPrefix);
			ns.setUri(schemaUri);
			
			String projectName = "";
			for( String s: routingKeys ) {
				if( s.contains("oai")) projectName= s;
			}
						
			ExtendedParameter ep = new ExtendedParameter();
			ep.setParameterName("reportId" );
			ep.setParameterValue(""+reportID);
			final ArrayList<ExtendedParameter> params = new ArrayList<ExtendedParameter>();
			params.add( ep );
			

			ItemMessage im = new ItemMessage();
			im.setDataset_id(mintDatasetID);
			im.setDatestamp(System.currentTimeMillis());
			im.setItem_id(mintRecordID);
			im.setOrg_id(mintOrgID);
			im.setPrefix(ns);
			im.setProject("");
			im.setSchema_id(schemaId);
			im.setSourceDataset_id(mintDatasetID);
			im.setSourceItem_id(mintRecordID);
			im.setUser_id(1);
			im.setXml(transformation);
			im.setParams(params);


			for( String routingKey: routingKeys ) 
				rmp.send(im, routingKey );
			
						
			
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private void getProperties() {
		
	}
}

