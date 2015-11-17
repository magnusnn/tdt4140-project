package gui;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollBar;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gui.AppointmentMain;
import db.DBConnect;
import logic.Group;
import logic.Person;


public class ParticipantSelector extends JPanel implements ActionListener{
	private JPanel panel;
	private JPanel groupPanel;
	
	private JList contactsList;
	private JList groupList;
	private JList addedList;
	private JList groupMemberList;
	
	private JButton addButton;
	private JButton removeButton; 
	private JButton okButton;
	private JButton cancelButton;

	private JTabbedPane groupPane;
	private JPanel contactPanel;
	private JPanel addedPanel; 
	
	private JScrollPane groupScroll;
	private JScrollPane contactScroll;
	private JScrollPane addedScroll;
	private JScrollPane memberScroll;
	
	
	private JLabel addedLabel;
	
	private DefaultListModel defaultListPerson;
	private DefaultListModel defaultListGroup;
	private DefaultListModel defaultListAdded;
	private DefaultListModel defaultListMembersGroup;
	
	private ArrayList<Person> addedPeople;
	private ArrayList<Person> groupMember; 
	private ArrayList<Person> personList;
	private ArrayList<Person> tempADDED;
	
	private int valgt;
	private int selectedAddedForRemove;
	
	private final boolean showCalendars;
	private JFrame frame;
	private MainFrame mainFrame;
	
	private Person owner = gui.MainFrame.getPerson();
	
