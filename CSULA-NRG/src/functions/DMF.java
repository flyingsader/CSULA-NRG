package functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import types.Device;
import types.GridData;
import types.WeatherData;


public class DMF{
	
	String url;
	String username;
	String password;
	Connection connection;
	
	private int currentGridDeficit;
	
	DMF() throws SQLException{
		String url = "jdbc:mysql://localhost:3306/NRG";
		String username = "root";
		String password = "4042951";
		connection = DriverManager.getConnection(url, username, password);
	}
	
	public List<WeatherData> getAllWeatherData() throws SQLException{
		//Retrieves all weather data edit test
		
		String tmp = "Select * from WeatherData";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(tmp);
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();		
		
		List<WeatherData> resultData = new ArrayList<WeatherData>();
		rs.next();
		while(!rs.isAfterLast()){
			
			resultData.add(new WeatherData(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)));
			rs.next();
		}

		return resultData;
	}
	
	public List<GridData> getAllGridData() throws SQLException{
		//Retrieves all grid data
		
		String tmp = "Select * from GridData";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(tmp);
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();		
		
		List<GridData> resultData = new ArrayList<GridData>();
		rs.next();
		while(!rs.isAfterLast()){
			
			resultData.add(new GridData(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
			rs.next();
		}

		return resultData;
	}
	
	public List<Device> getAllDevices() throws SQLException{
		//Retrieves all devices
		
		String tmp = "Select * from Devices";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(tmp);
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();		
		
		List<Device> resultData = new ArrayList<Device>();
		rs.next();
		while(!rs.isAfterLast()){
			
			resultData.add(new Device(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5)));
			rs.next();
		}

		return resultData;
	}
	
	//public List<DeviceData>(String deviceKey){}
	
	public int modifyDevice(Device device) throws SQLException{
		//Modifies a device entry

		String tmp = "Delete from Devices where DeviceID = " + device.getDeviceID();
		String tmp2 = "Insert into Devices values(" +
						device.getDeviceID() + ", " + 
						device.getDeviceDesc() + ", " + 
						device.getDeviceOwner() + ", " + 
						device.getDeviceUsage() + ", " + 
						device.getPriority() + ");";
		
		System.out.println(tmp);
		Statement stmt;
		stmt = connection.createStatement();
		stmt.execute(tmp);
		stmt.execute(tmp2);
		
		return 0;
	}
	
	public int insertWeatherData(WeatherData data) throws SQLException{
		
		String tmp = "Insert into WeatherData values(" +
				data.getLocality() + ", " + 
				data.getTimestamp() + ", " + 
				data.getTemperature() + ", " + 
				data.getWindspeed() + ", " + 
				data.getWindDir() + ", " + 
				data.getSolarRad() + ");";
		
		System.out.println(tmp);
		Statement stmt;
		stmt = connection.createStatement();
		stmt.execute(tmp);
		
		return 0;
		//Adds a weather data object to the DB
	}
	
	public int insertGridData(GridData data) throws SQLException{
		//Adds a grid data object to the DB
		
		String tmp = "Insert into GridData values(\"" +
				data.getLocality() + "\", \"" + 
				data.getTimestamp() + "\", " + 
				data.getCapacity() + ", " + 
				data.getDemand() + ");";
		
		System.out.println(tmp);
		Statement stmt;
		stmt = connection.createStatement();
		stmt.execute(tmp);

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
	
	public static void main(String[] args){
		System.out.println("test");
		
		
		try {
			DMF dmf = new DMF();
			
			GridData gdata = new GridData(10, 6);

			//dmf.insertGridData(gdata);
			
			
			List<WeatherData> testList = dmf.getAllWeatherData();
			
			for(WeatherData data: testList){
				System.out.println(data.toSqlEntry());
			}
			
			List<GridData> test2List = dmf.getAllGridData();
			
			for(GridData data: test2List){
				System.out.println(data.toSqlEntry());
			}
			
			List<Device> test3List = dmf.getAllDevices();
			
			for(Device data: test3List){
				System.out.println(data.toSqlEntry());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}