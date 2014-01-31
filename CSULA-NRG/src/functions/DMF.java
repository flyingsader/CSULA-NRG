package functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import types.Device;
import types.GridData;
import types.WeatherData;


public class DMF{
	
	String url;
	String username;
	String password;
	Connection connection;
	
	DMF() throws SQLException{
		String url = "jdbc:mysql://localhost:3306/cs245_final";
		String username = "root";
		String password = "963Qsb85c";
		Connection connection = null;
		connection = DriverManager.getConnection(url, username, password);
	}
	
	private int currentGridDeficit;
	
	public List<WeatherData> getAllWeatherData() throws SQLException{
		//Retrieves all weather data edit test
		
		String tmp = "Select * from WeatherData";
		Statement stmt = connection.createStatement();
		stmt.executeQuery(tmp);

		
		return null;
	}
	
	public List<GridData> getAllGridData(){
		//Retrieves all grid data
		
		String tmp = "Select * from GridData";
		return null;
	}
	
	public List<Device> getAllDevices(){
		//Retrieves all devices
		
		String tmp = "Select * from DeviceData";
		return null;
	}
	
	//public List<DeviceData>(String deviceKey){}
	
	public int modifyDevice(Device device){
		//Modifies a device entry

		String tmp = "Delete from Devices where DeviceID = " + device.getDeviceID();
		String tmp2 = "Insert into Devices values(" +
						device.getDeviceID() + ", " + 
						device.getDeviceDesc() + ", " + 
						device.getDeviceOwner() + ", " + 
						device.getDeviceUsage() + ", " + 
						device.getPriority() + ");";
		
		return 0;
	}
	
	public int insertWeatherData(WeatherData data){
		
		String tmp = "Insert into WeatherData values(" +
				data.getLocality() + ", " + 
				data.getTimestamp() + ", " + 
				data.getTemperature() + ", " + 
				data.getWindspeed() + ", " + 
				data.getWindDir() + ", " + 
				data.getSolarRad() + ");";
		
		return 0;
		//Adds a weather data object to the DB
	}
	
	public int insertGridData(GridData data){
		//Adds a grid data object to the DB
		
		String tmp = "Insert into GridData values(" +
				data.getLocality() + ", " + 
				data.getTimestamp() + ", " + 
				data.getCapacity() + ", " + 
				data.getDemand() + ");";
		
		return 0;
	}
	
	public int insertDevice(Device device){
		//Adds a device object to the DB
		
		String tmp = "Insert into Devices values(" +
				device.getDeviceID() + ", " + 
				device.getDeviceDesc() + ", " + 
				device.getDeviceOwner() + ", " + 
				device.getDeviceUsage() + ", " + 
				device.getPriority() + ");";
		
		return 0;
	}
	
//	public int insertDeviceData(DeviceData data){
//		//Adds a device data object to the DB
//		return 0;
//	}
	
	public int purgeOldWeatherData(int time){
		//deletes weather data entries prior to the given time value
		return 0;
	}
	
	public int purgeOldGridData(int time){
		//deletes grid data entries prior to the given time value
		return 0;
	}
	
	public int purgeOldDeviceData(int time){
		//deletes device data entries prior to the given time value
		return 0;
	}
	
	public int getDeficit(){
		return currentGridDeficit;
	}
	
	public void setDeficit(int deficit){
		currentGridDeficit = deficit;
	}
}