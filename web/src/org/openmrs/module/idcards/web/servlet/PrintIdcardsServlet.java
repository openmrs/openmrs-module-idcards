package org.openmrs.module.idcards.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.openmrs.Cohort;
import org.openmrs.Patient;
import org.openmrs.api.PatientSetService;
import org.openmrs.api.context.Context;
import org.openmrs.module.idcards.IdcardsExportFunctions;
import org.openmrs.module.idcards.IdcardsService;
import org.openmrs.module.idcards.IdcardsTemplate;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * This servlet prints id cards for a given set of patients. The current primary identifier that
 * those patients have is used as the identifier on the card.
 */
public class PrintIdcardsServlet extends HttpServlet {
	
	public static final long serialVersionUID = 123423L;
	
	private static Log log = LogFactory.getLog(PrintIdcardsServlet.class);
	
	private static IdcardsService getIdcardsService() {
		return (IdcardsService) Context.getService(IdcardsService.class);
	}
	
	/**
	 * Reprint Id Cards.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// get the patient set
		Integer locationId = ServletRequestUtils.getIntParameter(request, "locationId", 0);
		Integer patientId = ServletRequestUtils.getIntParameter(request, "patientId", 0);
		String patientIdsString = ServletRequestUtils.getStringParameter(request, "patientIds", "");
		String patientIdentifiersString = ServletRequestUtils.getStringParameter(request, "patientIdentifiers", "");
		Integer cohortId = ServletRequestUtils.getIntParameter(request, "cohortId", -1);
		String cohortDefinition = ServletRequestUtils.getStringParameter(request, "cohortDefinitionId", null);
		Integer cardTemplateId = ServletRequestUtils.getIntParameter(request, "cardTemplateId", 1);
		
		Cohort cohort = new Cohort();
		PatientSetService pss = Context.getPatientSetService();
		if (locationId != 0)
			cohort = pss.getPatientsHavingLocation(locationId);
		else if (patientId != 0)
			cohort.addMember(patientId);
		else if (patientIdsString.length() > 0) {
			String[] idArray = patientIdsString.split(",");
			List<Integer> patientIds = new ArrayList<Integer>(idArray.length);
			for (int i = 0; i < idArray.length; i++) {
				patientIds.add(Integer.valueOf(idArray[i].trim()));
			}
			List<Patient> pts = getIdcardsService().getPatientsByPatientId(patientIds);
			for (Patient p : pts) {
				cohort.addMember(p.getPatientId());
			}
		} else if (patientIdentifiersString.length() > 0) {
			// find and add patients by identifier
			String[] idArray = patientIdentifiersString.split(",");
			List<String> identifiers = new ArrayList<String>(idArray.length);
			for (int i = 0; i < idArray.length; i++) {
				identifiers.add(idArray[i].trim());
			}
			List<Patient> pts = getIdcardsService().getPatientsByIdentifier(identifiers);
			for (Patient p : pts) {
				cohort.addMember(p.getPatientId());
			}
		} else if (cohortId != -1) {
			cohort = Context.getCohortService().getCohort(cohortId);
		}
		
		IdcardsTemplate card = getIdcardsService().getIdcardsTemplate(cardTemplateId);
		
		StringBuffer requestURL = request.getRequestURL();
		String baseURL = requestURL.substring(0, requestURL.indexOf("/moduleServlet"));
		
		generateOutput(card, baseURL, cohort, response);
		
	}
	
	/**
	 * Writes the pdf to the given response
	 * 
	 * @param card
	 * @param baseURL
	 * @param cohort
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void generateOutput(IdcardsTemplate card, String baseURL, Cohort cohort, HttpServletResponse response)
	                                                                                                                    throws ServletException,
	                                                                                                                    IOException {
		IdcardsExportFunctions functions = new IdcardsExportFunctions();
		Properties props = new Properties();
		props.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
		props.setProperty("class.resource.loader.description", "VelocityClasspathResourceLoader");
		props.setProperty("class.resource.loader.class",
		    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");
		
		Writer writer = new StringWriter();
		try {
			// Allow images to be served from Unix servers.
			try {
				java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
			}
			catch (Throwable t) {
				log.warn("Unable to get graphics environment.  Make sure that -Djava.awt.headless=true is defined as a JAVA OPT during startup", t);
			}
			Velocity.init(props);
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("locale", Context.getLocale());
			velocityContext.put("fn", functions);
			velocityContext.put("cohort", cohort);
			velocityContext.put("baseURL", baseURL);
			Velocity.evaluate(velocityContext, writer, PrintIdcardsServlet.class.getName(), card.getXml());
			//            response.getOutputStream().print(writer.toString());
		}
		catch (ParseErrorException e) {
			throw new ServletException("Error parsing template: " + card.getXml(), e);
		}
		catch (MethodInvocationException e) {
			throw new ServletException("Error parsing template: " + card.getXml(), e);
		}
		catch (ResourceNotFoundException e) {
			throw new ServletException("Error parsing template: " + card.getXml(), e);
		}
		catch (Exception e) {
			log.error("Error initializing Velocity engine", e);
		}
		finally {
			functions.clear();
			System.gc();
		}
		
		try {
			//Setup a buffer to obtain the content length
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			FopFactory fopFactory = FopFactory.newInstance();
			FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
			// configure foUserAgent as desired (this will let us set creator, etc.)
			
			//Setup FOP
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
			
			//Setup Transformer
			Source xsltSrc = new StreamSource(new StringReader(card.getXslt()));
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(xsltSrc);
			
			//Make sure the XSL transformation's result is piped through to FOP
			Result res = new SAXResult(fop.getDefaultHandler());
			
			//Setup input
			String xml = writer.toString();
			Source src = new StreamSource(new StringReader(xml));
			
			//Start the transformation and rendering process
			transformer.transform(src, res);
			
			//Prepare response
			String time = new SimpleDateFormat("yyyyMMdd_Hm").format(new Date());
			String filename = card.getName().replace(" ", "_") + "-" + time + ".pdf";
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/pdf");
			response.setContentLength(out.size());
			
			//Send content to Browser
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(out.toByteArray());
			outputStream.flush();
		}
		catch (FOPException e) {
			throw new ServletException("Error generating report", e);
		}
		catch (TransformerConfigurationException e) {
			throw new ServletException("Error generating report", e);
		}
		catch (TransformerException e) {
			throw new ServletException("Error generating report", e);
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}
	
	/**
	 * Gets an example xslt as a String. The xslt is borrowed from an example on the FOP website:
	 * http://xmlgraphics.apache.org/fop/quickstartguide.html#essentials
	 * 
	 * @return an xslt as a String
	 */
	public String getXslt() {
		String xslt = "<?xml version='1.0' encoding='utf-8'?>";
		xslt += "<xsl:stylesheet version='1.0'";
		xslt += "      xmlns:xsl='http://www.w3.org/1999/XSL/Transform'";
		xslt += "      xmlns:fo='http://www.w3.org/1999/XSL/Format'>";
		xslt += "  <xsl:output method='xml' indent='yes'/>";
		xslt += "  <xsl:template match='/'>";
		xslt += "    <fo:root>";
		xslt += "      <fo:layout-master-set>";
		xslt += "        <fo:simple-page-master master-name='A4-portrait'";
		xslt += "              page-height='29.7cm' page-width='21.0cm' margin='2cm'>";
		xslt += "          <fo:region-body/>";
		xslt += "        </fo:simple-page-master>";
		xslt += "      </fo:layout-master-set>";
		xslt += "      <fo:page-sequence master-reference='A4-portrait'>";
		xslt += "        <fo:flow flow-name='xsl-region-body'>";
		xslt += "          <fo:block>";
		xslt += "            Hello, <xsl:value-of select='name'/>!";
		xslt += "          </fo:block>";
		xslt += "        </fo:flow>";
		xslt += "      </fo:page-sequence>";
		xslt += "    </fo:root>";
		xslt += "  </xsl:template>";
		xslt += "</xsl:stylesheet>";
		return xslt;
	}
	
}
