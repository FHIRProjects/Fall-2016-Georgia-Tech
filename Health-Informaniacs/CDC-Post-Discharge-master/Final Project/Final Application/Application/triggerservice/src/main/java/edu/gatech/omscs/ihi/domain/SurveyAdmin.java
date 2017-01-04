package edu.gatech.omscs.ihi.domain;

import java.io.Serializable;

public class SurveyAdmin implements Serializable
{
	private static final long serialVersionUID = -6867980034613150222L;

	private String username;
	
	private int surveyAdminId;
	
	private String firstName;
	
	private String lastName;
	
	private String password;
		
	public SurveyAdmin()
	{
		
	}
	
	public SurveyAdmin( int surveyAdminId, String firstName, String lastName, String username, String password )
	{
		this.surveyAdminId = surveyAdminId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
	}

	public int getSurveyAdminId()
	{
		return this.surveyAdminId;
	}
	
	public String getFirstName() 
	{
		return this.firstName;
	}

	public void setFirstName( String firstName ) 
	{
		this.firstName = firstName;
	}

	public String getLastName() 
	{
		return this.lastName;
	}

	public void setLastName( String lastName ) 
	{
		this.lastName = lastName;
	}

	public String getUsername() 
	{
		return this.username;
	}

	public void setUsername( String username ) 
	{
		this.username = username;
	}

	public String getPassword() 
	{
		return this.password;
	}

	public void setPassword( String password ) 
	{
		this.password = password;
	}
}