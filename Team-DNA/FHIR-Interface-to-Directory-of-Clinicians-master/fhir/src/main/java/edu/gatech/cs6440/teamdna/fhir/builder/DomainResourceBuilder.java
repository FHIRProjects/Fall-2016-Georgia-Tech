package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.DomainResource;
import edu.gatech.cs6440.teamdna.fhir.Extension;
import edu.gatech.cs6440.teamdna.fhir.Narrative;
import edu.gatech.cs6440.teamdna.fhir.ResourceContainer;

public class DomainResourceBuilder<T extends DomainResource> extends ResourceBuilder<T> {

	public DomainResourceBuilder<T> withText(Narrative value){
		getResource().setText(value);
		
		return this;
	}

	public DomainResourceBuilder<T> withContained(ResourceContainer value){
		getResource().getContaineds().add(value);
		
		return this;
	}

	public DomainResourceBuilder<T> withExtension(Extension value){
		getResource().getExtensions().add(value);
		
		return this;
	}
	
	public DomainResourceBuilder<T> withModifierExtension(Extension value){
		getResource().getModifierExtensions().add(value);
		
		return this;
	}
}
