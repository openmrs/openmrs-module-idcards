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
package org.openmrs.module.idcards.db.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.type.StandardBasicTypes;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.idcards.GeneratedIdentifier;
import org.openmrs.module.idcards.IdcardsTemplate;
import org.openmrs.module.idcards.db.IdcardsDAO;

/**
 * Hibernate specific Idcards database methods
 */
public class HibernateIdcardsDAO implements IdcardsDAO {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Set session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#saveIdcardsTemplate(org.openmrs.module.idcards.IdcardsTemplate)
	 */
	public void saveIdcardsTemplate(IdcardsTemplate idcardsTemplate) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(idcardsTemplate);
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#getIdcardsTemplate(java.lang.Integer)
	 */
	public IdcardsTemplate getIdcardsTemplate(Integer templateId) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(IdcardsTemplate.class, "template").add(
		    Expression.eq("template.templateId", templateId));
		List<IdcardsTemplate> template = criteria.list();
		return template.get(0);
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#getAllIdcardsTemplates()
	 */
	public List<IdcardsTemplate> getAllIdcardsTemplates() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(IdcardsTemplate.class, "template");
		return (List<IdcardsTemplate>) criteria.list();
	}
	
	/**
	 * Get all PatientIdentifiers for the patientId.
	 * 
	 * @param pid The Patient.patientId.
	 * @param preferred If true, only return preferred PatientIdentifiers
	 * @return
	 * @throws org.openmrs.api.db.DAOException
	 */
	public List<PatientIdentifier> getPatientIdentifiersByPatientId(Patient pid, boolean preferred) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PatientIdentifier.class, "identifier").add(
		    Expression.eq("identifier.patient", pid));
		
		if (preferred) {
			criteria.add(Expression.eq("identifier.preferred", true));
		}
		
		return criteria.list();
		
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#getPatientsByIdentifier(java.util.List)
	 */
	public List<Patient> getPatientsByIdentifier(List<String> identifiers) throws DAOException {
		String tmp1 = identifiers.toString();
		String tmp2 = tmp1.replaceAll(", ", "\\$|\\^");
		String tmp3 = tmp2.replaceAll(" ", "\\[ ]?");
		String tmp4 = tmp3.replaceAll("\\-", "\\[\\-\\]?");
		String regex = "^" + tmp4.substring(1, tmp4.length() - 1) + "$";
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Patient.class);
		criteria.createAlias("identifiers", "ids");
		criteria.add(Restrictions.sqlRestriction("identifier rlike ?", regex, StandardBasicTypes.STRING));
		log.debug("Patient identifier regex" + regex);
		log.debug("StandardBasicTypes.STRING: " + StandardBasicTypes.STRING.toString());
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#getPatientsByPatientId(java.util.List)
	 */
	public List<Patient> getPatientsByPatientId(List<Integer> patientIds) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Patient.class);
		criteria.add(Expression.in("patientId", patientIds));
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#generateIdentifiers(int, int)
	 */
	public void generateIdentifiers(int identifierStartValue, int quantityToGenerate) throws DAOException {
		Date generatedDate = new Date();
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(generatedDate);
		Integer currentUserId = Context.getAuthenticatedUser().getUserId();
		
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.setCacheMode(CacheMode.IGNORE);
		
		String insertPrefix = "insert into idcards_generated_identifier (id, generator, date_generated) values ";
		String valuesPrefix = "(";
		String valuesSuffix = ", " + currentUserId + ", '" + date + "')";
		
		// if we're in mysql, do extended inserts to speed things up
		SessionFactoryImplementor implementor = (SessionFactoryImplementor) sessionFactory;
		Dialect dialect = implementor.getDialect();
		boolean isMySQLDialect = MySQLDialect.class.getName().equals(dialect.getClass().getName());
		
		if (isMySQLDialect) {
			String sql = null;
			// loop over the list of numbers and get/insert the string identifier
			for (int x = identifierStartValue; x < identifierStartValue + quantityToGenerate; x++) {
				if (sql == null) // if its not the first element, add a comma
					sql = insertPrefix;
				else
					sql += ",";
				
				sql += valuesPrefix + x + valuesSuffix;
				
				// send to the database every 1001 entries or at the end 
				if (x % 100 == 0 || x == (identifierStartValue + quantityToGenerate - 1)) {
					try {
						SQLQuery query = currentSession.createSQLQuery(sql);
						query.executeUpdate();
						sql = null; // reset the sql string
						currentSession.flush();
					}
					catch (ConstraintViolationException cve) {
						log.error("Sql: " + sql);
						throw new DAOException("Error creating an identifier between " + x + " and " + (x - 1001)
						        + " because it already exists in the system", cve);
					}
				}
			}
			
		} else {
			for (int x = identifierStartValue; x < identifierStartValue + quantityToGenerate; x++) {
				String sql = insertPrefix + valuesPrefix + x + valuesSuffix;
				
				try {
					SQLQuery query = currentSession.createSQLQuery(sql);
					query.executeUpdate();
				}
				catch (ConstraintViolationException cve) {
					throw new DAOException("Unable to create identifier: " + x + " because it already exists in the system",
					        cve);
				}
				
				// control the number of objects in memory
				if (x % 500 == 0 || x == (identifierStartValue + quantityToGenerate - 1)) {
					currentSession.flush();
					currentSession.clear();
				}
			}
		}
		
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#getNumberOfUnprintedGeneratedIdentifiers()
	 */
	public Long getNumberOfUnprintedGeneratedIdentifiers() throws DAOException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(GeneratedIdentifier.class);
		crit.setProjection(Projections.count("printed"));
		crit.add(Expression.eq("printed", false));
		
		return (Long) crit.uniqueResult();
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#getPrintedIdentifiers()
	 */
	public List<GeneratedIdentifier> getPrintedIdentifiers() throws DAOException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(GeneratedIdentifier.class);
		crit.add(Expression.eq("printed", true));
		
		return (List<GeneratedIdentifier>) crit.list();
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#printGeneratedIdentifiers(int,IdcardsTemplate)
	 */
	public List<Integer> printGeneratedIdentifiers(int quantityToPrint, IdcardsTemplate template) throws DAOException {
		// find the identifiers to return and mark as printed
		String sql = "select id from GeneratedIdentifier where printed = 0 order by RAND()";
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		query.setMaxResults(quantityToPrint); // this is the "limit = quantityToPrint" for sql
		
		List<Integer> printedIdentifiers = (List<Integer>) query.list();
		
		// mark the identifiers as printed
		sql = "update GeneratedIdentifier set printed = 1, printedBy = :printedBy, datePrinted = :date, printedTemplate = :template where id in (:identifiers)";
		query = sessionFactory.getCurrentSession().createQuery(sql);
		query.setParameter("printedBy", Context.getAuthenticatedUser());
		query.setTimestamp("date", new Date());
		query.setParameter("template", template);
		query.setParameterList("identifiers", printedIdentifiers);
		query.executeUpdate();
		
		return printedIdentifiers;
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#getPrintedIdentifiersGrouped()
	 */
	public Map<GeneratedIdentifier, Long> getPrintedIdentifiersGrouped() throws DAOException {
		
		String hql = "select min(id), count(datePrinted) from GeneratedIdentifier where printed = 1 group by datePrinted";
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		Map<GeneratedIdentifier, Long> returnMap = new LinkedHashMap<GeneratedIdentifier, Long>();
		
		List<Object[]> rows = (List<Object[]>) query.list();
		
		for (Object[] row : rows) {
			Integer identifier = (Integer) row[0];
			Long count = (Long) row[1];
			GeneratedIdentifier genId = (GeneratedIdentifier) sessionFactory.getCurrentSession().createQuery(
			    "from GeneratedIdentifier where id = :id").setInteger("id", identifier).uniqueResult();
			if (genId != null)
				returnMap.put(genId, count);
		}
		
		return returnMap;
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#getGeneratedIdentifiersGrouped()
	 */
	public Map<GeneratedIdentifier, Long> getGeneratedIdentifiersGrouped() throws DAOException {
		
		String hql = "select min(id), count(dateGenerated) from GeneratedIdentifier group by dateGenerated";
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		Map<GeneratedIdentifier, Long> returnMap = new LinkedHashMap<GeneratedIdentifier, Long>();
		
		List<Object[]> rows = (List<Object[]>) query.list();
		
		for (Object[] row : rows) {
			Integer identifier = (Integer) row[0];
			Long count = (Long) row[1];
			GeneratedIdentifier genId = (GeneratedIdentifier) sessionFactory.getCurrentSession().createQuery(
			    "from GeneratedIdentifier where id = :id").setInteger("id", identifier).uniqueResult();
			if (genId != null)
				returnMap.put(genId, count);
		}
		
		return returnMap;
	}
	
	/**
	 * @see org.openmrs.module.idcards.db.IdcardsDAO#generateAndPrintIdentifiers(java.lang.Integer,
	 *      java.lang.Integer, java.lang.Integer, org.openmrs.module.idcards.IdcardsTemplate)
	 */
	public List<Integer> generateAndPrintIdentifiers(Integer count, Integer min, Integer max, IdcardsTemplate template)
	                                                                                                                   throws DAOException {
		
		// make sure there are enough identifiers to generate
		String hql = "select count(id) from GeneratedIdentifier where id >= :min and id < :max";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("min", min);
		query.setInteger("max", max);
		Long numIdsAlreadyGeneratedInRange = (Long) query.list().get(0);
		Integer numIdsInRange = max - min;
		Long availableIdentifiers = numIdsInRange - numIdsAlreadyGeneratedInRange;
		if (count > availableIdentifiers)
			throw new DAOException("You can't generate " + count + " identifiers because there are only "
			        + availableIdentifiers + " available identifiers between " + min + " and " + max);
		
		Session session = sessionFactory.getCurrentSession();
		
		List<Integer> identifiers = new ArrayList<Integer>(count);
		
		User user = Context.getAuthenticatedUser();
		Date date = new Date();
		
		for (int x = 0; x < count; x++) {
			Integer possibleId;
			do { // loop until we have a number that isn't used already
				possibleId = (int) (Math.random() * (max - min)) + min;
			} while (identifiers.contains(possibleId) || isIdentifierGenerated(possibleId));
			
			identifiers.add(possibleId);
		}
		
		// insert rows into idcards_generated_identifier
		for (Integer id : identifiers) {
			GeneratedIdentifier generatedIdObject = new GeneratedIdentifier(id, user, date);
			generatedIdObject.setPrinted(true);
			generatedIdObject.setPrintedBy(user);
			generatedIdObject.setDatePrinted(date);
			generatedIdObject.setPrintedTemplate(template);
			session.save(generatedIdObject);
		}
		
		return identifiers;
	}
	
	/**
	 * Checks the idcards_generated_identifiers table to see if the given string exists in it
	 * 
	 * @param identifier the identifier to look for
	 * @return true if the identifier is in the table
	 */
	public boolean isIdentifierGenerated(Integer identifier) {
		String hql = "select id from GeneratedIdentifier where id = :id";
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger("id", identifier);
		return query.list().size() > 0;
		
	}
	
}
