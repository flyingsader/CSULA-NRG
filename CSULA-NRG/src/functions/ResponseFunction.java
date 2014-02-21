package functions;

import java.sql.*;
import java.util.*;
import types.*;

public class ResponseFunction {
	
	private Statement stmt;
	private List<Device> devices; 
	private int currentGridDeficit;
	
	public ResponseFunction(Statement stmt, List<Device> devices, int currentGridDeficit) {
		this.stmt = stmt;
		this.devices = devices;
		this.currentGridDeficit = currentGridDeficit;
	}
	
	// Sort devices by priority level 0 to 9
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
	protected List<Device> powerConsumption(List<Device> devices) {
		
		List<Device> adjustedDevices = devices;
		
		int totalDeviceUsage = 0;
		
		// Get total device usage of all devices
		for (int i = 0; i < devices.size(); i++) {
			totalDeviceUsage += devices.get(i).getDeviceUsage();
		}
		
		// If the current grid deficit requires all devices to shutdown
		if (currentGridDeficit == totalDeviceUsage) {
			
			try {
				
				for (int i = 0; i < adjustedDevices.size(); i++) {
					
					adjustedDevices.get(i).setDeviceUsage(0);
					
					// Update device usage in database
					stmt.executeUpdate("UPDATE Devices SET DeviceUsage = 0 WHERE DeviceID = " + adjustedDevices.get(i).getDeviceID());
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			return adjustedDevices;
		}
		
		// Get total number of priority levels with at least one device
		int totalPriorities = totalPriority(adjustedDevices);
		
		// Calculate the device usage percentage for each priority level
		int[] distribution = distribution(totalPriorities);
		
		// All devices group by priority level
		List<List<Device>> devicesByPriority = devicesByPriority(devices);
		
		try {

			int remainingCurrentGridDeficit = currentGridDeficit;
			
			// Get the number of devices in each priority level
			int[] totalPrioritiesDevices = numOfDevicesWithPriority(adjustedDevices);
			int k = totalPrioritiesDevices.length - 1;
			int numOfDevices = totalPrioritiesDevices[k];
//			int count = 0;
			
			
			for (int i = 0/*, j = 0*/; i < distribution.length; i++) {
				
				// Deficit to remove for the current priority level
				double deficitToRemove = (double) currentGridDeficit * (double) distribution[i] * 0.01;

				if (k >= 0) {
					numOfDevices = totalPrioritiesDevices[k];
				}
				else {
					break;
				}
				
				// Skip priority levels that has no devices
				while (numOfDevices == 0) {
					k--;
					numOfDevices = totalPrioritiesDevices[k];
				}
				
				int sumOfDeviceUsage = 0;
				
				// Get the sum of all devices' usage for the particular priority level
				for (int l = devicesByPriority.get(k).size() - 1; l >= 0; l--) {
					sumOfDeviceUsage += devicesByPriority.get(k).get(l).getDeviceUsage();
				}
				
				// Decrease the deficit by decreasing the devices' usages to a minimum of 10 to keep all devices on
				if (sumOfDeviceUsage == (int) deficitToRemove) {
					
					// Skip devices with usages less than 10
					int totalDevicesGreaterThanTen = 0;
					
					for (int l = devicesByPriority.get(k).size() - 1; l >= 0; l--) {
						
						if (devicesByPriority.get(k).get(l).getDeviceUsage() >= 10) {
							devicesByPriority.get(k).get(l).setDeviceUsage(10);
							totalDevicesGreaterThanTen++;
						}
						else {
							deficitToRemove -= devicesByPriority.get(k).get(l).getDeviceUsage();
						}
					}
					
					remainingCurrentGridDeficit -= (int) deficitToRemove - (totalDevicesGreaterThanTen * 10);
				}
				else if (sumOfDeviceUsage < (int) deficitToRemove) {
					
					// Skip devices with usages less than 10
					int totalDevicesGreaterThanTen = 0;
					
					for (int l = devicesByPriority.get(k).size() - 1; l >= 0; l--) {
						
						if (devicesByPriority.get(k).get(l).getDeviceUsage() >= 10) {
							devicesByPriority.get(k).get(l).setDeviceUsage(10);
							totalDevicesGreaterThanTen++;
						}
						else {
							sumOfDeviceUsage -= devicesByPriority.get(k).get(l).getDeviceUsage();
						}
					}
					
					remainingCurrentGridDeficit -= sumOfDeviceUsage - (totalDevicesGreaterThanTen * 10);
				}
				else {
					
					for (int l = devicesByPriority.get(k).size() - 1; l >= 0; l--) {
						
						if (devicesByPriority.get(k).get(l).getDeviceUsage() == (int) deficitToRemove) {
							devicesByPriority.get(k).get(l).setDeviceUsage(10);
							remainingCurrentGridDeficit -= (int) deficitToRemove - 10;
							break;
						}
						else if (devicesByPriority.get(k).get(l).getDeviceUsage() < (int) deficitToRemove) {
							
							// Only decrease device usage from devices with device usage greater than or equal to 10
							if (devicesByPriority.get(k).get(l).getDeviceUsage() >= 10) {
								remainingCurrentGridDeficit -= devicesByPriority.get(k).get(l).getDeviceUsage() - 10;
								deficitToRemove -= devicesByPriority.get(k).get(l).getDeviceUsage();
								devicesByPriority.get(k).get(l).setDeviceUsage(10);
							}
							
						}
						else {
							devicesByPriority.get(k).get(l).setDeviceUsage(devicesByPriority.get(k).get(l).getDeviceUsage() - (int) deficitToRemove);
							remainingCurrentGridDeficit -= (int) deficitToRemove;
							break;
						}
					}
				}
				
				k--;
				
//				count++;
//				if (count == numOfDevices) {
//					//j++;
//					count = 0;
//					k++;
//					if (k < totalPrioritiesDevices.length) {
//						numOfDevices = totalPrioritiesDevices[k];
//					}
//					else {
//						break;
//					}
//				}
			}
			
			// If there is still remaining current grid deficit, decrease the deficit. Some devices will have to shutdown (device's usage is 0)
			if (remainingCurrentGridDeficit > 0) {
				
				for (int i = devicesByPriority.size() - 1; i >= 0; i--) {
					
					if (remainingCurrentGridDeficit == 0) {
						break;
					}
					
					int j = devicesByPriority.get(i).size() - 1;
					if (!devicesByPriority.get(i).isEmpty()) {
						
						while (j >= 0) {
							
							if (devicesByPriority.get(i).get(j).getDeviceUsage() > 0) {
								
								// Decrease the deficit
								if (devicesByPriority.get(i).get(j).getDeviceUsage() >= remainingCurrentGridDeficit) {
									devicesByPriority.get(i).get(j).setDeviceUsage(devicesByPriority.get(i).get(j).getDeviceUsage() - remainingCurrentGridDeficit);
									remainingCurrentGridDeficit = 0;
								}
								else {
									remainingCurrentGridDeficit -= devicesByPriority.get(i).get(j).getDeviceUsage();
									devicesByPriority.get(i).get(j).setDeviceUsage(0);
								}
							}
							
							j--;
						}
					}
				}
			}
			
			// Add the newly updated devices into a new array list
			List<Device> modifiedDevices = new ArrayList<Device>();
			for (int i = 0; i < devicesByPriority.size(); i++) {
				
				int j = 0;
				if (!devicesByPriority.get(i).isEmpty()) {
					
					while (j < devicesByPriority.get(i).size()) {
						
						modifiedDevices.add(devicesByPriority.get(i).get(j));
						j++;
					}
				}
			}
			
			// Update device usage in database
			for (int i = 0; i < modifiedDevices.size(); i++) {
				
				int deviceUsage = modifiedDevices.get(i).getDeviceUsage();
				
				stmt.executeUpdate("UPDATE Devices SET DeviceUsage = " + deviceUsage + " WHERE DeviceID = " + modifiedDevices.get(i).getDeviceID());
			}
			
			// Replace the old devices' usage with the new values from the new array list
			adjustedDevices = modifiedDevices;
			
//			for (int i = 0, j = 0; i < adjustedDevices.size(); i++) {
//				
//				// Skip priority levels that has no devices
//				while (numOfDevices == 0) {
//					k++;
//					numOfDevices = totalPrioritiesDevices[k];
//				}
//				
//				// Calculate the device usage and distribute evenly if there are more than one device with the same priority level.
//				double deficitToRemove = (double) currentGridDeficit * (double) distribution[j] * 0.01;
//				
//				double deficitDifference = adjustedDevices.get(i).getDeviceUsage() - deficitToRemove;
//				
//				// If a device used less than the deficitToRemove
//				if (deficitDifference < 0) {
//					remainingCurrentGridDeficit -= adjustedDevices.get(i).getDeviceUsage();
//				}
				
				
				
				
//				double deviceUsage = watts * (double) distribution[j] * 0.01;
//				deviceUsage /= (double) numOfDevices;
//				adjustedDevices.get(i).setDeviceUsage((int) deviceUsage);
//
//				// Update device usage in database
//				stmt.executeUpdate("UPDATE Devices SET DeviceUsage = " + (int)  deviceUsage + " WHERE DeviceID = " + adjustedDevices.get(i).getDeviceID());
//				
//				count++;
//				if (count == numOfDevices) {
//					j++;
//					count = 0;
//					k++;
//					if (k < totalPrioritiesDevices.length) {
//						numOfDevices = totalPrioritiesDevices[k];
//					}
//					else {
//						break;
//					}
//				}
//			}
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
		
		//System.out.println("Priorities total: " + totalPriorities);
		
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
	
	// All devices group by priority level
	protected List<List<Device>> devicesByPriority(List<Device> devices) {
		
		List<List<Device>> devicesByPriority = new ArrayList<List<Device>>();
		
		// Create totalPriorities of array lists
//		for (int i = 0; i < totalPriorities; i++) {
//			List<Device> device = new ArrayList<Device>();
//			devicesByPriority.add(device);
//		}
		
		for (int i = 0; i < 10; i++) {
			List<Device> device = new ArrayList<Device>();
			devicesByPriority.add(device);
		}
		
		// Add devices to the array list with priority level labeled
		for (int i = 0, j = 0; i < devices.size();) {
			if (devices.get(i).getPriority() == j) {
//				List<Device> device = new ArrayList<Device>();
//				device.add(devices.get(i));
//				devicesByPriority.add(j, device);
				devicesByPriority.get(j).add(devices.get(i));
				i++;
			}
			else if (devices.get(i).getPriority() > j) {
				j++;
			}
		}
		
		return devicesByPriority;
	}
	
	// Receive response packages from devices
	protected ResponsePackage responsePackage() {
		
		ResponsePackage rp = new ResponsePackage(stmt);
		
		return rp;
	}
	
	// Communication with Control Interface Function
//	public void sentToCF() {
//		CF cf = new CF();
//		cf.receiveResponsePackage(responsePackage());
//	}
	
}
