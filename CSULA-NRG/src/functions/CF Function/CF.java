import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.security.Timestamp;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

import types.Device;
import types.WeatherData;

public class CF extends JFrame {

	// card layout
	Container pane;
	CardLayout layout;
	// Panels
	private JPanel controlPanel, requestPanel, mainPanel, buttonPanel,
			graphPanel, addPanel, removePanel, leftControlPanel,
			rightControlPanel, rightAddPanel, rightRemovePanel;
	// add buttons for sub panels communicate with response function
	private JButton controls = new JButton("System Controls");
	private JButton main = new JButton("Return To Main Menu");
	private JButton mainTwo = new JButton("Return To Main Menu");
	private JButton submit = new JButton("Submit");
	private JButton searchType = new JButton("Search");
	private JButton searchPriority = new JButton("Search");
	private JButton setInst = new JButton("add instructions");
	private JButton removeInst = new JButton("remove instructions");
	private JButton add = new JButton("add");
	private JButton remove = new JButton("remove");
	// JLabel
	private JLabel userName, password, search, timestamp;
	// JTextField
	private JTextField overrideUser, overridePass, resumeUser, resumePass,
			searchData, newInst, removeIn;
	// JComboBox
	private JComboBox timeframe;
	// list of time
	private String[] hours = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
			"21", "22", "23", "24" };
	// graph objects
	private JTextArea deviceOutput, deviceInst, curInst, deviceInstTwo;
	// all data
	private JScrollPane scroll, scrollTwo, scrollThree, scrollFour;

	private List<ControlDevice> allInstructions = new ArrayList<ControlDevice>();

	// DMF
	private String setDueDate(String addHours) {
		int hours = Integer.parseInt(addHours);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, hours);
		SimpleDateFormat str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str.format(cal.getTime());
		return str.format(cal.getTime());
	}

	private List<Device> setDeviceList(String type) throws SQLException {
		DMF fun = new DMF();
		List<Device> controlList = fun.getAllDevices();
		List<Device> devices = new ArrayList<Device>();
		for (Device d : controlList) {
			if (d.getDeviceDesc().equals(type)) {
				devices.add(d);
			}

		}

		return devices;
	}

	private List<Device> setDeviceList(int type) throws SQLException {
		DMF fun = new DMF();
		List<Device> controlList = fun.getAllDevices();
		List<Device> devices = new ArrayList<Device>();
		for (Device d : controlList) {
			if (d.getPriority() >= type) {
				devices.add(d);

			}

		}

		return devices;
	}

	private void removeDeviceList(List<ControlDevice> allInstructions, int type)
			throws SQLException {
		for (Iterator<ControlDevice> iter = allInstructions.iterator(); iter
				.hasNext();) {
			ControlDevice curDevice = iter.next();
			System.out.println(allInstructions.indexOf(curDevice) + type + "!!!!!!!!!!!!!!!!!!!");
			if (allInstructions.indexOf(curDevice) == type)
				iter.remove();
		}
	}

	private void display() throws SQLException {
		deviceInst.setText("");
		deviceInst.append("\n");
		deviceInst.append("Current Instructions: --------------------\n");

		if (!allInstructions.isEmpty()) {
			for (ControlDevice d : allInstructions) {
				deviceInst.append("#: " + allInstructions.indexOf(d) + "\n");
				deviceInst.append("Type: " + d.getType() + " ");
				if (d.isSwtich()) {
					deviceInst.append("Switch is: on ");
				} else {
					deviceInst.append("Switch is: off ");
				}
				deviceInst.append("Due Date: " + d.getDue() + " ");
				for (Device dt : d.getControlDevices()) {

					deviceInst.append(dt.getDeviceID() + ", "
							+ dt.getDeviceDesc() + ", " + dt.getDeviceOwner()
							+ ", " + dt.getDeviceUsage() + ", "
							+ dt.getPriority() + "\n");
				}
				deviceInst.append("\n");
			}
		}
	}

	private void displayTwo() throws SQLException {
		curInst.setText("");
		curInst.append("\n");
		curInst.append("Current Instructions: --------------------\n");
		if (!allInstructions.isEmpty()) {
			for (ControlDevice d : allInstructions) {
				curInst.append("Type: " + d.getType() + " ");
				if (d.isSwtich()) {
					curInst.append("Switch is: on ");
				} else {
					curInst.append("Switch is: off ");
				}
				curInst.append("Due Date: " + d.getDue() + " ");
				for (Device dt : d.getControlDevices()) {

					curInst.append(dt.getDeviceID() + ", " + dt.getDeviceDesc()
							+ ", " + dt.getDeviceOwner() + ", "
							+ dt.getDeviceUsage() + ", " + dt.getPriority()
							+ "\n");
				}
				curInst.append("\n");
			}
		}
	}

	private void displayThree() throws SQLException {
		deviceInstTwo.setText("");
		deviceInstTwo.append("\n");
		deviceInstTwo.append("Current Instructions: --------------------\n");
		if (!allInstructions.isEmpty()) {
			for (ControlDevice d : allInstructions) {
				deviceInstTwo.append("Type: " + d.getType() + " ");
				if (d.isSwtich()) {
					deviceInstTwo.append("Switch is: on ");
				} else {
					deviceInstTwo.append("Switch is: off ");
				}
				deviceInstTwo.append("Due Date: " + d.getDue() + " ");
				for (Device dt : d.getControlDevices()) {

					deviceInstTwo.append(dt.getDeviceID() + ", "
							+ dt.getDeviceDesc() + ", " + dt.getDeviceOwner()
							+ ", " + dt.getDeviceUsage() + ", "
							+ dt.getPriority() + "\n");
				}
				deviceInstTwo.append("\n");
			}
		}
	}

	public CF() throws SQLException {
		// start layout
		layout = new CardLayout();
		setLayout(layout);
		pane = this.getContentPane();
		DMF dmf = new DMF();
		// All Labels
		userName = new JLabel("Username: ");
		password = new JLabel("Password: ");
		search = new JLabel("Search: ");
		timestamp = new JLabel("Select timeframe for instruction: ");
		// Configure Main Panel
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);

		// North
		JLabel welcome = new JLabel("Control Interface", JLabel.CENTER);
		welcome.setFont(new Font("Serif", Font.BOLD, 48));
		mainPanel.add(welcome, BorderLayout.NORTH);
		// Center
		graphPanel = new JPanel(new GridLayout(2, 2));
		// graphs
		TemperatureGraph temperature = new TemperatureGraph();
		WindGraph windSpeed = new WindGraph();
		TemperatureGraph temperatureTwo = new TemperatureGraph();
		TemperatureGraph temperatureThird = new TemperatureGraph();
		graphPanel.add(temperature);
		graphPanel.add(windSpeed);
		graphPanel.add(temperatureTwo);
		graphPanel.add(temperatureThird);

		mainPanel.add(graphPanel, BorderLayout.CENTER);

		// South (exclude east/west)
		buttonPanel = new JPanel(new GridLayout(0, 1, 20, 20));
		buttonPanel.add(controls);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		pane.add("Main", mainPanel);
		setTitle("Control Panel");
		setSize(new Dimension(960, 640));

		// control panel

		// Text Area for search results
		deviceOutput = new JTextArea(5, 20);
		final List<Device> testList = dmf.getAllDevices();
		deviceOutput.append("\n");
		deviceOutput.append("DataBase List: --------------------\n");
		for (Device data : testList) {
			deviceOutput.append(data.getDeviceID() + ", "
					+ data.getDeviceDesc() + ", " + data.getDeviceOwner()
					+ ", " + data.getDeviceUsage() + ", " + data.getPriority()
					+ "\n");
		}
		deviceOutput.setEditable(false);
		scroll = new JScrollPane(deviceOutput);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// set current instructions
		curInst = new JTextArea(5, 20);
		displayTwo();
		curInst.setEditable(false);
		scrollThree = new JScrollPane(curInst);
		scrollThree
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollThree
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		// search for current device data by type and priority

		final JTextField searchT = new JTextField(10);
		ButtonGroup searchChoices = new ButtonGroup();
		final JRadioButton RB_TYPE = new JRadioButton("Type");
		final JRadioButton RB_PR = new JRadioButton("Priority");
		searchChoices.add(RB_TYPE);
		searchChoices.add(RB_PR);
		RB_TYPE.setSelected(true);

		// Add to resume panel
		controlPanel = new JPanel(new BorderLayout());
		leftControlPanel = new JPanel(new GridLayout(2, 0));
		rightControlPanel = new JPanel(new FlowLayout());

		// north panel
		JLabel instructions = new JLabel("Instruction Interface", JLabel.CENTER);
		instructions.setFont(new Font("Serif", Font.BOLD, 48));
		controlPanel.add(instructions, BorderLayout.NORTH);

		// Center panel
		leftControlPanel.add(scroll);
		leftControlPanel.add(scrollThree);
		controlPanel.add(leftControlPanel, BorderLayout.CENTER);
		// south panel
		rightControlPanel.add(searchT);
		rightControlPanel.add(searchType);
		rightControlPanel.add(RB_TYPE);
		rightControlPanel.add(RB_PR);
		rightControlPanel.add(setInst);
		rightControlPanel.add(removeInst);
		controlPanel.add(rightControlPanel, BorderLayout.SOUTH);
		pane.add("control", controlPanel);

		// add inst panel
		// Text Area for current instructions

		deviceInst = new JTextArea(20, 20);
		deviceInst.append("\n");
		deviceInst.append("Current Instructions: --------------------\n");
		if (!allInstructions.isEmpty()) {
			for (ControlDevice d : allInstructions) {
				deviceInst.append("");
			}
		}
		scrollTwo = new JScrollPane(deviceInst);
		scrollTwo
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollTwo
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// add other things to addPanel
		timeframe = new JComboBox(hours);
		final JTextField newInst = new JTextField(10);
		ButtonGroup chooseType = new ButtonGroup();
		final JRadioButton RB_TYPE_TWO = new JRadioButton("Type");
		final JRadioButton RB_PR_TWO = new JRadioButton("Priority");
		chooseType.add(RB_TYPE_TWO);
		chooseType.add(RB_PR_TWO);

		// add to addPane

		// Add to resume panel
		addPanel = new JPanel(new BorderLayout());
		rightAddPanel = new JPanel(new FlowLayout());

		// north panel
		JLabel addText = new JLabel("Add Instructions", JLabel.CENTER);
		addText.setFont(new Font("Serif", Font.BOLD, 48));
		addPanel.add(addText, BorderLayout.NORTH);

		// Center panel
		addPanel.add(scrollTwo, BorderLayout.CENTER);
		// South Panel
		rightAddPanel.add(timestamp);
		rightAddPanel.add(timeframe);
		rightAddPanel.add(new JLabel("Select Group: "));
		rightAddPanel.add(newInst);
		rightAddPanel.add(new JLabel("By: "));
		rightAddPanel.add(RB_TYPE_TWO);
		rightAddPanel.add(RB_PR_TWO);
		rightAddPanel.add(add);
		rightAddPanel.add(mainTwo);
		addPanel.add(rightAddPanel, BorderLayout.SOUTH);
		pane.add("add", addPanel);

		// remove inst panel
		removePanel = new JPanel(new BorderLayout());
		rightRemovePanel = new JPanel(new FlowLayout());

		// north panel
		JLabel removeText = new JLabel("Remove Instructions", JLabel.CENTER);
		removeText.setFont(new Font("Serif", Font.BOLD, 48));
		removePanel.add(removeText, BorderLayout.NORTH);

		// Center panel
		deviceInstTwo = new JTextArea(20, 20);
		scrollFour = new JScrollPane(deviceInstTwo);
		scrollFour
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollFour
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		displayThree();

		removePanel.add(scrollFour, BorderLayout.CENTER);

		// South Panel
		removeIn = (new JTextField(10));

		rightRemovePanel.add(new JLabel("Select id to remove: "));
		rightRemovePanel.add(removeIn);
		rightRemovePanel.add(remove);
		rightRemovePanel.add(main);
		removePanel.add(rightRemovePanel, BorderLayout.SOUTH);

		pane.add("remove", removePanel);

