package adapt.enumerations;

public enum OperationType {
	BUSINESSTRANSACTION("BUSINESSTRANSACTION"),
	VIEWREPORT("VIEWREPORT"),
	JAVAOPERATION("JAVAOPERATION");
	
	String label;
	
	OperationType() {
	}
	
	OperationType(String label) {
		this.label = label;
	}
}
