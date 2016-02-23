<%@ include file="top.jsp" %>  
<%@page import="java.util.List"%>
<%@page import="gr.ntua.ivml.mint.persistent.Organization"%>
<%@page import="gr.ntua.ivml.mint.persistent.DataUpload"%>
<%@page import="gr.ntua.ivml.mint.db.DB" %>

<div id="home_logo" style="display: none">
	<cst:customJsp jsp="home_logo.jsp"></cst:customJsp>
</div>

<div id="home_custom" style="display: none">
	<cst:customJsp jsp="home_custom.jsp"></cst:customJsp>
</div>

<% String sessionId = request.getSession().getId();

%>
<script>
	document.title = "MINT Home";
	mintTitle = "<%= Config.get("mint.title") %>";
</script>
<style>
	#k-topbar #logout-button {
	    border : 0 none;
        background:url('images/logout.png') no-repeat;
        background-position: 6px 7px;
        width:32px;
        height:32px;
	}	

	#k-topbar #logout-button:hover {
	    border : 0 none;
        background:url('images/logout_hover.png') no-repeat;
        background-position: 6px 7px;
        width:32px;
        height:32px;
	}

	#k-topbar #help-button {
	    border : 0 none;
        background:url('images/help.png') no-repeat;
        background-position: 4px 4px;
        width:32px;
        height:32px;
	}	


</style>

