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
package org.openmrs.module.idcards.web.controller;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.api.context.Context;
import org.openmrs.module.idcards.IdcardsService;
import org.openmrs.module.idcards.IdcardsTemplate;
import org.openmrs.module.idcards.web.servlet.PrintEmptyIdcardsServlet;
import org.openmrs.module.idcards.web.servlet.PrintIdcardsServlet;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * The controller behind the template.form (templateForm.jsp) page that allows a user to add/edit id
 * card templates.
 */
@Controller
@RequestMapping(value = "/module/idcards/template")
// so that values for our formBackingObject are preserved across requests 
@SessionAttributes("template")
public class TemplateFormController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * Show the page to the user.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showPage(@RequestParam(required = false) Integer templateId, ModelMap modelMap) throws Exception {
		IdcardsTemplate template = new IdcardsTemplate();
		
		if (Context.isAuthenticated()) {
			IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
			if (templateId != null)
				template = service.getIdcardsTemplate(templateId);
		}
		
		modelMap.addAttribute("template", template);
		
		return "/module/idcards/templateForm";
	}
	
	/**
	 * Handles the user's submission of the form.
	 */
	@RequestMapping(method = RequestMethod.POST, params = "action=save")
	public String onSave(@ModelAttribute("template") IdcardsTemplate template, HttpSession httpSession) {
		
		if (Context.isAuthenticated()) {
			IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
			service.saveIdcardsTemplate(template);
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "idcards.template.saved");
		}
		
		return "redirect:/module/idcards/templates.list";
	}
	
	/**
	 * Handles the user's submission of the form.
	 */
	@RequestMapping(method = RequestMethod.POST, params = "action=saveAndPrintEmpty")
	public void onSaveAndPrintEmpty(@ModelAttribute("template") IdcardsTemplate template, HttpServletRequest request,
	                                HttpServletResponse response) throws IOException {
		
		if (Context.isAuthenticated()) {
			IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
			service.saveIdcardsTemplate(template);
			
			try {
				StringBuffer requestURL = request.getRequestURL();
				String baseURL = requestURL.substring(0, requestURL.indexOf("/module"));
				
				PrintEmptyIdcardsServlet.generateOutput(template, baseURL, response, Collections.nCopies(10, 0), null);
			}
			catch (Exception e) {
				log.error("Unable to print cards", e);
				e.printStackTrace(response.getWriter());
			}
		}
		
	}
	
	/**
	 * Handles the user's submission of the form.
	 */
	@RequestMapping(method = RequestMethod.POST, params = "action=saveAndReprint")
	public void onSaveAndReprint(@ModelAttribute("template") IdcardsTemplate template, HttpServletRequest request,
	                             HttpServletResponse response) throws IOException {
		
		if (Context.isAuthenticated()) {
			IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
			service.saveIdcardsTemplate(template);
			
			Cohort cohort = new Cohort();
			cohort.addMember(1);
			cohort.addMember(2);
			
			try {
				StringBuffer requestURL = request.getRequestURL();
				String baseURL = requestURL.substring(0, requestURL.indexOf("/module"));
				
				PrintIdcardsServlet.generateOutput(template, baseURL, cohort, response);
			}
			catch (Exception e) {
				log.error("Unable to print cards", e);
				e.printStackTrace(response.getWriter());
			}
		}
		
	}
	
}
