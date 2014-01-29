package functions;

import java.sql.Connection;
import java.util.List;
import types.Device;
import types.GridData;
import types.WeatherData;


public class DMF{
	
	String url;
	String username;
	String password;
	Connection connection;
	
	DMF(){
		String url = "jdbc:mysql://localhost:3306/cs245_final";
		String username = "root";
		String password = "963Qsb85c";
		Connection connection = null;
	}
	
	private int currentGridDeficit;
	
	public List<WeatherData> getAllWeatherData(){
		//Retrieves all weather data edit test
		
		String stmt = "Select * from WeatherData";
		return null;
	}
	
	public List<GridData> getAllGridData(){
		//Retrieves all grid data
		
		String stmt = "Select * from GridData";
		return null;
	}
	
	public List<Device> getAllDevices(){
		//Retrieves all devices
		
		String stmt = "Select * from DeviceData";
		return null;
	}
	
	//public List<DeviceData>(String deviceKey){}
	
	public int modifyDevice(Device device){
		//Modifies a device entry

		return 0;
	}
	
	public int insertWeatherData(WeatherData data){
		return 0;
		//Adds a weather data object to the DB
	}
	
	public int insertGridData(GridData data){
		//Adds a grid data object to the DB
		return 0;
	}
	
	public int insertDevice(Device device){
		//Adds a device object to the DB
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