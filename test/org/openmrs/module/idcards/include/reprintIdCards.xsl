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
                                    <fo:table-cell border="1pt dashed grey" display-align="center">
                                        <fo:block text-align="center" start-indent="0mm">
                                            <fo:block-container>
                                                <fo:block-container position="absolute">
                                                    <fo:block>
                                                        <fo:instream-foreign-object >
                                                            <barcode:barcode
                                                                xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                                                message="{$identifier}" orientation="0">
                                                                <barcode:code39>
                                                                    <barcode:height>20mm</barcode:height>
                                                                </barcode:code39>
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
                                                                        <fo:block text-align="center">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/card_logo_02.png"
                                                                                    scaling="uniform"
                                                                                    content-height="20mm"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell>
                                                                        <fo:block text-align="right">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/draft.png"
                                                                                    scaling="uniform"
                                                                                    content-width="15mm"/>
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
                                    <fo:table-cell border="1pt dashed grey" display-align="center">
                                        <fo:block text-align="center" start-indent="0mm">
                                            <fo:block-container>
                                                <fo:block-container position="absolute">
                                                    <fo:block>
                                                        <fo:table table-layout="fixed">
                                                            <fo:table-column column-width="100%"/>
                                                            <fo:table-body>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block font-weight="bold" font-size="large">
                                                                           <xsl:text>ID Card</xsl:text>
                                                                        </fo:block>
                                                                        <fo:block >
                                                                            <xsl:value-of select="$contextPath"/>
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
                                                                        <fo:block text-align="center">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/card_logo_02.png"
                                                                                    scaling="uniform"
                                                                                    content-height="20mm"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell>
                                                                        <fo:block text-align="right">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/draft.png"
                                                                                    scaling="uniform"
                                                                                    content-width="15mm"/>
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