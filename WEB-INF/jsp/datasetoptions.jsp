<%@ include file="_include.jsp"%>
<%@ page language="java" errorPage="error.jsp"%>
<%@page pageEncoding="UTF-8"%> 
<%@ page import="gr.ntua.ivml.mint.persistent.Transformation" %>
<%@ page import="gr.ntua.ivml.mint.persistent.Dataset" %>
<%@ page import="gr.ntua.ivml.mint.view.Import" %>
<%@ page import="gr.ntua.ivml.mint.util.Config" %>
<%@ page import="gr.ntua.ivml.mint.persistent.AnnotatedDataset" %>
<%@ page import="java.util.*" %>
<%@ page import="gr.ntua.ivml.mint.util.*" %>
<%@ page import="com.opensymphony.xwork2.util.TextParseUtil" %>


<div class="panel-body" <s:if test="current.isTransfOrAnnotatedWithTransfParent()"> style="background-color:#FFFEEE"</s:if>>
 <div class="block-nav">

	<div class="summary">
	
	<div class="label">
    <s:if test="current.isTransfOrAnnotatedWithTransfParent()"><img src="images/okblue.png" style="margin-top:10px;"></s:if><s:property value='current.name'/>
     <div style="float:right;font-size:0.5em;"><a onClick="$(this).closest('.label').next('div.info').find('div.setdetails').toggle()"><img border="0" align="middle" style="margin-right:3px;margin-top:13px;" src="images/plus.gif">Show details</a></div>
    </div>  

    <div class="info">
		<b>Status:</b> <s:if test="current.status=='FAILED'"><font color="red"><s:property value="current.message" /></font></s:if><s:else><font color="blue"><s:property value="current.message" /> </font></s:else>
		<br/>
		<div class="setdetails" style="display:none;">
		<s:if test="current.schema != ''"><b>Schema:</b> <font color="blue"><s:property value="current.schema"/></font>
		</s:if><br/>
		<b>No of Items:</b> <font color="blue"><s:property value="current.getNoOfItems()"/></font><br/>
		<b>Creation date:</b>  <font color="blue"><s:property value="current.getCreated()"/></font></br>
		<b>By user:</b> <font color="blue"><s:property value="current.getCreator()"/></font></div>
	</div>
	</div>
	
	     <s:if test="current.isProcessing()==true || current.status=='FAILED'">
	       <div title="Show log" data-load='{"kConnector":"html.page", "url":"Logview?datasetId=<s:property value="uploadId"/>", "kTitle":"Log view" }' class="items navigable">
				<div class="label">Show log</div>
				<div class="tail"></div>
			</div>
		</s:if>		
     
	    <s:if test="current.isProcessing()==false">
			 <!-- root is defined so higher option is show items -->
			 <s:if test="current.isRootDefined()==true && current.isItemized()==true">
			 <%if(user.hasRight(User.SUPER_USER) || (!Config.getBoolean("disablePublish") && user.hasRight(User.PUBLISH))){ %>
			    <cst:customJsp jsp="datasetoptions_nopublish.jsp">
			    <s:if test="!current.isTransfOrAnnotatedWithTransfParent()"> <!-- checking if this is an upload or transformation -->
			    <s:if test="current.isReadOnly()==true">
			      <div title="Unpublish" data-load='{"kConnector":"html.page", "url":"XSLselection?uploadId=<s:property value='uploadId'/>&orgId=<s:property value='organizationId'/>&userId=<s:property value='userId'/>&action=unpublish", "kTitle":"Dataset Publish" }' class="items navigable">
			     <div class="label">Unpublish</div>
			      <div class="tail"></div></div>   
			    </s:if>
			    <s:else>
				   <s:if test="du.isDirectlyPublishable()==true && current.getOrg().isPublishAllowed()">
				     <div title="Publish" data-load='{"kConnector":"html.page", "url":"XSLselection?uploadId=<s:property value='uploadId'/>&orgId=<s:property value='organizationId'/>&userId=<s:property value='userId'/>", "kTitle":"Dataset Publish" }' class="items navigable">
				     <div class="label">Publish</div>
				      <div class="tail"></div></div>   
				    </s:if>
				   <s:elseif test="du.isPublishable()==true && current.getOrg().isPublishAllowed()">
	                <div id="pubprep" title="Prepare for Publish" data-load='{"kConnector":"html.page", "url":"XSLselection_input?uploadId=<s:property value='uploadId'/>&orgId=<s:property value='organizationId'/>&userId=<s:property value='userId'/>", "kTitle":"Europeana Agreement" }' class="items navigable">
				    <div class="label">Prepare for Publish</div>
				      <div class="tail"></div></div>   
				    </s:elseif>
			    </s:else>
			    </s:if>
				</cst:customJsp>
			    <%} %> 
			   <div title="Show all items" data-load='{"kConnector":"html.page", "url":"ItemView.action?uploadId=<s:property value='uploadId'/>&organizationId=<s:property value='organizationId'/>&userId=<s:property value='userId'/>", "kTitle":"<s:property value="datasetType"/> Items" }' class="items navigable">
			   		<div class="label">Show all items</div>
					<div class="detail"><s:property value="getDu().getItemCount()"/> items</div>	
				   		<div class="tail"></div>
			   </div>

			   <s:if test="getDu().getInvalidItemCount() > 0">			   
				   <div title="Show invalid items" data-load='{"kConnector":"html.page", "url":"ItemView.action?uploadId=<s:property value='uploadId'/>&organizationId=<s:property value='organizationId'/>&userId=<s:property value='userId'/>&filter=invalid", "kTitle":"<s:property value="datasetType"/> Invalid Items" }' class="items navigable">
				   		<div class="label">Show invalid items</div>
						<div class="detail"><s:property value="getDu().getInvalidItemCount()"/> items</div>	
				   		<div class="tail"></div>
				   </div>
			   </s:if>
			    <%if(user.hasRight(User.MODIFY_DATA)){ %>
		 			<%
		 				if(user.hasRight(User.SUPER_USER) || !Config.getBoolean("ui.hide.mapping")) {
		 			%>
			  <!--  access mappings --> 
			   <div title="Mappings" data-load='{"kConnector":"html.page", "url":"MappingSummary.action?uploadId=<s:property value='uploadId'/>&orgId=<s:property value='organizationId'/>&userId=<s:property value='userId'/>", "kTitle":"Mappings" }' class="items navigable">
			   <div class="label">Mappings</div>
			   <div class="tail"></div></div>
			   		<%} %>

			   		<s:if test="du.getSchema() != null && !current.isPublished()" >
		 			<%
		 			if(user.hasRight(User.SUPER_USER) || !Config.getBoolean("ui.hide.annotator")) {
		 			%>			   		
			  		<!--  access annotations --> 
					<div title="Annotate" onclick="
					$K.kaiten('load', {kConnector: 'html.page', url: 'Annotator.action?uploadId=<s:property value='uploadId'/>', kTitle:'Annotator'});"
					class="items navigable">
					<div class="label">Annotate (in development)</div>
					<div class="tail"></div></div>
					<%} %>
					</s:if>			   		
					<%
		 				if(user.hasRight(User.SUPER_USER) || !Config.getBoolean("ui.hide.transform")) {
		 			%>
			   
			   <s:if test="current.isReadOnly()==false">
				    <div title="Transform" data-load='{"kConnector":"html.page", "url":"Transform_input.action?uploadId=<s:property value='uploadId'/>&organizationId=<s:property value='organizationId'/>", "kTitle":"Transform" }' class="items navigable">
				    <s:if test="current.isTransformed()">
				   <div class="label">Retransform</div>
				   </s:if><s:else><div class="label">Transform</div></s:else>
				   <div class="tail"></div></div>
			   </s:if>
			   		<%} %>
			  <%} %>
			  
			 </s:if>  
              <%if(user.hasRight(User.MODIFY_DATA)){ %>
		 			<%
		 				if(user.hasRight(User.SUPER_USER) || !Config.getBoolean("ui.hide.defineItems")) {
		 			%>
	          <s:if test="current.isReadOnly()==false && current.isTransfOrAnnotatedWithTransfParent()==false && current.isTransformed()==false && current.hasStats()==true">
	                       			
 			   <div title="Define Items"
 					  onclick="javascript: var cp=$($(this).closest('div[id^=kp]')); $K.kaiten('removeChildren',cp, false);
					  $K.kaiten('reload',cp,{kConnector:'html.page', url:'itemLevelLabelRequest?uploadId=<s:property value="uploadId"/>&transformed=<s:property value="current.isTransformed()"/>&orgId=<s:property value='organizationId'/>&userId=<s:property value="userId"/>', kTitle:'Define Items' });" class="items navigable">
				
					<div class="label">Define Items</div>
					<div class="info">
					<s:if test="current.isRootDefined()==true">
						<img  src="images/ok.png" style="vertical-align:sub;margin-top:10px;width:16px;height:16px;" onMouseOver="this.style.cursor='pointer';" title="root defined">
					</s:if>	
					</div>
					<div class="tail"></div>
				</div>
				</s:if>
					<%} %>
				<%} %>

			
			
			<%if(Config.get("with.schemas")!=null) {
			   Dataset ds = (Dataset) request.getAttribute( "du");
			   Set<String> schemas = TextParseUtil.commaDelimitedStringToSet(Config.get("with.schemas"));
			   if(( ds.getSchema() != null) && schemas.contains( ds.getSchema().getName() ) &&
					ds.getValidItemCount() > 0 ) {
			%>	
			    	<div title="Export To With" onclick="initWithCollection(${uploadId});" class="items navigable">
				    	<div class="label">Export to WITH</div>
				     	<div class="tail"></div>
				    </div>
			<% } 
			 }
			%>
			
			

			<s:if test="current.hasStats()"> 	   
		     <!-- access statistics -->
		     <div title="Statistics" data-load='{"kConnector":"html.page", "url":"Stats.action?uploadId=<s:property value='uploadId'/>", "kTitle":"Dataset Stats" }' class="items navigable">
			   <div class="head"><img src="images/stats2.png"></div>
			   <div class="label">Dataset Statistics</div>
			   <div class="tail"></div></div>
			 </s:if>
			<s:if test="!(current.status=='FAILED')">	    
			<!-- show log option -->
			 <div title="Show log" data-load='{"kConnector":"html.page", "url":"Logview?datasetId=<s:property value="uploadId"/>", "kTitle":"Log view" }' class="items navigable">
			<div class="label">Show log</div>
			<div class="tail"></div>	</div>    
			</s:if>
			<%if(user.hasRight(User.MODIFY_DATA)){ %>
	 			<%
	 			if(user.hasRight(User.SUPER_USER) || !(Config.getBoolean("ui.hide.deleteDataupload") && ((String) request.getAttribute("datasetType")).equals("Data Upload"))) {
		 		%>
				<s:if test="current.isReadOnly()==false">
				<div title="Delete <s:property value="notAnnDatasetType"/>"  class="items navigable"
				  onclick="var cp=$($(this).closest('div[id^=kp]'));$K.kaiten('removeChildren',cp, false);ajaxDatasetDelete('${current.name}', ${uploadId},${user.dbID},${organizationId},cp);">
		 			<div class="head"><img src="images/trash_can.png"></div>
				 			<div class="label">Delete <s:property value="notAnnDatasetType"/></div>
					<div class="info">
					</div>
					<div class="tail"></div>
				</div>
				</s:if>
	 			<%
	 				}
		 		%>
				<%if(((String) request.getAttribute("datasetType")).equals("AnnotatedDataset")) {
				%>

				<s:if test="current.isReadOnly()==false">
					<div title="Undo <s:property value="datasetType"/>"  class="items navigable"
					   onclick="
					    var cp=$($(this).closest('div[id^=kp]'));
					   ajaxUndoAnnotations('${current.name}', ${uploadId}, ${user.dbID},${organizationId},cp);">
					 	<div class="label">Reverse all annotations</div>
						<div class="info">
						</div>
						<div class="tail"></div>
					</div>
					</s:if>
				<%
	 				}
		 		%>
			<s:if test="current.downloads.size() > 0" >
			<div class="info">&nbsp;</div>
			   <div class="accordion">
      			   <h3><a href="#">Downloads </a></h3>
      			   <div>
			        <s:iterator value="current.downloads">
			        <div title="download" onclick="window.location='<s:property value="url"/>'" 
			             class="items clickable">			             
			 				<div class="head"><img src="images/kaiten/download-16.png"></div>
			 				<div class="label" style="width:80%">						
							  <s:property value="title" /> 
							</div>
							<div class="info">
							</div>
							<div class="tail"></div>
					</div>
			        </s:iterator>
			        </div>
			 </div>
			 </s:if>
			 <%} %>
	<s:if test="!current.isTransfOrAnnotatedWithTransfParent() && transformations.size>0">
	 <div class="summary">
	<div class="label" style="margin-bottom:-10px;margin-top:20px;">Transformations</div>
	
	
	</div>
         
		 <s:iterator id="trans" value="transformations">
		 
		  <%  
		  
			  Dataset t= (Dataset) request.getAttribute("trans");
		      Import tvew=new Import(t);
		   %>
		   
		   			<div title="<s:property value="name"/>" 
		 				onclick="var cp=$($(this).closest('div[id^=kp]'));
		 				$K.kaiten('removeChildren',cp, false);$(cp).find('div.k-active').removeClass('k-active');$(this).toggleClass('k-active');
		 				$K.kaiten('load',{kConnector:'html.page', url:'DatasetOptions.action?uploadId=<s:property value="dbID"/>', kTitle:'Dataset Options' });"
		 			   class="items navigable">
		 				
						<div class="label" style="width: 80%; margin-left: 10px;">						
					
							
						 <s:property value="name"/>&nbsp;&nbsp;&nbsp;<font size="0.8em"><s:property value="created"/></font></div>
						
						
						<div class="info">
							 <img id="transcontext<s:property value="dbID"/>" src="<%=tvew.getStatusIcon() %>" style="vertical-align:sub;margin-top:10px;width:16px;height:16px;" onMouseOver="this.style.cursor='pointer';" title="<%=tvew.getMessage()%>">
												
						</div>
						<div class="tail"></div>
					</div>
					<%=printTransformation(t,1,null)%>
		   			</s:iterator>
		   			
           
          
			 
	</s:if> 
