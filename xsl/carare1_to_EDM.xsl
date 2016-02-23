<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:carare="http://www.carare.eu/carareSchema" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:edm="http://www.europeana.eu/schemas/edm/" xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:skos="http://www.w3.org/2004/02/skos/core#" xmlns:wgs84_pos="http://www.w3.org/2003/01/geo/wgs84_pos#" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0">
   <xsl:strip-space elements="*" />
   <xsl:output method="xml" version="1.0" encoding="utf-8" indent="yes" />
   <xsl:param name="uid" />
   <xsl:param name="rights" />
   <xsl:param name="provider_name" />
   <xsl:param name="item_id" />
   <xsl:template match="/">
      <xsl:apply-templates select="carare:carareWrap" />
   </xsl:template>
   <xsl:template match="carare:carareWrap">
      <xsl:variable name="lower">abcdefghijklmnopqrstuvwxyz</xsl:variable>
      <xsl:variable name="upper">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>
      <rdf:RDF>
         <!-- edm:rights -->
         <xsl:variable name="edm_rights">
            <xsl:if test="$rights = 1">http://creativecommons.org/publicdomain/mark/1.0/</xsl:if>
            <xsl:if test="$rights = 2">http://www.europeana.eu/rights/out-of-copyright-non-commercial/</xsl:if>
            <xsl:if test="$rights = 3">http://creativecommons.org/publicdomain/zero/1.0/</xsl:if>
            <xsl:if test="$rights = 4">http://creativecommons.org/licenses/by/4.0/</xsl:if>
            <xsl:if test="$rights = 5">http://creativecommons.org/licenses/by-sa/4.0/</xsl:if>
            <xsl:if test="$rights = 6">http://creativecommons.org/licenses/by-nd/4.0/</xsl:if>
            <xsl:if test="$rights = 7">http://creativecommons.org/licenses/by-nc/4.0/</xsl:if>
            <xsl:if test="$rights = 8">http://creativecommons.org/licenses/by-nc-sa/4.0/</xsl:if>
            <xsl:if test="$rights = 9">http://creativecommons.org/licenses/by-nc-nd/4.0/</xsl:if>
            <xsl:if test="$rights = 10">http://www.europeana.eu/rights/rr-f/</xsl:if>
            <xsl:if test="$rights = 11">http://www.europeana.eu/rights/rr-p/</xsl:if>
            <xsl:if test="$rights = 12">http://www.europeana.eu/rights/orphan-work-eu/</xsl:if>
            <xsl:if test="$rights = 13">http://www.europeana.eu/rights/unknown/</xsl:if>
         </xsl:variable>
         <xsl:for-each select="carare:carare/carare:heritageAssetIdentification">
            <!--  edm:ProvidedCHO  -->
            <xsl:element name="edm:ProvidedCHO">
               <xsl:attribute name="rdf:about">
                  http://more.locloud.eu/object/
                  <xsl:value-of select="$provider_name" />
                  /
                  <xsl:value-of select="$item_id" />
               </xsl:attribute>
               <!--  dc:date  -->
               <xsl:for-each select="carare:characters/carare:temporal/carare:timeSpan/carare:startDate">
                  <xsl:if test="text() !=''">
                     <xsl:element name="dc:date">
                        <xsl:value-of select="text()" />
                     </xsl:element>
                  </xsl:if>
               </xsl:for-each>
               <xsl:for-each select="carare:characters/carare:temporal/carare:timeSpan/carare:endDate">
                  <xsl:if test="text() !=''">
                     <xsl:element name="dc:date">
                        <xsl:value-of select="text()" />
                     </xsl:element>
                  </xsl:if>
               </xsl:for-each>
               <xsl:for-each select="carare:characters/carare:temporal/carare:displayDate">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:date">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="translate(@lang,$upper,$lower)" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:date">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <xsl:for-each select="carare:characters/carare:temporal/carare:scientificDate">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:date">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="translate(@lang,$upper,$lower)" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:date">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <xsl:for-each select="carare:characters/carare:craft/carare:lastJourneyDetails/carare:dateOfLoss">
                  <xsl:if test="text() !=''">
                     <xsl:element name="dc:date">
                        <xsl:value-of select="text()" />
                     </xsl:element>
                  </xsl:if>
               </xsl:for-each>
               <!--  dc:description  -->
               <xsl:for-each select="carare:description">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:description">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="translate(@lang,$upper,$lower)" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:description">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <xsl:for-each select="carare:characters/carare:craft/carare:lastJourneyDetails/carare:mannerOfLoss">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:description">
                              <xsl:attribute name="lang">
                                 <xsl:value-of select="@lang" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:description">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <xsl:for-each select="carare:characters/carare:craft/carare:lastJourneyDetails/carare:cargo">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:description">
                              <xsl:attribute name="lang">
                                 <xsl:value-of select="@lang" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:description">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--
dc:format
					<xsl:element name="dc:format">text</xsl:element>
				
-->
               <!--  dc:identifier  -->
               <xsl:for-each select="carare:appellation/carare:id">
                  <xsl:if test="text() !=''">
                     <xsl:element name="dc:identifier">
                        <xsl:value-of select="text()" />
                     </xsl:element>
                  </xsl:if>
               </xsl:for-each>
               <!--  dc:language  -->
               <xsl:for-each select="carare:recordInformation/carare:language">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:language">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="translate(@lang,$upper,$lower)" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:language">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <xsl:if test="not(carare:recordInformation/carare:language)">
                  <xsl:choose>
                     <xsl:when test="carare:recordInformation/carare:language">
                        <xsl:element name="dc:language">
                           <xsl:for-each select="carare:recordInformation/carare:language">
                              <xsl:value-of select="text()" />
                           </xsl:for-each>
                        </xsl:element>
                     </xsl:when>
                     <xsl:when test="../carare:collectionInformation/carare:language">
                        <xsl:element name="dc:language">
                           <xsl:for-each select="../carare:collectionInformation/carare:language">
                              <xsl:value-of select="text()" />
                           </xsl:for-each>
                        </xsl:element>
                     </xsl:when>
                     <xsl:when test="carare:recordInformation/carare:country">
                        <xsl:element name="dc:language">
                           <xsl:for-each select="carare:recordInformation/carare:country">
                              <xsl:value-of select="text()" />
                           </xsl:for-each>
                        </xsl:element>
                     </xsl:when>
                     <xsl:otherwise />
                  </xsl:choose>
               </xsl:if>
               <!--  dc:publisher  -->
               <xsl:for-each select="carare:recordInformation/carare:source">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:publisher">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="translate(@lang,$upper,$lower)" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:publisher">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--  dc:source  -->
               <xsl:for-each select="carare:recordInformation/carare:source">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="current()[starts-with(.,'http')]">
                           <xsl:element name="dc:source">
                              <xsl:attribute name="rdf:resource">
                                 <xsl:value-of select="text()" />
                              </xsl:attribute>
                           </xsl:element>
                        </xsl:when>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:source">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="translate(@lang,$upper,$lower)" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:source">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--  dc:subject  -->
               <xsl:for-each select="carare:characters/carare:heritageAssetType">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:subject">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="@lang" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:subject">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <xsl:for-each select="carare:characters/carare:craft/carare:constructionMethod">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:subject">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="@lang" />
                              </xsl:attribute>
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:subject">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <xsl:for-each select="carare:characters/carare:craft/carare:propulsion">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:subject">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="@lang" />
                              </xsl:attribute>
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:subject">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <xsl:for-each select="recordInformation/carare:keywords">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:subject">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="translate(@lang,$upper,$lower)" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:subject">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--   dc:title and dcterms:alternative  -->
               <xsl:variable name="countNames">
                  <xsl:value-of select="count(carare:appellation/carare:name)" />
               </xsl:variable>
               <xsl:for-each select="carare:appellation/carare:name">
                  <xsl:if test="$countNames=1">
                     <xsl:element name="dc:title">
                        <xsl:if test="@lang">
                           <xsl:attribute name="xml:lang">
                              <xsl:value-of select="translate(@lang,$upper,$lower)" />
                           </xsl:attribute>
                        </xsl:if>
                        <xsl:value-of select="text()" />
                     </xsl:element>
                  </xsl:if>
                  <xsl:if test="$countNames&gt;1">
                     <xsl:if test="@preferred">
                        <xsl:choose>
                           <xsl:when test="@preferred='true'">
                              <xsl:element name="dc:title">
                                 <xsl:if test="@lang">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                 </xsl:if>
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:when>
                           <xsl:otherwise>
                              <xsl:element name="dcterms:alternative">
                                 <xsl:if test="@lang">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                 </xsl:if>
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:otherwise>
                        </xsl:choose>
                     </xsl:if>
                     <xsl:if test="not(@preferred)">
                        <xsl:choose>
                           <xsl:when test="position()=1">
                              <xsl:element name="dc:title">
                                 <xsl:if test="@lang">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                 </xsl:if>
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:when>
                           <xsl:otherwise>
                              <xsl:element name="dcterms:alternative">
                                 <xsl:if test="@lang">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                 </xsl:if>
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:otherwise>
                        </xsl:choose>
                     </xsl:if>
                  </xsl:if>
               </xsl:for-each>
               <!--
