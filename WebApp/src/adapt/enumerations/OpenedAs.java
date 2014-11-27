package adapt.enumerations;

public enum OpenedAs {
	DEFAULT("DEFAULT"),
	ZOOM("ZOOM"),
	NEXT("NEXT");
	
	String label;
	
	OpenedAs() {
	}
	
	OpenedAs(String label) {
		this.label = label;
	}
}
