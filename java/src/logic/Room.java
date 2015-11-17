package logic;

public class Room  {
	private String name;
	private int capasity;
	private int roomId;
	private Calendar calendar;
	
	public Room(String name, int capasity){
		setName(name);
		setCapasity(capasity);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getCapasity() {
		return capasity;
	}
	
	public void setCapasity(int capasity) {
		this.capasity = capasity;
	}
	
	public Calendar getCalendar() {
		return calendar;
	}
	
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
}