dc:title, dcterms:alternative
				<xsl:for-each select="carare:appellation/carare:name">
					<xsl:if test='@preferred'>
						<xsl:choose>        
							<xsl:when test="@lang &gt; '0' and @preferred = 'true'">
								<xsl:element name="dc:title">
									<xsl:attribute name="xml:lang">
										<xsl:value-of select="translate(@lang,$upper,$lower)" />
									</xsl:attribute>
									<xsl:value-of select="text()" />
								</xsl:element>
							</xsl:when>
							<xsl:when test="@lang &gt; '0'">
								<xsl:element name="dcterms:alternative">
									<xsl:attribute name="xml:lang">
										<xsl:value-of select="translate(@lang,$upper,$lower)" />
									</xsl:attribute>
									<xsl:value-of select="text()" />
								</xsl:element>
							</xsl:when>
							<xsl:otherwise>
								<xsl:element name="dcterms:alternative">
									<xsl:value-of select="text()" />
								</xsl:element>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if> 
				</xsl:for-each>
				<xsl:for-each select="carare:appellation/carare:name">
					<xsl:if test='not(@preferred)'> 
						<xsl:element name="dc:title"> 
							<xsl:attribute name="xml:lang">
								<xsl:value-of select="translate(@lang,$upper,$lower)" />
							</xsl:attribute>
							<xsl:value-of select="text()" />
					   </xsl:element>
					</xsl:if>
				</xsl:for-each>
				<xsl:for-each select="carare:appellation/carare:name/following-sibling::carare:name[1]">
					<xsl:if test='not(@preferred)'>    
						<xsl:choose>      
							<xsl:when test="@lang &gt; '0'">
								<xsl:element name="dcterms:alternative"> 
									<xsl:attribute name="xml:lang">
										<xsl:value-of select="translate(@lang,$upper,$lower)" />
									</xsl:attribute><xsl:value-of select="text()" />
								</xsl:element>
							</xsl:when>
							<xsl:otherwise>
								<xsl:element name="dcterms:alternative"> 
									<xsl:value-of select="text()" />
								</xsl:element>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:for-each>
				
-->
               <!--  dc:type  -->
               <xsl:for-each select="../carare:digitalResource[1]/carare:type">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dc:type">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="@lang" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dc:type">
                              <xsl:choose>
                                 <xsl:when test="contains(text(), 'http')">
                                    <xsl:attribute name="rdf:resource">
                                       <xsl:value-of select="text()" />
                                    </xsl:attribute>
                                 </xsl:when>
                                 <xsl:otherwise>
                                    <xsl:value-of select="text()" />
                                 </xsl:otherwise>
                              </xsl:choose>
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--   dcterms:extent  -->
               <xsl:for-each select="carare:characters/carare:dimensions">
                  <xsl:for-each select="carare:extent">
                     <xsl:if test="text() !=''">
                        <xsl:choose>
                           <xsl:when test="@lang &gt; '0'">
                              <xsl:element name="dcterms:extent">
                                 <xsl:attribute name="xml:lang">
                                    <xsl:value-of select="@lang" />
                                 </xsl:attribute>
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:when>
                           <xsl:otherwise>
                              <xsl:element name="dcterms:extent">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:otherwise>
                        </xsl:choose>
                     </xsl:if>
                  </xsl:for-each>
                  <xsl:for-each select="carare:measurementType">
                     <xsl:if test="text() !=''">
                        <xsl:choose>
                           <xsl:when test="@lang &gt; '0'">
                              <xsl:element name="dcterms:extent">
                                 <xsl:attribute name="xml:lang">
                                    <xsl:value-of select="@lang" />
                                 </xsl:attribute>
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:when>
                           <xsl:otherwise>
                              <xsl:element name="dcterms:extent">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:otherwise>
                        </xsl:choose>
                     </xsl:if>
                  </xsl:for-each>
                  <xsl:for-each select="carare:units">
                     <xsl:if test="text() !=''">
                        <xsl:element name="dcterms:extent">
                           <xsl:value-of select="text()" />
                        </xsl:element>
                     </xsl:if>
                  </xsl:for-each>
                  <xsl:for-each select="carare:scale">
                     <xsl:if test="text() !=''">
                        <xsl:element name="dcterms:extent">
                           <xsl:value-of select="text()" />
                        </xsl:element>
                     </xsl:if>
                  </xsl:for-each>
                  <xsl:for-each select="carare:value">
                     <xsl:if test="text() !=''">
                        <xsl:element name="dcterms:extent">
                           <xsl:value-of select="text()" />
                        </xsl:element>
                     </xsl:if>
                  </xsl:for-each>
               </xsl:for-each>
               <!--  dcterms:hasPart and dcterms:isPartOf  -->
               <xsl:for-each select="carare:relations/carare:typeOfRelation">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="self::node()[text()='hasPart']">
                           <xsl:element name="dcterms:hasPart">
                              <xsl:for-each select="../carare:targetOfRelation">
                                 <xsl:value-of select="text()" />
                              </xsl:for-each>
                           </xsl:element>
                        </xsl:when>
                        <xsl:when test="self::node()[text()='isPartOf']">
                           <xsl:element name="dcterms:isPartOf">
                              <xsl:for-each select="../carare:targetOfRelation">
                                 <xsl:value-of select="text()" />
                              </xsl:for-each>
                           </xsl:element>
                        </xsl:when>
                        <xsl:when test="self::node()[text()='isDerivativeOf']" />
                        <xsl:otherwise />
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--  dcterms:medium  -->
               <xsl:for-each select="carare:characters/carare:materials">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dcterms:medium">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="translate(@lang,$upper,$lower)" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dcterms:medium">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--  dcterms:references  -->
               <xsl:for-each select="carare:references">
                  <xsl:if test="text() !=''">
                     <xsl:element name="dcterms:references">
                        <xsl:for-each select="carare:actors/carare:name">
                           <xsl:value-of select="text()" />
                        </xsl:for-each>
                        <xsl:for-each select="carare:appellation/carare:name">
                           "
                           <xsl:value-of select="text()" />
                           ",
                        </xsl:for-each>
                        <xsl:for-each select="carare:rights">
                           <xsl:value-of select="text()" />
                           ,
                        </xsl:for-each>
                        <xsl:for-each select="carare:publicationStatement/carare:publisher">
                           <xsl:value-of select="text()" />
                           ,
                        </xsl:for-each>
                        <xsl:for-each select="carare:publicationStatement/carare:placeOfPublication">
                           <xsl:value-of select="text()" />
                        </xsl:for-each>
                     </xsl:element>
                  </xsl:if>
               </xsl:for-each>
               <!--  dcterms:spatial  -->
               <xsl:for-each select="carare:spatial/carare:locationSet">
                  <xsl:element name="dcterms:spatial">
                     <xsl:variable name="i" select="position()" />
                     <xsl:attribute name="rdf:resource">
                        <xsl:value-of select="../../carare:recordInformation/carare:id" />
                        /SP.
                        <xsl:value-of select="$i" />
                        <!--
<xsl:value-of select="$uid" />/SP.<xsl:value-of select="$i" />
-->
                     </xsl:attribute>
                  </xsl:element>
               </xsl:for-each>
               <!--  dcterms:temporal  -->
               <xsl:for-each select="carare:characters/carare:temporal/carare:periodName">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="dcterms:temporal">
                              <xsl:attribute name="xml:lang">
                                 <xsl:value-of select="translate(@lang,$upper,$lower)" />
                              </xsl:attribute>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:element name="dcterms:temporal">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--  edm:currentLocation  -->
               <xsl:for-each select="carare:repositoryLocation">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="@lang &gt; '0'">
                           <xsl:element name="edm:currentLocation">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:value-of select="text()" />
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--
edm:type
					<xsl:element name="edm:type">TEXT</xsl:element>
