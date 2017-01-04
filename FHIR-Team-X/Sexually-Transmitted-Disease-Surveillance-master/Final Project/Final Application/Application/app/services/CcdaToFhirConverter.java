package services;

import model.XEncounter;
import model.XPatient;

import play.libs.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.text.SimpleDateFormat;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.codesystems.IdentifierType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Immunization;
import org.hl7.fhir.dstu3.model.Immunization.ImmunizationStatus;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Encounter.EncounterLocationComponent;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Patient.ContactComponent;
import org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;

public class CcdaToFhirConverter
{
    public CcdaToFhirConverter( )
    {
        this.diagnosticReports        = new ArrayList<DiagnosticReport>( );
        this.encounter                = new XEncounter( );
        this.immunizations            = new ArrayList<Immunization>( );
        this.location                 = new Location();
        this.medicationAdministrations= new ArrayList<MedicationAdministration>( );
        this.observations             = new ArrayList<Observation>( );
        this.conditions               = new ArrayList<Condition>( );
        this.patient                  = new XPatient( );
        this.practitioner             = new Practitioner( );
    }

    public String convert( Document p_Doc )
    {
        this.eICR = p_Doc;
        convertAuthor( ); // 1. 2. 3.
        convertEncounter( );
        convertPatient( ); // 19. 20. 21. 22. 23. 24. 25. 26. 27. 28. 29. 30. 31. 32. 33.
        convertPractitioner( ); // 4. 5. 6. 7. 8. 9. 10. 11.
        convertLocation();
        convertImmunizations( ); // 35.
        convertSymptoms(); // 39.
        convertMedicationsAdministered( ); // 44.
        convertResults( ); // 47.
        convertServiceEvent( ); // 48.
        storeResources( );
        return this.encounter.getPeriod( ).getStartElement( ).toString( ) + " / " + this.encounter.getPeriod( ).getEndElement( ).toString( );
    }

    // 1. Date of the Report
    // 2. Report Submission Date/Time
    // 3. Sending Application
    private void convertAuthor( ){
        String s;

        s = XPath.selectText( ROOT + ID + ATTR_ROOT, eICR );
        String caseReportId = ( s != null ) ? s : "";
        s = XPath.selectText( ROOT + EFFECTIVE_TIME + ATTR_VALUE, eICR );
        String effectiveTime = ( s != null ) ? s : "";
        s = XPath.selectText( ROOT + AUTHOR + TIME + ATTR_VALUE, eICR );
        String authoredTime = ( s != null ) ? s : "";
        s = XPath.selectText( ROOT + AUTHOR + ASSIGNED_AUTHOR + ID + ATTR_ROOT, eICR );
        String assignedAuthorId = ( s != null ) ? s : "";

        // TODO: The C-CDA date is formatted as 20151107094421+0000 but should be 2015-11-07T09:42:21Z+0000. Only the Z or +0000 should be used.
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss+0000");
        try {
            Date reportDate = sf.parse(effectiveTime);
            this.encounter.setReportDate( new DateTimeType( reportDate ) );
            
            Date authoredDate = sf.parse(authoredTime);
            this.encounter.setReportSubmissionDate( new DateTimeType( authoredDate ) );
        } catch ( Exception e ) {
            
        }
        this.encounter.setSendingApplication( new StringType( assignedAuthorId ) );
        
        /*Date issued = new Date( );
        Attachment attachment = new Attachment( );
        attachment.setTitle( assignedAuthorId );*/

        /* ASSIGN RESOURCE IDENTIFIERS */
            /*Identifier ident = this.diagnosticReport.addIdentifier( );
                ident.setSystem( "urn:system" ).setValue( caseReportId );

        this.diagnosticReport.setEffective( effective );
        this.diagnosticReport.setIssued( issued );
        this.diagnosticReport.addPresentedForm( attachment );*/
        }

    private void convertEncounter( )
    {
        convertEncounterIdentifier();
        convertEncounterAdmissionDateTime( );
        convertEncounterDiagnosis( );
    }

    //  4. Provider ID
    //  5. Provider Name
    //  6. Provider Phone
    //  7. Provider Fax
    //  8. Provider Email
    //  9. Provider Facility/Office Name
    // 10. Provider Address
    // 11. Provider County
    private void convertPractitioner( )
    {
        convertPractitionerIdentifier();
        convertPractitionerName( );
        convertPractitionerPhone( );
        Address pracAddress = convertAddress(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + RESPONSIBLE_PARTY + ASSIGNED_ENTITY);
        this.practitioner.addAddress(pracAddress);
    }

    private void convertLocation()
    {
        convertLocationIdentifier();
        convertLocationName();
        convertLocationType();
        convertLocationPhone();
        Address locationAddress = convertAddress(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + LOCATION + HEALTH_CARE_FACILITY + SERVICE_PROVIDER_ORG);
        this.location.setAddress(locationAddress);
        convertLocationHospitalUnit();
    }

