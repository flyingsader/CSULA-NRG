package functions;

import java.sql.SQLException;
import java.sql.Statement;

import types.Device;

public class ResponsePackage {
	
	Statement stmt;
	
	public ResponsePackage(Statement stmt) {
		this.stmt = stmt;
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
