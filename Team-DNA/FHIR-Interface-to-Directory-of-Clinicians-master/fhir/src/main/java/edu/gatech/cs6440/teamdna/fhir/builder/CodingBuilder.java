package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.Code;
import edu.gatech.cs6440.teamdna.fhir.Coding;
import edu.gatech.cs6440.teamdna.fhir.Uri;

public class CodingBuilder extends ElementBuilder<Coding>{
	
	public CodingBuilder() {
		setElement(new Coding());
	}
	
	public CodingBuilder withCode(java.lang.String id, java.lang.String value){
		Code code = new Code();
		code.setId(id);
		code.setValue(value);

		getElement().setCode(code);
		
		return this;
	}
	
	public CodingBuilder withDisplay(java.lang.String value){
		getElement().setDisplay(FhirUtils.toFhirString(value));
		
		return this;
	}
	
	public CodingBuilder withSystem(java.lang.String value){
		Uri uri = new Uri();
		uri.setValue(value);
		getElement().setSystem(uri);
		
		return this;
	}
	
	public CodingBuilder withUserSelected(java.lang.String value){
		getElement().setUserSelected(FhirUtils.toFhirBoolean(value));
		
		return this;
	}
	
	public CodingBuilder withVersion(java.lang.String value){
		getElement().setVersion(FhirUtils.toFhirString(value));
		
		return this;
	}
}
