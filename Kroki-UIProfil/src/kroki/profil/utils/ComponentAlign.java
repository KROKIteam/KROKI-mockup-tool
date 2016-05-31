package kroki.profil.utils;


public enum ComponentAlign {
	ALIGN_LEFT("LEFT"), ALIGN_CENTER("CENTER"), ALIGN_RIGHT("RIGHT");
	
	private final String name;
	
	private ComponentAlign(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}