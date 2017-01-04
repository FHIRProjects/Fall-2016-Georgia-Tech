package edu.gatech.cs6440.teamdna.fhir.builder;

import edu.gatech.cs6440.teamdna.fhir.BundleEntry;
import edu.gatech.cs6440.teamdna.fhir.Practitioner;
import edu.gatech.cs6440.teamdna.fhir.ResourceContainer;
import edu.gatech.cs6440.teamdna.fhir.Uri;

public class BundleEntryBuilder extends BackboneElementBuilder<BundleEntry> {
	private static final String DEFAULT_PROTOCOL = "http";
	
	public BundleEntryBuilder() {
		setElement(new BundleEntry());
	}

	public BundleEntryBuilder withURI(String protocol, String host, String port, String path, String id) {
		Uri uri = new Uri();
		StringBuilder uriString = new StringBuilder();
		if(protocol != null){
			uriString.append(protocol);
		} else {
			uriString.append(DEFAULT_PROTOCOL);
		}
		uriString.append("://").append(host);
		if(port!=null){
			uriString.append(":").append(port);
		}
		uriString.append("/").append(path).append("/").append(id);
		
		uri.setValue(uriString.toString());
		getElement().setFullUrl(uri);

		return this;
	}

	public BundleEntryBuilder withResource(Practitioner resource) {
		ResourceContainer container = new ResourceContainer();
		container.setPractitioner(resource);
		getElement().setResource(container);

		return this;
	}
}
