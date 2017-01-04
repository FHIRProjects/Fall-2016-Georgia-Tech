package gatech.hi.fhirpit.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.ReferralRequest;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.valueset.ReferralStatusEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import gatech.hi.fhirpit.app.config.FHIRconfig;

@RestController
public class UtilAPIs {
	
	@Autowired
	FHIRconfig config;
	
	  @RequestMapping("/reset")
	    public String resetStatus() {

//	      Bundle results = client
//	            .search()
//	            .forResource(Patient.class)
//	            .where(Patient.FAMILY.matches().value("Goff"))
//	            .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
//	            .execute();

	        //return "Found " + results.getEntry().size() + " patients named 'Goff'";
	        
		  IGenericClient client = config.getClient();
	        
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
	            
	            
	            
	            
	           return "reset";      
	             
	            
	            
	    }

}
