package com.panelcomposer.components.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Ne dozvoljava unos vise od zadatog broja karaktera
 * @author xxx
 *
 */
public class MaxCharactersDocumentFilter extends DocumentFilter{
	/**
	 * Maksimalni broj karaktera
	 */
	private int maxLength=0;
	

	public MaxCharactersDocumentFilter(int maxLength) {
		super();
		this.maxLength = maxLength;
	}

	/**
	 * Poziva se kada se nesto ukuca ili paste-uje
	 */
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
	throws BadLocationException
	{

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
		// Nemoj dozvoliti da se insertuje vise od zeljene duzine
		if (text == null)
			return;
		int extra = text.length() - length;
		if (fb.getDocument().getLength() + extra >maxLength) {
			return;
		}

		// Sve ok, pozovi replace
		fb.replace(offset, length, text, attrs);
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}