-->
               <!--  edm:isDerivativeOf  -->
               <xsl:for-each select="carare:relations/carare:typeOfRelation">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="self::node()[text()='isDerivativeOf']">
                           <xsl:element name="edm:isDerivativeOf">
                              <xsl:for-each select="../carare:targetOfRelation">
                                 <xsl:value-of select="text()" />
                              </xsl:for-each>
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise />
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--  edm:isNextInSequence  -->
               <xsl:for-each select="carare:relations/carare:typeOfRelation">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="self::node()[text()='isNextInSequence']">
                           <xsl:element name="edm:isNextInSequence">
                              <xsl:for-each select="../carare:targetOfRelation">
                                 <xsl:value-of select="text()" />
                              </xsl:for-each>
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise />
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--  edm:isRelatedTo  -->
               <xsl:for-each select="carare:relations/carare:typeOfRelation">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="self::node()[text()='isRelatedTo']">
                           <xsl:element name="edm:isRelatedTo">
                              <xsl:attribute name="rdf:resource">
                                 <xsl:for-each select="../carare:targetOfRelation">
                                    <xsl:value-of select="text()" />
                                 </xsl:for-each>
                              </xsl:attribute>
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise />
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--  edm:isRepresentationOf  -->
               <xsl:for-each select="carare:relations/carare:typeOfRelation">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="self::node()[text()='isRepresentationOf']">
                           <xsl:element name="edm:isRepresentationOf">
                              <xsl:attribute name="rdf:resource">
                                 <xsl:for-each select="../carare:targetOfRelation">
                                    HA:
                                    <xsl:value-of select="text()" />
                                 </xsl:for-each>
                              </xsl:attribute>
                           </xsl:element>
                        </xsl:when>
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--  edm:isSuccessorOf  -->
               <xsl:for-each select="carare:relations/carare:typeOfRelation">
                  <xsl:if test="text() !=''">
                     <xsl:choose>
                        <xsl:when test="self::node()[text()='isSuccessorOf']">
                           <xsl:element name="edm:isSuccessorOf">
                              <xsl:for-each select="../carare:targetOfRelation">
                                 <xsl:value-of select="text()" />
                              </xsl:for-each>
                           </xsl:element>
                        </xsl:when>
                        <xsl:otherwise />
                     </xsl:choose>
                  </xsl:if>
               </xsl:for-each>
               <!--  edm:isRelatedTo  -->
               <xsl:for-each select="../carare:digitalResource">
                  <xsl:variable name="i" select="position()" />
                  <xsl:element name="edm:isRelatedTo">
                     <xsl:attribute name="rdf:resource">
                        DR:
                        <xsl:value-of select="carare:recordInformation/carare:id" />
                     </xsl:attribute>
                  </xsl:element>
               </xsl:for-each>
               <!--  edm:type  -->
               <xsl:for-each select="../carare:digitalResource[1]/carare:format">
                  <xsl:choose>
                     <xsl:when test="contains(text(), '3d')">
                        <xsl:element name="edm:type">3D</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), '3D')">
                        <xsl:element name="edm:type">3D</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'model')">
                        <xsl:element name="edm:type">3D</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'vrml')">
                        <xsl:element name="edm:type">3D</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'image')">
                        <xsl:element name="edm:type">IMAGE</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'jpg')">
                        <xsl:element name="edm:type">IMAGE</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'jpeg')">
                        <xsl:element name="edm:type">IMAGE</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'tiff')">
                        <xsl:element name="edm:type">IMAGE</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'img')">
                        <xsl:element name="edm:type">IMAGE</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'png')">
                        <xsl:element name="edm:type">IMAGE</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'gif')">
                        <xsl:element name="edm:type">IMAGE</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'svg')">
                        <xsl:element name="edm:type">IMAGE</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'text')">
                        <xsl:element name="edm:type">TEXT</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'html')">
                        <xsl:element name="edm:type">TEXT</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'xml')">
                        <xsl:element name="edm:type">TEXT</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'pdf')">
                        <xsl:element name="edm:type">TEXT</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'doc')">
                        <xsl:element name="edm:type">TEXT</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'video')">
                        <xsl:element name="edm:type">VIDEO</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'mpeg')">
                        <xsl:element name="edm:type">VIDEO</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'mp4')">
                        <xsl:element name="edm:type">VIDEO</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'ogg')">
                        <xsl:element name="edm:type">VIDEO</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'quicktime')">
                        <xsl:element name="edm:type">VIDEO</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'avi')">
                        <xsl:element name="edm:type">VIDEO</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'audio')">
                        <xsl:element name="edm:type">SOUND</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'wav')">
                        <xsl:element name="edm:type">SOUND</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'mp3')">
                        <xsl:element name="edm:type">SOUND</xsl:element>
                     </xsl:when>
                     <xsl:when test="contains(text(), 'flac')">
                        <xsl:element name="edm:type">SOUND</xsl:element>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:element name="edm:type">IMAGE</xsl:element>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:for-each>
            </xsl:element>
            <!--  edm:WebResource  -->
            <xsl:for-each select="../carare:digitalResource/carare:link">
               <xsl:element name="edm:WebResource">
                  <xsl:attribute name="rdf:about">
                     <xsl:value-of select="text()" />
                  </xsl:attribute>
                  <!--  dc:rights  -->
                  <xsl:for-each select="../carare:rights/carare:copyright/carare:rightsHolder">
                     <xsl:if test="text() !=''">
                        <xsl:choose>
                           <xsl:when test="@lang &gt; '0'">
                              <xsl:element name="dc:rights">
                                 <xsl:attribute name="xml:lang">
                                    <xsl:value-of select="@lang" />
                                 </xsl:attribute>
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:when>
                           <xsl:otherwise>
                              <xsl:element name="dc:rights">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:otherwise>
                        </xsl:choose>
                     </xsl:if>
                  </xsl:for-each>
                  <xsl:for-each select="../carare:rights/carare:copyright/carare:rightsDates">
                     <xsl:if test="text() !=''">
                        <xsl:element name="dc:rights">
                           <xsl:value-of select="text()" />
                        </xsl:element>
                     </xsl:if>
                  </xsl:for-each>
                  <xsl:for-each select="../carare:rights/carare:copyright/carare:creditLine">
                     <xsl:if test="text() !=''">
                        <xsl:choose>
                           <xsl:when test="@lang &gt; '0'">
                              <xsl:element name="dc:rights">
                                 <xsl:attribute name="xml:lang">
                                    <xsl:value-of select="@lang" />
                                 </xsl:attribute>
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:when>
                           <xsl:otherwise>
                              <xsl:element name="dc:rights">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:otherwise>
                        </xsl:choose>
                     </xsl:if>
                  </xsl:for-each>
                  <xsl:if test="not(../carare:rights/carare:copyright/carare:creditLine)">
                     <xsl:for-each select="../carare:rights/carare:licence">
                        <xsl:element name="dc:rights">
                           <xsl:value-of select="text()" />
                        </xsl:element>
                     </xsl:for-each>
                  </xsl:if>
               </xsl:element>
            </xsl:for-each>
            <xsl:if test="carare:spatial">
               <!--  edm:Place  -->
               <xsl:element name="edm:Place">
                  <xsl:variable name="i" select="position()" />
                  <xsl:attribute name="rdf:about">
                     <xsl:value-of select="carare:recordInformation/carare:id" />
                     /SP.
                     <xsl:value-of select="$i" />
                     <!--
<xsl:value-of select="$uid" />/SP.<xsl:value-of select="$i" />
-->
                  </xsl:attribute>
                  <!--  wgs84_pos:lat  -->
                  <xsl:for-each select="carare:spatial/carare:cartographicReference/carare:coordinates/carare:x">
                     <xsl:if test="text() !=''">
                        <xsl:element name="wgs84_pos:lat">
                           <xsl:value-of select="text()" />
                        </xsl:element>
                     </xsl:if>
                  </xsl:for-each>
                  <xsl:if test="not(carare:spatial/carare:cartographicReference/carare:coordinates/carare:x)">
                     <xsl:for-each select="carare:spatial/carare:geometry/carare:quickpoint/carare:x">
                        <xsl:element name="wgs84_pos:lat">
                           <xsl:value-of select="text()" />
                        </xsl:element>
                     </xsl:for-each>
                  </xsl:if>
                  <!--  wgs84_pos:long  -->
                  <xsl:for-each select="carare:spatial/carare:cartographicReference/carare:coordinates/carare:y">
                     <xsl:if test="text() !=''">
                        <xsl:element name="wgs84_pos:long">
                           <xsl:value-of select="text()" />
                        </xsl:element>
                     </xsl:if>
                  </xsl:for-each>
                  <xsl:if test="not(carare:spatial/carare:cartographicReference/carare:coordinates/carare:y)">
                     <xsl:for-each select="carare:spatial/carare:geometry/carare:quickpoint/carare:y">
                        <xsl:element name="wgs84_pos:long">
                           <xsl:value-of select="text()" />
                        </xsl:element>
                     </xsl:for-each>
                  </xsl:if>
                  <xsl:for-each select="carare:spatial/carare:locationSet">
                     <xsl:choose>
                        <!--  skos:prefLabel  -->
                        <xsl:when test="carare:namedLocation">
                           <xsl:if test="@preferred">
                              <xsl:for-each select="carare:namedLocation">
                                 <xsl:element name="skos:prefLabel">
                                    <xsl:if test="@lang">
                                       <xsl:attribute name="xml:lang">
                                          <xsl:value-of select="@lang" />
                                       </xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:for-each>
                           </xsl:if>
                           <xsl:if test="not(@preferred)">
                              <xsl:for-each select="carare:namedLocation">
                                 <xsl:element name="skos:prefLabel">
                                    <xsl:if test="@lang">
                                       <xsl:attribute name="xml:lang">
                                          <xsl:value-of select="@lang" />
                                       </xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="text()" />
                                    <xsl:if test="following-sibling::*">,</xsl:if>
                                 </xsl:element>
                              </xsl:for-each>
                           </xsl:if>
                        </xsl:when>
                        <xsl:when test="carare:cadastralReference">
                           <xsl:for-each select="carare:cadastralReference[1]">
                              <xsl:element name="skos:prefLabel">
                                 <xsl:if test="@lang">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="@lang" />
                                    </xsl:attribute>
                                 </xsl:if>
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:for-each>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:for-each select="carare:address">
                              <xsl:element name="skos:prefLabel">
                                 <xsl:for-each select="carare:roadName">
                                    <xsl:if test="@lang">
                                       <xsl:attribute name="xml:lang">
                                          <xsl:value-of select="@lang" />
                                       </xsl:attribute>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:buildingName">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:numberInRoad">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:roadName">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:townOrCity">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:postcodeOrZipcode">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:locality">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:adminArea">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:country">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                              </xsl:element>
                           </xsl:for-each>
                        </xsl:otherwise>
                     </xsl:choose>
                     <!--  skos:altLabel  -->
                     <xsl:for-each select="carare:cadastralReference">
                        <xsl:if test="../carare:namedLocation and text() !='' ">
                           <xsl:element name="skos:altLabel">
                              <xsl:if test="@lang">
                                 <xsl:attribute name="xml:lang">
                                    <xsl:value-of select="@lang" />
                                 </xsl:attribute>
                              </xsl:if>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:geopoliticalArea/geopoliticalAreaName">
                        <xsl:if test="text() !=''">
                           <xsl:element name="skos:altLabel">
                              <xsl:if test="@lang">
                                 <xsl:attribute name="xml:lang">
                                    <xsl:value-of select="@lang" />
                                 </xsl:attribute>
                              </xsl:if>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:historicalName">
                        <xsl:if test="text() !=''">
                           <xsl:element name="skos:altLabel">
                              <xsl:if test="@lang">
                                 <xsl:attribute name="xml:lang">
                                    <xsl:value-of select="@lang" />
                                 </xsl:attribute>
                              </xsl:if>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  skos:note  -->
                     <xsl:for-each select="carare:address">
                        <xsl:element name="skos:note">
                           <xsl:for-each select="carare:roadName">
                              <xsl:if test="@lang">
                                 <xsl:attribute name="xml:lang">
                                    <xsl:value-of select="@lang" />
                                 </xsl:attribute>
                              </xsl:if>
                           </xsl:for-each>
                           <xsl:for-each select="carare:buildingName">
                              <xsl:if test="text() !=''">
                                 <xsl:value-of select="text()" />
                                 <xsl:if test="following-sibling::*">,</xsl:if>
                              </xsl:if>
                           </xsl:for-each>
                           <xsl:for-each select="carare:numberInRoad">
                              <xsl:if test="text() !=''">
                                 <xsl:value-of select="text()" />
                                 <xsl:if test="following-sibling::*">,</xsl:if>
                              </xsl:if>
                           </xsl:for-each>
                           <xsl:for-each select="carare:roadName">
                              <xsl:if test="text() !=''">
                                 <xsl:value-of select="text()" />
                                 <xsl:if test="following-sibling::*">,</xsl:if>
                              </xsl:if>
                           </xsl:for-each>
                           <xsl:for-each select="carare:townOrCity">
                              <xsl:if test="text() !=''">
                                 <xsl:value-of select="text()" />
                                 <xsl:if test="following-sibling::*">,</xsl:if>
                              </xsl:if>
                           </xsl:for-each>
                           <xsl:for-each select="carare:postcodeOrZipcode">
                              <xsl:if test="text() !=''">
                                 <xsl:value-of select="text()" />
                                 <xsl:if test="following-sibling::*">,</xsl:if>
                              </xsl:if>
                           </xsl:for-each>
                           <xsl:for-each select="carare:locality">
                              <xsl:if test="text() !=''">
                                 <xsl:value-of select="text()" />
                                 <xsl:if test="following-sibling::*">,</xsl:if>
                              </xsl:if>
                           </xsl:for-each>
                           <xsl:for-each select="carare:adminArea">
                              <xsl:if test="text() !=''">
                                 <xsl:value-of select="text()" />
                                 <xsl:if test="following-sibling::*">,</xsl:if>
                              </xsl:if>
                           </xsl:for-each>
                           <xsl:for-each select="carare:country">
                              <xsl:if test="text() !=''">
                                 <xsl:value-of select="text()" />
                                 <xsl:if test="following-sibling::*">,</xsl:if>
                              </xsl:if>
                           </xsl:for-each>
                        </xsl:element>
                     </xsl:for-each>
                  </xsl:for-each>
               </xsl:element>
            </xsl:if>
         </xsl:for-each>
         <xsl:for-each select="carare:carare/carare:heritageAssetIdentification">
            <!--  ore:Aggregation  -->
            <xsl:for-each select="carare:recordInformation/carare:id">
               <xsl:element name="ore:Aggregation">
                  <xsl:attribute name="rdf:about">
                     http://more.locloud.eu/object/
                     <xsl:value-of select="$provider_name" />
                     /
                     <xsl:value-of select="$item_id" />
                     #aggregation
                  </xsl:attribute>
                  <!--
