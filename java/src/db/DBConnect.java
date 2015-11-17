package db;

import gui.MainFrame;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import logic.Appointment;
import logic.Event;
import logic.Group;
import logic.Meeting;
import logic.Notification;
import logic.Person;
import logic.Reminder;
import logic.Room;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;


public class DBConnect {


	private static final String CONNECTION_STRING = "jdbc:mysql://mysql.stud.ntnu.no/magnne_fellesprosjekt";
	private static final String USERNAME = "magnne_fellespro";
	private static final String PASSWORD = "fellesprosjekt";


	private static Connection conn;
	
	public static String ValidateUser(String email, String password){
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT email FROM PERSON WHERE email=? AND password=?";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);

			ps.setString(1, email);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()){
				return rs.getString("email");
			}else{
				return null;
			}
			
		}catch (Exception e){
			System.out.println(e.getMessage());
			return "" + e.getStackTrace();
		}finally{
			try {
				conn.close();
			}catch(Exception e){}
		}
	}

	public static Person getPerson(String email){
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT * FROM PERSON WHERE email=?";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, email);

			ResultSet rs = ps.executeQuery();

			if (rs.next()){
				try{
					Person person = new Person(rs.getString("email"), rs.getString("given_name"), rs.getString("surname"), 
							rs.getString("date_of_birth")) {};
							return person;

				}catch(Exception e){
					System.out.println(e.getMessage());
					return null;
				}

			}else{
				System.out.println("ERROR");
				return null;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}
		}
	}


	public static ArrayList<Person> getGroupMembers(int groupID){
		ArrayList<Person> groupMembers = new ArrayList<Person>();
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT email, given_name, surname, date_of_birth FROM MEMBER_OF, PERSON WHERE group_id=? " +
					"AND MEMBER_OF.person_email=PERSON.email;";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setLong(1, groupID);

			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				try{
					Person person = new Person(rs.getString("email"), rs.getString("given_name"), rs.getString("surname"),
							rs.getString("date_of_birth")) {};
							groupMembers.add(person);
				}catch(Exception e){
					System.out.println(e.getMessage());
					return null;
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}


		}
		return groupMembers;
	}


	public static ArrayList<Group> getGroups(){
		ArrayList<Group> groups = new ArrayList<Group>();
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT id, name FROM GROUPS";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList<Person> memberList;

			int count = 1;

			Group group;
			while (rs.next()){
				memberList = getGroupMembers(count);
				group = new Group(rs.getInt("id"), memberList, rs.getString("name"));
				groups.add(group);
				count++;	
			}

		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}

		}
		return groups;
	}

	public static ArrayList<Person> getAllInvitedPersons(int appointment_id){
		ArrayList<Person> invitedPersons  = new ArrayList<Person>();
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT person_email, is_going FROM INVITED_TO WHERE appointment_id=?";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, appointment_id);
			ResultSet rs = ps.executeQuery();
			Person person;
			while (rs.next()){
				person = getPerson(rs.getString("person_email"));
				person.setIsGoing(rs.getInt("is_going"));
				invitedPersons.add(person);
			}

		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}

		}
		return invitedPersons;
	}


	public static Event getEvent(int event_id){
		Event event = null;
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT DISTINCT(`id`), `day`, `start_time`, `duration`, `public`, `title`, `description`, " +
					"`place`, `room_name`, `owner_email`, `reminder`, `hidden`, `is_going` FROM APPOINTMENT, INVITED_TO " +
					"WHERE appointment_id = ? AND APPOINTMENT.id=appointment_id";

			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, event_id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()){
				ArrayList<Person> person = new ArrayList<Person>();
				int appID = rs.getInt("id");
				if(rs.getInt("public") == 1){
					person = getAllInvitedPersons(appID);
					Event meeting = new Meeting(appID, rs.getString("day"), rs.getString("start_time"), rs.getInt("duration"), 
							rs.getString("title"), rs.getString("description"), rs.getString("place"), person, rs.getInt("reminder"), 
							rs.getInt("is_going"), rs.getInt("hidden"),rs.getString("owner_email"), MainFrame.getPerson().getEmail());
					return meeting;
				}else{
					person.add(MainFrame.getPerson());
					Event appoint = new Appointment(appID, rs.getString("day"), rs.getString("start_time"), rs.getInt("duration"), 
							rs.getString("title"), rs.getString("description"), rs.getString("place"), rs.getInt("reminder"),
							rs.getInt("is_going"), rs.getInt("hidden"), person, rs.getString("owner_email"), MainFrame.getPerson().getEmail());
					return appoint;
				}
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}

		}
		return event;		
	}


	public static ArrayList<Reminder> getReminder(Person person){
		ArrayList<Reminder> reminders = new ArrayList<Reminder>();
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT person_email, appointment_id, reminder FROM INVITED_TO  WHERE person_email=?";

			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, person.getEmail());
			ResultSet rs = ps.executeQuery();
			ArrayList<Event> events = getAllEventsOnUser(person.getEmail());
			int counter = 0;
			Reminder reminder;
			while(rs.next()){
				reminder = new Reminder(rs.getInt("reminder"), events.get(counter));
				reminders.add(reminder);
				counter ++;
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}

		}return reminders;

	}



	public static ArrayList<Notification> getNotificationList(Person person){
		ArrayList<Notification> notifications = new ArrayList<Notification>();
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT id, message, time, day, appointment_id, is_read FROM NOTIFICATION, INVITED_TO " +
					"WHERE person_email=? AND NOTIFICATION.id=INVITED_TO.notification_id" + 
					" ORDER BY id DESC LIMIT 10";

			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, person.getEmail());
			ResultSet rs = ps.executeQuery();
			Notification notify;

			while(rs.next()){
				notify = new Notification(rs.getInt("id"), rs.getString("message"), getEvent(rs.getInt("appointment_id")), person, rs.getInt("is_read"));
				notifications.add(notify);
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}

		}return notifications;

	}


	public static ArrayList<Event> getAllEventsOnRoom(String room_name){
		ArrayList<Event> events = new ArrayList<Event>();
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT DISTINCT(`id`), `day`, `start_time`, `duration`, `public`, `title`, `description`, `place`, " +
					"`room_name`, `owner_email` FROM APPOINTMENT  WHERE room_name = ?";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, room_name);
			ResultSet rs = ps.executeQuery();

			Meeting meeting;
			ArrayList<Person> person;
			while (rs.next()){
				person = new ArrayList<Person>();
				int appID = rs.getInt("id");
				person = getAllInvitedPersons(appID);
				meeting = new Meeting(appID, rs.getString("day"), rs.getString("start_time"), rs.getInt("duration"), 
						rs.getString("title"), rs.getString("description"), rs.getString("place"), person, -1, 1, 0, rs.getString("owner_email"), null);
				events.add(meeting);
				

			}		
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}
		}
		return events;
	}



	public static ArrayList<Event> getAllEventsOnUser(String person_email){
		ArrayList<Event> appointments = new ArrayList<Event>();
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT DISTINCT(`id`), `day`, `start_time`, `duration`, `public`, `title`, `description`, `place`, `room_name`, " +
					"`owner_email`, `reminder`, `hidden`, `is_going`, `owner_email` FROM APPOINTMENT, INVITED_TO WHERE INVITED_TO.person_email=? AND " +
					"INVITED_TO.appointment_id=id";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, person_email);
			ResultSet rs = ps.executeQuery();

			Appointment appoint;
			Meeting meeting;
			ArrayList<Person> person;
			while (rs.next()){
				person = new ArrayList<Person>();
				int appID = rs.getInt("id");
				if(rs.getInt("public") == 1){
					person = getAllInvitedPersons(appID);

					meeting = new Meeting(appID, rs.getString("day"), rs.getString("start_time"), rs.getInt("duration"), 
							rs.getString("title"), rs.getString("description"), rs.getString("place"), person, rs.getInt("reminder"),
							rs.getInt("is_going"), rs.getInt("hidden"), rs.getString("owner_email"), person_email);
					appointments.add(meeting);

				}else{
					person.add(MainFrame.getPerson());
					appoint = new Appointment(appID, rs.getString("day"), rs.getString("start_time"), rs.getInt("duration"), 
							rs.getString("title"), rs.getString("description"), rs.getString("place"), rs.getInt("reminder"), 
							rs.getInt("is_going"), rs.getInt("hidden"), person, rs.getString("owner_email"), person_email);
					appointments.add(appoint);
				}

			}		
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}
		}
		return appointments;
	}

	public static ArrayList<Person> getAllPersonList(){
		ArrayList<Person> personList = new ArrayList<Person>();
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT * FROM PERSON";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			Person person;
			while (rs.next()){				
				person = new Person(rs.getString("email"), rs.getString("given_name"), rs.getString("surname"),
						rs.getString("date_of_birth")) {};
						personList.add(person);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}
		}
		return personList;
	}


	public static boolean addAppointment(String day, String start_time, int duration, int public_variable, String title, 
			String description, String place, String room_name, String owner_email, ArrayList<Person> invited_persons, int reminder){
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "INSERT INTO APPOINTMENT(day, start_time, duration, public, title, description, place, room_name , owner_email) " +
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);

			ps.setString(1, day);
			ps.setString(2, start_time);
			ps.setInt(3, duration);
			ps.setInt(4, public_variable);
			ps.setString(5, title);
			ps.setString(6, description);
			ps.setString(7, place);
			ps.setString(8, room_name);
			ps.setString(9, owner_email);

			ps.execute();

			String todate = new java.text.SimpleDateFormat("dd-MM-yyyy").format(new Date());
			String time = new java.text.SimpleDateFormat("HHmm").format(new Date());
			String message = "M�teinvitasjon: " + title + " den " + day +" klokken " + start_time;

			invitePersons(invited_persons, owner_email, message, time, todate);
			if(reminder != -1){
				changeStatus(1 , owner_email, getLastEventID(owner_email), 0, reminder);
			}else{
				changeStatus(1 , owner_email, getLastEventID(owner_email), 0, -1);
			}


		}catch (Exception e){
			System.out.println(e.getMessage());
			return false;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}

		}

		return true;
	}

	public static void changeReadingStatus(int notID, int reading_status){
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "UPDATE NOTIFICATION SET is_read=" + reading_status + " WHERE id=" + notID;
			conn.prepareStatement(sql).execute();

		}catch (Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				conn.close();
			}catch(Exception e){}

		}
	}

	public static void changeStatus(int is_going, String email, int appID, int hidden, int reminder){
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);

			String sql = "UPDATE INVITED_TO SET reminder=? , is_going=? , hidden=? " + 
					" WHERE appointment_id=" + appID + " AND person_email='" + email + "'";

			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, reminder);
			ps.setInt(2, is_going);
			ps.setInt(3, hidden);
			ps.executeUpdate();

		}catch (Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				conn.close();
			}catch(Exception e){}

		}

	}


	public static int isGoing(String email, int appID){
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT is_going FROM INVITED_TO WHERE person_email=? AND appointment_id=?";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);

			ps.setString(1, email);
			ps.setInt(2, appID);

			ResultSet rs = ps.executeQuery();

			if(rs.next()) return rs.getInt("is_going");

		}catch (Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				conn.close();
			}catch(Exception e){}

		}

		return -9;
	}



	public static int getLastEventID(String owner_email){
		int appID = 0;
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT id FROM APPOINTMENT WHERE owner_email='" + owner_email +  "'" +
					" ORDER BY id DESC LIMIT 1";
			ResultSet rs = conn.createStatement().executeQuery(sql);
			if(rs.next()){
				appID = rs.getInt("id");				
			}

		}catch (Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				conn.close();
			}catch(Exception e){}
		}
		return appID;
	}


	private static void invitePersons(ArrayList<Person> invited_persons, String owner_email, String message, String time, String todate) {
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			int appID = getLastEventID(owner_email);

			String sql = "INSERT INTO INVITED_TO (person_email, appointment_id, notification_id) VALUES (?,?,?)";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			for (int i = 0; i < invited_persons.size(); i++) {
				ps.setString(1, invited_persons.get(i).getEmail());
				ps.setInt(2, appID);
				if(!invited_persons.get(i).getEmail().equals(owner_email)){
					addNotification(message, time, todate);
					ps.setInt(3, getLastNotification());					
				}else{
					ps.setNull(3, Types.INTEGER);
				}
				ps.execute();
			}

		}catch (Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				conn.close();
			}catch(Exception e){}
		}
	}


	public static ArrayList<Room> getAllRooms(){
		ArrayList<Room> rooms = new ArrayList<Room>();
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT name, capasity FROM ROOM";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			Room room;
			while (rs.next()){
				if(rs.getInt("capasity") == 0){
				}
				else{
					room = new Room(rs.getString("name"), rs.getInt("capasity")) {};		
					rooms.add(room);					
				}

			}

		}catch (Exception e){
			return null;
		}finally{
			try{
				conn.close();
			}catch(Exception e){}

		}
		return rooms;

	}


	public static void addNotification(String message, String time, String day){
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "INSERT INTO NOTIFICATION(message, time, day) " +
					"VALUES(?,?,?)";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, message);
			ps.setString(2, time);
			ps.setString(3, day);

			ps.execute();

		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				conn.close();
			}catch(Exception e){e.getMessage();}

		}
	}

	public static void deleteAppointment(Event e){
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			ArrayList<Person> p = e.getPersonList();
			String sql_delete_invite = "DELETE FROM `INVITED_TO` WHERE appointment_id='" + e.getID() +"'";
			
			PreparedStatement ps2 = (PreparedStatement) conn.prepareStatement(sql_delete_invite);
			ps2.execute();

			String sql_delete = "DELETE FROM APPOINTMENT WHERE id='" +e.getID() + "'";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql_delete);
			ps.execute();
			String day = new java.text.SimpleDateFormat("dd-MM-yyyy").format(new Date());
			String time = new java.text.SimpleDateFormat("HHmm").format(new Date());

			if(p.size() > 1){
				for (int i = 0; i < p.size(); i++) {
					addNotification("Møte: " + e.getTitle() + " har blitt slettet.", time, day);		
					invitenull(p.get(i).getEmail(), getLastNotification());
				}				
			}


		}catch(Exception exc){
			exc.getMessage();
		}finally{
			try{
				conn.close();
			}catch(Exception exc){
				exc.getMessage();
			}

		}
	}


	private static int getLastNotification() {
		int notID = 0;
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "SELECT id FROM NOTIFICATION ORDER BY id DESC LIMIT 1";
			ResultSet rs = conn.createStatement().executeQuery(sql);
			if(rs.next()){
				notID = rs.getInt("id");
			}


		}catch(Exception exc){
			exc.getMessage();
		}finally{
			try{
				conn.close();
			}catch(Exception exc){
				exc.getMessage();
			}
		}
		return notID;
	}

	
	private static void invitenull(String email, int notID) {
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			String sql = "INSERT INTO INVITED_TO(person_email, notification_id) VALUES(?,?)";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, email);
			ps.setInt(2, notID);
			ps.execute();
		}catch(Exception exc){
			exc.getMessage();
		}finally{
			try{
				conn.close();
			}catch(Exception exc){
				exc.getMessage();
			}
		}
	}

	public static void editAppointment(Event event, String day, String start_time, int duration, int public_variable, String title, 
			String description, String place, String room_name, String owner_email, ArrayList<Person> invited_persons, int reminder){
		try{			
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			ArrayList<Person> p = event.getPersonList();
			String sql_update = "UPDATE APPOINTMENT SET day=?, start_time=?, duration=?, title=?, description=?, " +
					"place=?, room_name=?  WHERE id='" +event.getID() + "'";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql_update);
			ps.setString(1, day);
			ps.setString(2, start_time);
			ps.setInt(3, duration);
			ps.setString(4, title);
			ps.setString(5, description);
			ps.setString(6, place);
			ps.setString(7, room_name);

			ps.executeUpdate();
			String today = new java.text.SimpleDateFormat("dd-MM-yyyy").format(new Date());
			String time = new java.text.SimpleDateFormat("HHmm").format(new Date());

			for (int i = 0; i < p.size(); i++) {
				if(!p.get(i).getEmail().equals(event.getOwner())){
					addNotification("M�te: " + event.getTitle() + " har blitt endret. Se endringer!.", time, today);
					changeStatusAppointmentEdited(p.get(i).getEmail(), getLastNotification(), event.getID());
				}
			}

		}catch(Exception exc){
			exc.getMessage();
		}finally{
			try{
				conn.close();
			}catch(Exception exc){
				exc.getMessage();
			}
		}
	}

	public static void changeStatusAppointmentEdited(String email, int noteID, int appID){

		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);

			String sql = "UPDATE INVITED_TO SET notification_id=" + noteID +" , is_going=0 , hidden=0 " + 
					" WHERE appointment_id=" + appID + " AND person_email='" + email + "'";

			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.executeUpdate();

		}catch (Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				conn.close();
			}catch(Exception e){}
		}
	}


	public static void setReminder(String email, int eventID, int time){
		try{
			Connection conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);

			String sql = "UPDATE INVITED_TO SET reminder=" + time +  
					" WHERE appointment_id=" + eventID + " AND person_email='" + email + "'";

			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.executeUpdate();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				conn.close();
			}catch(Exception e){}
		}
	}
	
	public Connection getConnection() {
		return conn;
	}

}
