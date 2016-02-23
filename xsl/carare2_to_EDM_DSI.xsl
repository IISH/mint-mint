<?xml version="1.0" encoding="utf-8"?>

<!DOCTYPE xsl:stylesheet  [
	<!ENTITY nbsp   "&#160;">
    <!ENTITY quote  "&#34;"> 
	<!ENTITY copy   "&#169;">
	<!ENTITY reg    "&#174;">
	<!ENTITY trade  "&#8482;">
	<!ENTITY mdash  "&#8212;">
	<!ENTITY ldquo  "&#8220;">
	<!ENTITY rdquo  "&#8221;"> 
	<!ENTITY euro   "&#8364;">
	<!ENTITY amp   	"&#38;">
	<!ENTITY laquo   "&#171;">
	<!ENTITY raquo   "&#187;">
]>

<xsl:stylesheet version="2.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:carare="http://www.carare.eu/carareSchema" 
xmlns:edm="http://www.europeana.eu/schemas/edm/" 
xmlns:dc="http://purl.org/dc/elements/1.1/" 
xmlns:dcterms="http://purl.org/dc/terms/" 
xmlns:ore="http://www.openarchives.org/ore/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
xmlns:wgs84_pos="http://www.w3.org/2003/01/geo/wgs84_pos#"
xmlns:skos="http://www.w3.org/2004/02/skos/core#"
xmlns:owl="http://www.w3.org/2002/07/owl#"