<xsl:attribute name="rdf:about">http://store.carare.eu/uid/<xsl:value-of select="$uid" />/HA:<xsl:value-of select="text()" /> </xsl:attribute>
-->
                  <!--  edm:aggregatedCHO  -->
                  <xsl:element name="edm:aggregatedCHO">
                     <xsl:attribute name="rdf:about">
                        http://more.locloud.eu/object/
                        <xsl:value-of select="$provider_name" />
                        /
                        <xsl:value-of select="$item_id" />
                     </xsl:attribute>
                     <!--
<xsl:attribute name="rdf:resource">HA:<xsl:value-of select="text()" /> </xsl:attribute>
-->
                  </xsl:element>
                  <!--  edm:dataProvider  -->
                  <xsl:for-each select="../../carare:recordInformation/carare:source">
                     <xsl:if test="text() !=''">
                        <xsl:element name="edm:dataProvider">
                           <xsl:value-of select="text()" />
                        </xsl:element>
                     </xsl:if>
                  </xsl:for-each>
                  <!--  edm:hasView  -->
                  <xsl:choose>
                     <xsl:when test="../../../carare:digitalResource[2]/carare:link">
                        <xsl:for-each select="../../../carare:digitalResource/carare:link">
                           <xsl:if test="position()&gt;1">
                              <xsl:element name="edm:hasView">
                                 <xsl:attribute name="rdf:resource">
                                    <xsl:value-of select="text()" />
                                 </xsl:attribute>
                              </xsl:element>
                           </xsl:if>
                        </xsl:for-each>
                     </xsl:when>
                     <xsl:otherwise />
                  </xsl:choose>
                  <!--  edm:isShownAt  -->
                  <xsl:for-each select="../../../carare:digitalResource[1]/carare:isShownAt">
                     <xsl:if test="text() !=''">
                        <xsl:element name="edm:isShownAt">
                           <xsl:attribute name="rdf:resource">
                              <xsl:value-of select="text()" />
                           </xsl:attribute>
                        </xsl:element>
                     </xsl:if>
                  </xsl:for-each>
                  <!--
isShownAt-> store.carare.eu
						<xsl:element name="edm:isShownAt">
							<xsl:attribute name="rdf:resource">http://store.carare.eu/uid/<xsl:value-of select="$uid" />/HA:<xsl:value-of select="text()" />
							</xsl:attribute>
						</xsl:element>
-->
                  <!--  edm:isShownBy  -->
                  <xsl:for-each select="carare:relations/carare:typeOfRelation">
                     <xsl:choose>
                        <xsl:when test="contains(text(),'isShownBy')">
                           <xsl:element name="edm:isShownBy">
                              <xsl:for-each select="../carare:targetOfRelation">
                                 <xsl:attribute name="rdf:resource">
                                    <xsl:value-of select="text()" />
                                 </xsl:attribute>
                              </xsl:for-each>
                           </xsl:element>
                        </xsl:when>
                     </xsl:choose>
                  </xsl:for-each>
                  <xsl:for-each select="../../../carare:digitalResource[1]/carare:link">
                     <xsl:if test="text() !=''">
                        <xsl:element name="edm:isShownBy">
                           <xsl:attribute name="rdf:resource">
                              <xsl:value-of select="text()" />
                           </xsl:attribute>
                        </xsl:element>
                     </xsl:if>
                  </xsl:for-each>
                  <!--  edm:object  -->
                  <xsl:for-each select="../../../carare:digitalResource[1]/carare:object">
                     <xsl:if test="text() !=''">
                        <xsl:element name="edm:object">
                           <xsl:attribute name="rdf:resource">
                              <xsl:value-of select="text()" />
                           </xsl:attribute>
                        </xsl:element>
                     </xsl:if>
                  </xsl:for-each>
                  <!--  edm:provider  -->
                  <xsl:element name="edm:provider">CARARE</xsl:element>
                  <!--
<xsl:element name="dc:rights">http://creativecommons.org/licenses/by-sa/3.0/</xsl:element>
-->
                  <!--  dc:rights  -->
                  <xsl:for-each select="../../carare:rights/carare:copyright/carare:creditLine">
                     <xsl:if test="text() !=''">
                        <xsl:choose>
                           <xsl:when test="@lang &gt; '0'">
                              <xsl:element name="dc:rights">
                                 <xsl:attribute name="xml:lang">
                                    <xsl:value-of select="@lang" />
                                 </xsl:attribute>
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:when>
                           <xsl:otherwise>
                              <xsl:element name="dc:rights">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:otherwise>
                        </xsl:choose>
                     </xsl:if>
                  </xsl:for-each>
                  <!--
dc:rights
						<xsl:if test="not(../carare:rights/carare:copyright/carare:creditLine)">
							<xsl:for-each select="../carare:rights/carare:licence">   
								<xsl:element name="dc:rights">
									<xsl:value-of select="text()" />
								</xsl:element>
							</xsl:for-each>	
						</xsl:if>
