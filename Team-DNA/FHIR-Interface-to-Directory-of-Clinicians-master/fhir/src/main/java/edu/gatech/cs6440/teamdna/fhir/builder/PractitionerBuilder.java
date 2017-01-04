package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.Address;
import edu.gatech.cs6440.teamdna.fhir.Attachment;
import edu.gatech.cs6440.teamdna.fhir.Boolean;
import edu.gatech.cs6440.teamdna.fhir.CodeableConcept;
import edu.gatech.cs6440.teamdna.fhir.ContactPoint;
import edu.gatech.cs6440.teamdna.fhir.Date;
import edu.gatech.cs6440.teamdna.fhir.HumanName;
import edu.gatech.cs6440.teamdna.fhir.Identifier;
import edu.gatech.cs6440.teamdna.fhir.Practitioner;
import edu.gatech.cs6440.teamdna.fhir.PractitionerPractitionerRole;
import edu.gatech.cs6440.teamdna.fhir.PractitionerQualification;

public class PractitionerBuilder extends DomainResourceBuilder<Practitioner>{

	public PractitionerBuilder() {
		setResource(new Practitioner());
	}
	
	public PractitionerBuilder withName(HumanName name){
		getResource().setName(name);
		
		return this;
	}
	
	public PractitionerBuilder withBirthdate(java.util.Date value){
		Date fhirDate = FhirUtils.toFhirDate(value);
		getResource().setBirthDate(fhirDate);
		
		return this;
	}
	
	public PractitionerBuilder withBirthdate(java.lang.String value){
		Date fhirDate = FhirUtils.toFhirDate(value);
		getResource().setBirthDate(fhirDate);
		
		return this;
	}
	
	public PractitionerBuilder withActive(java.lang.String value){
		Boolean fhirValue = FhirUtils.toFhirBoolean(value);
		getResource().setActive(fhirValue);
		
		return this;
	}
	
	public PractitionerBuilder withGender(java.lang.String value){
		getResource().setGender(FhirUtils.toFhirCode(value));
		
		return this;
	}
	
	public PractitionerBuilder withQualification(PractitionerQualification value){
		getResource().getQualifications().add(value);
		
		return this;
	}
	
	public PractitionerBuilder withAddress(Address value){
		getResource().getAddresses().add(value);
		
		return this;
	}
	
	public PractitionerBuilder withCommunication(CodeableConcept value){
		getResource().getCommunications().add(value);
		
		return this;
	}
	
	public PractitionerBuilder withIdentifier(Identifier value){
		getResource().getIdentifiers().add(value);
		
		return this;
	}
	
	public PractitionerBuilder withPhoto(Attachment value){
		getResource().getPhotos().add(value);
		
		return this;
	}
	
	public PractitionerBuilder withPractitionerRole(PractitionerPractitionerRole value){
		getResource().getPractitionerRoles().add(value);
		
		return this;
	}
	
	public PractitionerBuilder withTelecom(ContactPoint value){
		getResource().getTelecoms().add(value);
		
		return this;
	}
}
