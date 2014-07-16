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
                        table-omit-header-at-break="false">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>

                    <fo:table-header>
                        <fo:table-row height="2mm" >
                            <fo:table-cell>
                                <fo:block font-size="6pt" font-weight="bold">
                                    DRAFT Patient ID Cards
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
                        <xsl:for-each select="doc/page/row">
                            <fo:table-row height="42mm">
                                <xsl:for-each select="col">
                                    <xsl:variable name="identifier" select="."/>
                                    <fo:table-cell border="solid" display-align="center">
                                        <fo:block text-align="center" start-indent="0mm">
                                            <fo:block-container>
                                                <fo:block-container position="absolute">
                                                    <fo:block>
                                                        <fo:instream-foreign-object >
                                                            <barcode:barcode
                                                                xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                                                message="{$identifier}" orientation="0">
                                                                <barcode:code128>
                                                                    <barcode:height>20mm</barcode:height>
                                                                </barcode:code128>
                                                            </barcode:barcode>
                                                        </fo:instream-foreign-object>
                                                    </fo:block>
                                                </fo:block-container>
                                                <fo:block-container position="relative">
                                                    <fo:block font-weight="normal"
                                                              text-align="center"
                                                              display-align="center"
                                                              color="blue"
                                                              font-size="10mm">
                                                        Code 39
                                                        <!--
                                                        <fo:external-graphic
                                                                src="test/org/openmrs/module/idcards/include/draft.png"
                                                                scaling="uniform"
                                                                content-height="10mm"/>
                                                        -->
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
<!--
<barcode message="123456789">
  <code39>
    <height>15mm</height>
    <module-width>0.19mm</module-width>
    <wide-factor>2.5</wide-factor>
    <interchar-gap-width>1mw</interchar-gap-width>
    <quiet-zone enabled="true">10mw</quiet-zone>
    <checksum>auto</checksum>
    <human-readable>
      <placement>bottom</placement>
      <font-name>Helvetica</font-name>
      <font-size>8pt</font-size>
      <pattern>{string}</pattern>
      <display-start-stop>false</display-start-stop>
      <display-checksum>false</display-checksum>
    </human-readable>
  </code39>
</barcode>
-->
  </xsl:template>
</xsl:stylesheet>
