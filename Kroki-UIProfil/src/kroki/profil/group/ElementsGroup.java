/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.group;

import java.util.ArrayList;
import java.util.List;

import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.FlowLayoutManager;
import kroki.mockup.model.layout.FreeLayoutManager;
import kroki.mockup.model.layout.LayoutManager;
import kroki.mockup.model.layout.VerticalLayoutManager;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlProperty;
import kroki.uml_core_basic.UmlType;

/**
 * Klasa  <code>ElementsGroup</code> oznaÄ�ava obeleÅ¾je klase
 * VisibleClass koje se koristi za grupisanje njenih elemenata
 * (obeleÅ¾ja, metoda, veza), formirajuÄ‡i na taj naÄ�in semantiÄ�ke celine koje se
 * preslikavaju na grupe komponenti korisniÄ�kog interfejsa u okviru panela
 * pridruÅ¾enog klasi.
 * @author Vladan MarseniÄ‡ (vladan.marsenic@gmail.com)
 */
public class ElementsGroup extends VisibleElement implements UmlProperty {

	private static final long serialVersionUID = 1L;
	
    /**Lista vidljivih elemenata*/
    protected List<VisibleElement> visibleElementList = new ArrayList<VisibleElement>();
    /**Orjentacija elemenata unutar grupe*/
    protected GroupOrientation groupOrientation;
    /**Poravnavanje elemenata unutar grupe*/
    protected GroupAlignment groupAlignment = GroupAlignment.left;
    /**Lokacija grupe*/
    protected GroupLocation groupLocation;
    /*OBELEŽJA METAKLASE PROPERTY*/
    protected boolean isComposite = false;
    protected boolean isDerived = false;
    protected boolean isReadOnly = false;
    protected UmlProperty opposite = null;
    protected UmlClass umlClass;
    /*OBELEŽJA METAKLASE TYPEDELEMENT*/
    protected UmlType umlType;
    /*OBELŽJA METAKLASE MULTIPLICITYELEMENT*/
    protected boolean isOrdered;
    protected boolean isUnique;
    protected int lower;
    protected int upper;

    public ElementsGroup() {
        super("ElementsGroup", true, ComponentType.PANEL);
    }

    public ElementsGroup(String label, boolean visible, ComponentType componentType) {
        super(label, visible, componentType);
    }

    public ElementsGroup(String label, boolean visible) {
        super(label, visible, ComponentType.PANEL);
    }

    public ElementsGroup(String label, ComponentType componentType) {
        super(label, true, componentType);
    }

    /***************/
    /**JAVNE METODE*/
    /***************/
  

    /**************************************/
    /*IMPLEMENTIRANE METODE OD UmlProperty*/
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
        //TODO: ne znam sta da postavim za default value za grupu elemenata
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

    /******************/
    /**GETERI I SETERI*/
    /******************/
    public GroupAlignment getGroupAlignment() {
        return groupAlignment;
    }

    public void setGroupAlignment(GroupAlignment groupAlignment) {
        this.groupAlignment = groupAlignment;
        LayoutManager layoutManager = ((Composite) component).getLayoutManager();
        switch (groupAlignment) {
            case center: {
                layoutManager.setAlign(LayoutManager.CENTER);
            }
            break;
            case left: {
                layoutManager.setAlign(LayoutManager.LEFT);
            }
            break;
            case right: {
                layoutManager.setAlign(LayoutManager.RIGHT);
            }
            break;
        }
    }

    public GroupLocation getGroupLocation() {
        return groupLocation;
    }

    public void setGroupLocation(GroupLocation groupLocation) {
        this.groupLocation = groupLocation;
    }

    public GroupOrientation getGroupOrientation() {
        return groupOrientation;
    }

    public void setGroupOrientation(GroupOrientation groupOrientation) {
        this.groupOrientation = groupOrientation;
        LayoutManager layoutManager = null;
        switch (groupOrientation) {
            case area: {
                layoutManager = new FreeLayoutManager();
            }
            break;
            case horizontal: {
                layoutManager = new FlowLayoutManager();
            }
            break;
            case vertical: {
                layoutManager = new VerticalLayoutManager();
            }
            break;
        }
        switch (this.groupAlignment) {
            case center: {
                layoutManager.setAlign(LayoutManager.CENTER);
            }
            break;
            case left: {
                layoutManager.setAlign(LayoutManager.LEFT);
            }
            break;
            case right: {
                layoutManager.setAlign(LayoutManager.RIGHT);
            }
            break;
        }
        ((Composite) component).setLayoutManager(layoutManager);
    }

    public List<VisibleElement> getVisibleElementList() {
        return visibleElementList;
    }

    public void setVisibleElementList(List<VisibleElement> visibleElementList) {
        this.visibleElementList = visibleElementList;
    }
}
