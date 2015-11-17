package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.Person;

public class AddNewPersonMain extends JFrame{
	static JFrame frame;
	private JPanel panel; 
	


	public static void main(final ArrayList<Person> participantList){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AddNewPersonMain();
					frame.setVisible(true);
					frame.add(new ParticipantSelector(false, null, participantList));
					frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}