>
<xsl:strip-space elements="*" />
<xsl:output method="xml"  version="1.0" encoding="utf-8" indent="yes"/>
<xsl:param name="uid" />

	<xsl:template match="/">
    <xsl:apply-templates select="carare:carareWrap" />
  </xsl:template>
    
  <xsl:template match="carare:carareWrap">

	<xsl:variable name="lower">abcdefghijklmnopqrstuvwxyz</xsl:variable>
	<xsl:variable name="upper">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>  
 
	<rdf:RDF>    
  
	<xsl:for-each select="carare:carare/carare:heritageAssetIdentification">
		<xsl:element name="edm:ProvidedCHO">
            <xsl:attribute name="rdf:about"><xsl:value-of select="carare:recordInformation/carare:id" />
            </xsl:attribute>
		<xsl:for-each select="carare:characters/carare:temporal/carare:timeSpan/carare:startDate">
		  <dc:date> <xsl:value-of select="text()" /></dc:date>
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:temporal/carare:timeSpan/carare:endDate">
		  <dc:date><xsl:value-of select="text()" /></dc:date>
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:temporal/carare:displayDate">
			<xsl:if test='@preferred'>	
				<xsl:choose>
				<xsl:when test="@lang &gt; '0' and @preferred = 'true'">
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
		<xsl:for-each select="carare:characters/carare:temporal/carare:displayDate[2]">
			<xsl:if test='not(@preferred)'>	
				<xsl:choose>
				<xsl:when test="node()">
					<xsl:element name="dc:date">
						<xsl:attribute name="xml:lang">
							<xsl:value-of select="translate(@lang,$upper,$lower)" />
						</xsl:attribute>
						<xsl:value-of select="text()" />
					</xsl:element>	      
				</xsl:when>
				<xsl:otherwise> 
				</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:temporal/carare:scientificDate">
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
		</xsl:for-each>
	    <xsl:for-each select="carare:characters/carare:craft/carare:lastJourneyDetails/carare:dateOfLoss">
		    <xsl:if test='@preferred'>	
				<xsl:choose>
				<xsl:when test="@lang &gt; '0' and @preferred = 'true'">
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
		<xsl:for-each select="carare:characters/carare:craft/carare:lastJourneyDetails/carare:dateOfLoss[2]">
			<xsl:if test='not(@preferred)'>	
				<xsl:choose>
				<xsl:when test="node()">
					<xsl:element name="dc:date">
						<xsl:attribute name="xml:lang">
							<xsl:value-of select="translate(@lang,$upper,$lower)" />
						</xsl:attribute>
						<xsl:value-of select="text()" />
					</xsl:element>	      
				</xsl:when>
				<xsl:otherwise> 
				</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:craft/carare:lastJourneyDetails/carare:displayDate">
		    <xsl:if test='@preferred'>	
				<xsl:choose>
				<xsl:when test="@lang &gt; '0' and @preferred = 'true'">
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
		<xsl:for-each select="carare:characters/carare:craft/carare:lastJourneyDetails/carare:displayDate[2]">
			<xsl:if test='not(@preferred)'>	
				<xsl:choose>
				<xsl:when test="node()">
					<xsl:element name="dc:date">
						<xsl:attribute name="xml:lang">
							<xsl:value-of select="translate(@lang,$upper,$lower)" />
						</xsl:attribute>
						<xsl:value-of select="text()" />
					</xsl:element>	      
				</xsl:when>
				<xsl:otherwise> 
				</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:for-each>
		<xsl:for-each select="carare:description">			
			<xsl:element name="dc:description">
				<xsl:if test='@lang'>
					<xsl:attribute name="xml:lang">
					   <xsl:value-of select="translate(@lang,$upper,$lower)" />
					</xsl:attribute>
				</xsl:if>
				<xsl:value-of select="text()" />
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="carare:description[2]">
			<xsl:if test='not(@preferred)'>	
				<xsl:choose>
				<xsl:when test="node()">
					<xsl:element name="dc:description">
						<xsl:attribute name="xml:lang">
							<xsl:value-of select="translate(@lang,$upper,$lower)" />
						</xsl:attribute>
						<xsl:value-of select="text()" />
					</xsl:element>	      
				</xsl:when>
				<xsl:otherwise> 
				</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:craft/carare:lastJourneyDetails/carare:mannerOfLoss">
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
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:craft/carare:lastJourneyDetails/carare:cargo">
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
		</xsl:for-each>
		<xsl:for-each select="carare:generalType">
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
		</xsl:for-each>
		<xsl:for-each select="carare:appellation/carare:id">
		    <dc:identifier><xsl:value-of select="text()" /></dc:identifier>
		</xsl:for-each>
		<xsl:for-each select="carare:recordInformation/carare:language">
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
		</xsl:for-each>
		<xsl:for-each select="carare:publicationStatement">
			<xsl:element name="dc:publisher">
				<xsl:for-each select="carare:publisher">
					<xsl:if test="text() !=''"><xsl:value-of select="text()" />
					<xsl:if test="following-sibling::*">, </xsl:if></xsl:if>
				</xsl:for-each>
				<xsl:for-each select="carare:place">
					<xsl:if test="text() !=''"><xsl:value-of select="text()" />
					<xsl:if test="following-sibling::*">, </xsl:if></xsl:if>
				</xsl:for-each>
				<xsl:for-each select="carare:date">
					<xsl:if test="text() !=''"><xsl:value-of select="text()" />
					<xsl:if test="following-sibling::*">, </xsl:if></xsl:if>
				</xsl:for-each>
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="carare:isSuccessorOf">
		    <edm:isSuccessorOf>
				<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>
			</edm:isSuccessorOf>
		</xsl:for-each>
		<xsl:for-each select="carare:hasPart">
			<xsl:choose>
				<xsl:when test="contains(text(), 'http')">
					<xsl:element name="dcterms:hasPart">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:when test="contains(text(), 'www')">
					<xsl:element name="dcterms:hasPart">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:element name="dcterms:hasPart">
						<xsl:value-of select="text()" />
					</xsl:element>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:isPartOf">
		    <xsl:choose>
				<xsl:when test="contains(text(), 'http')">
					<xsl:element name="dcterms:isPartOf">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>
					</xsl:element>
				</xsl:when>
				<xsl:when test="contains(text(), 'www')">
					<xsl:element name="dcterms:isPartOf">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<dcterms:isPartOf>
						<xsl:value-of select="text()" />
					</dcterms:isPartOf>
				 </xsl:otherwise>
			 </xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:isReplicaOf">
			<xsl:choose>
				<xsl:when test="contains(text(), 'http')">
					<xsl:element name="dcterms:isVersionOf">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>
					</xsl:element>
				</xsl:when>
				<xsl:when test="contains(text(), 'www')">
					<xsl:element name="dcterms:isVersionOf">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<dcterms:isVersionOf>
						<xsl:value-of select="text()" />
					</dcterms:isVersionOf>
				 </xsl:otherwise>
			 </xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:wasDigitizedBy">
		    <edm:hasMet>
				<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>
			</edm:hasMet>
		</xsl:for-each>
		<xsl:for-each select="carare:dcRelation">
			<xsl:choose>
				<xsl:when test="contains(text(), 'http')">
					<xsl:element name="dc:relation">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:when test="contains(text(), 'www')">
					<xsl:element name="dc:relation">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:element name="dc:relation">
						<xsl:value-of select="text()" />
					</xsl:element>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:rights/carare:copyrightCreditLine">
        	<xsl:choose>
              <xsl:when test="@lang &gt; '0'">
              <xsl:element name="dc:rights">
                <xsl:attribute name="xml:lang">
                    <xsl:value-of select="translate(@lang,$upper,$lower)" />
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
        </xsl:for-each>  
        <xsl:if test="not(carare:rights/carare:copyrightCreditLine)">
			<xsl:for-each select="carare:rights/carare:licence">   
		  <xsl:element name="dc:rights">
				<xsl:value-of select="text()" />
			  </xsl:element>
		  </xsl:for-each>
		</xsl:if>
		<xsl:for-each select="carare:recordInformation/carare:source">
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
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:heritageAssetType">
			<xsl:choose>
				<xsl:when test="contains(text(), 'http')">
					<xsl:element name="dc:subject">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:when test="contains(text(), 'www')">
					<xsl:element name="dc:subject">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:element name="dc:subject">
						<xsl:if test="@lang &gt; '0'">				
							<xsl:attribute name="xml:lang">
								<xsl:value-of select="translate(@lang,$upper,$lower)" />
							</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="text()" />
					</xsl:element>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:craft/carare:constructionMethod">
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
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:craft/carare:propulsion">
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
		</xsl:for-each>
		
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
			
			<xsl:if test="$countNames>1">
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
		</xsl:for-each>
		<xsl:for-each select="carare:generalType">
			<xsl:choose>
				<xsl:when test="@lang &gt; '0'">
					<xsl:element name="dc:type">
						<xsl:attribute name="xml:lang">
							<xsl:value-of select="translate(@lang,$upper,$lower)" />
						</xsl:attribute>
						<xsl:value-of select="text()" />
					</xsl:element>	      
				</xsl:when>   
				<xsl:otherwise>
					<xsl:element name="dc:type">
						<xsl:value-of select="text()" />
					</xsl:element>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:dimensions">
			<xsl:element name="dcterms:extent">
			<xsl:for-each select="carare:extent">
				<xsl:value-of select="text()" />     
				<xsl:if test="following-sibling::*">, </xsl:if>
			</xsl:for-each>
			<xsl:for-each select="carare:measurementType">
				<xsl:value-of select="text()" />     
				<xsl:if test="following-sibling::*">, </xsl:if>
			</xsl:for-each>
			<xsl:for-each select="carare:units">
				<xsl:value-of select="text()" />     
				<xsl:if test="following-sibling::*">, </xsl:if>
			</xsl:for-each>
			<xsl:for-each select="carare:scale">
				<xsl:value-of select="text()" />      
				<xsl:if test="following-sibling::*">, </xsl:if>
			</xsl:for-each>
			<xsl:for-each select="carare:value">
				<xsl:value-of select="text()" />
			</xsl:for-each>
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="carare:characters/carare:materials">
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
				<dcterms:medium><xsl:value-of select="text()" /></dcterms:medium>
			  </xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:provenance">
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
				<dcterms:provenance><xsl:value-of select="text()" /></dcterms:provenance>
			  </xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:references">
			<xsl:element name="dcterms:references">
				<xsl:for-each select="carare:appellation/carare:name">
					<xsl:value-of select="text()" />
					<xsl:if test="following-sibling::*">, </xsl:if>
				</xsl:for-each>
				<xsl:for-each select="carare:actors/carare:name">
					<xsl:value-of select="text()" />
				</xsl:for-each>
			</xsl:element>	      
		</xsl:for-each>
		<xsl:for-each select="carare:isSuccessorOf">
			<xsl:choose>
				<xsl:when test="contains(text(), 'http')">
					<xsl:element name="dcterms:replaces">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:when test="contains(text(), 'www')">
					<xsl:element name="dcterms:replaces">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:element name="dcterms:replaces">
						<xsl:value-of select="text()" />
					</xsl:element>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:spatial/carare:locationSet">
			<xsl:element name="dcterms:spatial"> 
			<xsl:variable name="i" select="position()" />
				<xsl:attribute name="rdf:resource"><xsl:value-of select="$uid" />/SP.<xsl:value-of select="$i" />
				</xsl:attribute>
			</xsl:element>
        </xsl:for-each>
		<xsl:for-each select="carare:characters/carare:temporal/carare:periodName">
			<xsl:choose>
				<xsl:when test="contains(text(), 'http')">
					<xsl:element name="dcterms:temporal">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
				<xsl:when test="contains(text(), 'www')">
					<xsl:element name="dc:temporal">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>						
					</xsl:element>
				</xsl:when>
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
		</xsl:for-each>
		<xsl:for-each select="carare:hasPart">
		    <xsl:element name="edm:incorporates">
				<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="carare:generalType">
			 <xsl:element name="edm:type">
				<xsl:choose>
					<xsl:when test="contains(text(), 'Monument')">TEXT</xsl:when>
					<xsl:when test="contains(text(), 'Artefact')">TEXT</xsl:when>
					<xsl:when test="contains(text(), 'Text')">TEXT</xsl:when>
					<xsl:when test="contains(text(), 'Image')">IMAGE</xsl:when>
					<xsl:when test="contains(text(), 'Sound')">SOUND</xsl:when>
					<xsl:when test="contains(text(), 'Movie')">VIDEO</xsl:when>					
					<xsl:otherwise>
					   <xsl:value-of select="text()" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:element>
		</xsl:for-each>
		</xsl:element>
	</xsl:for-each>
	
	<xsl:for-each select="carare:carare/carare:digitalResource">
		<xsl:element name="edm:WebResource">
			<xsl:choose>
				<xsl:when test="carare:link">
					<xsl:for-each select="carare:link">
						<xsl:attribute name="rdf:about"><xsl:value-of select="text()" /></xsl:attribute>
					</xsl:for-each>
				</xsl:when>
				<xsl:when test="carare:isShownAt">
					<xsl:for-each select="carare:isShownAt">
						<xsl:attribute name="rdf:about"><xsl:value-of select="text()" /></xsl:attribute>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="rdf:about"></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
		
		<xsl:for-each select="carare:description">
			<xsl:element name="dc:description">
				<xsl:if test="@lang">
					<xsl:attribute name="xml:lang">
						<xsl:value-of select="translate(@lang,$upper,$lower)" />
					</xsl:attribute>
				</xsl:if>
				<xsl:value-of select="text()" />
			</xsl:element>	      
		</xsl:for-each>
		<xsl:for-each select="carare:format">
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
		</xsl:for-each>
		<xsl:for-each select="carare:rights/carare:copyrightCreditLine">
        	<xsl:choose>
              <xsl:when test="@lang &gt; '0'">
              <xsl:element name="dc:rights">
                <xsl:attribute name="xml:lang">
                    <xsl:value-of select="translate(@lang,$upper,$lower)" />
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
        </xsl:for-each>  
        <xsl:if test="not(carare:rights/carare:copyrightCreditLine)">
			  <xsl:for-each select="carare:rights/carare:licence">   
				<xsl:element name="dc:rights">
					<xsl:value-of select="text()" />
				</xsl:element>
			  </xsl:for-each>	
        </xsl:if> 
		
		<xsl:for-each select="carare:recordInformation/carare:source">
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
		</xsl:for-each>
		<xsl:for-each select="carare:extent">
			<xsl:choose>
            <xsl:when test="@lang &gt; '0'">
				<xsl:element name="dcterms:extent">
					<xsl:attribute name="xml:lang">
						<xsl:value-of select="translate(@lang,$upper,$lower)" />
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
		</xsl:for-each>
		<xsl:for-each select="carare:hasPart">
		    <dcterms:hasPart>
				<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>
			</dcterms:hasPart>
		</xsl:for-each>
		<xsl:for-each select="carare:isPartOf">
			<xsl:element name="dcterms:isPartOf">
				<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="carare:nextInSequence">
		    <xsl:element name="edm:isNextInSequence">
				<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="carare:rights/carare:europeanaRights">
			<xsl:element name="edm:rights">
				<xsl:attribute name="rdf:resource">
					<xsl:if test="contains(text(), 'The Public Domain Mark (PDM)')">http://creativecommons.org/publicdomain/mark/1.0/</xsl:if>
					<xsl:if test="contains(text(), 'Out of copyright - non commercial re-use (OOC-NC)')">http://www.europeana.eu/rights/out-of-copyright-non-commercial/</xsl:if>
					<xsl:if test="contains(text(), 'The Creative Commons CC0 1.0 Universal Public Domain Dedication (CC0)')">http://creativecommons.org/publicdomain/zero/1.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution (BY)')">http://creativecommons.org/licenses/by/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution, ShareAlike (BY-SA)')">http://creativecommons.org/licenses/by-sa/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution, No Derivatives (BY-ND)')">http://creativecommons.org/licenses/by-nd/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution, Non-Commercial (BY-NC)')">http://creativecommons.org/licenses/by-nc/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution, Non-Commercial, ShareAlike (BY-NC-SA)')">http://creativecommons.org/licenses/by-nc-sa/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution, Non-Commercial, No Derivatives (BY-NC-ND)')">http://creativecommons.org/licenses/by-nc-nd/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Free access_no re_use')">http://www.europeana.eu/rights/rr-f/</xsl:if>
					<xsl:if test="contains(text(), 'Paid access_no re_use')">http://www.europeana.eu/rights/rr-p/</xsl:if>
					<xsl:if test="contains(text(), 'Orphan work')">http://www.europeana.eu/rights/orphan-work-eu/</xsl:if>
					<xsl:if test="contains(text(), 'Unknown')">http://www.europeana.eu/rights/unknown/</xsl:if>
				</xsl:attribute> 
			</xsl:element>
		</xsl:for-each>
		</xsl:element> 
	</xsl:for-each>

    <xsl:if test="carare:carare/carare:heritageAssetIdentification/carare:spatial">
		<xsl:for-each select="carare:carare/carare:heritageAssetIdentification/carare:spatial">
			<xsl:element name="edm:Place">
				<xsl:variable name="i" select="position()" />
					<xsl:attribute name="rdf:about"><xsl:value-of select="$uid" />/SP.<xsl:value-of select="$i" />
					</xsl:attribute>
				<xsl:for-each select="carare:geometry/carare:quickpoint/carare:x">
					<xsl:element name="wgs84_pos:lat">
						<xsl:value-of select="text()" /> 
					</xsl:element>
				</xsl:for-each> 
				<xsl:for-each select="carare:cartesianCoordinates3D/carare:viewpoints/carare:x">
					<xsl:element name="wgs84_pos:lat">
						<xsl:value-of select="text()" /> 
					</xsl:element>
				</xsl:for-each> 				
				<xsl:for-each select="carare:geometry/carare:quickpoint/carare:y">
					<xsl:element name="wgs84_pos:long">
						<xsl:value-of select="text()" />
					</xsl:element>
				</xsl:for-each>
				<xsl:for-each select="carare:cartesianCoordinates3D/carare:viewpoints/carare:y">
					<xsl:element name="wgs84_pos:long">
						<xsl:value-of select="text()" /> 
					</xsl:element>
				</xsl:for-each>
				<xsl:for-each select="carare:geometry/carare:quickpoint/carare:z">
					<xsl:element name="wgs84_pos:alt">
						<xsl:value-of select="text()" />
					</xsl:element>
				</xsl:for-each>
				<xsl:for-each select="carare:cartesianCoordinates3D/carare:viewpoints/carare:z">
					<xsl:element name="wgs84_pos:alt">
						<xsl:value-of select="text()" /> 
					</xsl:element>
				</xsl:for-each>
				<xsl:for-each select="carare:locationSet">
					<xsl:choose>
					<xsl:when test="carare:namedLocation">
					<xsl:if test='@preferred'>
						<xsl:for-each select="carare:namedLocation">
						<xsl:element name="skos:prefLabel">
							<xsl:if test="@lang">
							<xsl:attribute name="xml:lang">
								<xsl:value-of select="translate(@lang,$upper,$lower)" />
							</xsl:attribute>
							</xsl:if>
						<xsl:value-of select="text()" />
						</xsl:element>
						</xsl:for-each>
					</xsl:if>
					<xsl:if test='not(@preferred)'>    
						<xsl:for-each select="carare:namedLocation[2]">
						<xsl:element name="skos:prefLabel">
							<xsl:if test="@lang">
							<xsl:attribute name="xml:lang">
								<xsl:value-of select="translate(@lang,$upper,$lower)" />
							</xsl:attribute>
							</xsl:if>
						<xsl:value-of select="text()" />
						</xsl:element>
						</xsl:for-each>
					</xsl:if>
					</xsl:when>
					<xsl:otherwise> </xsl:otherwise>
					</xsl:choose>
					<xsl:for-each select="carare:historicalName">
						<xsl:if test="text() !=''">
							<xsl:element name="skos:altLabel">
							<xsl:if test="@lang">
								<xsl:attribute name="xml:lang">
									<xsl:value-of select="translate(@lang,$upper,$lower)" />
								</xsl:attribute>
								</xsl:if>
							<xsl:value-of select="text()" />
							</xsl:element>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="carare:address">
						<xsl:element name="skos:note">
							<xsl:for-each select="carare:buildingName"> 
								<xsl:if test="text() !=''">
								<xsl:value-of select="text()" />
									<xsl:if test="following-sibling::*">, </xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:for-each select="carare:numberInRoad"> 
								<xsl:if test="text() !=''"> 
								<xsl:value-of select="text()" />
									<xsl:if test="following-sibling::*">, </xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:for-each select="carare:roadName">
								<xsl:if test="text() !=''">
								<xsl:value-of select="text()" />
									<xsl:if test="following-sibling::*">, </xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:for-each select="carare:townOrCity"> 
								<xsl:if test="text() !=''">
								<xsl:value-of select="text()" />
									<xsl:if test="following-sibling::*">, </xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:for-each select="carare:postcodeOrZipcode"> 
								<xsl:if test="text() !=''">
								<xsl:value-of select="text()" />
									<xsl:if test="following-sibling::*">, </xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:for-each select="carare:locality"> 
								<xsl:if test="not(contains(text(), 'http'))">
									<xsl:value-of select="text()" />
									<xsl:if test="following-sibling::*">, </xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:for-each select="carare:adminArea"> 
								<xsl:if test="text() !=''">
								<xsl:value-of select="text()" />
									<xsl:if test="following-sibling::*">, </xsl:if>
								</xsl:if>
							</xsl:for-each>
							<xsl:for-each select="carare:country">
								<xsl:if test="text() !=''">
								<xsl:value-of select="text()" />
									<xsl:if test="following-sibling::*">, </xsl:if>
								</xsl:if>
							</xsl:for-each>
						</xsl:element>
					</xsl:for-each>
					<xsl:for-each select="carare:geopoliticalArea">
						<xsl:element name="dcterms:isPartOf">
							<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>
						</xsl:element>
					</xsl:for-each>
					<xsl:for-each select="carare:address/carare:locality">
						<xsl:if test="contains(text(), 'http')">
						<xsl:element name="owl:sameAs">
							<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>
						</xsl:element>
						</xsl:if>
					</xsl:for-each>
				</xsl:for-each>
			</xsl:element>
		</xsl:for-each>
	</xsl:if>
	
	<xsl:for-each select="carare:carare/carare:activity">
		<xsl:element name="edm:Event">
            <xsl:attribute name="rdf:about">
				<xsl:value-of select="carare:recordInformation/carare:id" />
            </xsl:attribute>
		<xsl:for-each select="carare:spatial/carare:locationSet">
			<xsl:choose>
				<xsl:when test="carare:namedLocation">
					<xsl:if test='@preferred'>
						<xsl:for-each select="carare:namedLocation">
						<xsl:element name="edm:happenedAt">
							<xsl:if test="@lang">
							<xsl:attribute name="xml:lang">
								<xsl:value-of select="translate(@lang,$upper,$lower)" />
							</xsl:attribute>
							</xsl:if>
						<xsl:value-of select="text()" />
						</xsl:element>
						</xsl:for-each>
					</xsl:if>
					<xsl:if test='not(@preferred)'>    
						<xsl:for-each select="carare:namedLocation[2]">
						<xsl:element name="edm:happenedAt">
							<xsl:if test="@lang">
							<xsl:attribute name="xml:lang">
								<xsl:value-of select="translate(@lang,$upper,$lower)" />
							</xsl:attribute>
							</xsl:if>
						<xsl:value-of select="text()" />
						</xsl:element>
						</xsl:for-each>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:temporal/carare:timeSpan/carare:startDate">
			<xsl:element name="edm:occuredAt">
				<xsl:value-of select="text()" />
			</xsl:element>
		</xsl:for-each>	
		<xsl:for-each select="carare:appellation/carare:name">
			<xsl:if test='@preferred'>
				<xsl:choose>        
					<xsl:when test="@lang &gt; '0' and @preferred = 'true'">
						<xsl:element name="skos:prefLabel">
							<xsl:attribute name="xml:lang">
								<xsl:value-of select="translate(@lang,$upper,$lower)" />
							</xsl:attribute>
							<xsl:value-of select="text()" />
						</xsl:element>
					</xsl:when>
					<xsl:when test="@lang &gt; '0'">
						<xsl:element name="skos:altLabel">
							<xsl:attribute name="xml:lang">
								<xsl:value-of select="translate(@lang,$upper,$lower)" />
							</xsl:attribute>
							<xsl:value-of select="text()" />
						</xsl:element>
					</xsl:when>
					<xsl:otherwise>
						<xsl:element name="skos:altLabel">
							<xsl:value-of select="text()" />
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>
		   </xsl:if> 
		</xsl:for-each>
		<xsl:for-each select="carare:appellation/carare:name[2]">
			<xsl:if test='@preferred'>    
				<xsl:choose>      
				<xsl:when test="node()">
					<xsl:element name="skos:prefLabel"> 
						<xsl:attribute name="xml:lang">
							<xsl:value-of select="translate(@lang,$upper,$lower)" />
						</xsl:attribute><xsl:value-of select="text()" />
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
				</xsl:otherwise>
				 </xsl:choose>
			</xsl:if>
		</xsl:for-each>
		<xsl:for-each select="carare:appellation/carare:name/following-sibling::carare:name[2]">
			<xsl:if test='not(@preferred)'>    
				<xsl:choose>      
				<xsl:when test="@lang &gt; '0'">
					<xsl:element name="skos:altLabel"> 
						<xsl:attribute name="xml:lang">
							<xsl:value-of select="translate(@lang,$upper,$lower)" />
						</xsl:attribute><xsl:value-of select="text()" />
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:element name="skos:altLabel"> 
						<xsl:value-of select="text()" />
					</xsl:element>
				</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:for-each>
		<xsl:for-each select="carare:description">
			<xsl:choose>
			<xsl:when test="@lang &gt; '0'">
				<xsl:element name="skos:note">
					<xsl:attribute name="xml:lang">
						<xsl:value-of select="translate(@lang,$upper,$lower)" />
					</xsl:attribute>
					<xsl:value-of select="text()" />
				</xsl:element>	      
			</xsl:when>
			<xsl:otherwise>
				<xsl:element name="skos:note">
					<xsl:value-of select="text()" />
				</xsl:element>
			</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:appellation/carare:id">
			<dc:identifier><xsl:value-of select="text()" /></dc:identifier>
		</xsl:for-each>
		<xsl:for-each select="carare:hasPart">
		    <dcterms:hasPart>
				<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>
			</dcterms:hasPart>
		</xsl:for-each>
		<xsl:for-each select="carare:isPartOf">
		    <xsl:choose>
			<xsl:when test="contains(text(), 'http')">
				<xsl:element name="dcterms:isPartOf">
					<xsl:attribute name="rdf:resource">
						<xsl:value-of select="text()" />
					</xsl:attribute>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise>
				<dcterms:isPartOf>
					<xsl:value-of select="text()" />
				</dcterms:isPartOf>
			 </xsl:otherwise>
			 </xsl:choose>
		</xsl:for-each>
		<xsl:for-each select="carare:eventType">
			<xsl:element name="edm:hasType">				 
				<xsl:if test="@lang &gt; '0'">
					<xsl:attribute name="xml:lang">
						<xsl:value-of select="translate(@lang,$upper,$lower)" />
					</xsl:attribute>
					<xsl:value-of select="text()" />
				</xsl:if>
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="carare:dcRelation">
		    <xsl:element name="edm:isRelatedTo">
				<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>
			</xsl:element>
		</xsl:for-each>
		</xsl:element>
	</xsl:for-each>
	
	<xsl:for-each select="carare:carare/carare:heritageAssetIdentification">
    	<xsl:element name="ore:Aggregation">
		<xsl:for-each select="carare:recordInformation/carare:id">
        	<xsl:attribute name="rdf:about"><xsl:value-of select="text()" />
            </xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="carare:recordInformation/carare:id">
		   <xsl:element name="edm:aggregatedCHO">
				<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" />
				</xsl:attribute> 
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="carare:recordInformation/carare:source">
			<xsl:element name="edm:dataProvider">
				<xsl:value-of select="text()" />
			</xsl:element>
		</xsl:for-each>
		<xsl:choose>
			<xsl:when test="../carare:digitalResource[2]/carare:link">
				<xsl:for-each select="../carare:digitalResource/carare:link">
					<xsl:if test="position()>1">
						<xsl:element name="edm:hasView">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>
						</xsl:element>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="../carare:digitalResource[2]/carare:isShownAt">
				<xsl:for-each select="../carare:digitalResource/carare:isShownAt">
					<xsl:if test="position()>1">
						<xsl:element name="edm:hasView">
						<xsl:attribute name="rdf:resource">
							<xsl:value-of select="text()" />
						</xsl:attribute>
						</xsl:element>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
		<xsl:for-each select="../carare:digitalResource[1]/carare:isShownAt">
		   <xsl:element name="edm:isShownAt">
			<xsl:attribute name="rdf:resource">
				<xsl:value-of select="text()" />
			</xsl:attribute>                
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="../carare:digitalResource[1]/carare:link">
			<xsl:element name="edm:isShownBy">
				<xsl:attribute name="rdf:resource">
					<xsl:value-of select="text()" />
				</xsl:attribute>               
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="../carare:digitalResource[1]/carare:object">
		<xsl:element name="edm:object">
			<xsl:attribute name="rdf:resource"><xsl:value-of select="text()" /></xsl:attribute>             
		</xsl:element>
		</xsl:for-each>
		<xsl:element name="edm:provider">CARARE</xsl:element>
		<xsl:for-each select="../carare:heritageAssetIdentification/carare:rights/carare:europeanaRights">
			<xsl:element name="edm:rights">
				<xsl:attribute name="rdf:resource">
					<xsl:if test="contains(text(), 'The Public Domain Mark (PDM)')">http://creativecommons.org/publicdomain/mark/1.0/</xsl:if>
					<xsl:if test="contains(text(), 'Out of copyright - non commercial re-use (OOC-NC)')">http://www.europeana.eu/rights/out-of-copyright-non-commercial/</xsl:if>
					<xsl:if test="contains(text(), 'The Creative Commons CC0 1.0 Universal Public Domain Dedication (CC0)')">http://creativecommons.org/publicdomain/zero/1.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution (BY)')">http://creativecommons.org/licenses/by/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution, ShareAlike (BY-SA)')">http://creativecommons.org/licenses/by-sa/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution, No Derivatives (BY-ND)')">http://creativecommons.org/licenses/by-nd/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution, Non-Commercial (BY-NC)')">http://creativecommons.org/licenses/by-nc/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution, Non-Commercial, ShareAlike (BY-NC-SA)')">http://creativecommons.org/licenses/by-nc-sa/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Creative Commons - Attribution, Non-Commercial, No Derivatives (BY-NC-ND)')">http://creativecommons.org/licenses/by-nc-nd/4.0/</xsl:if>
					<xsl:if test="contains(text(), 'Free access_no re_use')">http://www.europeana.eu/rights/rr-f/</xsl:if>
					<xsl:if test="contains(text(), 'Paid access_no re_use')">http://www.europeana.eu/rights/rr-p/</xsl:if>
					<xsl:if test="contains(text(), 'Orphan work')">http://www.europeana.eu/rights/orphan-work-eu/</xsl:if>
					<xsl:if test="contains(text(), 'Unknown')">http://www.europeana.eu/rights/unknown/</xsl:if>
				</xsl:attribute> 
			</xsl:element>
		</xsl:for-each>
		</xsl:element>
	</xsl:for-each>
		
    <xsl:for-each select="carare:format">
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
    
    </rdf:RDF>
    
  </xsl:template>
</xsl:stylesheet>