<script>
	function recentMappings() {
		var recent = $("<div>");
		
		$.get("Recent", {
			type: "mapping"
		}, function(result) {			
			if(result.mappings != undefined) {
				recent.append($("<div>").addClass("summary").append($("<div>").addClass("label").text("Recent mappings")));
				for(var i in result.mappings) {
					var entry = result.mappings[i];
					var div = kTemplater.jQuery('line.navigation', {
						label : "Mapping: " + entry.mapping.name,
						data : { kConnector:"html.page", url:"MappingOptions.action?uploadId=" + entry.dataset.dbID + "&selaction=editmaps&selectedMapping=" + entry.mapping.dbID, kTitle:"Mapping" }
					}).css({ height: "50px" }).appendTo(recent);
					div.append($("<div>").addClass("label").css({ top: "20px" }).text("Dataset: " + entry.dataset.name));
				}
			}
		}, "json");
		
		return recent;
	}
	
	function recentAnnotations() {
		var recent = $("<div>");
		
		$.get("Recent", {
			type: "annotation"
		}, function(result) {
			recent.append($("<div>").addClass("summary").append($("<div>").addClass("label").text("Recent annotations")));
			
			if(result.annotations != undefined) {
				for(var i in result.annotations) {
					var entry = result.annotations[i];
					var div = kTemplater.jQuery('line.navigation', {
						label : "Dataset: " + entry.dataset.name,
						data : { kConnector:"html.page", url:"Annotator.action?uploadId=" + entry.dataset.dbID, kTitle:"Annotation" }
					}).appendTo(recent);
				}
			}
		}, "json");
		
		return recent;
	}

	(function($){
		// keep a reference to Kaiten's container
		$K = $('#container');
		// initialize Kaiten
		$K.kaiten({ 
			// 3 panels max. on the screen
			columnWidth : '33%',
			optionsSelector : '#custom-options-text',
			startup : function(dataFromURL){
				// handle URL parameters sent when opening a panel in a new tab
				if (dataFromURL && ('kConnector' in dataFromURL))
				{
					this.kaiten('load', dataFromURL);
				} else {
					// create the home panel HTML...
					var $navBlock = kTemplater.jQuery('block.navigation');
					
					var logo = $("<div>").html($("#home_logo").html());
					
					$navBlock.append(logo);
					
					$navBlock.append(kTemplater.jQuery('line.summary', {
						label	: 'MINT Home',
						info	: 'MINT services compose a web based platform designed and developed to facilitate aggregation initiatives for cultural heritage content and metadata in Europe.<a class="wiki-page" href="http://mint.image.ece.ntua.gr/redmine/projects/mint/wiki/Introduction">..(read more)</a>',
						iconURL : 'images/mintsmall.png'
					}));

					<%if(user.hasRight(User.VIEW_DATA) ||user.hasRight(User.MODIFY_DATA) || user.hasRight(User.ADMIN) || user.hasRight(User.PUBLISH)){
					  long orgid=-1;
					  if(user.getOrganization()!=null){
						  orgid=user.getOrganization().getDbID();
					  }
					  %> 
					 
					// ...add navigation to a page on our server...
					$navBlock.append(kTemplater.jQuery('line.navigation', {
						label : 'My workspace',
						data : { kConnector:"html.page", url:"ImportSummary.action?orgId=<%=orgid%>", kTitle:"My workspace" }
					}));<%}%>
					// ...add navigation to a page on our server...
					$navBlock.append(kTemplater.jQuery('line.navigation', {
						label : 'My account',
						data : { kConnector:"html.page", url:"Profile.action", kTitle:"My account" }
					}));
					
				<%if(user.hasRight(User.ADMIN)){%> 
					$navBlock.append(kTemplater.jQuery('line.navigation', {
						label : 'Administration',
						data : { kConnector:"html.page", url:"Management_input", kTitle:"Administration area" }
					}));
				<%}%>
				
				<%if(user.hasRight(User.MODIFY_DATA) || user.hasRight(User.ADMIN)){%> 						
					$navBlock.append(kTemplater.jQuery('line.navigation', {
						label : 'Locks',
						data : { kConnector:"html.page", url:"LockSummary", kTitle:"Locks" }
					}));
                <%}%>                
                
                <%if(user.hasRight(User.SUPER_USER) || Config.getBoolean("mint.enableGoalReports", false)){%>			
					$navBlock.append(kTemplater.jQuery('line.navigation', {
						label : 'Data reports',
						data : { kConnector:"html.page", url:"DataReport", kTitle:"Data Report" }
					}));
					<%}%>                    
	            
	                <%if(user.hasRight(User.SUPER_USER) || Config.getBoolean("mint.enableGoalReports")){%>			
						$navBlock.append(kTemplater.jQuery('line.navigation', {
							label : 'Data statistics',
							data : { kConnector:"html.page", url:"DataStatistics", kTitle:"Data Statistics" }
						}));
						<%}%>


				this.kaiten('load', function(data, $panel, $kaiten){
					$panel.kpanel('setTitle', 'MINT Home');							
					return kTemplater.jQuery('panel.body', { content : $navBlock });
				});
	
				$($K.data("kaiten").selectors.optionsCustom).hide();
				$($K.data("kaiten").selectors.appMenuContainer).append($("<button>").attr("title", "Help").attr("id", "help-button").click(function () {
					Mint2.documentation.openDocumentation({
						resource: "/documentation",
						target: "_blank"
					});
				}));
					
				$($K.data("kaiten").selectors.appMenuContainer).append($("<button>").attr("title", "Logout").attr("id", "logout-button").click(function () {
						location.href="Logout.action";
					}));
				}				
			}
		});
	})(jQuery);
	  
</script>
<script>
	$(document).ready(function () {
		var nav = $(".block-nav");
		nav.append(recentMappings());
		nav.append(recentAnnotations());
		var deepLink = <s:property value="deepLink" escape="false"/>;
		for( var i=0; i<deepLink.length; i++ ) {
			var link = deepLink[i];
			$K.kaiten('load',{ kConnector:'html.page', url:link.url, 'kTitle':link.title });
			
		}
		// $K.kaiten('load',{ kConnector:'html.page', url:'Import_input.action', 'kTitle':'Start Import' });
		// $K.kaiten('load',{ kConnector:'html.page', url:'Import_input.action', 'kTitle':'Start Another Import' });

		
	});
</script>

</div>

</body>
</html>

