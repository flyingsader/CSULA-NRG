
public class DummyDataMaker {
	
	public static void main(String[] args){
		
		int entries = 20;
		
		Device device;
		WeatherData weather;
		GridData grid;
		
		
		for(int i = 0; i < entries; i++){
			
			//device = new Device();
			//weather = new WeatherData(i / 24 + 1, i % 24);
			grid = new GridData(i / 24 + 1, i % 24);
			System.out.println(grid.toSqlEntry());
			
		}
		
	}

}
