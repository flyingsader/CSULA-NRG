
public class DummyDataMaker {
	
	public static void main(String[] args){
		
		int entries = 20;
		
		Device device;
		
		
		for(int i = 0; i < entries; i++){
			
			device = new Device();
			System.out.println(device.toSqlEntry());
			
		}
		
	}

}
