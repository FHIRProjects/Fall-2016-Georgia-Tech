package edu.gatech.cs6440.teamdna.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gatech.cs6440.teamdna.Database;

public class EmbeddedDatabase extends Database{
	private static final Logger log = Logger.getLogger(EmbeddedDatabase.class.getName());
	private static final EmbeddedDatabase SINGLETON = new EmbeddedDatabase();
	
	public static final String DB_EXISTS		= "01J01";
    public static final String DRIVER 			= "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String DBNAME 			= "doc-db";
    public static final String CONNECTION_URL	= "jdbc:derby:" + DBNAME;

    private static ThreadLocal<Connection> threadLocalConn;
    
    private EmbeddedDatabase(){
    	try {
			create();
			if(isNew()){
				init();
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
    }
    
    public static final Database getInstance(){
    	return SINGLETON;
    }
    
    @Override
	protected boolean create() {
		try {
			threadLocalConn = new ThreadLocal<Connection>();
			Class.forName(DRIVER);
			getConnection();
			return true;
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

    @Override
	protected boolean init() {
		try {
			Connection connection =  getConnection();
			for(String sql :  CreateSQL.CREATE_SQL){
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
			}
			return true;
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}
    
	@Override
	public Connection getConnection() {
		try {
			if(threadLocalConn.get() == null){
				createConnection();
			} else if(threadLocalConn.get().isClosed()){
				threadLocalConn.remove();
				createConnection();
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			throw new IllegalAccessError("Unable to connect to data store");
		}
		
		return threadLocalConn.get();
	}
	
	private void createConnection() throws SQLException{
		String connenctionURL = CONNECTION_URL;
		if(!"true".equals(System.getProperty("database.init"))){
			System.out.println(" *&* CREATING " + System.getProperty("database.init"));

			connenctionURL  += ";create=true";
			System.getProperties().put("database.init", "true");
		}
		Connection c = DriverManager.getConnection(connenctionURL);
		threadLocalConn.set(c);
	}
	
	private boolean isNew() throws SQLException{
		List<String> tableNames = getTableNames();
		
		return tableNames == null || tableNames.size() == 0;
	}
}
