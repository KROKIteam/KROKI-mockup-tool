package kroki.profil.property;

import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlProperty;
import kroki.uml_core_basic.UmlType;

/**
 * <code>VisibleProperty</code> represents a visible 
 * property which is placed on a panel
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class VisibleProperty extends VisibleElement implements UmlProperty {
	
	private static final long serialVersionUID = 1L;

    /*VisibleProperty metaclass properties*/
    protected String columnLabel;
    protected boolean labelToCode;
    protected String displayFormat;
    protected boolean representative;
    protected boolean autoGo;
    protected boolean disabled;
    protected String defaultValue;
    protected String dataType;
    /*Property metaclass properties*/
    protected boolean isComposite = false;
    protected boolean isDerived = false;
    protected boolean isReadOnly = false;
    protected UmlProperty opposite = null;
    protected UmlClass umlClass;
    /*TypedElement metaclass properties*/
    protected UmlType umlType;
    /*MultiplicityElement metaclass properties*/
    protected boolean isOrdered;
    protected boolean isUnique;
    protected int lower;
    protected int upper;

    protected String enumeration;
    
    /*****************/
    /*Constructors   */
    /*****************/
    public VisibleProperty(String columnLabel, String displayFormat, boolean representative, boolean autoGo, boolean disabled, String defaultValue) {
        this.columnLabel = columnLabel;
        this.displayFormat = displayFormat;
        this.representative = representative;
        this.autoGo = autoGo;
        this.disabled = disabled;
        this.defaultValue = defaultValue;
        this.dataType = "String";
        this.lower = 1;
    }

    public VisibleProperty(String label, boolean visible, ComponentType componentType, String columnLabel, String displayFormat, boolean representative, boolean autoGo, boolean disabled, String defaultValue) {
        super(label, visible, componentType);
        this.columnLabel = columnLabel;
        this.displayFormat = displayFormat;
        this.representative = representative;
        this.autoGo = autoGo;
        this.disabled = disabled;
        this.defaultValue = defaultValue;
        this.dataType = "String";
        this.lower = 1;
        //System.out.println(umlClass);
    }

    public VisibleProperty() {
        super();
    }

    public VisibleProperty(String label, boolean visible, ComponentType componentType) {
        super(label, visible, componentType);
        this.lower = 1;
        //System.out.println(umlClass);
    }

    @Override
    public String toString() {
        return "VisibleProperty{" + label + '}';
    }

    /****************************************/
    /*UmlProperty interface methods*/
    /****************************************/
    public String getDefault() {
        return defaultValue;
    }

    public boolean isComposite() {
        return isComposite;
    }

    public boolean isDerived() {
        return isDerived;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public UmlProperty opposite() {
        return opposite;
    }

    public void setComposite(boolean isComposite) {
        this.isComposite = isComposite;
    }

    public void setDefault(String def) {
        this.defaultValue = def;
    }

    public void setDerived(boolean isDerived) {
        this.isDerived = isDerived;
    }

    public void setOpposite(UmlProperty opposite) {
        this.opposite = opposite;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public void setUmlClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public UmlClass umlClass() {
        return umlClass;
    }

    public void setType(UmlType umlType) {
        this.umlType = umlType;
    }

    public UmlType type() {
        return umlType;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public int lower() {
        return lower;
    }

    public int upper() {
        return upper;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public void setOrdered(boolean ordered) {
        this.isOrdered = ordered;
    }

    public void setUnique(boolean unique) {
        this.isUnique = unique;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    /**********************/
    /*GETTERS AND SETTERS*/
    /********************/
    public boolean isAutoGo() {
        return autoGo;
    }

    public void setAutoGo(boolean autoGo) {
        this.autoGo = autoGo;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        this.component.setEnabled(!disabled);
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public boolean isRepresentative() {
        return representative;
    }

    public void setRepresentative(boolean representative) {
        this.representative = representative;
    }

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(String enumeration) {
		this.enumeration = enumeration;
	}

	public boolean isLabelToCode() {
		return labelToCode;
	}

	public void setLabelToCode(boolean labelToCode) {
		this.labelToCode = labelToCode;
	}
}
