package org.openmrs.module.idcards;

import org.openmrs.reporting.export.DataExportFunctions;
import org.openmrs.PatientIdentifier;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p/>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p/>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
public class IdcardsExportFunctions extends DataExportFunctions {

    public IdcardsExportFunctions() {
        super();
    }

    public IdcardsExportFunctions(Patient patient) {
        super(patient);
    }

    public IdcardsExportFunctions(Integer patientId) {
        super(patientId);
    }

    /**
     * Get the previously used Patient Identifiers for the super.patientId Patient
     * to be used in a Velocity context.
     * @return
     */
    public List<PatientIdentifier> getPreviousPatientIdentifiers() {
        Set<PatientIdentifier> allIdentifiers = getPatient().getIdentifiers();
        PatientIdentifier curIdentifier = getPatient().getPatientIdentifier();
        List<PatientIdentifier> prevIdentifiers = new ArrayList<PatientIdentifier>(allIdentifiers.size());
        prevIdentifiers.addAll(allIdentifiers);
        prevIdentifiers.remove(curIdentifier);
        return prevIdentifiers;
    }
}
