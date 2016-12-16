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
package org.openmrs.module.idcards.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.idcards.GeneratedIdentifier;
import org.openmrs.module.idcards.IdcardsService;
import org.openmrs.module.idcards.IdcardsTemplate;
import org.openmrs.module.idcards.db.IdcardsDAO;

/**
 * Implementation of the {@link IdcardsService}
 * 
 * @see org.openmrs.api.context.Context
 */
public class IdcardsServiceImpl implements IdcardsService {
	
	private IdcardsDAO dao;
	
	public void setIdcardsDAO(IdcardsDAO dao) {
		this.dao = dao;
	}
	
	public IdcardsDAO getIdcardsDAO() {
		return this.dao;
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#saveIdcardsTemplate(org.openmrs.module.idcards.IdcardsTemplate)
	 */
	public void saveIdcardsTemplate(IdcardsTemplate idcardsTemplate) throws APIException {
		if (idcardsTemplate.getDateCreated() == null)
			idcardsTemplate.setDateCreated(new Date());
		if (idcardsTemplate.getCreator() == null)
			idcardsTemplate.setCreator(Context.getAuthenticatedUser());
		
		idcardsTemplate.setDateChanged(new Date());
		idcardsTemplate.setChangedBy(Context.getAuthenticatedUser());
		
		dao.saveIdcardsTemplate(idcardsTemplate);
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getIdcardsTemplate(java.lang.Integer)
	 */
	public IdcardsTemplate getIdcardsTemplate(Integer templateId) throws APIException {
		return dao.getIdcardsTemplate(templateId);
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getAllIdcardsTemplates()
	 */
	public List<IdcardsTemplate> getAllIdcardsTemplates() throws APIException {
		return dao.getAllIdcardsTemplates();
	}
	
	/**
	 * Gets a list of Patients from a list of Patient Identifier Strings.
	 * 
	 * @param identifiers
	 * @return
	 * @throws org.openmrs.api.APIException
	 */
	public List<Patient> getPatientsByIdentifier(List<String> identifiers) throws APIException {
		return dao.getPatientsByIdentifier(identifiers);
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getPatientsByPatientId(java.util.List)
	 */
	public List<Patient> getPatientsByPatientId(List<Integer> patientIds) throws APIException {
		return dao.getPatientsByPatientId(patientIds);
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#generateIdentifiers(int, int)
	 */
	public void generateIdentifiers(int identfierStartValue, int quantityToGenerate) throws APIException {
		dao.generateIdentifiers(identfierStartValue, quantityToGenerate);
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getNumberOfUnprintedGeneratedIdentifiers()
	 */
	public Long getNumberOfUnprintedGeneratedIdentifiers() throws APIException {
		return dao.getNumberOfUnprintedGeneratedIdentifiers();
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#printGeneratedIdentifiers(int,IdcardsTemplate)
	 */
	public List<Integer> printGeneratedIdentifiers(int quantityToPrint, IdcardsTemplate template) throws APIException {
		Long unprintedQuantity = getNumberOfUnprintedGeneratedIdentifiers();
		if (quantityToPrint > unprintedQuantity)
			throw new APIException("There are only " + unprintedQuantity
			        + " unprinted identifiers that have been generated and you requested to print " + quantityToPrint
			        + ".  Please generated some more identifiers before continuing");
		
		return dao.printGeneratedIdentifiers(quantityToPrint, template);
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getPrintedIdentifiers()
	 */
	public List<GeneratedIdentifier> getPrintedIdentifiers() throws APIException {
		return dao.getPrintedIdentifiers();
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getPrintedIdentifiersGrouped()
	 */
	public Map<GeneratedIdentifier, Long> getPrintedIdentifiersGrouped() throws APIException {
		return dao.getPrintedIdentifiersGrouped();
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getGeneratedIdentifiersGrouped()
	 */
	public Map<GeneratedIdentifier, Long> getGeneratedIdentifiersGrouped() throws APIException {
		return dao.getGeneratedIdentifiersGrouped();
	}
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#generateAndPrintIdentifiers(java.lang.Integer,
	 *      java.lang.Integer, java.lang.Integer, org.openmrs.module.idcards.IdcardsTemplate)
	 */
	public List<Integer> generateAndPrintIdentifiers(Integer count, Integer min, Integer max, IdcardsTemplate template)
	                                                                                                                   throws APIException {
		return dao.generateAndPrintIdentifiers(count, min, max, template);
	}
	
}
