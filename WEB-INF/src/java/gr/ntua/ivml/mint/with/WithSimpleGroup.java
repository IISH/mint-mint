package gr.ntua.ivml.mint.with;

import gr.ntua.ivml.mint.persistent.Item;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import nu.xom.Node;

public class WithSimpleGroup extends ArrayList<WithSimpleObject>{
	
	Edm edm;

	
	public WithSimpleGroup(Item item){
	}


	public void parseItem(String path, Item item) {
		WithSimpleObject result = new WithSimpleObject(); 		
		nu.xom.Nodes nods = item.getNodes(path);
		if (nods != null) {
			for (int i = 0; i < nods.size(); i++) {
				this.add(parseNode(nods.get(i)));
			}
		}
	
	}


	public WithSimpleObject parseNode(Node node) {
		// TODO Auto-generated method stub
		WithSimpleObject  result = new WithSimpleObject();
		return result;
	}
	
	public net.sf.json.JSONArray toJson(){
		net.sf.json.JSONArray jsar = new JSONArray();
		for (WithSimpleObject object:this){
			jsar.add(object.toJson());
		}
		return jsar;
		
	}


}
