<%@ page isErrorPage="true"%>
   
<%@ include file="_include.jsp"%>

<%@page pageEncoding="UTF-8"%>

<%! 
public void printException( JspWriter out, String title, Throwable th ) throws Exception  {
	if( th != null ) {
		out.println( "<h4>" + title + " " + th.getClass().getName()+ "</h4>");
		out.println( th.getMessage());
		out.println( "<br/>");
		
		for (StackTraceElement element : th.getStackTrace()) {
			out.println(element.toString());
			out.println("<br/>");
		}
		printException( out, "Caused by ", th.getCause());
	}
}
%>
<div class="panel-body">
 <div class="block-nav">

	<div class="summary">
		<s:if test="exception!=null">
        	<h4>Exception</h4>
        	<s:property value="%{exception.class.name}"/> <s:property value="%{exception.message}"/>

         	<s:set name="ex" value="%{exception}" scope="page"/>
        	<%
        		printException( out, "Full Stack Trace", (Exception)pageContext.getAttribute("ex"));
        	%>
      </s:if>
    </div>
 
 </div>
</div>





