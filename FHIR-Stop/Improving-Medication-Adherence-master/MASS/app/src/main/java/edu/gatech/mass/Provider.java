package edu.gatech.mass;

public class Provider extends User {
	
	private int providerID;		// the provider's ID from FHIR
	private String phoneNumber;	// the provider's work phone number
	
	/**
	 * Initializes instance of Provider.
	 *
	 * * @param name
	 * 		The user's name
	 * * @param email
	 * 		The user's email address
	 * * @param userID
	 * 		The user's custom ID used for logging in
	 * * @param password
	 * 		The user's password used for logging in
	 * * @param providerID
	 * 		The user's provider ID from FHIR
	 * * @param phoneNumber
	 * 		The user's work phone number
	 */
	public Provider(String name, String email, String userID, String password, int providerID, String phoneNumber) {
		super(name, email, userID, password);
		this.providerID = providerID;
		this.phoneNumber = phoneNumber;
	}
	
	public int getProviderID() {
		return providerID;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
}