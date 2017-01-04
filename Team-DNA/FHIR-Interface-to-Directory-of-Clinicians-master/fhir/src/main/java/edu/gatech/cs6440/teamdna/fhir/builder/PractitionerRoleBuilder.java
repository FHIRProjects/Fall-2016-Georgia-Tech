package edu.gatech.cs6440.teamdna.fhir.builder;

import java.time.LocalDate;

import edu.gatech.cs6440.teamdna.fhir.CodeableConcept;
import edu.gatech.cs6440.teamdna.fhir.PractitionerPractitionerRole;
import edu.gatech.cs6440.teamdna.fhir.Reference;

public class PractitionerRoleBuilder extends BackboneElementBuilder<PractitionerPractitionerRole>{
	public PractitionerRoleBuilder() {
		setElement(new PractitionerPractitionerRole());
	}

	public PractitionerRoleBuilder withManagingOrganization(Reference value){
		getElement().setManagingOrganization(value);
		
		return this;
	}
	
	public PractitionerRoleBuilder withPeriod(java.lang.String id, LocalDate start, LocalDate end){
		getElement().setPeriod(FhirUtils.asFhirPeriod(id, start, end));
		
		return this;
	}

	public PractitionerRoleBuilder withRole(CodeableConcept value){
		getElement().setRole(value);
		
		return this;
	}
	
	public PractitionerRoleBuilder withHealthcareService(Reference value){
		getElement().getHealthcareServices().add(value);
		
		return this;
	}	
	
	public PractitionerRoleBuilder withLocation(Reference value){
		getElement().getLocations().add(value);
		
		return this;
	}
	
	public PractitionerRoleBuilder withSpecialty(CodeableConcept value){
		getElement().getSpecialties().add(value);
		
		return this;
	}	
}
