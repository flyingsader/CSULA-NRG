public class DMF{
	
	private int currentGridDeficit;
	
	public List<WeatherData> getAllWeatherData(){
		//Retrieves all weather data
	}
	
	public List<GridData> getAllGridData(){
		//Retrieves all grid data
	}
	
	public List<Device> getAllDevices(){
		//Retrieves all devices
	}
	
	//public List<DeviceData>(String deviceKey){}
	
	public int modifyDevice(Device device){
		//Modifies a device entry
	}
	
	public int insertWeatherData(WeatherData data){
		//Adds a weather data object to the DB
	}
	
	public int insertGridData(GridData data){
		//Adds a grid data object to the DB
	}
	
	public int insertDevice(Device device){
		//Adds a device object to the DB
	}
	
	public int insertDeviceData(DeviceData data){
		//Adds a device data object to the DB
	}
	
	public int purgeOldWeatherData(int time){
		//deletes weather data entries prior to the given time value
	}
	
	public int purgeOldGridData(int time){
		//deletes grid data entries prior to the given time value
	}
	
	public int purgeOldDeviceData(int time){
		//deletes device data entries prior to the given time value
	}
	
	public int getDeficit(){
		return currentGridDeficit;
	}
	
	public void setDeficit(int deficit){
		currentGridDeficit = deficit;
	}
}