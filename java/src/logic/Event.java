package logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Event implements Comparable<Event>{
	private int id;
	private String date;
	private String startTime;
	private int duration;
	private String title;
	private String description;
	private int reminder;
	private int is_going;
	private int hidden;
	private ArrayList<Person> personList;
	private String owner;
	private String place;
	private String person_email;
	
	//SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	//SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
	
	public Event(int id, String date, String startTime, int duration, String title, String description, int reminder, int is_going, 
			int hidden, ArrayList<Person> personList, String owner, String place, String person_email){
		this.id = id;
		this.date = date;
		this.startTime = startTime;
		this.duration = duration;
		this.title = title;
		this.description = description;
		this.reminder = reminder;
		this.is_going = is_going;
		this.hidden = hidden;
		this.personList = personList;
		this.owner = owner;
		this.place = place;
		this.person_email = person_email;
	}
	
	public int getID(){
		return this.id;
	}
	public void setID(int id){
		this.id = id;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getReminder(){
		return reminder;
	}
	public void setReminder(int r){
		this.reminder = r;
	}
	public int getIsGoing(){
		return this.is_going;
	}
	public void setIsGoing(int going){
		this.is_going = going;
	}
	public int getHidden(){
		return this.hidden;
	}
	public void setHidden(int h){
		this.hidden = h;
	}
	public String getOwner(){
		return this.owner;
	}
	public void setOwner(String var){
		this.owner = var;
	}
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	public ArrayList<Person> getPersonList(){
		return this.personList;
	}
	public String getPersonEmail(){
		return this.person_email;
	}

	@Override
	public int compareTo(Event other) {
		try {
			Date time = new SimpleDateFormat("dd-MM-yyyy HHmm").parse(getDate() + " " + getStartTime());
			Date otherTime = new SimpleDateFormat("dd-MM-yyyy HHmm").parse(other.getDate() + " " + other.getStartTime());
			if (time.compareTo(otherTime) != 0)
				return time.compareTo(otherTime);
			return (id - other.getID());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public String toString() {
		return Integer.toString(id);
	}
}