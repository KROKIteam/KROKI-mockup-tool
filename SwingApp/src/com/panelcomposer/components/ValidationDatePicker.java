package com.panelcomposer.components;

import com.toedter.calendar.JDateChooser;

public class ValidationDatePicker extends JDateChooser {
	
	private static final long serialVersionUID = 1L;

	public enum E_VALID_DATES {BEFORE_INCLUDING, BEFORE, AFTER_INCLUDING, AFTER, ALL}
	
	public ValidationDatePicker(int nullable, String name, E_VALID_DATES validDates){
		super(new ValidationDateTextField(validDates, nullable == 0, name));
	}

}
