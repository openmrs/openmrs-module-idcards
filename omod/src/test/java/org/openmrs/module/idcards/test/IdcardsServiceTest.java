/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmrs.module.idcards.test;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.List;

/**
 * This should peform tests on the IdcardsService with an in memory database.
 * TODO: So far there are not any tests for the IdcardsService. 
 */
public class IdcardsServiceTest extends BaseModuleContextSensitiveTest {

//    protected static final String INITIAL_DATASET_XML = "org/openmrs/test/api/include/exampleTestDataset.xml";
    protected static final String CREATE_PATIENT_XML = "org/openmrs/module/idcards/include/PatientServiceTest-createPatient.xml";
    protected static final String CREATE_PATIENT_VALID_IDENT_XML = "org/openmrs/module/idcards/include/PatientServiceTest-createPatientValidIdent.xml";
    protected static final String JOHN_PATIENTS_XML = "org/openmrs/module/idcards/include/PatientServiceTest-lotsOfJohns.xml";
    protected static final String USERS_WHO_ARE_PATIENTS_XML = "org/openmrs/module/idcards/include/PatientServiceTest-usersWhoArePatients.xml";
    protected static final String FIND_PATIENTS_XML = "org/openmrs/module/idcards/include/PatientServiceTest-findPatients.xml";

    /**
     * Initialize in memory database tables.
     * @throws Exception
     */
    @Before
    public void runBeforeEachTest() throws Exception {
        initializeInMemoryDatabase();
        authenticate();
        executeDataSet(CREATE_PATIENT_XML);
        executeDataSet(CREATE_PATIENT_VALID_IDENT_XML);
        executeDataSet(JOHN_PATIENTS_XML);
        executeDataSet(USERS_WHO_ARE_PATIENTS_XML);
        executeDataSet(FIND_PATIENTS_XML);
    }

    /**
     * Test to see if we can even authenticate to the in memory database.
     */
    @Test
    public void shouldShowSomeInMemoryDatabase() {
        User u = Context.getAuthenticatedUser();
        assertNotNull(u);
    }
    
    /**
     * Test to see if we can get any patients from the in memory database
     */
    @Test
    public void shouldGetSomethingFromPatientService() {
        List<Patient> patients = Context.getPatientService().getAllPatients();
        assertNotNull(patients);
    }

}