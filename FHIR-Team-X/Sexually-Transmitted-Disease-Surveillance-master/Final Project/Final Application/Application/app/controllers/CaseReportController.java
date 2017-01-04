package controllers;

import play.mvc.*;
import play.libs.*;
import play.data.*;
import views.html.*;
import java.util.*;
import org.w3c.dom.Document;
import com.fasterxml.jackson.databind.JsonNode;
import services.CcdaToFhirConverter;
import services.TestMarshaller;
import services.FhirToCcdaConverter;
import java.text.SimpleDateFormat;

import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.hl7.fhir.dstu3.model.*;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;

public class CaseReportController extends Controller
{

    @BodyParser.Of( BodyParser.Xml.class )
    public Result receive( )
    {
        Document doc = request( ).body( ).asXml( );
        if ( doc == null )
        {
            return badRequest( "Expecting XML data." );
        }
        else
        {
            String name = "X";
            if ( name == null )
            {
                return badRequest( "Missing parameter [name]" );
            }
            else
            {
                //TestMarshaller tm = new TestMarshaller( );
                //tm.testit( doc );
                CcdaToFhirConverter ccdaToFhir = new CcdaToFhirConverter( );
                name = ccdaToFhir.convert( doc );
                return ok( "Hello " + name );
            }
        }
    }


    public Result pull( String id )
    {
        
        FhirToCcdaConverter fhirToCcda = new FhirToCcdaConverter( );
        String xmlStr = null;
        try{
            Document doc = fhirToCcda.pull( id );
            Source source = new DOMSource(doc);
            StringWriter stringWriter = new StringWriter();
            StreamResult result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            xmlStr = stringWriter.getBuffer().toString();
        }catch(TransformerConfigurationException e){
			System.out.println(e);
		}catch(TransformerException e){
			System.out.println(e);
		}
		
        return ok( xmlStr).as("text/xml");
    }
    
    @BodyParser.Of( BodyParser.Json.class )
    public Result list( )
    {
        Bundle bundle1,
               bundle2,
               bundle3,
               bundle4,
               bundle5;
        List< Bundle.BundleEntryComponent > bundleList,
                                            encounterList;
        Set< String > patientIdSet;
        String s,
               firstName,
               lastName,
               startDate,
               endDate,
               dateType,
               facilityId,
               dateField;

        JsonNode json = request( ).body( ).asJson( );

        if ( json == null ) {
            return badRequest( "Expecting JSON data." );
        }
        else {
            s = json.findValue( "firstName"  ).asText( );
            firstName  = ( s != null ) ? s.trim( ) : "" ;
            s = json.findValue( "lastName"   ).asText( );
            lastName   = ( s != null ) ? s.trim( ) : "" ;
            s = json.findValue( "startDate"  ).asText( );
            startDate  = ( s != null ) ? s.trim( ) : "" ;
            s = json.findValue( "endDate"    ).asText( );
            endDate    = ( s != null ) ? s.trim( ) : "" ;
            s = json.findValue( "dateType"   ).asText( );
            dateType   = ( s != null ) ? s.trim( ) : "" ;
            s = json.findValue( "facilityId" ).asText( );
            facilityId = ( s != null ) ? s.trim( ) : "" ;

    		FhirContext fhirContext = FhirContext.forDstu3( );
    		String serverBase = "http://162.249.6.176:8080/baseDstu3";
            String patientSystem = "2.16.840.1.113883.4.1";
    		IGenericClient client = fhirContext.newRestfulGenericClient( serverBase ); 

            if ( startDate.equals( "" ) ) {
                startDate = "0001-01-01";
                }
            if ( endDate.equals( "" ) ) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); 
                endDate = sf.format( new Date( ) );
                }

            dateField = dateType.equals( "received" ) ? "_lastUpdated" : "date";

            bundle3 = null;
            
