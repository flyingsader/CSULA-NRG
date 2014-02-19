package functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import types.Device;

public class ResponsePackage {
	
	Statement stmt;
	
	public ResponsePackage(Statement stmt) {
		this.stmt = stmt;
	}
	
	// Power saver mode
	protected void powerSave(Device device) {
		
		int deviceUsage = device.getDeviceUsage() / 2;
		device.setDeviceUsage(deviceUsage);
		
		try {
			stmt.executeUpdate("UPDATE Devices SET DeviceUsage = " + deviceUsage + " WHERE DeviceID = " + device.getDeviceID());
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
