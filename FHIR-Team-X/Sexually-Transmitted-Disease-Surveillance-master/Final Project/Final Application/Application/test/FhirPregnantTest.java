import org.junit.*;
import static org.junit.Assert.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;

public class FhirPregnantTest {
    
    public void createPregnantConditionForMarla() {
        FhirContext ctx = FhirContext.forDstu2();
        String serverBase = "http://162.249.6.176:8080/baseDstu3";
        String patientId = "12345";
 
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
        Bundle bundle = client.search().forResource(XPatient.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode("urn:system", patientId))
            .execute();
        if (bundle.size() < 1) {
            FhirXPatientTest fpt = new FhirXPatientTest();
            fpt.createXPatientFemale();
            bundle = client.search().forResource(XPatient.class)
                .where(new TokenClientParam("identifier").exactly().systemAndCode("urn:system", patientId))
                .execute();
        }
        XPatient marla = (XPatient)bundle.getResources(XPatient.class).get(0);
        assertEquals("Dixon", marla.getName().get(0).getFamily().get(0).toString());
        assertEquals("Marla", marla.getName().get(0).getGiven().get(0).toString());
        assertEquals("female", marla.getGender());
        
        CodeableConceptDt pregnancyCoding = new CodeableConceptDt("http://snomed.info/sct", "77386006");
        pregnancyCoding.setText("Patient currently pregnant (finding)");
        Condition cond = new Condition();
        cond.addIdentifier().setSystem("urn:system").setValue(patientId);
        cond.setCode(pregnancyCoding);
        cond.setPatient(new ResourceReferenceDt(marla.getId()));
        
        MethodOutcome outcome = client.create()
            .resource(cond)
            .conditional()
            .where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", patientId))
            .and(Condition.CODE.exactly().code("77386006"))
            .execute();
    }
}