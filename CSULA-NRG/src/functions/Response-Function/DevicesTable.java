package functions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import types.*;

public class DevicesTable extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JButton limitButton = new JButton("Limit Power");
	private JPanel jp = new JPanel();
	private JLabel currentDeficit;
	private JLabel totalDeviceUsageLabel;
	private JLabel priorities;
	private boolean clicked = false;
		
	public DevicesTable(List<Device> devices, int[] devicePercentage, int totalRecords, int remainingCurrentDeficit, int prioritiesLevels) {
			
		setLayout(new BorderLayout());
			
		JTable devicesData = new JTable();
			
		String[] columnLabels = {"Device ID", "Device Usage Percentage", "Priority"};
				
		// Create a row-column string array to store the record
		String[][] records = new String[totalRecords][3];
			
		// Store the record in the row-column string array	
		for (int i = 0; i < records.length; i++) {	
			records[i][0] = String.valueOf(devices.get(i).getDeviceID());
			records[i][1] = String.valueOf(devicePercentage[i]);
			records[i][2] = String.valueOf(devices.get(i).getPriority());
		}
				
		// Add the record to the table
		devicesData = new JTable(records, columnLabels);
		devicesData.setAutoCreateRowSorter(true);
			
		currentDeficit = new JLabel("Remaining Current Deficit: " + remainingCurrentDeficit);
		priorities = new JLabel("Total different priority levels: " + prioritiesLevels);
			
		JScrollPane scrollPane = new JScrollPane(devicesData);
			
		setTitle("Devices Table");
		setSize(300, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
			
		jp.add(currentDeficit);
		jp.add(priorities);
		add(BorderLayout.NORTH, jp);
		add(BorderLayout.CENTER, scrollPane);
	}
		
	public DevicesTable(List<Device> devices, int totalRecords, int prioritiesLevels, int totalDeviceUsage, int thisWindow) {
			
		setLayout(new BorderLayout());
			
		JTable devicesData = new JTable();
			
		String[] columnLabels = {"Device ID", "Device Usage", "Priority"};
				
		// Create a row-column string array to store the record
		String[][] records = new String[totalRecords][3];
				
		// Store the record in the row-column string array
		for (int i = 0; i < records.length; i++) {				
			records[i][0] = String.valueOf(devices.get(i).getDeviceID());
			records[i][1] = String.valueOf(devices.get(i).getDeviceUsage());
			records[i][2] = String.valueOf(devices.get(i).getPriority());
		}
				
		// Add the record to the table
		devicesData = new JTable(records, columnLabels);
		devicesData.setAutoCreateRowSorter(true);
			
		priorities = new JLabel("Total different priority levels: " + prioritiesLevels);
			
		totalDeviceUsageLabel = new JLabel("Total Device Usage: " + totalDeviceUsage);
			
		JScrollPane scrollPane = new JScrollPane(devicesData);
			
		ButtonListener listener = new ButtonListener();
		limitButton.addActionListener(listener);
			
		setTitle("Devices Table");
		setSize(300, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
			
		jp.add(priorities);
		jp.add(totalDeviceUsageLabel);
		add(BorderLayout.NORTH, jp);
		add(BorderLayout.CENTER, scrollPane);
		add(BorderLayout.SOUTH, limitButton);
	}
		
	class ButtonListener implements ActionListener {
			
		// Constructor
		public ButtonListener() {
			
		}
			
		// Exit the window
		public void actionPerformed(ActionEvent e) {
			clicked = true;
		}
	}

	public boolean isClicked() {
		return clicked;
	}
}
