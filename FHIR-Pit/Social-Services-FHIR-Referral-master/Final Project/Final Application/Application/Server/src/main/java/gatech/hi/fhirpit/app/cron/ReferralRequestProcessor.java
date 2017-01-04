package gatech.hi.fhirpit.app.cron;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.ReferralRequest;
import ca.uhn.fhir.model.dstu2.valueset.ReferralStatusEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import gatech.hi.fhirpit.app.config.FHIRconfig;

@Component
@EnableAutoConfiguration
public class ReferralRequestProcessor {

	@Autowired
	FHIRconfig config;

    @Autowired
    RefferalRequestForwarder forwarder;
	
    
    @Scheduled(fixedRate = 60*1000)
    public void processsReferralRequests() {
        
    	
    	System.out.println("processing refferal requests !!!");
    	IGenericClient client = config.getClient();
    	
        Bundle refferals = client
                .search()
                .forResource(ReferralRequest.class)               
                .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                .execute();        
            
            
            for(Entry en : refferals.getEntry())
            {
            	ReferralRequest r = (ReferralRequest) en.getResource();           
            	System.out.println(">>>>"+ r.getId() + r.getPatient() + r.getStatus() + r.getDate() + r.getSupportingInformation());
            	
                if(StringUtils.equals("requested",r.getStatus()))
                {
                	System.out.println("new request found :" + r.getId() + r.getPatient() );
                	forwarder.forwardRefferal(r);                    
                    r.setStatus(ReferralStatusEnum.ACTIVE);            	
                	MethodOutcome res = client.update().resource(r).execute();
                	res.getOperationOutcome();
                	System.out.println("changed status >>>" + res.getCreated() + res.getOperationOutcome() );                	
                	
                }   	            	
            }             
            
    }

  

}
