package edu.gatech.mass;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class massDate {
	
	private int day;
	private int month;
	private int year;
	private String date;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Initializes instance of SmoothieCartDate with current date.
	 *
	 */
	public massDate() {
		date = df.format(Calendar.getInstance().getTime());
		day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		month = Calendar.getInstance().get(Calendar.MONTH);
		year = Calendar.getInstance().get(Calendar.YEAR);
	}
	
	/**
	 * Initializes instance of SmoothieCartDate with custom date.
	 *
	 */
	public massDate(String date) {
		try {
			if(date == null){
				new massDate();
			}else {
				Date dt = df.parse(date);
				Calendar cal = Calendar.getInstance();
				cal.setTime(dt);
				day = cal.get(Calendar.DAY_OF_MONTH);
				month = cal.get(Calendar.MONTH);
				year = cal.get(Calendar.YEAR);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Compares this date with another date.
	 * 
	 * @param otherDate
	 * 		Date to which this date will be compared 
	 * @return -1, 0, or 1 as this date is earlier than, equal to, or later 
	 * than otherDate.
	 */
	public int compareTo(massDate otherDate) {
		int thisDateValue = year*10000 + month*100 + day;
		int otherDateValue = otherDate.year*1000 + otherDate.month*100 + otherDate.year;
		
		if (thisDateValue < otherDateValue) {
			return -1;
		}
		else if (thisDateValue == otherDateValue) {
			return 0;
		}
		else {
			return 1;
		}
	}
	
	/**
	 * Gets the numbered day-of-the-month. The first day of the month has a 
	 * value of 1.
	 * 
	 * @return Day of the month
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * Gets the numbered month-of-the-year. The first month of the year has 
	 * a value of 0.
	 * 
	 * @return Month
	 */
	public int getMonth() {
		return month;
	}
	
	/**
	 * Gets the year.
	 * 
	 * @return Year
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * Gets a string representation of the date.
	 * 
	 * @return Date string
	 */
	public String toString() {
		return Integer.toString(month + 1) + "/" + Integer.toString(day) + "/" + 
	Integer.toString(year);
	}

	public String getDate(){
		return date;
	}
}