package functions;

import java.util.List;

import types.Device;

public class ResponsePackage {
	
	List<Device> devices;
	int[] devicesPercentages;
	
	public ResponsePackage(List<Device> devices, int[] devicesPercentages) {
		this.devices = devices;
		this.devicesPercentages = devicesPercentages;
	}
	
	// Communication with Control Interface Function
	public void sentToCIF() {
		//Send the response package to the Control Interface Function
	}

	public List<Device> getDevices() {
		return devices;
	}

	public int[] getDevicesPercentages() {
		return devicesPercentages;
	}
}
