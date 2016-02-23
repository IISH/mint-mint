(function($) {
	var widget = "annotatorFunction";
	var defaults = {
		'ajaxUrl': 'Annotator_ajax'
	};

	var functions = [
    	 /*{
    	 	name: "",
    	 	description: "No function",
    	 	arguments:[]
 	     },*/
	     {
	    	 name: "editValue",
	    	 description: "Set constant value",
	    	 	arguments: [
	    	 	{ label: "New value"}
	    	 	]
	     },
	     {
	    	 name: "editValueIf",
	    	 description: "Replace value",
	    	 	arguments: [
	    	 	{label: "Current value"},
	    	 	{label: "New value"}	    	 	
	    	 	]
	    	 //checkbox: "Delete the whole subtree (check only if you are sure)"
	     },
	     {
	    	 name: "appendToString",
	    	 description: "Add before/after certain substring",
	    	 	arguments: [
	    	 	{label: "Substring in existing value"},
	    	 	{label: "String to add"}
	    	 	] 
	     },
	     {
	    	 name: "append",
	    	 description: "Add before/after current value",
	    	 	arguments: [
	    	 	{label: "String to add"}
	    	 	] 
	     },
	     //TODO: dates transformation functions
	     {
	    	 	name: "extensionToULCase",
	    	 	description: "File extension to lower/upper case",
	    	 	//message: "Substring after '.' will be set to lower/upper case",
	    	 	arguments: []
	     },
	     /*
	     {
	    	 	name: "extensionToUpperCase",
	    	 	description: "File extension to upper case",
	    	    message: "Substring after '.' will be set to lower case"",
	    	 	arguments: []
	     },
	     */
	     {
	    	 	name: "substringToULCase",
	    	 	description: "Substring to lower/upper case",
	    	 	arguments: [
	    	 	            { label: "String to be converted"}
	    	 	            ]
	     }
	     /*
	     {
	    	 	name: "substringToLowerCase",
	    	 	description: "Substring to lower case",
	    	 	arguments: [
	    	 	            { label: "String to be converted" }
	    	 	            ]
	     }
	     */
    ];
    
	function render(container) {
		var data = container.data(widget);
		var name = data.settings.name;
		var fheader = $("<div>").addClass("annotator-function-header").appendTo(container);
		var fbody = $("<div>").addClass("annotator-function-body").appendTo(container);
		if (name != undefined)
			$("<span>").text("Apply Function to " + name).addClass("annotator-function-text").appendTo(fheader);
		fheader.append("<br/>");
		var select = $("<select>").attr("id", "annotator-function-call")
			.css("border", "1px solid")
			.css("background", "#f1f1f1")
			.css("padding", "0px")
			//.css("margin-left", "6px")
			.width('auto')
			//.width(250)
			.appendTo(fheader);	
		for (var f in functions) {
			var name = functions[f].name;
			var description = functions[f].description;
			var option = $("<option>").attr("value", name).text(description);
			if (name == "editValue") {
				option.attr("selected", 'selected');
			}
			if (data.settings.enumerations != undefined || data.settings.thesaurus != undefined) {
				if (name == "editValue" || name == "editValueIf") 
					option.appendTo(select);
			}
			else 
				option.appendTo(select);
		}
		
		renderArguments(container);
		select.change(function() {
			renderArguments(container);
		});
			
	}

	function renderArguments(container) {
		var data = container.data(widget);
		var xpath = data.settings.xpath;
		var select = container.find('#annotator-function-call').val();
		
		var fbody = container.find('.annotator-function-body');
		fbody.empty();
		
		var farguments = $("<div>").addClass("annotator-function-arguments").appendTo(fbody);

		for(var f in functions) {
			var funct = functions[f];
			var name = funct.name;
			
			if (name == select) {
				//container.find('#annotator-function-call').width(funct.description.length*7.6+10);
				var fcount = 0;
				if (funct.message != undefined) {
					farguments.append($("<div>").text(funct.message).addClass("mapping-function-message"));
				}				
				var value = undefined;
				for (var fa in funct.arguments) {
					var fargument = funct.arguments[fa];
					if (fargument.type == undefined) {
						value = container.annotatorFunction("datatypeCheck", data.settings.id, data.settings.enumerations, data.settings.thesaurus);
					}
					var row = $("<div>").addClass("annotator-function-argument").appendTo(farguments);
					var maxWidth = 5;
					if (fargument.label.length > maxWidth) {
						var label = $("<span>").text(fargument.label).appendTo(row);
						row.append("<br\>");
					}
					else
						var label = $("<span>").addClass("annotator-function-argument-label").text(fargument.label).appendTo(row);
					
					if (value != undefined)
						value.appendTo(row);
					fcount += 1;   
				}
				// checkbox to add in functions panel
				/*
				if (funct.checkbox != null) {
					var checkbox = $("<input type=\"checkbox\">").attr("value", "false").text(funct.checkbox).appendTo(row);
					row.append("<br\>");
				}
				*/
				/*
				var maxWidth = 100;
				farguments.find(".annotator-function-argument-label").each(function(k, v) {
					var width = $(v).outerWidth();
					if (width > maxWidth) maxWidth = width;
				});
				
				farguments.find(".annotator-function-argument-label").each(function(k, v) {
					$(v).css("width", maxWidth + "px");
				});
				*/
				//TODO: some cleaning with repeated calldata, create function
				if (name.indexOf("ULCase") > 0) {
					var upper = $("<span>").css("margin-left", "5px").text("To Upper")
					.button().appendTo(fbody);
					var lower = $("<span>").css("margin-left", "10px").text("To Lower")
						.button().appendTo(fbody);
					upper.click(function() {
						var calldata = container.annotatorFunction("calldata");
						if (calldata != undefined)
							$("<div>").annotatorElement("addCallData", calldata.apply.replace('UL', 'Upper'), 
								xpath, calldata.arguments);
					});
					lower.click(function() {
						var calldata = container.annotatorFunction("calldata");
						if (calldata != undefined)
							$("<div>").annotatorElement("addCallData", calldata.apply.replace('UL', 'Lower'), 
								xpath, calldata.arguments);
					});
				}
				else if (name.indexOf("append") >= 0) {
					var before = $("<span>").css("margin-left", "5px").text("Before")
					.button().appendTo(fbody);
					var after = $("<span>").css("margin-left", "10px").text("After")
						.button().appendTo(fbody);
					after.click(function() {
						var calldata = container.annotatorFunction("calldata");
						if (calldata != undefined)
							$("<div>").annotatorElement("addCallData", calldata.apply + "After", 
								xpath, calldata.arguments);
					});
					before.click(function() {
						var calldata = container.annotatorFunction("calldata");
						if (calldata != undefined)
							$("<div>").annotatorElement("addCallData", calldata.apply + "Before", 
								xpath, calldata.arguments);
					});
				}
				else {
					var apply = $("<span>").text("Apply")
					.css("margin-left", "5px").button().appendTo(fbody);//{disabled: true}
					apply.click(function() {
						//renderArguments(container);
						var calldata = container.annotatorFunction("calldata");
						if (calldata != undefined)
							$("<div>").annotatorElement("addCallData", calldata.apply, 
								xpath, calldata.arguments);
					});
				}
			}
		}
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

		calldata: function() {
			var data = this.data(widget);
			var call = data.container.find('#annotator-function-call').val();
			var args = [];
			var thesUndefined = false;
			data.container.find(".annotator-function-argument-value").each(function(k, v) {
				if ($(v).val() == "thesaurus values")
					thesUndefined = true;
				else
					args.push($(v).val());
			});
			
			var result = {
				apply: call,
				arguments: args
			};
			if (thesUndefined)
			   return undefined;
			else
				return result;
		},
		
		datatypeCheck: function(id, enumerations, thesaurus) {
			var value = undefined;
			if (enumerations == undefined) {
				//fargument.type = "text";
				value = $("<input>", { type: "text" }).addClass("annotator-function-argument-value");
				value.attr("id", "value-"+id);
				if (thesaurus != undefined) {
					/*value.text("thesaurus");
					value.addClass("undefined-thesaurus");*/
					value.css('color','grey').val("thesaurus values");
					value.on("dblclick click",function() {
						value.prop('disabled', true);
						var thesaurusContainer = $("<div>").addClass("mapping-thesaurus-container").append(node);
						var node = $("<div>").appendTo(thesaurusContainer);
						$("<div>").annotatorElement("settings").editor.loadSubpanel(function(panel) {
							var thesaurusContainer = $("<div>").addClass("mapping-thesaurus-container").append(node);
							var node = $("<div>").appendTo(thesaurusContainer);
							panel.find('.panel-options').after(thesaurusContainer);
							var thesaurusBrowser = new ThesaurusBrowser(node, {
								thesaurus: thesaurus,
								select: function(concept) {
									$("#value-"+id).val(concept.concept);
									$("#value-"+id).attr("size", value.val().length);
									value.prop('disabled', false);
								}
							});
						}, " thesaurus");
					});
				}
			}
			else {
				value = $("<select>").addClass("annotator-function-argument-value")
				.css("border", "1px solid")
				//.css("background", "#f1f1f1")
				.css("padding", "0px")
				//.css("margin-left", "6px")
				.width('auto');
				for(var e in enumerations) {
					var enumValue = enumerations[e];
					var option = $("<option>").attr("value", enumValue).text(enumValue);
					option.appendTo(value);
				}
			}
			return value;
		}
    };
	
	$.fn.annotatorFunction = function(method) {
	    if ( methods[method] ) {
	        return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
	      } else if ( typeof method === 'object' || ! method ) {
	        return methods.init.apply( this, arguments );
	      } else {
	        $.error( 'Method ' +  method + ' does not exist on ' + widget );
	      }  
	};
	
})(jQuery);
