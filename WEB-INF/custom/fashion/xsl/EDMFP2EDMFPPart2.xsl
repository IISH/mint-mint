 <xsl:output indent="yes"/>
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>        
    </xsl:template>
     
    <xsl:template match="edm:ProvidedCHO/@rdf:about">
        <xsl:attribute name="rdf:about">
            <xsl:value-of select="concat($var1, $var2, '_', substring-after(.,'localID/europeana-fashion/'))"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="ore:Aggregation/@rdf:about">
        <xsl:attribute name="rdf:about">
            <xsl:value-of select="concat($var1, $var2, '_', substring-after(.,'localID/europeana-fashion/'))"/>
        </xsl:attribute>
    </xsl:template>
    
     <xsl:template match="edm:aggregatedCHO/@rdf:resource">
        <xsl:attribute name="rdf:resource">
            <xsl:value-of select="concat($var1, $var2, '_', substring-after(.,'localID/europeana-fashion/'))"/>
        </xsl:attribute>
    </xsl:template>

    
    

</xsl:stylesheet>