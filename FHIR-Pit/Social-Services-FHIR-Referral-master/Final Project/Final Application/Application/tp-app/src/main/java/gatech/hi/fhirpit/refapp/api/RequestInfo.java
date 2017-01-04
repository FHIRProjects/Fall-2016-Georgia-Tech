package gatech.hi.fhirpit.refapp.api;

public class RequestInfo {

	  public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getEmail() {
		return contactInfo;
	}
	public void setEmail(String email) {
		this.contactInfo = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getReferalId() {
		return referalId;
	}
	public void setReferalId(String referalId) {
		this.referalId = referalId;
	}
	String name ;
	  String patientName ;
      String contactInfo;
	  public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	String gender;
	  String dob;
	  String referalId;
	  String address;
	  String service;
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
   
}
