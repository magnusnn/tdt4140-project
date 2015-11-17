package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logic.Person;

public class Login extends JFrame{

	private JPanel contentPane;
	private JTextField emailTextField;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JPasswordField passwordTextField;
	private static Login frame;
	public static Frame mainFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Calendar v1.1");
		getContentPane().setLayout(null);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(83, 131, 46, 14);
		getContentPane().add(lblEmail);
		
		JLabel lblPassord = new JLabel("Passord");
		lblPassord.setBounds(83, 156, 46, 14);
		getContentPane().add(lblPassord);
		
		emailField = new JTextField();
		emailField.setBounds(174, 128, 123, 20);
		emailField.setColumns(10);
		getContentPane().add(emailField);
		
		
		passwordField = new JPasswordField();
		passwordField.setBounds(174, 153, 123, 20);
		getContentPane().add(passwordField);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(174, 214, 123, 48);
		getContentPane().add(loginButton);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblNewLabel.setToolTipText("");
		lblNewLabel.setBounds(156, 73, 72, 32);
		contentPane.add(lblNewLabel);
		
		JLabel lblEpost = new JLabel("Email");
		lblEpost.setBounds(72, 132, 62, 14);
		contentPane.add(lblEpost);
		
		
		emailTextField = new JTextField();
		emailTextField.setBounds(156, 129, 115, 20);
		contentPane.add(emailTextField);
		emailTextField.setColumns(10);
		
		JButton loginButton1 = new JButton("Login");
		loginButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String personEmail = db.DBConnect.ValidateUser(emailTextField.getText(), passwordTextField.getText());
				
				if(personEmail != null){
					Person person = db.DBConnect.getPerson(personEmail);
					frame.dispose();
					mainFrame = new MainFrame(person);
					mainFrame.setVisible(true);
					mainFrame.requestFocus();
				}
					
				else{
					JOptionPane.showMessageDialog(frame, "Feil brukernavn/passord", "Login feilet", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		loginButton1.setBounds(156, 228, 115, 34);
		contentPane.add(loginButton1);
		
		JLabel lblPassord_1 = new JLabel("Passord");
		lblPassord_1.setBounds(72, 157, 62, 14);
		contentPane.add(lblPassord_1);
		
		passwordTextField = new JPasswordField();
		passwordTextField.setBounds(156, 160, 115, 20);
		contentPane.add(passwordTextField);
	
		JButton btnNewButton = new JButton("getApp");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}			
			
		});
		btnNewButton.setBounds(156, 228, 115, 34);
		contentPane.add(btnNewButton);
	}

	
}
