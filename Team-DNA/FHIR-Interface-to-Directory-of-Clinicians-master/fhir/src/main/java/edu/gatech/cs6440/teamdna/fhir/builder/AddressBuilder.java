package edu.gatech.cs6440.teamdna.fhir.builder;

import java.time.LocalDate;

import edu.gatech.cs6440.teamdna.fhir.Address;
import edu.gatech.cs6440.teamdna.fhir.AddressType;
import edu.gatech.cs6440.teamdna.fhir.AddressTypeList;
import edu.gatech.cs6440.teamdna.fhir.AddressUse;
import edu.gatech.cs6440.teamdna.fhir.AddressUseList;
import edu.gatech.cs6440.teamdna.fhir.Period;

public class AddressBuilder extends ElementBuilder<Address>{
	
	public AddressBuilder() {
		setElement(new Address());
	}
	
	public AddressBuilder withLine(java.lang.String value){
		getElement().getLines().add(FhirUtils.toFhirString(value));
		
		return this;
	}
	
	public AddressBuilder withCity(java.lang.String value){
		getElement().setCity(FhirUtils.toFhirString(value));
		
		return this;
	}
	
	public AddressBuilder withCountry(java.lang.String value){
		getElement().setCountry(FhirUtils.toFhirString(value));
		
		return this;
	}
	
	public AddressBuilder withDistrict(java.lang.String value){
		getElement().setDistrict(FhirUtils.toFhirString(value));
		
		return this;
	}

	public AddressBuilder withPeriod(java.lang.String id, LocalDate start, LocalDate end){
		Period value = FhirUtils.asFhirPeriod(id, start, end);
		getElement().setPeriod(value);
		
		return this;
	}
	
	public AddressBuilder withPostalCode(java.lang.String value){
		getElement().setPostalCode(FhirUtils.toFhirString(value));
		
		return this;
	}
	
	public AddressBuilder withState(java.lang.String value){
		getElement().setState(FhirUtils.toFhirString(value));
		
		return this;
	}
	
	public AddressBuilder withText(java.lang.String value){
		getElement().setText(FhirUtils.toFhirString(value));
		
		return this;
	}
	
	public AddressBuilder withAddressType(AddressTypeList value){
		AddressType addressType = new AddressType();
		addressType.setValue(value);
		getElement().setType(addressType);
		
		return this;
	}
	
	public AddressBuilder withUse(AddressUseList value){
		AddressUse addressUse = new AddressUse();
		addressUse.setValue(value);
		getElement().setUse(addressUse);
		
		return this;
	}
}
