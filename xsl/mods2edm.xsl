<?xml version="1.0" encoding="UTF-8" ?>

<!-- New XSLT document created with EditiX XML Editor (http://www.editix.com) at Tue Feb 09 14:10:52 CET 2016 -->

<xsl:stylesheet version="2.0"
	xmlns:mods="http://www.loc.gov/mods/v3"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:dcterms="http://purl.org/dc/terms/"
	xmlns:edm="http://www.europeana.eu/schemas/edm/"
	xmlns:ore="http://www.openarchives.org/ore/terms/"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:math="http://exslt.org/math"
    extension-element-prefixes="math">

	<xsl:output method="xml" indent="yes"/>

	<!-- Set variables for production -->
    <xsl:variable name="recordID" select="//mods:recordInfo/mods:recordIdentifier"/>
    <xsl:variable name="persistentID" select="//mods:mods/mods:location/mods:url[@access='object in context']"/>
    <xsl:variable name="providedCHOID" select="concat($persistentID, '#1')"/>
    <xsl:variable name="aggregationID" select="concat($persistentID, '#aggr')"/>
    <xsl:variable name="contentProvider" select="//mods:recordInfo/mods:recordContentSource"/>
    <xsl:variable name="contentProviderURL" select="//mods:recordInfo/mods:recordInfoNote[@type='url']"/>
    <xsl:variable name="rights" select="//mods:mods/mods:accessCondition"/>
    <xsl:variable name="type" select="//mods:typeOfResource"/>

    <xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
    <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />


    <!-- Set variables for testing
    <xsl:variable name="recordID" select="//mods:recordInfo/mods:recordIdentifier"/>
    <xsl:variable name="persistentID" select="concat('http://hdl.handle.net/12345/', $recordID)"/>
    <xsl:variable name="providedCHOID" select="concat($persistentID, '#1')"/>
    <xsl:variable name="aggregationID" select="concat($persistentID, '#aggr')"/>
    <xsl:variable name="contentProvider" select="//mods:recordInfo/mods:recordContentSource"/>
    <xsl:variable name="contentProviderURL">http://www.socialhistoryportal.org</xsl:variable>
    <xsl:variable name="rights">http://www.europeana.eu/rights/rr-f/</xsl:variable>
    <xsl:variable name="type">TEXT</xsl:variable>
	-->

    <!-- Field template with language parameter / copied from HOPE2EDM-->
    <xsl:template name="langField">
        <xsl:param name="fieldName" />
        <xsl:param name="fieldValue" />
        <xsl:param name="language" />
        <xsl:choose>
            <xsl:when test="normalize-space($language) != ''">
                <xsl:element name="{$fieldName}">
                    <xsl:attribute name="xml:lang">
                        <xsl:value-of select="$language" />
                    </xsl:attribute>
                    <xsl:value-of select="$fieldValue" />
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="{$fieldName}">
                    <xsl:value-of select="$fieldValue" />
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

	<xsl:template match="/">
		<rdf:RDF>
			<xsl:call-template name="ProvidedCHO" />
			<xsl:call-template name="Aggregation" />
		</rdf:RDF>
	</xsl:template>

	<xsl:template name="ProvidedCHO">
		<edm:ProvidedCHO rdf:about="{$providedCHOID}">

            <!-- Language -->
            <xsl:for-each select="//mods:language">
                <dc:language><xsl:value-of select="normalize-space(.)"/></dc:language>
            </xsl:for-each>

			<!-- Book Number  -->
			<xsl:for-each select="//mods:identifier[@type='book number']">
				<dc:identifier><xsl:value-of select="normalize-space(.)"/></dc:identifier>
			</xsl:for-each>

            <!-- ISBN  -->
            <xsl:for-each select="//mods:identifier[@type='isbn']">
				<dc:identifier><xsl:value-of select="normalize-space(.)"/></dc:identifier>
			</xsl:for-each>

            <!-- ISSN  -->
            <xsl:for-each select="//mods:identifier[@type='issn']">
				<dc:identifier><xsl:value-of select="normalize-space(.)"/></dc:identifier>
			</xsl:for-each>

            <!-- Local ID  -->
            <xsl:for-each select="//mods:identifier[@type='local' and @displayLabel='isShownBy']">
				<dc:identifier><xsl:value-of select="normalize-space(.)"/></dc:identifier>
			</xsl:for-each>

            <!-- PID  -->
            <dc:identifier><xsl:value-of select="$persistentID"/></dc:identifier>

			<!-- Parent Record -->
			<xsl:if test="//mods:relatedItem[@type='constituent']">
				<dcterms:isPartOf>
					<xsl:value-of select="//mods:relatedItem[@type='constituent']"/>
				</dcterms:isPartOf>
			</xsl:if>

			<!-- Next Descriptive Unit -->
			<xsl:if test="//mods:relatedItem[@type='succeeding']">
				<edm:isNextInSequence>
					<xsl:value-of select="//mods:relatedItem[@type='succeeding']"/>
				</edm:isNextInSequence>
			</xsl:if>

			<!-- Title -->
			<xsl:for-each select="//mods:mods/mods:titleInfo[not(@type)]/mods:title">
				<dc:title><xsl:value-of select="normalize-space(.)"/></dc:title>
			</xsl:for-each>

			<!-- Alternative Title -->
            <xsl:for-each select="//mods:mods/mods:titleInfo[@type='alternative']/mods:title">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dcterms:alternative')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Translated Title -->
            <xsl:for-each select="//mods:mods/mods:titleInfo[@type='translated']/mods:title">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dcterms:alternative')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Subtitle -->
            <xsl:for-each select="//mods:mods/mods:titleInfo[not(@type)]/mods:subTitle">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dcterms:alternative')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Translated Subtitle -->
            <xsl:for-each select="//mods:mods/mods:titleInfo[@type='translated']/mods:subTitle">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dcterms:alternative')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Author / Contributor (Person) -->
            <xsl:for-each select="//mods:mods/mods:name[@type='personal']">
                <xsl:choose>
                    <xsl:when test="./mods:role/mods:roleTerm[@type='text'] = 'Creator' or ./mods:role/mods:roleTerm[@type='text'] = 'Author'">
                        <dc:creator>
                            <xsl:value-of select="./mods:namePart[not(@type)]"/>
                            <xsl:if test="./mods:namePart[@type='date']">
                                <xsl:text>, </xsl:text>
                                <xsl:value-of select="./mods:namePart[@type='date']"/>
                            </xsl:if>
                        </dc:creator>
                    </xsl:when>
                    <xsl:otherwise>
                        <dc:contributor>
                            <xsl:value-of select="./mods:namePart[not(@type)]"/>
                            <xsl:if test="./mods:namePart[@type='date']">
                                <xsl:text>, </xsl:text>
                                <xsl:value-of select="./mods:namePart[@type='date']"/>
                            </xsl:if>
                        </dc:contributor>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>

			<!-- Author / Contributor (Corporation) -->
            <xsl:for-each select="//mods:mods/mods:name[@type='corporation']">
                <xsl:choose>
                    <xsl:when test="./mods:role/mods:roleTerm[@type='text'] = 'Creator' or ./mods:role/mods:roleTerm[@type='text'] = 'Author' ">
                        <dc:creator>
                            <xsl:value-of select="./mods:namePart[not(@type)]"/>
                            <xsl:if test="./mods:namePart[@type='date']">
                                <xsl:text>, </xsl:text>
                                <xsl:value-of select="./mods:namePart[@type='date']"/>
                            </xsl:if>
                        </dc:creator>
                    </xsl:when>
                    <xsl:otherwise>
                        <dc:contributor>
                            <xsl:value-of select="./mods:namePart[not(@type)]"/>
                            <xsl:if test="./mods:namePart[@type='date']">
                                <xsl:text>, </xsl:text>
                                <xsl:value-of select="./mods:namePart[@type='date']"/>
                            </xsl:if>
                        </dc:contributor>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>

            <!-- Author / Contributor (Meeting) -->
            <xsl:for-each select="//mods:mods/mods:name[@type='conference']">
                <xsl:choose>
                    <xsl:when test="./mods:role/mods:roleTerm[@type='text'] = 'Creator' or ./mods:role/mods:roleTerm[@type='text'] = 'Author' ">
                        <dc:creator>
                            <xsl:value-of select="./mods:namePart[not(@type)]"/>
                            <xsl:if test="./mods:namePart[@type='date']">
                                <xsl:text>, </xsl:text>
                                <xsl:value-of select="./mods:namePart[@type='date']"/>
                            </xsl:if>
                        </dc:creator>
                    </xsl:when>
                    <xsl:otherwise>
                        <dc:contributor>
                            <xsl:value-of select="./mods:namePart[not(@type)]"/>
                            <xsl:if test="./mods:namePart[@type='date']">
                                <xsl:text>, </xsl:text>
                                <xsl:value-of select="./mods:namePart[@type='date']"/>
                            </xsl:if>
                        </dc:contributor>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>

            <!-- Edition Statement -->
            <xsl:for-each select="//mods:mods/mods:originInfo/mods:edition">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dc:description')" />
                    <xsl:with-param name="fieldValue" select="concat('Edition: ', .)" />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Statement of Responsibility -->
            <xsl:for-each select="//mods:mods/mods:note[@type='statement of responsibility']">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dc:description')" />
                    <xsl:with-param name="fieldValue" select="concat('Statement of Responsibility: ', .)" />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Statement of Responsibility relating to the edition -->
            <xsl:for-each select="//mods:mods/mods:note[@type='edition statement of responsibility']">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dc:description')" />
                    <xsl:with-param name="fieldValue" select="concat('Statement of Responsibility relating to the edition: ', .)" />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Publisher -->
            <xsl:for-each select="//mods:mods/mods:originInfo/mods:publisher">
                <dc:publisher>
                    <xsl:value-of select="normalize-space(.)"/>
                </dc:publisher>
            </xsl:for-each>

            <!-- Date Issued -->
            <xsl:for-each select="//mods:mods/mods:originInfo/mods:dateIssued">
                <dcterms:issued>
                    <xsl:value-of select="normalize-space(.)"/>
                </dcterms:issued>
            </xsl:for-each>

            <!-- Publisher / Printer, manufacturer, engraver -->
            <xsl:for-each select="//mods:mods/mods:originInfo/mods:publisher[@type='creator']">
                <dc:publisher>
                    <xsl:value-of select="normalize-space(.)"/>
                </dc:publisher>
            </xsl:for-each>

            <!-- Date Created / Date of printing, manufacture or engraving -->
            <xsl:for-each select="//mods:mods/mods:originInfo/mods:dateCreated">
                <dcterms:created>
                    <xsl:value-of select="normalize-space(.)"/>
                </dcterms:created>
            </xsl:for-each>

            <!-- Specific Material Designation -->
            <xsl:for-each select="//mods:mods/mods:physicalDescription/mods:form">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dcterms:medium')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Extent -->
            <xsl:for-each select="//mods:mods/mods:physicalDescription/mods:extent">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dcterms:extent')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Other Physical Details -->
            <xsl:for-each select="//mods:mods/mods:physicalDescription/mods:note[@type='other physical details']">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dc:description')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Dimensions -->
            <xsl:for-each select="//mods:mods/mods:physicalDescription/mods:dimensions">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dcterms:extent')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Abstract -->
            <xsl:for-each select="//mods:mods/mods:abstract">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dc:description')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Table of Contents -->
            <xsl:for-each select="//mods:mods/mods:tableOfContents">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dc:description')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Subject Person -->
            <xsl:for-each select="//mods:mods/mods:subject/mods:name[@type='personal']">
                <dc:subject>
                    <xsl:value-of select="./mods:namePart"/>
                    <xsl:if test="./mods:namePart[@type='date']">
                        <xsl:text>, </xsl:text>
                        <xsl:value-of select="./mods:namePart[@type='date']"/>
                    </xsl:if>
                </dc:subject>
            </xsl:for-each>

            <!-- Subject Corporation -->
            <xsl:for-each select="//mods:mods/mods:subject/mods:name[@type='corporation']">
                <dc:subject>
                    <xsl:value-of select="./mods:namePart"/>
                    <xsl:if test="./mods:namePart[@type='date']">
                        <xsl:text>, </xsl:text>
                        <xsl:value-of select="./mods:namePart[@type='date']"/>
                    </xsl:if>
                </dc:subject>
            </xsl:for-each>

            <!-- Subject Genre -->
            <xsl:for-each select="//mods:mods/mods:subject/mods:genre">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dc:subject')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Subject Topic -->
            <xsl:for-each select="//mods:mods/mods:subject/mods:topic">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dc:subject')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Subject Geographic -->
            <xsl:for-each select="//mods:mods/mods:subject/mods:geographic">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dc:coverage')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Subject Temporal -->
            <xsl:for-each select="//mods:mods/mods:subject/mods:temporal">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dcterms:temporal')" />
                    <xsl:with-param name="fieldValue" select="." />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Notes -->
            <xsl:for-each select="//mods:mods/mods:note">
                <xsl:call-template name="langField">
                    <xsl:with-param name="fieldName" select="string('dc:description')" />
                    <xsl:with-param name="fieldValue" select="concat('Note: ', .)" />
                    <xsl:with-param name="language" select="./@xml:lang" />
                </xsl:call-template>
            </xsl:for-each>

            <!-- Resource Type (Europeana Type / mandatory) -->
            <xsl:for-each select="//mods:typeOfResource">
                <edm:type>
                    <xsl:value-of select="$type"/>
                </edm:type>
            </xsl:for-each>

        </edm:ProvidedCHO>
	</xsl:template>

	<xsl:template name="Aggregation">
        <ore:Aggregation rdf:about="{$aggregationID}">
            <!-- Aggregated CHO -->
            <edm:aggregatedCHO>
                <xsl:attribute name="rdf:resource">
                    <xsl:value-of select="$providedCHOID"/>
                </xsl:attribute>
            </edm:aggregatedCHO>

            <!-- Data Provider -->
            <edm:dataProvider>
                <xsl:attribute name="rdf:resource">
                    <xsl:value-of select="$contentProviderURL"/>
                </xsl:attribute>
                <xsl:value-of select="$contentProvider"/>
            </edm:dataProvider>

            <!-- IsShownAt -->
            <edm:isShownAt>
                <xsl:attribute name="rdf:resource">
                    <xsl:value-of select="normalize-space($persistentID)"/>
                </xsl:attribute>
            </edm:isShownAt>

            <!-- IsShownBy -->
            <xsl:for-each select="//mods:mods/mods:location/mods:url[@access='raw object']">
                <edm:isShownBy>
                    <xsl:attribute name="rdf:resource">
                        <xsl:value-of select="normalize-space(.)"/>
                    </xsl:attribute>
                </edm:isShownBy>
            </xsl:for-each>

            <!-- Provider -->
            <edm:provider>HOPE - Heritage of the People's Europe</edm:provider>

            <!-- Rights -->
            <edm:rights>
                <xsl:attribute name="rdf:resource">
                    <xsl:value-of select="$rights"/>
                </xsl:attribute>
            </edm:rights>
        </ore:Aggregation>
    </xsl:template>
</xsl:stylesheet>
