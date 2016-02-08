package com.panelcomposer.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

public class TimeValidationTextField extends JFormattedTextField implements IValidationTextField{

	private static final long serialVersionUID = 1L;

	private boolean required;

	private MaskFormatter mask;

	public TimeValidationTextField(int nullable){
		super();
		setColumns(5);
		this.required = nullable == 0;
		try {
			mask = new MaskFormatter("##:##:##");
			mask.setPlaceholderCharacter('_');
			setFormatter(mask);
			setFocusLostBehavior(JFormattedTextField.PERSIST);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				String text = getText();
				text = text.replace("_", "0");
				setText(text);
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				if (getText().equals("__:__:__"))
					setCaretPosition(0);
			}
		});

	}

	@Override
	public boolean isEditValid(){
		if (required && getText().equals("__:__:__"))
			return false;
		try {
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			df.setLenient(false);
			df.parse(getText());
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	public void reset(){
		setText("__:__:__");
	}

}
