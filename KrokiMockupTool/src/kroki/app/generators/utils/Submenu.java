package kroki.app.generators.utils;

/**
 * Class that represents menu item (JMenuItem for desktop or {@code<li>} HTML list item for web application)
 * @author mrd
 */
public class Submenu {
	
	String activate;
	String label;
	String panel_type;
	
	public Submenu() {
	}

	public Submenu(String activate, String label, String panel_type) {
		super();
		this.activate = activate;
		this.label = label;
		this.panel_type = panel_type;
	}

	public String getActivate() {
		return activate;
	}

	public void setActivate(String activate) {
		this.activate = activate;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPanel_type() {
		return panel_type;
	}

	public void setPanel_type(String panel_type) {
		this.panel_type = panel_type;
	}
	
}
