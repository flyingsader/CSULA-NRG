package functions;

import java.sql.*;
import java.util.*;

import types.*;

public class ResponseFunction {
	
	private List<Device> originalDevices;
	private int currentGridDeficit;
	private DMF dmf;
	
	public ResponseFunction(List<Device> originalDevices, int currentGridDeficit, DMF dmf) {
		this.originalDevices = importanceSort(originalDevices);
		this.currentGridDeficit = currentGridDeficit;
		this.dmf = dmf;
	}
	
	// Sort devices by priority level 0 to 9
	protected List<Device> importanceSort(List<Device> devices) {
		
		List<Device> sortDevices = new ArrayList<Device>();
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < devices.size(); j++) {
				if (devices.get(j).getPriority() == i) {
					sortDevices.add(devices.get(j));
				}
			}
		}
		
		return sortDevices;
	}
	
	// When updating devices, remove and add the devices back to the database
	protected void keepOrder(List<Device> devices) {
		
		for (int i = 0; i < devices.size(); i++) {
			try {
				dmf.modifyDevice(devices.get(i));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Rank devices by priority. Modify devices' usage so that the current grid deficit becomes 0
	protected List<Device> powerConsumption(List<Device> devices) {
		
		List<Device> adjustedDevices = devices;
		
		int totalDeviceUsage = 0;
		
		// Get total device usage of all devices
		for (int i = 0; i < devices.size(); i++) {
			totalDeviceUsage += devices.get(i).getDeviceUsage();
		}
		
		// If the current grid deficit requires all devices to shutdown
		if (currentGridDeficit == totalDeviceUsage) {
				
			for (int i = 0; i < adjustedDevices.size(); i++) {
					
				adjustedDevices.get(i).setDeviceUsage(0);
					
				// Update device usage in database
				keepOrder(adjustedDevices);
			}
			
			return adjustedDevices;
		}
		
		// Get total number of priority levels with at least one device
		int totalPriorities = totalPriority(adjustedDevices);
		
		// Calculate the current grid deficit percentage for each priority level
		int[] distribution = distribution(totalPriorities);
		
		// All devices group by priority level
		List<List<Device>> devicesByPriority = devicesByPriority(devices);
		
		try {

			int remainingCurrentGridDeficit = currentGridDeficit;
			
			// Get the number of devices in each priority level
			int[] totalPrioritiesDevices = numOfDevicesWithPriority(adjustedDevices);
			int k = totalPrioritiesDevices.length - 1;
			int numOfDevices = totalPrioritiesDevices[k];
			
			for (int i = 0; i < distribution.length; i++) {
				
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
				dmf.modifyDevice(modifiedDevices.get(i));
			}
			
			// Replace the old devices' usage with the new values from the new array list
			adjustedDevices = modifiedDevices;	
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
		
		// Create 10 array lists and add them to the devicesByPriority list
		for (int i = 0; i < 10; i++) {
			List<Device> device = new ArrayList<Device>();
			devicesByPriority.add(device);
		}
		
		// Add devices to the array list with priority level labeled
		for (int i = 0, j = 0; i < devices.size();) {
			if (devices.get(i).getPriority() == j) {
				devicesByPriority.get(j).add(devices.get(i));
				i++;
			}
			else if (devices.get(i).getPriority() > j) {
				j++;
			}
		}
		
		return devicesByPriority;
	}
	
	// Adjust ranking of a device based on weather data
	protected List<Device> adjustRanking(List<Device> devices) {
		
		// Retrieve weather data and adjust each device's ranking
		for (int i = 0; i < devices.size(); i++) {
			
			// Analyze weather data and set device priority
//			devices.get(i).setPriority(// Determine a value);
		}
		
		return devices;
	}
	
	// Receive response packages from devices
	protected ResponsePackage responsePackage(List<Device> devices, int[] devicesPercentages) {
		
		ResponsePackage rp = new ResponsePackage(devices, devicesPercentages);
		
		return rp;
	}
	
	// Send wireless signal
	protected void sentWirelessSignal(Device device) {
			
		int deviceUsage = device.getDeviceUsage();
			
		// Sent the device usage value to the device
	}
		
	// Receive instructions from the Control Interface Function
	protected void receiveInstruction(Instruction instructions) {
			
		// If an instruction required devices to turn on
		/*
		Get the list of devices from the Instruction object and all the values each device demands
		Put the values into a integer array of size = the total devices that needs to be turn on
		turnOn(devices, deviceUsage);
		*/
			
		// If an instruction required devices to turn off
		/*
		Get the list of devices from the Instruction object that needs to be turn off
		turnOff(devices);
			*/
	}
		
	// Turn on devices
	protected void turnOn(List<Device> devices, int[] deviceUsage) {
		for (int i = 0; i < devices.size(); i++) {
			devices.get(i).setDeviceUsage(deviceUsage[i]);
			try {
				dmf.modifyDevice(devices.get(i));
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
			
	// Turn off devices
	protected void turnOff(List<Device> devices) {
		for (int i = 0; i < devices.size(); i++) {
			devices.get(i).setDeviceUsage(0);
			try {
				dmf.modifyDevice(devices.get(i));
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		
	// Display devices' usage as percentage	
	protected int[] wattsToPercent(List<Device> devices) {
				
		int[] devicePercentage = new int[originalDevices.size()];
		double percent = 0.0;
		for (int i = 0; i < originalDevices.size(); i++) {
			percent = ((double) devices.get(i).getDeviceUsage() / (double) originalDevices.get(i).getDeviceUsage()) * 100.0;
			percent = Math.floor(percent);
			devicePercentage[i] = (int) percent;
		}
			
		return devicePercentage;
	}
}
