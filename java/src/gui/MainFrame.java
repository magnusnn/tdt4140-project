package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import logic.Appointment;
import logic.Calendar;
import logic.Event;
import logic.Meeting;
import logic.Notification;
import logic.Person;
import logic.Reminder;
import db.DBConnect;
import java.awt.event.MouseAdapter;

public class MainFrame extends JFrame {
	
	private JPanel contentPane;
	private static Calendar calendar;
	private ArrayList<Event> eventList;
	private int topHour;
	private ArrayList<ArrayList<JPanel>> eventPanels; //a list of eventPanels for each day of the week, starting with Sunday
	private JPanel mondayPanel;
	private JPanel tuesdayPanel;
	private JPanel wednesdayPanel;
	private JPanel thursdayPanel;
	private JPanel fridayPanel;
	private JPanel saturdayPanel;
	private JPanel sundayPanel;
	private ArrayList<JLabel> hourLabels;
	private ArrayList<JPanel> days;
	private java.util.Calendar cal;
	private int weekNumber;
	private int year;
	private JLabel weeknr;
	private ArrayList<JLabel> dateLabels;
	private static Person loggedInPerson;
	public static JFrame showEventFrame;
	private NotiPanel notificationPanel;
	private static Person otherParticipant;
	private static ArrayList<Event> otherEvents;
	public static JFrame participantFrame;
	private MainFrame mainFrame;
	private String[] args;
	private ArrayList<Notification> notiList;
	private JLabel newDot;
	
	public static Person getPerson() {
		return loggedInPerson;
	}
	
	public static Calendar getCalendar() {
		return calendar;
	}
	
	public static void setOtherParticipant(Person person) {
		otherParticipant = person;
	}
	
	public void addDot() {
		newDot.setVisible(false);
	}
	
	public void removeDot() {
		newDot.setVisible(false);
	}
	
	public void incrementTopHour() {
		if (topHour < 16)
			topHour++;
		repaintTimes();
		if (otherParticipant == null)
			addAllEvents(false);
		else
			addAllEvents(true);
	}
	
	public void decrementTopHour() {
		if (topHour > 0)
			topHour--;
		repaintTimes();
		if (otherParticipant == null)
			addAllEvents(false);
		else
			addAllEvents(true);
	}
	
	public void nextWeek() {
		cal.setTime(new Date(cal.getTimeInMillis() + 604800000));
		changeWeek();
	}
	
	public void previousWeek() {
		cal.setTime(new Date(cal.getTimeInMillis() - 604800000));
		changeWeek();
	}
	
	public void changeWeek() {
		weekNumber = cal.get(java.util.Calendar.WEEK_OF_YEAR);
		year = cal.get(java.util.Calendar.YEAR);
		weeknr.setText(Integer.toString(weekNumber));
		paintDates();
		if (otherParticipant == null)
			addAllEvents(false);
		else
			addAllEvents(true);
	}
	
