package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import gui.NewAppointment;
import logic.Mail;
import logic.HintTextField;

public class EmailInviter extends JPanel{
	private Mail mailer;
	
	private DefaultListModel listModel;
	
	private final JList recipientJList;
	private JScrollPane recipientScroll;
	
	private HintTextField txtRecipient;
	
	private JTextArea infoText;
	private JTextArea errorText;
	
	private JButton btnAddToRecipients;
	private JButton btnRemove;
	private JButton btnCancel;
	private JButton btnSendInvite;
	
	private static ArrayList<String> recipients;

	/**
	 * Create the panel.
	 */
	public EmailInviter() {
		mailer = new Mail();
		listModel = new DefaultListModel();
		recipients = new ArrayList<String>();
		
		setLayout(null);
		setBounds(0,0,425,275);
		
		txtRecipient = new HintTextField("example@email.com");
		txtRecipient.setBounds(6, 65, 175, 28);
		add(txtRecipient);
		txtRecipient.setColumns(10);
		
		btnAddToRecipients = new JButton("Add to recipients");
		btnAddToRecipients.setBounds(6, 94, 175, 29);
		add(btnAddToRecipients);
		
		infoText = new JTextArea("Add the email of the recipients you want to invite");
		infoText.setLineWrap(true);
		infoText.setWrapStyleWord(true);
		infoText.setEditable(false);
		infoText.setBackground(super.getBackground());
		infoText.setBounds(10, 12, 180, 50);
		add(infoText);
		
		errorText = new JTextArea("");
		errorText.setWrapStyleWord(true);
		errorText.setLineWrap(true);
		errorText.setEditable(false);
		errorText.setBackground(UIManager.getColor("Button.background"));
		errorText.setForeground(Color.red);
		errorText.setBounds(10, 137, 170, 70);
		add(errorText);
		
		recipientJList = new JList(listModel);
		recipientJList.setBounds(190, 12, 220, 171);
		add(recipientJList);
		
		recipientScroll = new JScrollPane(recipientJList);
		recipientScroll.setBounds(190, 12, 207, 171);
		add(recipientScroll);
		
		btnRemove = new JButton("Remove");
		btnRemove.setBounds(276, 185, 125, 29);
		add(btnRemove);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(183, 220, 100, 29);
		add(btnCancel);
		
		btnSendInvite = new JButton("Legge til motagere");
		btnSendInvite.setBounds(276, 220, 125, 29);
		add(btnSendInvite);

		btnAddToRecipients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String recipient = txtRecipient.getText();
				//checks if email recipient is already in list
				if (listModel.contains(recipient)) {
					txtRecipient.setText("");
					txtRecipient.focusLost(null);
					errorText.setText("Recipient already added!");
				//checks if email is valid
				} else if (isValidEmailAddress(recipient)) {
					//email is valid, adds recipient to list
					listModel.addElement(recipient);
					txtRecipient.setText("");
					txtRecipient.focusLost(null);
					errorText.setText("");
				} else {
					//email is not valid
					errorText.setText("Not valid email, try again!");
				}
			}
		});
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					listModel.remove(recipientJList.getSelectedIndex());
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		});
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				NewAppointment.participantFrame.dispose();
			}
		});
		btnSendInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//recipients = new ArrayList<String>();
				int size = listModel.size();
				if (size>0) {
					ArrayList<String> mailList = new ArrayList<String>();
					for (int i = 0; i < size; i++) {
						//String recipient = (String) listModel.getElementAt(i);
						mailList.add((String)listModel.getElementAt(i));
						//new Mail(recipient, "Hear ye! Hear ye!", "Despite relish'd of a base descent, thou arth hereforth and hence the fortunous"
							//	+ " invitee to the Grand Jousting Tournament of Camelot!");
					}
					for (String s : mailList){
						System.out.println("mailList" + s);
					}
					NewAppointment.setListWithEmails(mailList);
					JOptionPane.showMessageDialog(btnSendInvite, "Mailmottakerne har blitt lagt til", null,JOptionPane.PLAIN_MESSAGE);
					NewAppointment.participantFrame.dispose();	
				} else {
					errorText.setText("No recipients!");
				}
			}
		});
	}
	
	//checks if email is valid
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}
}
