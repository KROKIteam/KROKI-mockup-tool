package com.panelcomposer.components.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DateDocumentFilter extends DocumentFilter{
	/**
	 * Maksimalni broj karaktera
	 */
	private int maxLength=11;
	

	public DateDocumentFilter() {
		super();
	}

	/**
	 * Poziva se kada se nesto ukuca ili paste-uje
	 */
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
	throws BadLocationException
	{
		if (!validText(string)) {
			return;
		}

		// Nemoj dozvoliti da se insertuje vise od zeljene duzine
		if (fb.getDocument().getLength() + string.length() >maxLength) {
			return;
		}

		//Sve ok, pozovi insert
		fb.insertString(offset, string, attr);
	}

	/**
	 * Poziva se kada se nesto replace-uje, bilo da se kuca ili paste-uje
	 */
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
	throws BadLocationException
	{
		
		if (text == null)
			return;
		
		if (!validText(text.trim())) {
			return;
		}
		
		
		// Nemoj dozvoliti da se insertuje vise od zeljene duzine
		int extra = text.length() - length;
		if (fb.getDocument().getLength() + extra >maxLength) {
			return;
		}

		// Sve ok, pozovi replace
		fb.replace(offset, length, text, attrs);
	}

	/**
	 * Proverava da li unos moze biti validan
	 * @param text za koji se proverava
	 * @return
	 */
	private boolean validText(String text) {
		for (int pos=0; pos < text.length(); pos++) {
			if (!Character.isDigit(text.charAt(pos)) && text.charAt(pos) != '.') {
				return false;
			}
		}
		return true;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}
