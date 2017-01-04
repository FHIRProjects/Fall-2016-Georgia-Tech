package edu.gatech.omscs.ihi.service;

import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

@Service( value = "ServerConnectionService" )
public interface ServerConnectionService 
{
	IGenericClient getConnectionClient( String endpoint );

	FhirContext getFhirContext();	
}