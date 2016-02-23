<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
  xmlns:cld="http://purl.org/cld/terms/"
  xmlns:crm="http://www.cidoc-crm.org/rdfs/cidoc_crm_v5.0.2_english_label.rdfs#"
  xmlns:dc="http://purl.org/dc/elements/1.1/"
  xmlns:dcterms="http://purl.org/dc/terms/"
  xmlns:ebucore="http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#"
  xmlns:edm="http://www.europeana.eu/schemas/edm/"
  xmlns:foaf="http://xmlns.com/foaf/0.1/"
  xmlns:mo="http://purl.org/ontology/mo/"
  xmlns:ore="http://www.openarchives.org/ore/terms/"
  xmlns:owl="http://www.w3.org/2002/07/owl#"
  xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/"
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
  xmlns:schema="http://schema.org/"
  xmlns:skos="http://www.w3.org/2004/02/skos/core#"
  xmlns:wgs84="http://www.w3.org/2003/01/geo/wgs84_pos#" 
  xmlns:xalan="http://xml.apache.org/xalan"
  xmlns:xml="http://www.w3.org/XML/1998/namespace" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output indent="yes"/>
    
  <xsl:template match="/">
    <xsl:apply-templates select="/rdf:RDF"/>
  </xsl:template>
  <xsl:template match="/rdf:RDF">
    <xsl:for-each select=".">
      <rdf:RDF>
        <xsl:for-each select="edm:ProvidedCHO">
          <edm:ProvidedCHO>
            <xsl:if test="@rdf:about">
              <xsl:attribute name="rdf:about">
                <xsl:for-each select="@rdf:about">
                  <xsl:if test="position() = 1">
                    <xsl:value-of select="."/>
                  </xsl:if>
                </xsl:for-each>
              </xsl:attribute>
            </xsl:if>
  
			<!-- dc:contributor -->
            <xsl:for-each select="dc:contributor">
              <dc:contributor>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:contributor>
            </xsl:for-each>
            
            <!-- dc:coverage -->
            <xsl:for-each select="dc:coverage">
              <dc:coverage>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:coverage>
            </xsl:for-each>
            
            <xsl:for-each select="skos:note">
              <dc:coverage>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:coverage>
            </xsl:for-each>
            
            <!-- dc:creator-->
            <xsl:for-each select="dc:creator">
              <dc:creator>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:creator>
            </xsl:for-each>
            
            <!-- dc:date -->
            <xsl:for-each select="dc:date">
              <dc:date>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:date>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:dateCopyrighted">
              <dc:date>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:date>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:modified">
              <dc:date>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:date>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:dateDigitised">
              <dc:date>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:date>
            </xsl:for-each>
            
            <!-- dc:description -->
            <xsl:for-each select="dc:description">
              <dc:description>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:description>
            </xsl:for-each>
            
            <!-- dc:format -->
            <xsl:for-each select="dc:format">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:audioTrackConfiguration">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:bitRate">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:fileSize">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:hasAudioEncodingFormat">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:hasMimeType">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:sampleSize">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>
            
            <!-- dc:identifier -->
            <xsl:for-each select="dc:identifier">
              <dc:identifier>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:identifier>
            </xsl:for-each>
            
            <!-- dc:language -->
            <xsl:for-each select="dc:language">
              <dc:language>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:language>
            </xsl:for-each>
            
            <!-- dc:publisher -->
            <xsl:for-each select="dc:publisher">
              <dc:publisher>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:publisher>
            </xsl:for-each>
            
            <!-- dc:relation -->
            <xsl:for-each select="dc:relation">
              <dc:relation>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:relation>
            </xsl:for-each>
            
            <!-- dc:rights -->
            <xsl:for-each select="dc:rights">
              <dc:rights>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:rights>
            </xsl:for-each>
            
            <!-- dc:source -->
            <xsl:for-each select="dc:source">
              <dc:source>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:source>
            </xsl:for-each>
            
            <!-- dc:subject -->
            <xsl:for-each select="dc:subject">
              <dc:subject>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:subject>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:hasGenre">
              <dc:subject>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:subject>
            </xsl:for-each>
            
            <!-- dc:title -->
            <xsl:for-each select="dc:title">
              <dc:title>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:title>
            </xsl:for-each>
            
            <!-- dc:type -->
            <xsl:for-each select="dc:type">
              <dc:type>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:type>
            </xsl:for-each>
            
            <!-- dcterms:alternative -->
            <xsl:for-each select="dcterms:alternative">
              <dcterms:alternative>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:alternative>
            </xsl:for-each>
            
            <!-- dcterms:conformsTo -->
            <xsl:for-each select="dcterms:conformsTo">
              <dcterms:conformsTo>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:conformsTo>
            </xsl:for-each>
            
            <!-- dcterms:created -->
            <xsl:for-each select="dcterms:created">
              <dcterms:created>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:created>
            </xsl:for-each>
            
            <!--  dcterms:extent -->
            <xsl:for-each select="dcterms:extent">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>
            
            <xsl:for-each select="mo:record_side">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>
            
            <xsl:for-each select="mo:track_number">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>
            
            <xsl:for-each select="mo:track_count">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:duration">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:audioChannelNumber">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>
            
            <xsl:for-each select="ebucore:sampleRate">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>
            
            <!--  dcterms:hasFormat -->
            <xsl:for-each select="dcterms:hasFormat">
              <dcterms:hasFormat>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:hasFormat>
            </xsl:for-each>
            
            <!--  dcterms:hasPart -->
            <xsl:for-each select="dcterms:hasPart">
              <dcterms:hasPart>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:hasPart>
            </xsl:for-each>
            
            <!--  dcterms:hasVersion -->
            <xsl:for-each select="dcterms:hasVersion">
              <dcterms:hasVersion>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:hasVersion>
            </xsl:for-each>
            
            <!--  dcterms:isFormatOf -->
            <xsl:for-each select="dcterms:isFormatOf">
              <dcterms:isFormatOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isFormatOf>
            </xsl:for-each>
            
            <!--  dcterms:isPartOf -->
            <xsl:for-each select="dcterms:isPartOf">
              <dcterms:isPartOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isPartOf>
            </xsl:for-each>
            
            <xsl:for-each select="edm:isGatheredInto">
              <dcterms:isPartOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isPartOf>
            </xsl:for-each>
            
            <!--  dcterms:isReferencedBy -->
            <xsl:for-each select="dcterms:isReferencedBy">
              <dcterms:isReferencedBy>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isReferencedBy>
            </xsl:for-each>
            
            <!--  dcterms:isReplacedBy -->
            <xsl:for-each select="dcterms:isReplacedBy">
              <dcterms:isReplacedBy>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isReplacedBy>
            </xsl:for-each>
            
            <!--  dcterms:isRequiredBy -->
            <xsl:for-each select="dcterms:isRequiredBy">
              <dcterms:isRequiredBy>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isRequiredBy>
            </xsl:for-each>
            
            <!--  dcterms:issued -->
            <xsl:for-each select="dcterms:issued">
              <dcterms:issued>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:issued>
            </xsl:for-each>
            
            <!--  dcterms:isVersionOf -->
            <xsl:for-each select="dcterms:isVersionOf">
              <dcterms:isVersionOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isVersionOf>
            </xsl:for-each>
            
            <!--  dcterms:medium -->
            <xsl:for-each select="dcterms:medium">
              <dcterms:medium>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:medium>
            </xsl:for-each>
            
            <!--  dcterms:provenance -->
            <xsl:for-each select="dcterms:provenance ">
              <dcterms:provenance >
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:provenance >
            </xsl:for-each>
            
            <!--  dcterms:references -->
            <xsl:for-each select="dcterms:references">
              <dcterms:references>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:references>
            </xsl:for-each>
            
            <!--  dcterms:replaces -->
            <xsl:for-each select="dcterms:replaces">
              <dcterms:replaces>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:replaces>
            </xsl:for-each>
            
            <!--  dcterms:requires -->
            <xsl:for-each select="dcterms:requires">
              <dcterms:requires>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:requires>
            </xsl:for-each>
            
            <!--  dcterms:spatial -->
            <xsl:for-each select="dcterms:spatial">
              <dcterms:spatial>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:spatial>
            </xsl:for-each>
            
            <!--  dcterms:tableOfContents -->
            <xsl:for-each select="dcterms:tableOfContents">
              <dcterms:tableOfContents>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:tableOfContents>
            </xsl:for-each>
            
            <!--  dcterms:temporal -->
            <xsl:for-each select="dcterms:temporal">
              <dcterms:temporal>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:temporal>
            </xsl:for-each>
            
            <!--  edm:currentLocation -->
            <xsl:for-each select="edm:currentLocation">
              <edm:currentLocation>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:currentLocation>
            </xsl:for-each>
            
            <!--  edm:hasMet -->
            <xsl:for-each select="edm:hasMet">
              <edm:hasMet>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:hasMet>
            </xsl:for-each>
            
            <!--  edm:hasType -->
            <xsl:for-each select="edm:hasType">
              <edm:hasType>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:hasType>
            </xsl:for-each>
            
            <!--  edm:incorporates -->
            <xsl:for-each select="edm:incorporates">
              <edm:incorporates>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:incorporates>
            </xsl:for-each>
            
            <!--  edm:isDerivativeOf -->
            <xsl:for-each select="edm:isDerivativeOf">
            <xsl:if test="@rdf:resource">
              <edm:isDerivativeOf>
                <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
               </edm:isDerivativeOf>
               </xsl:if>
            </xsl:for-each>
            
            <xsl:for-each select="mo:remaster_of">
            <xsl:if test="@rdf:resource">
              <edm:isDerivativeOf>        
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>       
              </edm:isDerivativeOf>
              </xsl:if>
            </xsl:for-each>
            
            <xsl:for-each select="schema:version">
            <xsl:if test="@rdf:resource">
              <edm:isDerivativeOf> 
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
              </edm:isDerivativeOf>
              </xsl:if>
            </xsl:for-each>
            
            <!--  edm:isNextInSequence -->
            <xsl:for-each select="edm:isNextInSequence">
              <edm:isNextInSequence>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isNextInSequence>
            </xsl:for-each>
            
            <!--  edm:isRelatedTo -->
            <xsl:for-each select="edm:isRelatedTo">
              <edm:isRelatedTo>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isRelatedTo>
            </xsl:for-each>
            
            <!--  edm:isRepresentationOf -->
            <xsl:for-each select="edm:isRepresentationOf">
              <edm:isRepresentationOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isRepresentationOf>
            </xsl:for-each>
            
            <!--  edm:isSimilarTo -->
            <xsl:for-each select="edm:isSimilarTo">
              <edm:isSimilarTo>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isSimilarTo>
            </xsl:for-each>
            
            <!--  edm:isSuccessorOf -->
            <xsl:for-each select="edm:isSuccessorOf">
              <edm:isSuccessorOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isSuccessorOf>
            </xsl:for-each>
            
            <!--  edm:realizes -->
            <xsl:for-each select="edm:realizes">
              <edm:realizes>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:realizes>
            </xsl:for-each>
            
            <!--  edm:type -->
            <xsl:for-each select="edm:type">
              <edm:type>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:type>
            </xsl:for-each>
            
             <!--  owl:sameAs -->
            <xsl:for-each select="owl:sameAs">
              <owl:sameAs>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </owl:sameAs>
            </xsl:for-each>
            
          </edm:ProvidedCHO>
        </xsl:for-each>

        <xsl:for-each select="edm:Collection">
          <edm:ProvidedCHO>
            <xsl:if test="@rdf:about">
              <xsl:attribute name="rdf:about">
                <xsl:for-each select="@rdf:about">
                  <xsl:if test="position() = 1">
                    <xsl:value-of select="."/>
                  </xsl:if>
                </xsl:for-each>
              </xsl:attribute>
            </xsl:if>
			
			<!-- dc:creator -->
            <xsl:for-each select="dc:creator">
              <dc:creator>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:creator>
            </xsl:for-each>

			<!-- dc:description -->
            <xsl:for-each select="dcterms:audience">
              <dc:description>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:description>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:description">
              <dc:description>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:description>
            </xsl:for-each>
			
			<!-- dc:format -->
			<xsl:for-each select="cld:itemsFormat">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>
			
			<!-- dc:identifier -->
            <xsl:for-each select="dc:identifier">
              <dc:identifier>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:identifier>
            </xsl:for-each>

			<!-- dc:language -->
            <xsl:for-each select="dc:language">
              <dc:language>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:language>
            </xsl:for-each>

			<!-- dc:relation -->
            <xsl:for-each select="dc:relation">
              <dc:relation>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:relation>
            </xsl:for-each>

			<!-- dc:rights -->
            <xsl:for-each select="dc:rights">
              <dc:rights>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:rights>
            </xsl:for-each>
			
			<!-- dc:subject -->
            <xsl:for-each select="dc:subject">
              <dc:subject>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:subject>
            </xsl:for-each>
            
            <xsl:for-each select="edm:itemGenre">
              <dc:subject>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:subject>
            </xsl:for-each>

			<!-- dc:title -->
            <xsl:for-each select="dc:title">
              <dc:title>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:title>
            </xsl:for-each>

			<!-- dc:type -->
			 <xsl:for-each select="cld:itemType">
              <dc:type>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:type>
            </xsl:for-each>
			
			<!-- dcterms:alternative -->
            <xsl:for-each select="dcterms:alternative">
              <dcterms:alternative>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:alternative>
            </xsl:for-each>
			
			<!-- dcterms:extent -->
            <xsl:for-each select="dcterms:accrualPeriodicity">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>

            <xsl:for-each select="dcterms:extent">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>

			<!-- dcterms:hasPart -->
            <xsl:for-each select="dcterms:hasPart">
              <dcterms:hasPart>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:hasPart>
            </xsl:for-each>

			<!-- dcterms:isPartOf -->
            <xsl:for-each select="dcterms:isPartOf">
              <dcterms:isPartOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isPartOf>
            </xsl:for-each>

			<!-- dcterms:isReferencedBy -->
            <xsl:for-each select="dcterms:isReferencedBy">
              <dcterms:isReferencedBy>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isReferencedBy>
            </xsl:for-each>

			<!-- dcterms:provenance -->
            <xsl:for-each select="dcterms:provenance">
              <dcterms:provenance>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:provenance>
            </xsl:for-each>

			<!-- dcterms:spatial -->
            <xsl:for-each select="dcterms:spatial">
              <dcterms:spatial>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:spatial>
            </xsl:for-each>

			<!-- dcterms:temporal -->
            <xsl:for-each select="dcterms:temporal">
              <dcterms:temporal>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:temporal>
            </xsl:for-each>
            
            <xsl:for-each select="cld:dateItemsCreated">
              <dcterms:temporal>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:temporal>
            </xsl:for-each>
            
            <!-- edm:isRelatedTo -->
            <xsl:for-each select="edm:isRelatedTo">
              <edm:isRelatedTo>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isRelatedTo>
            </xsl:for-each>
            
            <!-- edm:type -->
           
              <edm:type>
                 <xsl:value-of select="'SOUND'"/>
               </edm:type>
           
            
          </edm:ProvidedCHO>
        </xsl:for-each>
        
        <xsl:for-each select="ore:Aggregation">
          <ore:Aggregation>
            <xsl:if test="@rdf:about">
              <xsl:attribute name="rdf:about">
                <xsl:for-each select="@rdf:about">
                  <xsl:if test="position() = 1">
                    <xsl:value-of select="."/>
                  </xsl:if>
                </xsl:for-each>
              </xsl:attribute>
            </xsl:if>
            
            <xsl:for-each select="edm:aggregatedCHO">
              <edm:aggregatedCHO>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:aggregatedCHO>
            </xsl:for-each>
            
            <xsl:for-each select="edm:dataProvider">
              <edm:dataProvider>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:dataProvider >
            </xsl:for-each>
            
            <xsl:for-each select="edm:hasView">
              <edm:hasView>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:hasView>
            </xsl:for-each>
            
            <xsl:for-each select="edm:isShownAt">
              <edm:isShownAt>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isShownAt>
            </xsl:for-each>
            
            <xsl:for-each select="edm:isShownBy">
              <edm:isShownBy>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isShownBy>
            </xsl:for-each>
            
            <xsl:for-each select="edm:object">
              <edm:object>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:object>
            </xsl:for-each>
            
            <xsl:for-each select="edm:provider">
              <edm:provider>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:provider>
            </xsl:for-each>
            
            <xsl:for-each select="dc:rights">
              <dc:rights>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:rights>
            </xsl:for-each>
            
            <xsl:for-each select="edm:rights">
              <edm:rights>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:rights>
            </xsl:for-each>
            
            <xsl:for-each select="edm:ugc">
              <edm:ugc>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:ugc>
            </xsl:for-each>
            
            </ore:Aggregation>
        </xsl:for-each>
        
        <xsl:for-each select="edm:WebResource">
          <edm:WebResource>
            <xsl:if test="@rdf:about">
              <xsl:attribute name="rdf:about">
                <xsl:for-each select="@rdf:about">
                  <xsl:if test="position() = 1">
                    <xsl:value-of select="."/>
                  </xsl:if>
                </xsl:for-each>
              </xsl:attribute>
            </xsl:if>
            
            <xsl:for-each select="dc:creator">
              <dc:creator>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:creator>
            </xsl:for-each>
            
            <xsl:for-each select="dc:description">
              <dc:description>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:description>
            </xsl:for-each>

            <xsl:for-each select="dc:format">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>

            <xsl:for-each select="ebucore:audioTrackConfiguration">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>

            <xsl:for-each select="ebucore:bitRate">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>

            <xsl:for-each select="ebucore:fileSize">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>

            <xsl:for-each select="ebucore:hasAudioEncodingFormat">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>

            <xsl:for-each select="ebucore:hasMimeType">
              <dc:format>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:format>
            </xsl:for-each>

            <xsl:for-each select="dc:rights">
              <dc:rights>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:rights>
            </xsl:for-each>

            <xsl:for-each select="dcterms:dateCopyrighted">
              <dc:rights>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:rights>
            </xsl:for-each>

            <xsl:for-each select="dc:source">
              <dc:source>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:source>
            </xsl:for-each>

            <xsl:for-each select="dcterms:conformsTo">
              <dcterms:conformsTo>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:conformsTo>
            </xsl:for-each>

            <xsl:for-each select="dcterms:created">
              <dcterms:created>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:created>
            </xsl:for-each>

            <xsl:for-each select="ebucore:dateDigitised">
              <dcterms:created>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:created>
            </xsl:for-each>

            <xsl:for-each select="dcterms:extent">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>

            <xsl:for-each select="ebucore:duration">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>

            <xsl:for-each select="ebucore:audioChannelNumber">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>

            <xsl:for-each select="ebucore:sampleRate">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>

            <xsl:for-each select="ebucore:sampleSize">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>

            <xsl:for-each select="mo:record_side">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>

            <xsl:for-each select="mo:track_number">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>

            <xsl:for-each select="mo:track_count">
              <dcterms:extent>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:extent>
            </xsl:for-each>

            <xsl:for-each select="dcterms:hasPart">
              <dcterms:hasPart>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:hasPart>
            </xsl:for-each>

            <xsl:for-each select="dcterms:isFormatOf">
              <dcterms:isFormatOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isFormatOf>
            </xsl:for-each>

            <xsl:for-each select="dcterms:isPartOf">
              <dcterms:isPartOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isPartOf>
            </xsl:for-each>

            <xsl:for-each select="dcterms:issued">
              <dcterms:issued>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:issued>
            </xsl:for-each>

            <xsl:for-each select="edm:isNextInSequence">
              <edm:isNextInSequence>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isNextInSequence>
            </xsl:for-each>

            <xsl:for-each select="edm:rights">
              <edm:rights>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:rights>
            </xsl:for-each>

            <xsl:for-each select="mo:remaster_of">
            <xsl:if test="@rdf:resource">
              <edm:isDerivativeOf>
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
              </edm:isDerivativeOf>
              </xsl:if>
            </xsl:for-each>

            <xsl:for-each select="schema:version">
            <xsl:if test="@rdf:resource">
              <edm:isDerivativeOf>
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
              </edm:isDerivativeOf>
              </xsl:if>
            </xsl:for-each>

            <xsl:for-each select="owl:sameAs">
              <owl:sameAs>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </owl:sameAs>
            </xsl:for-each>

            </edm:WebResource>
        </xsl:for-each>
        
        <xsl:for-each select="edm:Agent">
          <edm:Agent>
            <xsl:if test="@rdf:about">
              <xsl:attribute name="rdf:about">
                <xsl:for-each select="@rdf:about">
                  <xsl:if test="position() = 1">
                    <xsl:value-of select="."/>
                  </xsl:if>
                </xsl:for-each>
              </xsl:attribute>
            </xsl:if>

            <xsl:for-each select="skos:prefLabel">
              <skos:prefLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:prefLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:altLabel">
              <skos:altLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:altLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:hiddenLabel">
              <skos:altLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:altLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:note">
              <skos:note>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:note>
            </xsl:for-each>
            
            <xsl:for-each select="dc:date">
              <dc:date>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:date>
            </xsl:for-each>
            
            <xsl:for-each select="dc:identifier">
              <dc:identifier>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:identifier>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:hasPart">
              <dcterms:hasPart>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:hasPart>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:isPartOf">
              <dcterms:isPartOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isPartOf>
            </xsl:for-each>
            
            <xsl:for-each select="edm:begin">
              <edm:begin>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:begin>
            </xsl:for-each>
            
            <xsl:for-each select="edm:end">
              <edm:end>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:end>
            </xsl:for-each>
            
            <xsl:for-each select="edm:hasMet">
              <edm:hasMet>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:hasMet>
            </xsl:for-each>
            
            <xsl:for-each select="edm:isRelatedTo">
              <edm:isRelatedTo>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isRelatedTo>
            </xsl:for-each>
            
            <xsl:for-each select="foaf:name">
              <foaf:name>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </foaf:name>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:biographicalInformation">
              <rdaGr2:biographicalInformation>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:biographicalInformation>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:dateOfBirth">
              <rdaGr2:dateOfBirth>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:dateOfBirth>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:dateOfDeath">
              <rdaGr2:dateOfDeath>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:dateOfDeath>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:dateOfEstablishment">
              <rdaGr2:dateOfEstablishment>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:dateOfEstablishment>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:dateOfTermination">
              <rdaGr2:dateOfTermination>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:dateOfTermination>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:gender">
              <rdaGr2:gender>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:gender>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:professionOrOccupation">
              <rdaGr2:professionOrOccupation>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:professionOrOccupation>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:placeOfBirth">
              <rdaGr2:placeofBirth>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:placeofBirth>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:placeOfDeath">
              <rdaGr2:placeofDeath>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:placeofDeath>
            </xsl:for-each>
            
            <xsl:for-each select="owl:sameAs">
              <owl:sameAs>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </owl:sameAs>
            </xsl:for-each>
            
            </edm:Agent>
        </xsl:for-each>
        
        <xsl:for-each select="edm:Place">
          <edm:Place>
            <xsl:if test="@rdf:about">
              <xsl:attribute name="rdf:about">
                <xsl:for-each select="@rdf:about">
                  <xsl:if test="position() = 1">
                    <xsl:value-of select="."/>
                  </xsl:if>
                </xsl:for-each>
              </xsl:attribute>
            </xsl:if>

            <xsl:for-each select="wgs84:lat">
              <wgs84:lat>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </wgs84:lat>
            </xsl:for-each>
            
            <xsl:for-each select="wgs84:long">
              <wgs84:long>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </wgs84:long>
            </xsl:for-each>
            
            <xsl:for-each select="wgs84:alt">
              <wgs84:alt>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </wgs84:alt>
            </xsl:for-each>
            
            <xsl:for-each select="skos:prefLabel">
              <skos:prefLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:prefLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:altLabel">
              <skos:altLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:altLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:hiddenLabel">
              <skos:altLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:altLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:note">
              <skos:note>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:note>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:hasPart">
              <dcterms:hasPart>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:hasPart>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:isPartOf">
              <dcterms:isPartOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isPartOf>
            </xsl:for-each>
            
            <xsl:for-each select="edm:isNextInSequence">
              <edm:isNextInSequence>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isNextInSequence>
            </xsl:for-each>
            
            <xsl:for-each select="owl:sameAs">
              <owl:sameAs>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </owl:sameAs>
            </xsl:for-each>
            
            </edm:Place>
        </xsl:for-each>
        
         <xsl:for-each select="edm:TimeSpan">
          <edm:TimeSpan>
            <xsl:if test="@rdf:about">
              <xsl:attribute name="rdf:about">
                <xsl:for-each select="@rdf:about">
                  <xsl:if test="position() = 1">
                    <xsl:value-of select="."/>
                  </xsl:if>
                </xsl:for-each>
              </xsl:attribute>
            </xsl:if>
            
            <xsl:for-each select="skos:prefLabel">
              <skos:prefLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:prefLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:altLabel">
              <skos:altLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:altLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:hiddenLabel">
              <skos:altLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:altLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:note">
              <skos:note>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:note>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:hasPart">
              <dcterms:hasPart>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:hasPart>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:isPartOf">
              <dcterms:isPartOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isPartOf>
            </xsl:for-each>
            
            <xsl:for-each select="edm:begin">
              <edm:begin>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:begin>
            </xsl:for-each>
            
            <xsl:for-each select="edm:end">
              <edm:end>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:end>
            </xsl:for-each>
            
            <xsl:for-each select="edm:isNextInSequence">
              <edm:isNextInSequence>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isNextInSequence>
            </xsl:for-each>
            
            <xsl:for-each select="owl:sameAs">
              <owl:sameAs>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </owl:sameAs>
            </xsl:for-each>
            
            </edm:TimeSpan>
        </xsl:for-each>
    
         <xsl:for-each select="skos:Concept">
          <skos:Concept>
            <xsl:if test="@rdf:about">
              <xsl:attribute name="rdf:about">
                <xsl:for-each select="@rdf:about">
                  <xsl:if test="position() = 1">
                    <xsl:value-of select="."/>
                  </xsl:if>
                </xsl:for-each>
              </xsl:attribute>
            </xsl:if>
            
            <xsl:for-each select="skos:prefLabel">
              <skos:prefLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:prefLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:altLabel">
              <skos:altLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:altLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:hiddenLabel">
              <skos:altLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:altLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:broader">
              <skos:broader>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:broader>
            </xsl:for-each>
            
            <xsl:for-each select="skos:narrower">
              <skos:narrower>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:narrower>
            </xsl:for-each>
            
            <xsl:for-each select="skos:related">
              <skos:related>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:related>
            </xsl:for-each>
            
            <xsl:for-each select="skos:broadMatch">
              <skos:broadMatch>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:broadMatch>
            </xsl:for-each>
            
            <xsl:for-each select="skos:narrowMatch">
              <skos:narrowMatch>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:narrowMatch>
            </xsl:for-each>
            
            <xsl:for-each select="skos:relatedMatch">
              <skos:relatedMatch>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:relatedMatch>
            </xsl:for-each>
            
            <xsl:for-each select="skos:exactMatch">
              <skos:exactMatch>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:exactMatch>
            </xsl:for-each>
            
            <xsl:for-each select="skos:closeMatch ">
              <skos:closeMatch >
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:closeMatch >
            </xsl:for-each>
            
            <xsl:for-each select="skos:note">
              <skos:note>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:note>
            </xsl:for-each>
            
            <xsl:for-each select="skos:notation">
              <skos:notation>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:notation>
            </xsl:for-each>
            
            <xsl:for-each select="skos:inScheme">
              <skos:inScheme>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:inScheme>
            </xsl:for-each>
            
            </skos:Concept>
        </xsl:for-each>
        
        <xsl:for-each select="mo:MusicGroup">
          <edm:Agent>
            <xsl:if test="@rdf:about">
              <xsl:attribute name="rdf:about">
                <xsl:for-each select="@rdf:about">
                  <xsl:if test="position() = 1">
                    <xsl:value-of select="."/>
                  </xsl:if>
                </xsl:for-each>
              </xsl:attribute>
            </xsl:if>
            
            <xsl:for-each select="skos:prefLabel">
              <skos:prefLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:prefLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:altLabel">
              <skos:altLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:altLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:hiddenLabel">
              <skos:altLabel>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:altLabel>
            </xsl:for-each>
            
            <xsl:for-each select="skos:note">
              <skos:note>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </skos:note>
            </xsl:for-each>
            
            <xsl:for-each select="dc:date">
              <dc:date>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:date>
            </xsl:for-each>
            
            <xsl:for-each select="dc:identifier">
              <dc:identifier>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dc:identifier>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:hasPart">
              <dcterms:hasPart>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:hasPart>
            </xsl:for-each>
            
            <xsl:for-each select="dcterms:isPartOf">
              <dcterms:isPartOf>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </dcterms:isPartOf>
            </xsl:for-each>
            
            <xsl:for-each select="edm:begin">
              <edm:begin>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:begin>
            </xsl:for-each>
            
            <xsl:for-each select="edm:end">
              <edm:end>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:end>
            </xsl:for-each>
            
            <xsl:for-each select="edm:hasMet">
              <edm:hasMet>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:hasMet>
            </xsl:for-each>
            
            <xsl:for-each select="edm:isRelatedTo">
              <edm:isRelatedTo>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:isRelatedTo>
            </xsl:for-each>
            
            <xsl:for-each select="foaf:name">
              <foaf:name>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </foaf:name>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:biographicalInformation">
              <rdaGr2:biographicalInformation>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:biographicalInformation>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:dateOfBirth">
              <rdaGr2:dateOfBirth>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:dateOfBirth>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:dateOfDeath">
              <rdaGr2:dateOfDeath>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:dateOfDeath>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:dateOfEstablishment">
              <rdaGr2:dateOfEstablishment>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:dateOfEstablishment>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:dateOfTermination">
              <rdaGr2:dateOfTermination>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:dateOfTermination>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:gender">
              <rdaGr2:gender>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:gender>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:professionOrOccupation">
              <rdaGr2:professionOrOccupation>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:professionOrOccupation>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:placeOfBirth">
              <rdaGr2:placeOfBirth>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:placeOfBirth>
            </xsl:for-each>
            
            <xsl:for-each select="rdaGr2:placeOfDeath">
              <rdaGr2:placeOfDeath>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </rdaGr2:placeOfDeath>
            </xsl:for-each>
            
            <xsl:for-each select="owl:sameAs">
              <owl:sameAs>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </owl:sameAs>
            </xsl:for-each>
            
            <xsl:for-each select="mo:collaborated_with">
              <edm:hasMet>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:hasMet>
            </xsl:for-each>
            
            <xsl:for-each select="mo:member_of">
              <edm:hasMet>
                 <xsl:if test="@xml:lang">
                  <xsl:attribute name="xml:lang">
                    <xsl:for-each select="@xml:lang">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                 <xsl:if test="@rdf:resource">
                  <xsl:attribute name="rdf:resource">
                    <xsl:for-each select="@rdf:resource">
                      <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
              </edm:hasMet>
            </xsl:for-each>
            
            </edm:Agent>
        </xsl:for-each>
    
    
      </rdf:RDF>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>  