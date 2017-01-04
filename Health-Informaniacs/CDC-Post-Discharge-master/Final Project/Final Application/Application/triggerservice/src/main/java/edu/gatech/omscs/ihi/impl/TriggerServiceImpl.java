package edu.gatech.omscs.ihi.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.IGenericClient;

import com.fasterxml.jackson.databind.JsonNode;

import edu.gatech.omscs.ihi.domain.id.PatientId;
import edu.gatech.omscs.ihi.service.ServerConnectionService;
import edu.gatech.omscs.ihi.service.TriggerService;
import edu.gatech.omscs.ihi.util.JsonUtils;

@Service( value = "TriggerService" )
public class TriggerServiceImpl implements TriggerService 
{
	@Autowired
	private ServerConnectionService serverConnectionService;
	
	private static int LAST_ONE_DAY = 1;
	
	private ArrayList<String> _strokeCodes = new ArrayList<String>();
	
	/**
	 * Method runs everyday @ 1:00 AM
	 */
    @Scheduled( cron = "0 0 1 * * *" )
	@Override
    public void timerExecute() 
    {
    	_strokeCodes.clear();
    	execute( LAST_ONE_DAY );
    }
    
    @Override
    public void execute( int days )
    {
		if ( days > 0 )
		{
	    	try
	    	{
	    		// Get a client connection to the FHIR server
	    		IGenericClient client  = this.serverConnectionService.getConnectionClient( "" );
	    		
	    		// Get first page of finished Encounters, 'date' attribute does not mean discharge,
	    		// just that the date is in the period, so filter on date in processEncounter().
	    		Bundle results = client.search()
	    		      .forResource(Encounter.class)
	    		      .where(Encounter.STATUS.exactly().code("finished"))
	    		      .returnBundle(Bundle.class)
	    		      .execute();
	    	    		
	    		System.out.println("===DEBUG: Found " + results.getTotal() + " finished Encounters.");
	    		// Query the StrokeCodes table and save the codes locally
	    		JsonNode strokeCodes = serverConnectionService.getStrokeCodesFromStrokeAppServer();
	    		Iterator<JsonNode> itrCodes = strokeCodes.elements();
	    		while (itrCodes.hasNext())
	    		{
	    			JsonNode current = itrCodes.next().get("icd10StrokeCode");	    			
	    			_strokeCodes.add(current.asText());
	    		}	    		
	    		
	    		// Process each finished Encounter
	    		for ( Entry current : results.getEntry() )
	    		{
	    			IResource encounter = current.getResource();
	    			if ( encounter instanceof Encounter )
	    			{
	    				processEncounter( ( Encounter ) encounter, days );
	    			}
	    		}
	    		
	    		// Iterate through the remaining pages
	    		while ( results.getLink( Bundle.LINK_NEXT ) != null ) 
	    		{
	    			// load next page
	    			results = client.loadPage().next( results ).execute();
	    			
	    			// Process each finished Encounter
	    			for ( Entry current : results.getEntry() )
		    		{
		    			IResource encounter = current.getResource();
		    			if ( encounter instanceof Encounter )
		    			{
		    				processEncounter( ( Encounter ) encounter, days );
		    			}
		    		}
	    		}
	    		//this.serverConnectionService.pushDataToStrokeAppServer( dischargedPatients );
	    	}
	    	catch ( Exception exception )
	    	{
	    		exception.printStackTrace();
	    	}	
		}
    }
    
    private void processEncounter( Encounter encounter, int days )
    {
    	Calendar cal = new GregorianCalendar();
		
    	cal.add( Calendar.DAY_OF_MONTH, - days );
		Date daysAgo = cal.getTime();
		
    	    	
		try
		{
			// Skip any Encounters that are not inpatient visits.
			String encounterClass = encounter.getClassElement();
			if ( !encounterClass.equals( "inpatient" ) )
			{
				return;
			}
			
			// Skip if the discharge happened before the search window
			Date startPeriod = encounter.getPeriod().getStart();
			Date endPeriod = encounter.getPeriod().getEnd();
			if ( endPeriod.before( daysAgo ) )
			{
				return;
			}
			
			// check icd code
			String encounterReasonCode = encounter.getReason().get(0).getCoding().get(0).getCode();
			if (!_strokeCodes.contains(encounterReasonCode))
			{
				System.out.println("===DEBUG Skipping encounter = " + encounter.toString());
				return;
			}
		
			// TODO: Save with Patient in strokeappdb
			IdDt destinationReference  = encounter.getHospitalization().getDestination().getReference();
			IdDt patientReference = encounter.getPatient().getReference();
			
			// Get a client connection to the FHIR server
    		IGenericClient client  = this.serverConnectionService.getConnectionClient( "" );
    		
    		// Get first page of finished Encounters, 'date' attribute does not mean discharge,
    		// just that the date is in the period, so filter on date in processEncounter().
    		IBaseResource results = client.read( patientReference ); 
    		if ( results instanceof Patient )
    		{
    			Patient dischargedPatient = (Patient)results;
    			
    			//System.out.println("Discharged patient:= " + dischargedPatient.getName().get(0).getFamilyAsSingleString());
    	    			
    			IParser parser = this.serverConnectionService
    					.getFhirContext().newJsonParser().setPrettyPrint(true); // setPrettyPrint is optional
    			
    			String jsonEncounter = parser.encodeResourceToString(encounter);
    			
    			//System.out.println("Patients encounter: " + jsonEncounter);
    			
    			edu.gatech.omscs.ihi.domain.Patient toPublish =  
    					new edu.gatech.omscs.ihi.domain.Patient( 
    							new PatientId( dischargedPatient.getId().getIdPart(),
    									encounter.getId().getIdPart(),
    									destinationReference.getIdPart() ), 
    							dischargedPatient.getName().get(0).getGivenAsSingleString(), 
    							dischargedPatient.getName().get(0).getFamilyAsSingleString(), 
    							new java.sql.Date(endPeriod.getTime()),
    							new java.sql.Date(startPeriod.getTime()),
    							jsonEncounter,
    							"",
    							"{}",
    							"{}"); // if this isn't quoted we get json parser error
    			
    			JsonNode patientPayload = JsonUtils.convertBeanToJsonNode(toPublish);
    				    		
    			this.serverConnectionService.pushDataToStrokeAppServer( patientPayload );				
    		}
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
    }
}