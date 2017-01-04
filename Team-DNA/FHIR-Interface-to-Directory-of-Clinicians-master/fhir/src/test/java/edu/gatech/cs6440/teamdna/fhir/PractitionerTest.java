package edu.gatech.cs6440.teamdna.fhir;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

//import org.eclipse.persistence.jaxb.MarshallerProperties;
//import org.junit.Ignore;
import org.junit.Test;

public class PractitionerTest extends JaxbTest{

//	@Test
//	@Ignore
//	public void testJson() {
//		Practitioner practitioner = buildExamplePractioner();
//
//		java.lang.String json = null;
//		try {
//			json = marshallJSON(practitioner);
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//		System.out.println(json);
//	}
//	
	@Test
	public void testXml() {
		Practitioner practitioner = buildExamplePractioner();
		
		java.lang.String json = null;
		try {
			json = marshallXML(practitioner);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		System.out.println(json);
	}
	
	private Practitioner buildExamplePractioner(){
		Practitioner practitioner = new Practitioner();
		Id id = new Id();
		id.setValue("280");
		practitioner.setId(id);
		practitioner.setBirthDate(toFhirDate("09/09/1909"));
		practitioner.setGender(toFhirCode("M"));
		practitioner.setLanguage(toFhirCode("ENG"));
		practitioner.setName(toHumanName("Dr.", "Ashley", "Farley", "Sr."));
//		practitioner.setText(value);
		practitioner.getAddresses().add(toFhirAddress("112 S 1540 E", "", "Provo", "UT", "84762", AddressTypeList.PHYSICAL));
		
		PractitionerQualification qualification = new PractitionerQualification();

		qualification.setCode(toCodeableConcept("Associate Marriage & Family Therapist", "87854-2780", "Marriage & Family Therapy", "Associate Marriage & Family Therapist"));
		practitioner.getQualifications().add(qualification);
		
		return practitioner;
	}
	
//	private java.lang.String marshallJSON(Practitioner practitioner) throws JAXBException{
//		// Create a JaxBContext
//		JAXBContext jc = JAXBContext.newInstance(Practitioner.class);
//
//		// Create the Marshaller Object using the JaxB Context
//		Marshaller marshaller = jc.createMarshaller();
//		
//		// Set the Marshaller media type to JSON or XML
//		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE,"application/json");
//		
//		// Set it to true if you need to include the JSON root element in the JSON output
//		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
//		
//		// Set it to true if you need the JSON output to formatted
//		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		
//		// Marshal the employee object to JSON and print the output to console
//		StringWriter stringWriter = new StringWriter();
//		marshaller.marshal(practitioner, stringWriter);	
//		
//		return stringWriter.toString();
//	}

	private java.lang.String marshallXML(Practitioner practitioner) throws JAXBException{
		// Create a JaxBContext
		JAXBContext jc = JAXBContext.newInstance(Practitioner.class);

		// Create the Marshaller Object using the JaxB Context
		Marshaller marshaller = jc.createMarshaller();
				
		// Set it to true if you need the JSON output to formatted
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		// Marshal the employee object to JSON and print the output to console
		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(practitioner, stringWriter);	
		
		return stringWriter.toString();
	}
}
