import org.junit.*;
import static org.junit.Assert.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;

public class FhirDeleteCaseReport {

    public void deletePertussisExample() {
        FhirContext ctx = FhirContext.forDstu3();
        String serverBase = "http://162.249.6.176:8080/baseDstu3";
 
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
        Bundle bundle;
        
        String diagReportSystem = "urn:system";
        String diagReportId = "a4307cb2-b3b4-4f42-be03-1d9077376f4a";
        client.delete()
            .resourceConditionalByType("DiagnosticReport")
            .where(DiagnosticReport.IDENTIFIER.exactly().systemAndIdentifier(diagReportSystem, diagReportId))
            .execute();
        bundle = client.search().forResource(DiagnosticReport.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode(diagReportSystem, diagReportId))
            .returnBundle(Bundle.class)
            .execute();
        assertEquals(bundle.getEntry().size(), 0);
        
        String observationSystem = "urn:system";
        String observationId = "7c0704bb-9c40-41b5-9c7d-26b2d59e234f[0]";
        client.delete()
            .resourceConditionalByType("Observation")
            .where(Observation.IDENTIFIER.exactly().systemAndIdentifier(observationSystem, observationId))
            .execute();
        bundle = client.search().forResource(Observation.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode(observationSystem, observationId))
            .returnBundle(Bundle.class)
            .execute();
        assertEquals(bundle.getEntry().size(), 0);
        
        observationId = "7c0704bb-9c40-41b5-9c7d-26b2d59e234f[1]";
        client.delete()
            .resourceConditionalByType("Observation")
            .where(Observation.IDENTIFIER.exactly().systemAndIdentifier(observationSystem, observationId))
            .execute();
        bundle = client.search().forResource(Observation.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode(observationSystem, observationId))
            .returnBundle(Bundle.class)
            .execute();
        assertEquals(bundle.getEntry().size(), 0);
        
        observationId = "bf9c0a26-4524-4395-b3ce-100450b9c9ac[0]";
        client.delete()
            .resourceConditionalByType("Observation")
            .where(Observation.IDENTIFIER.exactly().systemAndIdentifier(observationSystem, observationId))
            .execute();
        bundle = client.search().forResource(Observation.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode(observationSystem, observationId))
            .returnBundle(Bundle.class)
            .execute();
        assertEquals(bundle.getEntry().size(), 0);
        
        String encounterSystem = "urn:system";
        String encounterId = "9937012";       
        client.delete()
            .resourceConditionalByType("Encounter")
            .where(Encounter.IDENTIFIER.exactly().systemAndIdentifier(encounterSystem, encounterId))
            .execute();
        bundle = client.search().forResource(Encounter.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode(encounterSystem, encounterId))
            .returnBundle(Bundle.class)
            .execute();
        assertEquals(bundle.getEntry().size(), 0);
        
        String practitionerSystem = "2.16.840.1.113883.4.6";
        String practitionerId = "6666666666666";       
        client.delete()
            .resourceConditionalByType("Practitioner")
            .where(Practitioner.IDENTIFIER.exactly().systemAndIdentifier(practitionerSystem, practitionerId))
            .execute();
        bundle = client.search().forResource(Practitioner.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode(practitionerSystem, practitionerId))
            .returnBundle(Bundle.class)
            .execute();
        assertEquals(bundle.getEntry().size(), 0);
        
        String patientSystem = "2.16.840.1.113883.4.1";
        String patientId = "111001234";
        client.delete()
            .resourceConditionalByType("Patient")
            .where(Patient.IDENTIFIER.exactly().systemAndIdentifier(patientSystem, patientId))
            .execute();
        bundle = client.search().forResource(Patient.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode(patientSystem, patientId))
            .returnBundle(Bundle.class)
            .execute();
        assertEquals(bundle.getEntry().size(), 0);
        
        String locationSystem = "2.16.840.1.113883.4.6";
        String locationId = "77777777777";
        client.delete()
            .resourceConditionalByType("Location")
            .where(Location.IDENTIFIER.exactly().systemAndIdentifier(locationSystem, locationId))
            .execute();
        bundle = client.search().forResource(Location.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode(locationSystem, locationId))
            .returnBundle(Bundle.class)
            .execute();
        assertEquals(bundle.getEntry().size(), 0);
    }
}