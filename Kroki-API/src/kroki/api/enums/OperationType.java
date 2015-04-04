package kroki.api.enums;

public enum OperationType {

	REPORT ("Report"), TRANSACTION ("Transaction");

	private final String name;

	private OperationType(String name){
		this.name = name;
	}

	public String toString(){
		return name;
	}
}
