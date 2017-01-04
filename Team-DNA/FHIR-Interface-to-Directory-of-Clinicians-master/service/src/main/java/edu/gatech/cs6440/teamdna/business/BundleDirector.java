package edu.gatech.cs6440.teamdna.business;

import java.util.List;

import org.springframework.util.CollectionUtils;

import edu.gatech.cs6440.teamdna.fhir.Bundle;
import edu.gatech.cs6440.teamdna.fhir.Practitioner;
import edu.gatech.cs6440.teamdna.fhir.builder.BundleBuilder;
import edu.gatech.cs6440.teamdna.fhir.builder.BundleEntryBuilder;
import edu.gatech.cs6440.teamdna.model.DoCPDDo;

public class BundleDirector {
	public static Bundle build(List<DoCPDDo> docPDs, String protocol, String hostname, String port, String path) {
		BundleBuilder bundleBuilder = (new BundleBuilder());
		if(!CollectionUtils.isEmpty(docPDs)){
			bundleBuilder.withTotal(docPDs.size());
			for (DoCPDDo docPD : docPDs) {
				Practitioner practitioner = PractitionerDirector.build(docPD);
				if(practitioner != null){
					BundleEntryBuilder bundleEntryBuilder = new BundleEntryBuilder();
					String id = practitioner.getId().getValue();
					bundleEntryBuilder
						.withURI(protocol, hostname, port, path, id)
						.withResource(practitioner);
					
					bundleBuilder.withEntry(bundleEntryBuilder.build());
				}
			}
		}
		
		return bundleBuilder.build();
	}
}
