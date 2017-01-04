package edu.gatech.cs6440.teamdna.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.gatech.cs6440.teamdna.Database;

public class JNDIDatabase extends Database {
	private static final Logger log = Logger.getLogger(JNDIDatabase.class.getName());

	private static final Database SINGLETON = new JNDIDatabase();
	
	private Context context;
	
	private static final String JNDI_DATASOURCE_NAME = "jdbc/DoC";
	
	public JNDIDatabase() {
		create();
	}
	
	public static final Database getInstance(){
		return SINGLETON;
	}
	
	@Override
	protected boolean create() {
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return true;
	}

	@Override
	protected boolean init() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection() {
		Connection conn = null;
		 try {
			DataSource ds = (DataSource)context.lookup(JNDI_DATASOURCE_NAME);
			conn = ds.getConnection();
		} catch (NamingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} 
		 
		 return conn;
	}
}
