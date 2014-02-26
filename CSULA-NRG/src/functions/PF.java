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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendWeatherDataToDMF(WeatherData weatherData) {
		_dmf.insertWeatherData(weatherData);
	}

	public void sendGridDataToDMF(GridData gridData) {
		_dmf.insertGridData(gridData);
	}

	public void sendGridDeficitPredictionToDMF() {
		try {
			// request data to analyze from DMF
			List<WeatherData> wData = _dmf.getAllWeatherData();
			List<GridData> gData = _dmf.getAllGridData();
			GridData prediction = null;

			// map weather and grid data by timestamp
			HashMap<WeatherData, GridData> map = mapData(wData, gData);

			// grab past years of days to detect pattern
			HashMap<WeatherData, GridData> pastYears = getPastYears(map);
			
			Entry<WeatherData, GridData> latest = getLatestWeather(map);
			// send expected grid data to DMF
			
			_dmf.setDeficit((int) (prediction.getCapacity() - (prediction
					.getDemand() * 1.01)));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private Entry<WeatherData, GridData> getLatestWeather(
			HashMap<WeatherData, GridData> map) {
		Entry<WeatherData,GridData> entry = (Entry<WeatherData, GridData>) map.entrySet().toArray()[0];
		
		for(Entry<WeatherData,GridData> wg: map.entrySet())
		{
			
		}
		
		return entry;
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

	private boolean isInsideDemandThreshold(int demand, GridData value) {
		boolean insideThreshold = false;
		int demandThreshold = 10;
		
		if(demand + demandThreshold > value.getDemand() && demand - demandThreshold < value.getDemand())
			insideThreshold = true;
		
		return insideThreshold;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	private HashMap<WeatherData, GridData> getPastYears(
			HashMap<WeatherData, GridData> map) throws ParseException {
		HashMap<WeatherData, GridData> data = new HashMap<WeatherData, GridData>();

		for (WeatherData w : map.keySet()) {
			String target = w.getTimestamp();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS",
					Locale.ENGLISH);
			Date result = df.parse(target);
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.DAY_OF_YEAR, 1);

			if (result.getMonth() == tomorrow.MONTH
					&& result.getDay() == tomorrow.DAY_OF_MONTH)
				data.put(w, map.get(w));
		}

		return data;
	}

	public static void main(String[] args) {

	}

}
