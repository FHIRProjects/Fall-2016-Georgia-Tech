package model;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.StringType;

import java.util.Date;

@ResourceDef(name="Encounter", profile="http://fhirteamx.org/StructureDefinition/XEncounter")
public class XEncounter extends Encounter {
 
    private static final long serialVersionUID = 1L;
    
    @Child(name="triggerCode") 
    @Extension(url="triggerCode", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The trigger code that initiated the electronic case report")
    private CodeableConcept triggerCode;
    
    @Child(name="reportDate") 
    @Extension(url="reportDate", definedLocally=false, isModifier=false)
    @Description(shortDefinition="Date of the report")
    private DateTimeType reportDate;
    
    @Child(name="reportSubmissionDate") 
    @Extension(url="reportSubmissionDate", definedLocally=false, isModifier=false)
    @Description(shortDefinition="Report submission date")
    private DateTimeType reportSubmissionDate;
    
    @Child(name="sendingApplication") 
    @Extension(url="sendingApplication", definedLocally=false, isModifier=false)
    @Description(shortDefinition="Sending aplication")
    private StringType sendingApplication;
    
    @Child(name="hospitalUnit") 
    @Extension(url="hospitalUnit", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The location's hospital unit")
    private CodeableConcept hospitalUnit;
 
    /**
     * It is important to override the isEmpty() method, adding a check for any
     * newly added fields.
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && triggerCode.isEmpty() && reportDate.isEmpty() && reportSubmissionDate.isEmpty() && sendingApplication.isEmpty() && hospitalUnit.isEmpty();
    }
     
    /********
     * Accessors and mutators follow
     *
     * IMPORTANT:
     * Each extension is required to have an getter/accessor and a stter/mutator.
     * You are highly recommended to create getters which create instances if they
     * do not already exist, since this is how the rest of the HAPI FHIR API works.
     ********/
    public CodeableConcept getTriggerCode() {
        if (triggerCode == null) {
            triggerCode = new CodeableConcept();
        }
        return triggerCode;
    }
    
    public void setTriggerCode(CodeableConcept inTriggerCode) {
        triggerCode = inTriggerCode;
    }
    
    public DateTimeType getReportDate() {
        if (reportDate == null) {
            reportDate = new DateTimeType();
        }
        return reportDate;
    }
    
    public void setReportDate(DateTimeType inReportDate) {
        reportDate = inReportDate;
    }
    
    public DateTimeType getReportSubmissionDate() {
        if (reportSubmissionDate == null) {
            reportSubmissionDate = new DateTimeType(new Date());
        }
        return reportSubmissionDate;
    }
    
    public void setReportSubmissionDate(DateTimeType inReportSubmissionDate) {
        reportSubmissionDate = inReportSubmissionDate;
    }
    
    public StringType getSendingApplication() {
        if (sendingApplication == null) {
            sendingApplication = new StringType();
        }
        return sendingApplication;
    }
    
    public void setSendingApplication(StringType inSendingApplication) {
        sendingApplication = inSendingApplication;
    }
    
    public CodeableConcept getHospitalUnit() {
        if (hospitalUnit == null) {
            hospitalUnit = new CodeableConcept();
        }
        return hospitalUnit;
    }
    
    public void setHospitalUnit(CodeableConcept inHospitalUnit) {
        hospitalUnit = inHospitalUnit;
    }
}