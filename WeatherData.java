public class WeatherData {

	String Locality;
	String Timestamp;
	int Temperature;
	int Windspeed;
	int WindDir;
	int SolarRad;
	
	
	public WeatherData(int day, int hour){
		this.Locality = "Los Angeles";
		if(hour < 10){
			if(day < 10)
				this.Timestamp = "2014-01-0" + Integer.toString(day) + " 0" + Integer.toString(hour) + ":00:00";
			else
				this.Timestamp = "2014-01-" + Integer.toString(day) + " 0" + Integer.toString(hour) + ":00:00";
		}else{
			if(day < 10)
				this.Timestamp = "2014-01-0" + Integer.toString(day) + " " + Integer.toString(hour) + ":00:00";
			else
				this.Timestamp = "2014-01-" + Integer.toString(day) + " " + Integer.toString(hour) + ":00:00";
		}
		this.Temperature = (int)(Math.random() * 20 + 10);
		this.Windspeed = (int)(Math.random() * 15);
		this.WindDir = (int)(Math.random() * 360);
		this.SolarRad = (int)(Math.random() * 50) + 50;
	}
	
	public String toSqlEntry(){
		return "Insert Into WeatherData Values('" + this.Locality + "', '" + this.Timestamp + "', " + this.Temperature + ", " + this.Windspeed + ", " + this.WindDir + ", " + this.SolarRad + ");";
	}

}
