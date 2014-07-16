package org.openmrs.module.idcards.test;

import org.apache.fop.apps.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.Before;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.reporting.export.DataExportFunctions;
import org.openmrs.Cohort;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * This class tests the use of FOP to create a pdf file from an xslt and an xml.
 * Files for testing are in test.org.openmrs.module.idcards.include.*
 * The output pdf file should be in build/fop_test.pdf
 *
 */
public class FopTest {

    /**
     * Should successfully use FOP to create a pdf from an xslt and an xml.
     * The pdf is placed in the build directory.
     * This snippet was taken from the clinical summary module.
     * This test fails on any exception in the process.
     */
    @Test
    public void shouldSuccessfullyUseFopToCreateAPdfFromAnXsltAndAnXml( ) {
        FopFactory fopFactory = FopFactory.newInstance();
        TransformerFactory tFactory = TransformerFactory.newInstance();
        try {
            // Uncomment a pair of xslFile and xmlFile to test them.
            File xslFile = new File("test/org/openmrs/module/idcards/include/newIdCardsBack.xsl");
            File xmlFile = new File("test/org/openmrs/module/idcards/include/identifiers.xml");

            //File xslFile = new File("test/org/openmrs/module/idcards/include/reprintIdCards.xsl");
            //File xmlFile = new File("test/org/openmrs/module/idcards/include/reprintIdCards.xml");

            String xslt = OpenmrsUtil.getFileAsString(xslFile);
            String xml = OpenmrsUtil.getFileAsString(xmlFile);

            //Setup a buffer to obtain the content length
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired (this will let us set creator, etc.)

            //Setup FOP
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            //Setup Transformer
            //Source xsltSrc = new StreamSource(new StringReader(this.getXslt()));
            Source xsltSrc = new StreamSource(new StringReader(xslt));
            Transformer transformer = tFactory.newTransformer(xsltSrc);

            //Make sure the XSL transformation's result is piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            //Setup input
            Source src = new StreamSource(new StringReader(xml));

            //Start the transformation and rendering process
            transformer.transform(src, res);

            byte[] outBytes = out.toByteArray();

            // Get the output file.
            File file = new File("out/fop_test.pdf");

            try {
                FileOutputStream outStream = new FileOutputStream(file);
                outStream.write(outBytes);
                outStream.flush();
                outStream.close();
            } catch (IOException io) {
                org.junit.Assert.fail(io.getMessage());
                System.out.println("Unable to write output file: " + io.getMessage());
            }

        } catch (IOException e) {
            org.junit.Assert.fail(e.getMessage());
            throw new APIException("Cannot get xsl file. ", e);
        } catch (FOPException e) {
            org.junit.Assert.fail(e.getMessage());
            throw new APIException("Error generating report", e);
        } catch (TransformerConfigurationException e) {
            org.junit.Assert.fail(e.getMessage());
            throw new APIException("Error generating report", e);
        } catch (TransformerException e) {
            org.junit.Assert.fail(e.getMessage());
            throw new APIException("Error generating report", e);
        } catch (Exception e) {
            org.junit.Assert.fail(e.getMessage());
        }

    }

}