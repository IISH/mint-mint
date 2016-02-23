package gr.ntua.ivml.mint.actions;

import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.criterion.Restrictions;

/**
 * Use this as trigger for Noterick to start a publication on their server.
 * Trigger for our own backend to start publication on noterick server.
 * 
 * Trigger to delete from Noterick server.
 * 
 * @author Arne Stabenau 
 *
 */
@Results({
	@Result(name="list", type="stream", params={"inputName", "inputStream", "contentType", "text/plain",
			"contentCharSet", "UTF-8" }),
	@Result(name="error", type="httpheader", params={"error", "404", 
			"errorMessage", "Internal Error"}) 
})



public class FindItem extends GeneralAction {
	// which id should be scheduled for publish
	private static final Logger log = Logger.getLogger( FindItem.class );
	String nativeId;
	private StringBuilder output = new StringBuilder();

	Set<Long> printedItems = new HashSet<Long>();
	
	@Action( value="FindItem" )
	public String execute() {
		if( !StringUtils.empty( nativeId )) {
			List<Item> res = DB.getSession().createCriteria(Item.class).add( Restrictions.eq("persistentId", nativeId )).list();
			if( res.size() == 0 ) output.append( "The id '" + nativeId + "' wasn't found!\n\n" );
			for( Item i: res ) {
				printItemToOutput(i);	
			}
			return "list";
		}
		return "error";
	}

	// where in given dataset is the item with given id
	private int findRow( long itemId, long dsid ) {
	    List<Long> ls = DB.getSession().createQuery( "select dbID from Item where datasetId=" + dsid + " order by item_id")
	    .list();
	    int row = 1;
	    for( Long entry: ls) {
	        if( entry == itemId) return row;
	        row++;
	    }
	    return -1;
	}

	private void printItemToOutput( Item item ) {
		// check if its already printed
		if( printedItems.contains( item.getImportItem().getDbID())) return;
		printedItems.add( item.getImportItem().getDbID());
		
		// for the right DS name
		Dataset sourceDs = item.getDataset().getOrigin();
		
		output.append( sourceDs.getOrganization().getEnglishName());
		output.append( " - " + sourceDs.getName() +"\n" );
		if( ! StringUtils.empty(item.getLabel()))
			output.append( "Label: '" + item.getLabel() + "'\n");
		
		int row = findRow( item.getDbID(), item.getDataset().getDbID());
		int page15 =  ((int)(row-1)/15) + 1;
		int pageRow15 = (row-1)%15 + 1;
		
		int page100 = ((int)(row-1)/100) + 1;
		int pageRow100 = (row-1)%100 + 1;

		output.append( "Page (15) : " + page15 + " Row: " + pageRow15 +"\n");
		output.append( "Page (100): " + page100 + " Row: " + pageRow100 +"\n");
		output.append( "\n" );
	}
	
	//
	// Getter setter below
	//
	
	public String getNativeId() {
		return nativeId;
	}

	public void setNativeId(String id) {
		this.nativeId = id;
	}
	
	public InputStream getInputStream() {
		try {
			return new ByteArrayInputStream(output.toString().getBytes("UTF8"));
		} catch( Exception e ) {
			// encoding is there, stupid exception!
			return null;
		}
	}
}
