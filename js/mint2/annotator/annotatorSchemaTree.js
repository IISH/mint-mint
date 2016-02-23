(function($) {
	var widget = "annotatorSchemaTree";
	var defaults = {
		'ajaxUrl' : 'Annotator_ajax'
	};
	var data = {};
	var clicked = [];
	var iconClicked = [];
	var iconClickedFlag = false;
	var duplicateOff = {};
	var idTreeInfoMap = {};
    var afterOpen = false; 
    
	$.fn.annotatorSchemaTree = function(method) {
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(
					arguments, 1));
		} else if (typeof method == 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist on ' + widget);
		}
	};

	function closeAllDescendants(id) {
		var children = $("#schema-tree-" + id).find(".jstree-open");
		children.each(function() {
			var cid = $(this).attr('id');
			$("#" + cid).switchClass("jstree-open", "jstree-closed");
			$('#setValue' + cid.split("e-")[1]).remove();
			// treeContainer.jstree("close_node", $("#"+cid));
		});
	}

	/*
	function focusOnNode(id) {
		var nodeData = findNode(id, tree[0]);
		initSchemaTree(this, [nodeData]);
	}
	
	function findNode(id, jsonData) {
		var nodeData = undefined;
		if (jsonData.data.xpathHolderId == id) {
			return jsonData;
		}
		else {
			var children = jsonData.children;
			if (children != undefined && children.length > 0) {
				$.each(jsonData.children, function(k, v) {
				   nodeData = findNode(id, v);
				   if (nodeData != undefined) {
					   return false;
				   }
				});
				return nodeData;
			}
			else return undefined;
		}
	}
	*/
    function findLevel(el, level) {
    	var parent = el.parent().parent();
    	if (parent.attr("id") != undefined) {
    		if (parent.attr("id") != "treeContainer") {
    			return findLevel(parent, level + 1); 
    		}
			else {
				return level;
			}
    	}
    	else {
    		return level;
    	}
    }

    function initSchemaTree(container, tree) {
		var rootName = tree[0].text;
		container.empty().addClass("schema-tree-ann-container");
		if (tree === undefined) {
			container.text("Schema not defined");
		} else {
			this.treeContainer = $("<div>").addClass("schema-tree-ann")
			// .css("text-align", "center")
			// .css('font-weight', 'bold')
			// .css('font-size', 15)
			.attr("id", "treeContainer");

			this.searchContainer = Mint2.searchBox({
				prompt : "Search in schema",
				callback : function(term) {
					searchInTree(treeContainer, term);
				}
			}).addClass("schema-tree-search").appendTo(container);

			treeContainer.appendTo(container);
			treeContainer.jstree({
				plugins : [ "json", "search", "dnd"],//, "grid"],
				core : {
					// animation: 0,
					data : tree,
					"check_callback" : false,
					themes : {
						name : "proton",
						responsive : true,
						dots : false
					}
				}
			/*
				grid: { columns: [ 
				  //{title:"_DATA_",  header: "a", width: 50}, 
				  {width: 50},
				  {width: 10, header: "icons", 
					  value: function(node){
					  //var br = $("</br>");
					  var icon = $("<div>")
					  .addClass("group-annotator-duplicate")
					  .attr("title", "Duplicate element");
					  return(icon));
				  }}
				  ]
			  }
			  */
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
					if (t.length) {
						var targetArea = $(t).children(".mapping-element-mapping-area").first();
						targetArea.removeClass("defaultTextInput");
						var source = data.data.origin
								.get_node(data.element).data.xpath;
						targetArea.val(source);
					}
			});

			treeContainer.on("loaded.jstree refresh.jstree", function(event, data) {
				treeContainer.jstree("close_all");
				treeContainer.jstree('open_node', $("#schema-tree-" + tree[0].data.treeId));
			});
			
			treeContainer.on("open_node.jstree", function(event, data) {
				var id = data.node.data.treeId;
				//if node is clicked, have to show duplicate-delete options
				treeContainer.find(".jstree-themeicon").each(function(k, v) {
					$(v).click(function () {
						var p = $(this).parent().parent();
						$("<div>").annotatorSchemaTree("iconClickEvent",  p.attr("id").split("schema-tree-")[1]);
					});
				});
				if (clicked.indexOf(id)>=0 && !contains(iconClicked, id)) {
					afterOpen = true;
					$("<div>").annotatorSchemaTree("unclick", id, data.node.data.xpathHolderId);
					treeContainer.trigger('select_node.jstree', data);
				}
			});
			treeContainer.on("close_node.jstree", function(event, data) {
				var id = data.node.data.treeId;
				$("#commit"+id).remove();
			});
			// keep helpful info in map to avoid repeated and time-consuming searches in tree data
			fillXpathIdMap(tree, idTreeInfoMap);
		}
	}

	function refreshEditMode(container) {
		container.switchClass("schema-tree-ann-newDelMode-container",
				"schema-tree-ann-container");
		// clicking on info icon
		clicked = [];
		iconClicked = [];
		treeContainer.unbind("select_node.jstree");
		treeContainer.jstree("deselect_all");
		treeContainer.jstree("refresh");
		//open_node overwrites children, i.e. editFunction disappears when expanding
		treeContainer.bind("select_node.jstree", function(event, data) {
			var jsonData = data.node;
			var metadata = jsonData.data;
			var minOccurs = metadata.minOccurs;
			var maxOccurs = metadata.maxOccurs;
			var isSimpleContentType = metadata.simpleContentType;
			var title = jsonData.text;
			var id = metadata.treeId;
			if (clicked.indexOf(id) < 0 && !contains(iconClicked, id)) {
				if ((isSimpleContentType || title.indexOf("@") == 0)) {
					var fpopup = $("<div>").attr("id", "editFunctions" + id)
							.css("marginLeft", "20px");
					var beforeChildren = $("#schema-tree-" + id).children("a");
					beforeChildren.after(fpopup);
					fpopup.annotatorFunction({
						xpath : metadata.xpath,
						id: id,
						enumerations: metadata.enumerations,
						thesaurus: metadata.thesaurus,
						name : title
					});
					clicked.push(id);
				}
			} else
				$("<div>").annotatorSchemaTree("unclick", id, metadata.xpathHolderId);
			iconClickedFlag = false;
		});
	}

	function refreshNewDelMode(container) {
		// container.css({background: '#FFFF80'});
		container.switchClass("schema-tree-ann-container",
				"schema-tree-ann-newDelMode-container");
		treeContainer.jstree("deselect_all");
		treeContainer.unbind("select_node.jstree");
		// treeContainer.jstree("reload");
		clicked = [];
		iconClicked = [];
		treeContainer.jstree("refresh");
		treeContainer.bind("select_node.jstree", function(event, data) {
			var jsonData = data.node;
			var metadata = jsonData.data;
			var minOccurs = metadata.minOccurs;
			var maxOccurs = metadata.maxOccurs;
			var title = jsonData.text;
			// var title = data.rslt.obj.text().substring(2);
			var id = metadata.treeId;
			//$("#schema-tree-" + id + " a").addClass('jstree-clicked');
			if (clicked.indexOf(id) < 0 && !contains(iconClicked, id)) {
				clicked.push(id);
				if (duplicateOff[id] == undefined) {// && closedEl.indexOf(id) < 0) {
					var parent = jsonData.parent;
					if (parent !== "#") {
						var el = $("#schema-tree-" + id);
						var iconDelete = $("<div>").addClass("group-annotator-removeAll")
							.text("   ").attr("title", "Remove all elements");
						var iconDeleteIf = $("<div>").addClass("group-annotator-remove")
							.text("   ").attr("title", "Remove elements with value");
						iconDeleteIf.click(function() {
							$("#schema-tree-" + id).children(".annotator-function-body").first().remove();
							var fbody = $("<div>").addClass("annotator-function-body").width("220px").css("marginLeft", "20px");
							$("<span>").text("Remove elements with value:").addClass("annotator-function-text").appendTo(fbody);
							fbody.append("<br/>");
							var value = $("<input>").addClass("annotator-function-argument-value")
							.appendTo(fbody);
							var icon = $("#schema-tree-" + id).children("div").last();
							icon.after(fbody);
							fbody.append("<br>");
							var apply = $("<span>").text("Apply")
							.css("margin-left", "80px").button().appendTo(fbody);
							apply.click(function() {
								$("<div>").annotatorSchemaTree("addCallData", 
										"remove", metadata.xpath, [value.val()]);
							});
							/*$('input').blur(function() {
								$("<div>").annotatorSchemaTree("addCallData", 
										"remove", metadata.xpath, [value.val()]);
							});
							$('input').keyup(function(e){
							    if(e.keyCode == 13)
									$("<div>").annotatorSchemaTree("addCallData", 
												"remove", metadata.xpath, [value.val()]);
							});*/
							$("<div>").annotatorSchemaTree("hideIcons", id);
						});
						var level = findLevel(el, 0);
						var el1 = el.children("a").first();
						var margin = 300 - el1.outerWidth() - level*22;
						iconDelete.css("margin-left", margin+"px");
						iconDelete.click(function() {
							$("<div>").annotatorSchemaTree("hideIcons", id);
							$("<div>").annotatorSchemaTree("addCallData", 
								"remove", metadata.xpath, [ "*" ]);
						});
						//var allowDuplicate = !(title.indexOf("@") == 0);
						//Attributes have usually maxOccurs = -1 (undefined). Assume that this means maxOccurs=1 though.
						var allowDuplicate = true; //allow creation always, so that missing obligatory elements can be added
						var allowDelete = true; //allow deletetion always for similar reasons
						//var allowDelete = !(maxOccurs == 1 && minOccurs == 1) && !(minOccurs == 1 && title.indexOf("@") == 0);
						if (allowDelete && !allowDuplicate) {
							// allow deletion only
							el1.after(iconDelete);
							iconDelete.after(iconDeleteIf);
						} else if (allowDelete && allowDuplicate) {
							// allow duplication
							var iconDuplicate = $("<div>").addClass("group-annotator-duplicate");
							iconDuplicate.text("   ").attr("title", "Add new element");
							iconDuplicate.click(function() {
								//focusOnNode(id);
								$("<div>").annotatorDuplicate({
									treeContainer : treeContainer,
									id : id,
									enumerations: metadata.enumerations,
									thesaurus: metadata.thesaurus,
									duplicateOff : duplicateOff,
									idTreeInfoMap : idTreeInfoMap
								});
								$("#delDupl" + id).remove();
								$("<div>").annotatorSchemaTree("hideIcons", id);
							});
							el1.after(iconDelete);
							iconDelete.after(iconDeleteIf);
							iconDeleteIf.after(iconDuplicate);
						}
					}
				}
				else {
					$("<div>").annotatorDuplicate({
						treeContainer : treeContainer,
						id : id,
						duplicateOff : duplicateOff,
						idTreeInfoMap : idTreeInfoMap
					});
				}
			}
			else {
				$("<div>").annotatorSchemaTree("unclick", id, metadata.xpathHolderId);
			}
			iconClickedFlag = false;	
		});
	}

	var methods = {
		init : function(options) {
			options = $.extend({}, defaults, options);
			if (options.editor != undefined) {
				options.ajaxUrl = options.editor.ajaxUrl;
			}

			this.treeContainer = $("<div>").addClass("schema-tree-ann")
			// .css("text-align", "center")
			// .css('font-weight', 'bold')
			// .css('font-size', 15)
			.attr("id", "treeContainer");
			$(data).data(widget, {
				settings : options,
				container : this
			});
			// data = this.data(widget);

			initSchemaTree(this, $(data).data(widget).settings.target);

			refreshEditMode(this);

			return this;
		},

		addCallData : function(fname, xpath, arguments) {
			var xpathData = {
				type : fname,
				xpath : xpath,
				arguments : arguments
			};
			groupActs.push(xpathData);
		},

		refreshTree : function(editMode) {
			if (editMode)
				refreshEditMode($(data).data(widget).container);
			else
				refreshNewDelMode($(data).data(widget).container);
		},
		hideIcons: function(id) {
			$("#schema-tree-" + id).children(".group-annotator-remove").first().remove();
			$("#schema-tree-" + id).children(".group-annotator-removeAll").first().remove();
			$("#schema-tree-" + id).children(".group-annotator-duplicate").first().remove();
		},
		unclick : function(id, xpathHolderId) {
			if (iconClickedFlag == false) {
				$('#editFunctions' + id).remove();
				$("#schema-tree-" + id).children(".group-annotator-remove").first().remove();
				$("#schema-tree-" + id).children(".group-annotator-removeAll").first().remove();
				$("#schema-tree-" + id).children(".group-annotator-duplicate").first().remove();
				$("#schema-tree-" + id).children(".annotator-function-body").first().remove();
				$("#schema-tree-" + id).children(".annotator-function-header").first().remove();
				//closeAllDescendants(id);
				clicked.splice($.inArray(id, clicked), 1);
				if ($("#schema-tree-" + id).hasClass("group-annotator-border")) {
					$("#schema-tree-" + id).removeClass("group-annotator-border");
					$("#setValue" + id).remove();
					$("#commit" + id).remove();
					$.each(duplicateOff, function(i, cid) {
						$("#setValue" + cid).remove();
					});
					treeContainer.jstree("close_node", $("#schema-tree-" + id));
				}
				if (!afterOpen) 
					duplicateOff[id] = undefined;
				afterOpen = false;
			}
			if (iconClickedFlag == true && contains(iconClicked, id)) {
				// TODO: add show statistics code here
				iconClicked.splice($.inArray(id, iconClicked), 1);
				/*$("<div>").annotatorElement("settings").editor.loadSubpanel(function(panel) {
					var details = $("<div>").css("padding", "10px");
					var content = $("<div>").appendTo(details).addClass("editor-preview");
					var body = panel.find(".panel-body");
					details.css("height", body.height() - 20);
					details.css("overflow", "auto");
					panel.find(".panel-body").before(details);
					content.append(Mint2.message("No item loaded", Mint2.WARNING));
				}, "Preview");*/
				$("<div>").annotatorElement("settings").editor.loadSubpanel(function(panel) {
					var details = $("<div>").css("padding", "10px");
					var browser = $("<div>").appendTo(details);
					panel.find(".panel-body").before(details);
					browser.valueBrowser({
						xpathHolderId: xpathHolderId
					});
				}, "Values");
			}
		},
		iconClickEvent : function(itemId) {
			// var name = $.trim($(item).parent().text());
			/*var parent = $(item).parentsUntil("li[title]").parent();
			var tree = treeContainer.jstree("get_json", $("#"
					+ parent.attr("id")));*/
			iconClicked.push(itemId);
			iconClickedFlag = true;
		}
	};

})(jQuery);

