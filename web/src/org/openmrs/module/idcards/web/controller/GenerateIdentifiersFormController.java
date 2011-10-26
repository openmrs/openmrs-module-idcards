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

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.openmrs.api.context.Context;
import org.openmrs.module.idcards.IdcardsService;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Backs the page that lets an admin create a ton of new rows that have randomly ordered identifiers
 * that are waiting to be printed.
 */
@Controller
public class GenerateIdentifiersFormController {
	
	/**
	 * Show the page to the user.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/module/idcards/generateIdentifiers")
	public String showPage(Boolean showLog, ModelMap model) throws Exception {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		
		model.put("unprintedQuantity", service.getNumberOfUnprintedGeneratedIdentifiers());
		
		if (showLog != null && showLog == true) {
			model.put("printedGroups", service.getPrintedIdentifiersGrouped());
			model.put("generatedGroups", service.getGeneratedIdentifiersGrouped());
		}
		
		return "/module/idcards/generateIdentifiersForm";
	}
	
	/**
	 * Handles the user's submission of the form.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/module/idcards/generateIdentifiers")
	public String onSubmit(@RequestParam Integer initialMrn, @RequestParam Integer mrn_count, HttpSession httpSession) {
		
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		service.generateIdentifiers(initialMrn, mrn_count);
		
		httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Identifiers generated successfully at : " + new Date());
		
		return "redirect:/module/idcards/generateIdentifiers.form";
	}
	
}
