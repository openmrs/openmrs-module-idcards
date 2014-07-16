package org.openmrs.module.idcards.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.idcards.IdcardsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for the templates.list page (templatesList.jsp)
 */
@Controller
public class TemplateListController {
	
	/**
	 * Show the page to the user.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/module/idcards/templates")
	public String showPage(ModelMap model) throws Exception {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		model.put("templates", service.getAllIdcardsTemplates());
		
		return "/module/idcards/templatesList";
	}
	
}
