package edu.gatech.omscs.ihi.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity( name = "SurveyAdmin" )
@Table( name = "SurveyAdmin" )
public class SurveyAdmin implements Serializable
{
	private static final long serialVersionUID = -8112495462559397885L;
	
	@Id
	private String username;
	
	@Column( unique = true )
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