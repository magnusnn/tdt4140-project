package gui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import logic.Meeting;
import logic.Person;

import db.DBConnect;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class ShowInvite extends JPanel {
	private static Meeting meeting;
	private Person person;
	private int going = 0;
	private DefaultListModel listModel;
	private JCheckBox chckbxReminder;
	private JTextField reminder;
	/**
 *
 */
	
	public ShowInvite(Meeting m, Person p){
		meeting = m;
		this.person = p;
		going = meeting.getIsGoing();
		setLayout(null);
		showMeeting();
		
	}
	
	public void showMeeting(){
		
		setBounds(0,0,500,260);
		
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
		title.setBounds(123, 25, 150, 16);
		title.setForeground(Color.GRAY);
		add(title);
		
		JLabel description = new JLabel("<html>" + meeting.getDescription() + "</html");
		description.setVerticalAlignment(SwingConstants.TOP);
		description.setBounds(123, 56, 123, 60);
		description.setForeground(Color.GRAY);
		add(description);
		
		JLabel date = new JLabel(meeting.getDate());
		date.setBounds(123, 116, 150, 16);
		date.setForeground(Color.GRAY);
		add(date);
		
		JLabel time = new JLabel(meeting.getStartTime());
		time.setBounds(123, 146, 61, 16);
		time.setForeground(Color.GRAY);
		add(time);
		
		JLabel place = new JLabel(meeting.getPlace());
		place.setBounds(123, 176, 150, 16);
		place.setForeground(Color.GRAY);
		add(place);
		
		reminder = new JTextField();
		reminder.setBounds(123, 206, 110, 16);
		reminder.setForeground(Color.BLACK);
		add(reminder);
		
		
		listModel = new DefaultListModel();
		for (int i = 0; i<meeting.getPersonList().size(); i++){
			listModel.addElement(meeting.getPersonList().get(i).getGivenName() + " " + meeting.getPersonList().get(i).getSurname());
		}
		JList list = new JList(listModel);
		
		ListCellRenderer renderer = new AttendingInviteListCellRenderer();
	    list.setCellRenderer(renderer);
		
		list.setBounds(258, 26, 127, 122);
		add(list);
		
		JLabel lblRoom = new JLabel("Room");
		lblRoom.setBounds(258, 161, 61, 16);
		add(lblRoom);
		
		JLabel room = new JLabel(meeting.getPlace()); //Should be getRoom.
		room.setBounds(307, 160, 61, 16);
		add(room);
		
		ButtonGroup bg = new ButtonGroup();
		
		
		final JRadioButton rdbtnGoing = new JRadioButton("Going");
		rdbtnGoing.setBounds(19, 234, 73, 23);
		if(going == 1) rdbtnGoing.setSelected(true);
		bg.add(rdbtnGoing);
		add(rdbtnGoing);
		
		final JRadioButton rdbtnNotGoing = new JRadioButton("Not going");
		rdbtnNotGoing.setBounds(89, 234, 95, 23);
		if(going == -1) rdbtnNotGoing.setSelected(true);
		bg.add(rdbtnNotGoing);
		add(rdbtnNotGoing);
		
		JButton btnRespondToInvite = new JButton("Respond to invite");
		btnRespondToInvite.setBounds(337, 231, 163, 29);
		btnRespondToInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setReminderLoca();
				if(rdbtnGoing.isSelected()){
					DBConnect.changeStatus(1, person.getEmail(), meeting.getID(), meeting.getHidden(), meeting.getReminder());
					meeting.setIsGoing(1);
					meeting.setHidden(0);
				}
				else if(rdbtnNotGoing.isSelected()){
					DBConnect.changeStatus(-1, person.getEmail(), meeting.getID(), meeting.getHidden(), meeting.getReminder());
					meeting.setIsGoing(-1);
				}
				MainFrame.showEventFrame.dispose();
				Frame mainFrame = new MainFrame(person);
				Login.mainFrame.dispose();
				Login.mainFrame = mainFrame;
				mainFrame.setVisible(true);
			
			}

			private void setReminderLoca() {
				if(chckbxReminder.isSelected() && reminder.getText().length() > 0){
					try{
						meeting.setReminder(Integer.parseInt(reminder.getText()));
					}catch(Exception ex){
						meeting.setReminder(-1);
						ex.getMessage();
						}
					
				}else{ 
					meeting.setReminder(-1);
				
				}
				
			}
		});
		add(btnRespondToInvite);
		
		JButton btnCancel = new JButton("OK");
		btnCancel.setBounds(226, 231, 117, 29);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxReminder.isSelected()){
					try{						
						db.DBConnect.setReminder(person.getEmail(), meeting.getID(), Integer.parseInt(reminder.getText()));
						meeting.setReminder(Integer.parseInt(reminder.getText()));
						
					}catch(Exception except){
						except.getMessage();
					}
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
		chckbxReminder.setBounds(6, 203, 97, 23);
		
		add(chckbxReminder);
		
		if(meeting.getReminder() == -1){
			reminder.setText("No reminder");
			reminder.setEnabled(false);
			chckbxReminder.setSelected(false);
		}
		else{
			reminder.setText("" + meeting.getReminder());
			reminder.setEnabled(true);
			chckbxReminder.setSelected(true);
		}
			
		
	}

	
	public static Meeting getMeeting(){
		return meeting;
	}
}
