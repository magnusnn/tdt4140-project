package gui;


import gui.EmailInviter;
import gui.EmailMain;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JCheckBox;

import db.DBConnect;

import logic.Appointment;
import logic.Mail;

import logic.Calendar;
import logic.Event;
import logic.Person;
import logic.Room;
import logic.Util;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NewAppointment extends JPanel implements FocusListener{
	//TextFields for appointment
	private JTextField title;
	private JTextField reminder;
	private JTextField from;
	private JTextField to;
	private JTextField duration;
	private JTextField date;
	private JTextField place;

	private JTextArea textArea;
	
	//TextFields for meeting
	private JTextField meetingTitle;
	private JTextField meetingReminder;
	private JTextField meetingFrom;
	private JTextField meetingTo;
	private JTextField meetingDuration;
	private JTextField meetingDate;
	private JTextField meetingPlace;
	
	private JTextArea meetingTextArea;
	
	public static JFrame participantFrame;
	
	//Buttons
	private JButton btnOk;
	private JButton btnCancel;
	private JButton btnManageParticipants;
	private JButton btnInviteViaEmail;
	
	//ComboBoxes
	private JComboBox roomComboBox;
	private JComboBox capasityComboBox;
	
	//CheckBoxes
	private JCheckBox meetingCheckBox;
	private JCheckBox chckbxReminder;
	
	//TabbedPane
	private JTabbedPane tabbedPane;
	
	//Strings holding the input
	private String txtTitle = "";
	private int txtReminder = 0;
	private String txtFrom = "";
	private String txtTo = "";
	private int txtDuration = 0;
	private String txtDate = "";
	private String txtPlace = "";
	private String txtDescription = "";
	private String ownerEmail = "";
	private String[] args;
	
	static DefaultListModel listModel = new DefaultListModel();
	
	private static ArrayList<String> emailList = new ArrayList<String>();
	private Person person; 
	
	private static ArrayList<Person> foreveralone = new ArrayList<Person>();
	private static ArrayList<Person> participantList = new ArrayList<Person>();
	ArrayList<Room> rooms;
	
	public MainFrame mainFrame;

	
	public NewAppointment(Person person, MainFrame mainFrame) {
		setLayout(null);
		this.person = person;
		this.mainFrame = mainFrame;
		foreveralone.add(person);
		ownerEmail = person.getEmail();
		
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		//Listener for tab change
		newAppointmentPanel();
		newMeetingPanel();
		updateAppointmentFields();
		tabbedPane.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	        	
	            if (tabbedPane.getSelectedIndex()==1){
	            	getAppointmentText();
	        		tabbedPane.setBounds(0, 0, 680, 350);
	        		AppointmentMain.frame.setSize(680,370);
	        		updateMeetingFields();
	            }
	            else{
	            	getMeetingText();
	        		tabbedPane.setBounds(0, 0, 450, 320);
	        		AppointmentMain.frame.setSize(450,340);
	        		updateAppointmentFields();
	            }
	        }
	    });
		
		rooms = new ArrayList<Room>(DBConnect.getAllRooms());
		for (int i = 0; i < rooms.size(); i++) {
			rooms.get(i).setCalendar(new Calendar(DBConnect.getAllEventsOnRoom(rooms.get(i).getName())));
		}
		
		tabbedPane.setBounds(0, 0, 450, 320);
		add(tabbedPane);
		
		duration.addFocusListener(this);
		to.addFocusListener(this);
		meetingDuration.addFocusListener(this);
		meetingTo.addFocusListener(this);
		from.addFocusListener(this);
		meetingFrom.addFocusListener(this);
		date.addFocusListener(this);
		meetingDate.addFocusListener(this);
		
		capasityComboBox.addItem("All rooms");
		for (int i = 0; i < 50; i += 10){
			capasityComboBox.addItem("" + i + "-" + (i+9));
		}
		capasityComboBox.addItem("50+");
		handleRooms();
	}
	
	public void getAppointmentText() throws NumberFormatException{
		txtTitle = title.getText();
		txtFrom = from.getText();
		txtTo = to.getText();
		txtPlace = place.getText();
		txtDate = date.getText();
		txtDescription = textArea.getText();

		
		try{
			txtReminder = Integer.parseInt(reminder.getText());
		}
		catch (NumberFormatException nfe){
			String[] invalidString = nfe.toString().split("\"");
			if (invalidString.length < 2){
				System.out.println("Empty reminder");
			}
			else{
				System.out.println("\"" + invalidString[1] + "\" is an invalid reminder input. Must be integer.");
			}
			
		}
		try{
			txtDuration = Integer.parseInt(duration.getText());
		}
		catch (NumberFormatException nfe){
			String[] invalidString = nfe.toString().split("\"");
			if (invalidString.length < 2){
				System.out.println("Empty duration time");
			}
			else{
			System.out.println("\"" + invalidString[1] + "\" is an invalid duration input. Must be integer.");
			}
		}
	}
	
	public void getMeetingText() throws NumberFormatException{
		txtTitle = meetingTitle.getText();
		txtFrom = meetingFrom.getText();
		txtTo = meetingTo.getText();
		txtPlace = meetingPlace.getText();
		txtDate = meetingDate.getText();
		txtDescription = meetingTextArea.getText();

		
		try {
			txtReminder = Integer.parseInt(meetingReminder.getText());
			
		}
		catch (NumberFormatException nfe){
			String[] invalidString = nfe.toString().split("\"");
			if (invalidString.length < 2){
				System.out.println("Empty reminder");
			}
			else{
				System.out.println("\"" + invalidString[1] + "\" is an invalid reminder input. Must be integer.");
			}
		}
		try {
			txtDuration = Integer.parseInt(meetingDuration.getText());
		}
		catch (NumberFormatException nfe){
			String[] invalidString = nfe.toString().split("\"");
			if (invalidString.length < 2){
				System.out.println("Empty duration time");
			}
			else{
			System.out.println("\"" + invalidString[1] + "\" is an invalid duration input. Must be integer.");
			}
		}
	}
	
	private void updateMeetingFields(){
		meetingTitle.setText(txtTitle);
		meetingReminder.setText("" + txtReminder);
		meetingFrom.setText(txtFrom);
		meetingTo.setText(txtTo);
		meetingDuration.setText("" + txtDuration);
		meetingPlace.setText(txtPlace);
		meetingDate.setText(txtDate);
		meetingTextArea.setText(txtDescription);
	}
	private void updateAppointmentFields(){
		title.setText(txtTitle);
		reminder.setText("" + txtReminder);
		from.setText(txtFrom);
		to.setText(txtTo);
		duration.setText("" + txtDuration);
		place.setText(txtPlace);
		date.setText(txtDate);
		textArea.setText(txtDescription);
	}
	
	// GUI for meeting tab
	private void newMeetingPanel(){
		JPanel meetingPanel = new JPanel();
		tabbedPane.addTab("Meeting", null, meetingPanel, null);
		meetingPanel.setLayout(null);
		
		meetingTitle = new JTextField();
		meetingTitle.setBounds(21, 21, 134, 28);
		meetingPanel.add(meetingTitle);
		meetingTitle.setColumns(10);
		
		JLabel titleLabel = new JLabel("Title:");
		titleLabel.setBounds(31, 6, 61, 16);
		meetingPanel.add(titleLabel);
		
		JLabel descriptionLabel = new JLabel("Description:");
		descriptionLabel.setBounds(31, 61, 82, 16);
		meetingPanel.add(descriptionLabel);
		
		meetingTextArea = new JTextArea();
		meetingTextArea.setBounds(21, 82, 134, 77);
		meetingPanel.add(meetingTextArea);
		
		meetingReminder = new JTextField();
		meetingReminder.setBounds(25, 220, 67, 23);
		meetingPanel.add(meetingReminder);
		meetingReminder.setColumns(10);
		
		JLabel minBeforeLabel = new JLabel("min. before");
		minBeforeLabel.setBounds(99, 220, 78, 16);
		meetingPanel.add(minBeforeLabel);
		
		JLabel timeLabel = new JLabel("Time:");
		timeLabel.setBounds(218, 6, 61, 16);
		meetingPanel.add(timeLabel);
		
		JLabel fromLabel = new JLabel("from");
		fromLabel.setBounds(199, 28, 39, 16);
		fromLabel.setFont (fromLabel.getFont ().deriveFont (10.0f));
		fromLabel.setForeground(Color.GRAY);
		meetingPanel.add(fromLabel);
		
		JLabel toLabel = new JLabel("to");
		toLabel.setBounds(278, 27, 39, 16);
		toLabel.setFont (toLabel.getFont ().deriveFont (10.0f));
		toLabel.setForeground(Color.GRAY);
		meetingPanel.add(toLabel);
		
		meetingFrom = new JTextField();
		meetingFrom.setBounds(225, 21, 47, 28);
		meetingPanel.add(meetingFrom);
		meetingFrom.setColumns(10);
		
		meetingTo = new JTextField();
		meetingTo.setBounds(291, 21, 47, 28);
		meetingPanel.add(meetingTo);
		meetingTo.setColumns(10);
		
		JLabel exLabel = new JLabel("(ex. 1400)");
		exLabel.setBounds(340, 27, 61, 16);
		exLabel.setFont (exLabel.getFont ().deriveFont (10.0f));
		exLabel.setForeground(Color.gray);
		meetingPanel.add(exLabel);
		
		JLabel durationLabel = new JLabel("Duration:");
		durationLabel.setBounds(218, 61, 61, 16);
		meetingPanel.add(durationLabel);
		
		meetingDuration = new JTextField();
		meetingDuration.setBounds(290, 55, 47, 28);
		meetingDuration.setHorizontalAlignment(JTextField.RIGHT);
		meetingPanel.add(meetingDuration);
		meetingDuration.setColumns(10);
		
		JLabel minLabel = new JLabel("min");
		minLabel.setBounds(338, 61, 24, 16);
		meetingPanel.add(minLabel);
		
		JLabel lblDate = new JLabel("Date:");
		lblDate.setBounds(218, 99, 61, 16);
		meetingPanel.add(lblDate);
		
		meetingDate = new JTextField();
		meetingDate.setBounds(257, 93, 95, 28);
		meetingPanel.add(meetingDate);
		meetingDate.setColumns(10);
		
		JLabel dateExLabel = new JLabel("(dd-mm-yyyy)");
		dateExLabel.setBounds(354, 100, 84, 16);
		dateExLabel.setFont (dateExLabel.getFont ().deriveFont (10.0f));
		dateExLabel.setForeground(Color.gray);
		meetingPanel.add(dateExLabel);
		
		JLabel placeLabel = new JLabel("Place:");
		placeLabel.setBounds(218, 133, 61, 16);
		meetingPanel.add(placeLabel);
		
		meetingPlace = new JTextField();
		meetingPlace.setBounds(257, 127, 134, 28);
		meetingPanel.add(meetingPlace);
		meetingPlace.setColumns(10);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getMeetingText();
				String s = txtPlace.substring(0, txtPlace.indexOf(" |"));
				if(!meetingCheckBox.isSelected())
					txtReminder = -1;
				if(!participantList.contains(foreveralone))
					participantList.add(0, foreveralone.get(0));
				db.DBConnect.addAppointment(txtDate, txtFrom, txtDuration, 1, txtTitle, txtDescription, s, s, participantList.get(0).getEmail(), participantList, txtReminder);
				for(String str : emailList){
					new Mail(str, "Du har blitt invitert til et m¿te", "Tittel:" + txtTitle + "\nDate: " + txtDate + "\nDescription: " + txtDescription);
				}
				AppointmentMain.frame.dispose();
				Frame mainFrame = new MainFrame(person);
				Login.mainFrame.dispose();
				Login.mainFrame = mainFrame;
				mainFrame.setVisible(true);
			}
		});
		btnOk.setBounds(536, 279, 117, 29);
		meetingPanel.add(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AppointmentMain.frame.dispose();
			}
		});
		btnCancel.setBounds(430, 279, 117, 29);
		meetingPanel.add(btnCancel);
		
		roomComboBox = new JComboBox();
		roomComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				meetingPlace.setText("" + roomComboBox.getSelectedItem());
				
			}
		});
		roomComboBox.setBounds(209, 197, 90, 27);
		meetingPanel.add(roomComboBox);
		
		capasityComboBox = new JComboBox();
		capasityComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				handleRooms();
			}
		});
		capasityComboBox.setBounds(311, 197, 100, 27);
		meetingPanel.add(capasityComboBox);
		
		JLabel roomLabel = new JLabel("Room:");
		roomLabel.setBounds(218, 176, 61, 16);
		meetingPanel.add(roomLabel);
		
		btnManageParticipants = new JButton("Manage Participants");
		btnManageParticipants.setBounds(468, 220, 150, 29);
		btnManageParticipants.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*participantFrame = new JFrame();
				participantFrame.setBounds(180, 135, 425, 295);
				gui.ParticipantSelector inviter = new gui.ParticipantSelector();
				
				participantFrame.add(inviter);
				
				participantFrame.setVisible(true);
				participantFrame.setSize(inviter.getSize());
				participantFrame.add(inviter);
				participantFrame.requestFocus();
				*/
				//participantList
				AddNewPersonMain addNew = new AddNewPersonMain();
				addNew.main(participantList);
			}
		});
		meetingPanel.add(btnManageParticipants);
		
		btnInviteViaEmail = new JButton("Invite via email");
		btnInviteViaEmail.setBounds(468, 250, 150, 29);
		btnInviteViaEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				participantFrame = new JFrame();
				participantFrame.setBounds(180, 135, 425, 295);
				gui.EmailInviter inviter = new gui.EmailInviter();
