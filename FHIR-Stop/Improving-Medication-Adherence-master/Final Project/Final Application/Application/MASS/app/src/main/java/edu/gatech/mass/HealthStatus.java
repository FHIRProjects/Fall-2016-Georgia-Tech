package edu.gatech.mass;

import java.util.ArrayList;
import java.util.List;

public class HealthStatus {
	
	private List<Condition> conditions;		// list of patient's medical conditions
	private int patientID;					// patient's ID from FHIR
	
	/**
	 * Initializes an instance of HealthStatus.
	 * 
	 * @param patientID
	 * 		patient's ID from FHIR
	 */
	public HealthStatus(int patientID) {
		this.patientID = patientID;
		conditions = new ArrayList<Condition>();
	}
	
	public void addCondition(String name, String description, List<Fact> facts) {
		conditions.add(new Condition(name, description, facts));
	}
	
	public List<Condition> getConditions() {
		return conditions;
	}
	
	public int getPatientID() {
		return patientID;
	}
}