    // 19. Patient ID
    // 20. Patient Name
    // 21. Patient/Guardian Name
    // 22. Patient/Guardian Phone
    // 23. Patient/Guardian Email
    // 24. Street Address
    // 25. Birth Date
    // 26. Patient Sex
    // 27. Patient Class
    // 28. Race
    // 29. Ethnicity
    // 30. Preferred Language
    // 31. Occupation
    // 32. Pregnant
    // 33. Travel history
    private void convertPatient( ) {
        String s = "";
        HumanName hn;
        ContactPoint cp;

        /* RETRIEVE VALUES FROM C-CDA DOCUMENT */

            // 19. Patient ID
                s = XPath.selectText(ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT_ID + ATTR_ROOT, eICR);
                String patientSystem = ( s != null ) ? s : "";
                s = XPath.selectText(ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT_ID + ATTR_EXTENSION, eICR);
                String patientId = ( s != null ) ? s : "";
            // 20. Patient Name
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + NAME + GIVEN, eICR );
                String givenName = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + NAME + FAMILY, eICR );
                String familyName = ( s != null ) ? s : "";
            // 21. Parent/Guardian Name
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + GUARDIAN + GUARDIAN_PERSON + NAME + GIVEN, eICR );
                String parentGuardianGivenName = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + GUARDIAN + GUARDIAN_PERSON + NAME + FAMILY, eICR );
                String parentGuardianFamilyName = ( s != null ) ? s : "";
            // 22. Parent/Guardian Phone
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + GUARDIAN + TELECOM + ATTR_VALUE, eICR );
                String parentGuardianPhone = ( s != null ) ? s : "";
            // 23. Parent/Guardian Email
                // TODO: Find this in the C-CDA document.
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + GUARDIAN + TELECOM + ATTR_VALUE, eICR );
                s = "abc@xyz.com";
                String parentGuardianEmail = ( s != null ) ? s : "";
            // 24. Street Address
                Address patientAddress = convertAddress( ROOT + RECORD_TARGET + PATIENT_ROLE );
                Address guardianAddress = convertAddress( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + GUARDIAN );
            // 25. Birth Date
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + BIRTHTIME + ATTR_VALUE, eICR );
                String birthDate = ( s != null ) ? s : "";
            // 26. Patient Sex
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + GENDER + ATTR_CODE, eICR );
                String patientGender = ( s != null ) ? s : "";
            // 27. Patient Class
                s = XPath.selectText( ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + CODE + ATTR_CODE, eICR );
                String patientClassCode = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + CODE + ATTR_CODE_SYSTEM, eICR );
                String patientClassCodeSystem = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + CODE + ATTR_DISPLAY_NAME, eICR );
                String patientClassDisplayName = ( s != null ) ? s : "";
            // 28. Race
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + RACE + ATTR_CODE_SYSTEM, eICR );
                String patientRaceSystem  = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + RACE + ATTR_CODE, eICR );
                String patientRaceCode  = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + RACE + ATTR_DISPLAY_NAME, eICR );
                String patientRaceDisplayName = ( s != null ) ? s : "";
            // 29. Ethnicity
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + ETHNICITY + ATTR_CODE_SYSTEM, eICR );
                String patientEthnicitySystem  = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + ETHNICITY + ATTR_CODE, eICR );
                String patientEthnicityCode = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + ETHNICITY + ATTR_DISPLAY_NAME, eICR );
                String patientEthnicityDisplayName = ( s != null ) ? s : "";
            // 31. Occupation
                s = XPath.selectText( OCCUPATION, eICR );
                String occupation  = ( s != null ) ? s : "";
                this.patient.setOccupationString(new StringType(occupation));
            // 45. Death date
                s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + DECEASED_INDICATOR + ATTR_VALUE, eICR );
                boolean deceasedIndicator = ( s != null ) ? s.trim().equalsIgnoreCase("true") : false;
                Date deceasedTime = new Date();
                if (deceasedIndicator)
                {
                    s = XPath.selectText( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + DECEASED_TIME + ATTR_VALUE, eICR );
                    try {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd"); 
                        deceasedTime = sf.parse(s);
                    } catch (Exception e) {
                        
                    }
                }
                

        /* STORE VALUES IN FHIR RESOURCE MODELS */

            // 19. Patient ID
                Identifier ident = this.patient.addIdentifier( );
                    ident.setValue( patientId.replace( "-", "" ) );
                    ident.setSystem( patientSystem );
                    CodeableConcept identType = new CodeableConcept( );
                    Coding coding = new Coding( );
                    IdentifierType idType = IdentifierType.SB;
                    coding.setSystem( idType.getSystem( ) );
                    coding.setCode( idType.toCode( ) );
                    coding.setDisplay( idType.getDisplay( ) );
                    identType.addCoding( coding );
                    ident.setType( identType );
                    ident.setUse( IdentifierUse.USUAL );
            // 20. Patient Name
                hn = new HumanName( );
                    hn.addGiven( givenName );
                    hn.addFamily( familyName );
                    this.patient.addName( hn );
            // 21. Parent/Guardian Name
            // 22. Parent/Guardian Phone
            // 23. Parent/Guardian Email
            // 24. Street Address
                Patient.ContactComponent cc = new Patient.ContactComponent( );
                    hn = new HumanName( );
                        hn.addGiven( parentGuardianGivenName );
                        hn.addFamily( parentGuardianFamilyName );
                        cc.setName( hn );
                    cp = new ContactPoint( );
                        cp.setValue( parentGuardianPhone );
                        cp.setSystem( ContactPoint.ContactPointSystem.PHONE );
                        cc.addTelecom( cp );
                    cp = new ContactPoint( );
                        cp.setValue( parentGuardianEmail );
                        cp.setSystem( ContactPoint.ContactPointSystem.EMAIL );
                        cc.addTelecom( cp );
                    cc.setAddress( guardianAddress );
                this.patient.addContact( cc );
                this.patient.addAddress(patientAddress);
            // 25. Birth Date
                if ( birthDate != null && !birthDate.trim().equals("")) {
                    int year = Integer.parseInt( birthDate.substring( 0, 4 ) );
                    int month = Integer.parseInt( birthDate.substring( 4, 6) ) - 1;
                    int day = Integer.parseInt( birthDate.substring( 6 ) );
                    DateType dt = new DateType( year, month, day );
                    this.patient.setBirthDateElement( dt ); }
            // 26. Patient Sex
                Enumerations.AdministrativeGender gender;
                gender = Enumerations.AdministrativeGender.UNKNOWN;
                if ( patientGender != null ) {
                    if ( patientGender.equalsIgnoreCase( "M" ) ) {
                        gender = Enumerations.AdministrativeGender.MALE; }
                    else if ( patientGender.equalsIgnoreCase( "F" ) ) {
                        gender = Enumerations.AdministrativeGender.FEMALE; } }
                this.patient.setGender(gender);
            // 27. Patient Class
                CodeableConcept patientClassConcept = new CodeableConcept( );
                coding = new Coding( );
                coding.setSystem( patientClassCodeSystem );
                coding.setCode( patientClassCode );
                coding.setDisplay( patientClassDisplayName );
                patientClassConcept.addCoding( coding );
                this.patient.setPatientClass(patientClassConcept);
            // 28. Race
                CodeableConcept race = new CodeableConcept( );
                    Coding raceCoding = new Coding( );
                    raceCoding.setSystem( patientRaceSystem );
                    raceCoding.setCode( patientRaceCode );
                    raceCoding.setDisplay( patientRaceDisplayName );
                    race.addCoding( raceCoding );
                    race.setText( "Race &amp; Ethnicity - CDC" );
                this.patient.setRace( race );
            // 29. Ethnicity
                CodeableConcept ethnicity = new CodeableConcept( );
                    Coding ethnicityCoding = new Coding( );
                    ethnicityCoding.setSystem( patientEthnicitySystem );
                    ethnicityCoding.setCode( patientEthnicityCode );
                    ethnicityCoding.setDisplay( patientEthnicityDisplayName );
                    ethnicity.addCoding( ethnicityCoding );
                    ethnicity.setText( "Race &amp; Ethnicity - CDC" );
                this.patient.setEthnicity( ethnicity );
            // 30. Preferred Language
                NodeList langNodes = XPath.selectNodes( ROOT + RECORD_TARGET + PATIENT_ROLE + PATIENT + LANGUAGE_COMMUNICATION, eICR );
                int langNodeCount = langNodes.getLength( );
                for ( int i = 0; i < langNodeCount; i++ ) {
                    Node langNode = langNodes.item( i );
                    s = XPath.selectText( "." + LANGUAGE_CODE + ATTR_CODE, langNode );
                    String languageCode = ( s != null ) ? s : "";
                    s = XPath.selectText( "." + PREFERENCE_IND + ATTR_VALUE, langNode );
                    String preferenceIndicator = ( s != null ) ? s : "";
                    CodeableConcept language = new CodeableConcept( );
                    coding = new Coding( );
                    coding.setCode( languageCode );
                    language.addCoding( coding );
                    Patient.PatientCommunicationComponent patientCommunication = new Patient.PatientCommunicationComponent();
                    patientCommunication.setLanguage(language);
                    patientCommunication.setPreferred(preferenceIndicator.equalsIgnoreCase("true"));
                    this.patient.addCommunication(patientCommunication);
                }
            // 45. Death date
                if (deceasedIndicator) {
                    DateTimeType deathTime = new DateTimeType(deceasedTime);
                    this.patient.setDeceased(deathTime);
                } else {
                    BooleanType deathIndicator = new BooleanType(deceasedIndicator);
                    this.patient.setDeceased(deathIndicator);
                }
        }

    private void convertEncounterIdentifier()
    {
        Identifier ident = this.encounter.addIdentifier();
        String s = "";
        s = XPath.selectText(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + ENCOUNTER_ID, eICR);
        if (s == null)
        {
            ident.setValue(NOT_FOUND);
        }
        else
        {
            ident.setSystem("urn:system").setValue(s);
        }
    }

    private void convertEncounterAdmissionDateTime( )
    {
        String s = "";
        DateTimeType dateTimeTyp = null;
        Period period = new Period( );
        boolean hasChanged = false;

        // Get the encounter start date/time from the C-CDA document.
            s = XPath.selectText( ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + EFFECTIVE_TIME + LOW + ATTR_VALUE, eICR );
            if ( s == null )
            {
                // no default value
            }
            else
            {
                if ( s.length() != 8 )
                {
                    // no default value
                }
                else
                {
                    dateTimeTyp = new DateTimeType( s.substring( 0, 4 ) + "-" + s.substring( 4, 6 ) + "-" + s.substring( 6, 8 ) );
                    period.setStartElement( dateTimeTyp );
                    hasChanged = true;
                }
            }

        // Get the encounter end date/time from the C-CDA document.
            s = XPath.selectText( ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + EFFECTIVE_TIME + HIGH + ATTR_VALUE, eICR );
            if ( s == null )
            {
                // no default value
            }
            else
            {
                if ( s.length() != 8 )
                {
                    // no default value
                }
                else
                {
                    dateTimeTyp = new DateTimeType( s.substring( 0, 4 ) + "-" + s.substring( 4, 6 ) + "-" + s.substring( 6, 8 ) );
                    period.setEndElement( dateTimeTyp );
                    hasChanged = true;
                }
            }

        // Update the resource.
            if ( hasChanged )
            {
                this.encounter.setPeriod( period );
            }
    }
    
    // 42. Diagnosis, 43. Date of diagnosis
    private void convertEncounterDiagnosis( )
    {
        String s, code, displayName, codeSystem = "";
        
        s = XPath.selectText(DIAGNOSIS + ATTR_CODE, eICR);
        if (s == null)
        {
            code = NOT_FOUND;
        }
        else
        {
            code = s;
        }
        s = XPath.selectText(DIAGNOSIS + ATTR_DISPLAY_NAME, eICR);
        if (s == null)
        {
            displayName = NOT_FOUND;
        }
        else
        {
            displayName = s;
        }
        s = XPath.selectText(DIAGNOSIS + ATTR_CODE_SYSTEM, eICR);
        if (s == null)
        {
            codeSystem = NOT_FOUND;
        }
        else
        {
            codeSystem = s;
        }
        
        
        Identifier encounterIdent = this.encounter.getIdentifierFirstRep();
        Identifier condDiagnosisIdent = new Identifier();
        condDiagnosisIdent.setSystem(encounterIdent.getSystem()).setValue(encounterIdent.getValue() + "_diagnosis");
        Condition diagnosis = new Condition();
        diagnosis.addIdentifier(condDiagnosisIdent);
        
        CodeableConcept diagnosisConcept = new CodeableConcept( );
        Coding coding = new Coding( );
        coding.setCode( code );
        coding.setSystem( codeSystem );
        coding.setDisplay( displayName );
        diagnosisConcept.addCoding( coding );
        diagnosis.setCode( diagnosisConcept );
        
        s = XPath.selectText(DIAGNOSIS_DATE, eICR);
        if (s != null)
        {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd"); 
                Date dateRecorded = sf.parse(s);
                diagnosis.setDateRecorded(dateRecorded);
            } catch (Exception e) {
                
            }
        }
        
        CodeableConcept categoryConcept = new CodeableConcept( );
        coding = new Coding( );
        coding.setCode( "diagnosis" );
        coding.setSystem( "http://hl7.org/fhir/condition-category" );
        coding.setDisplay( "Diagnosis" );
        categoryConcept.addCoding( coding );
        diagnosis.addCategory( categoryConcept );
        diagnosis.setVerificationStatus(Condition.ConditionVerificationStatus.CONFIRMED);
        diagnosis.setSubject(new Reference(this.patient));
        diagnosis.setContext(new Reference(this.encounter));
        diagnosis.setAsserter(new Reference(this.practitioner));
        this.conditions.add(diagnosis);
    }

    private void convertPractitionerIdentifier()
    {
        Identifier ident = this.practitioner.addIdentifier();
        String s = "";
        s = XPath.selectText(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + RESPONSIBLE_PARTY + ASSIGNED_ENTITY + PROVIDER_ID + ATTR_ROOT, eICR);
        if (s == null)
        {
            ident.setSystem(NOT_FOUND);
        }
        else
        {
            ident.setSystem(s);
        }
        s = XPath.selectText(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + RESPONSIBLE_PARTY + ASSIGNED_ENTITY + PROVIDER_ID + ATTR_EXTENSION, eICR);
        if (s == null)
        {
            ident.setValue(NOT_FOUND);
        }
        else
        {
            ident.setValue(s);
        }
    }

    private void convertPractitionerName( )
    {
        String s = "";
        HumanName name = new HumanName( );

        s = XPath.selectText( ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + RESPONSIBLE_PARTY + ASSIGNED_ENTITY + ASSIGNED_PERSON + NAME + GIVEN, eICR );
        if ( s == null )
        {
            name.addGiven( NOT_FOUND );
        }
        else
        {
            name.addGiven( s );
        }

        s = XPath.selectText( ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + RESPONSIBLE_PARTY + ASSIGNED_ENTITY + ASSIGNED_PERSON + NAME + FAMILY, eICR );
        if ( s == null )
        {
            name.addFamily( NOT_FOUND );
        }
        else
        {
            name.addFamily( s );
        }

        this.practitioner.addName( name );
    }

    private void convertPractitionerPhone( )
    {
        String s = "";
        ContactPoint phone = new ContactPoint( );

        s = XPath.selectText( ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + RESPONSIBLE_PARTY + ASSIGNED_ENTITY + TELECOM + ATTR_VALUE, eICR );
        if ( s == null )
        {
            phone.setValue( NOT_FOUND );
        }
        else
        {
            phone.setSystem( ContactPoint.ContactPointSystem.PHONE );
            phone.setValue( s );
        }

        this.practitioner.addTelecom( phone );
    }

    private Address convertAddress( String p_BasePath ) {
        String s;

        /* RETRIEVE VALUES FROM C-CDA DOCUMENT */
        s = XPath.selectText( p_BasePath + ADDRESS + STREET_ADDRESS, eICR );
        String street = ( s != null ) ? s : "";
        s = XPath.selectText( p_BasePath + ADDRESS + CITY, eICR );
        String city = ( s != null ) ? s : "";
        s = XPath.selectText( p_BasePath + ADDRESS + STATE, eICR );
        String state = ( s != null ) ? s : "";
        s = XPath.selectText( p_BasePath + ADDRESS + POSTAL_CODE, eICR );
        String postalCode = ( s != null ) ? s : "";
        s = XPath.selectText( p_BasePath + ADDRESS + COUNTRY, eICR );
        String country = ( s != null ) ? s : "";

        /* STORE VALUES IN FHIR RESOURCE MODELS */
        Address address = new Address( );
            address.addLine( street );
            address.setCity( city );
            address.setState( state );
            address.setPostalCode( postalCode );
            address.setCountry( country );

        return address;
        }

    private void convertLocationIdentifier()
    {
        Identifier ident = this.location.addIdentifier();
        String s = "";
        s = XPath.selectText(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + LOCATION + HEALTH_CARE_FACILITY + PROVIDER_ID + ATTR_ROOT, eICR);
        if (s == null)
        {
            ident.setSystem(NOT_FOUND);
        }
        else
        {
            ident.setSystem(s);
        }
        s = XPath.selectText(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + LOCATION + HEALTH_CARE_FACILITY + PROVIDER_ID + ATTR_EXTENSION, eICR);
        if (s == null)
        {
            ident.setValue(NOT_FOUND);
        }
        else
        {
            ident.setValue(s);
        }
    }

    private void convertLocationName()
    {
        String s = "";
        s = XPath.selectText(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + LOCATION + HEALTH_CARE_FACILITY + SERVICE_PROVIDER_ORG + NAME, eICR);
        if (s == null)
        {
            s = NOT_FOUND;
        }
        this.location.setName(s);
    }

    private void convertLocationType()
    {
        CodeableConcept facilityTypeCoding = new CodeableConcept();
        Coding coding = new Coding();
        String s = "";
        s = XPath.selectText(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + LOCATION + HEALTH_CARE_FACILITY + FACILITY_TYPE + ATTR_CODE_SYSTEM, eICR);
        if (s == null)
        {
            coding.setSystem(NOT_FOUND);
        }
        else
        {
            coding.setSystem(s);
        }

        s = XPath.selectText(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + LOCATION + HEALTH_CARE_FACILITY + FACILITY_TYPE + ATTR_CODE, eICR);
        if (s == null)
        {
            coding.setCode(NOT_FOUND);
        }
        else
        {
            coding.setCode(s);
        }

        s = XPath.selectText(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + LOCATION + HEALTH_CARE_FACILITY + FACILITY_TYPE + ATTR_DISPLAY_NAME, eICR);
        if (s != null)
        {
            coding.setDisplay(s);
        }
        facilityTypeCoding.addCoding(coding);
        this.location.setType(facilityTypeCoding);
    }

    private void convertLocationPhone()
    {
        String s = "";
        ContactPoint phone = new ContactPoint();

        s = XPath.selectText(ROOT + COMPONENT_OF + ENCOMPASSING_ENCOUNTER + LOCATION + HEALTH_CARE_FACILITY + SERVICE_PROVIDER_ORG + TELECOM + ATTR_VALUE, eICR);
        if (s == null)
        {
            phone.setValue(NOT_FOUND);
        }
        else
        {
            phone.setSystem(ContactPointSystem.PHONE);
            phone.setValue(s);
        }

        this.location.addTelecom(phone);
    }

    // 35. Immunization history
    private void convertImmunizations( ) {
        CodeableConcept codeableConcept;
        Coding coding;
        String s;
        Node node;
        NodeList nodes;
        int nodeCount;
        SimpleDateFormat sf = null;
        SimpleQuantity simpleQuantity;
        String ENTRY_NODE_ROOT = ".";

        /* RETRIEVE VALUES FROM C-CDA DOCUMENT */
            // 35. Immunization history
                nodes = XPath.selectNodes( ROOT + COMPONENT + STRUCTURED_BODY + COMPONENT + SECTION + TEMPLATE_ID_IMMUNIZATION + PARENT + ENTRY, eICR );
                nodeCount = nodes.getLength( );

                for ( int i = 0; i < nodeCount; i++ ) {
                    node = nodes.item( i );
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + ID + ATTR_ROOT, node );
                            String id = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + STATUS_CODE + ATTR_CODE, node );
                            String statusCode = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + EFFECTIVE_TIME + ATTR_VALUE, node );
                            String effectiveTime = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + DOSE_QUANTITY + ATTR_VALUE, node );
                            String doseQuantity = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + ROUTE_CODE + ATTR_CODE, node );
                            String routeCode = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + ROUTE_CODE + ATTR_CODE_SYSTEM, node );
                            String routeCodeSystem = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + ROUTE_CODE + ATTR_CODE_SYSTEM_NAME, node );
                            String routeCodeSystemName = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + ROUTE_CODE + ATTR_DISPLAY_NAME, node );
                            String routeDisplayName = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + CONSUMABLE + MANUFACTURED_PRODUCT + MANUFACTURED_MATERIAL + CODE + ATTR_CODE, node );
                            String materialCode = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + CONSUMABLE + MANUFACTURED_PRODUCT + MANUFACTURED_MATERIAL + CODE + ATTR_CODE_SYSTEM, node );
                            String codeSystem = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + CONSUMABLE + MANUFACTURED_PRODUCT + MANUFACTURED_MATERIAL + CODE + ATTR_CODE_SYSTEM_NAME, node );
                            String codeSystemName = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + SUBSTANCE_ADMINISTRATION + CONSUMABLE + MANUFACTURED_PRODUCT + MANUFACTURED_MATERIAL + CODE + ATTR_DISPLAY_NAME, node );
                            String displayName = ( s != null ) ? s : "";

                        Immunization immunization = new Immunization( );
                        immunization.setId( IdDt.newRandomUuid( ) );
                        Identifier identifier = new Identifier( );
                        identifier.setSystem( "urn:system" ).setValue( this.encounter.getIdentifierFirstRep().getValue() + "_immunization[" + String.valueOf(i) + "]" );
                        immunization.addIdentifier( identifier );

                        try {
                            immunization.setStatus( Immunization.ImmunizationStatus.fromCode( statusCode ) );
                            }
                            catch ( Exception e ) { }
                        
                        try {
                            if ( effectiveTime.length( ) == 8 ) {
                                sf = new SimpleDateFormat( "yyyyMMdd" );
                                }
                            if ( effectiveTime.length( ) == 6 ) {
                                sf = new SimpleDateFormat( "yyyyMM" );
                                }
                            Date effectiveDate = sf.parse( effectiveTime );
                            immunization.setDate( effectiveDate );
                            }
                            catch ( Exception e ) { }

                        codeableConcept = new CodeableConcept( );
                        coding = new Coding( );
                        coding.setCode( materialCode );
                        coding.setSystem( codeSystem );
                        coding.setDisplay( displayName );
                        codeableConcept.addCoding( coding );
                        immunization.setVaccineCode( codeableConcept );
                        
                        try {
                            simpleQuantity = new SimpleQuantity( );
                            simpleQuantity.setValue( Long.parseLong( doseQuantity ) );
                            immunization.setDoseQuantity( simpleQuantity );
                            }
                            catch ( Exception e ) { }
                        
                        codeableConcept = new CodeableConcept( );
                        coding = new Coding( );
                        coding.setCode( routeCode );
                        coding.setSystem( routeCodeSystem );
                        coding.setDisplay( routeDisplayName );
                        codeableConcept.addCoding( coding );
                        immunization.setRoute( codeableConcept );
                        
                        immunization.setPatient( new Reference( this.patient ) );
                        this.immunizations.add( immunization );
                    }
        } // convertImmunizations()

    // 39. Symptoms
    private void convertSymptoms() {
        CodeableConcept codeableConcept;
        Coding coding;
        DateTimeType dateTimeType;
        int entryNodeCount, observationNodeCount;
        Node entryNode, observationNode;
        NodeList entryNodes, observationNodes;
        Observation observation;
        Observation.ObservationReferenceRangeComponent referenceRange;
        Period period;
        Quantity quantity;
        SimpleQuantity simpleQuantity;
        String code, codeSystem, displayName, effectiveTime, s, statusCode, value, valueCode, valueCodeSystem, valueCodeSystemName, valueDisplayName, valueType, valueUnit;
        String ENTRY_NODE_ROOT = ".";
        String OBSERVATION_NODE_ROOT = ".";

            entryNodes = XPath.selectNodes( ROOT + COMPONENT + STRUCTURED_BODY + COMPONENT + SECTION + TEMPLATE_ID_SYMPTOMS + PARENT + ENTRY, eICR );
            entryNodeCount = entryNodes.getLength( );
            for ( int i = 0; i < entryNodeCount; i++ ) {
                /* RETRIEVE VALUES FROM C-CDA DOCUMENT */
                    entryNode = entryNodes.item( i );
                        observationNodes = XPath.selectNodes( ENTRY_NODE_ROOT + ACT + ENTRY_RELATIONSHIP + OBSERVATION, entryNode );
                        observationNodeCount = observationNodes.getLength( );
                        for ( int j = 0; j < observationNodeCount; j++ ) {
                            /* RETRIEVE OBSERVATIONS FROM ENTRY */
                                observationNode = observationNodes.item( j );
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + CODE + ATTR_CODE, observationNode );
                                        code = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + CODE + ATTR_CODE_SYSTEM, observationNode );
                                        codeSystem = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + STATUS_CODE + ATTR_CODE, observationNode );
                                        statusCode = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + EFFECTIVE_TIME + LOW + ATTR_VALUE, observationNode );
                                        effectiveTime = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_TYPE, observationNode );
                                        valueType = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_CODE, observationNode );
                                        valueCode = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_CODE_SYSTEM, observationNode );
                                        valueCodeSystem = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_DISPLAY_NAME, observationNode );
                                        valueDisplayName = ( s != null ) ? s : "";
                                        
                                    if ( codeSystem.equals("2.16.840.1.113883.6.1") && (valueType.equals( "CO" ) ||  valueType.equals ( "CD" ) )) {
                                        Identifier encounterIdent = this.encounter.getIdentifierFirstRep();
                                        Identifier conditionIdent = new Identifier();
                                        
                                        conditionIdent.setSystem(encounterIdent.getSystem()).setValue(encounterIdent.getValue() + "_problem[" + 
                                        String.valueOf(i) + "][" + String.valueOf(j) + "]");

                                    /* ASSIGN RESOURCE IDENTIFIERS */
                                        Condition condition = new Condition( );
                                        condition.setId( IdDt.newRandomUuid( ) );
                                        condition.addIdentifier( conditionIdent );

                                    /* STORE VALUES IN FHIR RESOURCE MODELS */
                                        // Category
                                            CodeableConcept categoryConcept = new CodeableConcept( );
                                            coding = new Coding( );
                                            coding.setSystem( "http://hl7.org/fhir/condition-category" );
                                            if (code.equals("75325-1"))
                                            {
                                                coding.setCode( "symptom" );
                                                coding.setDisplay( "Symptom" );
                                            }
                                            else if (code.equals("75322-8"))
                                            {
                                                coding.setCode( "complaint" );
                                                coding.setDisplay( "Complaint" );
                                            }
                                            categoryConcept.addCoding( coding );
                                            condition.addCategory( categoryConcept );
                                            if (statusCode.equalsIgnoreCase("completed"))
                                            {
                                                condition.setVerificationStatus(Condition.ConditionVerificationStatus.CONFIRMED);
                                            }
                                            else
                                            {
                                                condition.setVerificationStatus(Condition.ConditionVerificationStatus.UNKNOWN);
                                            }
                                            condition.setSubject(new Reference(this.patient));
                                            condition.setContext(new Reference(this.encounter));
                                            condition.setAsserter(new Reference(this.practitioner));
                                        // Effective date
                                        // 38. Date of Onset
                                            if (!effectiveTime.trim().equals(""))
                                            {
                                                try {
                                                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd"); 
                                                    Date onsetDate = sf.parse(effectiveTime);
                                                    DateTimeType dt = new DateTimeType(onsetDate);
                                                    condition.setOnset(dt);
                                                } catch (Exception e) {
                                                    
                                                }
                                            }
                                            codeableConcept = new CodeableConcept( );
                                            coding = new Coding( );
                                            coding.setCode( valueCode );
                                            coding.setSystem( valueCodeSystem );
                                            coding.setDisplay( valueDisplayName );
                                            codeableConcept.addCoding( coding );
                                            condition.setCode( codeableConcept );
                                            this.conditions.add( condition );
                                    } // end condition add
                        } //  for j < observationNodeCount
            } // for i < entryNodeCount
    }

    // 44. Medication provided
    private void convertMedicationsAdministered( ) {
        String s;
        Node node;
        NodeList nodes;
        int nodeCount;

        /* RETRIEVE VALUES FROM C-CDA DOCUMENT */
            // 44. Medication provided
                nodes = XPath.selectNodes( ROOT + COMPONENT + STRUCTURED_BODY + COMPONENT + SECTION + TEMPLATE_ID_MEDS_ADMIN + PARENT + ENTRY, eICR );
                nodeCount = nodes.getLength( );

                for ( int i = 0; i < nodeCount; i++ ) {
                    node = nodes.item( i );
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + STATUS_CODE + ATTR_CODE, node );
                            String statusCode = ( s != null ) ? s : "";
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + EFFECTIVE_TIME + ATTR_VALUE, node );
                            String effectiveTime = ( s != null ) ? s : "";
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + DOSE_QUANTITY + ATTR_VALUE, node );
                            String doseQuantity = ( s != null ) ? s : "";
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + ROUTE_CODE + ATTR_CODE, node );
                            String routeCode = ( s != null ) ? s : "";
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + ROUTE_CODE + ATTR_CODE_SYSTEM, node );
                            String routeCodeSystem = ( s != null ) ? s : "";
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + ROUTE_CODE + ATTR_CODE_SYSTEM_NAME, node );
                            String routeCodeSystemName = ( s != null ) ? s : "";
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + ROUTE_CODE + ATTR_DISPLAY_NAME, node );
                            String routeDisplayName = ( s != null ) ? s : "";
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + CONSUMABLE + MANUFACTURED_PRODUCT + MANUFACTURED_MATERIAL + CODE + ATTR_CODE, node );
                            String materialCode = ( s != null ) ? s : "";
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + CONSUMABLE + MANUFACTURED_PRODUCT + MANUFACTURED_MATERIAL + CODE + ATTR_CODE_SYSTEM, node );
                            String codeSystem = ( s != null ) ? s : "";
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + CONSUMABLE + MANUFACTURED_PRODUCT + MANUFACTURED_MATERIAL + CODE + ATTR_CODE_SYSTEM_NAME, node );
                            String codeSystemName = ( s != null ) ? s : "";
                        s = XPath.selectText( "/" + SUBSTANCE_ADMINISTRATION + CONSUMABLE + MANUFACTURED_PRODUCT + MANUFACTURED_MATERIAL + CODE + ATTR_DISPLAY_NAME, node );
                            String displayName = ( s != null ) ? s : "";
                            
                        MedicationAdministration medAdmin = new MedicationAdministration();
                        Identifier medIdentifier = new Identifier();
                        medIdentifier.setSystem("urn:system").setValue(this.encounter.getIdentifierFirstRep().getValue() + "_medAdmin");
                        medAdmin.addIdentifier(medIdentifier);
                        try {
                            medAdmin.setStatus(MedicationAdministration.MedicationAdministrationStatus.fromCode(statusCode));
                        } catch (Exception e) {
                        }
                        
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                        try {
                            Date effectiveDate = sf.parse(effectiveTime);
                            DateTimeType effectiveDateTimeType = new DateTimeType(effectiveDate);
                            medAdmin.setEffectiveTime(effectiveDateTimeType);
                        } catch (Exception e) {
                        }
                        
                        CodeableConcept medConcept = new CodeableConcept( );
                        Coding coding = new Coding( );
                        coding.setCode( materialCode );
                        coding.setSystem( codeSystem );
                        coding.setDisplay( displayName );
                        medConcept.addCoding( coding );
                        medAdmin.setMedication( medConcept );
                        
                        MedicationAdministration.MedicationAdministrationDosageComponent dosage = new MedicationAdministration.MedicationAdministrationDosageComponent();
                        if (doseQuantity != null && doseQuantity.trim() != "") {
                            SimpleQuantity sq = new SimpleQuantity();
                            sq.setValue(Long.parseLong(doseQuantity));
                            dosage.setDose(sq);
                        }
                        
                        CodeableConcept routeConcept = new CodeableConcept( );
                        coding = new Coding( );
                        coding.setCode( routeCode );
                        coding.setSystem( routeCodeSystem );
                        coding.setDisplay( routeDisplayName );
                        routeConcept.addCoding( coding );
                        dosage.setRoute( routeConcept );
                        
                        medAdmin.setDosage(dosage);
                        
                        medAdmin.setPatient(new Reference(this.patient));
                        medAdmin.setPerformer(new Reference(this.practitioner));
                        medAdmin.setEncounter(new Reference(this.encounter));
                        this.medicationAdministrations.add(medAdmin);
                    }
        } // convertMedicationsAdministered()

    // 47. Laboratory results
    // &
    // 49. Laboratory test(s) performed
    private void convertResults( ) {
        CodeableConcept codeableConcept;
        Coding coding;
        DateTimeType dateTimeType;
        int entryNodeCount, observationNodeCount;
        Node entryNode, observationNode;
        NodeList entryNodes, observationNodes;
        Observation observation;
        Observation.ObservationReferenceRangeComponent referenceRange;
        Period period;
        Quantity quantity;
        SimpleQuantity simpleQuantity;
        String classCode, code, codeSystem, codeSystemName, displayName, effectiveTime, id, idRoot, interpCode, interpCodeSystem, 
               moodCode, rangeCode, rangeCodeSystem, rangeCodeSystemName, rangeDisplayName, rangeText, rangeLowValue, 
               rangeLowUnit, rangeHighValue, rangeHighUnit, rangeInterpCode, rangeInterpCodeSystem, rangeValueType, s, 
               statusCode, testCode, testCodeSystem, testDisplayName, value, valueCode, valueCodeSystem, valueCodeSystemName, valueDisplayName, valueType, valueUnit;
        String ENTRY_NODE_ROOT = ".";
        String OBSERVATION_NODE_ROOT = ".";

        // 47. Laboratory results
            entryNodes = XPath.selectNodes( ROOT + COMPONENT + STRUCTURED_BODY + COMPONENT + SECTION + TEMPLATE_ID_RESULTS + PARENT + ENTRY, eICR );
            entryNodeCount = entryNodes.getLength( );
            for ( int i = 0; i < entryNodeCount; i++ ) {
                /* RETRIEVE VALUES FROM C-CDA DOCUMENT */
                    entryNode = entryNodes.item( i );
                        s = XPath.selectText( ENTRY_NODE_ROOT + ORGANIZER + ATTR_CLASS_CODE, entryNode );
                            classCode = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + ORGANIZER + ATTR_MOOD_CODE, entryNode );
                            moodCode = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + ORGANIZER + STATUS_CODE + ATTR_CODE, entryNode );
                            statusCode = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + ORGANIZER + ID + ATTR_ROOT, entryNode );
                            idRoot = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + ORGANIZER + CODE + ATTR_CODE, entryNode );
                            testCode = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + ORGANIZER + CODE + ATTR_CODE_SYSTEM, entryNode );
                            testCodeSystem = ( s != null ) ? s : "";
                        s = XPath.selectText( ENTRY_NODE_ROOT + ORGANIZER + CODE + ATTR_DISPLAY_NAME, entryNode );
                            testDisplayName = ( s != null ) ? s : "";
                        // 49. Laboratory test(s) performed
                        DiagnosticReport dr = new DiagnosticReport( );
                        dr.setId ( IdDt.newRandomUuid( ) );
                        Identifier ident = new Identifier( );
                        ident.setSystem( "urn:system" ).setValue( this.encounter.getIdentifierFirstRep().getValue() + "_test[" + String.valueOf(i) + "]");
                        dr.addIdentifier(ident);
                        codeableConcept = new CodeableConcept( );
                        coding = new Coding( );
                        coding.setCode( testCode );
                        coding.setSystem( testCodeSystem );
                        coding.setDisplay( testDisplayName );
                        codeableConcept.addCoding( coding );
                        dr.setCode(codeableConcept);
                        // 47. Laboratory results
                        observationNodes = XPath.selectNodes( ENTRY_NODE_ROOT + ORGANIZER + COMPONENT + OBSERVATION, entryNode );
                        observationNodeCount = observationNodes.getLength( );
                        for ( int j = 0; j < observationNodeCount; j++ ) {
                            /* RETRIEVE OBSERVATIONS FROM ENTRY */
                                observationNode = observationNodes.item( j );
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + ID + ATTR_ROOT, observationNode );
                                        id = ( s != null ) ? s + "[" + Integer.toString( j ) + "]" : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + CODE + ATTR_CODE, observationNode );
                                        code = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + CODE + ATTR_CODE_SYSTEM, observationNode );
                                        codeSystem = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + CODE + ATTR_CODE_SYSTEM_NAME, observationNode );
                                        codeSystemName = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + CODE + ATTR_DISPLAY_NAME, observationNode );
                                        displayName = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + EFFECTIVE_TIME + ATTR_VALUE, observationNode );
                                        effectiveTime = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_TYPE, observationNode );
                                        valueType = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_VALUE, observationNode );
                                        value = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_UNIT, observationNode );
                                        valueUnit = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_CODE, observationNode );
                                        valueCode = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_CODE_SYSTEM, observationNode );
                                        valueCodeSystem = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_CODE_SYSTEM_NAME, observationNode );
                                        valueCodeSystemName = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + VALUE + ATTR_DISPLAY_NAME, observationNode );
                                        valueDisplayName = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + INTERPRETATION_CODE + ATTR_CODE, observationNode );
                                        interpCode = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + INTERPRETATION_CODE + ATTR_CODE_SYSTEM, observationNode );
                                        interpCodeSystem = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + TEXT, observationNode );
                                        rangeText = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + VALUE + ATTR_TYPE, observationNode );
                                        rangeValueType = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + VALUE + LOW + ATTR_VALUE, observationNode );
                                        rangeLowValue = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + VALUE + LOW + ATTR_UNIT, observationNode );
                                        rangeLowUnit = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + VALUE + HIGH + ATTR_VALUE, observationNode );
                                        rangeHighValue = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + VALUE + HIGH + ATTR_UNIT, observationNode );
                                        rangeHighUnit = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + VALUE + ATTR_CODE, observationNode );
                                        rangeCode = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + VALUE + ATTR_CODE_SYSTEM, observationNode );
                                        rangeCodeSystem = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + VALUE + ATTR_CODE_SYSTEM_NAME, observationNode );
                                        rangeCodeSystemName = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + VALUE + ATTR_DISPLAY_NAME, observationNode );
                                        rangeDisplayName = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + INTERPRETATION_CODE + ATTR_CODE, observationNode );
                                        rangeInterpCode = ( s != null ) ? s : "";
                                    s = XPath.selectText( OBSERVATION_NODE_ROOT + REFERENCE_RANGE + OBSERVATION_RANGE + INTERPRETATION_CODE + ATTR_CODE_SYSTEM, observationNode );
                                        rangeInterpCodeSystem = ( s != null ) ? s : "";

                                /* ASSIGN RESOURCE IDENTIFIERS */
                                    observation = new Observation( );
                                    observation.setId( IdDt.newRandomUuid( ) );
                                    ident = observation.addIdentifier( );
                                        ident.setSystem( "urn:system" ).setValue( this.encounter.getIdentifierFirstRep().getValue() + "_labResult[" + String.valueOf(i) + "][" + String.valueOf(j) + "]");

                                /* STORE VALUES IN FHIR RESOURCE MODELS */
                                    // Category
                                        codeableConcept = new CodeableConcept( );
                                        coding = new Coding( );
                                        coding.setCode( "laboratory" );
                                        coding.setSystem( "http://hl7.org/fhir/observation-category" );
                                        coding.setDisplay( "Laboratory" );
                                        codeableConcept.addCoding( coding );
                                        observation.addCategory(codeableConcept);
                                    // Status
                                        observation.setStatus( Observation.ObservationStatus.FINAL );
                                    // Observation code
                                        codeableConcept = new CodeableConcept( );
                                            coding = new Coding( );
                                            coding.setCode( code );
                                            coding.setSystem( codeSystem );
                                            coding.setDisplay( displayName );
                                            codeableConcept.addCoding( coding );
                                        observation.setCode( codeableConcept );
                                    // Effective date
                                        s = effectiveTime;
                                            dateTimeType = new DateTimeType( s.substring( 0, 4 ) + "-" + s.substring( 4, 6 ) + "-" + s.substring( 6, 8 ) );
                                            period = new Period( );
                                            period.setStartElement( dateTimeType );
                                        observation.setEffective( period );
                                    // Observation value as code
                                        if ( valueType.equals( "CO" ) || valueType.equals ( "CD" ) ) {
                                            codeableConcept = new CodeableConcept( );
                                                coding = new Coding( );
                                                coding.setCode( valueCode );
                                                coding.setSystem( valueCodeSystem );
                                                coding.setDisplay( valueDisplayName );
                                                codeableConcept.addCoding( coding );
                                            observation.setValue( codeableConcept );
                                            }
                                    // Observation value as percentage
                                        if ( valueType.equals( "PQ" ) ) {
                                            if (value != null && !value.trim().equals("")) {
                                                quantity = new Quantity();
                                                quantity.setValue( Double.parseDouble( value ) );
                                                quantity.setUnit( valueUnit );
                                                observation.setValue( quantity );
                                            }
                                            }
                                    // Interpretation
                                        codeableConcept = new CodeableConcept( );
                                            coding = new Coding( );
                                            coding.setCode( interpCode );
                                            coding.setSystem( interpCodeSystem );
                                        observation.setInterpretation( codeableConcept );
                                    // Reference range
                                        referenceRange = observation.addReferenceRange( );
                                            referenceRange.setText( rangeText );
                                        // Reference range value as code
                                        if ( rangeValueType.equals( "CO" ) || rangeValueType.equals( "CD" ) ) {
                                            codeableConcept = new CodeableConcept( );
                                                coding = new Coding( );
                                                coding.setCode( rangeCode );
                                                coding.setSystem( rangeCodeSystem );
                                                coding.setDisplay( rangeDisplayName );
                                                codeableConcept.addCoding( coding );
                                            referenceRange.addMeaning( codeableConcept );
                                            }
                                        // Reference range value as percentage
                                            if ( valueType.equals( "IVL_PQ" ) || valueType.equals( "PQ" ) ) {
                                                if (rangeLowValue != null && !rangeLowValue.trim().equals("")) {
                                                    simpleQuantity = new SimpleQuantity( );
                                                    simpleQuantity.setValue( Double.parseDouble( rangeLowValue ) );
                                                    referenceRange.setLow( simpleQuantity );
                                                }
                                                if (rangeHighValue != null && !rangeHighValue.trim().equals("")) {
                                                    simpleQuantity = new SimpleQuantity( );
                                                    simpleQuantity.setValue( Double.parseDouble( rangeHighValue ) );
                                                    referenceRange.setHigh( simpleQuantity );
                                                }
                                            }
                                            observation.setSubject( new Reference( this.patient ) );
                                            observation.setEncounter( new Reference( this.encounter ) );
                                            observation.setId( IdDt.newRandomUuid( ) );
                                            dr.addResult( new Reference (observation) );
                                            dr.setSubject( new Reference( this.patient ) );
                                            dr.setEncounter( new Reference( this.encounter ) );
                                            // Add to list of observations
                                            this.observations.add( observation );
                        } //  for j < observationNodeCount
                        this.diagnosticReports.add(dr);
            } // for i < entryNodeCount
        } // convertResults()

    // 48. Trigger code that initiated electronic case report
    private void convertServiceEvent( ) {
        String s;

        /* RETRIEVE VALUES FROM C-CDA DOCUMENT */
            // 48. Trigger code that initiated electronic case report
                s = XPath.selectText( ROOT + DOCUMENTATION_OF + SERVICE_EVENT + CODE + ATTR_CODE, eICR );
                String code = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + DOCUMENTATION_OF + SERVICE_EVENT + CODE + ATTR_CLASS_CODE, eICR );
                String classCode = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + DOCUMENTATION_OF + SERVICE_EVENT + CODE + ATTR_CODE_SYSTEM, eICR );
                String codeSystem = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + DOCUMENTATION_OF + SERVICE_EVENT + CODE + ATTR_CODE_SYSTEM_NAME, eICR );
                String codeSystemName = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + DOCUMENTATION_OF + SERVICE_EVENT + CODE + ATTR_CODE_SYSTEM_VERSION, eICR );
                String codeSystemVersion = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + DOCUMENTATION_OF + SERVICE_EVENT + CODE + ATTR_DISPLAY_NAME, eICR );
                String displayName = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + DOCUMENTATION_OF + SERVICE_EVENT + CODE + ATTR_VALUE_SET_VERSION, eICR );
                String valueSetVersion = ( s != null ) ? s : "";
                s = XPath.selectText( ROOT + DOCUMENTATION_OF + SERVICE_EVENT + EFFECTIVE_TIME + LOW + ATTR_VALUE, eICR );
                String lowEffectiveTime = ( s != null ) ? s : "";

        /* STORE VALUES IN FHIR RESOURCE MODELS */
            // 48. Trigger code that initiated electronic case report
                CodeableConcept reason = new CodeableConcept( );
                    Coding coding = new Coding( );
                    coding.setCode( code );
                    coding.setSystem( codeSystem );
                    coding.setDisplay( displayName );
                    reason.addCoding( coding );
                    this.encounter.setTriggerCode( reason );
        } // convertServiceEvent()

    private void convertLocationHospitalUnit()
    {
        CodeableConcept hospitalUnitCoding = new CodeableConcept();
        Coding coding = new Coding();
        String s = "";
        s = XPath.selectText(HOSPITAL_UNIT + ATTR_CODE_SYSTEM, eICR);
        if (s == null)
        {
            coding.setSystem(NOT_FOUND);
        }
        else
        {
            coding.setSystem(s);
        }

        s = XPath.selectText(HOSPITAL_UNIT + ATTR_CODE, eICR);
        if (s == null)
        {
            coding.setCode(NOT_FOUND);
        }
        else
        {
            coding.setCode(s);
        }

        s = XPath.selectText(HOSPITAL_UNIT + ATTR_DISPLAY_NAME, eICR);
        if (s != null)
        {
            coding.setDisplay(s);
        }
        hospitalUnitCoding.addCoding(coding);
        this.encounter.setHospitalUnit(hospitalUnitCoding);
    }

    public void storeResources( )
    {
        Observation observation;
        Condition condition;
        DiagnosticReport report;
        Immunization immunization;
        MedicationAdministration medicationAdministration;
        
        FhirContext ctx = FhirContext.forDstu3( );
        String serverBase = "http://162.249.6.176:8080/baseDstu3";
        IGenericClient client = ctx.newRestfulGenericClient( serverBase );

        this.patient.setId( IdDt.newRandomUuid( ) );
        this.practitioner.setId( IdDt.newRandomUuid( ) );
        this.location.setId( IdDt.newRandomUuid( ) );
        this.encounter.setId( IdDt.newRandomUuid( ) );

        this.encounter.setPatient( new Reference( this.patient ) );
        this.encounter.addParticipant( ).setIndividual( new Reference( this.practitioner ) );

        Encounter.EncounterLocationComponent eLocation = new Encounter.EncounterLocationComponent();
        eLocation.setLocation(new Reference(this.location));
        this.encounter.addLocation(eLocation);

        Bundle bundle = new Bundle( );
        bundle.setType( Bundle.BundleType.TRANSACTION );

        bundle.addEntry( )
            .setFullUrl( this.patient.getId( ) )
            .setResource( this.patient )
            .getRequest( )
                .setUrl( "Patient?identifier=" + this.patient.getIdentifierFirstRep( ).getSystem( ) + "|" +
                         this.patient.getIdentifierFirstRep( ).getValue() )
                .setMethod(Bundle.HTTPVerb.PUT );

        bundle.addEntry( )
            .setFullUrl( this.practitioner.getId( ) )
            .setResource( this.practitioner )
            .getRequest( )
                .setUrl( "Practitioner?identifier=" + this.practitioner.getIdentifierFirstRep( ).getSystem( ) + "|" +
                         this.practitioner.getIdentifierFirstRep( ).getValue() )
                .setMethod(Bundle.HTTPVerb.PUT );

        bundle.addEntry( )
            .setFullUrl( this.encounter.getId( ) )
            .setResource( this.encounter )
            .getRequest( )
                .setUrl( "Encounter?identifier=" + this.encounter.getIdentifierFirstRep( ).getSystem( ) + "|" +
                         this.encounter.getIdentifierFirstRep( ).getValue() )
                .setMethod(Bundle.HTTPVerb.PUT );

        bundle.addEntry( )
            .setFullUrl( this.location.getId( ) )
            .setResource( this.location )
            .getRequest( )
                .setUrl( "Location?identifier=" + this.location.getIdentifierFirstRep( ).getSystem( ) + "|" +
                         this.location.getIdentifierFirstRep( ).getValue() )
                .setMethod(Bundle.HTTPVerb.PUT );
                
        Iterator<DiagnosticReport> repIterator = this.diagnosticReports.iterator( );
        while ( repIterator.hasNext( ) ) {
            report = repIterator.next( );
            bundle.addEntry( )
                .setFullUrl( report.getId( ) )
                .setResource( report )
                .getRequest( )
                    .setUrl( "DiagnosticReport?identifier=" + report.getIdentifierFirstRep( ).getSystem( ) + "|" +
                             report.getIdentifierFirstRep( ).getValue( ) )
                    .setMethod( Bundle.HTTPVerb.PUT );
        }

        Iterator<Observation> obsIterator = this.observations.iterator( );
        while ( obsIterator.hasNext( ) ) {
            observation = obsIterator.next( );
            bundle.addEntry( )
                .setFullUrl( observation.getId( ) )
                .setResource( observation )
                .getRequest( )
                    .setUrl( "Observation?identifier=" + observation.getIdentifierFirstRep( ).getSystem( ) + "|" +
                             observation.getIdentifierFirstRep( ).getValue( ) )
                    .setMethod( Bundle.HTTPVerb.PUT );
        }
        
        Iterator<Condition> condIterator = this.conditions.iterator( );
        while ( condIterator.hasNext( ) ) {
            condition = condIterator.next( );
            bundle.addEntry( )
                .setFullUrl( condition.getId( ) )
                .setResource( condition )
                .getRequest( )
                    .setUrl( "Condition?identifier=" + condition.getIdentifierFirstRep( ).getSystem( ) + "|" +
                             condition.getIdentifierFirstRep( ).getValue( ) )
                    .setMethod( Bundle.HTTPVerb.PUT );
        }
        
        Iterator<MedicationAdministration> medIterator = this.medicationAdministrations.iterator( );
        while ( medIterator.hasNext( ) ) {
            medicationAdministration = medIterator.next( );
            bundle.addEntry( )
                .setFullUrl( medicationAdministration.getId( ) )
                .setResource( medicationAdministration )
                .getRequest( )
                    .setUrl( "MedicationAdministration?identifier=" + medicationAdministration.getIdentifierFirstRep( ).getSystem( ) + "|" +
                             medicationAdministration.getIdentifierFirstRep( ).getValue( ) )
                    .setMethod( Bundle.HTTPVerb.PUT );
        }

        Iterator<Immunization> immunizationIterator = this.immunizations.iterator( );
        while ( immunizationIterator.hasNext( ) ) {
            immunization = immunizationIterator.next( );
            bundle.addEntry( )
                .setFullUrl( immunization.getId( ) )
                .setResource( immunization )
                .getRequest( )
                    .setUrl( "Immunization?identifier=" + immunization.getIdentifierFirstRep( ).getSystem( ) + "|" +
                             immunization.getIdentifierFirstRep( ).getValue( ) )
                    .setMethod( Bundle.HTTPVerb.PUT );
        }

        client.transaction().withBundle(bundle).execute();
    }

    private Document eICR;
    private List<DiagnosticReport> diagnosticReports;
    private XEncounter encounter;
    private List<Immunization> immunizations;
    private List<MedicationAdministration> medicationAdministrations;
    private List<Observation> observations;
    private List<Condition> conditions;
    private Practitioner practitioner;
    private Location location;
    private XPatient patient;

    private static final String ACT                      = "/*[local-name() = 'act']";
    private static final String ADDRESS                  = "/*[local-name() = 'addr']";
    private static final String ASSIGNED_AUTHOR          = "/*[local-name() = 'assignedAuthor']";
    private static final String ASSIGNED_ENTITY          = "/*[local-name() = 'assignedEntity']";
    private static final String ASSIGNED_PERSON          = "/*[local-name() = 'assignedPerson']";
    private static final String AUTHOR                   = "/*[local-name() = 'author']";
    private static final String BIRTHTIME                = "/*[local-name() = 'birthTime']";
    private static final String CITY                     = "/*[local-name() = 'city']";
    private static final String CODE                     = "/*[local-name() = 'code']";
    private static final String COMPONENT                = "/*[local-name() = 'component']";
    private static final String COMPONENT_OF             = "/*[local-name() = 'componentOf']";
    private static final String CONSUMABLE               = "/*[local-name() = 'consumable']";
    private static final String COUNTRY                  = "/*[local-name() = 'country']";
    private static final String DECEASED_INDICATOR       = "/*[local-name() = 'deceasedInd']";
    private static final String DECEASED_TIME            = "/*[local-name() = 'deceasedTime']";
    private static final String DIAGNOSIS                = "//*[local-name() = 'templateId'][@root = '2.16.840.1.113883.10.20.22.4.80']/../*[local-name() = 'entryRelationship']/*[local-name() = 'observation']/*[local-name() = 'value']";
    private static final String DIAGNOSIS_DATE           = "//*[local-name() = 'templateId'][@root = '2.16.840.1.113883.10.20.22.4.80']/../*[local-name() = 'entryRelationship']/*[local-name() = 'observation']/*[local-name() = 'effectiveTime']/*[local-name() = 'low']/@value";
    private static final String DOCUMENTATION_OF         = "/*[local-name() = 'documentationOf']";
    private static final String DOSE_QUANTITY            = "/*[local-name() = 'doseQuantity']";
    private static final String EFFECTIVE_TIME           = "/*[local-name() = 'effectiveTime']";
    private static final String ENCOMPASSING_ENCOUNTER   = "/*[local-name() = 'encompassingEncounter']";
    private static final String ENCOUNTER_ID             = "/*[local-name() = 'id']/@extension";
    private static final String ENTRY_RELATIONSHIP       = "/*[local-name() = 'entryRelationship']";
    private static final String ENTRY                    = "/*[local-name() = 'entry']";
    private static final String ETHNICITY                = "/*[local-name() = 'ethnicGroupCode']";
    private static final String FACILITY_TYPE            = "/*[local-name() = 'code'][@codeSystem='2.16.840.1.113883.1.11.17660']";
    private static final String FAMILY                   = "/*[local-name() = 'family']";
    private static final String GENDER                   = "/*[local-name() = 'administrativeGenderCode']";
    private static final String GIVEN                    = "/*[local-name() = 'given']";
    private static final String GUARDIAN                 = "/*[local-name() = 'guardian']";
    private static final String GUARDIAN_PERSON          = "/*[local-name() = 'guardianPerson']";
    private static final String HEALTH_CARE_FACILITY     = "/*[local-name() = 'healthCareFacility']";
    private static final String HIGH                     = "/*[local-name() = 'high']";
    private static final String HOSPITAL_UNIT            = "//*[local-name()='participantRole']//*[local-name()='code'][@codeSystem='2.16.840.1.113883.1.11.20275']";
    private static final String ID                       = "/*[local-name() = 'id']";
    private static final String INTERPRETATION_CODE      = "/*[local-name() = 'interpretationCode']";
    private static final String LANGUAGE_CODE            = "/*[local-name() = 'languageCode']";
    private static final String LANGUAGE_COMMUNICATION   = "/*[local-name() = 'languageCommunication']";
    private static final String LOCATION                 = "/*[local-name() = 'location']";
    private static final String LOW                      = "/*[local-name() = 'low']";
    private static final String MANUFACTURED_MATERIAL    = "/*[local-name() = 'manufacturedMaterial']";
    private static final String MANUFACTURED_PRODUCT     = "/*[local-name() = 'manufacturedProduct']";
    private static final String NAME                     = "/*[local-name() = 'name']";
    private static final String OBSERVATION              = "/*[local-name() = 'observation']";
    private static final String OBSERVATION_RANGE        = "/*[local-name() = 'observationRange']";
    private static final String OCCUPATION               = "//*[local-name() = 'templateId'][@root = '2.16.840.1.113883.10.20.22.4.38']/../*[local-name() = 'code'][@code = '364703007' and @codeSystem = '2.16.840.1.113883.6.96']/../*[local-name() = 'value']";
    private static final String ORGANIZER                = "/*[local-name() = 'organizer']";
    private static final String PARENT                   = "/..";
    private static final String PATIENT                  = "/*[local-name() = 'patient']";
    private static final String PATIENT_ID               = "/*[local-name() = 'id'][@root='2.16.840.1.113883.4.1']"; // patient social security number
    //private static final String PATIENT_ID               = "/*[local-name() = 'id'][@root='2.16.840.1.113883.4.1']/@extension"; // patient social security number
    private static final String PATIENT_ROLE             = "/*[local-name() = 'patientRole']";
    private static final String POSTAL_CODE              = "/*[local-name() = 'postalCode']";
    private static final String PREFERENCE_IND           = "/*[local-name() = 'preferenceInd']";
    private static final String PROVIDER_ID              = "/*[local-name() = 'id'][@root='2.16.840.1.113883.4.6']"; // national provider id
    //private static final String PROVIDER_ID              = "/*[local-name() = 'id'][@root='2.16.840.1.113883.4.6']/@extension"; // national provider id
    private static final String RACE                     = "/*[local-name() = 'raceCode']";
    private static final String RECORD_TARGET            = "/*[local-name() = 'recordTarget']";
    private static final String REFERENCE_RANGE          = "/*[local-name() = 'referenceRange']";
    private static final String RESPONSIBLE_PARTY        = "/*[local-name() = 'responsibleParty']";
    private static final String ROOT                     = "/*[local-name() = 'ClinicalDocument']";
    private static final String ROUTE_CODE               = "/*[local-name() = 'routeCode']";
    private static final String SECTION                  = "/*[local-name() = 'section']";
    private static final String SERVICE_EVENT            = "/*[local-name() = 'serviceEvent']";
    private static final String SERVICE_PROVIDER_ORG     = "/*[local-name() = 'serviceProviderOrganization']";
    private static final String STATE                    = "/*[local-name() = 'state']";
    private static final String STATUS_CODE              = "/*[local-name() = 'statusCode']";
    private static final String STREET_ADDRESS           = "/*[local-name() = 'streetAddressLine']";
    private static final String STRUCTURED_BODY          = "/*[local-name() = 'structuredBody']";
    private static final String SUBSTANCE_ADMINISTRATION = "/*[local-name() = 'substanceAdministration']";
    private static final String TELECOM                  = "/*[local-name() = 'telecom']";
    private static final String TEMPLATE_ID_ENCOUNTER    = "/*[local-name() = 'templateId'][@root = '2.16.840.1.113883.10.20.22.2.22.1']";
    private static final String TEMPLATE_ID_IMMUNIZATION = "/*[local-name() = 'templateId'][@root = '2.16.840.1.113883.10.20.22.2.2.1']";
    private static final String TEMPLATE_ID_MEDS_ADMIN   = "/*[local-name() = 'templateId'][@root = '2.16.840.1.113883.10.20.22.2.38']";
    private static final String TEMPLATE_ID_RESULTS      = "/*[local-name() = 'templateId'][@root = '2.16.840.1.113883.10.20.22.2.3.1']";
    private static final String TEMPLATE_ID_SYMPTOMS     = "/*[local-name() = 'templateId'][@root = '2.16.840.1.113883.10.20.22.2.5.1']";
    private static final String TEXT                     = "/*[local-name() = 'text']";
    private static final String TIME                     = "/*[local-name() = 'time']";
    private static final String VALUE                    = "/*[local-name() = 'value']";

    private static final String ATTR_CLASS_CODE          = "/@classCode";
    private static final String ATTR_CODE                = "/@code";
    private static final String ATTR_CODE_SYSTEM         = "/@codeSystem";
    private static final String ATTR_CODE_SYSTEM_NAME    = "/@codeSystemName";
    private static final String ATTR_CODE_SYSTEM_VERSION = "/@codeSystemVersion";
    private static final String ATTR_DISPLAY_NAME        = "/@displayName";
    private static final String ATTR_MOOD_CODE           = "/@moodCode";
    private static final String ATTR_EXTENSION           = "/@extension";
    private static final String ATTR_ROOT                = "/@root";
    private static final String ATTR_UNIT                = "/@unit";
    private static final String ATTR_VALUE               = "/@value";
    private static final String ATTR_VALUE_SET_VERSION   = "/@*[local-name() = 'valueSetVersion']";
    private static final String ATTR_TYPE                = "/@*[local-name() = 'type']";

    private static final String NOT_FOUND                = "NOT_FOUND_IN_CCDA";
}
