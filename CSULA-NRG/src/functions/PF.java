package functions;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import types.GridData;
import types.WeatherData;

public class PF {

	private DMF _dmf;

	public PF() {
		try {
			_dmf = new DMF();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sendWeatherDataToDMF(WeatherData weatherData) {
		try {
			System.out.println("Sending weather data to DMF...");
			_dmf.insertWeatherData(weatherData);
			System.out.println("Weather data successfully sent.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendGridDataToDMF(GridData gridData) {
		try {
			System.out.println("Sending grid data to DMF...");
			_dmf.insertGridData(gridData);
			System.out.println("Grid data successfully sent.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendGridDeficitPredictionToDMF() {
		try {
			Date started = new Date();
			System.out.println("Time started: " + started.toString());
			System.out.println();
			System.out.println("Pulling historical data from DMF...");
			System.out.println();
			// request data to analyze from DMF
			List<WeatherData> wData = _dmf.getAllWeatherData();
			List<GridData> gData = _dmf.getAllGridData();

			System.out.println("Mapping data by timestamp...");
			System.out.println();

			// map weather and grid data by timestamp
			HashMap<WeatherData, GridData> map = mapData(wData, gData);

			System.out.println("Data mapped. Querying past years...");
			System.out.println();

			// grab past years of days to detect pattern
			HashMap<WeatherData, GridData> pastYears = getPastYears(map);

			System.out.println("Calculating capacity and demand...");
			System.out.println();

			// calculate average of grid demand for past years
			int totalDemand = 0;
			int totalCap = 0;
			for (GridData gd : pastYears.values()) {
				totalDemand += gd.getDemand();
				totalCap += gd.getCapacity();
			}

			System.out.println(String.format("Total capacity: " + totalCap));
			System.out.println(String.format("Total demand: " + totalDemand));
			System.out.println();

			double avgCap = totalCap / pastYears.size();
			double avgDemand = totalDemand / pastYears.size();

			System.out.println(String.format("Average capacity: %.2f", avgCap));
			System.out
					.println(String.format("Average demand: %.2f", avgDemand));
			System.out.println();

			// send expected grid data to DMF
			double difference = avgCap - (avgDemand * 1.01);
			System.out.println(String.format("Capacity - Demand * 1.01: %.2f",
					difference));
			if (difference > 0.0)
				System.out.println("There's a predicted surplus");
			else
				System.out.println("There's a predicted deficit");
			System.out.println();
			System.out.println("Sending prediction to DMF...");
			System.out.println();
			_dmf.setDeficit(0 - ((int) difference));
			System.out.println("Prediction sent successfully");
			System.out.println();
			System.out.println("Time ended: " + new Date().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private HashMap<WeatherData, GridData> mapData(List<WeatherData> wData,
			List<GridData> gData) {
		HashMap<WeatherData, GridData> map = new HashMap<WeatherData, GridData>();

		for (WeatherData w : wData) {
			for (GridData g : gData) {
				if (w.getTimestamp().equals(g.getTimestamp())) {
					map.put(w, g);
					break;
				}
			}
		}

		return map;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	private HashMap<WeatherData, GridData> getPastYears(
			HashMap<WeatherData, GridData> map) throws ParseException {
		HashMap<WeatherData, GridData> data = new HashMap<WeatherData, GridData>();

		for (WeatherData w : map.keySet()) {
			String target = w.getTimestamp();
			DateFormat df = new SimpleDateFormat(WeatherData.TimestampFormat,
					Locale.ENGLISH);
			Date result = df.parse(target);
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.HOUR, 1);

			if (result.getMonth() == tomorrow.MONTH
					&& result.getDay() == tomorrow.DAY_OF_MONTH
					&& result.getHours() == tomorrow.HOUR_OF_DAY)
				data.put(w, map.get(w));
		}

		return data;
	}

	public static void main(String[] args) {
		PF _pf = new PF();
		_pf.sendGridDataToDMF(new GridData(3,5,2014,10));
		System.out.println();
		_pf.sendWeatherDataToDMF(new WeatherData(3,5,2014,10));
		System.out.println();
		_pf.sendGridDeficitPredictionToDMF();
	}

}
