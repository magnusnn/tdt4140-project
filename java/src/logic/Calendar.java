package logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Calendar {
	
	private ArrayList<Event> eventList;
	
	public Calendar(ArrayList<Event> eventList) {
		setEventList(eventList);
	}
	
	public void setEventList(ArrayList<Event> eventList) {
		this.eventList = eventList;
	}
	
	public ArrayList<Event> getEventList() {
		return eventList;
	}
	
	public void addEvent(Event event) {
		eventList.add(event);
	}
	
	public void addEvent(Appointment a) {
		eventList.add(a);
	}
	
	public void addEvent(Meeting m) {
		eventList.add(m);
	}
	
	public void deleteEvent(Event event) {
		for (int i = 0; i < eventList.size(); i++) {
			if (event.equals(eventList.get(i))) {
				eventList.remove(i);
				return;
			}
		}
		throw new IllegalArgumentException("Event not found in calendar");
	}
	
	public boolean isAvailable(String startTime, String endTime) {
		try {
			Date start = new SimpleDateFormat("dd-MM-yyyy HHmm").parse(startTime);
			Date end = new SimpleDateFormat("dd-MM-yyyy HHmm").parse(endTime);
			for (int i = 0; i < eventList.size(); i++) {
				Date eventStart = new SimpleDateFormat("dd-MM-yyyy HHmm").parse(eventList.get(i).getDate() + " " + eventList.get(i).getStartTime());
				Date eventEnd = new Date(eventStart.getTime() + eventList.get(i).getDuration());
				if ((eventStart.after(start) && eventStart.before(end)) || (eventEnd.after(start) && eventEnd.before(end)))
					return false;
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}