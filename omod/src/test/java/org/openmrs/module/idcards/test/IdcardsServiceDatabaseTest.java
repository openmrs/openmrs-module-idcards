/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmrs.module.idcards.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.idcards.GeneratedIdentifier;
import org.openmrs.module.idcards.IdcardsExportFunctions;
import org.openmrs.module.idcards.IdcardsService;
import org.openmrs.module.idcards.IdcardsTemplate;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * This class tests the IdcardsService using the real database; not the in-memory database. For
 * testing the in-memory database, use @see org.openmrs.module.idcards.test.IdcardsServiceTest
 */
public class IdcardsServiceDatabaseTest extends BaseModuleContextSensitiveTest {
	
	@Before
	public void runBeforeEachTest() throws Exception {
		initializeInMemoryDatabase();
		
		authenticate();
		
		executeDataSet("org/openmrs/module/idcards/include/IdcardsServiceTest.xml");
	}
	
	//    /**
	//     * Do not use the in memory database. 
	//     * @return
	//     */
	//    public Boolean useInMemoryDatabase() {
	//        return false;
	//    }
	
	/**
	 * Test to see if the user is authenticated.
	 */
	@Test
	public void shouldGetAuthenticatedUser() {
		User u = Context.getAuthenticatedUser();
		assertNotNull(u);
	}
	
	/**
	 * Test to see if we can get some locations from the openmrs real database
	 */
	@Test
	public void shouldGetAListOfLocations() {
		List<Location> locations = Context.getLocationService().getAllLocations();
		System.out.println(locations);
		assertNotNull(locations);
	}
	
	/**
	 * Test to see if we get any IdcardsTemplates from the database.
	 */
	@Test
	public void shouldGetAllIdcardsTemplates() {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		List<IdcardsTemplate> templates = service.getAllIdcardsTemplates();
		System.out.println("IdcardsTemplates: " + templates);
		assertNotNull(templates);
		IdcardsTemplate template = service.getIdcardsTemplate(1);
		System.out.println("One IdcardsTemplate " + template);
		assertNotNull(template);
	}
	
	/**
	 * Tests the pre-generating of identifiers
	 */
	@Test
	public void shouldGenerateIdentifiers() throws Exception {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		service.generateIdentifiers(129, 5);
		Assert.assertEquals(8, service.getNumberOfUnprintedGeneratedIdentifiers().intValue());
		List<Integer> identifiers = service.printGeneratedIdentifiers(8, null);
		
		// make sure they were generated
		Assert.assertTrue(identifiers.contains(129));
		Assert.assertTrue(identifiers.contains(130));
		Assert.assertTrue(identifiers.contains(131));
		Assert.assertTrue(identifiers.contains(132));
		Assert.assertTrue(identifiers.contains(133));
	}
	
	/**
	 * Test printing out generated identifiers
	 */
	@Test
	public void shouldPrintGeneratedIdentifiers() {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		List<Integer> identifiers = service.printGeneratedIdentifiers(3, null);
		
		// there are only 3 unprinted identifiers, so they should be the ones returned
		Assert.assertTrue(identifiers.contains(128));
		Assert.assertTrue(identifiers.contains(127));
		Assert.assertTrue(identifiers.contains(126));
	}
	
	/**
	 * Make sure printing happens randomly over entire table.
	 */
	@Test
	public void shouldPrintIdentifiersInRandomOrderOverEntireTable() {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		service.generateIdentifiers(129, 500);
		
		List<Integer> identifiers = service.printGeneratedIdentifiers(3, null);
		
		// make sure they are retrieved out of order.
		// This will fail 1 out of 500 tries because they will randomly be returned in the true order!
		Assert.assertFalse(identifiers.get(0).equals(128) && identifiers.get(1).equals(127)
		        && identifiers.get(2).equals(126));
	}
	
	/**
	 * Test printing out generated identifiers
	 */
	@Test
	public void shouldSaveTemplateWhenPrintingIdentifiers() {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		IdcardsTemplate template = new IdcardsTemplate(2);
		service.printGeneratedIdentifiers(3, template);
		
		List<GeneratedIdentifier> printedIdentifiers = service.getPrintedIdentifiers();
		
		Assert.assertEquals(new IdcardsTemplate(2), printedIdentifiers.get(6).getPrintedTemplate());
		Assert.assertEquals(new IdcardsTemplate(2), printedIdentifiers.get(7).getPrintedTemplate());
		Assert.assertEquals(new IdcardsTemplate(2), printedIdentifiers.get(8).getPrintedTemplate());
	}
	
	/**
	 * There are only 3 unprinted identifiers in the database, trying to print 4 should throw an
	 * error
	 */
	@Test(expected = APIException.class)
	public void shouldThrowErrorWhenPrintingTooManyIdentifiers() {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		service.printGeneratedIdentifiers(4, null);
	}
	
	/**
	 * Makes sure the first identifier in the group is the lowest identifier.
	 */
	@Test
	public void shouldGetPrintedIdentifiersGroup() {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		Map<GeneratedIdentifier, Long> grouped = service.getPrintedIdentifiersGrouped();
		Iterator<Entry<GeneratedIdentifier, Long>> iterator = grouped.entrySet().iterator();
		Entry<GeneratedIdentifier, Long> firstGroup = iterator.next();
		Entry<GeneratedIdentifier, Long> secondGroup = iterator.next();
		
		Assert.assertEquals(120, firstGroup.getKey().getId().intValue()); // check the ordering of the group
		Assert.assertEquals(4, firstGroup.getValue().intValue()); // check the size
		Assert.assertEquals(126, secondGroup.getKey().getId().intValue());// check the ordering of the group
		Assert.assertEquals(2, secondGroup.getValue().intValue()); // check the size
	}
	
