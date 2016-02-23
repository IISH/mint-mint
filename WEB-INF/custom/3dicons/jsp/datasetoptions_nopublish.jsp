<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="gr.ntua.ivml.mint.persistent.Dataset" %>
<%@ page import="java.util.*" %>
<%@ page import="gr.ntua.ivml.mint.util.*" %>
<%@ page import="com.opensymphony.xwork2.util.TextParseUtil" %>



	<s:set var="publicationSchema" value="@gr.ntua.ivml.mint.util.Config@get('3dicons.publish.schema')"/>
	<s:set var="oldpublicationSchema" value="@gr.ntua.ivml.mint.util.Config@get('3dicons.old.publish.schema')"/>
	
	<s:if test="user.can('publish data', du.organization) && du.organization.publishAllowed" >
		<s:if test="du.organization.publishing"> 
			<div title="Publication in Progress" class="items navigable" >
			<div class="label">Publication in Progress</div>
			</div>
		</s:if>
		<s:else>
			<s:if test="du.getBySchemaName(#publicationSchema).isPublished()" >
			<%-- <s:if test="du.getBySchemaName(#publicationSchema).isPublished()"> --%>
				<div title="Unpublish from MORE"
					data-load='{"kConnector":"html.page", "url":"UnpublishMore?datasetId=<s:property value='uploadId'/>", "kTitle":"Unpublish from MORE" }'class="items navigable">
					<div class="label">Unpublish from MORE</div>
				</div>
			</s:if>
			<s:if test="du.getBySchemaName(#oldpublicationSchema).isPublished()" >
			<%-- <s:if test="du.getBySchemaName(#publicationSchema).isPublished()"> --%>
				<div title="Unpublish from MORE"
					data-load='{"kConnector":"html.page", "url":"UnpublishMore?datasetId=<s:property value='uploadId'/>", "kTitle":"Unpublish from MORE" }'class="items navigable">
					<div class="label">Unpublish from MORE</div>
				</div>
			</s:if>
	<!--  if schema for carare is there, with valid items -->
	 		<s:if test="current.getValidBySchemaName(#publicationSchema)>0"> 
			<!--  check if org is publishing -->
			<%-- 	 <s:if test="!current.getOrganization().isPublishing()"> 
 			--%>	<!--  check publish or unpublish -->
	
				<div title="Publish to MORE"
				data-load='{"kConnector":"html.page", "url":"PublishMore?datasetId=<s:property value='uploadId'/>", "kTitle":"publish to MORE" }'class="items navigable">
				<div class="label">Publish to MORE</div>
				</div>
	
	
			</s:if>
		</s:else>
	</s:if>
	
	
		