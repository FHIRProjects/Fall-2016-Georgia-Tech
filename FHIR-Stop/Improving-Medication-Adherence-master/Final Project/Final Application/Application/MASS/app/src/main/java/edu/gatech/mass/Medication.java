package edu.gatech.mass;

import java.util.List;

public class Medication {
	
	private String name;		// the name of the medication
	private String description;	// a description of the medication's physical characteristics (color, shape, etc.)
	private List<Fact> facts;
	
	
	/**
	 * Initializes instance of Medication.
	 *
	 * * @param name
	 * 		The name of the medication
	 * * @param description
	 * 		A description of the medication's physical characteristics (color, shape, etc.)
	 * * @param facts
	 * 		A list of facts related to the medication
	 */
	public Medication(String name, String description, List<Fact> facts) {
		this.name = name;
		this.description = description;
		this.facts = facts;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public List<Fact> getFacts() {
		return facts;
	}
}