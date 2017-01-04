package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.Id;
import edu.gatech.cs6440.teamdna.fhir.Meta;
import edu.gatech.cs6440.teamdna.fhir.Resource;
import edu.gatech.cs6440.teamdna.fhir.Uri;

public class ResourceBuilder<T extends Resource> {
	private T resource;
	
	protected T getResource(){
		return resource;
	}
	
	protected void setResource(T element){
		this.resource = element;
	}
	
	public ResourceBuilder<T> withLanguage(java.lang.String language){
		getResource().setLanguage(FhirUtils.toFhirCode(language));
		
		return this;
	}
	
	public ResourceBuilder<T> withId(java.lang.String id, java.lang.String value){
		ElementBuilder<Id> idBuilder = new ElementBuilder<Id>();
		idBuilder.setElement(new Id());
		Id fhirId = idBuilder.withId(id).build();
		fhirId.setValue(value);
		getResource().setId(fhirId);
		
		return this;
	}
	
	public ResourceBuilder<T> withImplicitRule(Uri value){
		getResource().setImplicitRules(value);
		
		return this;
	}
	
	public ResourceBuilder<T> withMeta(Meta value){
		getResource().setMeta(value);
		
		return this;
	}
	
	public T build(){
		return resource;
	}
}
