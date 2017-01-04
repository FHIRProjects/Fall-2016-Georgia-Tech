package edu.gatech.cs6440.teamdna.fhir.builder;

import java.time.LocalDate;

import edu.gatech.cs6440.teamdna.fhir.Extension;
import edu.gatech.cs6440.teamdna.fhir.HumanName;
import edu.gatech.cs6440.teamdna.fhir.NameUse;
import edu.gatech.cs6440.teamdna.fhir.NameUseList;

public class HumanNameBuilder extends ElementBuilder<HumanName> {

	public HumanNameBuilder() {
		setElement(new HumanName());
	}
	
	public HumanNameBuilder withLastName(java.lang.String value) {
		getElement().getFamilies().add(FhirUtils.toFhirString(value));

		return this;
	}

	public HumanNameBuilder withFirstName(java.lang.String value) {
		getElement().getGivens().add(FhirUtils.toFhirString(value));

		return this;
	}

	public HumanNameBuilder withNamePrefix(java.lang.String value) {
		getElement().getPrefixes().add(FhirUtils.toFhirString(value));

		return this;
	}

	public HumanNameBuilder withNameSuffix(java.lang.String value) {
		getElement().getSuffixes().add(FhirUtils.toFhirString(value));

		return this;
	}
	
	public HumanNameBuilder withPeriod(java.lang.String id, LocalDate start, LocalDate end){
		getElement().setPeriod(FhirUtils.asFhirPeriod(id, start, end));
		
		return this;
	}
	
	public HumanNameBuilder withUse(NameUseList useCode){
		NameUse nameUse = new NameUse();
		nameUse.setValue(useCode);
		getElement().setUse(nameUse);
		
		return this;
	}
	
	public HumanNameBuilder withText(java.lang.String text){
		getElement().setText(FhirUtils.toFhirString(text));
		
		return this;
	}
	
	public HumanNameBuilder withFirstName(Extension extension){
		getElement().getExtensions().add(extension);
		
		return this;
	}
}
