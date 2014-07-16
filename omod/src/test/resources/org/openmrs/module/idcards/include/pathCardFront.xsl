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
            margin-top="0.6cm" margin-left="0.9cm" margin-right="1cm" margin-bottom="0.8cm">
            <fo:region-body margin-top="0.6cm" margin-bottom="0.8cm" />
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simple">
        <fo:flow flow-name="xsl-region-body" font-family="Helvetica" font-size="8pt">
            <fo:block text-align="center">
                <fo:table table-layout="fixed" start-indent="(100% - 160mm) div 2"
                        table-omit-header-at-break="false">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>

                    <fo:table-header border="solid white" >
                        <fo:table-row height="6mm" >
                            <fo:table-cell>
                                <fo:block font-size="6pt" font-weight="bold" display-align="before">
                                    DRAFT <xsl:value-of select="doc/cardName"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block font-size="6pt" font-weight="bold">
                                    Page: <fo:page-number/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-header>

                    <fo:table-body>
                        <xsl:variable name="baseURL" select="doc/baseURL"/>
                        <!-- xsl:variable name="contextPath" select="idCardList/contextPath" -->
                        <xsl:for-each select="doc/page/row">
                            <fo:table-row height="50mm">
                                <xsl:for-each select="col">
                                    <xsl:variable name="id" select="."/>
                                    <fo:table-cell border="2pt solid black" display-align="before">
                                        <fo:block text-align="center" start-indent="0mm" margin-top="5mm">
                                            <fo:block-container>
                                                <fo:block-container position="absolute">
                                                    <fo:block>
                                                        <fo:table table-layout="fixed">
                                                            <fo:table-column column-width="100%"/>
                                                            <fo:table-body>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block text-align="center" display-align="before">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/card_logo_05.png"
                                                                                    scaling="uniform"
                                                                                    content-height="12mm"/>
                                                                        </fo:block>
                                                                        <fo:block>
                                                                            <fo:table table-layout="fixed">
                                                                                <fo:table-column column-width="10%"/>
                                                                                <fo:table-column column-width="80%"/>
                                                                                <fo:table-column column-width="10%"/>
                                                                                <fo:table-body>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-size="4pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="5mm"
                                                                                                    border-bottom-color="black"
                                                                                                    border-bottom-style="dotted"
                                                                                                    border-bottom-width="1pt">
                                                                                                    Name:
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                    </fo:table-row>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-size="4pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="4mm"
                                                                                                    margin-bottom="1mm"
                                                                                                    border-bottom-color="black"
                                                                                                    border-bottom-style="dotted"
                                                                                                    border-bottom-width="1pt">
                                                                                                    Patient Identifier:
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                    </fo:table-row>
                                                                                </fo:table-body>
                                                                            </fo:table>
                                                                        </fo:block>
                                                                        <fo:block font-size="8pt">
                                                                            <fo:table table-layout="fixed">
                                                                                <fo:table-column column-width="30%" />
                                                                                <fo:table-column column-width="30%" />
                                                                                <fo:table-column column-width="20%" />
                                                                                <fo:table-column column-width="10%" />
                                                                                <fo:table-column column-width="10%" />
                                                                                <fo:table-body>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell>
                                                                                            <fo:block text-align="right"
                                                                                                    font-style="italic">
                                                                                                Birthdate:
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block text-align="left"
                                                                                                    margin-left="2mm"
                                                                                                    margin-top="2.5mm"
                                                                                                    border-bottom-color="black"
                                                                                                    border-bottom-style="dotted"
                                                                                                    border-bottom-width="1pt">
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block text-align="right"
                                                                                                    font-style="italic">
                                                                                                Gender:
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell text-align="center">
                                                                                            <fo:block>M / F</fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block >
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                    </fo:table-row>
                                                                                </fo:table-body>
                                                                            </fo:table>
                                                                        </fo:block>
                                                                        <fo:block font-size="8pt">
                                                                            <fo:table table-layout="fixed">
                                                                                <fo:table-column column-width="30%"/>
                                                                                <fo:table-column column-width="60%"/>
                                                                                <fo:table-column column-width="10%"/>
                                                                                <fo:table-body>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell>
                                                                                            <fo:block text-align="right"
                                                                                                    font-style="italic">
                                                                                                Address:
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block text-align="left"
                                                                                                      margin-left="2mm"
                                                                                                      margin-top="2.5mm"
                                                                                                      border-bottom-color="black"
                                                                                                      border-bottom-style="dotted"
                                                                                                      border-bottom-width="1pt">
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                    </fo:table-row>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell>
                                                                                            <fo:block></fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block text-align="left"
                                                                                                      margin-left="2mm"
                                                                                                      margin-top="2.5mm"
                                                                                                      border-bottom-color="black"
                                                                                                      border-bottom-style="dotted"
                                                                                                      border-bottom-width="1pt">
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                    </fo:table-row>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell>
                                                                                            <fo:block text-align="right"
                                                                                                    font-style="italic">
                                                                                                Mobile Phone:
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block text-align="left"
                                                                                                      margin-left="2mm"
                                                                                                      margin-top="2.5mm"
                                                                                                      border-bottom-color="black"
                                                                                                      border-bottom-style="dotted"
                                                                                                      border-bottom-width="1pt">
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                    </fo:table-row>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell>
                                                                                            <fo:block text-align="right"
                                                                                                    font-style="italic">
                                                                                                Alternate Id's:
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block text-align="left"
                                                                                                      margin-left="2mm"
                                                                                                      margin-top="2.5mm"
                                                                                                      border-bottom-color="black"
                                                                                                      border-bottom-style="dotted"
                                                                                                      border-bottom-width="1pt">
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                    </fo:table-row>
                                                                                </fo:table-body>
                                                                            </fo:table>
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
                                </xsl:for-each>
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