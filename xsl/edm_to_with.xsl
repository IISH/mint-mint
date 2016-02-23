<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet exclude-result-prefixes="rdf edm ore dcterms dc"
  version="2.0" xmlns:dc="http://purl.org/dc/elements/1.1/"
  xmlns:dcterms="http://purl.org/dc/terms/"
  xmlns:edm="http://www.europeana.eu/schemas/edm/"
  xmlns:ore="http://www.openarchives.org/ore/terms/"
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:with="http://www.ntua.gr/WITH"
  xmlns:xalan="http://xml.apache.org/xalan" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <xsl:apply-templates select="/rdf:RDF"/>
  </xsl:template>
  <xsl:template match="/rdf:RDF">
    <!-- with:withJson, id: 0 -->
    <with:withJson>
      <!-- Check for mandatory elements on with:title -->
      <xsl:if test="edm:ProvidedCHO/dc:title">
        <!-- with:title, id: 1 -->
        <xsl:for-each select="edm:ProvidedCHO/dc:title">
          <xsl:if test="position() = 1">
            <with:title>
              <xsl:value-of select="."/>
            </with:title>
          </xsl:if>
        </xsl:for-each>
      </xsl:if>
      <!-- Check for mandatory elements on with:description -->
      <xsl:if test="edm:ProvidedCHO/dc:description">
        <!-- with:description, id: 2 -->
        <xsl:for-each select="edm:ProvidedCHO/dc:description">
          <xsl:if test="position() = 1">
            <with:description>
              <xsl:value-of select="."/>
            </with:description>
          </xsl:if>
        </xsl:for-each>
      </xsl:if>
      <!-- with:source, id: 3 -->
      <xsl:for-each select="ore:Aggregation/edm:provider">
        <xsl:if test="position() = 1">
          <with:source>
            <xsl:value-of select="."/>
          </with:source>
        </xsl:if>
      </xsl:for-each>
      <!-- with:sourceId, id: 4 -->
      <xsl:for-each select="edm:ProvidedCHO/dc:identifier">
        <xsl:if test="position() = 1">
          <with:sourceId>
            <xsl:value-of select="."/>
          </with:sourceId>
        </xsl:if>
      </xsl:for-each>
      <!-- with:sourceUrl, id: 5 -->
      <xsl:for-each select="ore:Aggregation/@rdf:about">
        <xsl:if test="position() = 1">
          <with:sourceUrl>
            <xsl:value-of select="."/>
          </with:sourceUrl>
        </xsl:if>
      </xsl:for-each>
      <!-- with:thumbnailUrl, id: 6 -->
      <xsl:for-each select="ore:Aggregation/edm:isShownBy/@rdf:resource">
        <xsl:if test="position() = 1">
          <with:thumbnailUrl>
            <xsl:value-of select="."/>
          </with:thumbnailUrl>
        </xsl:if>
      </xsl:for-each>
      <!-- with:rights, id: 7 -->
      <xsl:for-each select="ore:Aggregation/dc:rights/@rdf:resource">
        <xsl:if test="position() = 1">
          <with:rights>
            <xsl:value-of select="."/>
          </with:rights>
        </xsl:if>
      </xsl:for-each>
      <!-- with:type, id: 8 -->
      <xsl:for-each select="edm:ProvidedCHO/dc:type">
        <xsl:if test="position() = 1">
          <with:type>
            <xsl:value-of select="."/>
          </with:type>
        </xsl:if>
      </xsl:for-each>
      <!-- with:provider, id: 9 -->
      <xsl:for-each select="ore:Aggregation/edm:dataProvider">
        <xsl:if test="position() = 1">
          <with:provider>
            <xsl:value-of select="."/>
          </with:provider>
        </xsl:if>
      </xsl:for-each>
    </with:withJson>
  </xsl:template>
</xsl:stylesheet>