//				ivar.EmailMain main = new ivar.EmailMain();
				participantFrame.getContentPane().add(inviter);
				
//				panel = new EmailInviter();
//				frame = new EmailMain();
				participantFrame.setVisible(true);
				participantFrame.setSize(inviter.getSize());
				participantFrame.getContentPane().add(inviter);
				participantFrame.requestFocus();
			}
		});
		meetingPanel.add(btnInviteViaEmail);
		
		JLabel capasityLabel = new JLabel("Capasity:");
		capasityLabel.setBounds(314, 176, 61, 16);
		meetingPanel.add(capasityLabel);
		
		JList list = new JList(listModel);
		list.setBounds(456, 20, 172, 200);
		meetingPanel.add(list);
		
		meetingCheckBox = new JCheckBox("Reminder");
		meetingCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(meetingCheckBox.isSelected()){
					meetingReminder.setEnabled(true);
				}else{
					meetingReminder.setEnabled(false);
				}
			}
		});
		meetingCheckBox.setBounds(21, 190, 128, 23);
//		meetingCheckBox.set
		meetingPanel.add(meetingCheckBox);
		
		
	}
	
	//GUI for appointment tab
	private void newAppointmentPanel(){
		
		setBounds(0,0,450,320);
		JPanel panel = new JPanel();
		tabbedPane.addTab("Appointment", null, panel, null);
		panel.setLayout(null);
		
		title = new JTextField();
		title.setBounds(21, 21, 134, 28);
		panel.add(title);
		title.setColumns(10);
		
		JLabel titleLabel = new JLabel("Title:");
		titleLabel.setBounds(31, 6, 61, 16);
		panel.add(titleLabel);
		
		JLabel descriptionLabel = new JLabel("Description:");
		descriptionLabel.setBounds(31, 61, 82, 16);
		panel.add(descriptionLabel);
		
		textArea = new JTextArea();
		textArea.setBounds(21, 82, 134, 77);
		panel.add(textArea);
		
		reminder = new JTextField();
		reminder.setBounds(25, 219, 67, 23);
		panel.add(reminder);
		reminder.setColumns(10);
		
		JLabel minBeforeLabel = new JLabel("min. before");
		minBeforeLabel.setBounds(112, 222, 78, 16);
		panel.add(minBeforeLabel);
		
		JLabel timeLabel = new JLabel("Time:");
		timeLabel.setBounds(218, 6, 61, 16);
		panel.add(timeLabel);
		
		JLabel fromLabel = new JLabel("from");
		fromLabel.setBounds(199, 28, 39, 16);
		fromLabel.setFont (fromLabel.getFont ().deriveFont (10.0f));
		fromLabel.setForeground(Color.GRAY);
		panel.add(fromLabel);
		
		JLabel toLabel = new JLabel("to");
		toLabel.setBounds(278, 27, 39, 16);
		toLabel.setFont (toLabel.getFont ().deriveFont (10.0f));
		toLabel.setForeground(Color.GRAY);
		panel.add(toLabel);
		
		from = new JTextField();
		from.setBounds(225, 21, 47, 28);
		panel.add(from);
		from.setColumns(10);
		
		to = new JTextField();
		to.setBounds(291, 21, 47, 28);
		panel.add(to);
		to.setColumns(10);
		
		JLabel exLabel = new JLabel("(ex. 1400)");
		exLabel.setBounds(340, 27, 61, 16);
		exLabel.setFont (exLabel.getFont ().deriveFont (10.0f));
		exLabel.setForeground(Color.gray);
		panel.add(exLabel);
		
		JLabel durationLabel = new JLabel("Duration:");
		durationLabel.setBounds(218, 61, 61, 16);
		panel.add(durationLabel);
		
		duration = new JTextField();
		duration.setBounds(290, 55, 47, 28);
		duration.setHorizontalAlignment(JTextField.RIGHT);
		panel.add(duration);
		duration.setColumns(10);
		
		JLabel minLabel = new JLabel("min");
		minLabel.setBounds(338, 61, 24, 16);
		panel.add(minLabel);
		
		JLabel lblDate = new JLabel("Date:");
		lblDate.setBounds(218, 99, 61, 16);
		panel.add(lblDate);
		
		date = new JTextField();
		date.setBounds(257, 93, 95, 28);
		panel.add(date);
		date.setColumns(10);
		
		JLabel dateExLabel = new JLabel("(dd-mm-yyyy)");
		dateExLabel.setBounds(354, 100, 84, 16);
		dateExLabel.setFont (dateExLabel.getFont ().deriveFont (10.0f));
		dateExLabel.setForeground(Color.gray);
		panel.add(dateExLabel);
		
		JLabel placeLabel = new JLabel("Place:");
		placeLabel.setBounds(218, 133, 61, 16);
		panel.add(placeLabel);
		
		place = new JTextField();
		place.setBounds(257, 127, 134, 28);
		panel.add(place);
		place.setColumns(10);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getAppointmentText();
				if (!chckbxReminder.isSelected())
					txtReminder = -1;
				db.DBConnect.addAppointment(txtDate, txtFrom, txtDuration, 0, txtTitle, txtDescription, txtPlace, "nil", ownerEmail, foreveralone, txtReminder);
				AppointmentMain.frame.dispose();
				Frame mainFrame = new MainFrame(person);
				Login.mainFrame.dispose();
				Login.mainFrame = mainFrame;
				mainFrame.setVisible(true);
				//mainFrame.getCalendar().addEvent(new Appointment(db.DBConnect.getLastEventID(person.getEmail()), txtDate, txtFrom, txtDuration, 
						//txtTitle, txtDescription, txtPlace, txtReminder));
			}
		});
		btnOk.setBounds(306, 219, 117, 29);
		panel.add(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AppointmentMain.frame.dispose();
			}
		});
		btnCancel.setBounds(200, 219, 117, 29);
		panel.add(btnCancel);
		
		chckbxReminder = new JCheckBox("Reminder");
		chckbxReminder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxReminder.isSelected()){
					reminder.setEnabled(true);
				}else{
					reminder.setEnabled(false);
				}
			}
		});
		chckbxReminder.setBounds(21, 192, 128, 23);
		panel.add(chckbxReminder);
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		
	}
	
	

	@Override
	public void focusLost(FocusEvent e) {
		
		if(e.getSource() == date){
			if(Util.valiDate(date.getText())){
				date.setBackground(Color.green);
			}
			else{
				date.setBackground(Color.red);
			}
		}
		
		else if(e.getSource() == meetingDate){
			if(Util.valiDate(meetingDate.getText())){
				meetingDate.setBackground(Color.green);
			}
			else{
				meetingDate.setBackground(Color.red);
			}
		}
		
		if (e.getSource() == duration && from.getText().length() == 4 && Integer.parseInt(duration.getText()) > 0){
			to.setText(timeHandler(from.getText(), duration.getText()));
			to.setBackground(Color.green);
			duration.setBackground(Color.green);
		}
		
		else if (e.getSource() == to && to.getText().length() == 4 && from.getText().length() == 4){
			duration.setText(durationCalculator(from.getText(), to.getText()));
		}
		else if(e.getSource() == meetingDuration && meetingFrom.getText().length() == 4 && Integer.parseInt(meetingDuration.getText()) > 0){
			meetingTo.setText(timeHandler(meetingFrom.getText(), meetingDuration.getText()));
		}
		else if(e.getSource() == meetingTo && meetingTo.getText().length() == 4 && meetingFrom.getText().length() == 4){
			meetingDuration.setText(durationCalculator(meetingFrom.getText(), meetingTo.getText()));
		}
		else if(e.getSource() == from){
			if (!isValidTimeInput(from.getText())){
				from.setBackground(Color.red);
			}
			else{
				from.setBackground(Color.green);
			}
				
		}
		else if (e.getSource() == to && to.getText().length() == 4 && from.getText().length() == 4){
			duration.setText(durationCalculator(from.getText(), to.getText()));
		}
		if (e.getSource() == meetingDuration && meetingFrom.getText().length() == 4 && Integer.parseInt(meetingDuration.getText()) > 0){
			meetingTo.setText(timeHandler(meetingFrom.getText(), meetingDuration.getText()));
			meetingTo.setBackground(Color.green);
			meetingDuration.setBackground(Color.green);
		}

		else if(e.getSource() == meetingTo && Integer.parseInt(meetingTo.getText().substring(2,4)) > 59){
			meetingTo.setBackground(Color.red);
		}
		
		else if(e.getSource() == meetingFrom){
			if (!isValidTimeInput(meetingFrom.getText())){
				meetingFrom.setBackground(Color.red);
			}
			else{
				meetingFrom.setBackground(Color.green);
			}
				
		}
	}
	
	public boolean isValidTimeInput(String s) throws NumberFormatException{
		try{
			if (s.length() == 4 && Integer.parseInt(s.substring(0,2)) < 24 && Integer.parseInt(s.substring(2,4)) < 60 && Integer.parseInt(s.substring(0,2)) > -1 && Integer.parseInt(s.substring(2,4)) > -1){
				return true;
			}
		}
		catch (NumberFormatException nfe){
			System.out.println(nfe);
		}
		return false;
	}
	
	//Calculates duration of meeting
	public String durationCalculator(String from, String to){
		
		int fhrs = 0;
		int fmin = 0;
		int thrs = 0;
		int tmin = 0;
		if (from.length() == 4 && to.length() == 4){
			fhrs = Integer.parseInt(from.substring(0,2));
			fmin = Integer.parseInt(from.substring(2,4));
			thrs = Integer.parseInt(to.substring(0,2));
			tmin = Integer.parseInt(to.substring(2,4));
		}else{
			System.out.println("Not valid time input!");
		}
		int duration = (thrs-fhrs)*60 + tmin - fmin;
		return "" + duration;
	}
	
	//Gives end time
	public String timeHandler(String startTime, String duration){
		int hrs = Integer.parseInt(startTime.substring(0, 2));
		int min = Integer.parseInt(startTime.substring(2,4));
		int dur = Integer.parseInt(duration);
		if(dur/60 > 0){
			hrs += dur/60;
			min += dur%60;
		}
		else{
			min += dur;
		}
			
		String minString = "";
		String hrsString = "";
		minString = "" + min;
		hrsString = "" + hrs;
		if (min < 10){
			minString = "0" + min;
		}
		if (hrs < 10){
			hrsString = "0" + hrs;
		}
		
		return hrsString + minString;
	}
	
	public static void setListWithParticipants(ArrayList<Person>list){
		listModel.clear();
		participantList = list;
		for (int i = 0; i < list.size(); i++){
			listModel.addElement(list.get(i).getGivenName() + " " + list.get(i).getSurname());
		}
		for (int i = 0; i < emailList.size(); i++){
			listModel.addElement(emailList.get(i));
		}
	}
	
	public static void setListWithEmails(ArrayList<String> list){
		
		listModel.clear();
		for (int i = 0; i < participantList.size(); i++){
			listModel.addElement(participantList.get(i).getGivenName() + " " + participantList.get(i).getSurname());
		}
		for(int i = 0; i < list.size(); i++){
			if(!emailList.contains(list.get(i))){				
				emailList.add(list.get(i));
			}
		}
		for (int i = 0; i < emailList.size(); i++){
			listModel.addElement(emailList.get(i));
		}
	}
	
	private void addRoom(int a, int b, ArrayList<Room> rooms){
		roomComboBox.removeAllItems();
		for (int i = 0; i < rooms.size(); i++){
			if(rooms.get(i).getCapasity() < b && rooms.get(i).getCapasity() >= a && rooms.get(i).getCalendar().isAvailable(meetingDate.getText() + " " + meetingFrom.getText(), meetingDate.getText() + " " + meetingTo.getText())){
				roomComboBox.addItem(rooms.get(i).getName() + " |" + rooms.get(i).getCapasity());
			}
		}
	}
	public void addRooms(ArrayList<Room> rooms){
		roomComboBox.removeAllItems();
		for (int i = 0; i < rooms.size(); i++){
			if(rooms.get(i).getCalendar().isAvailable(meetingDate.getText() + " " + meetingFrom.getText(), meetingDate.getText() + " " + meetingTo.getText()))
				roomComboBox.addItem(rooms.get(i).getName() + " |" + rooms.get(i).getCapasity());
		}
	}
	
	private void handleRooms(){
		
		
		
		
		switch(capasityComboBox.getSelectedIndex()){
		case 0:
			addRooms(rooms);
			break;
		case 1:
			addRoom(0,10, rooms);
			break;
		case 2:
			addRoom(10,20, rooms);
			break;
		case 3:
			addRoom(20,30, rooms);
			break;
		case 4:
			addRoom(30,40, rooms);
			break;
		case 5:
			addRoom(40,50, rooms);
			break;
		case 6:
			addRoom(50,10000,rooms);
			break;
		}
	}
}
