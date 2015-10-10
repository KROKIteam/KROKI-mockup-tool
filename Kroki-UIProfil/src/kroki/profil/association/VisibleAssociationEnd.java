package kroki.profil.association;

import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.std.StdDataSettings;
import kroki.profil.panel.std.StdPanelSettings;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlProperty;
import kroki.uml_core_basic.UmlType;

/**
 * Stereotype VisibleAssociationEnd represents an association end. It is extended
 * by <code>Next</code>, <code>Zoom</code> and <code>Hierarchy</code>
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class VisibleAssociationEnd extends VisibleElement implements UmlProperty {

	private static final long serialVersionUID = 1L;
	
	/**Indicates if new data can be added*/
    protected boolean add = true;
	/**Indicates if data can be updated*/
    protected boolean update = true;
    /**Indicates if data can be copied*/
    protected boolean copy = true
    /**Indicates if data can be deleted*/;
    protected boolean delete = true;
    /**Indicates if data can be searched*/
    protected boolean search = true;
    /**Indicates if view can be changed between tabular and one record per screen*/ 
    protected boolean changeMode = true;
    /**Indicates if navigation is enabled (next, previous, first, last)*/
    protected boolean dataNavigation = true;
    /**Standard panel settings*/
    protected transient StdPanelSettings stdPanelSettings = new StdPanelSettings();
    /**Standard data settings*/
    protected transient StdDataSettings stdDataSettings = new StdDataSettings();
    /**Activation panel*/
    protected VisibleClass activationPanel;
    /**Target panel*/
    protected VisibleClass targetPanel;

    /*PROPERTY METACLASS PROPERTIES*/
    protected boolean isComposite = false;
    protected boolean isDerived = false;
    protected boolean isReadOnly = false;
    protected UmlProperty opposite = null;
    protected UmlClass umlClass;
    /*TYPEDELEMENT METACLASS PROPERTIES*/
    protected UmlType umlType;
    /*MULTIPLICITYELEMENT METACLASS PROPERTIES*/
    protected boolean isOrdered;
    protected boolean isUnique;
    protected int lower;
    protected int upper;

    public VisibleAssociationEnd() {
    }

    public VisibleAssociationEnd(String label, boolean visible, ComponentType componentType) {
        super(label, visible, componentType);
    }
    
    @Override
    public String toString(){
    	return label + " [ " + activationPanel + " ]" ;
    }

    /**************************************/
    /*UmlProperty METHODS*/
    /**************************************/
    public String getDefault() {
        return "";
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
        //this.defaultValue = def;
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

    /*****************/
    /*GETTERS AND SETTERS*/
    /*****************/
    public StdDataSettings getStdDataSettings() {
    	if (stdDataSettings == null)
    		stdDataSettings = new StdDataSettings();
        return stdDataSettings;
    }

    public void setStdDataSettings(StdDataSettings stdDataSettings) {
        this.stdDataSettings = stdDataSettings;
    }

    public StdPanelSettings getStdPanelSettings() {
    	if (stdPanelSettings == null)
    		stdPanelSettings = new StdPanelSettings();
        return stdPanelSettings;
    }

    public void setStdPanelSettings(StdPanelSettings stdPanelSettings) {
        this.stdPanelSettings = stdPanelSettings;
    }

    public VisibleClass getActivationPanel() {
        return activationPanel;
    }

    public void setActivationPanel(VisibleClass activationPanel) {
        this.activationPanel = activationPanel;
    }

    public VisibleClass getTargetPanel() {
        return targetPanel;
    }

    public void setTargetPanel(VisibleClass targetPanel) {
        this.targetPanel = targetPanel;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isChangeMode() {
        return changeMode;
    }

    public void setChangeMode(boolean changeMode) {
        this.changeMode = changeMode;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public boolean isDataNavigation() {
        return dataNavigation;
    }

    public void setDataNavigation(boolean dataNavigation) {
        this.dataNavigation = dataNavigation;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
