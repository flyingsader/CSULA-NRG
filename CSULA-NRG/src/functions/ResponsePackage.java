package functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import types.Device;

public class ResponsePackage {
	
	Statement stmt;
	List<Device> originalDevices;
	List<Device> devices;
	DMF dmf;
	
	public ResponsePackage(Statement stmt, List<Device> originalDevices, List<Device> devices, DMF dmf) {
		this.stmt = stmt;
		this.originalDevices = originalDevices;
		this.devices = devices;
		this.dmf = dmf;
	}
	
	// Power saver mode
	protected void powerSave(Device device) {
		
		int deviceUsage = device.getDeviceUsage() / 2;
		device.setDeviceUsage(deviceUsage);
		
		try {
			
			dmf.modifyDevice(device);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Register new device
	protected void registerDevice(Device device) {
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Devices WHERE DeviceID = " + device.getDeviceID());

			boolean resultHasNext = rs.next();
			if (resultHasNext) {
				
				int deviceID = rs.getInt("DeviceID");
				
				if (deviceID > 0) {
					System.out.println("Device is already registered in the database.");
				}
				else {
					stmt.executeUpdate(device.toSqlEntry());
				}
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Send wireless signal
	protected void sentWirelessSignal(Device device) {
		
		int deviceUsage = device.getDeviceUsage();
		
		// Sent the device usage value to the device
	}
		
	// Turn on devices
	protected void turnOn(Device device, int deviceUsage) {
		device.setDeviceUsage(deviceUsage);
		try {
			dmf.modifyDevice(device);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	// Turn off devices
	protected void turnOff(Device device) {
		device.setDeviceUsage(0);
		try {
			dmf.modifyDevice(device);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Display devices' usage as percentage
	protected int[] wattsToPercent() {
			
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
