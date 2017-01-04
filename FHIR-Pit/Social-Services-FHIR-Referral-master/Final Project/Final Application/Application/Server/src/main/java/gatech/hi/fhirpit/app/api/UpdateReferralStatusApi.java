package gatech.hi.fhirpit.app.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.model.dstu2.resource.ReferralRequest;
import ca.uhn.fhir.model.dstu2.valueset.ReferralStatusEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import gatech.hi.fhirpit.app.config.FHIRconfig;
import gatech.hi.fhirpit.app.repo.RequestsRepo;

@RestController
public class UpdateReferralStatusApi {
      @Autowired
      RequestsRepo repo;
      
	  @Autowired
	  FHIRconfig config;
	
	  @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.PUT)
	    public ResponseEntity<String> updateUser(@PathVariable("id") String id, @RequestParam("status") String status) {
	        System.out.println("Updating Referal Request " + id + "status >>" + status);
	        IGenericClient client = config.getClient();
	        ReferralRequest req = repo.getReferralRequest(id);
	        System.out.println("Updating Referral Request >>" + req);
	        
	        if(req !=null)
	        {
	        	
	        	
	        	req.setStatus(getStatus(status));            	
               	MethodOutcome res = client.update().resource(req).execute();
               	res.getOperationOutcome();
	        	
	        }
	         
	        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	        params.set("status", "done");
	     
	       return new ResponseEntity<String>(params,HttpStatus.OK);
	    }

	private ReferralStatusEnum getStatus(String status) {
	
		if(StringUtils.equals("accepted", status))
		{
		   return ReferralStatusEnum.ACCEPTED;	
		}
		if(StringUtils.equals("completed", status))
		{
			return ReferralStatusEnum.COMPLETED;
		}
		
		return ReferralStatusEnum.ACCEPTED;	
	}
	
	
	
}
