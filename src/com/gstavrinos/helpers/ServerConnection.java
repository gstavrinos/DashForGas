package com.gstavrinos.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ServerConnection {

	private String serverIP = "83.212.83.74";
	public Connection sqlConnection = null;
	private String port = "3306";
	private String database = "DASH_FOR_GAS";
	public Statement statement = null;
	public Statement statement2 = null;
	public ResultSet resultSet = null;
	private String username = "root";
	private String password = "password";	
	
	
	public ServerConnection(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					sqlConnection = DriverManager.getConnection("jdbc:mysql://"+serverIP + ":" + port +"/" + database, username, password);
					statement = sqlConnection.createStatement();
					statement2 = sqlConnection.createStatement();
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	//close database connection
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
		    }
			    if (statement != null) {
		    	statement.close();
		    }
		    
		    if (sqlConnection != null) {
		    	sqlConnection.close();
		    }
		} 
		catch (Exception e) {}
	}
}
