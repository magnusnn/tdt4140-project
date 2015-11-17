package logic;

import java.util.ArrayList;


public class Person {
	private String givenName;
	private String surname;
	private String dateOfBirth;
	private String email;
	private ArrayList<Notification> notificationList = new ArrayList<Notification>();
	private ArrayList<Reminder> reminderList = new ArrayList<Reminder>();
	private int is_going;
	
			
	public Person(String email, String givenName, String surname, String dateOfBirth, ArrayList<Notification> notificationList,
			ArrayList<Reminder> reminderList, int is_going){
		this.email=email;
		this.givenName = givenName;
		this.surname=surname;
		this.dateOfBirth=dateOfBirth;
		this.notificationList = notificationList;
		this.reminderList = reminderList;
		this.is_going = is_going;
	}
	
	public Person(String email, String givenName, String surname, String dateOfBirth){
		this.email=email;
		this.givenName = givenName;
		this.surname=surname;
		this.dateOfBirth=dateOfBirth;
//		this.is_going = is_going;
	}
	
	public Person(String email, String givenName, String surname, String dateOfBirth, int is_going){
		this.email=email;
		this.givenName = givenName;
		this.surname=surname;
		this.dateOfBirth=dateOfBirth;
		this.is_going = is_going;
	}
	
	public void setGivenName(String givenName){
		this.givenName = givenName;
	}
	
	public void setSurname(String surname){
		this.surname = surname;
	}
	
	public void setDateOfBirth(String dateOfBirth){
		this.dateOfBirth = dateOfBirth;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getGivenName(){
		return givenName;
	}
	public String getSurname(){
		return surname;
	}
	public String getDateOfBirth(){
		return dateOfBirth;
	}
	public String getEmail(){
		return email;
	}
	
	public ArrayList<Notification> getNotificationList(){
		return notificationList;
	}
	
	public void setNotificationList(ArrayList<Notification> list){
		this.notificationList = list;
	}
	
	public ArrayList<Reminder> getReminderList(){
		return reminderList;
	}
	
	public void setReminderList(ArrayList<Reminder> list){
		this.reminderList = list;
	}
	
	public String toString(){
		return "Navn" + getGivenName() + getSurname();
	}
	public int getIsGoing(){
		return is_going;
	}
	public void setIsGoing(int is_going){
		this.is_going = is_going;
	}
	
}