-->
                  <!--  edm:rights  -->
                  <xsl:element name="edm:rights">
                     <xsl:attribute name="rdf:resource">
                        <xsl:value-of select="$edm_rights" />
                     </xsl:attribute>
                  </xsl:element>
               </xsl:element>
            </xsl:for-each>
         </xsl:for-each>
         <!-- digitalResource -->
         <xsl:if test="not(carare:carare/carare:heritageAssetIdentification)">
            <xsl:for-each select="carare:carare/carare:digitalResource">
               <xsl:if test=". !='' or @*[. !='']">
                  <xsl:variable name="i" select="position()" />
                  <!--  edm:ProvidedCHO  -->
                  <xsl:element name="edm:ProvidedCHO">
                     <xsl:attribute name="rdf:about">
                        http://more.locloud.eu/object/
                        <xsl:value-of select="$provider_name" />
                        /
                        <xsl:value-of select="$item_id" />
                     </xsl:attribute>
                     <xsl:value-of select="text()" />
                     <!--  dc:creator  -->
                     <xsl:for-each select="carare:actors/carare:name">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:creator">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:creator">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dc:date  -->
                     <xsl:for-each select="carare:temporal/carare:timeSpan/carare:startDate">
                        <xsl:if test="text() !=''">
                           <xsl:element name="dc:date">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:temporal/carare:timeSpan/carare:endDate">
                        <xsl:if test="text() !=''">
                           <xsl:element name="dc:date">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:temporal/carare:displayDate">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:date">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:date">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:temporal/carare:scientificDate">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:date">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:date">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dc:description  -->
                     <xsl:for-each select="carare:description">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:description">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:description">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:note">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:description">
                                    <xsl:attribute name="lang">
                                       <xsl:value-of select="@lang" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:description">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dc:format  -->
                     <xsl:for-each select="carare:format">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:format">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:format">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dc:identifier  -->
                     <xsl:for-each select="carare:appellation/carare:id">
                        <xsl:if test="text() !=''">
                           <xsl:element name="dc:identifier">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dc:language  -->
                     <xsl:for-each select="carare:language">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:language">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:language">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:recordInformation/carare:source">
                        <xsl:if test="contains(text(), 'Scuola Normale Superiore')">
                           <xsl:for-each select="../carare:language">
                              <xsl:choose>
                                 <xsl:when test="not(text())">
                                    <xsl:element name="dc:language">
                                       <xsl:value-of select="@lang" />
                                    </xsl:element>
                                 </xsl:when>
                                 <xsl:otherwise />
                              </xsl:choose>
                           </xsl:for-each>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dc:publisher  -->
                     <xsl:for-each select="carare:publicationStatement/carare:publisher">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:publisher">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:publisher">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:publicationStatement/carare:placeOfPublication">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:publisher">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:publisher">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:publicationStatement/carare:date">
                        <xsl:if test="text() !=''">
                           <xsl:element name="dc:publisher">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dc:rights  -->
                     <xsl:for-each select="carare:rights/carare:copyright/carare:creditLine">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:rights">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="@lang" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:rights">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:if test="not(carare:rights/carare:copyright/carare:creditLine)">
                        <xsl:for-each select="carare:rights/carare:licence">
                           <xsl:element name="dc:rights">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:for-each>
                     </xsl:if>
                     <!--  dc:source  -->
                     <xsl:for-each select="carare:recordInformation/carare:source">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:source">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:source">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dc:subject  -->
                     <xsl:for-each select="carare:subject">
                        <xsl:if test="text() !=''">
                           <xsl:element name="dc:subject">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:recordInformation/carare:keywords">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:subject">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:subject">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dc:title  -->
                     <xsl:variable name="countNames">
                        <xsl:value-of select="count(carare:appellation/carare:name)" />
                     </xsl:variable>
                     <xsl:for-each select="carare:appellation/carare:name">
                        <xsl:if test="$countNames=1">
                           <xsl:element name="dc:title">
                              <xsl:if test="@lang">
                                 <xsl:attribute name="xml:lang">
                                    <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                 </xsl:attribute>
                              </xsl:if>
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:if>
                        <xsl:if test="$countNames&gt;1">
                           <xsl:if test="@preferred">
                              <xsl:choose>
                                 <xsl:when test="@preferred='true'">
                                    <xsl:element name="dc:title">
                                       <xsl:if test="@lang">
                                          <xsl:attribute name="xml:lang">
                                             <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                          </xsl:attribute>
                                       </xsl:if>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:when>
                                 <xsl:otherwise>
                                    <xsl:element name="dcterms:alternative">
                                       <xsl:if test="@lang">
                                          <xsl:attribute name="xml:lang">
                                             <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                          </xsl:attribute>
                                       </xsl:if>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:otherwise>
                              </xsl:choose>
                           </xsl:if>
                           <xsl:if test="not(@preferred)">
                              <xsl:choose>
                                 <xsl:when test="position()=1">
                                    <xsl:element name="dc:title">
                                       <xsl:if test="@lang">
                                          <xsl:attribute name="xml:lang">
                                             <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                          </xsl:attribute>
                                       </xsl:if>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:when>
                                 <xsl:otherwise>
                                    <xsl:element name="dcterms:alternative">
                                       <xsl:if test="@lang">
                                          <xsl:attribute name="xml:lang">
                                             <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                          </xsl:attribute>
                                       </xsl:if>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:otherwise>
                              </xsl:choose>
                           </xsl:if>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dcterms:alternative  -->
                     <xsl:for-each select="carare:appellation/carare:name[1]">
                        <xsl:if test="not(@preferred)">
                           <xsl:choose>
                              <xsl:when test="node()">
                                 <xsl:element name="dc:title">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise />
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="carare:appellation/carare:name/following-sibling::carare:name[1]">
                        <xsl:if test="not(@preferred)">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dcterms:alternative">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dcterms:alternative">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dc::type  -->
                     <xsl:for-each select="carare:type">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dc:type">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="@lang" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dc:type">
                                    <xsl:choose>
                                       <xsl:when test="contains(text(), 'http')">
                                          <xsl:attribute name="rdf:resource">
                                             <xsl:value-of select="text()" />
                                          </xsl:attribute>
                                       </xsl:when>
                                       <xsl:otherwise>
                                          <xsl:value-of select="text()" />
                                       </xsl:otherwise>
                                    </xsl:choose>
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dcterms:created  -->
                     <xsl:for-each select="carare:created">
                        <xsl:if test="text() !=''">
                           <xsl:element name="dcterms:created">
                              <xsl:value-of select="@lang" />
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dcterms:extent  -->
                     <xsl:for-each select="carare:extent">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dcterms:extent">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="@lang" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dcterms:extent">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dcterms:hasPart and dcterms:isPartOf  -->
                     <xsl:for-each select="carare:relations/carare:typeOfRelation">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="self::node()[text()='hasPart']">
                                 <xsl:element name="dcterms:hasPart">
                                    <xsl:for-each select="../carare:targetOfRelation">
                                       <xsl:value-of select="text()" />
                                    </xsl:for-each>
                                 </xsl:element>
                              </xsl:when>
                              <xsl:when test="self::node()[text()='isPartOf']">
                                 <xsl:element name="dcterms:isPartOf">
                                    <xsl:for-each select="../carare:targetOfRelation">
                                       <xsl:value-of select="text()" />
                                    </xsl:for-each>
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise />
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dcterms:medium  -->
                     <xsl:for-each select="carare:medium">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dcterms:medium">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <dcterms:medium>
                                    <xsl:value-of select="text()" />
                                 </dcterms:medium>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dcterms:provenance  -->
                     <xsl:for-each select="carare:provenance">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dcterms:provenance">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dcterms:provenance">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  dcterms:spatial  -->
                     <xsl:for-each select="carare:spatial/carare:locationSet">
                        <xsl:element name="dcterms:spatial">
                           <xsl:variable name="i" select="position()" />
                           <xsl:attribute name="rdf:resource">
                              <xsl:value-of select="../../carare:recordInformation/carare:id" />
                              /SP.
                              <xsl:value-of select="$i" />
                              <!--
