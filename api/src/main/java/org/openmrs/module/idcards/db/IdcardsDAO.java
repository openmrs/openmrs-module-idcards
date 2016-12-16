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
package org.openmrs.module.idcards.db;

import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.idcards.GeneratedIdentifier;
import org.openmrs.module.idcards.IdcardsTemplate;

/**
 *
 */
public interface IdcardsDAO {
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public void saveIdcardsTemplate(IdcardsTemplate idcardsTemplate) throws DAOException;
	
	public IdcardsTemplate getIdcardsTemplate(Integer templateId) throws DAOException;
	
	public List<IdcardsTemplate> getAllIdcardsTemplates() throws DAOException;
	
	public List<PatientIdentifier> getPatientIdentifiersByPatientId(Patient p, boolean preferred) throws DAOException;
	
	public List<Patient> getPatientsByIdentifier(List<String> identifiers) throws DAOException;
	
	public List<Patient> getPatientsByPatientId(List<Integer> identifiers) throws DAOException;
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#generateIdentifiers(int, int)
	 */
	public void generateIdentifiers(int identfierStartValue, int quantityToGenerate) throws DAOException;
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getNumberOfUnprintedGeneratedIdentifiers()
	 */
	public Long getNumberOfUnprintedGeneratedIdentifiers() throws DAOException;
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#printGeneratedIdentifiers(int,IdcardsTemplate)
	 */
	public List<Integer> printGeneratedIdentifiers(int quantityToPrint, IdcardsTemplate template) throws DAOException;
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getPrintedIdentifiers()
	 */
	public List<GeneratedIdentifier> getPrintedIdentifiers() throws DAOException;
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getPrintedIdentifiersGrouped()
	 */
	public Map<GeneratedIdentifier, Long> getPrintedIdentifiersGrouped() throws DAOException;
	
	/**
	 * @see org.openmrs.module.idcards.IdcardsService#getGeneratedIdentifiersGrouped()
	 */
	public Map<GeneratedIdentifier, Long> getGeneratedIdentifiersGrouped() throws DAOException;

	/**
     * @see org.openmrs.module.idcards.IdcardsService#generateAndPrintIdentifiers(java.lang.Integer, java.lang.Integer, java.lang.Integer, org.openmrs.module.idcards.IdcardsTemplate)
     */
    public List<Integer> generateAndPrintIdentifiers(Integer count, Integer min, Integer max, IdcardsTemplate template) throws DAOException;
	
}
