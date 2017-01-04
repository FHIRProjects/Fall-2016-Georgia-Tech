package edu.gatech.cs6440.teamdna.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDAO {

	protected void close(ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void close(Statement stmnt){
		if(stmnt != null){
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void close(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void close(ResultSet rs, Statement stmnt){
		close(rs);
		close(stmnt);
	}
	
	protected void close(Statement stmnt, Connection conn){
		close(stmnt);
		close(conn);
	}
	
	protected void close(ResultSet rs, Statement stmnt, Connection conn){
		close(rs);
		close(stmnt);
		close(conn);
	}
}
