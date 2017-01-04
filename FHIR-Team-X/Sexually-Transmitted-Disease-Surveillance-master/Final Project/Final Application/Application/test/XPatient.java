import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.primitive.StringDt;

@ResourceDef(name="Patient", profile="http://fhirteamx.org/StructureDefinition/xpatient")
public class XPatient extends Patient {
 
    private static final long serialVersionUID = 1L;
    
    @Child(name="race") 
    @Extension(url="http://hl7.org/fhir/StructureDefinition/us-core-race", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The patient's race")
    private CodingDt race;
    
    @Child(name="ethnicity") 
    @Extension(url="http://hl7.org/fhir/StructureDefinition/us-core-ethnicity", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The patient's ethnicity")
    private CodingDt ethnicity;
    
    @Child(name="occupation") 
    @Extension(url="http://hl7.org/fhir/StructureDefinition/us-core-occupation", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The patient's occupation")
    private StringDt occupation;
 
    /**
     * It is important to override the isEmpty() method, adding a check for any
     * newly added fields.
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ethnicity.isEmpty() && race.isEmpty() && occupation.isEmpty();
    }
     
    /********
     * Accessors and mutators follow
     *
     * IMPORTANT:
     * Each extension is required to have an getter/accessor and a stter/mutator.
     * You are highly recommended to create getters which create instances if they
     * do not already exist, since this is how the rest of the HAPI FHIR API works.
     ********/
    public CodingDt getRace() {
        if (race == null) {
            race = new CodingDt();
        }
        return race;
    }
    
    public void setRace(CodingDt inRace) {
        race = inRace;
    }
    
    public CodingDt getEthnicity() {
        if (ethnicity == null) {
            ethnicity = new CodingDt();
        }
        return ethnicity;
    }
    
    public void setEthnicity(CodingDt inEthnicity) {
        ethnicity = inEthnicity;
    }
    
    public StringDt getOccupation() {
        if (occupation == null) {
            occupation = new StringDt();
        }
        return occupation;
    }
    
    public void setOccupation(StringDt inOccupation) {
        occupation = inOccupation;
    }
}