	public void paintDates() {
		int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK) - 2;
		if (dayOfWeek == -1)
			dayOfWeek = 6;
		cal.setTime(new Date(cal.getTimeInMillis() - 86400000 * dayOfWeek));
		for (int i = 0; i < dateLabels.size(); i++) {
			String dayZero = "";
			if (cal.get(java.util.Calendar.DATE) < 10)
				dayZero = "0";
			String monthZero = "";
			if (cal.get(java.util.Calendar.MONTH) < 9)
				monthZero = "0";
			dateLabels.get(i).setText(dayZero + Integer.toString(cal.get(java.util.Calendar.DATE)) + "-" + monthZero + Integer.toString(cal.get(java.util.Calendar.MONTH) + 1) + "-" + Integer.toString(cal.get(java.util.Calendar.YEAR)));
			cal.setTime(new Date(cal.getTimeInMillis() + 86400000));
		}
		cal.setTime(new Date(cal.getTimeInMillis() - 86400000 * (7 - dayOfWeek)));
	}
	
	public void repaintTimes() {
		for (int i = 0; i < 8; i++) {
			String zero = "";
			if (topHour + i < 10)
				zero = "0";
			hourLabels.get(i).setText(zero + (topHour + i) + ":00");
		}
	}
	
	public void showOthers() {
		otherEvents = new ArrayList<Event>(DBConnect.getAllEventsOnUser(otherParticipant.getEmail())); 
		addAllEvents(true);
		final JLabel otherName = new JLabel(otherParticipant.getGivenName() + " " + otherParticipant.getSurname());
		otherName.setBounds(134, 39, 110, 20);
		getContentPane().add(otherName);
		final JPanel otherColor = new JPanel();
		otherColor.setBounds(112, 44, 10, 10);
		otherColor.setBackground(new Color(0xD6EBFF));
		otherColor.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		getContentPane().add(otherColor);
		final JPanel hideOther = new JPanel();
		hideOther.setBounds(250, 39, 15, 20);
		hideOther.add(new JLabel("X"));
		hideOther.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		
			@Override
			public void mousePressed(MouseEvent e) {
				hideOthers(otherColor, otherName, hideOther);
			}
		
			@Override
			public void mouseExited(MouseEvent e) {
			}
		
			@Override
			public void mouseEntered(MouseEvent e) {
			}
		
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		getContentPane().add(hideOther);
	}
	
	public void hideOthers(JPanel otherColor, JLabel otherName, JPanel hideOther) {
		otherParticipant = null;
		addAllEvents(false);
		getContentPane().remove(otherColor);
		getContentPane().remove(otherName);
		getContentPane().remove(hideOther);
	}
	
	public void addAllEvents(boolean other) {
		ArrayList<Event> events = new ArrayList<Event>(eventList);
		if (other)
			events.addAll(otherEvents);
		eventPanels = new ArrayList<ArrayList<JPanel>>();
		for (int i = 0; i < 7; i++) {
			eventPanels.add(new ArrayList<JPanel>());
		}
		Collections.sort(events);
		Collections.reverse(events);
		mondayPanel.removeAll();
		tuesdayPanel.removeAll();
		wednesdayPanel.removeAll();
		thursdayPanel.removeAll();
		fridayPanel.removeAll();
		saturdayPanel.removeAll();
		sundayPanel.removeAll();
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getHidden() == 0 || (!events.get(i).getPersonEmail().equals(loggedInPerson.getEmail()) && events.get(i).getIsGoing() != -1)) {
				if (!events.get(i).getPersonEmail().equals(loggedInPerson.getEmail()))
					addEvent(events.get(i), 0); // other participant
				else if (events.get(i).getOwner().equals(loggedInPerson.getEmail()))
					addEvent(events.get(i), 1); // is owner
				else
					addEvent(events.get(i), 2); // is not owner
			}
		}
		for (int i = 0; i < eventPanels.size(); i++) {
			for (int j = 0; j < eventPanels.get(i).size(); j++) {
				if (eventPanels.get(i).get(j).getWidth() == 61) {
					if (j == 0 || eventPanels.get(i).get(j-1).getY() >= eventPanels.get(i).get(j).getY() + eventPanels.get(i).get(j).getHeight()) {
						if (j == eventPanels.get(i).size() - 1 || eventPanels.get(i).get(j+1).getY() + eventPanels.get(i).get(j+1).getHeight() <= eventPanels.get(i).get(j).getY())
							eventPanels.get(i).get(j).setSize(121, eventPanels.get(i).get(j).getHeight());
					}
				}
			}
		}
		repaint();
		this.getRootPane().revalidate();
	}
	
	public void addEvent(Event event, int type) {
		Date date = null;
		try {
			date = new SimpleDateFormat("dd-MM-yyyy").parse(event.getDate());
		}
		catch (Exception e) {
			return;
		}
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setFirstDayOfWeek(java.util.Calendar.MONDAY);
		c.setTime(date);
		int eventWeek = c.get(java.util.Calendar.WEEK_OF_YEAR);
		int eventYear = c.get(java.util.Calendar.YEAR);
		if (eventWeek != weekNumber || eventYear != year)
			return;
		int weekDay = c.get(java.util.Calendar.DAY_OF_WEEK); // 1 is Sunday, 7 is Saturday
		weekDay = weekDay % 7;
		if (weekDay == 0)
			weekDay = 7;
		int hoursStart = Integer.parseInt(event.getStartTime().substring(0, 2));
		int minutesStart = Integer.parseInt(event.getStartTime().substring(2));
		int heightStart = (hoursStart - topHour) * 70 + minutesStart * 7/6;
		int height = event.getDuration() * 7/6;
		if (heightStart + height > 1680) {
			int newWeekDay = weekDay + 1;
			if (newWeekDay > 7)
				newWeekDay -= 7;
			addEventPanel(event, 0, heightStart + height - 1680, newWeekDay, type);
			height = 1680 - heightStart;
		}
		addEventPanel(event, heightStart, height, weekDay, type);
	}
	
	public void addEventPanel(final Event event, int heightStart, int height, int weekDay, int type) {
		int widthStart = -1;
		if (eventPanels.get(weekDay - 1).size() == 0)
			widthStart = 0;
		else if (eventPanels.get(weekDay - 1).get(eventPanels.get(weekDay - 1).size() - 1).getY() >= heightStart + height)
			widthStart = 0;
		else if (eventPanels.get(weekDay - 1).get(eventPanels.get(weekDay - 1).size() - 1).getX() == 60 && (eventPanels.get(weekDay - 1).size() == 1 || eventPanels.get(weekDay - 1).get(eventPanels.get(weekDay - 1).size() - 2).getY() > heightStart + height))
			widthStart = 0;
		else if (eventPanels.get(weekDay - 1).get(eventPanels.get(weekDay - 1).size() - 1).getX() == 0 && (eventPanels.get(weekDay - 1).size() == 1 || eventPanels.get(weekDay - 1).get(eventPanels.get(weekDay - 1).size() - 2).getY() > heightStart + height))
			widthStart = 60;
		else {
			return;
		}
		Color color = new Color(255, 182, 193);
		if (type == 0)
			color = new Color(0xD6EBFF);
		else if (type == 1)
			color = new Color(0xFFFFB2);
		int width = 61;
		JPanel eventPanel = new JPanel();
		eventPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		eventPanel.setBackground(color);
		eventPanel.setBounds(widthStart, heightStart, width, height);
		if (type != 0) {
			String going = "Not answered";
			if (event.getIsGoing() == 1)
				going = "Is going";
			else if (event.getIsGoing() == -1)
				going = "Not going";
			JLabel label = new JLabel("<html>" + event.getTitle() + "<br><br>" + going + "</html>");
			eventPanel.add(label);
			JPanel hide = new JPanel();
			hide.add(new JLabel("X"));
			hide.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}
			
				@Override
				public void mousePressed(MouseEvent e) {
					DBConnect.changeStatus(-1, loggedInPerson.getEmail(), event.getID(), 1, -1);
					event.setHidden(1);
					if (otherParticipant == null)
						addAllEvents(false);
					else
						addAllEvents(true);
				}
			
				@Override
				public void mouseExited(MouseEvent e) {
				}
			
				@Override
				public void mouseEntered(MouseEvent e) {
				}
			
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			if (event.getIsGoing() == -1) {
				eventPanel.add(hide);
			}
			eventPanel.addMouseListener(new MouseListener() {
			
				@Override
				public void mouseReleased(MouseEvent e) {
				}
			
				@Override
				public void mousePressed(MouseEvent e) {
					showEvent(event);
				}
			
				@Override
				public void mouseExited(MouseEvent e) {
				}
			
				@Override
				public void mouseEntered(MouseEvent e) {
				}
			
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
		}
		else {
			JLabel name = new JLabel("Busy");
			eventPanel.add(name);
		}
		days.get(weekDay - 1).add(eventPanel);
		eventPanels.get(weekDay - 1).add(eventPanel);
	}
	
	public void showEvent(Event event) {
		boolean is_owner = false;
		if(event.getOwner().equals(loggedInPerson.getEmail())) is_owner = true;
		boolean meeting = false;
		if (event instanceof Meeting)
			meeting = true;
		ShowEvent showEvent = null;
		ShowInvite showInvite = null;
		if(is_owner){
			if (meeting)
				showEvent = new ShowEvent((Meeting) event, loggedInPerson);
			else
				showEvent = new ShowEvent((Appointment) event, loggedInPerson);
		}
		else{
			showInvite = new ShowInvite((Meeting) event, loggedInPerson);
		}
		showEventFrame = new JFrame();			
		showEventFrame.setBounds(0, 0, 600, 310);
		showEventFrame.setLocationRelativeTo(null);
		if(is_owner) showEventFrame.getContentPane().add(showEvent);
		else showEventFrame.getContentPane().add(showInvite);
		showEventFrame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(Person person) {
		mainFrame = this;
		loggedInPerson = person;
		Calendar calendar = new Calendar(DBConnect.getAllEventsOnUser(person.getEmail()));
		notiList = new ArrayList<Notification>(DBConnect.getNotificationList(loggedInPerson));
		for (int i = 0; i < calendar.getEventList().size(); i++) {
			if (calendar.getEventList().get(i).getReminder() != -1) {
				Reminder reminder = new Reminder(calendar.getEventList().get(i).getReminder(), calendar.getEventList().get(i));
			}
		}
		notificationPanel = null;
		dateLabels = new ArrayList<JLabel>();
		cal = java.util.Calendar.getInstance();
		weekNumber = cal.get(java.util.Calendar.WEEK_OF_YEAR);
		year = cal.get(java.util.Calendar.YEAR);
		eventList = calendar.getEventList();
		topHour = 8;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 941, 735);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(56, 79, 840, 600);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		mondayPanel = new JPanel();
		mondayPanel.setBorder(new MatteBorder(1, 1, 1, 0, (Color) new Color(0, 0, 0)));
		mondayPanel.setBackground(Color.WHITE);
		mondayPanel.setBounds(0, 40, 120, 560);
		panel.add(mondayPanel);
		mondayPanel.setLayout(null);
				
		tuesdayPanel = new JPanel();
		tuesdayPanel.setBorder(new MatteBorder(1, 1, 1, 0, (Color) new Color(0, 0, 0)));
		tuesdayPanel.setBackground(Color.WHITE);
		tuesdayPanel.setBounds(120, 40, 120, 560);
		panel.add(tuesdayPanel);
		tuesdayPanel.setLayout(null);
		
		wednesdayPanel = new JPanel();
		wednesdayPanel.setBorder(new MatteBorder(1, 1, 1, 0, (Color) new Color(0, 0, 0)));
		wednesdayPanel.setBackground(Color.WHITE);
		wednesdayPanel.setBounds(240, 40, 120, 560);
		panel.add(wednesdayPanel);
		wednesdayPanel.setLayout(null);
		
		thursdayPanel = new JPanel();
		thursdayPanel.setBorder(new MatteBorder(1, 1, 1, 0, (Color) new Color(0, 0, 0)));
		thursdayPanel.setBackground(Color.WHITE);
		thursdayPanel.setBounds(360, 40, 120, 560);
		panel.add(thursdayPanel);
		thursdayPanel.setLayout(null);
		
		fridayPanel = new JPanel();
		fridayPanel.setBorder(new MatteBorder(1, 1, 1, 0, (Color) new Color(0, 0, 0)));
		fridayPanel.setBackground(Color.WHITE);
		fridayPanel.setBounds(480, 40, 120, 560);
		panel.add(fridayPanel);
		fridayPanel.setLayout(null);
		
		saturdayPanel = new JPanel();
		saturdayPanel.setBorder(new MatteBorder(1, 1, 1, 0, (Color) new Color(0, 0, 0)));
		saturdayPanel.setBackground(Color.WHITE);
		saturdayPanel.setBounds(600, 40, 120, 560);
		panel.add(saturdayPanel);
		saturdayPanel.setLayout(null);
		
		sundayPanel = new JPanel();
		sundayPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		sundayPanel.setBackground(Color.WHITE);
		sundayPanel.setBounds(720, 40, 120, 560);
		panel.add(sundayPanel);
		sundayPanel.setLayout(null);
		
		JPanel mondayBar = new JPanel();
		mondayBar.setBorder(new MatteBorder(1, 1, 0, 0, (Color) new Color(0, 0, 0)));
		mondayBar.setBackground(SystemColor.textHighlight);
		mondayBar.setBounds(0, 0, 120, 40);
		panel.add(mondayBar);
		mondayBar.setLayout(null);
		
		JLabel lblMonday = new JLabel("Monday");
		lblMonday.setBounds(35, 6, 50, 17);
		lblMonday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		mondayBar.add(lblMonday);
		
		JLabel lblMondayDate = new JLabel();
		lblMondayDate.setBounds(6, 26, 108, 17);
		lblMonday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		mondayBar.add(lblMondayDate);
		dateLabels.add(lblMondayDate);
		
		JPanel tuesdayBar = new JPanel();
		tuesdayBar.setBorder(new MatteBorder(1, 1, 0, 0, (Color) new Color(0, 0, 0)));
		tuesdayBar.setBackground(SystemColor.textHighlight);
		tuesdayBar.setBounds(120, 0, 120, 40);
		panel.add(tuesdayBar);
		tuesdayBar.setLayout(null);
		
		JLabel lblTuesday = new JLabel("Tuesday");
		lblTuesday.setBounds(34, 6, 53, 17);
		lblTuesday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		tuesdayBar.add(lblTuesday);
		
		JLabel lblTuesdayDate = new JLabel();
		lblTuesdayDate.setBounds(6, 26, 108, 17);
		lblTuesday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		tuesdayBar.add(lblTuesdayDate);
		dateLabels.add(lblTuesdayDate);
		
		JPanel wednesdayBar = new JPanel();
		wednesdayBar.setBorder(new MatteBorder(1, 1, 0, 0, (Color) new Color(0, 0, 0)));
		wednesdayBar.setBackground(SystemColor.textHighlight);
		wednesdayBar.setBounds(240, 0, 120, 40);
		panel.add(wednesdayBar);
		wednesdayBar.setLayout(null);
		
		JLabel lblWednesday = new JLabel("Wednesday");
		lblWednesday.setBounds(23, 6, 74, 17);
		lblWednesday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		wednesdayBar.add(lblWednesday);
		
		JLabel lblWednesdayDate = new JLabel();
		lblWednesdayDate.setBounds(6, 26, 108, 17);
		lblWednesday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		wednesdayBar.add(lblWednesdayDate);
		dateLabels.add(lblWednesdayDate);
		
		JPanel thursdayBar = new JPanel();
		thursdayBar.setBorder(new MatteBorder(1, 1, 0, 0, (Color) new Color(0, 0, 0)));
		thursdayBar.setBackground(SystemColor.textHighlight);
		thursdayBar.setBounds(360, 0, 120, 40);
		panel.add(thursdayBar);
		thursdayBar.setLayout(null);
		
		JLabel lblThursday = new JLabel("Thursday");
		lblThursday.setBounds(31, 6, 58, 17);
		lblThursday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		thursdayBar.add(lblThursday);
		
		JLabel lblThursdayDate = new JLabel();
		lblThursdayDate.setBounds(6, 26, 108, 17);
		lblThursday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		thursdayBar.add(lblThursdayDate);
		dateLabels.add(lblThursdayDate);
		
		JPanel fridayBar = new JPanel();
		fridayBar.setBorder(new MatteBorder(1, 1, 0, 0, (Color) new Color(0, 0, 0)));
		fridayBar.setBackground(SystemColor.textHighlight);
		fridayBar.setBounds(480, 0, 120, 40);
		panel.add(fridayBar);
		fridayBar.setLayout(null);
		
		JLabel lblFriday = new JLabel("Friday");
		lblFriday.setBounds(40, 6, 40, 17);
		lblFriday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		fridayBar.add(lblFriday);
		
		JLabel lblFridayDate = new JLabel();
		lblFridayDate.setBounds(6, 26, 108, 17);
		lblFriday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		fridayBar.add(lblFridayDate);
		dateLabels.add(lblFridayDate);
		
		JPanel saturdayBar = new JPanel();
		saturdayBar.setBorder(new MatteBorder(1, 1, 0, 0, (Color) new Color(0, 0, 0)));
		saturdayBar.setBackground(SystemColor.textHighlight);
		saturdayBar.setBounds(600, 0, 120, 40);
		panel.add(saturdayBar);
		saturdayBar.setLayout(null);
		
		JLabel lblSaturday = new JLabel("Saturday");
		lblSaturday.setBounds(32, 6, 57, 17);
		lblSaturday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		saturdayBar.add(lblSaturday);
		
		JLabel lblSaturdayDate = new JLabel();
		lblSaturdayDate.setBounds(6, 26, 108, 17);
		lblSaturday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		saturdayBar.add(lblSaturdayDate);
		dateLabels.add(lblSaturdayDate);
		
		JPanel sundayBar = new JPanel();
		sundayBar.setBorder(new MatteBorder(1, 1, 0, 1, (Color) new Color(0, 0, 0)));
		sundayBar.setBackground(SystemColor.textHighlight);
		sundayBar.setBounds(720, 0, 120, 40);
		panel.add(sundayBar);
		sundayBar.setLayout(null);
		
		JLabel lblSunday = new JLabel("Sunday");
		lblSunday.setBounds(36, 6, 48, 17);
		lblSunday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		sundayBar.add(lblSunday);
		
		JLabel lblSundayDate = new JLabel();
		lblSundayDate.setBounds(6, 26, 108, 17);
		lblSunday.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		sundayBar.add(lblSundayDate);
		dateLabels.add(lblSundayDate);
		
		JPanel datePanel = new JPanel();
		datePanel.setBorder(new MatteBorder(1, 1, 1, 0, (Color) new Color(0, 0, 0)));
		datePanel.setBackground(Color.LIGHT_GRAY);
		datePanel.setBounds(10, 79, 46, 600);
		getContentPane().add(datePanel);
		datePanel.setLayout(null);
		
		newDot = new JLabel("<html><font color='red'>!</html>");
		newDot.setBounds(766, 3, 30, 46);
		newDot.setFont(new Font("Helvetica Neue", Font.BOLD, 50));
		getContentPane().add(newDot);
		
		JButton upButton = new JButton("^");
		upButton.setBounds(0, 0, 46, 22);
		datePanel.add(upButton);
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				decrementTopHour();
			}
		});
		
		JButton downButton = new JButton("v");
		downButton.setBounds(0, 578, 46, 22);
		datePanel.add(downButton);
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				incrementTopHour();
			}
		});
		
		hourLabels = new ArrayList<JLabel>();
		for (int i = 0; i < 8; i++) {
			String zero = "";
			if (topHour + i < 10)
				zero = "0";
			JLabel newLabel = new JLabel(zero + (topHour + i) + ":00");
			newLabel.setBounds(10, 25+70*i, 50, 50);
			hourLabels.add(newLabel);
			datePanel.add(newLabel);
		}
		
		JButton btnOtherCalendars = new JButton("Other calendars");
		btnOtherCalendars.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		btnOtherCalendars.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				participantFrame = new JFrame();
				participantFrame.setSize(500, 500);
				participantFrame.setLocationRelativeTo(null);
				participantFrame.getContentPane().add(new ParticipantSelector(true, mainFrame,  new ArrayList<Person>()));
				participantFrame.setVisible(true);
			}
		});
		btnOtherCalendars.setBounds(57, 22, 135, 29);
		//getContentPane().add(btnOtherCalendars);
		
		JButton btnNotification = new JButton("Notifications");
		btnNotification.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		btnNotification.setBounds(787, 22, 117, 29);
		btnNotification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (notificationPanel == null) {
					notificationPanel = new NotiPanel(notiList, loggedInPerson);
					notificationPanel.setBounds(893, 130, 200, 300);
					notificationPanel.getContentPane().setLayout(null);
					notificationPanel.setUndecorated(true);
					notificationPanel.setVisible(true);
				}
				else {
					notificationPanel.dispose();
					notificationPanel = null;
				}
			}
		});
		//getContentPane().add(btnNotification);
		
		JButton btnAddEvent = new JButton("Add Event");
		btnAddEvent.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		btnAddEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AppointmentMain.main(loggedInPerson,(MainFrame) Login.mainFrame);
				
			}
		});
		btnAddEvent.setBounds(669, 22, 117, 29);
		//getContentPane().add(btnAddEvent);
		
		
		JLabel lblWeek = new JLabel("WEEK");
		lblWeek.setFont(new Font("Helvetica Neue", Font.BOLD, 22));
		lblWeek.setBounds(424, 22, 77, 24);
		getContentPane().add(lblWeek);
		
		weeknr = new JLabel(Integer.toString(weekNumber));
		weeknr.setFont(new Font("Helvetica Neue", Font.BOLD, 22));
		weeknr.setBounds(503, 20, 30, 29);
		getContentPane().add(weeknr);
		
		JButton rButton = new JButton();
		try{
			Image right = ImageIO.read(getClass().getResource("arrowRight.png"));
			rButton.setIcon(new ImageIcon(right));
			rButton.setBackground(Color.WHITE);
			rButton.setBorder(null);
		}
		catch(IOException ex){	
		}
		rButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				nextWeek();
			}
		});
		rButton.setBounds(537, 22, 45, 29);
		contentPane.add(rButton);
		
		JButton lButton = new JButton();
		try{
			Image left = ImageIO.read(getClass().getResource("arrowLeft.png"));
			lButton.setIcon(new ImageIcon(left));
			lButton.setBackground(Color.WHITE);
			lButton.setBorder(null);
		} 
		catch(IOException ex){	
		}
		lButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				previousWeek();
			}
		});
		lButton.setBounds(367, 22, 45, 29);
		contentPane.add(lButton);
		
		days = new ArrayList<JPanel>();
		days.add(sundayPanel);
		days.add(mondayPanel);
		days.add(tuesdayPanel);
		days.add(wednesdayPanel);
		days.add(thursdayPanel);
		days.add(fridayPanel);
		days.add(saturdayPanel);
		
		/*for (int i = 0; i < 7; i++) {
			for (int j = 1; j < 8; j++) {
				JPanel line = new JPanel();
				line.setBounds(120*i, 70*j, 120, 1);
				line.setBackground(new Color(190, 190, 190));
				days.get(i).add(line);
			}
		} Doesn't work */
		
		JLabel yourName = new JLabel(loggedInPerson.getGivenName() + " " + loggedInPerson.getSurname());
		yourName.setBounds(134, 16, 110, 20);
		getContentPane().add(yourName);
		JPanel firstColor = new JPanel();
		firstColor.setBounds(90, 22, 10, 10);
		firstColor.setBackground(new Color(255, 182, 193));
		firstColor.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		getContentPane().add(firstColor);
		JPanel secondColor = new JPanel();
		secondColor.setBounds(112, 22, 10, 10);
		secondColor.setBackground(new Color(0xDBFFB8));
		secondColor.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		getContentPane().add(secondColor);
		
		addAllEvents(false);
		paintDates();
		
		BufferedImage otherCal=null;
		try {
			otherCal = ImageIO.read(this.getClass().getResource("other.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JLabel picLabel = new JLabel(new ImageIcon(otherCal));
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				participantFrame = new JFrame();
				participantFrame.setSize(500, 500);
				participantFrame.setLocationRelativeTo(null);
				participantFrame.getContentPane().add(new ParticipantSelector(true, mainFrame,  new ArrayList<Person>()));
				participantFrame.setVisible(true);
			}

		});
		picLabel.setBounds(15, 11, 50, 50);
		getContentPane().add(picLabel);
		
		BufferedImage eventPic = null;
		try {
			eventPic = ImageIO.read(this.getClass().getResource("addEvent.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JLabel addEvent = new JLabel(new ImageIcon(eventPic));
		addEvent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AppointmentMain.main(loggedInPerson,(MainFrame) Login.mainFrame);
			}
		});
		addEvent.setBounds(692, 22, 41, 40);
		getContentPane().add(addEvent);
		
		BufferedImage logout = null;
		try {
			logout = ImageIO.read(this.getClass().getResource("logout.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JLabel logOut = new JLabel(new ImageIcon(logout));
		logOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loggedInPerson = null;
				try{
					notificationPanel.dispose();					
				}catch(Exception except){except.getMessage();}
				try {
					AppointmentMain.frame.dispose();					
				} catch (Exception e2) {e2.getMessage();}
				try {
					AddNewPersonMain.frame.dispose();					
				} catch (Exception e2) {e2.getMessage();}
				try {
					EditEventMain.frame.dispose();					
				} catch (Exception e2) {e2.getMessage();}
				
				Login.mainFrame.dispose();
				Login.main(args);
			}
		});
		logOut.setBounds(855, 24, 41, 40);
		getContentPane().add(logOut);
		
		BufferedImage bell = null;
		try {
			bell = ImageIO.read(this.getClass().getResource("notifi.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JLabel noti = new JLabel(new ImageIcon(bell));
		noti.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (notificationPanel == null) {
					notificationPanel = new NotiPanel(notiList, loggedInPerson);
					notificationPanel.setBounds(740, 122, 200, 300);
					notificationPanel.getContentPane().setLayout(null);
					notificationPanel.setUndecorated(true);
					notificationPanel.setVisible(true);
					removeDot();
				}
				else {
					notificationPanel.dispose();
					notificationPanel = null;
				}
			}
		});
		noti.setBounds(745, 22, 41, 40);
		getContentPane().add(noti);
		
		BufferedImage update = null;
		try {
			update = ImageIO.read(this.getClass().getResource("update.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JLabel updateButton = new JLabel(new ImageIcon(update));
		updateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try{
					notificationPanel.dispose();					
				}catch(Exception except){except.getMessage();}
				try {
					AppointmentMain.frame.dispose();					
				} catch (Exception e2) {e2.getMessage();}
				try {
					AddNewPersonMain.frame.dispose();					
				} catch (Exception e2) {e2.getMessage();}
				try {
					EditEventMain.frame.dispose();					
				} catch (Exception e2) {e2.getMessage();}
				
				Frame mainFrame = new MainFrame(loggedInPerson);
				Login.mainFrame.dispose();
				Login.mainFrame = mainFrame;
				mainFrame.setVisible(true);
			}
		});
		updateButton.setBounds(798, 22, 40, 40);
		getContentPane().add(updateButton);
		
		JLabel lblOtherCalendars = new JLabel("Other calendars");
		lblOtherCalendars.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		lblOtherCalendars.setBounds(10, 51, 90, 16);
		contentPane.add(lblOtherCalendars);
		
		JLabel lblNewEvent = new JLabel("New event");
		lblNewEvent.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		lblNewEvent.setBounds(683, 57, 50, 16);
		contentPane.add(lblNewEvent);
		
		JLabel lblNotifications = new JLabel("Notifications");
		lblNotifications.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		lblNotifications.setBounds(736, 57, 61, 16);
		contentPane.add(lblNotifications);
		
		JLabel lblUpdate = new JLabel("Update");
		lblUpdate.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		lblUpdate.setBounds(802, 57, 41, 16);
		contentPane.add(lblUpdate);
		
		JLabel lblLogout = new JLabel("Logout");
		lblLogout.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		lblLogout.setBounds(865, 57, 41, 16);
		contentPane.add(lblLogout);
	
		if (notiList.get(0).getIsRead() == true)
			addDot();
	}
}
