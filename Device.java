
public class Device {
	int DeviceID;
	String DeviceDesc;
	String DeviceOwner;
	int DeviceUsage;
	int Priority;
	
	
	public Device(){
		this.DeviceID = (int)(Math.random() * 1000000);
		this.DeviceDesc = "Appliance";
		this.DeviceOwner = "Someone";
		this.DeviceUsage = (int)(Math.random() * 500);
		this.Priority = (int)(Math.random() * 10);
	}
	
	public String toSqlEntry(){
		return "Insert Into Devices Values(" + this.DeviceID + ", '" + this.DeviceDesc + "', '" + this.DeviceOwner + "', " + this.DeviceUsage + ", " + this.Priority + ");";
	}
}
