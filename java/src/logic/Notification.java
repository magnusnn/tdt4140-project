package logic;


public class Notification {

	private String text;
	private int id;
	private Event event;
	private Person person;
	private boolean isRead;
	
	public Notification(int id, String text, Event event, Person person, int isRead) {
		this.id = id;
		setText(text);
		setEvent(event);
		this.person = person;
		boolean var = false;
		if(isRead == 1){var = true;}
		setIsRead(var);
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}
	
	public String getText() {
		return text;
	}
	
	public Event getEvent() {
		return event;
	}
	
	public boolean getIsRead() {
		return isRead;
	}
	
	public Person getPerson(){
		return this.person;
	}
	
	public void setPerson(Person p){
		this.person = p;
	}
	
	public int getID(){
		return this.id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	
}