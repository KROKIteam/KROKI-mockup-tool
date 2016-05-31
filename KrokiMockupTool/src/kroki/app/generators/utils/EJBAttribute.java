package kroki.app.generators.utils;

import java.awt.Color;
import java.util.ArrayList;

public class EJBAttribute {

	private ArrayList<String> annotations;
	private String type;
	private String name;
	private String label;
	private String databaseName;
	private Boolean mandatory;
	private Boolean unique;
	private Boolean representative;
	private Enumeration enumeration;
	private ArrayList<EJBAttribute> columnRefs;
	private Integer backgroundRGB;
	private Integer foregraundRGB;
	private Boolean visible;
	private Boolean readOnly;
	private Boolean autoGo;
	private Integer lenght;
	private Boolean wrap;
	private Integer length;
	private Integer precision;
	private Integer positionX;
	private Integer positionY;
	
	public EJBAttribute(ArrayList<String> annotations, String type, String name,
			String label, String databaseName, Integer length, Integer precision, 
			Boolean mandatory,
			Boolean unique, Boolean representative, Enumeration enumeration, Boolean visible,
			Boolean readOnly, Boolean autoGo,
			Integer backgroundRGB, Integer foregraundRGB, Integer lenght, Boolean wrap,
			Integer positionX, Integer positionY) {
		super();
		this.annotations = annotations;
		this.type = type;
		this.name = name;
		this.label = label;
		this.databaseName = databaseName;
		this.mandatory = mandatory;
		this.unique = unique;
		this.representative = representative;
		this.enumeration = enumeration;
		this.visible = visible;
        this.backgroundRGB = backgroundRGB;
		this.foregraundRGB = foregraundRGB;
		this.readOnly = readOnly;
		this.autoGo = autoGo;
		this.lenght = lenght;
		this.wrap = wrap;
		this.positionX = positionX;
		this.positionY = positionY;
		columnRefs = new ArrayList<EJBAttribute>();
	}
	
	
	public EJBAttribute(ArrayList<String> annotations, String type, String name,
			String label, String databaseName, Integer length, Integer precision, Boolean mandatory,
			Boolean unique, Boolean representative, Enumeration enumeration) {
		super();
		this.annotations = annotations;
		this.type = type;
		this.name = name;
		this.label = label;
		this.databaseName = databaseName;
		this.mandatory = mandatory;
		this.unique = unique;
		this.representative = representative;
		this.enumeration = enumeration;
		this.visible = true;
		this.autoGo = false;
		this.readOnly = false;
        this.backgroundRGB = Color.WHITE.getRGB();
		this.foregraundRGB = Color.BLACK.getRGB();
		this.lenght = 50;
		this.wrap = false;
		columnRefs = new ArrayList<EJBAttribute>();
		this.positionX = 0;
		this.positionY = 0;
	}

	public ArrayList<String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(ArrayList<String> annotations) {
		this.annotations = annotations;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	public Boolean getRepresentative() {
		return representative;
	}

	public void setRepresentative(Boolean representative) {
		this.representative = representative;
	}

	public Enumeration getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(Enumeration enumeration) {
		this.enumeration = enumeration;
	}

	public ArrayList<EJBAttribute> getColumnRefs() {
		return columnRefs;
	}

	public void setColumnRefs(ArrayList<EJBAttribute> columnRefs) {
		this.columnRefs = columnRefs;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Integer getBackgroundRGB() {
		return backgroundRGB;
	}

	public void setBackgroundRGB(Integer backgroundRGB) {
		this.backgroundRGB = backgroundRGB;
	}

	public Integer getForegraundRGB() {
		return foregraundRGB;
	}

	public void setForegraundRGB(Integer foregraundRGB) {
		this.foregraundRGB = foregraundRGB;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getAutoGo() {
		return autoGo;
	}

	public void setAutoGo(Boolean autoGo) {
		this.autoGo = autoGo;
	}

	public Boolean getWrap() {
		return wrap;
	}

	public void setWrap(Boolean wrap) {
		this.wrap = wrap;
	}

	public Integer getLenght() {
		return lenght;
	}

	public void setLenght(Integer lenght) {
		this.lenght = lenght;
	}

	public Integer getPositionX() {
		return positionX;
	}

	public void setPositionX(Integer positionX) {
		this.positionX = positionX;
	}

	public Integer getPositionY() {
		return positionY;
	}

	public void setPositionY(Integer positionY) {
		this.positionY = positionY;
	}


	public Integer getLength() {
		return length;
	}


	public void setLength(Integer length) {
		this.length = length;
	}


	public Integer getPrecision() {
		return precision;
	}


	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
}
