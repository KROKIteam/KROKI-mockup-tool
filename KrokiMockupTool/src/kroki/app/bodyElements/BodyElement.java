package kroki.app.bodyElements;

/**
 * <p>
 * Sadrzi element iz tela ogranicenja
 * @param value Sadrzi vrednost elementa
 * @param type Ime klase na koju se vrednost odnosi
 * @param valueType Tip elementa
 * </p>
 */
public class BodyElement {

	protected String value;  
	protected String type;
	protected String valueType;
	
	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public BodyElement(String v, String t){
		value=v;
		type=t;
	}
	public BodyElement(){}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
