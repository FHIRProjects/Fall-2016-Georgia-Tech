package edu.gatech.cs6440.teamdna.impl;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gatech.cs6440.teamdna.Database;

public class DatabaseFactory {
	private static final Logger log = Logger.getLogger(DatabaseFactory.class.getName());
	private static final Properties CONFIG = new Properties(System.getProperties());
	private static Database database;
	
	static{
		try {
			if(ClassLoader.getSystemResource("database.properties") != null){
				CONFIG.load(ClassLoader.getSystemResourceAsStream("database.properties"));
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public static final Database getDatabase(){
		if(database != null){
			return database; 
		}
		
		if(ConfigUtils.toBoolean(CONFIG.getProperty("database.useembedded"))){
			database = EmbeddedDatabase.getInstance();
		} else {
			database = JNDIDatabase.getInstance();
		}
		return database;
	}
}