            // Search with Facility ID
            if ( !facilityId.equals( "" ) ) {
                // Search Encounters with First Name and Last Name 
                if ( !firstName.equals( "" ) && !lastName.equals( "" ) ) {
                    bundle3 = 
                        client.search( ).forResource( model.XEncounter.class )
                        .where( new DateClientParam( dateField ).afterOrEquals( ).day( startDate ) )
                        .where( new DateClientParam( dateField ).beforeOrEquals( ).day( endDate ) )
                        .where( new StringClientParam( "patient.given" ).matches( ).value( firstName ) )
                        .where( new StringClientParam( "patient.family" ).matches( ).value( lastName ) )
                        .where( new TokenClientParam( "location.identifier" ).exactly( ).code( facilityId ) )
                        .include( Encounter.INCLUDE_PRACTITIONER.asRecursive( ) )
                        .include( Encounter.INCLUDE_LOCATION.asRecursive( ) )
                        .returnBundle( Bundle.class )
                        .execute( );
                }
                // Search Encounters with First Name only
                else if ( !firstName.equals( "" ) && lastName.equals( "" ) ) {
                    bundle3 = 
                        client.search( ).forResource( model.XEncounter.class )
                        .where( new DateClientParam( dateField ).afterOrEquals( ).day( startDate ) )
                        .where( new DateClientParam( dateField ).beforeOrEquals( ).day( endDate ) )
                        .where( new StringClientParam( "patient.given" ).matches( ).value( firstName ) )
                        .where( new TokenClientParam( "location.identifier" ).exactly( ).code( facilityId ) )
                        .include( Encounter.INCLUDE_PRACTITIONER.asRecursive( ) )
                        .include( Encounter.INCLUDE_LOCATION.asRecursive( ) )
                        .returnBundle( Bundle.class )
                        .execute( );
                }
                // Search Encounters with First Name only
                else if ( firstName.equals( "" ) && !lastName.equals( "" ) ) {
                    bundle3 = 
                        client.search( ).forResource( model.XEncounter.class )
                        .where( new DateClientParam( dateField ).afterOrEquals( ).day( startDate ) )
                        .where( new DateClientParam( dateField ).beforeOrEquals( ).day( endDate ) )
                        .where( new StringClientParam("patient.family" ).matches( ).value( lastName ) )
                        .where( new TokenClientParam( "location.identifier" ).exactly( ).code( facilityId ) )
                        .include( Encounter.INCLUDE_PRACTITIONER.asRecursive( ) )
                        .include( Encounter.INCLUDE_LOCATION.asRecursive( ) )
                        .returnBundle( Bundle.class )
                        .execute( );
                }
                // Search Encounters with no name parameters
                else if ( firstName.equals( "" ) && lastName.equals( "" ) ) {
                    bundle3 = 
                        client.search( ).forResource( model.XEncounter.class )
                        .where( new DateClientParam( dateField ).afterOrEquals( ).day( startDate ) )
                        .where( new DateClientParam( dateField ).beforeOrEquals( ).day( endDate ) )
                        .where( new TokenClientParam( "location.identifier" ).exactly( ).code( facilityId ) )
                        .include( Encounter.INCLUDE_PRACTITIONER.asRecursive( ) )
                        .include( Encounter.INCLUDE_LOCATION.asRecursive( ) )
                        .returnBundle( Bundle.class )
                        .execute( );
                }
            }
            // Search without Facility ID
            else {
                // Search Encounters with First Name and Last Name 
                if ( !firstName.equals( "" ) && !lastName.equals( "" ) ) {
                    bundle3 = 
                        client.search( ).forResource( model.XEncounter.class )
                        .where( new DateClientParam( dateField ).afterOrEquals( ).day( startDate ) )
                        .where( new DateClientParam( dateField ).beforeOrEquals( ).day( endDate ) )
                        .where( new StringClientParam( "patient.given" ).matches( ).value( firstName ) )
                        .where( new StringClientParam( "patient.family" ).matches( ).value( lastName ) )
                        .include( Encounter.INCLUDE_PRACTITIONER.asRecursive( ) )
                        .include( Encounter.INCLUDE_LOCATION.asRecursive( ) )
                        .returnBundle( Bundle.class )
                        .execute( );
                }
                // Search Encounters with First Name only
                else if ( !firstName.equals( "" ) && lastName.equals( "" ) ) {
                    bundle3 = 
                        client.search( ).forResource( model.XEncounter.class )
                        .where( new DateClientParam( dateField ).afterOrEquals( ).day( startDate ) )
                        .where( new DateClientParam( dateField ).beforeOrEquals( ).day( endDate ) )
                        .where( new StringClientParam( "patient.given" ).matches( ).value( firstName ) )
                        .include( Encounter.INCLUDE_PRACTITIONER.asRecursive( ) )
                        .include( Encounter.INCLUDE_LOCATION.asRecursive( ) )
                        .returnBundle( Bundle.class )
                        .execute( );
                }
                // Search Encounters with First Name only
                else if ( firstName.equals( "" ) && !lastName.equals( "" ) ) {
                    bundle3 = 
                        client.search( ).forResource( model.XEncounter.class )
                        .where( new DateClientParam( dateField ).afterOrEquals( ).day( startDate ) )
                        .where( new DateClientParam( dateField ).beforeOrEquals( ).day( endDate ) )
                        .where( new StringClientParam("patient.family" ).matches( ).value( lastName ) )
                        .include( Encounter.INCLUDE_PRACTITIONER.asRecursive( ) )
                        .include( Encounter.INCLUDE_LOCATION.asRecursive( ) )
                        .returnBundle( Bundle.class )
                        .execute( );
                }
                // Search Encounters with no name parameters
                else if ( firstName.equals( "" ) && lastName.equals( "" ) ) {
                    bundle3 = 
                        client.search( ).forResource( model.XEncounter.class )
                        .where( new DateClientParam( dateField ).afterOrEquals( ).day( startDate ) )
                        .where( new DateClientParam( dateField ).beforeOrEquals( ).day( endDate ) )
                        .include( Encounter.INCLUDE_PRACTITIONER.asRecursive( ) )
                        .include( Encounter.INCLUDE_LOCATION.asRecursive( ) )
                        .returnBundle( Bundle.class )
                        .execute( );
                }
            }

