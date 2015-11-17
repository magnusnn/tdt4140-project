package logic;


public class Reminder {

	private int reminder;
	private Event event;
	
	public Reminder(int reminder, Event event) {
		setReminder(reminder);
		setEvent(event);
	}
	
	public void setReminder(int reminder) {
			this.reminder = reminder;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public int getReminder() {
		return reminder;
	}
	
	public Event getEvent() {
		return event;
	}
	
	public void fire() {
		//add a new reminder frame
	}
}