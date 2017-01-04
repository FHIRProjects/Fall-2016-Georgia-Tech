package edu.gatech.cs6440.teamdna.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConversionUtils {
	private static final Logger log = Logger.getLogger(ConversionUtils.class.getName());

	private static final String DATE_FORMAT = "yyyyMMdd";
	
	private static final  SimpleDateFormat SDF = new SimpleDateFormat(DATE_FORMAT);
	public static final Date toDate(String dateString){
		Date date = null;
		try {
			date = SDF.parse(dateString);
		} catch (ParseException e) {			
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return date;
	}
	
	public static final String toString(Date date){
		return SDF.format(date);
	}
}