<xsl:value-of select="$uid" />/SP.<xsl:value-of select="$i" />
-->
                           </xsl:attribute>
                        </xsl:element>
                     </xsl:for-each>
                     <!--   dcterms:temporal   -->
                     <xsl:for-each select="carare:temporal/carare:periodName">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="@lang &gt; '0'">
                                 <xsl:element name="dcterms:temporal">
                                    <xsl:attribute name="xml:lang">
                                       <xsl:value-of select="translate(@lang,$upper,$lower)" />
                                    </xsl:attribute>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:element name="dcterms:temporal">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  edm:type  -->
                     <xsl:for-each select="carare:format[1]">
                        <xsl:choose>
                           <xsl:when test="contains(text(), '3d')">
                              <xsl:element name="edm:type">3D</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), '3D')">
                              <xsl:element name="edm:type">3D</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'model')">
                              <xsl:element name="edm:type">3D</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'vrml')">
                              <xsl:element name="edm:type">3D</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'image')">
                              <xsl:element name="edm:type">IMAGE</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'jpg')">
                              <xsl:element name="edm:type">IMAGE</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'jpeg')">
                              <xsl:element name="edm:type">IMAGE</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'tiff')">
                              <xsl:element name="edm:type">IMAGE</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'img')">
                              <xsl:element name="edm:type">IMAGE</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'png')">
                              <xsl:element name="edm:type">IMAGE</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'gif')">
                              <xsl:element name="edm:type">IMAGE</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'svg')">
                              <xsl:element name="edm:type">IMAGE</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'text')">
                              <xsl:element name="edm:type">TEXT</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'html')">
                              <xsl:element name="edm:type">TEXT</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'xml')">
                              <xsl:element name="edm:type">TEXT</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'pdf')">
                              <xsl:element name="edm:type">TEXT</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'doc')">
                              <xsl:element name="edm:type">TEXT</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'video')">
                              <xsl:element name="edm:type">VIDEO</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'mpeg')">
                              <xsl:element name="edm:type">VIDEO</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'mp4')">
                              <xsl:element name="edm:type">VIDEO</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'ogg')">
                              <xsl:element name="edm:type">VIDEO</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'quicktime')">
                              <xsl:element name="edm:type">VIDEO</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'avi')">
                              <xsl:element name="edm:type">VIDEO</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'audio')">
                              <xsl:element name="edm:type">SOUND</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'wav')">
                              <xsl:element name="edm:type">SOUND</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'mp3')">
                              <xsl:element name="edm:type">SOUND</xsl:element>
                           </xsl:when>
                           <xsl:when test="contains(text(), 'flac')">
                              <xsl:element name="edm:type">SOUND</xsl:element>
                           </xsl:when>
                           <xsl:otherwise>
                              <xsl:element name="edm:type">IMAGE</xsl:element>
                           </xsl:otherwise>
                        </xsl:choose>
                     </xsl:for-each>
                     <!--  edm:isDerivativeOf  -->
                     <xsl:for-each select="carare:relations/carare:typeOfRelation">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="self::node()[text()='isDerivativeOf']">
                                 <xsl:element name="edm:isDerivativeOf">
                                    <xsl:for-each select="../carare:targetOfRelation">
                                       <xsl:value-of select="text()" />
                                    </xsl:for-each>
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise />
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  edm:isNextInSequence  -->
                     <xsl:for-each select="carare:relations/carare:typeOfRelation">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="self::node()[text()='isNextInSequence']">
                                 <xsl:element name="edm:isNextInSequence">
                                    <xsl:for-each select="../carare:targetOfRelation">
                                       <xsl:value-of select="text()" />
                                    </xsl:for-each>
                                 </xsl:element>
                              </xsl:when>
                              <xsl:otherwise />
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  edm:isRelatedTo  -->
                     <xsl:for-each select="carare:relations/carare:typeOfRelation">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="self::node()[text()='isRelatedTo']">
                                 <xsl:element name="edm:isRelatedTo">
                                    <xsl:attribute name="rdf:resource">
                                       <xsl:for-each select="../carare:targetOfRelation">
                                          <xsl:value-of select="text()" />
                                       </xsl:for-each>
                                    </xsl:attribute>
                                 </xsl:element>
                              </xsl:when>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  edm:isRepresentationOf  -->
                     <xsl:for-each select="carare:relations/carare:typeOfRelation">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="self::node()[text()='isRepresentationOf']">
                                 <xsl:element name="edm:isRepresentationOf">
                                    <xsl:attribute name="rdf:resource">
                                       <xsl:for-each select="../carare:targetOfRelation">
                                          HA:
                                          <xsl:value-of select="text()" />
                                       </xsl:for-each>
                                    </xsl:attribute>
                                 </xsl:element>
                              </xsl:when>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  edm:isSuccessorOf  -->
                     <xsl:for-each select="carare:relations/carare:typeOfRelation">
                        <xsl:if test="text() !=''">
                           <xsl:choose>
                              <xsl:when test="self::node()[text()='isSuccessorOf']">
                                 <xsl:element name="edm:isSuccessorOf">
                                    <xsl:for-each select="../carare:targetOfRelation">
                                       <xsl:value-of select="text()" />
                                    </xsl:for-each>
                                 </xsl:element>
                              </xsl:when>
                           </xsl:choose>
                        </xsl:if>
                     </xsl:for-each>
                     <!--
isRelatedTo
									<xsl:for-each select="../carare:heritageAssetIdentification"> 
										<xsl:variable name="i" select="position()" />
										<xsl:element name="edm:isRelatedTo">
											<xsl:attribute name="rdf:resource">HA:<xsl:value-of select="carare:recordInformation/carare:id" />
											</xsl:attribute>
										</xsl:element>
									</xsl:for-each> 
-->
                  </xsl:element>
                  <!--  edm:WebResource  -->
                  <xsl:for-each select="carare:link">
                     <xsl:element name="edm:WebResource">
                        <xsl:attribute name="rdf:about">
                           <xsl:value-of select="text()" />
                        </xsl:attribute>
                        <!--  dc::rights  -->
                        <xsl:for-each select="../carare:rights/carare:copyright/carare:rightsHolder">
                           <xsl:if test="text() !=''">
                              <xsl:choose>
                                 <xsl:when test="@lang &gt; '0'">
                                    <xsl:element name="dc:rights">
                                       <xsl:attribute name="xml:lang">
                                          <xsl:value-of select="@lang" />
                                       </xsl:attribute>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:when>
                                 <xsl:otherwise>
                                    <xsl:element name="dc:rights">
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:otherwise>
                              </xsl:choose>
                           </xsl:if>
                        </xsl:for-each>
                        <xsl:for-each select="../carare:rights/carare:copyright/carare:rightsDates">
                           <xsl:if test="text() !=''">
                              <xsl:element name="dc:rights">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:if>
                        </xsl:for-each>
                        <xsl:for-each select="../carare:rights/carare:copyright/carare:creditLine">
                           <xsl:if test="text() !=''">
                              <xsl:choose>
                                 <xsl:when test="@lang &gt; '0'">
                                    <xsl:element name="dc:rights">
                                       <xsl:attribute name="xml:lang">
                                          <xsl:value-of select="@lang" />
                                       </xsl:attribute>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:when>
                                 <xsl:otherwise>
                                    <xsl:element name="dc:rights">
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:otherwise>
                              </xsl:choose>
                           </xsl:if>
                        </xsl:for-each>
                        <xsl:if test="not(../carare:rights/carare:copyright/carare:creditLine)">
                           <xsl:for-each select="../carare:rights/carare:licence">
                              <xsl:element name="dc:rights">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:for-each>
                        </xsl:if>
                     </xsl:element>
                  </xsl:for-each>
                  <!--  edm:Place  -->
                  <xsl:if test="carare:spatial">
                     <xsl:element name="edm:Place">
                        <xsl:variable name="i" select="position()" />
                        <!--
<xsl:variable name="j">
									<xsl:if test="../../carare:heritageAssetIdentification"><xsl:value-of select="$i+1" /></xsl:if>
									<xsl:if test="not(../../carare:heritageAssetIdentification)"><xsl:value-of select="$i" /></xsl:if>
								</xsl:variable>
-->
                        <xsl:attribute name="rdf:about">
                           <xsl:value-of select="carare:recordInformation/carare:id" />
                           /SP.
                           <xsl:value-of select="$i" />
                           <!--
