package graphedit.model.enums;

public enum ClassStereotypeUI {
	STANDARD_PANEL ("StandardPanel"), PARENT_CHILD ("ParentChild");
	
	private final String name;
	
	private ClassStereotypeUI(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
}
