<?xml version="1.0" encoding="iso-8859-1" standalone="yes"?>
<xsl:stylesheet 
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
<xsl:output method='xml'/>

<xsl:template match='/'>
        <xsl:apply-templates/>
</xsl:template>

<xsl:template match="*[local-name()= 'datafield']">
	<xsl:variable name="tag" select="concat('tag_', @tag)" />
	<xsl:element name="{$tag}">
		<xsl:if test="string-length(normalize-space(@ind1))&gt;0">
			<ind1><xsl:value-of select="@ind1" /></ind1>
		</xsl:if>
		<xsl:if test="string-length(normalize-space(@ind2))&gt;0">
			<ind2><xsl:value-of select="@ind2" /></ind2>
		</xsl:if>
		<xsl:apply-templates/>
	</xsl:element>
</xsl:template>

<xsl:template match="*[local-name()= 'controlfield']">
	<xsl:variable name="code" select="concat('ctrl_', @tag)" />
	<xsl:element name="{$code}">
		<xsl:apply-templates/>
	</xsl:element>
</xsl:template>


<xsl:template match="*[local-name()= 'subfield']">
	<xsl:variable name="code" select="concat('code_', @code)" />
	<xsl:element name="{$code}">
		<xsl:apply-templates/>
	</xsl:element>
</xsl:template>

<xsl:template match='*'>
        <xsl:element name="{local-name()}">
                <xsl:for-each select="@*">
                        <xsl:attribute name="{local-name()}">
                                <xsl:value-of select='.'/>
                        </xsl:attribute>
                </xsl:for-each><xsl:apply-templates/>
        </xsl:element>
</xsl:template>

<xsl:template match='text()'>
        <xsl:value-of select='.'/>
</xsl:template>

</xsl:stylesheet>