package graphedit.model.components;

public enum MethodStereotypeUI {
	REPORT ("Report"), TRANSACTION ("Transaction");

	private final String name;

	private MethodStereotypeUI(String name){
		this.name = name;
	}

	public String toString(){
		return name;
	}

}
