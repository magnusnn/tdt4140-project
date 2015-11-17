package test;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import logic.Event;
import logic.Group;
import logic.Notification;
import logic.Person;
import logic.Room;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.mysql.jdbc.Connection;

import db.DBConnect;

@RunWith(JUnit4.class)
public class DBTest {
	
	private static final String CONNECTION_STRING = "jdbc:mysql://mysql.stud.ntnu.no/test";
	private static final String USERNAME = "magnne_fellespro";
	private static final String PASSWORD = "fellesprosjekt";


	private static Connection conn;
	private static Statement s;
	private static ResultSet rs;

	public DBTest() {
		try {
			conn = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test database connection. Create a table, push some data, pull some data and delete table.
	 */
	@Test
	public void testConnection() throws SQLException  {
		String sql = "CREATE TABLE test_flargblarg (" +
					"id int, " +
					"name varchar(30) " +
				");";
		
		s = conn.createStatement();
		s.executeUpdate(sql);
		
		sql = "INSERT INTO test_flargblarg values (14, 'Noe');";
		s = conn.createStatement();
		s.executeUpdate(sql);
		
		sql = "INSERT INTO test_flargblarg values (12, 'Hei');";
		s = conn.createStatement();
		s.executeUpdate(sql);
		
		sql = "SELECT * FROM test_flargblarg";
		s = conn.createStatement();
		rs = s.executeQuery(sql);
		
		rs.next();
		Assert.assertEquals(14, rs.getInt("id"));
		Assert.assertEquals("Noe", rs.getString("name"));

		rs.next();
		Assert.assertEquals(12, rs.getInt("id"));
		Assert.assertEquals("Hei", rs.getString("name"));

		Assert.assertFalse("There should be no more rows in table", rs.next());

		sql = "DROP TABLE test_flargblarg;";
		s = conn.createStatement();
		s.executeUpdate(sql);
	}
	@Test
	public void testValidate() {
		Assert.assertEquals(DBConnect.ValidateUser("anniken", "anniken"),"anniken");
		Assert.assertEquals(DBConnect.ValidateUser("ivar", "ivar"),"ivar");
		Assert.assertEquals(DBConnect.ValidateUser("kjetil", "kjetil"),"kjetil");
		Assert.assertEquals(DBConnect.ValidateUser("magnus", "magnus"),"magnus");
		Assert.assertEquals(DBConnect.ValidateUser("marie", "marie"),"marie");
		Assert.assertEquals(DBConnect.ValidateUser("per", "per"),"per");

		Assert.assertNull(DBConnect.ValidateUser("dsadsad", "sdafaSAF"));
		
	}
	@Test
	public void testGetPerson(){
		Person person1 = DBConnect.getPerson("kjetil");
		Assert.assertNotNull(person1);
		Person person2 = new Person("kjetil", "Kjetil", "Aune","24-03-1992");
		Assert.assertEquals(person1.getEmail(),person2.getEmail());
		Assert.assertEquals(person1.getGivenName(),person2.getGivenName());
		Assert.assertEquals(person1.getSurname(),person2.getSurname());
		Assert.assertEquals(person1.getDateOfBirth(),person2.getDateOfBirth());
	}
	@Test
	public void testGetGroupMembers(){
		ArrayList<Person> group = DBConnect.getGroupMembers(2);
		Assert.assertNotNull(group);

		Person wrongPerson = DBConnect.getPerson("ivar");
		for (int i = 0; i < group.size(); i++) {
			Person current = group.get(i);
			
			Assert.assertNotEquals(current, wrongPerson);
			
			Person person = DBConnect.getPerson(current.getEmail());
			Assert.assertEquals(current.getGivenName(), person.getGivenName());
			Assert.assertEquals(current.getSurname(), person.getSurname());
			Assert.assertEquals(current.getDateOfBirth(), person.getDateOfBirth());
		}
	}
	@Test
	public void testGetGroups() {
		ArrayList<Group> groups = DBConnect.getGroups();
		Assert.assertNotNull(groups);
	}
	@Test
	public void testGetAllInvitedPersons() {
		ArrayList<Person> invited = DBConnect.getAllInvitedPersons(4);
		Person invitee = DBConnect.getPerson("kjetil");
		Assert.assertNotNull(invited);
		for (int i = 0; i < invited.size(); i++) {
			if(invited.get(i).getEmail() == invitee.getEmail()) {
				Assert.assertEquals(invited.get(i).getGivenName(), invitee.getGivenName());
			}
		}
	}
	@Test
	public void testGetEvent() {
		Event event = DBConnect.getEvent(5);
		Assert.assertNotNull(event);
	}
	@Test
	public void testGetNotifications() {
		Person person = DBConnect.getPerson("kjetil");
		ArrayList<Notification> notifications = DBConnect.getNotificationList(person);
		
		Assert.assertNotNull(notifications);
		Assert.assertEquals(notifications.get(0).getPerson(), person);
	}
	@Test
	public void testGetAllRooms() {
		ArrayList<Room> rooms = DBConnect.getAllRooms();
		Assert.assertNotNull(rooms);
	}
	@Test
	public void testGetAllEventsOnRoom() {
		Room room = DBConnect.getAllRooms().get(0);
		Room room2 = DBConnect.getAllRooms().get(1);
		ArrayList<Event> events = DBConnect.getAllEventsOnRoom(room.getName());
		ArrayList<Event> events2 = DBConnect.getAllEventsOnRoom(room2.getName());

		Assert.assertNotNull(events);
		Assert.assertNotNull(events2);
		
		Assert.assertNotEquals(events, events2);
	}
	@Ignore
	@Test
	public void testAddAndEditAppointment() {
		DBConnect.addAppointment("18-04-2014", "2100", 90, 1, "test!", "testAddAppointment()", "flagre", "flagre", "anniken", null, 1);
		ArrayList<Event> events = DBConnect.getAllEventsOnRoom("flagre");
		Assert.assertNotNull(events);

		Event test = null;
		for (Event event : events) {
			if(event.getDate() == "18-04-2014") {
				test = event;
				break;
			}
		}
		DBConnect.editAppointment(test, "19-04-2014","2100", 90, 1, "test!", "testAddAppointment()", "flagre", "flagre", "anniken", null, 1);
		events = DBConnect.getAllEventsOnRoom("flagre");
		Assert.assertNotNull(events);

		Event test2 = null;
		for (Event event : events) {
			if(event.getDate() == "19-04-2014") {
				test2 = event;
				break;
			}
		}
		Assert.assertNotEquals(test, test2);
	}
}