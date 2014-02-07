package functions;

import java.sql.*;
import java.util.*;

import types.*;

public class ResponseFunction {
	
	private Statement stmt;
	private List<Device> devices; 
	@SuppressWarnings("unused")
	private List<GridData> gridData;
	
	public ResponseFunction(Statement stmt, List<Device> devices, List<GridData> gridData) {
		this.stmt = stmt;
		this.devices = devices;
		this.gridData = gridData;
	}
	
	protected List<Device> importanceSort() {
		
		List<Device> sortDevices = new ArrayList<Device>();
		
		try {
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM Devices ORDER BY Priority");
			
			boolean resultHasNext = rs.next();
			while (resultHasNext) {
				
				int deviceID = rs.getInt("DeviceID");
				
				for (int i = 0; i < devices.size(); i++) {
					if (devices.get(i).getDeviceID() == deviceID) {
						sortDevices.add(devices.get(i));
						break;
					}
				}
				
				resultHasNext = rs.next();
			}
			
			return sortDevices;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// Get an arbitrary device usage max. Rank devices by priority. Modify and share the device usage across all devices to meet max device usage.
	protected List<Device> powerConsumption(double watts, List<Device> devices) {
		
		List<Device> adjustedDevices = devices;
		
		// Get total number of priority levels with at least one device
		int totalPriorities = totalPriority(adjustedDevices);
		
		// Calculate the device usage percentage for each priority level
		int[] distribution = distribution(totalPriorities);

		try {

			int k = 0;
			int[] totalPrioritiesDevices = numOfDevicesWithPriority(adjustedDevices);
			int numOfDevices = totalPrioritiesDevices[k];
			int count = 0;
			for (int i = 0, j = 0; i < adjustedDevices.size(); i++) {
				
				// Skip priority levels that has no devices
				while (numOfDevices == 0) {
					k++;
					numOfDevices = totalPrioritiesDevices[k];
				}
				
				// Calculate the device usage and distribute evenly if there are more than one device with the same priority level.
				double deviceUsage = watts * (double) distribution[j] * 0.01;
				deviceUsage /= (double) numOfDevices;
				adjustedDevices.get(i).setDeviceUsage((int) deviceUsage);

				// Update device usage in database
				stmt.executeUpdate("UPDATE Devices SET DeviceUsage = " + (int) deviceUsage + " WHERE DeviceID = " + adjustedDevices.get(i).getDeviceID());
				
				count++;
				if (count == numOfDevices) {
					j++;
					count = 0;
					k++;
					if (k < totalPrioritiesDevices.length) {
						numOfDevices = totalPrioritiesDevices[k];
					}
					else {
						break;
					}
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return adjustedDevices;
	}
	
	// Get the number of devices for each priority
	protected int[] numOfDevicesWithPriority(List<Device> devices) {
		
		int[] numOfDevicesWithPriority = new int[10];
		for (int i = 0, j = 0; i < devices.size();) {
			if (devices.get(i).getPriority() == j) {
				numOfDevicesWithPriority[j]++;
				i++;
			}
			else {
				j++;
			}
		}
		
		return numOfDevicesWithPriority;
	}
	
	// Get the number of priorities
	protected int totalPriority(List<Device> devices) {
		
		int totalPriorities = 0;
		int priority = 0;

		for (int i = 0; i < devices.size(); i++) {
			if (devices.get(i).getPriority() == priority) {
				totalPriorities++;
				priority++;
			}
			else if (devices.get(i).getPriority() > priority) {
				priority = devices.get(i).getPriority() + 1;
				totalPriorities++;
			}
		}
		
		System.out.println("Priorities total: " + totalPriorities);
		
		return totalPriorities;
	}
	
	// Distribute power percent usage appropriately
	protected int[] distribution(int totalPriorities) {
		
		// Arbitrary initial device usage
		

		List<List<Integer>> adjustmentList = new ArrayList<List<Integer>>();
		for (int i = 0; i < 10; i++) {
			List<Integer> list = new ArrayList<Integer>();
			adjustmentList.add(list);
		}
		
		adjustmentList.get(0).add(100);
		
		adjustmentList.get(1).add(40);
		adjustmentList.get(1).add(60);
		
		adjustmentList.get(2).add(10);
		adjustmentList.get(2).add(30);
		adjustmentList.get(2).add(60);
		
		adjustmentList.get(3).add(10);
		adjustmentList.get(3).add(20);
		adjustmentList.get(3).add(30);
		adjustmentList.get(3).add(40);
		
		adjustmentList.get(4).add(9);
		adjustmentList.get(4).add(10);
		adjustmentList.get(4).add(18);
		adjustmentList.get(4).add(27);
		adjustmentList.get(4).add(36);
		
		adjustmentList.get(5).add(6);
		adjustmentList.get(5).add(10);
		adjustmentList.get(5).add(12);
		adjustmentList.get(5).add(18);
		adjustmentList.get(5).add(24);
		adjustmentList.get(5).add(30);
		
		adjustmentList.get(6).add(4);
		adjustmentList.get(6).add(8);
		adjustmentList.get(6).add(12);
		adjustmentList.get(6).add(16);
		adjustmentList.get(6).add(16);
		adjustmentList.get(6).add(20);
		adjustmentList.get(6).add(24);
		
		adjustmentList.get(7).add(3);
		adjustmentList.get(7).add(6);
		adjustmentList.get(7).add(9);
		adjustmentList.get(7).add(12);
		adjustmentList.get(7).add(15);
		adjustmentList.get(7).add(16);
		adjustmentList.get(7).add(18);
		adjustmentList.get(7).add(21);
		
		adjustmentList.get(8).add(2);
		adjustmentList.get(8).add(5);
		adjustmentList.get(8).add(7);
		adjustmentList.get(8).add(10);
		adjustmentList.get(8).add(12);
		adjustmentList.get(8).add(12);
		adjustmentList.get(8).add(15);
		adjustmentList.get(8).add(17);
		adjustmentList.get(8).add(20);
		
		adjustmentList.get(9).add(2);
		adjustmentList.get(9).add(4);
		adjustmentList.get(9).add(6);
		adjustmentList.get(9).add(8);
		adjustmentList.get(9).add(10);
		adjustmentList.get(9).add(10);
		adjustmentList.get(9).add(12);
		adjustmentList.get(9).add(14);
		adjustmentList.get(9).add(16);
		adjustmentList.get(9).add(18);
		
		double adjustment = adjustmentList.get(totalPriorities - 1).get(0);
		int[] distribution = new int[totalPriorities];
		
		int j = 1;
		for (int i = distribution.length - 1; i >= 0; i--) {
			
			distribution[i] = (int) adjustment;
			
			if (j >= distribution.length) break;
			
			adjustment = adjustmentList.get(totalPriorities - 1).get(j);
			
			j++;
		}
		
		return distribution;
	}
	
	// Dummy method to send wireless signal
	protected void sentWirelessSignal() {
		
	}
	
	// Turn on devices
	protected void turnOn(Device device, int deviceUsage) {
		device.setDeviceUsage(deviceUsage);
		try {
			stmt.executeUpdate("UPDATE Devices SET DeviceUsage = " + deviceUsage + " WHERE DeviceID = " + device.getDeviceID());
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Turn off devices
	protected void turnOff(Device device) {
		device.setDeviceUsage(0);
		try {
			stmt.executeUpdate("UPDATE Devices SET DeviceUsage = 0 WHERE DeviceID = " + device.getDeviceID());
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
