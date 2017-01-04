package edu.gatech.omscs.ihi.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service( value = "FhirResourceService" )
public interface FhirResourceService
{
	JsonNode getFhirResource( String resourceType, String resourceId );
	
	String updateResource( String resourceType, String resource );
	
	void deleteFhirResource( String resourceType, String resourceId );
}
