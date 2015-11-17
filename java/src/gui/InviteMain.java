package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.Person;

public class InviteMain extends JFrame{
	private JPanel contentPane;
	static AppointmentMain frame;
	public static MainFrame mainFrame;
	/**
	 * Launch the application.
	 */
	public static void main(final Person person, final MainFrame mainFrame) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AppointmentMain();
					frame.setVisible(true);
					frame.add(new NewAppointment(person, mainFrame));
					frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void setMainFrame(MainFrame frame){
		mainFrame = frame;
	}

	/**
	 * Create the frame.
	 */
	public InviteMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
}
