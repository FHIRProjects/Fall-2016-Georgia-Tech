package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.CodeableConcept;
import edu.gatech.cs6440.teamdna.fhir.Coding;

public class CodeableConceptBuilder extends ElementBuilder<CodeableConcept>{
	public CodeableConceptBuilder() {
		setElement(new CodeableConcept());
	}

	public CodeableConceptBuilder withText(java.lang.String value){
		getElement().setText(FhirUtils.toFhirString(value));
		
		return this;
	}
	
	public CodeableConceptBuilder withCoding(Coding coding){
		getElement().getCodings().add(coding);

		return this;
	}
}
