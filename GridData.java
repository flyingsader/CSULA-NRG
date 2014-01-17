
public class GridData {
	String Locality;
	String Timestamp;
	int Capacity;
	int Demand;
	
	
	public GridData(int day, int hour){
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
		this.Demand = (int)(Math.random() * 25000 + 25000);
		this.Capacity = (int)(Math.random() * 5000 + 47500);
	}
	
	public String toSqlEntry(){
		return "Insert Into Devices Values('" + this.Locality + "', '" + this.Timestamp + "', " + this.Capacity + ", " + this.Demand + ");";
	}
}
