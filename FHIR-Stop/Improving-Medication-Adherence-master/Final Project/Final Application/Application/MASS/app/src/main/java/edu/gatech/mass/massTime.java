package edu.gatech.mass;

import java.sql.Time;

public class massTime {
	
	private int hours;		// hours in military time
	private int minutes;
	private String timeStr;	// string representation of time
	
	/**
	 * Initializes instance of massTime.
	 * 
	 * @param time
	 * 		String representation of time in military time ("7:30" or "19:24")
	 */
	public massTime(String time) {
		timeStr = time;
		hours = Integer.parseInt(time.substring(0,timeStr.length()-3));
		minutes = Integer.parseInt(time.substring(timeStr.length()-2));
	}
	
	/**
	 * Initializes instance of massTime.
	 * 
	 * @param time
	 * 		String representation of time in military time ("7:30" or "19:24")
	 */
	public massTime(long time) {
		Time t = new Time(time);
		timeStr = t.toString().substring(0, 5);
		hours = Integer.parseInt(timeStr.substring(0,timeStr.length()-3));
		minutes = Integer.parseInt(timeStr.substring(timeStr.length()-2));
	}
	
	/**
	 * Compares this time with another time.
	 * 
	 * @param otherTime
	 * 		Date to which this time will be compared 
	 * @return -1, 0, or 1 as this time is earlier than, equal to, or later 
	 * than otherTime.
	 */
	public int compareTo(massTime otherTime) {
		int thisTimeValue = hours*60 + minutes;
		int otherTimeValue = otherTime.getHours()*60 + otherTime.getMinutes();
		
		if (thisTimeValue < otherTimeValue) {
			return -1;
		}
		else if (thisTimeValue == otherTimeValue) {
			return 0;
		}
		else {
			return 1;
		}
	}
	
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public String toString() {
		return timeStr;
	}
}