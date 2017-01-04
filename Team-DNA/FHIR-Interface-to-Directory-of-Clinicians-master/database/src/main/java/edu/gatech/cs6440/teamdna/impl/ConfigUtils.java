package edu.gatech.cs6440.teamdna.impl;

public class ConfigUtils {
	private static final String[] AFFIRMATIVE = {"Y", "YES", "TRUE"};
	
	public static final boolean toBoolean(String configValue){
		for (String affirmative : AFFIRMATIVE) {
			if(affirmative.equalsIgnoreCase(configValue)){
				return true;
			}
		}
		
		return false;
	}
}