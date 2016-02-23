(function($) {
	var widget = "annotatorDuplicate";
	var defaults = {};
	var idTreeInfoMap = {};
    var descendantIds = [];
    var enumerations = undefined;
    var thesaurus = undefined
    
	function render(container) {
		var data = container.data(widget);
		var treeContainer = data.settings.treeContainer;
		var id = data.settings.id;
		enumerations = data.settings.enumerations;
		thesaurus = data.settings.thesaurus;
		descendantIds = [];
		idTreeInfoMap = data.settings.idTreeInfoMap;
		treeContainer.jstree("open_all", $("#schema-tree-" + id));
		var duplicateOff = data.settings.duplicateOff[id] = [];
		$("#schema-tree-" + id).addClass("group-annotator-border");
		childrenDupl([id], duplicateOff, id);
		duplicateOff.splice($.inArray(id, duplicateOff), 1);
		$("#commit" + id).remove();
		var apply = $("<span>").text("Apply")
		.button()
		.attr("id", "commit"+id)
		.appendTo($("#schema-tree-" + id));
		apply.css("margin-left", $("#schema-tree-" + id).width()/2 - apply.outerWidth()/2);
		apply.css({"top": "5px", "margin-bottom":"10px"});
		apply.click(function() {
			var xpathValues = [];
			$.each(descendantIds, function(i, cid) {
				var value = $("#setValue"+cid).find(".annotator-function-argument-value").first().val();					
				if (value.length > 0) {
					var treeInfo = idTreeInfoMap[cid];
					xpathValues.push({"xpath": treeInfo.xpath, "value": value});
				}
			});
			$("<div>").annotatorSchemaTree("unclick", id,  idTreeInfoMap[id].xpathHolderId);
			$('<div>').annotatorSchemaTree("addCallData", "add new", idTreeInfoMap[id].xpath, xpathValues);
		});
		/*treeContainer.on("open_node.jstree", function(data, event) {
			//TODO: if node id is in descendant ids, reopen with values view
			var id = data.node.data.xpathHolderId;
			alert(id);
		    var childrenIds = treeInfo.childrenIds;
			alert(JSON.stringify(childrenIds));
			alert(JSON.stringify(descendantIds));
			$.each(descendantIds, function(i, cid) {
				if (childrenIds.indexOf(cid) >= 0) {
					$("<div>").annotatorDuplicate("setValue", id, id);
				}
			});
		});*/
	}

	function childrenDupl(ids, duplicateOff, parentId) {
		$.each(ids, function(i, id) {
			var treeInfo = idTreeInfoMap[id];
			var isSimpleContent = treeInfo.isSimpleContent;
			var names = treeInfo.xpath.split("/");
			var name = names[names.length-1];
			var childrenIds = treeInfo.childrenIds;
			duplicateOff.push(id);
			if (isSimpleContent || name.indexOf("@") == 0)
				$("<div>").annotatorDuplicate("setValue", id, parentId);
			childrenDupl(childrenIds, duplicateOff, parentId);
		});
	}
	
	var methods = {
		init: function(options) {
			options = $.extend({}, defaults, options);
			this.data(widget, {
				settings: options,
				container: this
			});
			render(this);
			return(this);
		},
		setValue: function(id, parentId) {
			if (parentId != id) {
				$("#setValue"+id).remove();
				$("#commit"+id).remove();
				$("#schema-tree-" + id).removeClass("group-annotator-border");
			}
			descendantIds.push(id);
			var fbody = $("<div>").attr("id", "setValue"+id);
			var value = $("<div>").annotatorFunction("datatypeCheck", enumerations, thesaurus);
			value.css("marginLeft", "20px");
			/*if (enumerations == undefined) {
				value = $("<input>", { type: "text" }).addClass("annotator-function-argument-value");
				value.css("marginLeft", "20px");
			}
			else {
				value = $("<select>").addClass("annotator-function-argument-value")
				.css("border", "1px solid")
				//.css("background", "#f1f1f1")
				.css("padding", "0px")
				.css("marginLeft", "20px")
				.width('auto');
				for(var e in enumerations) {
					var enumValue = enumerations[e];
					var option = $("<option>").attr("value", enumValue).text(enumValue);
					option.appendTo(value);
				}
			}*/
			value.appendTo(fbody);
			$("#schema-tree-"+id).children("a").first().after(fbody);
		}	
    };
	
	$.fn.annotatorDuplicate = function(method) {
	    if ( methods[method] ) {
	        return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
	      } else if ( typeof method === 'object' || ! method ) {
	        return methods.init.apply( this, arguments );
	      } else {
	        $.error( 'Method ' +  method + ' does not exist on ' + widget );
	      }  
	};
	
})(jQuery);