/**
 * Tree widget that shows and handles a dataset's schema.
 * 
 * @class SchemaTree
 * @constructor
 * @this {SchemaTree}
 * 
 * @param containerId
 * @param {String|jQuery} Id of an element or jQuery element that will be used as the widget's container.
 * @param {Object} options widget's options.
 * @param {Number} options.dataUploadId dbID of dataset.
 * @param {function} [options.select] function callback that will be called on tree's node selection.
 * @param {function} [options.drop] function callback that will be called on tree's node drag & drop operation.
 * @param {function} [options.afterLoad] function callback that will be called after schema loads.
 * @param {Number} [options.ajaxUrl="Tree"] action URL used in ajax calls.
 * @returns
 */
function SchemaTree(containerId, options) {
	this.defaults = {
			dataUploadId: null,
			select: null,
			drop: null,
			afterLoad: null,
			ajaxUrl: "Tree"
	}

	this.options = $.extend({}, this.defaults, options);
	this.ajaxUrl = this.options.ajaxUrl;
	this.selectNodeCallback = this.options.select;
	this.dropCallback = this.options.drop;
	this.afterLoadCallback = this.options.afterLoad;

	this.schema = null;
	this.idTreeInfoMap = {};
	if(containerId != undefined) {
		this.render(containerId);
	}	
	
	this.iconsClicked = null;
}

/**
 * Render the tree in the specified container
 */
SchemaTree.prototype.render = function (container) {
	if(container instanceof jQuery) {
		this.container = container;
	} else {
		this.container = $("#" + container);		
	}
	
	this.container.empty();
	this.container.addClass("schema-tree-container");

	var self = this;

	this.searchContainer = Mint2.searchBox({
		prompt: "Search in input schema",
		callback: function(term) {
			self.search(term);
		}
 	}).addClass("schema-tree-search").appendTo(this.container);
	
	var tree = this.treeContainer = $("<div>").addClass("schema-tree").appendTo(this.container);

	if(this.options.dataUploadId != undefined) {
		this.loadFromDataUpload();
	}
	this.refresh();
}

/**
 * Load schema from specified dataset.
 * 
 * @param {Number} dataUploadId Dataset's dbID.
 * @param {function} afterLoadCallback callback that will be called after loading of the schema. Overrides default afterLoad callback.
 */
SchemaTree.prototype.loadFromDataUpload = function(dataUploadId, afterLoadCallback) {
	var id = dataUploadId;
	if(dataUploadId == undefined) {
		id = this.options.dataUploadId;
	}
	
	$.ajax({
		url: this.ajaxUrl,
		context: this,
		data: {
			dataUploadId: id,
			annotatorMode: false
		},
		success: function(response) {
			if(response != undefined) {
				if(response.error != undefined) {
					alert(response.error);
				} else if(response) {
					this.load(response.tree);
					if(afterLoadCallback != undefined) {
						afterLoadCallback();
					}
				}				
			} else {
				alert("Could not retrieve tree data");
			}
		}
	});	
}

/**
 * Set a tree schema and refresh.
 */
SchemaTree.prototype.load = function(schema) {
	this.schema = schema;
	this.refresh();
}

/**
 * Refresh widget contents.
 */
