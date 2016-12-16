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
package org.openmrs.module.idcards;

import java.util.List;
import java.util.Map;

import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.module.idcards.db.IdcardsDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 */
@Transactional
public interface IdcardsService {
	
	/**
	 * Sets the DAO for this service. This is done through spring injection
	 * 
	 * @param dao DAO for this service
	 */
	public void setIdcardsDAO(IdcardsDAO dao);
	
	/**
	 * Auto generated method comment
	 * 
	 * @param idcardsTemplate
	 * @throws APIException
	 */
	public void saveIdcardsTemplate(IdcardsTemplate idcardsTemplate) throws APIException;
	
	/**
	 * Auto generated method comment
	 * 
	 * @param templateId
	 * @return
	 * @throws APIException
	 */
	public IdcardsTemplate getIdcardsTemplate(Integer templateId) throws APIException;
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 * @throws APIException
	 */
	public List<IdcardsTemplate> getAllIdcardsTemplates() throws APIException;
	
	/**
	 * This adds rows to the generated_identifier table.
	 * 
	 * @param identfierStartValue the number to start the identifiers at
	 * @param quantityToGenerate the number of identifiers to generate
	 * @throws APIException
	 */
	public void generateIdentifiers(int identfierStartValue, int quantityToGenerate) throws APIException;
	
	/**
	 * Returns a list of identifiers to print. The list will be unprinted identifiers from the
	 * generated_identifiers table.<br/>
	 * <br/>
	 * All of the identifiers returned will be marked as printed at this time by the current user.
	 * (If an error happens after this list is returned and the identifiers are never printed, that
	 * is not this methods problem.<br/>
	 * <br/>
	 * The returned identifiers are selected randomly from the entire table of unprinted
	 * identifiers.
	 * 
	 * @param quantityToPrint the number of identifiers to print at this time.
	 * @param template the IdcardsTemplate that the identifiers are being printed with
	 * @return a list of size "quantityToPrint" that contains the identifier strings with check
	 *         digits
	 * @throws APIException if quantityToPrint is greater than the number of available identifiers
	 *             left
	 */
	public List<Integer> printGeneratedIdentifiers(int quantityToPrint, IdcardsTemplate template) throws APIException;
	
	/**
	 * Gets the number of rows in the generated_identifiers table that have not been printed yet.
	 * 
	 * @return number of rows where printed == false
	 * @throws APIException
	 */
	public Long getNumberOfUnprintedGeneratedIdentifiers() throws APIException;
	
	/**
	 * Returns the rows in generated_identifiers that have printed = true
	 * 
	 * @return list of already printed identifiers
	 * @throws APIException
	 */
	public List<GeneratedIdentifier> getPrintedIdentifiers() throws APIException;
	
	/**
	 * Returns a map from generated_identifiers that have printed = true grouped by the datePrinted
	 * to the number of identifiers in that group. The GeneratedIdentifier key will be the row with
	 * the lowest identifier.
	 * 
	 * @return list of already printed identifiers mapped to size
	 * @throws APIException
	 */
	public Map<GeneratedIdentifier, Long> getPrintedIdentifiersGrouped() throws APIException;
	
	/**
	 * Returns a map from generated_identifiers grouped by the dateGenerated to the number of
	 * identifiers in that group. The GeneratedIdentifier key will be the row with the lowest
	 * identifier.
	 * 
	 * @return list of generated identifiers mapped to number that were generated at the time
	 * @throws APIException
	 */
	public Map<GeneratedIdentifier, Long> getGeneratedIdentifiersGrouped() throws APIException;
	
	/**
	 * Gets a list of Patients from a list of Patient Identifier Strings.
	 * 
	 * @param identifiers
	 * @return
	 * @throws APIException
	 */
	public List<Patient> getPatientsByIdentifier(List<String> identifiers) throws APIException;
	
	/**
	 * Auto generated method comment
	 * 
	 * @param patientIds
	 * @return
	 * @throws APIException
	 */
	public List<Patient> getPatientsByPatientId(List<Integer> patientIds) throws APIException;
	
	/**
	 * Returns a list of identifiers to print. The list will of <code>count</code> size and will be
	 * randomly chosen between <code>min</code> and <code>max</code><br/>
	 * <br/>
	 * All of the identifiers returned will be marked as printed at this time by the current user.
	 * (If an error happens after this list is returned and the identifiers are never printed, that
	 * is not this method's problem.<br/>
	 * <br/>
	 * The generated identifiers are unique across the idcards_generated_identifier table.
	 * 
	 * @param count the number of random MRNs within min and max to generate+print
	 * @param min the lowest possible MRN
	 * @param max the highest possible MRN
	 * @param template the IdcardsTemplate that the identifiers are being printed with
	 * @return a list of size "quantityToPrint" that contains the identifier strings with check
	 *         digits
	 */
	public List<Integer> generateAndPrintIdentifiers(Integer count, Integer min, Integer max, IdcardsTemplate template) throws APIException;
	
}
