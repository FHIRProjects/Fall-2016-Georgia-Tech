package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.Uri;

public class UriBuilder extends ElementBuilder<Uri> {
	public UriBuilder() {
		setElement(new Uri());
	}

	public UriBuilder withValue(String value){
		getElement().setValue(value);
		
		return this;
	}
}
