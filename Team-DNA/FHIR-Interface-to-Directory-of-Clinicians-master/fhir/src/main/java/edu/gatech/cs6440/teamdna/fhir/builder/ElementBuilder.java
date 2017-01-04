package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.Element;
import edu.gatech.cs6440.teamdna.fhir.Extension;

public class ElementBuilder<T extends Element> {
	private T element;
	
	protected T getElement(){
		return element;
	}
	
	protected void setElement(T element){
		this.element = element;
	}
	
	public ElementBuilder<T> withId(java.lang.String value){
		getElement().setId(value);
		
		return this;
	}
	
	public ElementBuilder<T> withExtension(Extension value){
		getElement().getExtensions().add(value);
		
		return this;
	}
	
	public T build(){
		return element;
	}
}
