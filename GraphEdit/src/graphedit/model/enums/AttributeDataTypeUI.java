package graphedit.model.enums;

public enum AttributeDataTypeUI {
	
	STRING("String"), INTEGER("Integer"), LONG("Long"), BIG_DECIMAL("BigDecimal"), BOOLEAN("Boolean"), 
	ENUMERATION("Enumeration"), DATE("Date");
	
	private final String name;

	private  AttributeDataTypeUI(String name){
		this.name = name;
	}

	public String toString(){
		return name;
	}

}
