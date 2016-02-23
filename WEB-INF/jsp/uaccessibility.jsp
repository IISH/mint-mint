<%@ include file="_include.jsp"%>
<%@ page language="java" errorPage="error.jsp"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="gr.ntua.ivml.mint.persistent.Organization"%>
<%@page import="gr.ntua.ivml.mint.util.Config"%>

<%  String uaction=(String)request.getAttribute("uaction");
   if(uaction==null){uaction="";}   
%>	
	
<div class="panel-body">

	<div class="block-nav">
	<div class="summary">
	<div class="label"></div>
	 <s:if test="hasActionErrors() || actionmessage!=null">
	 
	
	<% if( request.getAttribute( "actionmessage" ) != null ) {  %>
		<div class="errorMessage">
		<%=(String) request.getAttribute( "actionmessage" )%></div>
      <%}%>
					
		<s:if test="hasActionErrors()">Error:
		<s:iterator value="actionErrors">
			<div class="errorMessage"><s:property escape="false" /> </div>
		</s:iterator>
		</s:if>
			
	</s:if>		
	<s:else>
		<div class="info">&nbsp;</div>
	</s:else>
		</div>
		<div style="margin-top:10px; padding: 0 5px 0 5px;">

		<%--
	
if(u!=null && (uaction.equalsIgnoreCase("edituser") || uaction.equalsIgnoreCase("saveuser") || uaction.equalsIgnoreCase("createuser") )){ 
--%>
		
		
		<s:form name="accessform" action="Management" cssClass="athform" theme="mytheme">
				<div class="fitem">
				<label>	
			           Only root can publish
				</label>
				<% 
                    if(Config.getBoolean("disablePublish")) { 
                %> 
				    <s:checkbox name="publish" value="true" cssClass="checks"/>
				<%
                     } else { 
                %>
				    <s:checkbox name="publish" cssClass="checks"/>
				<% 
                    } 
                %>
				</div>
			
			<div class="fitem">
				<label>	
			           Only root can login
				</label>
				<%
                     if(Config.getBoolean("disableLogin")) { 
                %> 
				    <s:checkbox name="login" value="true" cssClass="checks"/>
				<%
                     } else { 
                %>
				    <s:checkbox name="login" cssClass="checks"/>
				<%
                     } 
                %>
				</div>
			
			
			<p align="left">
				<a class="navigable focus k-focus"  
					 onclick="loginAndPublishAccess();">
					 <span>Submit</span></a>  
				<a class="navigable focus k-focus" 
					 onclick="this.blur();document.usrform.reset();">
					 <span>Reset</span></a>  
			
					
				<input type="hidden" name="uaction" value="saveaccessibility"/>
				<s:if test="%{seluser.dbID!=null}">
				<s:hidden name="seluser.dbID" value="%{seluser.dbID}"/>				
			     </s:if>
				
				</p>
			
		</s:form>
		
		
		
		
		

</div>
</div>

</div>

