<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet exclude-result-prefixes="foaf1 mrel gr edmfp xml"
  version="2.0"
  xmlns:crm="http://www.cidoc-crm.org/rdfs/cidoc_crm_v5.0.2_english_label.rdfs#"
  xmlns:dc="http://purl.org/dc/elements/1.1/"
  xmlns:dcterms="http://purl.org/dc/terms/"
  xmlns:edm="http://www.europeana.eu/schemas/edm/"
  xmlns:edmfp="http://www.europeanafashion.eu/edmfp/"
  xmlns:foaf="http://xmlns.com/foaf/0.1/"
  xmlns:foaf1="http://xmlns.com/foaf/0.1/"
  xmlns:gr="http://www.heppnetz.de/ontologies/goodrelations/v1#"
  xmlns:mrel="http://id.loc.gov/vocabulary/relators/"
  xmlns:ore="http://www.openarchives.org/ore/terms/"
  xmlns:owl="http://www.w3.org/2002/07/owl#"
  xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/"
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
  xmlns:skos="http://www.w3.org/2004/02/skos/core#"
  xmlns:wgs84="http://www.w3.org/2003/01/geo/wgs84_pos#"
  xmlns:xalan="http://xml.apache.org/xalan"
  xmlns:xml="http://www.w3.org/XML/1998/namespace" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output indent="yes"/>

  <xsl:param name="var1" select="'http://mint-projects.image.ntua.gr/europeana-fashion/'"/>
  
  
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