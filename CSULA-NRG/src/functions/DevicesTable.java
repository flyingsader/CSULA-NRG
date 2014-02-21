package functions;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class DevicesTable extends JFrame {

		private JButton limitButton = new JButton("Limit Power");
		private JPanel jp = new JPanel();
		private JLabel currentDeficit;
		private JLabel totalDeviceUsageLabel;
		private JLabel priorities;
		private int totalRecords;
		private int remainingCurrentDeficit;
		private int totalDeviceUsage;
		private int prioritiesLevels;
		private boolean clicked = false;
		
		public DevicesTable(Statement stmt, int totalRecords, int remainingCurrentDeficit, int prioritiesLevels) {
			
			this.totalRecords = totalRecords;
			this.remainingCurrentDeficit = remainingCurrentDeficit;
			this.prioritiesLevels = prioritiesLevels;
			
			setLayout(new BorderLayout());
			
			JTable devicesData = new JTable();
			
			String[] columnLabels = {"DeviceID", "DeviceUsage", "Priority"};
			//String[] columnLabels = {"DeviceID", "DeviceDesc", "DeviceOwner", "DeviceUsage", "Priority"};
			
			try {  
				
				// Create a row-column string array to store the record
				String[][] records = new String[totalRecords][3];
				//String[][] records = new String[totalRecords][5];
				
				// Store the record in the row-column string array
				ResultSet printResults = stmt.executeQuery("SELECT * FROM Devices Order By Priority");
				
				for (int i = 0; i < records.length; i++) {
					if (printResults.next()) {
						records[i][0] = printResults.getString("DeviceID");
//						records[i][1] = printResults.getString("DeviceDESC");
//						records[i][2] = printResults.getString("DeviceOwner");
						records[i][1] = printResults.getString("DeviceUsage");
						records[i][2] = printResults.getString("Priority");
					}
				}
				
				// Add the record to the table
				devicesData = new JTable(records, columnLabels);
				devicesData.setAutoCreateRowSorter(true);
			}
			
			catch (SQLException sql) {
				sql.printStackTrace();
			}
			
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
		
		public DevicesTable(Statement stmt, int totalRecords, int prioritiesLevels, int totalDeviceUsage, int thisWindow) {
			
			this.totalRecords = totalRecords;
			this.prioritiesLevels = prioritiesLevels;
			this.totalDeviceUsage = totalDeviceUsage;
			
			setLayout(new BorderLayout());
			
			JTable devicesData = new JTable();
			
			String[] columnLabels = {"DeviceID", "DeviceUsage", "Priority"};
			//String[] columnLabels = {"DeviceID", "DeviceDesc", "DeviceOwner", "DeviceUsage", "Priority"};
			
			try {  
				
				// Create a row-column string array to store the record
				String[][] records = new String[totalRecords][3];
				//String[][] records = new String[totalRecords][5];
				
				// Store the record in the row-column string array
				ResultSet printResults = stmt.executeQuery("SELECT * FROM Devices Order By Priority");
				
				for (int i = 0; i < records.length; i++) {
					if (printResults.next()) {
						records[i][0] = printResults.getString("DeviceID");
//						records[i][1] = printResults.getString("DeviceDESC");
//						records[i][2] = printResults.getString("DeviceOwner");
						records[i][1] = printResults.getString("DeviceUsage");
						records[i][2] = printResults.getString("Priority");
					}
				}
				
				// Add the record to the table
				devicesData = new JTable(records, columnLabels);
				devicesData.setAutoCreateRowSorter(true);
			}
			
			catch (SQLException sql) {
				sql.printStackTrace();
			}
			
			priorities = new JLabel("Total different priority levels: " + prioritiesLevels);
			//priorities.setHorizontalAlignment(SwingConstants.CENTER);
			
			totalDeviceUsageLabel = new JLabel("Total Device Usage: " + totalDeviceUsage);
			
			JScrollPane scrollPane = new JScrollPane(devicesData);
			
			ButtonListener listener = new ButtonListener(stmt);
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
			
			Statement stmt;
			
			// Constructor
			public ButtonListener(Statement stmt) {
				this.stmt = stmt;
			}
			
			// Exit the window
			public void actionPerformed(ActionEvent e) {
				//setVisible(false);
				clicked = true;
			}
		}

		public boolean isClicked() {
			return clicked;
		}
}
