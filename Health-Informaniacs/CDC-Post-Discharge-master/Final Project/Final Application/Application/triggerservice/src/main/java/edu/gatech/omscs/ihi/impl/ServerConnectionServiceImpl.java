package edu.gatech.omscs.ihi.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

import com.fasterxml.jackson.databind.JsonNode;

import edu.gatech.omscs.ihi.service.ServerConnectionService;

@Service( value = "ServerConnectionService" )
public class ServerConnectionServiceImpl implements ServerConnectionService 
{
	@Value( "${fhirService.baseUrl}" )
	private String fhirServiceUrl;
	
	@Value( "${strokeService.baseUrl}" )
	private String strokeServiceUrl;
	
	private static final String API_ENDPOINT = "/cdc/api/stroke";
	
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
	
	@Override
	public void pushDataToStrokeAppServer( JsonNode dischargedPatients  )
	{
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType( MediaType.APPLICATION_JSON );
			HttpEntity< String > request = new HttpEntity< String >( dischargedPatients.toString(), headers );
			
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.postForObject( strokeServiceUrl + API_ENDPOINT + "/patient", request, JsonNode.class );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
	
	@Override
	public JsonNode getStrokeCodesFromStrokeAppServer()
	{
		JsonNode values = null;
		try
		{	
			RestTemplate restTemplate = new RestTemplate();
			values = restTemplate.getForObject(strokeServiceUrl + API_ENDPOINT + "/codes/icd", JsonNode.class);
			
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
		
		return values;
	}
}