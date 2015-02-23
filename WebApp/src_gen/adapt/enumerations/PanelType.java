package adapt.enumerations;

public enum PanelType {
	STANDARDPANEL("STANDARDPANEL"),
	PARENTCHILDPANEL("PARENTCHILDPANEL"),
	MANYTOMANYPANEL("MANYTOMANYPANEL");
	
	String label;
	
	PanelType() {
	}
	
	PanelType(String label) {
		this.label = label;
	}
}
