package functions;

import java.io.*;
import java.sql.*;
import java.util.*;

import types.*;

import misc.DummyDataMaker;

public class NRGResponse {

	public static void main(String[] args) {

		try {
			
			DMF dmf = new DMF();

			Statement stmt = dmf.connection.createStatement();
		        
		    File file = new File("NRGv2.sql");
		    FileReader fr = new FileReader(file);
		    BufferedReader br = new BufferedReader(fr);
		        
		    // Create tables
		    String query = "";
		    int countLine = 0;
		    while(query != null && countLine < 5) {
		    	
		    	query += br.readLine();
		        if (query != null && query != "" && !query.contains("null") && !query.isEmpty() && !query.startsWith(" ")) {
		        		
		        	if (query.charAt(query.length() - 1) == ';') {
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
		        
		    // Insert data to the tables
		    DummyDataMaker ddm = new DummyDataMaker();
		        
		    for (int i = 0; i < ddm.getDevices().size(); i++) {
		    	dmf.insertDevice(ddm.getDevices().get(i));
		    }
		        
		    for (int i = 0; i < ddm.getGridData().size(); i++) {
		    	dmf.insertGridData(ddm.getGridData().get(i));
		    }
		        
		    for (int i = 0; i < ddm.getWeatherData().size(); i++) {
		    	dmf.insertWeatherData(ddm.getWeatherData().get(i));
		    }
		    
		    // Generate a random deficit
		    int currentDeficit = randomCurrentDeficit(ddm.getDevices());
		    
		    ResponseFunction rf = new ResponseFunction(stmt, dmf.getAllDevices(), dmf.getAllDevices(), currentDeficit, dmf);
		        
		    // Rank the devices in order of importance (0: Most important, 9: Least important)
		    List<Device> sortedDevices = rf.importanceSort(dmf.getAllDevices());
		    
		    DevicesTable dt = new DevicesTable(stmt, sortedDevices, sortedDevices.size(), rf.totalPriority(sortedDevices), totalDeviceUsage(dmf.getAllDevices()), 0);

		    System.out.println("Total Device Usage: " + totalDeviceUsage(dmf.getAllDevices()));
		    
		    while(!dt.isClicked()) {
		        	
		    }
		        
		    System.out.println("Current Deficit: " + currentDeficit);
		    
		    // Adjust the devices' power usage appropriately
		    
		    List<Device> modifiedDevices = rf.powerConsumption(sortedDevices);
		    System.out.println("Total Device Usage After: " + totalDeviceUsage(modifiedDevices));
		    
		    dt = new DevicesTable(stmt, modifiedDevices, rf.responsePackage().wattsToPercent(), sortedDevices.size(), currentDeficit, rf.totalPriority(sortedDevices));
		        
			stmt.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	static int totalDeviceUsage(List<Device> devices) {
		
		int totalDeviceUsage = 0;
		
		for (int i = 0; i < devices.size(); i++) {
			totalDeviceUsage += devices.get(i).getDeviceUsage();
		}
		
		return totalDeviceUsage;
	}
	
	static int randomCurrentDeficit(List<Device> devices) {
		
		Random random = new Random();
		int currentDeficit = random.nextInt(totalDeviceUsage(devices) + 1);
		
		return currentDeficit;
	}
}
