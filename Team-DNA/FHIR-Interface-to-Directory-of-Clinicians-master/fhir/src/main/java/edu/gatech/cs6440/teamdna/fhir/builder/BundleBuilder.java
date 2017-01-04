package edu.gatech.cs6440.teamdna.fhir.builder;

import java.math.BigInteger;

import edu.gatech.cs6440.teamdna.fhir.Bundle;
import edu.gatech.cs6440.teamdna.fhir.BundleEntry;
import edu.gatech.cs6440.teamdna.fhir.UnsignedInt;

public class BundleBuilder extends ResourceBuilder<Bundle>{
	public BundleBuilder() {
		setResource(new Bundle());
	}
	
	public BundleBuilder withEntry(BundleEntry entry){
		getResource().getEntries().add(entry);
		
		return this;
	}

	public void withTotal(int size) {
		UnsignedInt total = new UnsignedInt();
		total.setValue(BigInteger.valueOf(size));
		getResource().setTotal(total);
		
	}
}
