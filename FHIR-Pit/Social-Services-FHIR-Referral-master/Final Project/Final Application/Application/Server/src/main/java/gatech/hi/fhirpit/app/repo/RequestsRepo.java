package gatech.hi.fhirpit.app.repo;

import ca.uhn.fhir.model.dstu2.resource.ReferralRequest;

public interface RequestsRepo {
	
	
	public ReferralRequest getReferralRequest(String id);

	public void strorReferralRequest(ReferralRequest req);


}
