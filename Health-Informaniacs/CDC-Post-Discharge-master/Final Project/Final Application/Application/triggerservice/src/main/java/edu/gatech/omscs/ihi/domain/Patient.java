package edu.gatech.omscs.ihi.domain;

import java.io.Serializable;
import java.sql.Date;


import edu.gatech.omscs.ihi.domain.id.PatientId;

public class Patient implements Serializable
{
	private static final long serialVersionUID = -1693238231132711914L;

	private PatientId patientId;
	
	private String firstName;
	
	private String lastName;
	
	private Date admitDate;
	
	private Date dischargeDate;
	
	private String encounterJson;
	
	private String questionnaireResponseId;
	
	private String questionnaireResponseJson;
	
	private String questionnaireResponseCsv;
	

	public Patient()
	{
		
	}
	
	public Patient( PatientId patientId, 
			String firstName, 
			String lastName, 
			Date dischargeDate, 
			Date admitDate, 
			String encounterJson)
	{
		this.patientId = patientId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dischargeDate = dischargeDate;
		this.admitDate = admitDate;
		this.encounterJson = encounterJson;		
	}
	
	
	public Patient( PatientId patientId, 
					String firstName, 
					String lastName, 
					Date dischargeDate, 
					Date admitDate, 
					String encounterJson,
					String qrId,
					String qrJson,
					String qrCsv)
	{
		this.patientId = patientId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dischargeDate = dischargeDate;
		this.admitDate = admitDate;
		this.encounterJson = encounterJson;
		this.questionnaireResponseId = qrId;
		this.questionnaireResponseJson = qrJson;
		this.questionnaireResponseCsv = qrCsv;
	}
	
	public PatientId getPatientId() 
	{
		return this.patientId;
	}

	public void setPatientId( PatientId patientId ) 
	{
		this.patientId = patientId;
	}

	public String getEncounterJson() 
	{
		return this.encounterJson;
	}

	public void setEncounterJson( String encounterJson ) 
	{
		this.encounterJson = encounterJson;
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
	
	public Date getAdmitDate() 
	{
		return this.admitDate;
	}
	
	public void setAdmitDate( Date admitDate ) 
	{
		this.admitDate = admitDate;
	}
	
	public Date getDischargeDate() 
	{
		return this.dischargeDate;
	}
	
	public void setDischargeDate( Date dischargeDate ) 
	{
		this.dischargeDate = dischargeDate;
	}

	public String getQuestionnaireResponseId() {
		return questionnaireResponseId;
	}

	public void setQuestionnaireResponseId(String questionnaireResponseId) {
		this.questionnaireResponseId = questionnaireResponseId;
	}

	public String getQuestionnaireResponseJson() {
		return questionnaireResponseJson;
	}

	public void setQuestionnaireResponseJson(String qr_json) {
		this.questionnaireResponseJson = qr_json;
	}

	public String getQuestionnaireResponseCsv() {
		return questionnaireResponseCsv;
	}

	public void setQuestionnaireResponseCsv(String qr_csv) {
		this.questionnaireResponseCsv = qr_csv;
	}
}