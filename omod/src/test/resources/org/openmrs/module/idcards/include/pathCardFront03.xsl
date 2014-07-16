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
            margin-top="0.6cm" margin-left="0cm" margin-right="0cm" margin-bottom="0.8cm">
            <fo:region-body margin-top="0.4cm" margin-bottom="0cm" />
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simple">
        <fo:flow flow-name="xsl-region-body" font-family="Helvetica" font-size="8pt">
            <fo:block text-align="center">
                <fo:table table-layout="fixed" start-indent="(100% - 168mm - 3mm) div 2"
                        table-omit-header-at-break="false">
                    <fo:table-column column-width="84mm"/>
                    <fo:table-column column-width="84mm"/>

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
                            <fo:table-row height="52mm">
                                <xsl:for-each select="col">
                                    <xsl:variable name="id" select="."/>
                                    <fo:table-cell border="2pt solid black" display-align="before">
                                        <fo:block text-align="center" start-indent="0mm" margin-top="4mm" font-weight="bold">
                                            <fo:block-container>
                                                <fo:block-container position="absolute">
                                                    <fo:block>
                                                        <fo:table table-layout="fixed">
                                                            <fo:table-column column-width="100%"/>
                                                            <fo:table-body>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block color="black" font-weight="bold" >
                                                                            <fo:table table-layout="fixed">
                                                                                <fo:table-column column-width="10%"/>
                                                                                <fo:table-column column-width="30%"/>
                                                                                <fo:table-column column-width="25%"/>
                                                                                <fo:table-column column-width="25%"/>
                                                                                <fo:table-column column-width="10%"/>
                                                                                <fo:table-body>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-weight="bold"
                                                                                                    font-size="6pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="4.5mm"
                                                                                                    border-top-color="black"
                                                                                                    border-top-style="dotted"
                                                                                                    border-top-width="1pt">
                                                                                                    First Name
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-weight="bold"
                                                                                                    font-size="6pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="4.5mm"
                                                                                                    border-top-color="black"
                                                                                                    border-top-style="dotted"
                                                                                                    border-top-width="1pt">
                                                                                                    Middle Name
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-weight="bold"
                                                                                                    font-size="6pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="4.5mm"
                                                                                                    border-top-color="black"
                                                                                                    border-top-style="dotted"
                                                                                                    border-top-width="1pt">
                                                                                                    Family Name
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                    </fo:table-row>
                                                                                </fo:table-body>
                                                                            </fo:table>
                                                                        </fo:block>
                                                                        <fo:block color="black" font-weight="bold" >
                                                                            <fo:table table-layout="fixed">
                                                                                <fo:table-column column-width="10%"/>
                                                                                <fo:table-column column-width="55%"/>
                                                                                <fo:table-column column-width="25%"/>
                                                                                <fo:table-column column-width="10%"/>
                                                                                <fo:table-body>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-weight="bold"
                                                                                                    font-size="6pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="4.5mm">
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-size="10pt"
                                                                                                      font-weight="bold"
                                                                                                      display-align="after"
                                                                                                      margin-top="1mm">
                                                                                                <fo:table table-layout="fixed">
                                                                                                    <fo:table-column column-width="50%"/>
                                                                                                    <fo:table-column column-width="50%"/>
                                                                                                    <fo:table-body color="black">
                                                                                                        <fo:table-row>
                                                                                                            <fo:table-cell>
                                                                                                                <fo:block text-align="center">M</fo:block>
                                                                                                            </fo:table-cell>
                                                                                                            <fo:table-cell>
                                                                                                                <fo:block text-align="left">F</fo:block>
                                                                                                            </fo:table-cell>
                                                                                                        </fo:table-row>
                                                                                                    </fo:table-body>
                                                                                                </fo:table>
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                    </fo:table-row>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-weight="bold"
                                                                                                    font-size="6pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="0mm"
                                                                                                    border-top-color="black"
                                                                                                    border-top-style="dotted"
                                                                                                    border-top-width="1pt">
                                                                                                    Birthdate (use ? for unknown day or month)
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-weight="bold"
                                                                                                    font-size="6pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="0mm"
                                                                                                    border-top-color="black"
                                                                                                    border-top-style="dotted"
                                                                                                    border-top-width="1pt">
                                                                                                    Gender (circle one)
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                    </fo:table-row>
                                                                                </fo:table-body>
                                                                            </fo:table>
                                                                        </fo:block>
                                                                        <fo:block color="black" font-weight="bold">
                                                                            <fo:table table-layout="fixed">
                                                                                <fo:table-column column-width="10%"/>
                                                                                <fo:table-column column-width="80%"/>
                                                                                <fo:table-column column-width="10%"/>
                                                                                <fo:table-body>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-weight="bold"
                                                                                                    font-size="6pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="4.5mm"
                                                                                                    border-top-color="black"
                                                                                                    border-top-style="dotted"
                                                                                                    border-top-width="1pt">
                                                                                                    Location
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                    </fo:table-row>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-weight="bold"
                                                                                                    font-size="6pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="4.5mm"
                                                                                                    border-top-color="black"
                                                                                                    border-top-style="dotted"
                                                                                                    border-top-width="1pt">
                                                                                                    Sublocation
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                    </fo:table-row>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-weight="bold"
                                                                                                    font-size="6pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="4.5mm"
                                                                                                    border-top-color="black"
                                                                                                    border-top-style="dotted"
                                                                                                    border-top-width="1pt">
                                                                                                    Mobile Phone
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell><fo:block/></fo:table-cell>
                                                                                    </fo:table-row>
                                                                                    <fo:table-row>
                                                                                        <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                                                                        <fo:table-cell>
                                                                                            <fo:block font-style="italic"
                                                                                                    font-weight="bold"
                                                                                                    font-size="6pt"
                                                                                                    text-align="left"
                                                                                                    display-align="after"
                                                                                                    margin-top="4.5mm"
                                                                                                    border-top-color="black"
                                                                                                    border-top-style="dotted"
                                                                                                    border-top-width="1pt">
                                                                                                    Previous Medical Identifier(s)
                                                                                            </fo:block>
                                                                                        </fo:table-cell>
                                                                                        <fo:table-cell><fo:block></fo:block></fo:table-cell>
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