package gr.ntua.ivml.mint.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.util.TextParseUtil;

import de.schlichtherle.util.Arrays;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.util.ApplyI;
import gr.ntua.ivml.mint.util.Config;
import net.minidev.json.JSONNavi;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

@Results({
	@Result(name= "json", location="json.jsp" ),
	@Result(name="error", type="httpheader", params={"error", "404", 
			"errorMessage", "Internal Error"}) 
})



public class WithDatasets extends GeneralAction implements ApplyI<WithDatasets.WithExporter> {
   // call to list datasets with name, itemCount, date
	
	public static class WithExporter implements Runnable {
		String datasetId, collectionId, token;
		Progres progres = new Progres( 0,0,0);
		ApplyI<WithExporter> onFinish;
		
		
		public static class Progres {
			public Progres( int total, int exported, int failed) {
				this.totalValidItems = total;
				this.exportedItems = exported;
				this.failedItems = failed;
			}
			
			public int totalValidItems;
			public int exportedItems;
			public int failedItems;
			
			public boolean fininshed( ) {
				return ( totalValidItems>0 && (totalValidItems == exportedItems + failedItems));
			}
		}
		
		public WithExporter( String datasetId, String collectionId, String token ) {
			this.datasetId = datasetId;
			this.collectionId = collectionId;
			this.token = token;
		}
		
		@Override
		public void run() {
			// login to with
			// create publicationRecord
			// make an  ExportToWith and run it
			// loop over items
			// add them to collection
			// update progres every now and again ( 20 times? )
		}
		
		public Progres getProgres() {
			return progres;
		}
		public void setProgres( Progres progres ) {
			this.progres = progres;
		}
		
		@Override
		public int hashCode() {
			return (collectionId + datasetId).hashCode();
		}
		
		public boolean equals( Object obj ) {
		    if (obj == null) {
		        return false;
		    }
		    if (getClass() != obj.getClass()) {
		        return false;
		    }
		    final WithExporter other = (WithExporter) obj;
		    if( !this.datasetId.equals( other.datasetId)) return false;
		    if( !this.collectionId.equals( other.collectionId)) return false;
		    return true;
		}
		
		public void onFinish( ApplyI<WithExporter> callback ) {
			this.onFinish = callback;
		}
 	}
	
	
	private JSONObject json=null;

	// what parameters we want? 
	// list - with listings for the logged in user
	// import - start the import of a dataset into with
	//   the target collection has to exist already but can be empty
	String action;
	
	
	String datasetId;
	String collectionId;
	
	// a set of the running importers
	public static HashSet<WithExporter> importers = new HashSet<WithExporter>();
	
	// a map of finished importers and when they have finished
	public static HashMap<WithExporter, Long> finishedImports = new HashMap<WithExporter,Long>();
	
	// authentication for importing data into With
	String loginToken;
	
	@Action( "WithDatasets")
	public String execute() {
		if( "list".equals(action )) {
			// list datasets from this user that can be imported into WITH
		} else if( "import".equals( action )) {
			// execute the import for datasetId and collectionId 
			// they are queued into the "db" queue 
		} else if( "status".equals( action )) {
			// take datasetId and collectionId and find the status of running import.
			// allow for no parameters to have a list of running imports with their status
		}
		return "json";
	}
	
	
	// fill json with datasets  that are exportable
	public void listDatasets() {
		
		Set<String> withSchemas= TextParseUtil.commaDelimitedStringToSet(Config.get("with.schema"));

		// all the datasets the user can get
		List<Dataset> lds = DB.getDatasetDAO().findUploadsByAvailableSchemas(getUser(),  withSchemas.toArray(new String[0]));
		
		json = new JSONObject();
		
		// make some json for it
		for( Dataset ds: lds ) {
			JSONObject dsJson = JSONNavi.newInstanceObject()
					.set( "organization", ds.getOrganization().getEnglishName())
					.set( "dbID", ds.getDbID())
					.set( "validItemCount", ds.getValidItemCount())
					.set( "date", ds.getCreated().toString())
					.set( "name", ds.getName())
					.getRoot();
		}
		
	}
	
	
	// json with status
	public void status() {
		
	}
	
	public void apply( WithExporter finishedImport ) {
		// move from running to finished
	}
}
