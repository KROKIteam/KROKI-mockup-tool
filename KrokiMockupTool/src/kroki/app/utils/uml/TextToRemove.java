package kroki.app.utils.uml;

import java.util.List;

/**
 * Saves the data necessary to determine
 * one text that will be removed from
 * the names of UML elements during 
 * import of UML diagram.
 *  
 * @author Zeljko Ivkovic (ivkovicszeljko@gmail.com)
 *
 */
public class TextToRemove {

	/**
	 * Text to be removed from the UML diagram elements.
	 */
	private String text;
	/**
	 * Is the text prefix in the names of the UML
	 * diagram elements.
	 */
	private boolean prefix;
	/**
	 * Is the text suffix in the names of the UML
	 * diagram elements.
	 */
	private boolean suffix;
	/**
	 * If the text should be removed from the 
	 * name of the package element.
	 */
	private boolean fromPackageElement;
	/**
	 * If the text should be removed from the 
	 * name of the class element.
	 */
	private boolean fromClassElement;
	/**
	 * If the text should be removed from the 
	 * name of the property element.
	 */
	private boolean fromPropertyElement;
	/**
	 * If the text should be removed from the 
	 * name of the operation element.
	 */
	private boolean fromOperationElement;
	
	/**
	 * Indicates if this text was already
	 * removed from the name of package,
	 * class, property or operation before
	 * the name was converted from camel case
	 * to human readable text.
	 */
	private boolean usedAlready;
	
	/**
	 * Sets the received text and sets that the text is
	 * a prefix and that it should be removed from the
	 * names of the package, class, property and operation
	 * elements.
	 * @param text
	 */
	public TextToRemove(String text){
		this(text,true,false,true,true,true,true);
	}
	
	public TextToRemove(String text, boolean prefix, boolean suffix,
			boolean fromPackageElement, boolean fromClassElement,
			boolean fromPropertyElement, boolean fromOperationElement) {
		super();
		this.text = text;
		this.prefix = prefix;
		this.suffix = suffix;
		this.fromPackageElement = fromPackageElement;
		this.fromClassElement = fromClassElement;
		this.fromPropertyElement = fromPropertyElement;
		this.fromOperationElement = fromOperationElement;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isPrefix() {
		return prefix;
	}
	public void setPrefix(boolean prefix) {
		this.prefix = prefix;
	}
	public boolean isSuffix() {
		return suffix;
	}
	public void setSuffix(boolean suffix) {
		this.suffix = suffix;
	}
	public boolean isFromPackageElement() {
		return fromPackageElement;
	}
	public void setFromPackageElement(boolean fromPackageElement) {
		this.fromPackageElement = fromPackageElement;
	}
	public boolean isFromClassElement() {
		return fromClassElement;
	}
	public void setFromClassElement(boolean fromClassElement) {
		this.fromClassElement = fromClassElement;
	}
	public boolean isFromPropertyElement() {
		return fromPropertyElement;
	}
	public void setFromPropertyElement(boolean fromPropertyElement) {
		this.fromPropertyElement = fromPropertyElement;
	}
	public boolean isFromOperationElement() {
		return fromOperationElement;
	}
	public void setFromOperationElement(boolean fromOperationElement) {
		this.fromOperationElement = fromOperationElement;
	}

	public boolean isUsedAlready() {
		return usedAlready;
	}

	public void setUsedAlready(boolean usedAlready) {
		this.usedAlready = usedAlready;
	}
}
