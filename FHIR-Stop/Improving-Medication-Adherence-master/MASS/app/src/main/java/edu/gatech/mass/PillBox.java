package edu.gatech.mass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PillBox {
	
	private Map<massTime, List<Prescription>> prescriptions;	// map of patient's prescriptions
	private int patientID;						// patient's ID from FHIR
	private int lateDosageCount;
	private int lateDosageLimit;
	
	/**
	 * Initializes an instance of PillBox.
	 * 
	 * @param patientID
	 * 		patient's ID from FHIR
	 */
	public PillBox(int patientID) {
		this.patientID = patientID;
		lateDosageCount = 0;
		lateDosageLimit = 10000;
	}
	
	/**
	 * Add prescription to PillBox
	 * 
	 * @param dosageTime
	 * 		The time(s) the medication must be taken
	 * @param medication
	 * 		The medication prescribed to the patient
	 * @param instructions
	 * 		String of instructions for how to take the medication
	 * @param lateDosageLimit
	 * 		How many times the medication dosage is missed before contacting provider
	 * @param prescriptionID
	 * 		The medication order ID from FHIR
	 */
	public void addPrescription(List<massTime> dosageTimes, Medication medication, 
			String instructions, int lateDosageLimit, int prescriptionID) {
		
		Iterator<massTime> dosageTimesIterator = dosageTimes.iterator();
		
		while (dosageTimesIterator.hasNext()) {
			massTime time = dosageTimesIterator.next();
			
			List<Prescription> pList = prescriptions.get(time);
			if (pList == null) {
				pList = new ArrayList<Prescription>();
				prescriptions.put(time, pList);
			}
			
			pList.add(new Prescription(medication, instructions, lateDosageLimit, 
					prescriptionID));
		}
		
		if (this.lateDosageLimit > lateDosageLimit) {
			this.lateDosageLimit = lateDosageLimit;
		}
	}
	
	public Map<massTime, List<Prescription>> getPrescriptions() {
		return prescriptions;
	}
	
	public int getPatientID() {
		return patientID;
	}
	
	public int getLateDosageCount() {
		return lateDosageCount;
	}
	
	public int getLateDosageLimit() {
		return lateDosageLimit;
	}
}