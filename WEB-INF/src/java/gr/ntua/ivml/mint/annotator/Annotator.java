package gr.ntua.ivml.mint.annotator;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.xml.sax.SAXException;

import gr.ntua.ivml.mint.concurrent.Queues;
import gr.ntua.ivml.mint.concurrent.Solarizer;
import gr.ntua.ivml.mint.db.DB;
import gr.ntua.ivml.mint.mapping.AbstractMappingManager;
import gr.ntua.ivml.mint.mapping.MappingCache;
import gr.ntua.ivml.mint.mapping.model.Element;
import gr.ntua.ivml.mint.mapping.model.Mappings;
import gr.ntua.ivml.mint.mapping.model.SchemaConfiguration;
import gr.ntua.ivml.mint.mapping.model.SchemaConfiguration.Views;
import gr.ntua.ivml.mint.mapping.model.SimpleMapping;
import gr.ntua.ivml.mint.persistent.AnnotatedDataset;
import gr.ntua.ivml.mint.persistent.DataUpload;
import gr.ntua.ivml.mint.persistent.Dataset;
import gr.ntua.ivml.mint.persistent.Item;
import gr.ntua.ivml.mint.persistent.Transformation;
import gr.ntua.ivml.mint.persistent.User;
import gr.ntua.ivml.mint.persistent.XmlSchema;
import gr.ntua.ivml.mint.util.Preferences;
import gr.ntua.ivml.mint.util.StringUtils;
import gr.ntua.ivml.mint.util.XMLUtils;
import gr.ntua.ivml.mint.xml.transform.XMLFormatter;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class Annotator {
	public class AnnotationDocument {
		private Mappings mappings = null;
		private Item item = null;
		
		public AnnotationDocument(Mappings mappings) {
			this.setMappings(mappings);
		}
		
		public AnnotationDocument(Item item) throws ValidityException, SAXException, ParsingException, IOException {
			this.setItem(item);
			Mappings mappings = Mappings.templateFromXML(item.getXml());

			XmlSchema schema = getSchema();
			if(schema != null) {
				Mappings output = schema.getTemplate();
				Element input = mappings.findFirst("//" + output.getTemplate().getFullName());
				output.getTemplate().expand(input);
				this.setMappings(output);
			} else {
				this.setMappings(mappings);
			}
		}
		
		public Item getItem() { return this.item; };
		public void setItem(Item item) { this.item = item; }
		
		public Mappings getMappings() { return this.mappings; }
		public void setMappings(Mappings mappings) { this.mappings = mappings; }
		
		public Element getTemplate() { return this.mappings.getTemplate(); }
	}
	
	protected final Logger log = Logger.getLogger(getClass());
	private MappingCache cache = new MappingCache();
	private JSONObject configuration = new JSONObject();

	private long datasetId;
	private XmlSchema schema = null;
	AnnotationDocument document = null;
	private User user;
	
	private JSONObject errorResponse(String error, HttpServletRequest request) {
		JSONObject response = new JSONObject();
		if(request != null) response.put("request", request.getParameterMap());
		response.put("error", error);
		return response;
	}

	public Annotator(User user) {
		this.user = user;
	}

	/**
	 * Initialize annotator for a dataset.
	 * @param dataUploadId
	 */
	public void init(String datasetId) {
		log.debug("Initialize with datasetId: " + this.datasetId);
		this.datasetId = Long.parseLong(datasetId);
		this.schema = null;
		this.document = null;
		this.cache.reset();
		Dataset ds = DB.getDatasetDAO().findById(this.datasetId, false);
		if (ds != null && (ds instanceof Transformation || ds instanceof DataUpload)) {
			if (ds.getAnnotatedDataset() != null)
				this.datasetId = ds.getAnnotatedDataset().getDbID();
			else {
				log.info("Creation of AnnotatedDataset started");
				AnnotatedDataset ads = AnnotatedDataset.fromDataset(ds);
				DB.getAnnotatedDatasetDAO().makePersistent(ads);
				DB.commit();
				this.datasetId = ads.getDbID();
				AnnotatedDataset annotatedDS = (AnnotatedDataset) DB.getDatasetDAO().getById(this.datasetId, false);
				//annotatedDS.logEvent("Copying items to AnnotatedDataset");
				CreateAnnotatedDataset gac = new CreateAnnotatedDataset(annotatedDS, user);
		    	//Queue as ConditionedRunnable to check the locks
		    	Queues.queue(gac, "db");
			}	
		}
	}
	
	
	
	/**
	 * Execute ajax commands of the annotator.
	 * @param request
	 * @return
	 */
	public JSONObject execute(HttpServletRequest request) {
		JSONObject response = new JSONObject();
		String command = request.getParameter("command");
		if(command == null) return this.errorResponse("No command defined", request);
		try {
			// get loggedin user
			User user= (User) request.getSession().getAttribute("user");
			if( user != null ) {
				user = DB.getUserDAO().findById(user.getDbID(), false );
			}
			if(command.equals("init")) {
				this.init(request.getParameter("dataUploadId"));						
			} else if(command.equals("loadItem")) {
				String itemId = request.getParameter("itemId");
				response = this.loadItem(Long.parseLong(itemId));
			} else if(command.equals("newItem")) {
				response = this.newItem();
			} else if(command.equals("deleteItem")) {
				response = this.deleteItem();
			} else if(command.equals("setPreferences")) {
				String preferences = request.getParameter("preferences");
				Preferences.put(user, AbstractMappingManager.PREFERENCES, preferences);
				response = new JSONObject();
				response.put("preferences", preferences);
			} else if(command.equals("setConstantValueMapping")) {
				String id = request.getParameter("id");
				String value = request.getParameter("value");
				String annotation = request.getParameter("annotation");
				int index = Integer.parseInt(request.getParameter("index"));	
				if(id != null) {
					if(value != null) value = URLDecoder.decode(value, "UTF-8");
					if(annotation != null) annotation = URLDecoder.decode(annotation, "UTF-8");
					return this.setConstantValueMapping(id, value, annotation, index);
				} else {
					return this.errorResponse("argument missing", request);
				}
			} else if(command.equals("removeMapping")) {
				String id = request.getParameter("id");
				int index = Integer.parseInt(request.getParameter("index"));
				
				if(id != null) {
					return this.removeMapping(id, index);
				} else {
					return this.errorResponse("argument missing", request);
				}
			} else if (command.contains("duplicateNode")) {
				String id = request.getParameter("id");
				if(id != null) {
					if (command.equals("duplicateNode"))
						return this.duplicateNode(id, false);
					else if(command.equals("duplicateNodeAttribute")) 
						return this.duplicateNode(id, true);
				} else 
					return this.errorResponse("argument missing", request);
			} else if (command.contains("removeNode")) {
				String id = request.getParameter("id");
				if(id != null) {
					if (command.equals("removeNode")) 
						return this.removeNode(id, false);
					else if (command.equals("removeNodeAttribute"))
						return this.removeNode(id, true);
				}
				else 
					return this.errorResponse("argument missing", request);
			} else if (command.equals("find")) {
				String id = request.getParameter("root");				
				String xpath = request.getParameter("xpath");
				HashMap<String, String> metadata = new HashMap<String, String>();
				
				// find metadata
				for(Object keyObj: request.getParameterMap().keySet()) {
					String key = (String) keyObj;
					if(key.startsWith("metadata[")) {
						String metadataKey = key.replaceFirst("metadata\\[", "").replaceFirst("\\]", "");
						metadata.put(metadataKey, request.getParameter(key));
					}
				}
				
				if(xpath != null) {
					response = new JSONObject();
					
					Collection<Element> elements = this.find(xpath, id);
					JSONArray results = new JSONArray();
					for(Element element: elements) {
						JSONObject elementResult = new JSONObject();
						
						elementResult.put("target", element.asJSONObject());
						
						JSONObject metadataResults = new JSONObject();
						for(String key: metadata.keySet()) {
							String value = this.findValue(metadata.get(key), element.getId());
							metadataResults.put(key, value);
						}
						elementResult.put("metadata", metadataResults);
						results.add(elementResult);
					}
					
					response.put("results", results);
				} else {
					return this.errorResponse("argument missing", request);
				}
			} else if(command.equals("getXML")) {
				JSONObject xml = new JSONObject();
				xml.put("xml", this.getXML());
				return xml;
			}
			else if (command.equals("groupAnnotate")) {
				String stringGroupActs = request.getParameter("groupActs");
				JSONParser jp = new JSONParser(0);
				List<JSONObject> groupActs = (List<JSONObject>) jp.parse(stringGroupActs);
				JSONArray itemsXpathActions = formItemsXpathActions(groupActs);
				response = groupAnnotate(itemsXpathActions);
			}
			else {
				return this.errorResponse("unsupported operation", request);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return this.errorResponse(e.getMessage(), request);
		}
		return response;
	}
	
	private JSONArray formItemsXpathActions(List<JSONObject> groupActs) {
		JSONArray itemsXpathActions = new JSONArray();
		List<Long> omitted = new ArrayList<Long>();
		List<Long> itemList = solrQueryItems("", "", "");
		boolean lastActionIsSelect = false;
		long selected = 0;
		for (JSONObject act: groupActs) {
			String type = (String) act.get("type");
			if (type.equals("search")) {
				JSONObject cquery = (JSONObject) act.get("arguments");
				String xpath =  "";
				if (cquery.containsKey("xpath"))
					xpath = (String) cquery.get("xpath");
				itemList = solrQueryItems((String) cquery.get("query"), (String) cquery.get("queryType"), xpath);
			}
			else if (type.equals("select")) {
				JSONArray args = (JSONArray) ((JSONArray) act.get("arguments")).get(1);
				//arguments[0]=-1 means omitted contains the SELECTED items
				selected = (Long) ((JSONArray) act.get("arguments")).get(0);
				for (Object arg: args) {
					omitted.add((Long) arg);
				}
				if (selected == -1) {
					itemList = omitted;
					omitted = new ArrayList<Long>();
				}
				lastActionIsSelect = true;
			}
			else {
				for (Long item: omitted) {
						itemList.remove(item);
				}
				if (itemList.size() > 0) {
					JSONObject xpathAction = new JSONObject();
					xpathAction.put("type", type);
					xpathAction.put("xpath", (String) act.get("xpath"));
					xpathAction.put("arguments", (JSONArray) act.get("arguments"));
					if (lastActionIsSelect) {
						JSONObject itemsXpathAction = new JSONObject();
						itemsXpathAction.put("itemIds", itemList);
						itemsXpathAction.put("actionList", new JSONArray());
						itemsXpathActions.add(itemsXpathAction);
					}
					((JSONArray) ((JSONObject) itemsXpathActions.get(itemsXpathActions.size()-1))
					.get("actionList")).add(xpathAction);
				}
				lastActionIsSelect = false;
			}
		}
		return itemsXpathActions;
	}
	
	private JSONObject groupAnnotate(JSONArray itemsXpathActions) {
		GroupAnnotator ga = new GroupAnnotator(datasetId, user);
		ga.modifyElementsInItemsList(itemsXpathActions);  
		JSONObject response = new JSONObject();
		return response;
	}
	
	
	private List<Long> solrQueryItems(String query, String queryType, String xpath) {
		List<Long> itemIds;
		SolrQuery sq = new SolrQuery();
		sq.addFilterQuery("dataset_id:"+String.valueOf(datasetId));
		sq.setFields("item_id");
		sq.setStart(0);
		sq.setRows(10000000);
		if (StringUtils.empty(query))
			sq.setQuery("*");
		else
			if (StringUtils.empty(queryType) || queryType.equals("label"))
				sq.setQuery("label_tg:(" + query + ")");
			else if(queryType.equals( "all" ))
				sq.setQuery("all:(" + query + ")");
			else if (queryType.equals("field") && !StringUtils.empty(xpath)) {
				String solrXpath = Solarizer.smoothXpathForSolr(xpath);
				sq.setQuery(solrXpath + "_s:" +  query);
			}			
			else
				sq.setQuery(query);
		itemIds = queryToList(sq);
		return itemIds;
	}
	
	private List<Long> queryToList(SolrQuery sq) {
		ArrayList<Long> result = new ArrayList<Long>();
		SolrClient ss = Solarizer.getSolrClient();
		try {
			if( ss != null ) {
				QueryResponse qr = ss.query(sq);
				SolrDocumentList sdl = qr.getResults();
				for(SolrDocument sd: sdl) {
					Long itemId = Long.parseLong(sd.getFirstValue("item_id").toString());
					result.add(itemId);
				}
			}
		} catch(Exception e) {
			log.error( "Solr query failed" ,e );
		}
		return result;
	}

	private JSONObject newItem() throws ValidityException, SAXException, ParsingException, IOException {
		Dataset dataset = DB.getDatasetDAO().findById(this.datasetId, false);
		if(dataset != null) {
			Item item = dataset.createItem();
			
			Mappings mappings = this.getItemTemplate();
			item.setXml(XMLUtils.toXML(mappings));
			item.setLastModified(new Date());
			item.setLabel("New item");
			
			dataset.updateItem(item);
			
			JSONObject response = new JSONObject();
			response.put("itemId", item.getDbID());
			response.put("item", this.loadItem(item));
			return response;		
		}
		
		return this.errorResponse("Could not get dataset", null);
	}
	
	private JSONObject deleteItem() {
		Dataset dataset = DB.getDatasetDAO().findById(this.datasetId, false);
		Item item = this.document.getItem();
		
		if(item != null) {
			boolean result = dataset.deleteItem(item);
			JSONObject response = new JSONObject();
			response.put("deleteItem", result);			
			this.document = null;
			
			return response;
		}
		
		return this.errorResponse("no item loaded", null); 
	}
	
	private XmlSchema getSchema() {
		if(this.schema == null) {
			Dataset dataset = DB.getDatasetDAO().findById(this.datasetId, false);
			if(dataset != null && dataset.getSchema() != null) {
				this.schema = dataset.getSchema();
			}
		}
		
		return this.schema;
	}

	public Mappings getItemTemplate() {
		Dataset dataset = DB.getDatasetDAO().findById(this.datasetId, false);
		if(dataset != null && dataset.getSchema() != null) {
			return dataset.getSchema().getTemplate();
		}
		return null;
	}

	/**
	 * Loads a database item in annotator.
	 * @param itemId the item's dbID
	 * @return returns mapping template of loaded item
	 * @throws ValidityException
	 * @throws SAXException
	 * @throws ParsingException
	 * @throws IOException
	 */
	public JSONObject loadItem(long itemId) throws ValidityException, SAXException, ParsingException, IOException {
		Item item = DB.getItemDAO().getById(itemId, false);
		return this.loadItem(item);
	}
	
	private JSONObject loadItem(Item item) throws ValidityException, SAXException, ParsingException, IOException {
		this.document = new AnnotationDocument(item);
		this.cache = new MappingCache(this.document.getTemplate().asJSONObject());
		return this.document.getMappings().asJSONObject();
	}

	
	/**
	 * Sets a constant value mapping of specified element from the cache.
	 * @param id
	 * @param value
	 * @param annotation
	 * @param index
	 * @return
	 */
	private JSONObject setConstantValueMapping(String id, String value, String annotation, int index) {
		Element element = this.cache.getElementHandler(id);
		element.getMappingCase(0, true).setMapping(index, SimpleMapping.MAPPING_TYPE_CONSTANT, value, annotation);
		
		this.save();

		return element.asJSONObject();
	}
	
	/**
	 * Removes a mapping of specified element from the cache.
	 * @return
	 */	
	private JSONObject removeMapping(String id, int index) {
		Element element = this.cache.getElementHandler(id);
		element.getMappingCase(0, true).removeMapping(index);

		this.save();

		return element.asJSONObject();
	}
	
	private JSONObject duplicateNode(String id, boolean isAttribute) {
		Element duplicate = null;
		if (isAttribute) 
			duplicate = this.cache.duplicateAttribute(id);
		else 
			duplicate = this.cache.duplicate(id);
		//if(duplicate != null) duplicate.setRemovable(true);
		
		this.save();

		if(duplicate != null) {
			JSONObject result = new JSONObject();
			result.put("original", id);
			result.put("duplicate", duplicate.asJSONObject());
			return result;
		}
		else return null;
	}

	private JSONObject removeNode(String id, boolean isAttribute) {
		JSONObject result = new JSONObject();
		Element parent = null;
		Element child = null;
		if (isAttribute) {
			//parent element which contains attribute will be removed
			child = this.cache.getParentHandler(id);
			id = child.getId();
			parent = this.cache.getParentHandler(id); 
		}
		else {
			parent = this.cache.getParentHandler(id);
			child = this.cache.getElementHandler(id);
		}
		if(child == null || parent == null) {
			result.put("error", "could not find target or parent element");
		} else if(parent.find(child.getFullName()).size() > 1) {
			result.put("id", id);
			result.put("parent", parent.getString("id"));
			child = parent.removeChild(id);
			if(child != null) {
				this.cache.removeElement(id);
				result.put("id", id);
				result.put("parent", parent.getString("id"));
			}
		} else {
			child.clearMappingsRecursive();
		}

		save();
		
		return result;
	}

	
	public String getXML() {
		String xml = "";

		if(this.document != null) {
			xml = XMLUtils.toXML(this.document.getMappings());
			xml = XMLFormatter.format(xml);
		}
		
		return xml;
	}
	
	public JSONObject getMetadata() {
		return new JSONObject();
	}

	public JSONObject getConfiguration() {
		return configuration;
	}

	public void setConfiguration(JSONObject configuration) {
		this.configuration = configuration;
	}

	public JSONObject getViews() {
		SchemaConfiguration configuration = this.getSchema().getConfiguration();
		if(configuration != null) {
			Views views = configuration.getViews();
			if (views != null) return views.asJSONObject();
		}
		return null;
	}
	
	public Collection<Element> find(String xpath) {
		return this.document.mappings.find(xpath);
	}
	
	public Collection<Element> find(String xpath, String rootId) {
		if (rootId == null) 
			return this.find(xpath);
		Element root = this.cache.getElementHandler(rootId);
		if (root != null)
			return root.find(xpath);
		else
			return new ArrayList<Element>();
	}
	
	public String findValue(String xpath) {
		Element result = this.document.mappings.findFirst(xpath);
		ArrayList<SimpleMapping> constants = result.getAllMappings(SimpleMapping.MAPPING_TYPE_CONSTANT);
		if(constants.size() > 0) return constants.get(0).getValue();
		return null;
	}
	
	public String findValue(String xpath, String rootId) {	
		if(rootId == null) return this.findValue(xpath);
		
		Element root = this.cache.getElementHandler(rootId);
		Element result = root.findFirst(xpath);
		ArrayList<SimpleMapping> constants = result.getAllMappings(SimpleMapping.MAPPING_TYPE_CONSTANT);
		if(constants.size() > 0) return constants.get(0).getValue();
		return null;
	}

	
	public synchronized void save() {
		log.debug("SAVING");
		if(this.document != null && this.document.getItem() != null) {
			// merge item to session
			Item item = this.document.getItem();
			
			item = (Item) DB.getSession().merge(item);
			this.document.setItem(item);

			// save item xml
			String xml = this.getXML();
			item.setXml(xml);
			
			Dataset dataset = DB.getDatasetDAO().findById(this.datasetId, false);
			dataset.updateItem(item);
			
			DB.commit();
			log.debug("Item saved");
		} else {
			log.debug("Item is null");
		}
	}
	
	public Long getDatasetId() {
		return this.datasetId;
	}
}