</s:if> 
		
	 
</div></div>


<%!private StringBuffer printTransformation(Dataset parent, int depth, StringBuffer html ){
	if( html == null ) html = new StringBuffer();
   if(parent.getDerivedTransfLeaves()!=null){
	 //get list containing Transformations and/or AnnotatedDatasets
	for(Dataset d: parent.getDerivedTransfLeaves() ) {
		 //if(d instanceof gr.ntua.ivml.mint.persistent.Transformation ){
			    //Transformation tr=(Transformation)d;
			    Import tvew=new Import(d);
			 html.append("<div title=\""+d.getSchema().getName()+"\" onclick=\"var cp=$($(this).closest('div[id^=kp]'));$(cp).find('div.k-active').removeClass('k-active');$(this).toggleClass('k-active');$K.kaiten('removeChildren',cp, false);"+
			 				"$K.kaiten('load',{kConnector:'html.page', url:'DatasetOptions.action?uploadId="+d.getDbID()+"', kTitle:'Dataset Options' });\""
			 			    +" class=\"items navigable\">");
			 				
							html.append("<div class=\"label\" style=\"width: 80%; margin-left: 10px;\">");						
							int spacedepth=depth;
							while(spacedepth>0){
								 spacedepth--;
							     html.append("&nbsp;&nbsp&nbsp;&nbsp;");}
							     
							
								
							 html.append(d.getName()+" &nbsp;&nbsp;<font size=\"0.8em\">"+d.getCreated()+"</font></div>");
							
							html.append("<div class=\"info\">"+
									 "<img id=\"transcontext"+d.getDbID()+"\" src=\""+tvew.getStatusIcon()+"\" style=\"vertical-align:sub;margin-top:10px;width:16px;height:16px;\" onMouseOver=\"this.style.cursor='pointer';\" title=\""+tvew.getMessage()+"\">"
							   	+"</div><div class=\"tail\"></div></div>");
			 
			          	 html=printTransformation(d,depth+1,html);
			 //}
		}
	}
	return html;
}%>

<script type="text/javascript">
withBaseUrl='<%=Config.getWithDefault("with.url", "" )%>';

jQuery(document).ready(function(){
	$(".accordion").accordion({ header: "h3", autoHeight: false, collapsible: true, active: false});
	    var search = "Transformation unsuccessfull"; 
	    var cp=$('div[id^="kp"]:last');
	    var $titlediv = $('div.summary');
	    elm=$(cp).find('div.summary>div.info');
	    if (elm.html() != null) {
	    	elm.html(elm.html().replace(search,"<span style='color:red'>"+search+"</span>"));
	    }
	 

	   
});

</script>