	/**
	 * Test getting both the printed and nonprinted identifiers in groups
	 */
	@Test
	public void shouldGetGeneratedIdentifiersGroup() {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		Map<GeneratedIdentifier, Long> grouped = service.getGeneratedIdentifiersGrouped();
		Iterator<Entry<GeneratedIdentifier, Long>> iterator = grouped.entrySet().iterator();
		Entry<GeneratedIdentifier, Long> group = iterator.next();
		Assert.assertEquals(120, group.getKey().getId().intValue()); // check the ordering of the group
		Assert.assertEquals(6, group.getValue().intValue()); // check the size
		
		group = iterator.next();
		Assert.assertEquals(9, group.getKey().getId().intValue()); // check the ordering of the group
		Assert.assertEquals(3, group.getValue().intValue()); // check the size
		
		Assert.assertFalse(iterator.hasNext());
	}
	
	/**
	 * Make sure we're getting all the unprinted identifiers
	 */
	@Test
	public void shouldGetNumberOfUnprintedIdentifiers() {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		Long unprintedQuantity = service.getNumberOfUnprintedGeneratedIdentifiers();
		Assert.assertEquals(3, unprintedQuantity.intValue());
	}
	
	/**
	 * Test to see if we get a list of patients from a list of identifiers. <br/>
	 * <br/>
	 * <b>"rlike" DOESN'T WORK ON HSQLDB DATABASES</b>
	 */
	@Test
	public void shouldGetSomePatientsFromListOfIdentifiers() {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		PatientService ps = Context.getPatientService();
		List<String> identifiers = new ArrayList<String>();
		identifiers.add(ps.getPatient(2).getPatientIdentifier().getIdentifier());
		identifiers.add(ps.getPatient(3).getPatientIdentifier().getIdentifier());
		identifiers.add(ps.getPatient(4).getPatientIdentifier().getIdentifier());
		// will fail if using in-memory database because hsqldb doesn't have "rlike" function
		List<Patient> patients = service.getPatientsByIdentifier(identifiers);
		for (Patient p : patients) {
			System.out.println(p.getPatientId() + ", " + p.getPatientIdentifier());
		}
		assertEquals(Long.valueOf(patients.size()), Long.valueOf(identifiers.size()));
		
	}
	
	/**
	 * Test to see if we get a list of patients from a list of patientIds.
	 */
	@Test
	public void shouldGetSomePatientsFromListOfPatientIds() {
		IdcardsService service = (IdcardsService) Context.getService(IdcardsService.class);
		List<Integer> patientIds = new ArrayList<Integer>();
		patientIds.add(2);
		patientIds.add(3);
		patientIds.add(4);
		List<Patient> patients = service.getPatientsByPatientId(patientIds);
		for (Patient p : patients) {
			System.out.println(p.getPatientId() + ", " + p.getPatientIdentifier());
		}
		assertEquals(Long.valueOf(patientIds.size()), Long.valueOf(patients.size()));
	}
	
	/**
	 * Tests that IdcardsExportFunctions#getPreviousPatientIdentifiers() returns a list of
	 * PatientIdentifiers with cardinality of one less than getPatient().getIdentifiers().
	 */
	@Test
	public void shouldAssertPreviousPatientIdentifiersContainsOneLessThanAllPatientIdentifiers() {
		//Patient patient = Context.getPatientService().getPatient(10);
		IdcardsExportFunctions fn = new IdcardsExportFunctions(2);
		System.out.println("patient identifiers: " + fn.getPatient().getIdentifiers());
		System.out.println("previous identifiers: " + fn.getPreviousPatientIdentifiers());
		assertEquals(fn.getPatient().getIdentifiers().size(), fn.getPreviousPatientIdentifiers().size() + 1);
	}
	
	/**
	 * Test to see if we get a Person Attribute from DataExportFunctions
	 */
	@Test
	public void shouldGetPatientAttributeFromDataExportFunctions() {
		IdcardsExportFunctions fn = new IdcardsExportFunctions(4);
		Object attr = fn.getPersonAttribute("Contact Phone Number");
		System.out.println(attr);
		assertNotNull(attr);
	}
	
	@Test
	public void shouldGetPersonAddressFromDataExportFuctions() {
		IdcardsExportFunctions fn = new IdcardsExportFunctions(4);
		PersonAddress address = fn.getPatient().getPersonAddress();
		System.out.println("region " + address.getAddress6());
		System.out.println("subregion " + address.getAddress5());
		assertNotNull(address);
	}
	
	/**
	 * Print the contents of the given tableName to system.out Call this from any
	 * {@link BaseContextSensitiveTest} child by: TestUtil.printOutTableContents(getConnection(),
	 * "encounter");
	 * 
	 * @param sqlConnection the connection to use
	 * @param tableNames the name(s) of the table(s) to print out
	 * @throws Exception
	 */
	public static void printOutTableContents(Connection sqlConnection, String... tableNames) throws Exception {
		for (String tableName : tableNames) {
			System.out.println("The contents of table: " + tableName);
			IDatabaseConnection connection = new DatabaseConnection(sqlConnection);
			DatabaseConfig config = connection.getConfig();
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
			QueryDataSet outputSet = new QueryDataSet(connection);
			outputSet.addTable(tableName);
			FlatXmlDataSet.write(outputSet, System.out);
		}
	}
}
