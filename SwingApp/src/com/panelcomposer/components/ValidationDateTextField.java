package com.panelcomposer.components;


import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.util.Date;

import javax.swing.text.AbstractDocument;

import util.Resources;

import com.panelcomposer.components.ValidationDatePicker.E_VALID_DATES;
import com.panelcomposer.components.document.DateDocumentFilter;
import com.toedter.calendar.JTextFieldDateEditor;

public class ValidationDateTextField extends JTextFieldDateEditor implements IValidationTextField {

	private static final long serialVersionUID = 1L;

	private E_VALID_DATES validDates;
	private boolean required;

	public ValidationDateTextField(E_VALID_DATES validDates, boolean required,
			String name) {
		super();
		setSize(20, 40);
		// super("dd.MM.yyyy", "##.##.####",'_');
		this.validDates = validDates;
		this.required = required;
		setName(name);
		AbstractDocument doc = (AbstractDocument)getDocument();
		doc.setDocumentFilter(new DateDocumentFilter());		
	}

	@Override
	public boolean isEditValid() {
		Date date = getDate();
		if (date == null)
			return required ? false : getText().replace("_", "")
					.replace(".", "").trim().equals("");

		if (validDates == E_VALID_DATES.ALL)
			return true;
		Date today = new Date();
		if (validDates == E_VALID_DATES.BEFORE)
			return today.after(date);
		if (validDates == E_VALID_DATES.BEFORE_INCLUDING)
			return !today.before(date);
		if (validDates == E_VALID_DATES.AFTER)
			return today.before(date);
		if (validDates == E_VALID_DATES.AFTER_INCLUDING)
			return !today.after(date);

		return true;

	}

	@Override
	public void setText(String t) {
		super.setText(t);
		// ako se postavi preko kalendara, potrebno je proveriti validnost
		if (!isEditValid())
			setBorder(Resources.getInvalidBorder());
		else
			setBorder(Resources.getOriginalBorder());
	}

	public E_VALID_DATES getValidDates() {
		return validDates;
	}

	public void setValidDates(E_VALID_DATES validDates) {
		this.validDates = validDates;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(70, (int) super.getPreferredSize().getHeight());
	}

	@Override
	public void focusLost(FocusEvent focusEvent){
		String text = getText();
		if (!text.endsWith(".")){
			text = text.concat(".");
			try {
				Date date = dateFormatter.parse(text);
				setDate(date, true);
			} catch (Exception e) {
				// ignore
			}
		}
		else
			super.focusLost(focusEvent);
	}
}
