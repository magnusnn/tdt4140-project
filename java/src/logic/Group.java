package logic;

import java.util.ArrayList;

public class Group {

	private int groupID;
	private String name;
	private ArrayList<Person> personList;
	
	public Group(int groupID, ArrayList<Person> personList, String name) {
		setGroupID(groupID);
		setPersonList(personList);
		setName(name);
	}
	
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	
	public void setPersonList(ArrayList<Person> personList) {
		this.personList = personList;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getGroupID() {
		return groupID;
	}
	
	public ArrayList<Person> getPersonList() {
		return personList;
	}
	
	public String getName() {
		return name;
	}
}