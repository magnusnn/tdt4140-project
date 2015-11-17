package gui;

import javax.swing.JOptionPane;

import logic.Reminder;

public class FireReminder {

	public FireReminder(Reminder reminder) {
		JOptionPane.showConfirmDialog(null, "Reminder: " + reminder.getEvent().getTitle() + ", " + reminder.getEvent().getStartTime(), null, JOptionPane.YES_NO_OPTION);
	}
}