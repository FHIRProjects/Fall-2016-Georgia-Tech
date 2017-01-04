package edu.gatech.omscs.ihi.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import edu.gatech.omscs.ihi.service.ServerConnectionService;

@Service( value = "ServerConnectionService" )
public class ServerConnectionServiceImpl implements ServerConnectionService 
{
	@Value( "${fhirService.baseUrl}" )
	private String fhirServiceUrl;
	
	private FhirContext context = FhirContext.forDstu2();
	
	@Override
	public FhirContext getFhirContext()
	{
		return context;
	}
	
	@Override
	public IGenericClient getConnectionClient( String endpoint )
	{	
		IGenericClient client = null;
		try
		{
			String url = fhirServiceUrl + endpoint;
			
			client = context.newRestfulGenericClient( url );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
		
		return client;
	}
}