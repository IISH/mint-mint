(function($) {	
	var widget = "itemBrowser";
	var defaults = {
		'ajaxUrl': 'ItemList',
		'startIndex': 0,
		'pageSize': 15,
		'width': false,				// does not work well if itemList is inside a dialog
		'click': false,
		'annotatorMode': undefined,
		'itemId':undefined
	};
	
	var omittedItemIds = new Array();
	var selectedItemsNumber = 0;
	var annotMode = "";
	var select = true;
	var afterAggrSelect = false;
	
	var methods = {
		init: function(options) {
			omittedItemIds = new Array();
			selectedItemsNumber = 0;
			select = true;
			afterAggrSelect = false;
			options = $.extend({}, defaults, options);
			annotMode = options.annotatorMode;
			var columnWidth = options.width;
			if(columnWidth == false) columnWidth = this.parent().width();
			var columnItems = {
			    id: "label", name: "Item", field: "label", width: columnWidth, 
			    formatter: function(row, column, value, grid, item) {
					var style = "";
					if (item.valid == undefined) style = "";
					else if (item.valid == true) style = "style='color: green'";
					else style = "style='color: red'";
			    	var label = (item.label != undefined) ? item.label : "- label not set -";
			    	var itemView = "<span " + style + " title='#" + item.id + "'>" + label + "</span>"; 
					
					return itemView;
			    }
			};
			
			var columns = [];
			var checkboxSelector = new Slick.CheckboxSelectColumn({
			      cssClass: "slick-cell-checkboxsel"
			    });
			if (annotMode != undefined && annotMode == XMLAnnotator.MODE_GROUP) {
				columns.push(checkboxSelector.getColumnDefinition());
			}
			columns.push(columnItems);
			var self = this;
			var selections = {
				"all" : "Search everything",
				"label": "Search labels",
				"custom": "Solr query"
			};
			if (annotMode != undefined && annotMode == XMLAnnotator.MODE_GROUP) {
				selections = $.extend({}, selections, {"field": "Field search"});
			}
			Mint2.searchBox( {
				callback: function(term, type) {
					var data = self.data(widget);
					if (data.pagination != undefined)
						data.pagination.remove(); 
					data.pagination = undefined;
					data.settings.startIndex= 0; 
					if ($.trim(term)) {
						data.settings.queryType = type;
						if (type ==  "field") {
							data.settings.query = term.value;
							data.settings.xpath = term.xpath;
						}
						else 
							data.settings.query = term;
						self.itemBrowser("refresh");
					} else {
						data.settings.query = undefined;
						data.settings.queryType = undefined;
						self.itemBrowser( "refresh");
					}
					if (annotMode != undefined && annotMode == XMLAnnotator.MODE_GROUP && groupActs!=undefined) {
						if(groupActs.length>0 && groupActs[groupActs.length-1].type=="search"){
							groupActs.pop();
						}
						groupActs.push({"type":"search", "xpath":"","arguments": self.itemBrowser("getQuery")});
						omittedItemIds = new Array();
					}
				},
				selections: selections
			}).appendTo(this);
			
			var node = $("<div>").css({
				width: "100%",
				height: "100%",
			}).addClass("value-browser").appendTo(this);

			var dataView = new Slick.Data.DataView();
			var grid = new Slick.Grid(node, dataView, columns, {
				enableCellNavigation: true,
				dataItemColumnValueExtractor: function(item, colDef) {
					return item[colDef.field];
				}
			    
			});
			
			// wire up model events to drive the grid
			dataView.onRowCountChanged.subscribe(function (e, args) {
			    grid.updateRowCount();
			    grid.render();
			});
	
			dataView.onRowsChanged.subscribe(function (e, args) {
			    grid.invalidateRows(args.rows);
			    grid.render();
			});
			  
			grid.setSelectionModel(new Slick.RowSelectionModel({selectActiveRow: false}));
			if (annotMode != undefined && annotMode == XMLAnnotator.MODE_GROUP) {
			    grid.registerPlugin(checkboxSelector);
			    grid.onSelectedRowsChanged.subscribe(function (e) {
			        var selRows = grid.getSelectedRows();
			        // remove selected rows from omitted
			        //add unselected to omitted
			    	for (var i=0; i<grid.getData().getPagingInfo().totalRows; i++) {
			            var item = dataView.getItem(i);
			            if (item) {
			                var rowId = item.id;
			                var index = omittedItemIds.indexOf(rowId);
			                if (selRows.indexOf(i) >= 0) { //selected
			                	if (select == true) {
					                if (index >= 0) {
					                	omittedItemIds.splice(index, 1);
					                }
			                	}
					            else //select = false (equivalent to selectedItemsNumber=-1) 
					            	//means omit list contains SELECTED items !!!
					            	if (index < 0) {
					            		omittedItemIds.push(rowId); 
					            	}
			                }
			                else {
			                	if (select == true) {
				                	if (index < 0)
				                		omittedItemIds.push(rowId);
			                	}
				                else //select = false means that deselected item has to be 
				                	//removed from omit list
				                	if (index >= 0)
				                		omittedItemIds.splice(index, 1);
			                }
			            }
			        }
			        if (annotMode != undefined && annotMode == XMLAnnotator.MODE_GROUP && groupActs!=undefined) {
						if (groupActs.length>0 && groupActs[groupActs.length-1].type=="select") {
							groupActs.pop();
						}
						var newSelectedNumber = selectedItemsNumber - omittedItemIds.length;
						if (selectedItemsNumber == -1)
							newSelectedNumber = -1;
			            groupActs.push({"type":"select", "xpath":"","arguments": [newSelectedNumber, omittedItemIds]});		
                    }
		       });
			}
			Mint2.documentation.embed(node, "itemBrowser");
			this.data(widget, {
				settings: options,
				container: this,
				grid: grid,
				dataView: dataView
			});
			$(".pager").live("click", function() {
				options.pageSize=~~($(this).html());
				options.startIndex = 0; 
				$(".pagination").parent().remove();
				self.data(widget, {
					settings: options,
					container: self,
					grid: grid,
					dataView: dataView,
					pagination: undefined
				});
				self.itemBrowser("refresh");
			});
			if (annotMode != undefined && annotMode == XMLAnnotator.MODE_GROUP && groupActs!=undefined) {
				if(groupActs.length>0 && groupActs[groupActs.length-1].type=="search"){
					groupActs.pop();
				}
				groupActs.push({"type":"search", "xpath":"","arguments": self.itemBrowser("getQuery")});
			}
			self.itemBrowser("refresh", {"init": true});
			return this;
		},
		
		destroy: function() {
			this.removeData(widget);
		},
		
		settings: function() {
			return this.data(widget);
		},
		
		refresh: function(options) {
			var data = this.data(widget);
			var settings = data.settings;
			var query = {
					datasetId: settings.datasetId,
					start: settings.startIndex,
					max: settings.pageSize,
					itemId: settings.itemId
			};
			if(settings.filter != undefined) {
				query.filter = settings.filter;
			}
			if( settings.queryType != undefined ) {
				query.queryType = settings.queryType;
			}
			if( settings.query != undefined ) {
				query.query = settings.query;
			}
			if (settings.xpath != undefined) {
				query.xpath = settings.xpath;
			} 
			$.ajax({
				url: settings.ajaxUrl,
				context: this,
				dataType: 'json',
				data: query,
				success: function(response) {
					if (response != undefined) {
						data.list = response.values;
						data.dataView.beginUpdate();
						data.dataView.setItems(data.list);
						data.dataView.endUpdate();
						// reset the itemId, so the browser can work normally 
						settings.itemId = -1;
						selectedItemsNumber = response.total;
						if (annotMode != undefined && annotMode == XMLAnnotator.MODE_GROUP) {
							var selectionIds = new Array();
							if (select == false) {
								if (afterAggrSelect) {
									omittedItemIds = [];
									afterAggrSelect = false;
								}
								selectedItemsNumber = -1;
								data.grid.setSelectedRows(data.dataView.mapIdsToRows(omittedItemIds));
								/*for (var i in data.list)
									omittedItemIds.push(data.list[i].id);*/
							}
							else {
								if (afterAggrSelect && select == true)
									omittedItemIds = [];
								for (var i in data.list) {
									var itemId = data.list[i].id;
									if (omittedItemIds.indexOf(itemId) < 0)
										selectionIds.push(itemId);
								}
								var selection = data.dataView.mapIdsToRows(selectionIds);
								afterAggrSelect = false;
								data.grid.setSelectedRows(selection);
							}
						}
						data.grid.resizeCanvas();
						data.grid.onClick.subscribe(function (e, args) {
							var row = args.row;
							var item=data.dataView.getItem(row);
							item.index = settings.startIndex + row;
							if(data.settings.click != undefined) {
								data.settings.click(item);
							}
						});
						/*if (options.init) {
							if (groupActs!=undefined) {
					            groupActs.push({"type":"select", "xpath":"", "arguments": [selectedItemsNumber]});
							}
						}*/
						if (data.pagination == undefined) {
							var initPage = 0;
							if( response.page != undefined ) {
								initPage = response.page;
							}
						    data.pagination = $("<div>").css("margin-top", "5px").appendTo(data.container);
						    data.pagination.pagination(selectedItemsNumber, {
						        num_display_entries:5,
						        num_edge_entries: 1,
						        callback: function(page) {
									var start = page * data.settings.pageSize;
									data.settings.startIndex = start;
									data.container.itemBrowser("refresh");
									return false;
						    	},
						        load_first_page:false,
						        items_per_page:data.settings.pageSize,
						        current_page:initPage
						    });
					   }
					   if ($("#pageroptions").length == 0) {
						     data.pagination.append("<span id='pageroptions' style='float:right;'>Show: <a data='15' class='pager'>15</a><a data='100' class='pager'>100</a><a data='1000' class='pager'>1000</a>");
					   }	
					   if (annotMode != undefined && annotMode == XMLAnnotator.MODE_GROUP
							   && options != undefined && options.init) {
						   data.container.append("<br\><br\>");
						   var selectBtn = $("<a>")
						   	.css("color", "blue")
							.text("Deselect all")
							.click(function () {
								if (select == true) {
									select = false;
									afterAggrSelect = true;
									data.container.itemBrowser("refresh");
									$(this).text("Select all items");
								} else {
									select = true;
									omittedItemIds = [];
									afterAggrSelect = true;
									data.container.itemBrowser("refresh");
									$(this).text("Deselect all items");			
								}
							})
							.appendTo(data.container);
					  }
					} else {
						alert("Could not retrieve item list");
					}
				}/*
				error: function(xhr, ajaxOptions, thrownError) {
					alert("Some error occured");
				},*/
				
			});	
		},
		
		load: function(options) {
		},

		getQuery: function() {
			var settings = this.data(widget).settings;
			var query = "";
			var queryType = "";
			var xpath = "";
			if(settings.queryType != undefined ) {
				query = settings.query;
			}
			if( settings.query != undefined ) {
				queryType = settings.queryType;
			}
			if (settings.xpath != undefined) {
				xpath = settings.xpath;
			}
			var response =  {
					query: query, 
					queryType: queryType,
					xpath: xpath
			};
			return response;
		},
		
		getItemsToOmmit: function() {
			var data = this.data(widget);
			var itemsToOmmit = [];
			var selectRows = data.grid.getSelectedRows();
			//alert(JSON.stringify(data.list));
			var j=0;
			/*for (var i = 0; i < this.items.length; i++) {
				if (selectRows[j] != i) {
				   itemsToOmmit[i] = this.items[i].id;
				}
				else 
				  j++;
			}*/
			//alert(itemsToOmmit);
			return itemsToOmmit;
		}
	};
	
	$.fn.itemBrowser = function(method) {
	    if ( methods[method] ) {
	        return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
	      } else if ( typeof method === 'object' || ! method ) {
	        return methods.init.apply( this, arguments );
	      } else {
	        $.error( 'Method ' +  method + ' does not exist on ' + widget );
	      }   
	};
})(jQuery);
