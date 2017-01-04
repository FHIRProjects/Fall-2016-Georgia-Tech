package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.CodeableConcept;
import edu.gatech.cs6440.teamdna.fhir.Identifier;
import edu.gatech.cs6440.teamdna.fhir.Period;
import edu.gatech.cs6440.teamdna.fhir.PractitionerQualification;

public class PractitionerQualificationBuilder extends BackboneElementBuilder<PractitionerQualification>{
	public PractitionerQualificationBuilder() {
		setElement(new PractitionerQualification());
	}

	public PractitionerQualificationBuilder withCode(CodeableConcept codeableConcept){
		getElement().setCode(codeableConcept);
		
		return this;
	}
	
	public PractitionerQualificationBuilder withPeriod(Period value){
		getElement().setPeriod(value);
		
		return this;
	}

	public PractitionerQualificationBuilder withIssuer(java.lang.String id, java.lang.String reference, java.lang.String display){
		getElement().setIssuer(FhirUtils.asFhirReference(id, reference, display));
		
		return this;
	}
	
	public PractitionerQualificationBuilder withIdentifier(Identifier value){
		getElement().getIdentifiers().add(value);
		
		return this;
	}
}
