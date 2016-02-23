import gr.ntua.ivml.mint.db.*
import gr.ntua.ivml.mint.persistent.*
import gr.ntua.ivml.mint.concurrent.*
import gr.ntua.ivml.mint.util.ApplyI
import nu.xom.Document
import nu.xom.Element
import nu.xom.Nodes

// create node <topic>Special collections</topic>
// when there is something like that in the source Item and not in the target Item

// Iterate over all valid euscreen publishable datasets
// iterate over all the items
//  is
// add ds ids of modified ds to a set
FixSpecial fix = new FixSpecial()
Queues.queue( fix, "now")

class FixSpecial implements Runnable {
	Set modifiedDs = [] as Set;
	Set<Long> specialCollectionItems = [] as Set;
	List childNames = ["extendedDescription", "seriesSeasonNumber", "episodeNumber"] as List;

	File progress = new File( "/tmp/progress.txt")
	
	public void run() {
		// find all items which have special collection
		List<Dataset> inputDs = DB.datasetDAO.simpleList( "organization=1025 and schema=1000" )
		progress.withPrintWriter( "UTF8", { progress ->
			for( Dataset ds: inputDs ) {
				ds.processAllItems(new ApplyI<Item>() {
							public void apply(Item i ) {
								Document d = i.document
								Nodes nodes = d.query("//*[local-name()='topic'][text()='Special Collections']")
								if( nodes.size() > 0 ) {
									specialCollectionItems.add( i.dbID )
								}
							}
						}, false)
				progress.println( "Dataset ${ds.dbID} SC: ${specialCollectionItems.size()}")
				progress.flush()
			}
			progress.println( "\nAll Datasets imported items analyzed\n")
			// find parent element of the topic
			// loop over index until you hit a element in the Set of afterElement

			List<Dataset> allDs = DB.datasetDAO.simpleList( "organization=1025 and schema=1010" )
			for( Dataset ds: allDs ) {
				progress.println( "Processing #${ds.dbID} with ${ds.itemCount}")
				progress.flush()
				ds.processAllItems(new ApplyI<Item>() {
							public void apply(Item item ) {
								// find the source item
								Item source = item.getImportItem()
								if( specialCollectionItems.contains( source.dbID)) {
									Document docu = item.document
									Nodes nodes = docu.query( "//*[local-name()='ContentDescriptiveMetadata']")
									Nodes checkExist = docu.query("//*[local-name()='topic'][text()='Special collections']" )
									if( checkExist.size() > 0 ) {
										progress.println( "Item #${item.dbID} already fixed.")
										return;
									}
									boolean inserted = false;
									if( nodes.size() != 1 ) {
										// buu shouldnt happen
									} else {
										Element parent = (Element) nodes.get(0);
										def prefixes = parent.namespacePrefixesInScope
										Element newTopic = new Element("topic" )
										
										for( e in prefixes.entrySet() ) {
											if( e.value =~ /euscreen/ ) {
												newTopic.setNamespaceURI(e.value)
												newTopic.setNamespacePrefix(e.key)
											}		
										}
										newTopic.appendChild("Special collections")

										for( int i=0; i<parent.childCount; i++ ) {
											nu.xom.Node child = parent.getChild(i)
											if (child instanceof Element ) {
												Element childElement = (Element) child
												String childName = childElement.localName
												if( childNames.contains(childName)) {
													parent.insertChild(newTopic, i)
													progress.println( "Corrected item #${item.dbID} '${item.label}'")
													// insert here
													inserted = true
													break
												}
											}
										}
										if( ! inserted ) {
											parent.insertChild( newTopic, parent.childCount)
											progress.println( "Corrected item #${item.dbID} '${item.label}'")
											
										}
										item.setXml(docu.toXML())
									}
								}
							}}, true)
			
			}
			progress.println( "Finished!")
			progress.flush()
		} )
	}
}
