package functions;

import java.io.*;
import java.sql.*;
import java.util.*;

import types.*;

import misc.DummyDataMaker;

public class NRGResponse {

	public static void main(String[] args) {

		try {
			
			// CSULA Connection
			String url = "jdbc:mysql://localhost:3306/";
			String username = "root";
			String password = "";
			
			Connection connection = null;
			
			System.out.println("Connecting database...");
			    /*This is the connection url for MySQL database. 
			     *Each driver has a different syntax for the url.
			     *In our case, we provide a host, a port and a database name.
			     **/ 
			    connection = DriverManager.getConnection(url, username, password);
			    System.out.println("Database connected!");
				/*
				 * The createStatement() method of the connection object creates 
				 * a Statement object for sending SQL statements to the database
				 * The createStatement() method of the connection object executes the given SQL statement, 
				 * which returns a single ResultSet object. 
				 * */
		        Statement stmt = connection.createStatement();
		        
		        FileReader fr = new FileReader("E:/CS 437/workspace/Response Function/NRGv2.sql");
		        //FileReader fr = new FileReader("F:/CS 437/workspace/Response Function/NRGv2.sql");
		        BufferedReader br = new BufferedReader(fr);
		        
		        // Create tables
		        String query = "";
		        int countLine = 0;
		        while(query != null && countLine < 5) {
		        	query += br.readLine();
		        	if (query != null && query != "" && !query.contains("null") && !query.isEmpty() && !query.startsWith(" ")) {
		        		
		        		if (query.charAt(query.length() - 1) == ';') {
			        		//System.out.println("Query: " + query);
			        		stmt.execute(query);
			        		query = "";
			        		countLine = 0;
			        	}
		        	}
		        	else {
		        		countLine++;
		        	}
		        	
		        }
		        
		        br.close();
		        fr.close();
		        
		        //System.out.println("Done");
		        
		        // Insert data to the tables
		        DummyDataMaker ddm = new DummyDataMaker();
		        
		        for (int i = 0; i < ddm.getDevices().size(); i++) {
		        	stmt.executeUpdate(ddm.getDevices().get(i).toSqlEntry());
		        }
		        
		        for (int i = 0; i < ddm.getGridData().size(); i++) {
		        	stmt.executeUpdate(ddm.getGridData().get(i).toSqlEntry());
		        }
		        
		        for (int i = 0; i < ddm.getWeatherData().size(); i++) {
		        	stmt.executeUpdate(ddm.getWeatherData().get(i).toSqlEntry());
		        }
		        
		        ResponseFunction rf = new ResponseFunction(stmt, ddm.getDevices(), ddm.getGridData());
		        
		        // Rank the devices in order of importance (0: Most important, 9: Least important)
		        List<Device> sortedDevices = rf.importanceSort();
		        
		        System.out.println("First results:");
		        try {
					
		        	// Test printing table results
					ResultSet rs = stmt.executeQuery("SELECT * FROM Devices ORDER BY Priority");
					boolean resultHasNext = rs.next();
					while (resultHasNext) {
						int deviceID = rs.getInt("DeviceID");
						String deviceDesc = rs.getString("DeviceDESC");
						String deviceOwner = rs.getString("DeviceOwner");
						int deviceUsage = rs.getInt("DeviceUsage");
						int priority = rs.getInt("Priority");
						System.out.println("DeviceID: " + deviceID + " DeviceDesc: " + deviceDesc + " DeviceOwner: " + deviceOwner + " DeviceUsage: " + deviceUsage + " Priority: " + priority);
						resultHasNext = rs.next();
					}
				}
		        
		        catch (SQLException e) {
					e.printStackTrace();
				}
		        
		        // Generate a randomly how much power is currently available
		        Random random = new Random();
		        double watts = (double) random.nextInt(500);
		        
		        DevicesTable dt = new DevicesTable(stmt, sortedDevices.size(), rf.totalPriority(sortedDevices));
		        
		        while(!dt.isClicked()) {
		        	
		        }
		        
		        System.out.println("Watts: " + watts);
		        // Adjust the devices' power usage appropriately
		        rf.powerConsumption(watts, sortedDevices);
		        
		        System.out.println("Second results:");
		        try {
					
		        	// Test printing table results
					ResultSet rs = stmt.executeQuery("SELECT * FROM Devices ORDER BY Priority");
					boolean resultHasNext = rs.next();
					while (resultHasNext) {
						int deviceID = rs.getInt("DeviceID");
						String deviceDesc = rs.getString("DeviceDESC");
						String deviceOwner = rs.getString("DeviceOwner");
						int deviceUsage = rs.getInt("DeviceUsage");
						int priority = rs.getInt("Priority");
						System.out.println("DeviceID: " + deviceID + " DeviceDesc: " + deviceDesc + " DeviceOwner: " + deviceOwner + " DeviceUsage: " + deviceUsage + " Priority: " + priority);
						resultHasNext = rs.next();
					}
				}
				
				catch (SQLException e) {
					e.printStackTrace();
				}
		        
		        dt = new DevicesTable(stmt, sortedDevices.size(), watts, rf.totalPriority(sortedDevices));
		        
				connection.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
