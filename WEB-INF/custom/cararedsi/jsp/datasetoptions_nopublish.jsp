<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="gr.ntua.ivml.mint.persistent.Dataset" %>
<%@ page import="java.util.*" %>
<%@ page import="gr.ntua.ivml.mint.util.*" %>
<%@ page import="com.opensymphony.xwork2.util.TextParseUtil" %>



	<s:set var="publicationSchemas" value="@gr.ntua.ivml.mint.util.Config@get('cararedsi.publish.schemas').split(\",\")" />
	<s:if test="user.can('publish data', du.organization)" >
		<s:if test="du.isPublishing()"> 
			<div title="Publication in Progress" class="items navigable" >
			<div class="label">Publication in Progress</div>
			</div>
		</s:if>
		<s:else>
		
			<s:set var="hasToUnPublish" value="%{false}" />
			<s:iterator value="publicationSchemas" var="pschema">
	 			<s:if test="du.getBySchemaName(#pschema) != null && du.getBySchemaName(#pschema).isPublished()">
	 					<s:set var="hasToUnPublish" value="%{true}" />
	 			 </s:if>
	 		</s:iterator>
	 	 
			<s:if test="#hasToUnPublish" >
				<div title="Unpublish from MORE"
					data-load='{"kConnector":"html.page", "url":"UnpublishMore?datasetId=<s:property value='uploadId'/>", "kTitle":"Unpublish from MORE" }'class="items navigable">
					<div class="label">Unpublish from MORE</div>
				</div>
			</s:if>
			<s:else>
				<!--  if schema for carare is there, with valid items -->

				<s:set var="hasPSchema" value="%{false}"/>
				<s:iterator value="publicationSchemas" var="pschema">
		 			 <s:if test="current.getValidBySchemaName(#pschema)>0"> 
		 					<s:set var="hasPSchema" value="%{true}"/>
		 			 </s:if> 
		 		</s:iterator> 
				<!--  check if org is publishing -->
				<%-- 	 <s:if test="!current.getOrganization().isPublishing()"> --%>
	 			<!--  check publish or unpublish -->
	 			<%-- <s:if test="@gr.ntua.ivml.mint.actions.PublishPrepare@latestWithValidItems( du ) != null"> --%> 
				<s:if test="#hasPSchema"> 
		 					<div title="Publish to MORE"
								data-load='{"kConnector":"html.page", "url":"PublishMore?datasetId=<s:property value='uploadId'/>", "kTitle":"publish to MORE" }'class="items navigable">
								<div class="label">Publish to MORE</div>
							</div>
				</s:if>
			</s:else>
		</s:else>
	</s:if>
	
	
		