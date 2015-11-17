package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import db.DBConnect;
import logic.Meeting;
import logic.Notification;
import logic.Person;

public class NotiPanel extends JFrame {
	
	private ArrayList<Notification> notiList;
	
	public NotiPanel(ArrayList<Notification> notiList, final Person person){
		//Add notifications to alist
		this.notiList = notiList;

		//Setup backpanel
		setLayout(null);
		setSize(250, 400);
		
		//JPanel som inneholder miniNotiPanels
		JPanel backPanel = new JPanel();
		backPanel.setSize(175, 1000);
		GridLayout g = new GridLayout(0,1);
		backPanel.setLayout(g);
		add(backPanel);
	
		//Lage miniPanels
		int counter = 0;
		for(final Notification n:notiList){
			JPanel miniPanel = new JPanel();
			miniPanel.setLayout(null);
			miniPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
			miniPanel.setBackground(Color.WHITE);
			miniPanel.setBounds(0, 60*counter, 175, 60);
			miniPanel.setPreferredSize(new Dimension(175,60));
			
			JLabel lblDescription = new JLabel("Descrp");
			lblDescription.setBounds(6, 6, 175, 48);
			lblDescription.setText("<html>" + n.getText() + "</html>"); //html tags to make multiple lines possible
			miniPanel.add(lblDescription);
			
			if (!n.getIsRead()) {
				JLabel notRead = new JLabel("<html><font color='red'>&bull;</html>•");
				notRead.setBounds(170, 3, 25, 25);
				miniPanel.add(notRead);
			}
			
			miniPanel.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					try {
						
						MainFrame.showEventFrame = new JFrame();
						ShowInvite showInvite = new ShowInvite((Meeting) n.getEvent(), person);
						MainFrame.showEventFrame.setBounds(0, 0, 600, 310);
						MainFrame.showEventFrame.setLocationRelativeTo(null);
						MainFrame.showEventFrame.getContentPane().add(showInvite);
						MainFrame.showEventFrame.setVisible(true);
					}
					catch (NullPointerException except) {
					}
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			
			//legge til backpanel
			backPanel.add(miniPanel);
			counter++;
			
			n.setIsRead(true);
			DBConnect.changeReadingStatus(n.getID(), 1);
		}
		
		//Legge til JScrollPane
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(backPanel);
		int height = 60;
		if (notiList.isEmpty()) {
			JPanel miniPanel = new JPanel();
			miniPanel.setLayout(null);
			miniPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
			miniPanel.setBackground(Color.WHITE);
			miniPanel.setBounds(0, 0, 175, 60);
			miniPanel.setPreferredSize(new Dimension(175,60));
			
			JLabel lblDescription = new JLabel("Descrp");
			lblDescription.setBounds(6, 6, 175, 48);
			lblDescription.setText("<html>You have no notifications.</html>"); //html tags to make multiple lines possible
			miniPanel.add(lblDescription);
			backPanel.add(miniPanel);
		}
		else {
			height = 300;
			if (notiList.size() < 5)
				height = notiList.size() * 60;
		}
		scroll.setBounds(0, 0, 200, height);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(scroll);
		repaint();
	}
}