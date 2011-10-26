package org.openmrs.module.idcards.extension.html;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;

public class AdminExt extends AdministrationSectionExt {

	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}
	
	public String getTitle() {
		return "idcards.title";
	}
	
	public String getRequiredPrivilege() {
		return "Reprint Id Cards";
	}
	
	public Map<String, String> getLinks() {
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		boolean enableGen = "true".equals(Context.getAdministrationService().getGlobalProperty("idcards.enableIdentifierGeneration"));
		if (Context.hasPrivilege("Pre-Generate Random Patient Identifiers") && enableGen) {
            map.put("module/idcards/generateIdentifiers.form", "idcards.generateIdentifiers.title");
        }
		
		if (Context.hasPrivilege("Print Id Cards")) {
            map.put("module/idcards/printEmptyIdcards.form", "idcards.printEmpty.title");
        }

        if (Context.hasPrivilege("Reprint Id Cards")) {
            map.put("module/idcards/reprintIdcards.form", "idcards.print.title");
        }
        
        if (Context.hasPrivilege("Manage Id Card Templates")) {
            map.put("module/idcards/templates.list", "idcards.templates.title");
        }

        return map;
	}
	
}
