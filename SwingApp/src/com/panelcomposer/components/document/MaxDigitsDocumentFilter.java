package com.panelcomposer.components.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Zadaje se maksimalan broj cifara
 * JTextField cijiem se dokumentu postavi ovaj filter moci ce da primi samo cifre
 * Koristi se kada je maksimalan sadrzaj od jedne do maxLength cifara, recimo za redni broj
 * @author Renata
 *
 */
public class MaxDigitsDocumentFilter extends DocumentFilter{
	/**
	 * Maksimalni broj cifara
	 */
	private int maxLength=0;
	
	
	public MaxDigitsDocumentFilter(int maxLength) {
		super();
		this.maxLength = maxLength;
	}

	/**
	 * Poziva se kada se nesto ukuca ili paste-uje
	 */
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
	throws BadLocationException
	{
		if (string == null)
			return;
		// Nista ne radi ako nisu sve cifre
		if (!allDigits(string)) {
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
		// Nista ne radi ako nisu sve cifre
		if (!allDigits(text.trim())) {
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
	 * Proverava da li su sve cifre
	 * @param text za koji se proverava
	 * @return
	 */
	private boolean allDigits(String text) {
		for (int pos=0; pos < text.length(); pos++) {
			if (!Character.isDigit(text.charAt(pos))) {
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
