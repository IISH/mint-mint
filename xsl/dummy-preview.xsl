<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output indent="yes" method="html" encoding="utf-8" omit-xml-declaration="yes"/>

    <!-- Stylesheet to remove all namespaces from a document -->
    <!-- NOTE: this will lead to attribute name clash, if an element contains
        two attributes with same local name but different namespace prefix -->
    <!-- Nodes that cannot have a namespace are copied as such -->



    <!-- template to copy elements -->
    <xsl:template match="/">
    <html>
    	<head>
    		<title>Preview for Item</title>
    	</head>
    	<body> 
        	<xsl:apply-templates />        
	</body>
	</html>
    </xsl:template>
    
    <xsl:template match="/*[local-name() = 'metadata']">
    	<xsl:apply-templates select="*"/>
    </xsl:template>
    
    <xsl:template match="*">
    	<xsl:choose>
    	<xsl:when test="./*">
    		<h3><xsl:value-of select="local-name()"/></h3>
    		<div style="margin-left: 30px;">
	    	<xsl:apply-templates select="*"/>    
        	</div>
    	</xsl:when>
    	<xsl:otherwise>
    		<p><b><xsl:value-of select ="local-name()"/></b>:
        		<xsl:value-of select="text()"/>
        		<xsl:for-each select="@*">
        		   <i><br/>
        		     @<xsl:value-of select="concat(name(), '=&quot;', ., '&quot;')"/>
        		   </i>
                </xsl:for-each>
        	</p>
    	</xsl:otherwise>
    	</xsl:choose>    
    </xsl:template>


</xsl:stylesheet>
