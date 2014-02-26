package types;

public class GridData {
	String locality;
	String timestamp;
	int capacity;
	int demand;

	public static final String TimestampFormat = "yyyy-MM-dd hh:mm:ss:SS";
	private static final String TimestampStringFormat = "%s-%s-%s %s:00:00:00";

	public GridData(int day, int hour) {
		this.locality = "Los Angeles";
		if (hour < 10) {
			if (day < 10)
				this.timestamp = "2014-01-0" + Integer.toString(day) + " 0"
						+ Integer.toString(hour) + ":00:00";
			else
				this.timestamp = "2014-01-" + Integer.toString(day) + " 0"
						+ Integer.toString(hour) + ":00:00";
		} else {
			if (day < 10)
				this.timestamp = "2014-01-0" + Integer.toString(day) + " "
						+ Integer.toString(hour) + ":00:00";
			else
				this.timestamp = "2014-01-" + Integer.toString(day) + " "
						+ Integer.toString(hour) + ":00:00";
		}
		this.demand = (int) (Math.random() * 25000 + 25000);
		this.capacity = (int) (Math.random() * 5000 + 47500);
	}

	public GridData(int month, int day, int year, int hour) {
		this(day, hour);
		String sHour = Integer.toString(hour);
		String sDay = Integer.toString(day);
		String sMonth = Integer.toString(month);
		if (hour < 10)
			sHour = "0" + sHour;
		if (day < 10)
			sDay = "0" + sDay;

		if (month < 10)
			sMonth = "0" + sMonth;
		this.timestamp = String.format(TimestampStringFormat, year, sMonth,
				sDay, sHour);

	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getDemand() {
		return demand;
	}

	public void setDemand(int demand) {
		this.demand = demand;
	}

	public String toSqlEntry() {
		return "Insert Into GridData Values('" + this.locality + "', '"
				+ this.timestamp + "', " + this.capacity + ", " + this.demand
				+ ");";
	}
}
