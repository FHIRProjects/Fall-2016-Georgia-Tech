import org.junit.*;
import static org.junit.Assert.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.instance.hapi.validation.FhirInstanceValidator;
import ca.uhn.fhir.validation.ValidationResult;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierTypeCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;

public class FhirXPatientTest {

    public void createXPatientFemale() {
        FhirContext ctx = FhirContext.forDstu2();
        String serverBase = "http://162.249.6.176:8080/baseDstu3";
        String patientId = "12345";
 
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
        XPatient patient = new XPatient();
        patient.addIdentifier().setSystem("urn:system").setValue(patientId);
        patient.addIdentifier().setSystem("http://hl7.org/fhir/sid/us-ssn").setType(IdentifierTypeCodesEnum.SOCIAL_BENEFICIARY_IDENTIFIER).setUse(IdentifierUseEnum.USUAL).setValue("123456789");
        patient.addName().addFamily("Dixon").addGiven("Marla");
        patient.setGender(AdministrativeGenderEnum.FEMALE);
        
        CodingDt raceCoding = new CodingDt();
        raceCoding.setSystem("http://hl7.org/fhir/v3/Race");
        raceCoding.setCode("2056-0");
        raceCoding.setDisplay("Black");
        patient.setRace(raceCoding);
        
        CodingDt ethnicityCoding = new CodingDt();
        ethnicityCoding.setSystem("http://hl7.org/fhir/StructureDefinition/us-core-ethnicity");
        ethnicityCoding.setCode("2186-5");
        ethnicityCoding.setDisplay("Not Hispanic or Latino");
        patient.setEthnicity(ethnicityCoding);
        
        DateDt birthDate = new DateDt(1970, 2, 14);
        patient.setBirthDate(birthDate);
        
        patient.setOccupation(new StringDt("Secretary"));
        // Conditional create, only create if it doesn't already exist
        MethodOutcome outcome = client.create()
            .resource(patient)
            .conditional()
            .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system", patientId))
            .execute();
            
        Bundle bundle = client.search().forResource(XPatient.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode("urn:system", patientId))
            .execute();
        assertEquals(bundle.size(), 1);
        XPatient marla = (XPatient)bundle.getResources(XPatient.class).get(0);
        assertEquals("2056-0", marla.getRace().getCode());
        assertEquals("2186-5", marla.getEthnicity().getCode());
        assertEquals("Secretary", marla.getOccupation().toString());
        assertEquals("Dixon", marla.getName().get(0).getFamily().get(0).toString());
        assertEquals("Marla", marla.getName().get(0).getGiven().get(0).toString());
        assertEquals("female", marla.getGender());
        
        /*client.delete()
            .resourceConditionalByType("Patient")
            .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system", patientId))
            .execute();
            
        bundle = client.search().forResource(XPatient.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode("urn:system", patientId))
            .execute();
        assertEquals(bundle.size(), 0);
    }
    
    
    public void createXPatientMale() {
        FhirContext ctx = FhirContext.forDstu2();
        String serverBase = "http://162.249.6.176:8080/baseDstu3";
        String patientId = "34567";
 
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
        XPatient patient = new XPatient();
        patient.addIdentifier().setSystem("urn:system").setValue(patientId);
        patient.addIdentifier().setSystem("http://hl7.org/fhir/sid/us-ssn").setType(IdentifierTypeCodesEnum.SOCIAL_BENEFICIARY_IDENTIFIER).setUse(IdentifierUseEnum.USUAL).setValue("987654321");
        patient.addName().addFamily("Boggs").addGiven("Wade");
        patient.setGender(AdministrativeGenderEnum.MALE);
        
        CodingDt raceCoding = new CodingDt();
        raceCoding.setSystem("http://hl7.org/fhir/v3/Race");
        raceCoding.setCode("2106-3");
        raceCoding.setDisplay("White");
        patient.setRace(raceCoding);
        
        CodingDt ethnicityCoding = new CodingDt();
        ethnicityCoding.setSystem("http://hl7.org/fhir/StructureDefinition/us-core-ethnicity");
        ethnicityCoding.setCode("2186-5");
        ethnicityCoding.setDisplay("Not Hispanic or Latino");
        patient.setEthnicity(ethnicityCoding);
        
        patient.setOccupation(new StringDt("Retired baseball player"));
        // Conditional create, only create if it doesn't already exist
        MethodOutcome outcome = client.create()
            .resource(patient)
            .conditional()
            .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system", patientId))
            .execute();
            
        Bundle bundle = client.search().forResource(XPatient.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode("urn:system", patientId))
            .execute();
        assertEquals(bundle.size(), 1);
        XPatient wade = (XPatient)bundle.getResources(XPatient.class).get(0);
        assertEquals("2106-3", wade.getRace().getCode());
        assertEquals("2186-5", wade.getEthnicity().getCode());
        assertEquals("Retired baseball player", wade.getOccupation().toString());
        assertEquals("Boggs", wade.getName().get(0).getFamily().get(0).toString());
        assertEquals("Wade", wade.getName().get(0).getGiven().get(0).toString());
        assertEquals("male", wade.getGender());
        
        /*client.delete()
            .resourceConditionalByType("Patient")
            .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system", patientId))
            .execute();
            
        bundle = client.search().forResource(XPatient.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode("urn:system", patientId))
            .execute();
        assertEquals(bundle.size(), 0);*/
    }
    
    
    public void validateXPatient() {
        FhirContext ctx = FhirContext.forDstu2();
        FhirValidator validator = ctx.newValidator();
        FhirInstanceValidator instanceValidator = new FhirInstanceValidator();
        validator.registerValidatorModule(instanceValidator);
        
        XPatient patient = new XPatient();
        CodingDt raceCoding = new CodingDt();
        raceCoding.setSystem("http://hl7.org/fhir/v3/Race");
        raceCoding.setCode("2056-0");
        raceCoding.setDisplay("Black");
        patient.setRace(raceCoding);
        
        CodingDt ethnicityCoding = new CodingDt();
        ethnicityCoding.setSystem("http://hl7.org/fhir/StructureDefinition/us-core-ethnicity");
        ethnicityCoding.setCode("2186-5");
        ethnicityCoding.setDisplay("Not Hispanic or Latino");
        patient.setEthnicity(ethnicityCoding);
        patient.setOccupation(new StringDt("Golf player"));
        
        ValidationResult result = validator.validateWithResult(patient);
        assertEquals(true, result.isSuccessful());
   }
}