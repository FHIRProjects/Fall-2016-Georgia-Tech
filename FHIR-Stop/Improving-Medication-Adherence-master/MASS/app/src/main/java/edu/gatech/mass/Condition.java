package edu.gatech.mass;

import java.util.List;

public class Condition {
	
	private String name;		// the name of the condition
	private String description;	// a brief, easily understood description of the condition
	private List<Fact> facts;
	private massDate dateDiagnosed;
	
	
	/**
	 * Initializes instance of Condition.
	 *
	 * * @param name
	 * 		The name of the condition
	 * * @param description
	 * 		A brief, easily understood description of the condition
	 * * @param facts
	 * 		A list of facts related to the condition
	 */
	public Condition(String name, String description, List<Fact> facts) {
		this.name = name;
		this.description = description;
		this.facts = facts;
		dateDiagnosed = new massDate();
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
	
	public massDate getDateDiagnosed() {
		return dateDiagnosed;
	}
}