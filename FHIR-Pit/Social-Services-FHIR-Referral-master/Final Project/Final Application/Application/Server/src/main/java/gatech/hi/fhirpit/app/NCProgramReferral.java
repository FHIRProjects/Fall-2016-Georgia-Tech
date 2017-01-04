package gatech.hi.fhirpit.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.ReferralRequest;
import ca.uhn.fhir.model.dstu2.valueset.ReferralStatusEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class NCProgramReferral {

    private static FhirContext ctx = null;
    private static IGenericClient client = null;
    private static final String serverBase = "https://secure-api.hspconsortium.org/FHIRPit/open";
    //private static final String serverBase = "http://52.172.48.21:8080/fhir-omopv5/base";

    @RequestMapping("/")
    String home() {

      Bundle results = client
            .search()
            .forResource(Patient.class)
            .where(Patient.FAMILY.matches().value("Goff"))
            .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
            .execute();

        //return "Found " + results.getEntry().size() + " patients named 'Goff'";
        
        
        Bundle results2 = client
                .search()
                .forResource(ReferralRequest.class)
                //.where(ReferralRequest.STATUS.exactly().equalsIgnoreCase("requested"))
                .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                .execute();

           // return "Found " + results.getEntry().size() + " patients named 'Goff'" + "  results >>>" + results2.getResourceName() + ">>>" + results2.getType();
            
            
            for(Entry en : results2.getEntry())
            {
            	ReferralRequest r = (ReferralRequest) en.getResource();
            	System.out.println(en.getResource() + "" + en.getId() + en.getResponse());
            	System.out.println(">>>>"+ r.getId() + r.getPatient() + r.getStatus() + r.getDate() + r.getSupportingInformation());
                r.setStatus(ReferralStatusEnum.REQUESTED);            	
            	MethodOutcome res = client.update().resource(r).execute();
            	res.getOperationOutcome();
            	System.out.println("changed status >>>" + res.getCreated() + res.getOperationOutcome() );
            	
            }
            
            
            
            
             return "Found " + results.getEntry().size() + " patients named 'Goff'" + "  results >>>" + results2.getResourceName() + ">>>" + results2.getTotal();
             
             
            
            
    }
    
    
    public static void main(String[] args) throws Exception {

        // Create FHIR client
        ctx = FhirContext.forDstu2();
        client = ctx.newRestfulGenericClient(serverBase);

        SpringApplication.run(NCProgramReferral.class, args);
    }

}
