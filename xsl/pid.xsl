<?xml version="1.0" encoding="UTF-8" ?>

<!--
Takes the isShownAt value. If set, replaces:
/edm:ProvidedCHO@rdf:about="http://mint-projects.image.ntua.gr/data/hope/810109">
/edm:aggregatedCHO@rdf:resource
/ore:Aggregation@rdf:about
-->

<xsl:stylesheet version="2.0"
                xmlns:edm="http://www.europeana.eu/schemas/edm/"
                xmlns:ore="http://www.openarchives.org/ore/terms/"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <xsl:variable name="isShownAt" select="rdf:RDF/ore:Aggregation/edm:isShownAt/@rdf:resource"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="ore:Aggregation/@rdf:about">
        <xsl:attribute name="rdf:about">
            <xsl:value-of select="concat($isShownAt, '#aggregation')" />
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="edm:ProvidedCHO/@rdf:about">
        <xsl:attribute name="rdf:about">
            <xsl:value-of select="concat($isShownAt, '#cho')" />
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="edm:aggregatedCHO/@rdf:resource">
        <xsl:attribute name="rdf:resource">
            <xsl:value-of select="concat($isShownAt, '#cho')" />
        </xsl:attribute>
    </xsl:template>

</xsl:stylesheet>