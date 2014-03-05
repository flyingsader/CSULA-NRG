package types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeatherData {

	String locality;
	String timestamp;
	int temperature;
	int windspeed;
	int windDir;
	int solarRad;

	public static final String TimestampFormat = "yyyy-MM-dd hh:mm:ss";
	private static final String TimestampStringFormat = "%s-%s-%s %s:00:00";

	public WeatherData(int day, int hour) {
		this.locality = "Los Angeles";
		if (hour < 10) {
			if (day < 10)
				this.timestamp = "2014-01-0" + Integer.toString(day) + " 0"
						+ Integer.toString(hour) + ":00:00";
			else
				this.timestamp = "2014-01-" + Integer.toString(day) + " 0"
						+ Integer.toString(hour) + ":00:00";
		} else {
			if (day < 10)
				this.timestamp = "2014-01-0" + Integer.toString(day) + " "
						+ Integer.toString(hour) + ":00:00";
			else
				this.timestamp = "2014-01-" + Integer.toString(day) + " "
						+ Integer.toString(hour) + ":00:00";
		}
		this.temperature = (int) (Math.random() * 20 + 10);
		this.windspeed = (int) (Math.random() * 15);
		this.windDir = (int) (Math.random() * 360);
		this.solarRad = (int) (Math.random() * 50) + 50;
	}

	public WeatherData(int month, int day, int year, int hour) {
		this(hour, day);

		String sHour = Integer.toString(hour);
		String sDay = Integer.toString(day);
		String sMonth = Integer.toString(month);
		if (hour < 10)
			sHour = "0" + sHour;
		if (day < 10)
			sDay = "0" + sDay;

		if (month < 10)
			sMonth = "0" + sMonth;
		this.timestamp = String.format(TimestampStringFormat, year, sMonth,
				sDay, sHour);
	}

	public WeatherData(String loc, String time, int temp, int windsp,
			int windDir, int sol) {
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
	
	public Calendar parseTimestampToDate() throws ParseException
	{
		DateFormat df = new SimpleDateFormat(TimestampFormat,
				Locale.ENGLISH);
		Date result = df.parse(timestamp);
		Calendar time = Calendar.getInstance();
		time.set(Calendar.MONTH, result.getMonth());
		time.set(Calendar.DAY_OF_MONTH, result.getDay());
		time.set(Calendar.YEAR, result.getYear());
		time.set(Calendar.HOUR_OF_DAY, result.getHours());
		time.set(Calendar.MINUTE, result.getMinutes());
		return time;
	}

	public String toSqlEntry() {
		return "Insert Into WeatherData Values('" + this.locality + "', "
				+ "STR_TO_DATE('" + this.timestamp + "', '%Y-%m-%d %H:%i:%s')" + ", " + this.temperature + ", "
				+ this.windspeed + ", " + this.windDir + ", " + this.solarRad
				+ ");";
	}

}