<xsl:value-of select="$uid" />/SP.<xsl:value-of select="$j" />
-->
                        </xsl:attribute>
                        <!--  wgs84_pos:lat  -->
                        <xsl:for-each select="carare:spatial/carare:cartographicReference/carare:coordinates/carare:x">
                           <xsl:if test="text() !=''">
                              <xsl:element name="wgs84_pos:lat">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:if>
                        </xsl:for-each>
                        <xsl:if test="not(carare:spatial/carare:cartographicReference/carare:coordinates/carare:x)">
                           <xsl:for-each select="carare:spatial/carare:geometry/carare:quickpoint/carare:x">
                              <xsl:if test="text() !=''">
                                 <xsl:element name="wgs84_pos:lat">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:if>
                           </xsl:for-each>
                        </xsl:if>
                        <!--  wgs84_pos:long  -->
                        <xsl:for-each select="carare:spatial/carare:cartographicReference/carare:coordinates/carare:y">
                           <xsl:if test="text() !=''">
                              <xsl:element name="wgs84_pos:long">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:if>
                        </xsl:for-each>
                        <xsl:if test="not(carare:spatial/carare:cartographicReference/carare:coordinates/carare:y)">
                           <xsl:for-each select="carare:spatial/carare:geometry/carare:quickpoint/carare:y">
                              <xsl:if test="text() !=''">
                                 <xsl:element name="wgs84_pos:long">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:if>
                           </xsl:for-each>
                        </xsl:if>
                        <xsl:for-each select="carare:spatial/carare:locationSet">
                           <!--  skos:prefLabel  -->
                           <xsl:choose>
                              <xsl:when test="carare:namedLocation">
                                 <xsl:for-each select="carare:namedLocation">
                                    <xsl:element name="skos:prefLabel">
                                       <xsl:if test="@lang">
                                          <xsl:attribute name="xml:lang">
                                             <xsl:value-of select="@lang" />
                                          </xsl:attribute>
                                       </xsl:if>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:for-each>
                              </xsl:when>
                              <xsl:when test="carare:cadastralReference">
                                 <xsl:for-each select="carare:cadastralReference[1]">
                                    <xsl:element name="skos:prefLabel">
                                       <xsl:if test="@lang">
                                          <xsl:attribute name="xml:lang">
                                             <xsl:value-of select="@lang" />
                                          </xsl:attribute>
                                       </xsl:if>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:for-each>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:for-each select="carare:address">
                                    <xsl:element name="skos:prefLabel">
                                       <xsl:for-each select="carare:roadName">
                                          <xsl:if test="@lang">
                                             <xsl:attribute name="xml:lang">
                                                <xsl:value-of select="@lang" />
                                             </xsl:attribute>
                                          </xsl:if>
                                       </xsl:for-each>
                                       <xsl:for-each select="carare:buildingName">
                                          <xsl:if test="text() !=''">
                                             <xsl:value-of select="text()" />
                                             <xsl:if test="following-sibling::*">,</xsl:if>
                                          </xsl:if>
                                       </xsl:for-each>
                                       <xsl:for-each select="carare:numberInRoad">
                                          <xsl:if test="text() !=''">
                                             <xsl:value-of select="text()" />
                                             <xsl:if test="following-sibling::*">,</xsl:if>
                                          </xsl:if>
                                       </xsl:for-each>
                                       <xsl:for-each select="carare:roadName">
                                          <xsl:if test="text() !=''">
                                             <xsl:value-of select="text()" />
                                             <xsl:if test="following-sibling::*">,</xsl:if>
                                          </xsl:if>
                                       </xsl:for-each>
                                       <xsl:for-each select="carare:townOrCity">
                                          <xsl:if test="text() !=''">
                                             <xsl:value-of select="text()" />
                                             <xsl:if test="following-sibling::*">,</xsl:if>
                                          </xsl:if>
                                       </xsl:for-each>
                                       <xsl:for-each select="carare:postcodeOrZipcode">
                                          <xsl:if test="text() !=''">
                                             <xsl:value-of select="text()" />
                                             <xsl:if test="following-sibling::*">,</xsl:if>
                                          </xsl:if>
                                       </xsl:for-each>
                                       <xsl:for-each select="carare:locality">
                                          <xsl:if test="text() !=''">
                                             <xsl:value-of select="text()" />
                                             <xsl:if test="following-sibling::*">,</xsl:if>
                                          </xsl:if>
                                       </xsl:for-each>
                                       <xsl:for-each select="carare:adminArea">
                                          <xsl:if test="text() !=''">
                                             <xsl:value-of select="text()" />
                                             <xsl:if test="following-sibling::*">,</xsl:if>
                                          </xsl:if>
                                       </xsl:for-each>
                                       <xsl:for-each select="carare:country">
                                          <xsl:if test="text() !=''">
                                             <xsl:value-of select="text()" />
                                             <xsl:if test="following-sibling::*">,</xsl:if>
                                          </xsl:if>
                                       </xsl:for-each>
                                    </xsl:element>
                                 </xsl:for-each>
                              </xsl:otherwise>
                           </xsl:choose>
                           <!--  skos:altLabel  -->
                           <xsl:for-each select="carare:cadastralReference">
                              <xsl:if test="../carare:namedLocation and text() !=''">
                                 <xsl:element name="skos:altLabel">
                                    <xsl:if test="@lang">
                                       <xsl:attribute name="xml:lang">
                                          <xsl:value-of select="@lang" />
                                       </xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:if>
                           </xsl:for-each>
                           <xsl:for-each select="carare:geopoliticalArea/carare:geopoliticalAreaName">
                              <xsl:if test="text() !=''">
                                 <xsl:element name="skos:altLabel">
                                    <xsl:if test="@lang">
                                       <xsl:attribute name="xml:lang">
                                          <xsl:value-of select="@lang" />
                                       </xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:if>
                           </xsl:for-each>
                           <xsl:for-each select="carare:historicalName">
                              <xsl:if test="text() !=''">
                                 <xsl:element name="skos:altLabel">
                                    <xsl:if test="@lang">
                                       <xsl:attribute name="xml:lang">
                                          <xsl:value-of select="@lang" />
                                       </xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:if>
                           </xsl:for-each>
                           <!--  skos:note  -->
                           <xsl:for-each select="carare:address">
                              <xsl:element name="skos:note">
                                 <xsl:for-each select="carare:roadName">
                                    <xsl:if test="@lang">
                                       <xsl:attribute name="xml:lang">
                                          <xsl:value-of select="@lang" />
                                       </xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="text()" />
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:buildingName">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:numberInRoad">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:roadName">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:townOrCity">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:postcodeOrZipcode">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:locality">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:adminArea">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                                 <xsl:for-each select="carare:country">
                                    <xsl:if test="text() !=''">
                                       <xsl:value-of select="text()" />
                                       <xsl:if test="following-sibling::*">,</xsl:if>
                                    </xsl:if>
                                 </xsl:for-each>
                              </xsl:element>
                           </xsl:for-each>
                        </xsl:for-each>
                     </xsl:element>
                  </xsl:if>
                  <!--   reduntant as this is the case having ONLY DR  -->
                  <xsl:if test="not(carare:spatial)">
                     <xsl:if test="../carare:heritageAssetIdentification/carare:spatial">
                        <xsl:element name="edm:Place">
                           <xsl:attribute name="rdf:about">
                              <xsl:value-of select="$uid" />
                              /SP.1
                           </xsl:attribute>
                           <xsl:for-each select="../carare:heritageAssetIdentification/carare:spatial/carare:cartographicReference/carare:coordinates/carare:x">
                              <xsl:element name="wgs84_pos:lat">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:for-each>
                           <xsl:if test="not(carare:spatial/carare:cartographicReference/carare:coordinates/carare:x)">
                              <xsl:for-each select="../carare:heritageAssetIdentification/carare:spatial/carare:geometry/carare:quickpoint/carare:x">
                                 <xsl:element name="wgs84_pos:lat">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:for-each>
                           </xsl:if>
                           <xsl:for-each select="../carare:heritageAssetIdentification/carare:spatial/carare:cartographicReference/carare:coordinates/carare:y">
                              <xsl:element name="wgs84_pos:long">
                                 <xsl:value-of select="text()" />
                              </xsl:element>
                           </xsl:for-each>
                           <xsl:if test="not(carare:spatial/carare:cartographicReference/carare:coordinates/carare:y)">
                              <xsl:for-each select="../carare:heritageAssetIdentification/carare:spatial/carare:geometry/carare:quickpoint/carare:y">
                                 <xsl:element name="wgs84_pos:long">
                                    <xsl:value-of select="text()" />
                                 </xsl:element>
                              </xsl:for-each>
                           </xsl:if>
                           <xsl:for-each select="../carare:heritageAssetIdentification/carare:spatial/carare:locationSet">
                              <xsl:choose>
                                 <xsl:when test="carare:namedLocation">
                                    <xsl:if test="@preferred">
                                       <xsl:for-each select="carare:namedLocation">
                                          <xsl:element name="skos:prefLabel">
                                             <xsl:if test="@lang">
                                                <xsl:attribute name="xml:lang">
                                                   <xsl:value-of select="@lang" />
                                                </xsl:attribute>
                                             </xsl:if>
                                             <xsl:value-of select="text()" />
                                          </xsl:element>
                                       </xsl:for-each>
                                    </xsl:if>
                                    <xsl:if test="not(@preferred)">
                                       <xsl:element name="skos:prefLabel">
                                          <xsl:for-each select="carare:namedLocation">
                                             <xsl:if test="@lang">
                                                <xsl:attribute name="xml:lang">
                                                   <xsl:value-of select="@lang" />
                                                </xsl:attribute>
                                             </xsl:if>
                                             <xsl:value-of select="text()" />
                                             <xsl:if test="following-sibling::*">,</xsl:if>
                                          </xsl:for-each>
                                       </xsl:element>
                                    </xsl:if>
                                 </xsl:when>
                                 <xsl:when test="carare:cadastralReference">
                                    <xsl:for-each select="carare:cadastralReference[1]">
                                       <xsl:element name="skos:prefLabel">
                                          <xsl:if test="@lang">
                                             <xsl:attribute name="xml:lang">
                                                <xsl:value-of select="@lang" />
                                             </xsl:attribute>
                                          </xsl:if>
                                          <xsl:value-of select="text()" />
                                       </xsl:element>
                                    </xsl:for-each>
                                 </xsl:when>
                                 <xsl:otherwise>
                                    <xsl:for-each select="carare:address">
                                       <xsl:element name="skos:prefLabel">
                                          <xsl:for-each select="carare:roadName">
                                             <xsl:if test="@lang">
                                                <xsl:attribute name="xml:lang">
                                                   <xsl:value-of select="@lang" />
                                                </xsl:attribute>
                                             </xsl:if>
                                          </xsl:for-each>
                                          <xsl:for-each select="carare:buildingName">
                                             <xsl:if test="text() !=''">
                                                <xsl:value-of select="text()" />
                                                <xsl:if test="following-sibling::*">,</xsl:if>
                                             </xsl:if>
                                          </xsl:for-each>
                                          <xsl:for-each select="carare:numberInRoad">
                                             <xsl:if test="text() !=''">
                                                <xsl:value-of select="text()" />
                                                <xsl:if test="following-sibling::*">,</xsl:if>
                                             </xsl:if>
                                          </xsl:for-each>
                                          <xsl:for-each select="carare:roadName">
                                             <xsl:if test="text() !=''">
                                                <xsl:value-of select="text()" />
                                                <xsl:if test="following-sibling::*">,</xsl:if>
                                             </xsl:if>
                                          </xsl:for-each>
                                          <xsl:for-each select="carare:townOrCity">
                                             <xsl:if test="text() !=''">
                                                <xsl:value-of select="text()" />
                                                <xsl:if test="following-sibling::*">,</xsl:if>
                                             </xsl:if>
                                          </xsl:for-each>
                                          <xsl:for-each select="carare:postcodeOrZipcode">
                                             <xsl:if test="text() !=''">
                                                <xsl:value-of select="text()" />
                                                <xsl:if test="following-sibling::*">,</xsl:if>
                                             </xsl:if>
                                          </xsl:for-each>
                                          <xsl:for-each select="carare:locality">
                                             <xsl:if test="text() !=''">
                                                <xsl:value-of select="text()" />
                                                <xsl:if test="following-sibling::*">,</xsl:if>
                                             </xsl:if>
                                          </xsl:for-each>
                                          <xsl:for-each select="carare:adminArea">
                                             <xsl:if test="text() !=''">
                                                <xsl:value-of select="text()" />
                                                <xsl:if test="following-sibling::*">,</xsl:if>
                                             </xsl:if>
                                          </xsl:for-each>
                                          <xsl:for-each select="carare:country">
                                             <xsl:if test="text() !=''">
                                                <xsl:value-of select="text()" />
                                                <xsl:if test="following-sibling::*">,</xsl:if>
                                             </xsl:if>
                                          </xsl:for-each>
                                       </xsl:element>
                                    </xsl:for-each>
                                 </xsl:otherwise>
                              </xsl:choose>
                              <xsl:for-each select="carare:cadastralReference">
                                 <xsl:if test="../carare:namedLocation and text() != ''">
                                    <xsl:element name="skos:altLabel">
                                       <xsl:if test="@lang">
                                          <xsl:attribute name="xml:lang">
                                             <xsl:value-of select="@lang" />
                                          </xsl:attribute>
                                       </xsl:if>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:if>
                              </xsl:for-each>
                              <xsl:for-each select="carare:address">
                                 <xsl:if test="text() !=''">
                                    <xsl:element name="skos:altLabel">
                                       <xsl:for-each select="carare:roadName">
                                          <xsl:if test="@lang">
                                             <xsl:attribute name="xml:lang">
                                                <xsl:value-of select="@lang" />
                                             </xsl:attribute>
                                          </xsl:if>
                                       </xsl:for-each>
                                    </xsl:element>
                                 </xsl:if>
                              </xsl:for-each>
                              <xsl:for-each select="carare:geopoliticalArea/geopoliticalAreaName">
                                 <xsl:if test="text() !=''">
                                    <xsl:element name="skos:altLabel">
                                       <xsl:if test="@lang">
                                          <xsl:attribute name="xml:lang">
                                             <xsl:value-of select="@lang" />
                                          </xsl:attribute>
                                       </xsl:if>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:if>
                              </xsl:for-each>
                              <xsl:for-each select="carare:historicalName">
                                 <xsl:if test="text() !=''">
                                    <xsl:element name="skos:altLabel">
                                       <xsl:if test="@lang">
                                          <xsl:attribute name="xml:lang">
                                             <xsl:value-of select="@lang" />
                                          </xsl:attribute>
                                       </xsl:if>
                                       <xsl:value-of select="text()" />
                                    </xsl:element>
                                 </xsl:if>
                              </xsl:for-each>
                              <xsl:for-each select="carare:address">
                                 <xsl:element name="skos:note">
                                    <xsl:for-each select="carare:roadName">
                                       <xsl:if test="@lang">
                                          <xsl:attribute name="xml:lang">
                                             <xsl:value-of select="@lang" />
                                          </xsl:attribute>
                                       </xsl:if>
                                    </xsl:for-each>
                                    <xsl:for-each select="carare:buildingName">
                                       <xsl:if test="text() !=''">
                                          <xsl:value-of select="text()" />
                                          <xsl:if test="following-sibling::*">,</xsl:if>
                                       </xsl:if>
                                    </xsl:for-each>
                                    <xsl:for-each select="carare:numberInRoad">
                                       <xsl:if test="text() !=''">
                                          <xsl:value-of select="text()" />
                                          <xsl:if test="following-sibling::*">,</xsl:if>
                                       </xsl:if>
                                    </xsl:for-each>
                                    <xsl:for-each select="carare:roadName">
                                       <xsl:if test="text() !=''">
                                          <xsl:value-of select="text()" />
                                          <xsl:if test="following-sibling::*">,</xsl:if>
                                       </xsl:if>
                                    </xsl:for-each>
                                    <xsl:for-each select="carare:townOrCity">
                                       <xsl:if test="text() !=''">
                                          <xsl:value-of select="text()" />
                                          <xsl:if test="following-sibling::*">,</xsl:if>
                                       </xsl:if>
                                    </xsl:for-each>
                                    <xsl:for-each select="carare:postcodeOrZipcode">
                                       <xsl:if test="text() !=''">
                                          <xsl:value-of select="text()" />
                                          <xsl:if test="following-sibling::*">,</xsl:if>
                                       </xsl:if>
                                    </xsl:for-each>
                                    <xsl:for-each select="carare:locality">
                                       <xsl:if test="text() !=''">
                                          <xsl:value-of select="text()" />
                                          <xsl:if test="following-sibling::*">,</xsl:if>
                                       </xsl:if>
                                    </xsl:for-each>
                                    <xsl:for-each select="carare:adminArea">
                                       <xsl:if test="text() !=''">
                                          <xsl:value-of select="text()" />
                                          <xsl:if test="following-sibling::*">,</xsl:if>
                                       </xsl:if>
                                    </xsl:for-each>
                                    <xsl:for-each select="carare:country">
                                       <xsl:if test="text() !=''">
                                          <xsl:value-of select="text()" />
                                          <xsl:if test="following-sibling::*">,</xsl:if>
                                       </xsl:if>
                                    </xsl:for-each>
                                 </xsl:element>
                              </xsl:for-each>
                           </xsl:for-each>
                        </xsl:element>
                     </xsl:if>
                  </xsl:if>
                  <!--  ore:Aggregation  -->
                  <!--
