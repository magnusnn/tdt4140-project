package logic;

import java.util.ArrayList;

public class Appointment extends Event{
	private String place;


	public Appointment(int id, String date, String startTime, int duration, String title,
			String description, String place, int reminder, int is_going, int hidden, ArrayList<Person> personList, String owner, String person_email) {
		super(id ,date, startTime, duration, title, description, reminder, is_going, hidden, personList, owner, place, person_email);
		// TODO Auto-generated constructor stub
		this.place = place;
	}

	
	public void setPlace(String place){
		this.place=place;
	}
	
	public String getPlace(){
		return place;
	}

}
