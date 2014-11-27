package gui.menudesigner.model;

import java.io.Serializable;

public class Menu implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5529723233860087796L;
	protected Menu parent;

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

}
