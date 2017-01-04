import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.DiagnosticRequest;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;

public class FhirBundleTest {

    /* The following example shows how to post a transaction with two resources, where one resource contains a reference to the other. A temporary ID (a UUID) is used as an ID to refer to, and this ID will be replaced by the server by a permanent ID. */
    public void createPractitionerAndLocationInTransaction() {
        FhirContext ctx = FhirContext.forDstu3();
        String serverBase = "http://162.249.6.176:8080/baseDstu3";
        String id = "12345";
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
        
        String practitionerSystem = "urn:system";
        String practitionerId = "12345";       
        client.delete()
            .resourceConditionalByType("Practitioner")
            .where(Practitioner.IDENTIFIER.exactly().systemAndIdentifier(practitionerSystem, practitionerId))
            .execute();
        Bundle bundle = client.search().forResource(Practitioner.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode(practitionerSystem, practitionerId))
            .returnBundle(Bundle.class)
            .execute();
        assertEquals(bundle.getEntry().size(), 0);
        
        String locationSystem = "urn:system";
        String locationId = "12345";
        client.delete()
            .resourceConditionalByType("Location")
            .where(Location.IDENTIFIER.exactly().systemAndIdentifier(locationSystem, locationId))
            .execute();
        bundle = client.search().forResource(Location.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode(locationSystem, locationId))
            .returnBundle(Bundle.class)
            .execute();
        assertEquals(bundle.getEntry().size(), 0);
 
        /*IGenericClient client = ctx.newRestfulGenericClient(serverBase);
        Location loc = new Location();
        loc.addIdentifier().setSystem("urn:system").setValue(id);
        loc.setName("Fairbanks Medical Practice");
        loc.setId(IdDt.newRandomUuid());
        
        Practitioner prac = new Practitioner();
        prac.addIdentifier().setSystem("urn:system").setValue(id);
        prac.setId(IdDt.newRandomUuid());
        HumanNameDt hn = new HumanNameDt();
        hn.addGiven("Doug");
        hn.addFamily("Fairbanks");
        prac.setName(hn);
        PractitionerRole pr = new PractitionerRole();
        pr.setLocation(Arrays.asList(new ResourceReferenceDt(loc.getId())));
        prac.setPractitionerRole(Arrays.asList(pr));
        
        ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
        bundle.setType(BundleTypeEnum.TRANSACTION);
        
        bundle.addEntry()
            .setFullUrl(loc.getId())
            .setResource(loc)
            .getRequest()
                .setUrl("Location") // setIfNoneExist for conditional insertion
                .setIfNoneExist("Location?identifier=urn:system|" + id)
                .setMethod(HTTPVerbEnum.POST);

        bundle.addEntry()
            .setFullUrl(prac.getId())
            .setResource(prac)
            .getRequest()
                .setUrl("Practitioner") // setIfNoneExist for conditional insertion
                .setIfNoneExist("Practitioner?identifier=urn:system|" + id)
                .setMethod(HTTPVerbEnum.POST);
        
        client.transaction().withBundle(bundle).execute();*/
    }
    /* Gets the Practitioner resource created above along with the referenced Location resource*/
    /*public void getPractitionerAndLocationTogether() {
        FhirContext ctx = FhirContext.forDstu3();
        String serverBase = "http://162.249.6.176:8080/baseDstu3";
        String id = "12345";
 
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
        ca.uhn.fhir.model.api.Bundle bundle = client.search().forResource(Practitioner.class)
            .where(new TokenClientParam("identifier").exactly().systemAndCode("urn:system", id))
            .include(Practitioner.INCLUDE_LOCATION.asNonRecursive())
            .execute();
            
        assertEquals(2, bundle.size());
        
        Practitioner prac = (Practitioner)bundle.getResources(Practitioner.class).get(0);
        assertEquals("Fairbanks", prac.getName().getFamily().get(0).toString());
        
        Location loc = (Location)bundle.getResources(Location.class).get(0);
        assertEquals("Fairbanks Medical Practice", loc.getName());
    }*/
}