SchemaTree.prototype.refresh = function() {
	var tree = this;
	
	if (this.treeContainer == null) return;

	this.treeContainer.empty();
	if(this.schema == null) {
		this.treeContainer.text("No data loaded");
	} else {
		SchemaTree.prototype.fillXpathIdMap(this.schema, this.idTreeInfoMap);
		this.treeContainer.jstree({
			plugins : ["search", "json", "dnd", "unique"],
			core: {
				data: this.schema,
				animation: 100,
				"check_callback" : false,
				themes : {
					name : "proton",
					responsive : true,
					dots : false
				}
			}
		});
		this.treeContainer.bind("loaded.jstree open_node.jstree", function(event, data) {
			var tags = [];
			tree.treeContainer.find("a ").each(function(k, v) {
				/*var tag = $(v).contents().filter(function() {
						return this.nodeType == 3
		        	}).text();*/
				tags.push($(v).text());
			});	
			tree.treeContainer.find(".jstree-themeicon").each(function(k, v) {
				$(v).click(function () {
					var p = $(this).parent().parent();
					var id = parseInt(p.attr("id").split("schema-tree-")[1], 10);
					//if (tree.iconsClicked.indexOf(id) < 0) 
						//tree.iconsClicked.push(id);
					tree.iconsClicked = id;
				});
			});
			tree.searchContainer.find("input").autocomplete({
				source: tags
			});
			//tree.treeContainer.append($("<div>").css("height", "80px"));
			SchemaTree.prototype.highlight(tree);
		});
		
		this.treeContainer.bind("select_node.jstree", function(event, data) {
			var jsonData = data.instance.get_node(data.selected[0]);
			var xpathHolderId = jsonData.original.metadata.xpathHolderId;
			if (tree.selectNodeCallback != null && tree.iconsClicked == xpathHolderId) {//.indexOf(xpathHolderId) > -1) {
				tree.selectNodeCallback(jsonData.original.metadata);
				tree.iconsClicked = null;
				//tree.iconsClicked.splice($.inArray(xpathHolderId, tree.iconsClicked), 1);
			}
		});
		
		$(document)
		.on('dnd_move.vakata', function(e, data) {
			var t = $(data.event.target);
			if (t.closest('.schema-tree-drop').length) {
				data.helper.find('.jstree-icon')
						.removeClass('jstree-er').addClass(
								'jstree-ok');
			} else {
				data.helper.find('.jstree-icon')
				.removeClass('jstree-ok').addClass('jstree-er');
			}
		})
		.on('dnd_stop.vakata', function(e, data) {
			var t = $(data.event.target).closest(
					'.schema-tree-drop');
			if (t.length && tree.dropCallback != null) {
				var source = data.data.origin
				.get_node(data.element).original.metadata;
				tree.dropCallback(source, t);
			}
		});
	}	

}

/**
 * Search for a specified term in the schema and highlight any results.
 */
SchemaTree.prototype.search = function(term) {
	this.treeContainer.find(".jstree-search").removeClass("jstree-search");
	this.treeContainer.jstree("search", term);
}


SchemaTree.prototype.getNodeId = function(xpath) {
	return this.idTreeInfoMap[xpath];
}


SchemaTree.prototype.updateXpathMappings = function (xpathMappings, editor) {
	this.xpathMappings = xpathMappings;
	this.editor = editor;
	SchemaTree.prototype.highlight(this);
}

/**
 * Focus on a schema's node with the specified xpath.
 * @param {String} xpath XPath of node that should be focused.
 */
SchemaTree.prototype.selectXPath = function(xpath) {
	xxx = xpath;
	var id = this.getNodeId(xpath);
	if(id != undefined) {
		var selector = "#schema-tree-" + id;
		this.treeContainer.find(".jstree-search").removeClass("jstree-search");
		this.treeContainer.jstree("select_node", selector);
		$(selector).children("a").addClass("jstree-search");
		
		var scrollTop = $(selector).offset().top - this.treeContainer.offset().top + this.treeContainer.scrollTop();
		this.treeContainer.scrollTop(scrollTop);
		
		var scrollLeft = $(selector).offset().left - this.treeContainer.offset().left + this.treeContainer.scrollLeft();
		this.treeContainer.scrollLeft(scrollLeft) - 10;
		
		return true;
	}
	
	return false;
}


/**
 * Highlight nodes that correspond to the specified xpaths.
 * @param {Array} xpath List of string xpaths.
 */
SchemaTree.prototype.highlight = function(self) {
	self.treeContainer.find("a").css({
		//"color": "blue",
		//"font-weight": "normal",
		"text-decoration": "none"
	});
	var xpaths = {};
    for (var xpath in self.xpathMappings) {
        var nodeId = self.idTreeInfoMap[xpath];
        if (nodeId != undefined) {
	        var node = $("#schema-tree-" + nodeId)
	        .children("a")
	        .attr("id", nodeId)
	        .css({
				//"color": "blue",
				//"font-weight": "bold",
				"text-decoration":  "underline"
			});
	        xpaths[nodeId]=xpath;
	        node.click(function() {
	        	var id = parseInt($(this).attr("id"), 10);
	        	var targetId = self.xpathMappings[xpaths[id]];
	        	//if (self.iconsClicked.indexOf(id) < 0) {
	        	if (self.iconsClicked != id) {
					self.editor.showMapping(targetId);
	        	}
			});
        }
    }
}

SchemaTree.prototype.fillXpathIdMap = function(tree, map) {
	$.each(tree, function(k, node) {
		var xpath = node.metadata.xpath;
		var title = node.text;
		var id = node.metadata.xpathHolderId;
		var children = node.children;
		map[xpath] = id;
		if (children.length == 0 || children == undefined) {
			return;
		} else {
			SchemaTree.prototype.fillXpathIdMap(children, map);
			return;
		}
	});
}

function contains(array, item) {
	for (var i in array) {
		if (array[i] == item) //indexOf uses ===
			return true;
	}
	return false;
}