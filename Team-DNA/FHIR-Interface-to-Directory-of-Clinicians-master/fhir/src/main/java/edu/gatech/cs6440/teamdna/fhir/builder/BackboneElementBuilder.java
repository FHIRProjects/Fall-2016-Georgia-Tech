package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.BackboneElement;
import edu.gatech.cs6440.teamdna.fhir.Extension;

public class BackboneElementBuilder<T extends BackboneElement> extends ElementBuilder<T> {

	public BackboneElementBuilder<T> withModifierExtension(Extension value){
		getElement().getModifierExtensions().add(value);
		
		return this;
	}

}