function fillXpathIdMap(tree, map) {
	$.each(tree, function(k, node) {
		var xpath = node.data.xpath;
		//var title = node.text;
		var id = node.data.treeId;
		var isSimpleContent = node.data.simpleContentType;
		//var maxOccurs = node.data.maxOccurs;
		//var minOccurs = node.data.minOccurs;
		var children = node.children;
		var childrenIds = [];
		$.each(children, function(j, child) {
			childrenIds.push(child.data.treeId);
		});
		map[id] = {
			//"title" : title,
			"xpath" : xpath,
			"xpathHolderId": node.data.xpathHolderId,
			"isSimpleContent" : isSimpleContent,
			"childrenIds" : childrenIds
			//"maxOccurs" : maxOccurs,
			//"minOccurs" : minOccurs
		};
		if (children.length == 0 || children == undefined) {
			return;
		} else {
			fillXpathIdMap(children, map);
			return;
		}
	});
}

function searchInTree(treeContainer, term) {
	treeContainer.find(".jstree-search").removeClass("jstree-search");
	treeContainer.jstree("search", term);
}

function iterateOverNodes(tree) {
	$.each(tree, function(k, node) {
		var children = node.children;
		if (node.medadata.simpleContentType) {// children.length == 0 || children
											// == undefined) {
			var id = node.data.xpathHolderId;
			// alert(node.data.title + " " +
			// jQuery("#treeContainer").jstree("select_node", "#schema-tree-" +
			// id).html());
			$("#schema-tree-" + id + " a")
			// .find("a")
			.css({
				"text-decoration" : "underline"
			});
			return;
		} else {
			iterateOverNodes(children);
			return;
		}
	});
}

function contains(array, item) {
	for ( var i in array) {
		if (array[i] == item) // indexOf uses ===
			return true;
	}
	return false;
}

function getJustText(element) {
	var text = element.contents().filter(function() {
		return this.nodeType == 3;
	}).text();
	return text;
}
