package model;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;

@ResourceDef(name="Patient", profile="http://fhirteamx.org/StructureDefinition/XPatient")
public class XPatient extends Patient {
 
    private static final long serialVersionUID = 1L;
    
    @Child(name="race") 
    @Extension(url="http://hl7.org/fhir/StructureDefinition/us-core-race", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The patient's race")
    private CodeableConcept race;
    
    @Child(name="ethnicity") 
    @Extension(url="http://hl7.org/fhir/StructureDefinition/us-core-ethnicity", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The patient's ethnicity")
    private CodeableConcept ethnicity;
    
    @Child(name="occupationString") 
    @Extension(url="occupationString", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The patient's occupation")
    private StringType occupationString;
    
    @Child(name="patientClass") 
    @Extension(url="patientClass", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The patient's class")
    private CodeableConcept patientClass;
 
    /**
     * It is important to override the isEmpty() method, adding a check for any
     * newly added fields.
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ethnicity.isEmpty() && race.isEmpty() && occupationString.isEmpty() && patientClass.isEmpty();
    }
     
    /********
     * Accessors and mutators follow
     *
     * IMPORTANT:
     * Each extension is required to have an getter/accessor and a stter/mutator.
     * You are highly recommended to create getters which create instances if they
     * do not already exist, since this is how the rest of the HAPI FHIR API works.
     ********/
    public CodeableConcept getRace() {
        if (race == null) {
            race = new CodeableConcept();
        }
        return race;
    }
    
    public void setRace(CodeableConcept inRace) {
        race = inRace;
    }
    
    public CodeableConcept getEthnicity() {
        if (ethnicity == null) {
            ethnicity = new CodeableConcept();
        }
        return ethnicity;
    }
    
    public void setEthnicity(CodeableConcept inEthnicity) {
        ethnicity = inEthnicity;
    }
    
    public StringType getOccupationString() {
        if (occupationString == null) {
            occupationString = new StringType();
        }
        return occupationString;
    }
    
    public void setOccupationString(StringType inOccupationString) {
        occupationString = inOccupationString;
    }
    
    public CodeableConcept getPatientClass() {
        if (patientClass == null) {
            patientClass = new CodeableConcept();
        }
        return patientClass;
    }
    
    public void setPatientClass(CodeableConcept inPatientClass) {
        patientClass = inPatientClass;
    }
}
