/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.idcards.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import org.apache.fop.pdf.PDFEncryptionParams;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.openmrs.api.context.Context;
import org.openmrs.module.idcards.IdcardsService;
import org.openmrs.module.idcards.IdcardsTemplate;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * Prints out a list of pre-generated Patient Identifier numbers, check digit included, and posts a
 * file with the numbers listed.
 */
public class PrintEmptyIdcardsServlet extends HttpServlet {

	private static Log log = LogFactory.getLog(PrintEmptyIdcardsServlet.class);

	public static final long serialVersionUID = 12312381L;

	private static IdcardsService getIdcardsService() {
		return (IdcardsService) Context.getService(IdcardsService.class);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Integer mrnCount = ServletRequestUtils.getRequiredIntParameter(request, "mrn_count");
		Integer templateId = ServletRequestUtils.getIntParameter(request, "templateId", 1);
		String generatedMRNs = ServletRequestUtils.getStringParameter(request, "generated_mrns", "none");
		String password = ServletRequestUtils.getStringParameter(request, "pdf_password");
		if (!StringUtils.hasLength(password))
			throw new ServletException("A non-empty password is required.");

		IdcardsTemplate card = getIdcardsService().getIdcardsTemplate(templateId);

		StringBuffer requestURL = request.getRequestURL();
		String baseURL = requestURL.substring(0, requestURL.indexOf("/moduleServlet"));

		List<Integer> identifiers = null;

		if ("none".equals(generatedMRNs)) {
			identifiers = Collections.nCopies(mrnCount, 0);
		} else if ("pregenerated".equals(generatedMRNs)){
			identifiers = getIdcardsService().printGeneratedIdentifiers(mrnCount, card);
		}
		else if ("generateNew".equals(generatedMRNs)) {
			Integer min = Integer.valueOf(Context.getAdministrationService().getGlobalProperty("idcards.generateMin"));
			Integer max = Integer.valueOf(Context.getAdministrationService().getGlobalProperty("idcards.generateMax"));
			identifiers = getIdcardsService().generateAndPrintIdentifiers(mrnCount, min, max, card);
		}
		else
			throw new ServletException("Invalid choice for 'generatedMRNs' parameter");


		generateOutput(card, baseURL, response, identifiers, password);

	}

	/**
	 * Write the pdf to the given response
	 *
	 * @param card
	 * @param baseURL
	 * @param request
	 * @param response
	 * @param useGeneratedMRNs
	 * @param password the password to encrypt the pdf with. If null, no encryption is done
	 */
	public static void generateOutput(IdcardsTemplate card, String baseURL, HttpServletResponse response,
	                                  List<Integer> identifiers, String password) throws ServletException, IOException {

		// add check digits to the identifiers
		List<String> checkdigitedIdentifiers = new ArrayList<String>(identifiers.size());
		try {
			for (Integer id : identifiers) {
				checkdigitedIdentifiers.add(id + "-" + OpenmrsUtil.getCheckDigit(id.toString()));
			}
		}
		catch (Exception e) {
			throw new ServletException("Unable to generate check digit on given identifier", e);
		}

		generateOutputForIdentifiers(card, baseURL, response, checkdigitedIdentifiers, password);
	}

	/**
	 * Write the pdf to the given response
	 */
	@SuppressWarnings("unchecked")
	public static void generateOutputForIdentifiers(IdcardsTemplate card, String baseURL, HttpServletResponse response,
													List<String> identifiers, String password) throws ServletException, IOException {

		Properties props = new Properties();
		props.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
		props.setProperty("class.resource.loader.description", "VelocityClasspathResourceLoader");
		props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");

		// do the velocity magic
		Writer writer = new StringWriter();
		try {
			// Allow images to be served from Unix servers.
			try {
				java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
			}
			catch (Throwable t) {
				log.warn("Unable to get graphics environment.  " +
						 "Make sure that -Djava.awt.headless=true is defined as a JAVA OPT during startup", t);
			}
			Velocity.init(props);
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("locale", Context.getLocale());
			velocityContext.put("identifiers", identifiers);
			velocityContext.put("baseURL", baseURL);
			Velocity.evaluate(velocityContext, writer, PrintEmptyIdcardsServlet.class.getName(), card.getXml());
		}
		catch (ParseErrorException e) {
			throw new ServletException("Error parsing template: ", e);
		}
		catch (MethodInvocationException e) {
			throw new ServletException("Error parsing template: ", e);
		}
		catch (ResourceNotFoundException e) {
			throw new ServletException("Error parsing template: ", e);
		}
		catch (Exception e) {
			throw new ServletException("Error initializing Velocity engine", e);
		}
		finally {
			System.gc();
		}

		try {
			//Setup a buffer to obtain the content length
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			FopFactory fopFactory = FopFactory.newInstance();

			//fopFactory supports customization with a config file
			//Load the config file before creating the user agent.
			String userConfigFile = Context.getAdministrationService().getGlobalProperty("idcards.fopConfigFilePath");
			if( userConfigFile != null ){
				try {
					fopFactory.setUserConfig( new java.io.File(userConfigFile) );
					log.debug("Successfully loaded config file |" + userConfigFile + "|");

				} catch( java.io.IOException e){
					log.error("Could not load fopFactory user config file at " +
							userConfigFile + ". Error message:" + e.getMessage());
				} catch( org.xml.sax.SAXException e ){
					log.error("Could not parse fopFactory user config file at  " +
							userConfigFile + ". Error message:" + e.getMessage());
				}
			}

			FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
			foUserAgent.getRendererOptions().put("encryption-params", new PDFEncryptionParams(password, null, true, false, false, false));

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
			String time = new SimpleDateFormat("yyyy-MM-dd_Hms").format(new Date());
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
}
