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
	
	DMF(){
		String url = "jdbc:mysql://localhost:3306/NRG";
		String username = "root";
		String password = "qwerty";
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<WeatherData> getAllWeatherData() throws SQLException{
		//Retrieves all weather data edit test
		
		String tmp = "Select * from WeatherData";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(tmp);
		ResultSetMetaData rsmd = rs.getMetaData();		
		
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
	
	public int insertDevice(Device device) throws SQLException{
		//Adds a device object to the DB
		
		String tmp = "Insert into Devices values(" +
				device.getDeviceID() + ", " + 
				device.getDeviceDesc() + ", " + 
				device.getDeviceOwner() + ", " + 
				device.getDeviceUsage() + ", " + 
				device.getPriority() + ");";
		
		System.out.println(tmp);
		Statement stmt;
		stmt = connection.createStatement();
		stmt.execute(tmp);
		
		return 0;
	}
	
//	public int insertDeviceData(DeviceData data){
//		//Adds a device data object to the DB
//		return 0;
//	}
	
	public int purgeOldWeatherData(long time) throws SQLException{
		//deletes weather data entries prior to the given time value
		
		String tmp = "Delete from WeatherData where WeatherTimeStamp < '" + milliToTimeStamp(time) + "';";
		Statement stmt;
		stmt = connection.createStatement();
		stmt.execute(tmp);
		
		return 0;
	}
	
	public int purgeOldGridData(long time) throws SQLException{
		//deletes grid data entries prior to the given time value
		String tmp = "Delete from WeatherData where GridTimeStamp < '" + milliToTimeStamp(time) + "';";
		Statement stmt;
		stmt = connection.createStatement();
		stmt.execute(tmp);
		
		return 0;
	}
	
	public int purgeOldDeviceData(long time) throws SQLException{
		//deletes device data entries prior to the given time value
		String tmp = "Delete from WeatherData where DeviceTimeStamp < '" + milliToTimeStamp(time) + "';";
		Statement stmt;
		stmt = connection.createStatement();
		stmt.execute(tmp);
		
		return 0;
	}
	
	public int getDeficit(){
		return currentGridDeficit;
	}
	
	public void setDeficit(int deficit){
		currentGridDeficit = deficit;
	}
	
	public static void main(String[] args){
		
		System.out.println(milliToTimeStamp(System.currentTimeMillis()));
		
//		try {
//			DMF dmf = new DMF();
//			
//			GridData gdata = new GridData(10, 6);
//
//			//dmf.insertGridData(gdata);
//			
//			
//			List<WeatherData> testList = dmf.getAllWeatherData();
//			
//			for(WeatherData data: testList){
//				System.out.println(data.toSqlEntry());
//			}
//			
//			List<GridData> test2List = dmf.getAllGridData();
//			
//			for(GridData data: test2List){
//				System.out.println(data.toSqlEntry());
//			}
//			
//			List<Device> test3List = dmf.getAllDevices();
//			
//			for(Device data: test3List){
//				System.out.println(data.toSqlEntry());
//			}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static String milliToTimeStamp(long mils){
		String time;
		
		long tmp = mils - (3600 * 1000 * 8);
		int sec, min, hour, day, month, year;
		
		sec = (int)(tmp / 1000 % 60);
		tmp = (tmp / 60000);
		
		min = (int)(tmp % 60);
		tmp = tmp / 60;
		
		hour = (int)(tmp % 24);
		tmp = tmp / 24;
		
		day = (int)(tmp % 365);
		year = (int)tmp / 365;
		
		day = day - (year - 2) / 4;
		
		int[] monthLength = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		
		if(year % 4 == 2)
			monthLength[1] = 29;
		
		month = 1;
		
		while(day > monthLength[month - 1]){
				day -= monthLength[month - 1];
			month++;
		}
		
		year += 1970;
		
		
		time = "  year: " + year + "  month: " + month + "  day: " + day + "  hour: " + hour + "  min: " + min + "  sec: " + sec;
		time = year + "-";
		
		if(month < 10)
			time += "0";
		time += month + "-";
		
		if(day < 10)
			time += "0";
		time += day + " ";
		
		if(hour < 10)
			time += "0";
		time += hour + ":";
		
		if(min < 10)
			time += "0";
		time += min + ":";
		
		if(sec < 10)
			time += "0";
		time += sec + "";
		
		return time;
	}
}
