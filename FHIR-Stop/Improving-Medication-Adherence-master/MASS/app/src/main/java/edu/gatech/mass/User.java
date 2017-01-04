package edu.gatech.mass;

public class User {
	
	private String name;		// user's name
	private String email;		// user's email
	private String userID;		// user ID used to log in
	private String password;	// password used to log in
	private Boolean loginStatus;	// whether the user is currently logged in
	
	/**
	 * Initializes instance of User.
	 * * @param name
	 * 		The user's name
	 * * @param email
	 * 		The user's email address
	 * * @param userID
	 * 		The user's custom ID used for logging in
	 * * @param password
	 * 		The user's password used for logging in
	 */
	public User(String name, String email, String userID, String password) {
		this.name = name;
		this.email = email;
		this.userID = userID;
		this.password = password;
		loginStatus = false;
	}
	
	/**
	 * Logs in a user if the provided password is correct.
	 * 
	 * * @param password
	 * 		The provided password used to login
	 */
	public void login(String password) {
		if (this.password.equals(password)) {
			loginStatus = true;
		}
	}
	
	/**
	 * Logs out the user.
	 * 
	 */
	public void logout() {
		loginStatus = false;
	}

	public String getUserID() {
		return userID;
	}

	public Boolean getLoginStatus() {
		return loginStatus;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}