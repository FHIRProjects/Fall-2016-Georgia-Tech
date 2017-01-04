package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.CodeableConcept;
import edu.gatech.cs6440.teamdna.fhir.Identifier;
import edu.gatech.cs6440.teamdna.fhir.IdentifierUse;
import edu.gatech.cs6440.teamdna.fhir.IdentifierUseList;
import edu.gatech.cs6440.teamdna.fhir.Period;
import edu.gatech.cs6440.teamdna.fhir.Reference;
import edu.gatech.cs6440.teamdna.fhir.Uri;

public class IdentifierBuilder extends ElementBuilder<Identifier> {

	public IdentifierBuilder() {
		setElement(new Identifier());
	}
//    protected CodeableConcept type;
	
	public IdentifierBuilder withAssigner(Reference value) {
		getElement().setAssigner(value);

		return this;
	}

	public IdentifierBuilder withValue(java.lang.String value) {
		getElement().setValue(FhirUtils.toFhirString(value));

		return this;
	}

	public IdentifierBuilder withSystem(Uri value) {
		getElement().setSystem(value);

		return this;
	}

	public IdentifierBuilder withType(CodeableConcept value) {
		getElement().setType(value);

		return this;
	}
	
	public IdentifierBuilder withPeriod(Period value){
		getElement().setPeriod(value);
		
		return this;
	}
	
	public IdentifierBuilder withUse(IdentifierUseList useCode){
		IdentifierUse identifierUse = new IdentifierUse();
		identifierUse.setValue(useCode);
		getElement().setUse(identifierUse);
		
		return this;
	}
}
