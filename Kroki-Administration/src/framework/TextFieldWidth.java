package framework;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public  class TextFieldWidth implements KeyListener {

	private Integer width;

	public TextFieldWidth(Integer columnName) {
		super();
		this.width = columnName;
	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {

		// TODO Auto-generated method stub
		if (e.getSource() instanceof JTextField) {
			JTextField tf = (JTextField) e.getSource();
			
			if (tf.getText().length() > width - 1) {
				e.consume();
			}
		}else if(e.getSource() instanceof JTextArea){
			JTextArea tf = (JTextArea) e.getSource();
			
			if (tf.getText().length() > width - 1) {
				e.consume();
			}
			
		}

		// TODO Auto-generated method stub

	}
}
