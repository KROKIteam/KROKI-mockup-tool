package graphedit.model.components;

public enum AttributeTypeUI {
	
	TEXT_FILED("TextField"), COMBO_BOX("ComboBox"), TEXT_AREA("TextArea"), CHECK_BOX("CheckBox");
	
	private final String name;

	private  AttributeTypeUI(String name){
		this.name = name;
	}

	public String toString(){
		return name;
	}

}