// //////////////////////////////////////////////////////////////////////////////////////////
		controls.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				layout.show(pane, "control");
			}
		});
		setInst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				layout.show(pane, "add");
			}
		});
		removeInst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				layout.show(pane, "remove");
			}
		});
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (RB_TYPE_TWO.isSelected()) {
					try {
						allInstructions.add(new ControlDevice(
								false,
								"device",
								setDueDate((String) timeframe.getSelectedItem()),
								setDeviceList(newInst.getText().trim())));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					deviceInst.append("");
				}
				if (RB_PR_TWO.isSelected()) {
					try {
						allInstructions
								.add(new ControlDevice(false, "device",
										setDueDate((String) timeframe
												.getSelectedItem()),
										setDeviceList(Integer.parseInt(newInst
												.getText().trim()))));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				try {
					display();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				curInst.append("");
				deviceInstTwo.setText("");
				try {
					displayTwo();
					displayThree();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				layout.show(pane, "add");
			}
		});
		removeInst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				layout.show(pane, "remove");
			}
		});
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					removeDeviceList(allInstructions,
							Integer.parseInt(removeIn
									.getText().trim()));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				deviceInstTwo.setText("");
				try {
					displayThree();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				layout.show(pane, "remove");
			}
		});
		searchType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				if (RB_TYPE.isSelected()) {
					deviceOutput.setText("");
					String id = searchT.getText().trim();
					deviceOutput.append("Results by type \n");
					for (Device data : testList) {

						if (id.equals(data.getDeviceDesc())) {

							deviceOutput.append(data.getDeviceID() + ", "
									+ data.getDeviceDesc() + ", "
									+ data.getDeviceOwner() + ", "
									+ data.getDeviceUsage() + ", "
									+ data.getPriority() + "\n");
						}
					}
				}
				if (RB_PR.isSelected()) {
					deviceOutput.setText("");
					int id = Integer.parseInt(searchT.getText().trim());
					deviceOutput.append("Results by priority \n");
					for (Device data : testList) {

						if (id == data.getPriority()) {

							deviceOutput.append(data.getDeviceID() + ", "
									+ data.getDeviceDesc() + ", "
									+ data.getDeviceOwner() + ", "
									+ data.getDeviceUsage() + ", "
									+ data.getPriority() + "\n");
						}
					}
				}

			}
		});

		main.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				layout.show(pane, "Main");
			}
		});
		mainTwo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				layout.show(pane, "Main");
			}
		});

	}


	public static void main(String[] arg) throws SQLException {
		CF f = new CF();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
