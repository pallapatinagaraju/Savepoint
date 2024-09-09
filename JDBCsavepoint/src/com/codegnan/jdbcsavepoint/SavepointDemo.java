package com.codegnan.jdbcsavepoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

public class SavepointDemo {
	static final String JDBC_URL = "jdbc:mysql://localhost:3306/adjava";
	static final String USERNAME = "root";
	static final String PASSWORD = "root";


	public static void main(String[] args) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;


		try {
			// establish connection
			connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);


			// create the statement object
			statement = connection.createStatement();
			
			connection.setAutoCommit(false);
			statement.executeUpdate("insert into politicians values('babu','Tdp')");
			statement.executeUpdate("insert into politicians values('kcr','Brs')");
			
			// create save point 
			Savepoint sp = connection.setSavepoint();
			
			statement.executeUpdate("insert into politicians values('siddu','Bjp')");
			
			//if transaction failure we use rollback
			System.out.println("oops..... wrong entry rolling back ");
			connection.rollback(sp); // rolling back to sp (savepoint object) so that first 2 records will be there (again new records will get added after that 2 records
			
			System.out.println("rollback successfull");
			
			connection.commit();
			System.out.println("transaction comitted");
			
			
		} catch (SQLException e) {
			e.printStackTrace();


		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}


			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}


		}
	}
}
