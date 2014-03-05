package types;


public class WeatherData {

	String locality;
	String timestamp;
	int temperature;
	int windspeed;
	int windDir;
	int solarRad;


	public WeatherData(int day, int hour){
		this.locality = "Los Angeles";
		if(hour < 10){
			if(day < 10)
				this.timestamp = "2014-01-0" + Integer.toString(day) + " 0" + Integer.toString(hour) + ":00:00";
			else
				this.timestamp = "2014-01-" + Integer.toString(day) + " 0" + Integer.toString(hour) + ":00:00";
		}else{
			if(day < 10)
				this.timestamp = "2014-01-0" + Integer.toString(day) + " " + Integer.toString(hour) + ":00:00";
			else
				this.timestamp = "2014-01-" + Integer.toString(day) + " " + Integer.toString(hour) + ":00:00";
		}
		this.temperature = (int)(Math.random() * 20 + 10);
		this.windspeed = (int)(Math.random() * 15);
		this.windDir = (int)(Math.random() * 360);
		this.solarRad = (int)(Math.random() * 50) + 50;
	}

	public WeatherData(String loc, String time, int temp, int windsp, int windDir, int sol){
		this.locality = loc;
		this.timestamp = time;
		this.temperature = temp;
		this.windspeed = windsp;
		this.windDir = windDir;
		this.solarRad = sol;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getWindspeed() {
		return windspeed;
	}

	public void setWindspeed(int windspeed) {
		this.windspeed = windspeed;
	}

	public int getWindDir() {
		return windDir;
	}

	public void setWindDir(int windDir) {
		this.windDir = windDir;
	}

	public int getSolarRad() {
		return solarRad;
	}

	public void setSolarRad(int solarRad) {
		this.solarRad = solarRad;
	}

	public String toSqlEntry(){
		return "Insert Into WeatherData Values('" + this.locality + "', '" + this.timestamp + "', " + this.temperature + ", " + this.windspeed + ", " + this.windDir + ", " + this.solarRad + ");";
	}

}