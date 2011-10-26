<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      xmlns:fo="http://www.w3.org/1999/XSL/Format"
      xmlns:fn="http://www.w3.org/2005/02/xpath-functions">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/">
    <fo:root>
      <fo:layout-master-set>
            <fo:simple-page-master master-name="simple" page-height="297mm" page-width="210mm"
                margin-top="1cm" margin-left="1cm" margin-right="1cm" margin-bottom="1cm">
                <fo:region-body margin-top="0.8cm" margin-bottom="1.2cm" />
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simple">
        <fo:flow flow-name="xsl-region-body" font-family="Helvetica" font-size="8pt">
            <fo:block text-align="center">
                <fo:table table-layout="fixed" start-indent="(100% - 160mm) div 2"
                        table-omit-header-at-break="false" border="2 pt solid black">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>

                    <fo:table-header border="solid white">
                        <fo:table-row height="2mm" >
                            <fo:table-cell>
                                <fo:block font-size="6pt" font-weight="bold">
                                    DRAFT Patient ID Cards
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block font-size="6pt" font-weight="bold">
                                    Page: <fo:page-number/>
                                    Context: <xsl:value-of select="idCardList/contextPath"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-header>

                    <fo:table-body>
                        <xsl:variable name="contextPath" select="idCardList/contextPath"/>
                        <xsl:for-each select="idCardList/idCard">
                            <fo:table-row height="50mm" border="2pt solid black">
                                    <xsl:variable name="identifier" select="./id"/>
                                    <fo:table-cell border="1pt dashed grey" display-align="before">
                                        <fo:block text-align="center" start-indent="0mm" margin-top="12mm">
                                            <fo:block-container>
                                                <fo:block-container position="absolute">
                                                    <fo:block>
                                                        <fo:instream-foreign-object >
                                                            <barcode:barcode
                                                                xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                                                message="{$identifier}" orientation="0">
                                                                <barcode:code39>
                                                                    <barcode:height>24mm</barcode:height>
                                                                    <barcode:human-readable>
                                                                      <barcode:placement>bottom</barcode:placement>
                                                                      <barcode:font-size>12pt</barcode:font-size>
                                                                    </barcode:human-readable>
                                                                </barcode:code39>
                                                                <barcode:quiet-zone enabled="false"/>
                                                            </barcode:barcode>
                                                        </fo:instream-foreign-object>
                                                    </fo:block>
                                                </fo:block-container>
                                                <fo:block-container position="relative">
                                                    <fo:block font-weight="normal"
                                                              text-align="left"
                                                              display-align="center"
                                                              color="blue"
                                                              font-size="10mm">
                                                        <fo:table table-layout="fixed">
                                                            <fo:table-column column-width="25mm"/>
                                                            <fo:table-column column-width="45mm"/>
                                                            <fo:table-column column-width="20mm"/>
                                                            <fo:table-body>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block margin-top="5mm"></fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block text-align="center">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/card_logo_02.png"
                                                                                    scaling="uniform"
                                                                                    content-height="10mm"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell>
                                                                        <fo:block text-align="right">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/draft.png"
                                                                                    scaling="uniform"
                                                                                    content-width="10mm"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell>
                                                                        <fo:block>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                            </fo:table-body>
                                                        </fo:table>
                                                    </fo:block>
                                                </fo:block-container>
                                            </fo:block-container>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="1pt dashed grey" display-align="before">
                                        <fo:block text-align="center" start-indent="0mm" margin-top="8mm">
                                            <fo:block-container>
                                                <fo:block-container position="absolute">
                                                    <fo:block>
                                                        <fo:table table-layout="fixed">
                                                            <fo:table-column column-width="100%"/>
                                                            <fo:table-body>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block font-weight="bold" font-size="10pt">
                                                                           <xsl:text>PATH Card</xsl:text>
                                                                        </fo:block>
                                                                        <fo:block font-weight="bold" font-size="12pt">
                                                                            <xsl:value-of select="name"/>
                                                                        </fo:block>
                                                                        <fo:block>
                                                                            <xsl:value-of select="id"/>
                                                                        </fo:block>
                                                                        <fo:block>
                                                                          <xsl:value-of select="gender"/>
                                                                        </fo:block>
                                                                        <fo:block>
                                                                          <xsl:value-of select="birthdate"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                                <fo:table-row>
                                                                    <fo:table-cell text-align="center">
                                                                        <fo:block>
                                                                            <xsl:value-of select="subLocation"/>, <xsl:value-of select="location"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                                <fo:table-row>
                                                                    <fo:table-cell text-align="center">
                                                                        <fo:block>
                                                                            Mobile Phone: <xsl:value-of select="phone"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                                <fo:table-row>
                                                                    <fo:table-cell text-align="center">
                                                                        <fo:block font-size="6pt">
                                                                            Alternate Id's: 123-0, 456-0, 78910-1
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                            </fo:table-body>
                                                        </fo:table>
                                                    </fo:block>
                                                </fo:block-container>
                                                <fo:block-container position="relative">
                                                    <fo:block font-weight="normal"
                                                              text-align="left"
                                                              display-align="center"
                                                              color="blue"
                                                              font-size="10mm">
                                                        <fo:table table-layout="fixed">
                                                            <fo:table-column column-width="25mm"/>
                                                            <fo:table-column column-width="45mm"/>
                                                            <fo:table-column column-width="20mm"/>
                                                            <fo:table-body>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block margin-top="10mm"></fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block text-align="center">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/card_logo_02.png"
                                                                                    scaling="uniform"
                                                                                    content-height="10mm"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell>
                                                                        <fo:block text-align="right">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/draft.png"
                                                                                    scaling="uniform"
                                                                                    content-width="10mm"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell>
                                                                        <fo:block>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                            </fo:table-body>
                                                        </fo:table>
                                                    </fo:block>
                                                </fo:block-container>
                                            </fo:block-container>
                                        </fo:block>
                                    </fo:table-cell>
                            </fo:table-row>
                        </xsl:for-each>
                    </fo:table-body>
                </fo:table>
            </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
</xsl:stylesheet>