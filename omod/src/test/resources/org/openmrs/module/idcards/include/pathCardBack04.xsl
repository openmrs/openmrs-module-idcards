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
                        <xsl:variable name="lostAndFound" select="doc/lostAndFound"/>
                        <!-- xsl:variable name="contextPath" select="idCardList/contextPath" -->
                        <xsl:variable name="padding" select="'000000000'"/>
                        <xsl:for-each select="doc/page/row">
                            <fo:table-row height="52mm">
                                <xsl:for-each select="col">
                                    <xsl:variable name="id" select="."/>
                                    <xsl:variable name="identifier" select="substring(concat($padding, $id), string-length($id) - 1, string-length($padding) + 2)" />
                                    <fo:table-cell border="2pt solid black" display-align="before">
                                        <fo:block text-align="center" start-indent="0mm" margin-top="0mm">
                                            <fo:block-container>
                                                <fo:block-container position="absolute">
                                                    <fo:block font-size="6pt" font-weight="bold">
                                                        <fo:table table-layout="fixed" >
                                                            <fo:table-column column-width="40%"/>
                                                            <fo:table-column column-width="20%"/>
                                                            <fo:table-column column-width="40%"/>
                                                            <fo:table-body>
                                                                <fo:table-row height="6mm" >
                                                                    <fo:table-cell><fo:block/></fo:table-cell>
                                                                </fo:table-row>
                                                                <fo:table-row >
                                                                    <fo:table-cell color="black"
                                                                            font-weight="bold"
                                                                            font-size="6pt"
                                                                            font-style="italic"
                                                                            border-top-color="black"
                                                                            border-top-style="dotted"
                                                                            border-top-width="1pt">
                                                                        <fo:block>Date Issued</fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell >
                                                                            <fo:block></fo:block>
                                                                        </fo:table-cell>
                                                                    <fo:table-cell color="black"
                                                                            font-weight="bold"
                                                                            font-size="6pt"
                                                                            font-style="italic"
                                                                            border-top-color="black"
                                                                            border-top-style="dotted"
                                                                            border-top-width="1pt">
                                                                        <fo:block>Assigning Location</fo:block>
                                                                    </fo:table-cell>
                                                                 </fo:table-row>
                                                            </fo:table-body>
                                                        </fo:table>
                                                    </fo:block>
                                                    <fo:block>
                                                        <fo:table table-layout="fixed">
                                                            <fo:table-column column-width="25%"/>
                                                            <fo:table-column column-width="50%"/>
                                                            <fo:table-column column-width="25%"/>
                                                            <fo:table-body>
                                                                <fo:table-row>
                                                                    <fo:table-cell><fo:block/></fo:table-cell>
                                                                    <fo:table-cell>
                                                                        <fo:block text-align="center" display-align="after" margin-top="0mm" margin-bottom="0mm">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/card_logo_06.png"
                                                                                    scaling="uniform"
                                                                                    content-height="8mm"
                                                                                    content-width="scale-to-fit"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell>
                                                                        <fo:block text-align="center" display-align="after"
                                                                                  margin-top="0mm" margin-bottom="0mm" margin-left="0mm">
                                                                            <fo:external-graphic
                                                                                    src="test/org/openmrs/module/idcards/include/mtrh_logo.png"
                                                                                    scaling="uniform"
                                                                                    content-height="8mm"
                                                                                    content-width="scale-to-fit"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                            </fo:table-body>
                                                        </fo:table>
                                                    </fo:block>
                                                    <fo:block margin-top="0mm" margin-bottom="1mm">
                                                        <fo:instream-foreign-object >
                                                            <barcode:barcode
                                                                xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                                                message="{$identifier}" orientation="0">
                                                                <barcode:code39>
                                                                    <barcode:height>24mm</barcode:height>
                                                                    <barcode:quiet-zone enabled="false"></barcode:quiet-zone>
                                                                    <barcode:checksum>ignore</barcode:checksum>
                                                                    <barcode:module-width>0.30mm</barcode:module-width>
                                                                    <barcode:human-readable>
                                                                      <barcode:placement>bottom</barcode:placement>
                                                                      <barcode:font-size>12pt</barcode:font-size>
                                                                    </barcode:human-readable>
                                                                </barcode:code39>
                                                                <barcode:quiet-zone enabled="false"/>
                                                            </barcode:barcode>
                                                        </fo:instream-foreign-object>
                                                    </fo:block>
                                                    <fo:block font-size="6pt" font-weight="bold">
                                                        <xsl:value-of select="$lostAndFound"/>
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