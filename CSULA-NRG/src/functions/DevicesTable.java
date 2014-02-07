package functions;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class DevicesTable extends JFrame {

		private JButton limitButton = new JButton("Limit Power");
		private JPanel jp = new JPanel();
		private JLabel watts;
		private JLabel priorities;
		private int totalRecords;
		private double availableWatts;
		private int prioritiesLevels;
		private boolean clicked = false;
		
		public DevicesTable(Statement stmt, int totalRecords, double availableWatts, int prioritiesLevels) {
			
			this.totalRecords = totalRecords;
			this.availableWatts = availableWatts;
			this.prioritiesLevels = prioritiesLevels;
			
			setLayout(new BorderLayout());
			
			JTable devicesData = new JTable();
			
			String[] columnLabels = {"DeviceID", "DeviceDesc", "DeviceOwner", "DeviceUsage", "Priority"};
			
			try {  
				
				// Create a row-column string array to store the record
				String[][] records = new String[totalRecords][5];
				
				// Store the record in the row-column string array
				ResultSet printResults = stmt.executeQuery("SELECT * FROM Devices Order By Priority");
				
				for (int i = 0; i < records.length; i++) {
					if (printResults.next()) {
						records[i][0] = printResults.getString("DeviceID");
						records[i][1] = printResults.getString("DeviceDESC");
						records[i][2] = printResults.getString("DeviceOwner");
						records[i][3] = printResults.getString("DeviceUsage");
						records[i][4] = printResults.getString("Priority");
					}
				}
				
				// Add the record to the table
				devicesData = new JTable(records, columnLabels);
				devicesData.setAutoCreateRowSorter(true);
			}
			
			catch (SQLException sql) {
				sql.printStackTrace();
			}
			
			watts = new JLabel("Available watts: " + availableWatts);
			priorities = new JLabel("Total different priority levels: " + prioritiesLevels);
			
			JScrollPane scrollPane = new JScrollPane(devicesData);
			
			setTitle("Devices Table");
			setSize(300, 300);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
			setExtendedState(MAXIMIZED_BOTH);
			
			jp.add(watts);
			jp.add(priorities);
			add(BorderLayout.NORTH, jp);
			add(BorderLayout.CENTER, scrollPane);
		}
		
		public DevicesTable(Statement stmt, int totalRecords, int prioritiesLevels) {
			
			this.totalRecords = totalRecords;
			this.prioritiesLevels = prioritiesLevels;
			
			setLayout(new BorderLayout());
			
			JTable devicesData = new JTable();
			
			String[] columnLabels = {"DeviceID", "DeviceDesc", "DeviceOwner", "DeviceUsage", "Priority"};
			
			try {  
				
				// Create a row-column string array to store the record
				String[][] records = new String[totalRecords][5];
				
				// Store the record in the row-column string array
				ResultSet printResults = stmt.executeQuery("SELECT * FROM Devices Order By Priority");
				
				for (int i = 0; i < records.length; i++) {
					if (printResults.next()) {
						records[i][0] = printResults.getString("DeviceID");
						records[i][1] = printResults.getString("DeviceDESC");
						records[i][2] = printResults.getString("DeviceOwner");
						records[i][3] = printResults.getString("DeviceUsage");
						records[i][4] = printResults.getString("Priority");
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
			add(BorderLayout.NORTH, priorities);
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
				setVisible(false);
				clicked = true;
			}
		}

		public boolean isClicked() {
			return clicked;
		}
}
