package gui;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import logic.Person;
import gui.ShowEvent;

public class AttendingListCellRenderer implements ListCellRenderer{
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	
	private ArrayList<Person> participantList = ShowEvent.getMeeting().getPersonList();
	
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
		        isSelected, cellHasFocus);
		
		if(participantList.size() != 0){
			if(participantList.get(index).getIsGoing() == -1){
				renderer.setBackground(Color.red);
			}
			else if(participantList.get(index).getIsGoing() == 0){
				renderer.setBackground(Color.yellow);
			}
			else{
				renderer.setBackground(Color.green);
			}
		}
		return renderer;
	}

}
