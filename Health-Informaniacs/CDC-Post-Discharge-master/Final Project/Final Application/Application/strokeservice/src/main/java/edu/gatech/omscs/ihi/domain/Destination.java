package edu.gatech.omscs.ihi.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity( name = "Destination" )
@Table( name = "Destination" )
public class Destination implements Serializable
{
	private static final long serialVersionUID = 6238581566078443698L;

	@Id
	private int destinationId ;
	
	private String destinationName;
	
	private int surveyAdminId; 
	
	public Destination()
	{
		
	}
	
	public Destination( int id, String name, int surveyAdminId )
	{
		this.destinationId = id;
		this.destinationName = name;
		this.surveyAdminId = surveyAdminId;
	}
	
	public int getDestinationId() 
	{
		return this.destinationId;
	}
	
	public void setDestinationId( int destinationId ) 
	{
		this.destinationId = destinationId;
	}
	
	public String getDestinationName() 
	{
		return this.destinationName;
	}
	
	public void setDestinationName( String destinationName ) 
	{
		this.destinationName = destinationName;
	}
	
	public int getSurveyAdminId() 
	{
		return this.surveyAdminId;
	}
	
	public void setSurveyAdminId( int surveyAdminId ) 
	{
		this.surveyAdminId = surveyAdminId;
	}	
}