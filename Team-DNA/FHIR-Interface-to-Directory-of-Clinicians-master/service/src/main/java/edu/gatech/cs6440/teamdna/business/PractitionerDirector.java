package edu.gatech.cs6440.teamdna.business;

import org.apache.commons.lang3.StringUtils;

import edu.gatech.cs6440.teamdna.fhir.Address;
import edu.gatech.cs6440.teamdna.fhir.CodeableConcept;
import edu.gatech.cs6440.teamdna.fhir.Coding;
import edu.gatech.cs6440.teamdna.fhir.ContactPoint;
import edu.gatech.cs6440.teamdna.fhir.ContactPointSystemList;
import edu.gatech.cs6440.teamdna.fhir.HumanName;
import edu.gatech.cs6440.teamdna.fhir.Identifier;
import edu.gatech.cs6440.teamdna.fhir.IdentifierUseList;
import edu.gatech.cs6440.teamdna.fhir.Practitioner;
import edu.gatech.cs6440.teamdna.fhir.PractitionerPractitionerRole;
import edu.gatech.cs6440.teamdna.fhir.PractitionerQualification;
import edu.gatech.cs6440.teamdna.fhir.Uri;
import edu.gatech.cs6440.teamdna.fhir.builder.AddressBuilder;
import edu.gatech.cs6440.teamdna.fhir.builder.CodeableConceptBuilder;
import edu.gatech.cs6440.teamdna.fhir.builder.CodingBuilder;
import edu.gatech.cs6440.teamdna.fhir.builder.ContactPointBuilder;
import edu.gatech.cs6440.teamdna.fhir.builder.FhirUtils;
import edu.gatech.cs6440.teamdna.fhir.builder.HumanNameBuilder;
import edu.gatech.cs6440.teamdna.fhir.builder.IdentifierBuilder;
import edu.gatech.cs6440.teamdna.fhir.builder.PractitionerBuilder;
import edu.gatech.cs6440.teamdna.fhir.builder.PractitionerQualificationBuilder;
import edu.gatech.cs6440.teamdna.fhir.builder.PractitionerRoleBuilder;
import edu.gatech.cs6440.teamdna.fhir.builder.UriBuilder;
import edu.gatech.cs6440.teamdna.model.DoCPDDo;

public class PractitionerDirector {
	public static Practitioner build(DoCPDDo docPD) {
		HumanName name = new HumanNameBuilder()
				.withFirstName(docPD.getFirstName())
				.withLastName(docPD.getLastName())
				.build();

		Address address = buildAddress(docPD);

		ContactPoint telephone = (new ContactPointBuilder())
				.withSystem(ContactPointSystemList.PHONE)
				.withValue(docPD.getAddrPhone())
				.build();
		ContactPoint email = (new ContactPointBuilder())
				.withSystem(ContactPointSystemList.EMAIL)
				.withValue(docPD.getAddrEmail())
				.build();
		
		PractitionerQualification qualification = buildQualification(docPD);
		PractitionerPractitionerRole practitionerRole = buildRole(docPD);
		
		Practitioner practitioner = new PractitionerBuilder()
				.withAddress(address)
				.withTelecom(telephone)
				.withTelecom(email)
				.withBirthdate(docPD.getDateOfBirth())
				.withName(name)
				.withQualification(qualification)
				.withPractitionerRole(practitionerRole)
				.withId(null, docPD.getDocPdId())
				.build();

		return practitioner;
	}
	
	private static Address buildAddress(DoCPDDo docPD){
		StringBuilder addressText = new StringBuilder();
		addressText.append(docPD.getAddrLine1());
		if(StringUtils.isNotEmpty(docPD.getAddrLine2())){
			addressText.append(" ").append(docPD.getAddrLine2());
		}
		addressText.append(", ").append(docPD.getAddrCity())
				.append(", ").append(docPD.getAddrState())
				.append(" ").append(docPD.getAddrZipcode());
		
		Address address = new AddressBuilder()
				.withLine(docPD.getAddrLine1())
				.withLine(docPD.getAddrLine2())
				.withCity(docPD.getAddrCity())
				.withDistrict(docPD.getAddrCounty())
				.withState(docPD.getAddrState())
				.withPostalCode(docPD.getAddrZipcode())
				.withText(addressText.toString())
				.withCountry("USA")
				.build();
		
		return address;
	}
	
	private static PractitionerPractitionerRole buildRole(DoCPDDo docPD){
		CodeableConcept role = new CodeableConceptBuilder()
				.withText(docPD.getProfessionName())
				.build();
		PractitionerPractitionerRole practitionerRole = new PractitionerRoleBuilder()
				.withRole(role)
				.build();
		
		return practitionerRole;
	}
	
	private static PractitionerQualification buildQualification(DoCPDDo docPD){

		Coding licenseName = (new CodingBuilder())
				.withCode("LicenseName", docPD.getLicenseName())
//				.withSystem(value) TODO: support with the official system for the status code
				.build();
		Coding licenseName2 = (new CodingBuilder())
				.withCode("LicenseName2", docPD.getLicenseName2())
//				.withSystem(value) TODO: support with the official system for the status code
				.build();
		
		CodeableConcept license = new CodeableConceptBuilder()
				.withCoding(licenseName)
				.withCoding(licenseName2)
				.build();
		
		Uri uri = (new UriBuilder()).withValue("urn:oid:2.16.840.1.113883.4.642.1.30").build();
		Coding medLicCoding = new CodingBuilder().withCode(null, "MD").withSystem("http://hl7.org/fhir/v2/0203").build();
		CodeableConcept type = new CodeableConceptBuilder().withCoding(medLicCoding).build();
		Identifier licIdentifier = new IdentifierBuilder()
				.withUse(IdentifierUseList.OFFICIAL)
				.withSystem(uri)
				.withType(type)
				.withValue(docPD.getLicenseNo())
				.build();
		
		Coding licenseStatusCode = (new CodingBuilder()).withCode(null, "LicStatus").build();
		CodeableConcept licenseStatus = new CodeableConceptBuilder().withCoding(licenseStatusCode).build();
		Identifier status = new IdentifierBuilder()
				.withType(licenseStatus)	
				.withValue(docPD.getLicStatus())
				.withPeriod(FhirUtils.asFhirPeriod(docPD.getDateThisStatus(), null))
				.build();
		
		PractitionerQualification qualification = new PractitionerQualificationBuilder()
				.withCode(license)
				.withIssuer(null, null, "Utah Division of Occupational and Professional Licensing (DOPL)") //TODO: support with the official system for license
				.withPeriod(FhirUtils.asFhirPeriod(docPD.getIssueDate(), docPD.getExpirationDate()))
				.withIdentifier(licIdentifier)
				.withIdentifier(status)
				.build();
		
		return qualification;
	}
}