	public ParticipantSelector(final boolean showCalendars, MainFrame mainFrame, ArrayList<Person> participantList) {
		tempADDED = addedPeople;
		this.mainFrame = mainFrame;
		if (showCalendars)
			frame = MainFrame.participantFrame;
		else
			frame = AddNewPersonMain.frame;
		this.showCalendars = showCalendars;
		addedPeople = new ArrayList<logic.Person>();
		
		defaultListPerson = new DefaultListModel();
		defaultListGroup = new DefaultListModel();
		defaultListAdded = new DefaultListModel();
		defaultListMembersGroup = new DefaultListModel();
		
		addedPeople = participantList;
		if (addedPeople.size() > 0){
			for(Person p : addedPeople){
				defaultListAdded.addElement(p.getGivenName() + " " + p.getSurname());
			}
		}
		
		setLayout(null);
		
		addButton = new JButton(">>");
		addButton.setBounds(203, 103, 56, 23);
		addButton.addActionListener(this);
		addButton.setActionCommand("Add");
		add(addButton);
		
		removeButton = new JButton("<<");
		removeButton.setBounds(203, 138, 56, 23);
		removeButton.addActionListener(this);
		removeButton.setActionCommand("Remove");
		add(removeButton);
		
		okButton = new JButton("OK");
		okButton.setBounds(327, 271, 117, 29);
		okButton.addActionListener(this);
		okButton.setActionCommand("Ok");
		add(okButton);
		
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(221, 271, 117, 29);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		add(cancelButton);

		
		groupPane = new JTabbedPane(JTabbedPane.TOP);
		groupPane.setBounds(19, 34, 182, 228);	
		groupPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				 if (groupPane.getSelectedIndex()==1){
					 frame.setSize(680,368);
					 addedPanel.setBounds(475, 69, 154, 188);
					 addedList.setBounds(6, 6, 127, 176);
					 removeButton.setBounds(420, 138, 56, 23);
					 addButton.setBounds(420, 103, 56, 23);
					 
					 groupMemberList = new JList(defaultListMembersGroup);
					 groupMemberList.setLayoutOrientation(JList.VERTICAL);
					 groupMemberList.setBackground(Color.WHITE);
					 groupMemberList.setBounds(220, 69, 146, 170);
					 add(groupMemberList);
					 
					valgt = 1; 
				 }else{
					 try{
						 remove(groupMemberList);
						 addedPanel.setBounds(271, 69, 154, 188);
						 addedList.setBounds(6, 6, 127, 176);
						 removeButton.setBounds(203, 138, 56, 23);
						 addButton.setBounds(203, 103, 56, 23);
					 }
					 catch (NullPointerException npe){
						 System.out.println("Remove List");
					 }
					 
					 
					 
					 valgt = 0;
					 frame.setSize(465,368);
				 }
			}});


		contactPanel = new JPanel();
		groupPane.addTab("Contacts", null, contactPanel, null);
		contactPanel.setLayout(null);
		add(groupPane);
		
		contactsList = new JList(defaultListPerson);
		personList = DBConnect.getAllPersonList();
			for (Person p : personList) {
			defaultListPerson.addElement(p.getGivenName() + " " + p.getSurname());
		}
		contactsList.setLayoutOrientation(JList.VERTICAL);
		contactsList.setBackground(Color.WHITE);
		contactsList.setBounds(6, 6, 149, 170);
		contactPanel.add(contactsList);
		
		contactScroll = new JScrollPane(contactsList);
		contactScroll.setBounds(6, 6, 149, 170);
		contactPanel.add(contactScroll);
		
		groupPanel = new JPanel();
		groupPane.addTab("Group", null, groupPanel, null);
		groupPanel.setLayout(null);
		groupPanel.setBounds(5,7,134,171);
		
		groupList = new JList(defaultListGroup);
		ArrayList<Group> personGroupList = DBConnect.getGroups();
		for (Group g : personGroupList){
			defaultListGroup.addElement(g.getName());
		}
		
		groupList.setLayoutOrientation(JList.VERTICAL);
		groupList.setBackground(Color.WHITE);
		groupList.setBounds(6, 6, 133, 181);
		
		groupList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
		        if (evt.getClickCount() == 1) {
		        	defaultListMembersGroup.removeAllElements();
		            int index = list.locationToIndex(evt.getPoint());
		            try{
		            	groupMember = DBConnect.getGroupMembers(index + 1);
		            }
		            catch (NullPointerException npe){
		            	System.out.println("Try again");
		            }
		            defaultListMembersGroup.addElement("Select all");
				 	for (Person m : groupMember){
				 		defaultListMembersGroup.addElement(m.getGivenName() + " " + m.getSurname());
				 	}
		        } 
		    }
		});
		
		groupPanel.add(groupList);
		
		groupScroll = new JScrollPane(groupList);
		groupScroll.setBounds(6, 6, 149, 170);
		groupPanel.add(groupScroll);
		
		addedPanel = new JPanel();
		addedPanel.setForeground(Color.WHITE);
		addedPanel.setBounds(271, 69, 154, 188);
		addedPanel.setLayout(null);
		add(addedPanel);
		
		addedList = new JList(defaultListAdded);
		addedList.setForeground(Color.BLACK);
		addedList.setBounds(6, 6, 127, 176);
		addedList.setBackground(Color.WHITE);
		
		addedList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
		        selectedAddedForRemove = list.getSelectedIndex();
		    }
		});
		
		addedPanel.add(addedList);
		
		
		addedScroll = new JScrollPane (addedList);
		addedScroll.setBounds(10, 6, 138, 176);
		addedPanel.add(addedScroll);
		
		addedLabel = new JLabel("Added");
		addedLabel.setBounds(271, 41, 61, 16);
		add(addedLabel);
	}


	@Override
	public void actionPerformed(ActionEvent evt) {
		// addButton
		defaultListAdded.removeAllElements();
		
		for (Person m : addedPeople){
			defaultListAdded.addElement(m.getGivenName() + " " + m.getSurname());
			
		}
		
		if(evt.getActionCommand().equals("Add")){
			
			if (valgt == 1){
				ArrayList<logic.Person> recentlyAdded = new ArrayList<logic.Person>(); 
				if(groupMemberList.getSelectedValue() == "Select all"){
					
					for(Person m : groupMember){
						boolean alreadyAdded = false;
						for(Person a : addedPeople){
							if(a.getEmail().equals(m.getEmail()) || m.getEmail().equals(owner.getEmail())){
								System.out.println("Already added");
								alreadyAdded = true;
							}
						}
						if(!alreadyAdded){
							addedPeople.add(m);
							recentlyAdded.add(m);
						}
					}
				}
				
				else if(!addedPeople.contains(groupMember.get(groupMemberList.getSelectedIndex()-1)) || !personList.get(contactsList.getSelectedIndex()).getEmail().equals(owner.getEmail())){
					addedPeople.add(groupMember.get(groupMemberList.getSelectedIndex()-1));
				}
				for(Person m : recentlyAdded){
					defaultListAdded.addElement(m.getGivenName() + " " + m.getSurname());
				}
			}

			else if(valgt == 0){
				String selected = (String) contactsList.getSelectedValue();
				if (defaultListAdded.contains(selected) || personList.get(contactsList.getSelectedIndex()).getEmail().equals(owner.getEmail())){
					System.out.println ("du har allerede lagt til denne personen");
				}else{
					addedPeople.add(personList.get(contactsList.getSelectedIndex()));
					defaultListAdded.addElement(addedPeople.get(addedPeople.size()-1).getGivenName() + " " + addedPeople.get(addedPeople.size()-1).getSurname());
				}
			}
		}
		//remove button
		else if(evt.getActionCommand().equals("Remove")){
			
			
			if(selectedAddedForRemove == -1){
				System.out.println("No person selected");
			}
			else if (addedPeople.size() > 0){
				
				addedPeople.remove(selectedAddedForRemove);
				defaultListAdded.removeAllElements();
				for (Person m : addedPeople){
					defaultListAdded.addElement(m.getGivenName() + " " + m.getSurname());
				}
			}
			selectedAddedForRemove = -1;
		}
		
		//ok button
		else if(evt.getActionCommand().equals("Ok")){
			if (showCalendars) {
				mainFrame.setOtherParticipant(addedPeople.get(0));
				mainFrame.showOthers();
			}
			else{
				NewAppointment.setListWithParticipants(addedPeople);
				EditEvent.setListWithParticipants(addedPeople);
			}
			frame.dispose();
		}
		//cancel button
		else if(evt.getActionCommand().equals("Cancel")){
			frame.dispose();	
			addedPeople = tempADDED;
		}		
			
	}
	
}
