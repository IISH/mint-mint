package gr.ntua.ivml.mint.with;

import gr.ntua.ivml.mint.persistent.Item;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;

public class Provenance extends WithSimpleGroup{

	protected final Logger log = Logger.getLogger(getClass());
	
	Item item;

	public Provenance(Item item){
		super(item);

		ProvenanceObject dataProvider = new ProvenanceObject();
		dataProvider.put("provider",this.getDataProvider(item));
		this.add(dataProvider);
		ProvenanceObject provider = new ProvenanceObject();
		provider.put("provider",this.getProvider(item));
		provider.put("resourceId",this.getIsShownAt(item));
		this.add(provider);
		ProvenanceObject mint = new ProvenanceObject();
		mint.put("provider","Mint");
		mint.put("resourceId", this.getProvider(item) +"_"+item.getDataset().getOrganization().getDbID()+"_"+item.getDatasetId()+"_" +this.getRdfAbout(item));
		this.add(mint);
		
	}

	
	public String getProvider(Item item){
		String path = Edm.getProvenancepaths().get("provider");			
		//nu.xom.Nodes nods = item.getNodes(path);
		nu.xom.Nodes nods = item.getNodes(path);
		String s = null ;

		if (nods.size()>=0){
			s = nods.get(0).getValue();
					
		}
		return s;
	}
	
	
	public String getDataProvider(Item item){
		String path = Edm.getProvenancepaths().get("dataProvider");			
		//nu.xom.Nodes nods = item.getNodes(path);
		nu.xom.Nodes nods = item.getNodes(path);
		String s = null ;

		if (nods.size()>=0){
			s = nods.get(0).getValue();
					
		}
		return s;
	}

	
	public String getIsShownAt(Item item){
		String path = Edm.getProvenancepaths().get("isShownAt");			
		//nu.xom.Nodes nods = item.getNodes(path);
		nu.xom.Nodes nods = item.getNodes(path);
		String s = null ;

		if (nods.size()>=0){
			Element el = (Element) nods.get(0);
			Attribute atr = el.getAttribute(0);
			s  = atr.getValue();
		}
		return s;
	}
	
	public String getRdfAbout(Item item){
		String path = Edm.getProvenancepaths().get("ProvidedCHO");			
		//nu.xom.Nodes nods = item.getNodes(path);
		nu.xom.Nodes nods = item.getNodes(path);
		String s = null ;

		if (nods.size()>=0){
			Element el = (Element) nods.get(0);
			Attribute atr = el.getAttribute(0);
			s  = atr.getValue();
		}
		return s;
	}
	
	public void parseItem(String path, Item item) {
		ProvenanceObject result = new ProvenanceObject(); 		
		nu.xom.Nodes nods = item.getNodes(path);
		if (nods != null) {
			for (int i = 0; i < nods.size(); i++) {
				this.add(parseNode(nods.get(i)));
			}
		}
		
	
	}


	public ProvenanceObject parseNode(Node node) {
		 Element element = (Element) node;
		 
		 ProvenanceObject result = new ProvenanceObject();
		 for (int i = 0; i < element.getAttributeCount(); i++) {
				
				String type;
				Attribute atr = element.getAttribute(i);
				type = atr.getQualifiedName();
				if (type.contains("rdf:resource")) {
							result.put("uri", atr.getValue());
					break;
				} else if (type.contains("xml:lang")) {
					result.put("provider", element.getValue());

					break;
				}
			
			}
			if (element.getAttributeCount() == 0) {
				result.put("provider", node.getValue());
			}
		

		// TODO Auto-generated method stub
		return result;
		

	}
	

}
