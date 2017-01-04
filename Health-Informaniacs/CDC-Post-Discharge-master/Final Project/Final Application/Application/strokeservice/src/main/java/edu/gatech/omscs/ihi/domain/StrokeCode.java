package edu.gatech.omscs.ihi.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity( name = "StrokeCode")
@Table( name = "StrokeCode")
public class StrokeCode implements Serializable
{
	private static final long serialVersionUID = 9006567554757699338L;

	@Id
	private String icd10StrokeCode ;
	
	public StrokeCode()
	{
		
	}
	
	public StrokeCode( String code )
	{
		this.icd10StrokeCode = code;
	}
	
	public String getIcd10StrokeCode()
	{
		return this.icd10StrokeCode;
	}
	
	public void setIcd10StrokeCode( String code )
	{
		this.icd10StrokeCode = code;
	}	
}