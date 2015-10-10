package bp.event;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class TextChangeListener {

	private transient Document document;
	
	public TextChangeListener(Document document) { 
		this.document = document;
	}
	
	public TextChangeListener() { }
	
    public void textChanged(String text) {
    	try {
            document.remove(0, document.getLength());
            document.insertString(0, text, null);
        } catch (final BadLocationException e) {
            e.printStackTrace();
        }
    }

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
    
}