            // Add encounters bundle to the set of resource bundles
            Bundle completeBundle = new Bundle( );
            bundleList = new ArrayList< Bundle.BundleEntryComponent >( );
            bundleList.addAll( bundle3.getEntry( ) );

            // Loop through bundle and retrieve set of patient IDs
            patientIdSet = new HashSet< String >( );
            encounterList = new ArrayList< Bundle.BundleEntryComponent >( bundle3.getEntry( ) );

		    for ( int i = 0; i < encounterList.size( ); i++ ) {
		        if ( encounterList.get( i ).getResource( ).fhirType( ).equals( "Encounter" ) ) {
		            Encounter encounter = ( Encounter ) encounterList.get( i ).getResource( );
			        patientIdSet.add( encounter.getPatient( ).getReference( ) );
		        }
		    }

            Iterator< String > iterator = patientIdSet.iterator( );
            while ( iterator.hasNext( ) ) {
                String patientReference = iterator.next( );

                bundle1 = 
                    client.search().forResource(DiagnosticReport.class)
                    .where(new TokenClientParam("patient._id").exactly().code(patientReference))
                    .include(DiagnosticReport.INCLUDE_RESULT.asRecursive())
                    .returnBundle(Bundle.class)
                    .execute();

                bundle2 = 
                    client.search().forResource(model.XPatient.class)
                    .where(new TokenClientParam("_id").exactly().code(patientReference))
                    .returnBundle(Bundle.class)
                    .execute();

                bundle4 = 
                    client.search().forResource(Condition.class)
                    .where(new TokenClientParam("patient._id").exactly().code(patientReference))
                    .returnBundle(Bundle.class)
                    .execute();
               
                bundle5 = 
                    client.search().forResource(MedicationAdministration.class)
                    .where(new TokenClientParam("patient._id").exactly().code(patientReference))
                    .returnBundle(Bundle.class)
                    .execute();

                bundleList.addAll( bundle1.getEntry( ) );
                bundleList.addAll( bundle2.getEntry( ) );
                bundleList.addAll( bundle4.getEntry( ) );
                bundleList.addAll( bundle5.getEntry( ) );
            }

            completeBundle.setEntry( bundleList );


            /*
            bundle1 = 
                client.search().forResource(DiagnosticReport.class)
                .where(new StringClientParam("patient.given").matches().value(firstName))
                .where(new StringClientParam("patient.given").isMissing(false))
                .where(new StringClientParam("patient.family").matches().value(lastName))
                .where(new StringClientParam("patient.family").isMissing(false))
                .include(DiagnosticReport.INCLUDE_RESULT.asRecursive())
                .returnBundle(Bundle.class)
                .execute();

            bundle2 = 
                client.search().forResource(model.XPatient.class)
                .where(new StringClientParam("given").matches().value(firstName))
                .where(new StringClientParam("given").isMissing(false))
                .where(new StringClientParam("family").matches().value(lastName))
                .where(new StringClientParam("family").isMissing(false))
                .returnBundle(Bundle.class)
                .execute();

            bundle4 = 
                client.search().forResource(Condition.class)
                .where(new StringClientParam("patient.given").matches().value(firstName))
                .where(new StringClientParam("patient.given").isMissing(false))
                .where(new StringClientParam("patient.family").matches().value(lastName))
                .where(new StringClientParam("patient.family").isMissing(false))
                .returnBundle(Bundle.class)
                .execute();
               
            bundle5 = 
                client.search().forResource(MedicationAdministration.class)
                .where(new StringClientParam("patient.given").matches().value(firstName))
                .where(new StringClientParam("patient.given").isMissing(false))
                .where(new StringClientParam("patient.family").matches().value(lastName))
                .where(new StringClientParam("patient.family").isMissing(false))
                .returnBundle(Bundle.class)
                .execute();

            Bundle completeBundle = new Bundle( );
            bundleList = new ArrayList< Bundle.BundleEntryComponent >( bundle1.getEntry( ) );
            bundleList.addAll( bundle2.getEntry( ) );
            if ( bundle3 != null ) {
                bundleList.addAll( bundle3.getEntry( ) );
            }
            bundleList.addAll( bundle4.getEntry( ) );
            bundleList.addAll( bundle5.getEntry( ) );
            completeBundle.setEntry( bundleList );
            */   

    		s = fhirContext.newJsonParser( ).setPrettyPrint( true ).encodeResourceToString( completeBundle );
            
            json = Json.parse( s );
            
            return ok( json );
        }
    }
    
    public Result send() {
        return ok(send.render(""));
    }
}
