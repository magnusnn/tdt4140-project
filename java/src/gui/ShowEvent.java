package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import logic.Appointment;
import logic.Meeting;
import logic.Person;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class ShowEvent extends JPanel {
	private Appointment appointment;
	private static Meeting meeting;
	private Person person;
	private JTextField reminder;
	private JCheckBox chckbxReminder;
	private JTextField btnreminder;
	private JCheckBox btnchckbxReminder;
	public MainFrame mainframe;
	
	private DefaultListModel listModel;
	JList list;
	
	ArrayList<Person> participantList = new ArrayList<Person>();
	/**
	 * @wbp.parser.constructor
	 *
	 */
	public ShowEvent(Appointment event, Person p) {
		this.appointment = event;
		this.person = p;
		setLayout(null);
		showAppointment(event);
//		showMeeting();
		
	}
	
	public ShowEvent(Meeting m, Person p){
		meeting = m;
		this.person = p;
		this.participantList = m.getPersonList();
		
		setLayout(null);
		showMeeting(m);
	}
	
	public void showMeeting(final Meeting m){
		
		//setBounds(0,0,1000,1000);
		
		JLabel lblNewLabel = new JLabel("Description");
		lblNewLabel.setBounds(19, 56, 73, 16);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Date");
		lblNewLabel_1.setBounds(19, 116, 61, 16);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Time");
		lblNewLabel_2.setBounds(19, 146, 61, 16);
		add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Place");
		lblNewLabel_3.setBounds(19, 176, 61, 16);
		add(lblNewLabel_3);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(19, 25, 61, 16);
		lblTitle.setFont (lblTitle.getFont ().deriveFont (16.0f));
		//lblTitle.setForeground(Color.GRAY);
		add(lblTitle);
		
		JLabel title = new JLabel(meeting.getTitle());
		title.setBounds(123, 14, 220, 39);
		setFont (title.getFont().deriveFont(15.0f));
		add(title);
		
		JLabel description = new JLabel("<html>" + meeting.getDescription() + "</html");
		description.setBounds(123, 56, 220, 60);
		description.setForeground(Color.GRAY);
		add(description);
		
		JLabel date = new JLabel(meeting.getDate());
		date.setBounds(123, 116, 99, 16);
		date.setForeground(Color.GRAY);
		add(date);
		
		JLabel time = new JLabel(meeting.getStartTime());
		time.setBounds(123, 146, 99, 16);
		time.setForeground(Color.GRAY);
		add(time);
		
		
		JLabel place = new JLabel(meeting.getPlace());
		place.setBounds(123, 176, 99, 16);
		place.setForeground(Color.GRAY);
		add(place);
		
		btnreminder = new JTextField("");
		btnreminder.setBounds(123, 206, 99, 16);
		btnreminder.setForeground(Color.BLACK);
		add(btnreminder);
		
		listModel = new DefaultListModel();
		for (int i = 0; i<meeting.getPersonList().size(); i++){
			listModel.addElement(meeting.getPersonList().get(i).getGivenName() + " " + meeting.getPersonList().get(i).getSurname());
		}
		list = new JList(listModel);
		
		ListCellRenderer renderer = new AttendingListCellRenderer();
	    list.setCellRenderer(renderer);
		
		list.setBounds(355, 57, 127, 122);
		add(list);
		
		JLabel lblRoom = new JLabel("Room");
		lblRoom.setBounds(305, 206, 61, 16);
		add(lblRoom);
		
		JLabel room = new JLabel(meeting.getPlace()); //Should be getRoom.
		room.setBounds(355, 206, 61, 16);
		add(room);
		
		JButton btnEditEvent = new JButton("Edit event");
		btnEditEvent.setBounds(154, 235, 117, 29);
		btnEditEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainframe.showEventFrame.dispose();
				EditEventMain.main(person, mainframe, m);
			}
		});
		add(btnEditEvent);
		
		JButton btnDeleteEvent = new JButton("Cancel meeting");
		btnDeleteEvent.setBounds(254 + 16, 235, 125, 29);
		btnDeleteEvent.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, 
						   "Are you sure you want to cancel the meeting?",null, JOptionPane.YES_NO_OPTION);
						if(result == JOptionPane.YES_OPTION) {
						    db.DBConnect.deleteAppointment(m);
						    MainFrame.showEventFrame.dispose();
						    try{
						    	AppointmentMain.frame.dispose();
						    }catch(NullPointerException npe){
						    	
						    }
							Frame mainFrame = new MainFrame(person);
							Login.mainFrame.dispose();
							Login.mainFrame = mainFrame;
							mainFrame.setVisible(true);
						    
						} 
			}
		});
		add(btnDeleteEvent);
		
		
		JButton btnCancel = new JButton("OK");
		btnCancel.setBounds(254 + 140, 235, 117, 29);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnchckbxReminder.isSelected()){
					try{						
						db.DBConnect.setReminder(person.getEmail(), m.getID(), Integer.parseInt(btnreminder.getText()));
						m.setReminder(Integer.parseInt(btnreminder.getText()));
					}catch(Exception except){
						except.getMessage();
						m.setReminder(-1);
					}
				}else{
					m.setReminder(-1);
				}
				MainFrame.showEventFrame.dispose();
			}
		});
		add(btnCancel);
		
		btnchckbxReminder = new JCheckBox("Reminder");
		btnchckbxReminder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnchckbxReminder.isSelected()){
					btnreminder.setEnabled(true);
					btnreminder.setText("" + meeting.getReminder());
					btnreminder.setForeground(Color.BLACK);
				}
				else{
					btnreminder.setEnabled(false);
					btnreminder.setText("No reminder");
					btnreminder.setForeground(Color.GRAY);	
				}
			}
		});
		btnchckbxReminder.setBounds(6, 199, 97, 23);
		add(btnchckbxReminder);
		
		
		if(meeting.getReminder() == -1){
			btnreminder.setText("No reminder");
			btnreminder.setEnabled(false);
			btnchckbxReminder.setSelected(false);
		}
		else{
			btnreminder.setText("" + meeting.getReminder());
			btnreminder.setEnabled(true);
			btnchckbxReminder.setSelected(true);
		}
	}
	public void showAppointment(final Appointment a){
		
		setBounds(0,0,461,333);
		
		JLabel lblNewLabel = new JLabel("Description");
		lblNewLabel.setBounds(19, 56, 73, 16);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Date");
		lblNewLabel_1.setBounds(19, 116, 61, 16);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Time");
		lblNewLabel_2.setBounds(19, 146, 61, 16);
		add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Place");
		lblNewLabel_3.setBounds(19, 176, 61, 16);
		add(lblNewLabel_3);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(19, 25, 61, 16);
		lblTitle.setFont (lblTitle.getFont ().deriveFont (16.0f));
		//lblTitle.setForeground(Color.GRAY);
		add(lblTitle);
		
		
		JLabel title = new JLabel(appointment.getTitle());
		title.setBounds(123, 14, 220, 39);
		title.setFont(title.getFont().deriveFont(15.0f));
		add(title);
		
		JLabel description = new JLabel("<html>" + appointment.getDescription() + "</html>");
		description.setVerticalAlignment(SwingConstants.TOP);
		description.setBounds(123, 55, 220, 50);
		description.setForeground(Color.GRAY);
		add(description);
		
		JLabel date = new JLabel(appointment.getDate());
		date.setBounds(123, 116, 99, 16);
		date.setForeground(Color.GRAY);
		add(date);
		
		JLabel time = new JLabel(appointment.getStartTime());
		time.setBounds(123, 146, 99, 16);
		time.setForeground(Color.GRAY);
		add(time);
		
		JLabel place = new JLabel(appointment.getPlace());
		place.setBounds(123, 176, 99, 16);
		place.setForeground(Color.GRAY);
		add(place);
		
		reminder = new JTextField("");
		reminder.setBounds(123, 206, 99, 16);
		reminder.setForeground(Color.BLACK);
		add(reminder);
		
		JButton btnEditEvent = new JButton("Edit event");
		btnEditEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainframe.showEventFrame.dispose();
				EditEventMain.main(person, mainframe, a);
			}
		});
		btnEditEvent.setBounds(154, 240, 117, 29);;
		add(btnEditEvent);
		
		
		JButton btnDeleteEvent = new JButton("Delete appointment");
		btnDeleteEvent.setBounds(254 + 16, 240, 145, 29);
		btnDeleteEvent.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, 
						   "Er du sikker på at du vil slette avtalen?",null, JOptionPane.YES_NO_OPTION);
						if(result == JOptionPane.YES_OPTION) {
						    db.DBConnect.deleteAppointment(a);
						    MainFrame.showEventFrame.dispose();
						    AppointmentMain.frame.dispose();
							Frame mainFrame = new MainFrame(person);
							Login.mainFrame.dispose();
							Login.mainFrame = mainFrame;
							mainFrame.setVisible(true);
						} 
			}
		});
		add(btnDeleteEvent);
		
		
		JButton btnCancel = new JButton("OK");
		btnCancel.setBounds(254 + 160, 240, 117, 29);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxReminder.isSelected()){
					try{						
						db.DBConnect.setReminder(person.getEmail(), a.getID(), Integer.parseInt(reminder.getText()));
						a.setReminder(Integer.parseInt(reminder.getText()));
					}catch(Exception except){
						except.getMessage();
						a.setReminder(-1);
					}
				}else{
					a.setReminder(-1);
				}
				MainFrame.showEventFrame.dispose();
			}
		});
		add(btnCancel);
		
		chckbxReminder = new JCheckBox("Reminder");
		chckbxReminder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxReminder.isSelected()){
					reminder.setEnabled(true);
					reminder.setText("" + meeting.getReminder());
					reminder.setForeground(Color.BLACK);
				}
				else{
					reminder.setEnabled(false);
					reminder.setText("No reminder");
					reminder.setForeground(Color.GRAY);	
				}
			}
		});
		chckbxReminder.setBounds(6, 199, 97, 23);
		add(chckbxReminder);
		
		
		
		if(appointment.getReminder() == -1){
			reminder.setText("No reminder");
			reminder.setEnabled(false);
			chckbxReminder.setSelected(false);
		}
		else{
			reminder.setText("" + appointment.getReminder());
			reminder.setEnabled(true);
			reminder.setForeground(Color.BLACK);
			chckbxReminder.setSelected(true);
		}
		
	}
	public static Meeting getMeeting(){
		return meeting;
	}

	
}
