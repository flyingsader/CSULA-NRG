package functions;

import java.sql.SQLException;
import java.util.List;

import types.GridData;
import types.WeatherData;

public class PF {
	
	private DMF _dmf;
	
	public PF()
	{
		try {
			_dmf = new DMF();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendWeatherDataToDMF(WeatherData weatherData)
	{
		_dmf.insertWeatherData(weatherData);
	}
	
	public void sendGridDataToDMF(GridData gridData)
	{
		_dmf.insertGridData(gridData);
	}
	
	public void sendGridDeficitPredictionToDMF()
	{
		try {
			List<WeatherData> wData = _dmf.getAllWeatherData();
			List<GridData> gData = _dmf.getAllGridData();
			GridData prediction = null;
			
			
			
			_dmf.setDeficit((int) (prediction.getCapacity() - (prediction.getDemand() * 1.01)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {

	}

}
