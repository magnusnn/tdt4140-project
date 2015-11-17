package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class EmailMain extends JFrame {
	protected static JFrame frame;
	private static JPanel panel;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					panel = new EmailInviter();
					frame = new EmailMain();
					frame.setVisible(true);
					frame.setSize(panel.getSize());
					frame.add(panel);
					frame.requestFocus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public EmailMain(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
