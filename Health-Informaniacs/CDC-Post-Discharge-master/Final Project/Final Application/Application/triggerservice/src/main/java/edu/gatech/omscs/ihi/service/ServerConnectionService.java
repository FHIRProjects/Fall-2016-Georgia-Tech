package edu.gatech.omscs.ihi.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

@Service( value = "ServerConnectionService" )
public interface ServerConnectionService 
{
	IGenericClient getConnectionClient( String endpoint );
	
	void pushDataToStrokeAppServer( JsonNode dischargedPatients  );

	JsonNode getStrokeCodesFromStrokeAppServer();

	FhirContext getFhirContext();	
}