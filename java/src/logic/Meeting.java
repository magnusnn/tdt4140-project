package logic;

import java.util.ArrayList;

public class Meeting extends Event{

	private String place;
	private ArrayList<Person> personList = new ArrayList<Person>();
	
	
	public Meeting(int id, String date, String startTime, int duration, String title,
			String description, String place, ArrayList<Person> personList, int reminder, int is_going, int hidden, String owner, String person_email){
		
		super(id, date, startTime, duration, title, description, reminder, is_going, hidden, personList, owner, place, person_email);
		// TODO Auto-generated constructor stub
		this.place = place;
		this.personList = personList;
			}

	

	
	public void setPersonList(ArrayList<Person> personList){
		this.personList=personList;
	}
	
	public ArrayList<Person> getPersonList(){
		return this.personList;
	}
	
	public void addParticipant(Person p){
		personList.add(p);
	}
	
	public void removeParticipant(Person p){
		//loop through ArrayList to find invited persons
		for(int i=0; i<personList.size();i++){ 
			if(personList.get(i)==p){
				personList.remove(i);
			}
		}
	}
	public void cancel(){
		
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	
	
}