<xsl:for-each select="carare:recordInformation/carare:id">
-->
                  <xsl:element name="ore:Aggregation">
                     <xsl:attribute name="rdf:about">
                        http://more.locloud.eu/object/
                        <xsl:value-of select="$provider_name" />
                        /
                        <xsl:value-of select="$item_id" />
                        #aggregation
                     </xsl:attribute>
                     <!--
<xsl:attribute name="rdf:about">http://store.carare.eu/uid/<xsl:value-of select="$uid" />/DR:<xsl:value-of select="text()" />.<xsl:value-of select="$i" /></xsl:attribute>
-->
                     <!--  edm:aggregatedCHO  -->
                     <xsl:element name="edm:aggregatedCHO">
                        <xsl:attribute name="rdf:about">
                           http://more.locloud.eu/object/
                           <xsl:value-of select="$provider_name" />
                           /
                           <xsl:value-of select="$item_id" />
                        </xsl:attribute>
                        <!--
<xsl:attribute name="rdf:resource">DR:<xsl:value-of select="text()" /> </xsl:attribute>
-->
                     </xsl:element>
                     <!--  edm:dataProvider  -->
                     <xsl:for-each select="carare:recordInformation/carare:source">
                        <xsl:if test="text() !=''">
                           <xsl:element name="edm:dataProvider">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  edm:hasView  -->
                     <xsl:choose>
                        <xsl:when test="../carare:digitalResource[2]/carare:link">
                           <xsl:for-each select="../../../carare:digitalResource/carare:link">
                              <xsl:if test="position()&gt;1">
                                 <xsl:element name="edm:hasView">
                                    <xsl:attribute name="rdf:resource">
                                       <xsl:value-of select="text()" />
                                    </xsl:attribute>
                                 </xsl:element>
                              </xsl:if>
                           </xsl:for-each>
                        </xsl:when>
                        <xsl:otherwise />
                     </xsl:choose>
                     <!--  edm:isShownAt  -->
                     <xsl:for-each select="../carare:digitalResource[1]/carare:isShownAt">
                        <xsl:if test="text() !=''">
                           <xsl:element name="edm:isShownAt">
                              <xsl:attribute name="rdf:resource">
                                 <xsl:value-of select="text()" />
                              </xsl:attribute>
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  edm:isShownBy  -->
                     <xsl:for-each select="../carare:digitalResource[1]/carare:link">
                        <xsl:if test="text() !=''">
                           <xsl:element name="edm:isShownBy">
                              <xsl:attribute name="rdf:resource">
                                 <xsl:value-of select="text()" />
                              </xsl:attribute>
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  edm:object  -->
                     <xsl:for-each select="../carare:digitalResource[1]/carare:object">
                        <xsl:if test="text() !=''">
                           <xsl:element name="edm:object">
                              <xsl:attribute name="rdf:resource">
                                 <xsl:value-of select="text()" />
                              </xsl:attribute>
                           </xsl:element>
                        </xsl:if>
                     </xsl:for-each>
                     <!--  edm:provider  -->
                     <xsl:element name="edm:provider">CARARE</xsl:element>
                     <!--
<xsl:element name="dc:rights">http://creativecommons.org/publicdomain/zero/1.0/</xsl:element>
-->
                     <!--
dc:rights
							<xsl:for-each select="carare:rights/carare:copyright/carare:creditLine">
								<xsl:if test="text() !=''">
									<xsl:choose>
										<xsl:when test="@lang &gt; '0'">
											<xsl:element name="dc:rights">
												<xsl:attribute name="xml:lang">
													<xsl:value-of select="@lang" />
												</xsl:attribute>
												<xsl:value-of select="text()" />
											</xsl:element>	      
										</xsl:when>
										<xsl:otherwise>
											<xsl:element name="dc:rights">
												<xsl:value-of select="text()" />
											</xsl:element>	      
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>
							</xsl:for-each> 
-->
                     <xsl:if test="not(carare:rights/carare:copyright/carare:creditLine)">
                        <xsl:for-each select="carare:rights/carare:licence">
                           <xsl:element name="dc:rights">
                              <xsl:value-of select="text()" />
                           </xsl:element>
                        </xsl:for-each>
                     </xsl:if>
                     <!--  edm:rights  -->
                     <xsl:element name="edm:rights">
                        <xsl:attribute name="rdf:resource">
                           <xsl:value-of select="$edm_rights" />
                        </xsl:attribute>
                     </xsl:element>
                  </xsl:element>
                  <!--  </xsl:for-each>  -->
               </xsl:if>
            </xsl:for-each>
         </xsl:if>
      </rdf:RDF>
   </xsl:template>
</xsl:stylesheet>