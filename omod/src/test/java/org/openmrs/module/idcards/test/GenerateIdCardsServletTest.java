package org.openmrs.module.idcards.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.idcards.IdcardsExportFunctions;
import org.openmrs.module.idcards.IdcardsUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.OpenmrsUtil;

/**
 * This class tests the use of FOP to create a pdf file from an xslt and an xml.
 * Files for testing are in test.org.openmrs.module.idcards.include.*
 * The output pdf file should be in build/fop_test.pdf
 *
 */
public class GenerateIdCardsServletTest extends BaseModuleContextSensitiveTest {

    @Before
    public void runBeforeEachTest() throws Exception {
        authenticate();
    }

    /**
     * Do not use the in memory database.
     * @return
     */
    public Boolean useInMemoryDatabase() {
        return false;
    }


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
            ////File xslFile = new File("test/org/openmrs/module/idcards/include/newIdCardsBack.xsl");
            ////File xmlFile = new File("test/org/openmrs/module/idcards/include/identifiers.xml");
            ////File xmlFile = new File("test/org/openmrs/module/idcards/include/identifier.xml");

            //File xslFile = new File("test/org/openmrs/module/idcards/include/reprintIdCards05.xsl");
            //File xmlFile = new File("test/org/openmrs/module/idcards/include/reprintIdCards.xml");

            File xslFile = new File("test/org/openmrs/module/idcards/include/pathCardBack04.xsl");
            //File xslFile = new File("test/org/openmrs/module/idcards/include/pathCardFront03.xsl");
            File xmlFile = new File("test/org/openmrs/module/idcards/include/pathCardBack.xml");

            String xslt = OpenmrsUtil.getFileAsString(xslFile);
            String xml = OpenmrsUtil.getFileAsString(xmlFile);

            ArrayList<String> identifiers = new ArrayList<String>();
            for (int i=1; i<11; i++) {
                Integer idNumber = Integer.valueOf(i);
                int checkdigit;
                try {
                    checkdigit = IdcardsUtil.getCheckDigit(idNumber.toString());
                }
                catch (Exception e) {
                    throw new ServletException(e);
                }
                String identifier = idNumber.toString() + "-" + checkdigit;
                identifiers.add(identifier);
            }

            Cohort cohort = new Cohort();
            /*
            cohort.addMember(9);
            cohort.addMember(11);
            cohort.addMember(12);
            cohort.addMember(13);
            cohort.addMember(14);
            cohort.addMember(15);
            cohort.addMember(16);
            cohort.addMember(17);
            */
            cohort.addMember(10);
            cohort.addMember(18);
            cohort.addMember(19);
            cohort.addMember(20);
            cohort.addMember(369);
            cohort.addMember(364);
            IdcardsExportFunctions functions = new IdcardsExportFunctions();

            //IdcardsTemplate card = getIdcardsService().getIdcardsTemplate(Integer.valueOf(2));

            Properties props = new Properties();
            props.setProperty(RuntimeConstants.RESOURCE_LOADER,"class");
            props.setProperty("class.resource.loader.description","VelocityClasspathResourceLoader");
            props.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");

            //VelocityEngine engine = new VelocityEngine();
            Velocity.init(props);
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("locale", Context.getLocale());
            velocityContext.put("identifiers", identifiers);
            velocityContext.put("fn", functions);
            velocityContext.put("cohort", cohort);
            velocityContext.put("baseURL", "http://localhost:8080/openmrs");
            Writer writer = new StringWriter();
            Velocity.evaluate(velocityContext, writer, this.getClass().getName(), xml);

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
            Source src = new StreamSource(new StringReader(writer.toString()));

            //Start the transformation and rendering process
            transformer.transform(src, res);

            byte[] outBytes = out.toByteArray();

            // Get the output file.
            File file = new File("out/fop_test.pdf");
            File fileXml = new File("out/fop_test.xml");

            try {
                FileOutputStream outStream = new FileOutputStream(file);
                outStream.write(outBytes);
                outStream.flush();
                outStream.close();
                FileWriter fwriter = new FileWriter(fileXml);
                fwriter.write(writer.toString());
                fwriter.flush();
                fwriter.close();
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

    /**
     * This is a test for formatting the xml within the servlet.
     * It does not test anything; it's only here to avoid having to
     * reload the module while trying to format the xml.
     * @see org.openmrs.module.idcards.web.servlet.GenerateIdcardsServlet
     */
    //@Test
    public void shouldPrintCorrectXmlFormatForIdentifiers() {
        Integer COLS_PER_ROW = 2;
        Integer ROWS_PER_PAGE = 8;
        String NEWLINE = System.getProperty("line.separator");
        String TAB = "  ";
        Integer column = 1, end = 30;
        Double page = 0.0, row = 0.0;//, col = 0.0;
        while (column < end) {

            int checkdigit = 0;
            String line = new String();
            String identifier = NEWLINE + TAB + TAB + "<col>" + column.toString() + "-" + checkdigit
                    + "</col>";

            // first page, row, column.
            if (page == 0 && row == 0) {
                line = "<page name='" + page + "'>" + NEWLINE + TAB + "<row name='" + row + "'>";
                row++;
                page++;
                line = line + identifier;
            }
            else {
                // new page and row.
                if (row % ROWS_PER_PAGE == 0 && column % COLS_PER_ROW == 0) {
                    line = NEWLINE + TAB + "</row>" + NEWLINE + "</page>"
                        + NEWLINE + "<page name='" + page + "'>" + NEWLINE + TAB + "<row name='" + row + "'>";
                    row++;
                    page++;
                }
                // new row.
                else if (column % COLS_PER_ROW == 0) {
                    line = NEWLINE + TAB + "</row>" + NEWLINE + TAB + "<row name='" + row + "'>";
                    row++;
                }
                line = identifier + line;
            }
            column++;
            // last column, row, page.
            if (column == end) {
                line = line + NEWLINE + TAB + "</row>" + NEWLINE + "</page>";
            }
            Double rMod8 = row % 8;
            Double pMod2 = page % 2;
            System.out.print(line);
            //System.out.println(line + "row%8: " + rMod8 + " page%2: " + pMod2);
        }
    }

}