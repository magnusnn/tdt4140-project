package logic;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class HintTextField extends JTextField implements FocusListener {
	private final String hint;
	private boolean showingHint;
	
	public HintTextField(final String hint) {
		super(hint);
		this.hint = hint;
		this.showingHint = true;
		super.addFocusListener(this);
		super.setForeground(Color.gray);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		if(this.getText().isEmpty()) {
			super.setText("");
			showingHint = false;
			super.setForeground(Color.black);
		}
	}
	@Override
	public void focusLost(FocusEvent e) {
		if(this.getText().isEmpty()) {
			super.setText(hint);
			super.setForeground(Color.gray);
			showingHint = true;
		}
	}
	@Override
	public String getText() {
		return showingHint ? "" :
			super.getText();
	}
}