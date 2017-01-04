package gatech.hi.fhirpit.app.repo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.ReferralRequest;
import ca.uhn.fhir.rest.client.IGenericClient;
import gatech.hi.fhirpit.app.config.FHIRconfig;

@Component
public class RequestsRepoInMemoryImpl implements RequestsRepo
{
	
	
	@Autowired
	FHIRconfig config;

	Map<String, ReferralRequest> reqRepo = new HashMap<String, ReferralRequest>();
	
	@Override
	public ReferralRequest getReferralRequest(String id) {

//		ReferralRequest req = reqRepo.get(id);
//		if(null == req)
//		{
//			return getReferralRequestFromFHIR(id);   
//			
//		}
		return getReferralRequestFromFHIR(id); 
		//return req;
	}


	private ReferralRequest getReferralRequestFromFHIR(String id) {
		System.out.println("Fetching refferal requests from FHIR!!!");
		IGenericClient client = config.getClient();
		
		Bundle refferals = client
		        .search()
		        .forResource(ReferralRequest.class)    
		        .where(ReferralRequest.RES_ID.matches().value(id))
		        .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
		        .execute();
		
		ReferralRequest rr = null;
        for(Entry en : refferals.getEntry())
        {
        	rr = (ReferralRequest) en.getResource();
        	System.out.println("patient >>" +rr.getId().getIdPart());  
        	return rr;
        }
        
        return null;
	}
	
	
	@Override
	public void strorReferralRequest(ReferralRequest req) {

		reqRepo.put(req.getId().getIdPart(), req);
	}
	
	
	


}
