package gatech.hi.fhirpit.app.cron;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.ReferralRequest;
import ca.uhn.fhir.rest.client.IGenericClient;
import gatech.hi.fhirpit.app.config.FHIRconfig;
import gatech.hi.fhirpit.app.domain.RequestInfo;
import gatech.hi.fhirpit.app.repo.RequestsRepo;

@Component
public class RefferalRequestForwarder {

	
	
	@Autowired
	FHIRconfig config;
	
	@Autowired
	RequestsRepo repo;
	
	@Value("${service.host}")	
	String referralServiceEndPoint ; 
	
	public void forwardRefferal(ReferralRequest req)
	{
		
		System.out.println("Forwarding ReferralRequest >>" + req.getId().getIdPart());
		IGenericClient client = config.getClient();
		String patientId = req.getPatient().getReference().getValueAsString();
		System.out.println("patient name is >>>" + patientId);
		patientId = StringUtils.substringAfter(patientId, "/");
		System.out.println("Fetching Details for Patient Id >>" + patientId);
		
	    Bundle refferals = client
	                .search()
	                .forResource(Patient.class)      
	                //.where(Patient.IDENTIFIER.exactly().code(patientId))
	                .where(Patient.RES_ID.matches().value(patientId))
	                .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
	                .execute();  
	    
	    Patient patient = null;
        for(Entry en : refferals.getEntry())
        {
        	patient = (Patient) en.getResource();
        	System.out.println("patient >>" +patient.getResourceName() + patient.getId());        	
        }
        
    	String reqInfo = createReqInfo(req,patient);
    	forwardRequest(reqInfo);
        
        repo.strorReferralRequest(req);
    	
//    	JSONObject j   = new JSONObject(reqInfo);
//    	System.out.println(j.toString());
		
		
	}

	public void forwardRequest(String reqInfo)
	{
		System.out.println("reqInfo >>>" + reqInfo);
		RestTemplate restTemplate = new RestTemplate();		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> entity = new HttpEntity<String>(reqInfo,headers);
		String resp = restTemplate.postForObject(referralServiceEndPoint, entity, String.class);
		System.out.println("resp >>" +resp);
		
	}

	

	private String createReqInfo(ReferralRequest req, Patient patient) {
		
		
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setReferalId(req.getId().getIdPart());
		for(ResourceReferenceDt rf : req.getSupportingInformation())
		{
			requestInfo.setService(rf.getReference().getValue());
		}

	    if(patient==null)
	    	return "";
		
		for(HumanNameDt name : patient.getName())
		{
			
			requestInfo.setName(name.getNameAsSingleString());
			System.out.println("name >>>>>" + name.getNameAsSingleString());
		}
		
		for(AddressDt add : patient.getAddress())
		{
			requestInfo.setAddress(add.getLine() + " " + add.getCity() + " " + add.getState() + " " + add.getCountry() + " " + add.getPostalCode());
			//System.out.println("adddress >>>>>" + add.getCity() + add.getState() + add.getCountry() + add.getLine() );
		}
		
		for(ContactPointDt ct : patient.getTelecom())
		{	
			requestInfo.setContactInfo(ct.getSystem() + " : " + ct.getValue() + " \t");
			System.out.println("telecom >>>>>" + ct.getSystem() + ">>>" + ct.getValue());
		}
		
		requestInfo.setDob(patient.getBirthDate() + "");
		
		requestInfo.setGender(patient.getGender());
	
		
		System.out.println( patient.getBirthDate() + ">>>>" + patient.getMaritalStatus().getText());
		
     	JSONObject j   = new JSONObject(requestInfo);
     	
    	return j.toString();
		
	}
	
	
//	public static void main(String[] args) {
//		
//	    String serverBase = "https://secure-api.hspconsortium.org/FHIRPit/open";
//		FhirContext ctx = FhirContext.forDstu2();
//		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
//		
//		Bundle refferals = client.search().forResource(Patient.class)
//				.where(Patient.RES_ID.matches().value("SMART-2169591"))
//				//.where(Patient.IDENTIFIER.exactly().identifier("SMART-621799"))
//				//.where(Patient.RES_ID.matches().value("BILIBABY"))
//				.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class).execute();
//		
//		System.out.println("refferals >>>" + refferals + ">>>>>>>>" + refferals.getTotal());
//		
//		
//		Patient patient = null;
//		for (Entry en : refferals.getEntry()) {
//			patient = (Patient) en.getResource();
//			System.out.println("patient >>" + patient.getName() + "Id >>>>>" + patient.getIdentifier());	
//
//		}		
//
//		createReqInfo2(patient);
//		
//	}
	
//	private static void createReqInfo2(Patient patient) {
//		
//		
//		
//		RequestInfo requestInfo = new RequestInfo();
//
//	    if(patient==null)
//	    	return;
//		
//		for(HumanNameDt name : patient.getName())
//		{
//			
//			requestInfo.setName(name.getNameAsSingleString());
//			//System.out.println("name >>>>>" + name.getNameAsSingleString());
//		}
//		
//		for(AddressDt add : patient.getAddress())
//		{
//			requestInfo.setAddress(add.getLine() + " " + add.getCity() + " " + add.getState() + " " + add.getCountry() + " " + add.getPostalCode());
//			//System.out.println("adddress >>>>>" + add.getCity() + add.getState() + add.getCountry() + add.getLine() );
//		}
//		
//		for(ContactPointDt ct : patient.getTelecom())
//		{	
//			requestInfo.setContactInfo(ct.getSystem() + " : " + ct.getValue() + " \t");
//			//System.out.println("telecom >>>>>" + ct.getSystem() + ">>>" + ct.getValue());
//		}
//		
//		requestInfo.setDob(patient.getBirthDate() + "");		
//		//System.out.println( patient.getBirthDate() + ">>>>" + patient.getMaritalStatus().getText());		
//     	JSONObject j   = new JSONObject(requestInfo);
//    	System.out.println(j.toString());
//    	
//    	System.out.println("Frowareded Requests >>>>>>\n\n/n/n");
//		
//		
//	}
	
}
