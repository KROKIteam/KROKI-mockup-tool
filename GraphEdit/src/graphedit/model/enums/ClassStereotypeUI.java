package graphedit.model.enums;

public enum ClassStereotypeUI {
	STANDARD_PANEL ("Standard Panel"), PARENT_CHILD ("Parent Child");
	
	private final String name;
	
	private ClassStereotypeUI(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
}
