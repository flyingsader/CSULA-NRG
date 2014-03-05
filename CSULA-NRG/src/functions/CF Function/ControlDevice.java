import java.awt.List;
import java.security.Timestamp;
import java.util.ArrayList;

import types.Device;
import types.WeatherData;

public class ControlDevice {
	private java.util.List<Device> controlDevices;
	private boolean swtich;
	private String due;
	private String type;

	public ControlDevice() {
	}

	public ControlDevice(boolean p, String t, String du,
			java.util.List<Device> list) {
		this.controlDevices = list;
		this.swtich = p;
		this.due = du;
		this.type = t;

	}

	public boolean isSwtich() {
		return swtich;
	}

	public void setSwtich(boolean swtich) {
		this.swtich = swtich;
	}

	public String getDue() {
		return due;
	}

	public void setDue(String due) {
		this.due = due;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public java.util.List<Device> getControlDevices() {
		return controlDevices;
	}

	public void setControlDevices(java.util.List<Device> controlDevices) {
		this.controlDevices = controlDevices;
	}
}
