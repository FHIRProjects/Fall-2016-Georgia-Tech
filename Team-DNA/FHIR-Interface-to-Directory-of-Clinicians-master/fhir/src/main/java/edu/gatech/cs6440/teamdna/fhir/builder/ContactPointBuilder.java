package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.ContactPoint;
import edu.gatech.cs6440.teamdna.fhir.ContactPointSystem;
import edu.gatech.cs6440.teamdna.fhir.ContactPointSystemList;
import edu.gatech.cs6440.teamdna.fhir.ContactPointUse;
import edu.gatech.cs6440.teamdna.fhir.ContactPointUseList;
import edu.gatech.cs6440.teamdna.fhir.Period;

public class ContactPointBuilder extends ElementBuilder<ContactPoint> {

	public ContactPointBuilder() {
		setElement(new ContactPoint());
	}
	
	public ContactPointBuilder withSystem(ContactPointSystemList value){
		ContactPointSystem system = new ContactPointSystem();
		system.setValue(value);
		
		getElement().setSystem(system);
		
		return this;
	}
	
	public ContactPointBuilder withPeriod(Period period){
		getElement().setPeriod(period);
		
		return this;
	}
	
	public ContactPointBuilder withValue(java.lang.String value){
		getElement().setValue(FhirUtils.toFhirString(value));
		
		return this;
	}
	
	public ContactPointBuilder asUse(ContactPointUseList value){
		ContactPointUse use = new ContactPointUse();
		use.setValue(value);
		
		getElement().setUse(use);
		
		return this;
	}
	
	
}
