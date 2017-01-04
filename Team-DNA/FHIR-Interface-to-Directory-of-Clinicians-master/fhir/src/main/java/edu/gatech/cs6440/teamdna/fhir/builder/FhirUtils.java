package edu.gatech.cs6440.teamdna.fhir.builder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.time.format.DateTimeParseException;
 
import edu.gatech.cs6440.teamdna.fhir.Boolean;
import edu.gatech.cs6440.teamdna.fhir.Code;
import edu.gatech.cs6440.teamdna.fhir.Date;
import edu.gatech.cs6440.teamdna.fhir.DateTime;
import edu.gatech.cs6440.teamdna.fhir.Period;
import edu.gatech.cs6440.teamdna.fhir.Reference;
import edu.gatech.cs6440.teamdna.fhir.String;


public class FhirUtils {
	private static final java.lang.String PATTERN = "MM/dd/yy";
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(PATTERN);
	private static final java.util.List<java.lang.String> AFFIRMATIVES = Arrays.asList(new java.lang.String[]{"Y", "YES", "TRUE"});
	public static final Date toFhirDate(java.util.Date date){
		return toFhirDate(formatDate(date));
	}

	public static final Date toFhirDate(java.lang.String dateString){
		Date fhirDate = new Date();
		fhirDate.setValue(dateString);
		
		return fhirDate;
	}
	
	public static final DateTime toFhirDateTime(LocalDate dateTime){
		return toFhirDateTime(formatDateTime(dateTime));
	}
	
	public static final DateTime toFhirDateTime(java.lang.String dateTimeString){
		DateTime dateTime = new DateTime();
		dateTime.setValue(dateTimeString);
		
		return dateTime;
	}
	
	public static final String toFhirString(java.lang.String value){
		String fhirString = new String();
		fhirString.setValue(value);
		
		return fhirString;
	}
	
	public static final Code toFhirCode(java.lang.String code){
		Code fhirCode = new Code();
		fhirCode.setValue(code);
		
		return fhirCode;
	}
	
	public static final Boolean toFhirBoolean(java.lang.String booleanString){
		return toFhirBoolean(AFFIRMATIVES.contains(booleanString));
	}
	
	public static final Boolean toFhirBoolean(java.lang.Boolean value){
		Boolean fhirBoolean = new Boolean();
		fhirBoolean.setValue(value);
		
		return fhirBoolean;
	}
	
	public static final Period asFhirPeriod(java.lang.String id, LocalDate start, LocalDate end){
		Period fhirPeriod = new Period();
		try {
			if(id != null){
				fhirPeriod.setId(id);
			}
			if(start != null){
				fhirPeriod.setStart(FhirUtils.toFhirDateTime(start));
			}
			if(end != null){
				fhirPeriod.setEnd(FhirUtils.toFhirDateTime(end));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return fhirPeriod;
	}
	
	public static final Period asFhirPeriod(java.lang.String id, java.lang.String start, java.lang.String end){
		LocalDate	 startDateTime = null;
		if(start != null){
			try{
				startDateTime = LocalDate.parse(start, DateTimeFormatter.ofPattern("M/d/yyyy"));
				}
			catch(DateTimeParseException e){
				System.out.println(e);
			}
			
			}
		
		LocalDate endDateTime = null;	
		if(end != null){
			endDateTime = LocalDate.parse(end, DateTimeFormatter.ofPattern("M/d/yyyy"));
		}

		return asFhirPeriod(id, startDateTime, endDateTime);
	}
	
	public static final Period asFhirPeriod(LocalDate start, LocalDate end){
		return asFhirPeriod(null, start, end);
	}

	public static final Period asFhirPeriod(java.lang.String start, java.lang.String end){
		return asFhirPeriod(null, start, end);
	}
	
	public static final Reference asFhirReference(java.lang.String id, java.lang.String reference, java.lang.String display){
		Reference fhirReference = new Reference();
		fhirReference.setId(id);
		fhirReference.setDisplay(toFhirString(display));
		fhirReference.setReference(toFhirString(reference));
		
		return fhirReference;
	}
	
	private static final java.lang.String formatDate(java.util.Date date) {
		return DATE_FORMATTER.format(date);
	}
	
	private static final java.lang.String formatDateTime(LocalDate date) {
		if(date == null){
			return null;
		}
		return date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	}
}
