package types;

public class Device {
	int deviceID;
	String deviceDesc;
	String deviceOwner;
	int deviceUsage;
	int priority;

	public Device(){
		this.deviceID = (int)(Math.random() * 1000000);
		this.deviceDesc = "Appliance";
		this.deviceOwner = "Someone";
		this.deviceUsage = (int)(Math.random() * 500);
		this.priority = (int)(Math.random() * 10);
	}

	public Device(int ID, String desc, String owner, int usage, int prio){
		this.deviceID = ID;
		this.deviceDesc = desc;
		this.deviceOwner = owner;
		this.deviceUsage = usage;
		this.priority = prio;
	}

	public int getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	public String getDeviceDesc() {
		return deviceDesc;
	}

	public void setDeviceDesc(String deviceDesc) {
		this.deviceDesc = deviceDesc;
	}

	public String getDeviceOwner() {
		return deviceOwner;
	}

	public void setDeviceOwner(String deviceOwner) {
		this.deviceOwner = deviceOwner;
	}

	public int getDeviceUsage() {
		return deviceUsage;
	}

	public void setDeviceUsage(int deviceUsage) {
		this.deviceUsage = deviceUsage;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String toSqlEntry(){
		return "Insert Into Devices Values(" + this.deviceID + ", '" + this.deviceDesc + "', '" + this.deviceOwner + "', " + this.deviceUsage + ", " + this.priority + ");";
	}
}