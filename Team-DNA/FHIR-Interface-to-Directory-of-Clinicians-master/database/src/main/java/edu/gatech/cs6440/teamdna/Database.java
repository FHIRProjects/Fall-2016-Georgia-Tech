package edu.gatech.cs6440.teamdna;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class Database {
	public static final String[] TYPES = new String[] { "TABLE" };
	public static final String METADATA_TABLE_CAT 		= "TABLE_CAT"; //String => table catalog (may be null)
	public static final String METADATA_TABLE_SCHEM		= "TABLE_SCHEM"; // String => table schema (may be null)
	public static final String METADATA_TABLE_NAME		= "TABLE_NAME"; // String => table name
	public static final String METADATA_TABLE_TYPE		= "TABLE_TYPE"; // String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	public static final String METADATA_REMARKS			= "REMARKS"; // String => explanatory comment on the table
	public static final String METADATA_TYPE_CAT		= "TYPE_CAT"; // String => the types catalog (may be null)
	public static final String METADATA_TYPE_SCHEM		= "TYPE_SCHEM"; // String => the types schema (may be null)
	public static final String METADATA_TYPE_NAME		= "TYPE_NAME"; // String => type name (may be null)
	public static final String METADATA_SELF_REFERENCING_COL_NAME= "SELF_REFERENCING_COL_NAME"; // String => name of the designated "identifier" column of a typed table (may be null)
	public static final String METADATA_REF_GENERATION	= "REF_GENERATION"; // String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)

	public abstract Connection getConnection();
	protected abstract boolean create();
	protected abstract boolean init();

	protected List<String> getTableNames(){
		List<String> tableNames = new ArrayList<String>();

		try {
			DatabaseMetaData meta = getConnection().getMetaData();
			ResultSet rs = meta.getTables(null, null, null, TYPES);
			while (rs.next()) {
				tableNames.add(rs.getString(METADATA_TABLE_NAME));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tableNames;
	}
	
	protected void dropAllTables(){
		Connection conn = getConnection();
		boolean complete = false;
		while (!complete) {
			List<String> tableNames = getTableNames();
			if(tableNames == null || tableNames.size() == 0){
				complete = true;
			}
			for (String tableName : tableNames) {
				Statement stmnt = null;
				try {
					stmnt = conn.createStatement();
					stmnt.execute("DROP TABLE " + tableName);
				} catch (SQLException e) {
					;//ignore
				} finally {
					if(stmnt != null){
						try {
							stmnt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
				}
			}
		}
	}
	
	public void reset() {
		dropAllTables();
		init();
	}
}
