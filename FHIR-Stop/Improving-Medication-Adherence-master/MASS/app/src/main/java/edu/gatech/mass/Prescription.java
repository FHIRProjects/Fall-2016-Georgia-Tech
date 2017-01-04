package edu.gatech.mass;

import java.util.ArrayList;
import java.util.List;

public class Prescription {
	
	private Medication medication;		// medication prescribed to the patient
	private String instructions;		// instructions on when and how to take medication
	private int lateDosageLimit;		// number of late dosages before provider is contacted
	private int prescriptionID;			// medication order ID from FHIR
	private massDate datePrescribed;	// date the medication was prescribed to the patient
	private List<massTime> dosageTimes;	// times throughout one day that the medication must be taken
	
	/**
	 * Initializes instance of Prescription.
	 * 
	 * @param instructions
	 * 		instructions on when and how to take medication
	 * @param lateDosageLimit
	 * 		number of late dosages before provider is contacted
	 * @param prescriptionID
	 * 		medication order ID from FHIR
	 */
	public Prescription(Medication medication, String instructions, int lateDosageLimit, 
			int prescriptionID) {
		this.medication = medication;
		this.instructions = instructions;
		this.lateDosageLimit = lateDosageLimit;
		this.prescriptionID = prescriptionID;
		datePrescribed = new massDate();
		dosageTimes = new ArrayList<massTime>();
	}
	
	/**
	 * Initializes instance of Prescription.
	 * 
	 * @param dosageTime
	 * 		time of day that the prescribed medication must be taken
	 */
	public void addDosageTime(massTime dosageTime) {
		dosageTimes.add(dosageTime);
	}
	
	public String getInstructions() {
		return instructions;
	}
	
	public int getLateDosageLimit() {
		return lateDosageLimit;
	}
	
	public int getPrescriptionID() {
		return prescriptionID;
	}
	
	public massDate getDatePrescribed() {
		return datePrescribed;
	}
	
	public Medication getMedication() {
		return medication;
	}

	public List<massTime> getDosageTimes() {
		return dosageTimes;
	}
}