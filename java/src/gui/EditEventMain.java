package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.Event;
import logic.Person;

public class EditEventMain extends JFrame {

	private JPanel contentPane;
	static EditEventMain frame;
	public static MainFrame mainFrame;
	/**
	 * Launch the application.
	 */
	public static void main(final Person person, final MainFrame mainFrame, final Event event) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new EditEventMain();
					frame.setVisible(true);
					frame.add(new EditEvent(person, mainFrame, event));
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
	public EditEventMain() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
