package kroki.commons.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Filter which only accepts digits
 * @author KROKI Team
 *
 */
public class OnlyDigitsDocumentFilter extends DocumentFilter{
	
	
	public OnlyDigitsDocumentFilter() {
		super();
	}

	/**
	 * Will be called when something is typed or pasted. Doesn't accept a text containing something
	 * other than digits to be entered
	 */
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
	throws BadLocationException
	{
		if (string == null)
			return;
		if (!allDigits(string)) {
			return;
		}


		//ok, insert
		fb.insertString(offset, string, attr);
	}

	/**
	 *Called when something is replaced
	 */
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
	throws BadLocationException
	{
		if (!allDigits(text.trim())) {
			return;
		}

		//ok, insert
		fb.replace(offset, length, text, attrs);
	}

	/**
	 * Check if all characters are digits
	 * @param text being checked
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

}
