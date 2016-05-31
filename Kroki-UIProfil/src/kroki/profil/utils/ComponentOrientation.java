package kroki.profil.utils;

public enum ComponentOrientation {
	ORIENTATION_HORIZONTAL("HORIZONTAL"), ORIENTATION_VERTICAL("VERTICAL"), ORIENTATION_FREE("FREE");
	
	private final String name;
	
	private ComponentOrientation(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}