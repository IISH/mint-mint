(function($) {
	var widget = "annotatorElement";
	var defaults = {
		'xpath': undefined,
		'ajaxUrl': 'Annotator_ajax',
		'label': null
	};
	
	var data = {};
    
	$.fn.annotatorElement = function(method) {
	    if ( methods[method] ) {
	      return methods[ method ].apply(this, Array.prototype.slice.call(arguments, 1));
	    } else if ( typeof method == 'object' || ! method ) {
	      return methods.init.apply(this, arguments);
	    } else {
	      $.error( 'Method ' +  method + ' does not exist on ' + widget );
	    }   
	};
	
	function existsInArray(jsonArray, value) {
	    for (var i = 0; jsonArray.length > i; i += 1) {
	        if (jsonArray[i] == value) 
	            return true;
	    }
	    return false;
	}
	
	function schemaTree(container, tree) {
		/*container 
		.empty()
		.addClass("schema-tree-ann-container");*/
		var annotatorSchemaTree = 
		$("<div>").annotatorSchemaTree({
			target: tree,
			editor: this
		});
		//annotatorSchemaTree.appendTo(container);
		return annotatorSchemaTree;
	}
	
	function selectMode(container, tree) {
		var annotatorSchemaTree = schemaTree(container, tree);
		var tabs = $("<div>");
		var ul = $("<ul>").appendTo(tabs);
		$("<li>").append($("<a>").attr("href", "#edit").append($("<span>").text("Edit values"))).appendTo(ul);
		$("<li>").append($("<a>").attr("href", "#add").append($("<span>").text("Delete/Add elements"))).appendTo(ul);
		var editMode = $("<div>").attr("id", "edit").appendTo(tabs);
		editMode.append(annotatorSchemaTree);
		var addMode = $("<div>").attr("id", "add").appendTo(tabs);
		tabs.tabs({
			select: function(event, ui) {
				if(ui.index == 0){
					$("<div>").annotatorSchemaTree("refreshTree", true);
					editMode.append(annotatorSchemaTree);
				}
				else {
					$("<div>").annotatorSchemaTree("refreshTree", false);
					addMode.empty();
					addMode.append(annotatorSchemaTree);
				}					
			}
		});
		tabs.tabs("select", "edit");
		tabs.appendTo(container);

		/*
		var modeButtons=$("<span>").attr("style","padding:20px;align:center");
		var self = this;
		editMode = true;
		btnEdit = $("<button>")
		.attr("title", "Edit values")
		.attr('disabled', editMode)
		.text("Edit existing values");
		btnEdit.css("margin-right", "10px");
		btnNewDel = $("<button>")
		.attr("title", "Create/Delete elements")
		.attr('disabled', !editMode)
		.text("Create/Delete elements");
		btnEdit
		.click(function () {
			if (!editMode) {
			  $(this).prop("disabled", true);
			  editMode = true;
			  btnNewDel.prop("disabled", false);
			  $("<div>").annotatorSchemaTree("refreshTree", editMode);
			}
		}).appendTo(modeButtons);
		btnNewDel
		.click(function () {
			if (editMode) {
			  $(this).prop("disabled", true);
			  editMode = false;
			  btnEdit.prop("disabled", false);
			  $("<div>").annotatorSchemaTree("refreshTree", editMode);
			}
		}).appendTo(modeButtons);
		modeButtons.appendTo(container);
		*/
	}
	
	function enable(button) {
		button.prop("disabled", true);
	}
	
	var methods = {
		init: function(options) {
			options = $.extend({}, defaults, options);
			if(options.editor != undefined) {
				options.ajaxUrl = options.editor.ajaxUrl;
			}
			
			$(data).data(widget, {
				settings: options,
				container: this,
				target: options.target,
				editor: options.editor
			});
			
			selectMode(this, $(data).data(widget).target);
			return this;
		},
		
		settings: function() {
			return $(data).data(widget);
		},
		
		addCallData: function(fname, xpath, arguments) {
			var xpathData = {type: fname, xpath: xpath, arguments: arguments};
			groupActs.push(xpathData);
	    }
	};
})(jQuery);