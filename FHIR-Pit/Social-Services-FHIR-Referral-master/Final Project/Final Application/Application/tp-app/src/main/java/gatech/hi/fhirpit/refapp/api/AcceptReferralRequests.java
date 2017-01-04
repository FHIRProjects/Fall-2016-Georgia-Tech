package gatech.hi.fhirpit.refapp.api;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AcceptReferralRequests {
    @Autowired
	ReferralRepo repo;
	
	
    @RequestMapping( method = RequestMethod.POST, value = "/createReferral/")
    public @ResponseBody MultiValueMap<String, String> acceptReferral(@RequestBody RequestInfo req) {
        System.out.println("Creating referral requests " + req.getReferalId());
        repo.acceptRequestInfo(req);
       
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set("status", "done");     
      // return new ResponseEntity<String>(params,HttpStatus.OK);       
        return params;
    }
    
    
    @RequestMapping(value = "/getReferral", method = RequestMethod.GET)
    public ResponseEntity<RequestInfo> getReferral(@RequestParam String reqId) {
    	RequestInfo info = repo.getRequestInfo(reqId); 
       return new ResponseEntity<RequestInfo>(info,HttpStatus.OK);        
    }
    
    
    @RequestMapping(value = "/getAllReferral", method = RequestMethod.GET)
    public ResponseEntity<List<RequestInfo>> getAllReferral() {
       List<RequestInfo> info = repo.getAllRequestInfo();
       return new ResponseEntity<List<RequestInfo>>(info,HttpStatus.OK);        
    }
    
	
}
