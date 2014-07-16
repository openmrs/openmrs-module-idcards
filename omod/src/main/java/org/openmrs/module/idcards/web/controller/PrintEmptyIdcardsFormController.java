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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.context.Context;
import org.openmrs.module.idcards.IdcardsService;
import org.openmrs.module.idcards.IdcardsTemplate;
import org.openmrs.module.idcards.web.servlet.PrintEmptyIdcardsServlet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Backs the page that lets a user create a bunch of blank cards with new identifiers ready for
 * patient demographics
 */
@Controller
public class PrintEmptyIdcardsFormController {
	
	/**
	 * Show the page to the user.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/module/idcards/printEmptyIdcards")
	public String showPage(Boolean showLog, ModelMap model) throws Exception {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		
		model.put("unprintedQuantity", service.getNumberOfUnprintedGeneratedIdentifiers());
		model.put("cardTemplates", service.getAllIdcardsTemplates());
		
		if (showLog != null && showLog == true) {
			model.put("printedGroups", service.getPrintedIdentifiersGrouped());
		}
		
		return "/module/idcards/printEmptyIdcardsForm";
	}
	
	/**
	 * Handles an upload of identifiers to print onto empty id cards
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/module/idcards/uploadAndPrintIdCards")
	public void uploadAndPrintIdCards(ModelMap model, HttpServletRequest request, HttpServletResponse response,
				   @RequestParam(required=true, value="templateId") Integer templateId,
				   @RequestParam(required=true, value="inputFile") MultipartFile inputFile,
				   @RequestParam(required=true, value="pdfPassword") String pdfPassword) throws Exception {
		
		// Get identifiers from file
		List<String> identifiers = new ArrayList<String>();
		BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader(inputFile.getInputStream()));
			for (String s = r.readLine(); s != null; s = r.readLine()) {
				identifiers.add(s);
			}
		}
		finally {
			if (r != null) {
				r.close();
			}
		}
		
		IdcardsService is = (IdcardsService)Context.getService(IdcardsService.class);
		IdcardsTemplate card = is.getIdcardsTemplate(templateId);
		
		StringBuffer requestURL = request.getRequestURL();
		String baseURL = requestURL.substring(0, requestURL.indexOf("/module/idcards"));		
		
		PrintEmptyIdcardsServlet.generateOutputForIdentifiers(card, baseURL, response, identifiers, pdfPassword);
	}
}
