package edu.gatech.cs6440.teamdna.fhir;

public class JaxbTest {

	public Date toFhirDate(java.lang.String dateString){
		Date fhirDate = new Date();

		fhirDate.setValue(dateString);
		
		return fhirDate;
	}
	
	public String toFhirString(java.lang.String value){
		String fhirString = new String();
		fhirString.setValue(value);
		
		return fhirString;
	}
	
	public Code toFhirCode(java.lang.String code){
		Code fhirCode = new Code();
		fhirCode.setValue(code);
		
		return fhirCode;
	}
	
	public HumanName toHumanName(java.lang.String prefix, java.lang.String fname, java.lang.String lname, java.lang.String suffix){
		HumanName humanName = new HumanName();
		humanName.getGivens().add(toFhirString(fname));
		humanName.getFamilies().add(toFhirString(lname));
		humanName.getSuffixes().add(toFhirString(suffix));
		humanName.getPrefixes().add(toFhirString(prefix));
		
		return humanName;
	}
	
	public Address toFhirAddress(java.lang.String add1, java.lang.String add2, java.lang.String city, java.lang.String state, java.lang.String zip, AddressTypeList addressTypeList) {
		Address address = new Address();
		address.setCity(toFhirString(city));
		address.setPostalCode(toFhirString(zip));
		address.setState(toFhirString(state));
		AddressType addressType = new AddressType();
		addressType.setValue(addressTypeList);
		address.setType(addressType);
		
		return address;
	}
	
	public CodeableConcept toCodeableConcept(java.lang.String codeString, java.lang.String codeId, java.lang.String codeValue, java.lang.String codeDisplay){
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.setText(toFhirString(codeString));
		Coding coding = new Coding();
		Code code = new Code();
		code.setId(codeId);
		code.setValue(codeValue);
		coding.setCode(code);
		coding.setDisplay(toFhirString(codeDisplay));
		codeableConcept.getCodings().add(coding);
		return codeableConcept;
	}
}
