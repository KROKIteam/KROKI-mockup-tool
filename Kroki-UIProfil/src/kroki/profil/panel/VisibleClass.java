/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kroki.mockup.model.Composite;
import kroki.profil.BusinessProcessModelingSubject;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.property.Persistent;
import kroki.profil.property.VisibleProperty;
import kroki.profil.utils.settings.SettingsPanel;
import kroki.profil.utils.settings.VisibleClassSettings;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlProperty;

/**
 * Označava klasu koja se preslikava na panel (obični ili tabulatorski) u okviru korisničkog interfejsa aplikacije.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SettingsPanel(VisibleClassSettings.class)
public class VisibleClass extends BusinessProcessModelingSubject implements UmlClass {

    /**Indikator modalnosti klase*/
    protected boolean modal = true;
    /**Lista svih elemenata koje poseduje klasa*/
    protected List<VisibleElement> visibleElementList = new ArrayList<VisibleElement>();
    /*OBELEÅ½JA METAKLASE CLASS*/
    protected boolean isAbstract;
    protected UmlPackage umlPackage;
    //Component counter map used for incremental component naming
    HashMap<ComponentType, Integer> componentCounts;
    
    /*****************/
    /*Konstruktori   */
    /*****************/
    public VisibleClass(boolean modal) {
        super();
        this.modal = modal;
        componentCounts = new HashMap<ComponentType, Integer>();
    }

    public VisibleClass(String label, boolean visible, ComponentType componentType, boolean modal) {
        super(label, visible, componentType);
        this.modal = modal;
        componentCounts = new HashMap<ComponentType, Integer>();
    }

    public VisibleClass() {
        super();
        this.modal = true;
        componentCounts = new HashMap<ComponentType, Integer>();
    }

    /**************/
    /*JAVNE METODE*/
    /**************/
    /**
     * Dodaje novi vidljivi element u listu svih vidljivih elemenata koje poseduje klasa.
     * @param visibleElement vidljivi element
     */
    public void addVisibleElement(VisibleElement visibleElement) {
        if (!visibleElementList.contains(visibleElement)) {
            if (visibleElement instanceof UmlProperty) {
                ((UmlProperty) visibleElement).setUmlClass(this);
                visibleElementList.add(visibleElement);
            } else if (visibleElement instanceof UmlOperation) {
                ((UmlOperation) visibleElement).setUmlClass(this);
                visibleElementList.add(visibleElement);
            }
        }
    }

    public void addVisibleElement(int index, VisibleElement visibleElement) {
        if (!visibleElementList.contains(visibleElement)) {
            //visibleElement.setParentPanel(this);
            if (visibleElement instanceof UmlProperty) {
                ((UmlProperty) visibleElement).setUmlClass(this);
                visibleElementList.add(index, visibleElement);
            } else if (visibleElement instanceof UmlOperation) {
                ((UmlOperation) visibleElement).setUmlClass(this);
                visibleElementList.add(index, visibleElement);
            } else {
                return;
            }
        }
    }

    /**
     * BriÅ¡e vidljivi element iz liste svih vidljivih elemenata koje poseduje klasa.
     * @param visibleElement vidljivi element
     */
    public void removeVisibleElement(VisibleElement visibleElement) {
        if (visibleElementList.contains(visibleElement)) {
            visibleElementList.remove(visibleElement);
            ((Composite) component).removeChild(visibleElement.getComponent());
        }
    }

    /**
     * BriÅ¡e vidljivi element sa odreÄ‘ene pozicije u listi svih vidljivih elemenata klase.
     * @param index pozicija vidljivog elementa u listi
     * @return vidljivi element koji je obrisan. U sluÄ�aju da je <code>index</code> van opsega povratna vrednost ove metode je <code>null</code>
     */
    public VisibleElement removeVisibleElement(int index) {
        VisibleElement removed = null;
        if (index >= 0 || index < visibleElementList.size()) {
            removed = visibleElementList.remove(index);
            ((Composite) component).removeChild(removed.getComponent());
        }
        return removed;
    }

    /**
     * VraÄ‡a vidljivi element iz liste svih vidljivih elemenata sa pozicije odreÄ‘ene prosleÄ‘enim indeksom.
     * @param index indeks
     * @return vidljivi element
     */
    public VisibleElement getVisibleElementAt(int index) {
        return visibleElementList.get(index);
    }

    /**
     * VraÄ‡a broj vidljivih elemenata
     * @return broj vidljivih elemenata
     */
    public int getVisibleElementNum() {
        return visibleElementList.size();
    }

    /**
     * Pronalazi vidljiv element Ä�ija se grafiÄ�ka komponenta nalazi na prosleÄ‘enoj lokaciji
     * @param point lokacija
     * @return vidljivi element
     */
    public VisibleElement getVisibleElementAtPoint(Point point) {
        VisibleElement returnElement = null;
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof ElementsGroup) {
                ElementsGroup elementsGroup = (ElementsGroup) visibleElement;
                if (elementsGroup.getComponent().contains(point)) {
                    returnElement = elementsGroup.getVisibleElementAtPoint(point);
                    if (returnElement == null) {
                        returnElement = elementsGroup;
                    }
                    break;
                }
            } else {
                if (visibleElement.getComponent().contains(point)) {
                    returnElement = visibleElement;
                    break;
                }
            }
        }
        if (returnElement == null) {
            if (this.getComponent().contains(point)) {
                returnElement = this;
            }
        }
        return returnElement;
    }

    public ElementsGroup getElementsGroupAtPoint(Point point) {
        ElementsGroup returnElement = null;
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof ElementsGroup) {
                ElementsGroup elementsGroup = (ElementsGroup) visibleElement;
                if (elementsGroup.getComponent().contains(point)) {
                    returnElement = elementsGroup.getElementsGroupAtPoint(point);
                    if (returnElement == null) {
                        returnElement = elementsGroup;
                    }
                    break;
                }
            }
        }
        return returnElement;
    }

    /**
     * VraÄ‡a sve vidljiva polja
     * @return
     */
    public List<VisibleProperty> containedProperties() {
        List<VisibleProperty> visiblePropertyList = new ArrayList<VisibleProperty>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof VisibleProperty) {
                visiblePropertyList.add((VisibleProperty) visibleElement);
            }
        }
        return visiblePropertyList;
    }

    /**VraÄ‡a listu svih perzistentnih obelezja klase*/
    public List<Persistent> containedPersistents() {
        List<Persistent> persistentList = new ArrayList<Persistent>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof Persistent) {
                persistentList.add((Persistent) visibleElement);
            }
        }
        return persistentList;
    }

    /**
     * VraÄ‡a sve operacije
     * @return
     */
    public List<VisibleOperation> containedOperations() {
        List<VisibleOperation> visibleOperationList = new ArrayList<VisibleOperation>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof VisibleOperation) {
                visibleOperationList.add((VisibleOperation) visibleElement);
            }
        }
        return visibleOperationList;
    }

    /**VraÄ‡a listu svih izvesstaja*/
    public List<Report> containedReports() {
        List<Report> reportList = new ArrayList<Report>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof Report) {
                reportList.add((Report) visibleElement);
            }
        }
        return reportList;
    }

    /**VraÄ‡a listu svih transakcija*/
    public List<Transaction> containedTransactions() {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof Transaction) {
                transactionList.add((Transaction) visibleElement);
            }
        }
        return transactionList;
    }

    /**
     * VraÄ‡a sve krajeve asocijacije
     * @return
     */
    public List<VisibleAssociationEnd> containedAssociationEnds() {
        List<VisibleAssociationEnd> visibleAssociationEndList = new ArrayList<VisibleAssociationEnd>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof VisibleAssociationEnd) {
                visibleAssociationEndList.add((VisibleAssociationEnd) visibleElement);
            }
        }
        return visibleAssociationEndList;
    }

    /**VraÄ‡a listu svih zumova*/
    public List<Zoom> containedZooms() {
        List<Zoom> zoomList = new ArrayList<Zoom>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof Zoom) {
                zoomList.add((Zoom) visibleElement);
            }
        }
        return zoomList;
    }

    /**VraÄ‡a listu sih nekstova*/
    public List<Next> containedNexts() {
        List<Next> nextList = new ArrayList<Next>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof Next) {
                nextList.add((Next) visibleElement);
            }
        }
        return nextList;
    }
    
	 /**Vraca listu hijerarhija*/
	 public List<Hierarchy> containedHierarchies() {
		 List<Hierarchy> hierarchyList = new ArrayList<Hierarchy>();
		 for (VisibleElement visibleElement : visibleElementList) {
			 if (visibleElement instanceof Hierarchy) {
				 hierarchyList.add((Hierarchy) visibleElement);
			 }
		 }
		 return hierarchyList;
	 }


    /**
     * OsveÅ¾ava stanje svojih grafiÄ�kih komponenti.
     */
    @Override
    public void update() {
        super.update();
    }

    /**
     * Gets number of components with specified type
     */
    public Integer getComponentCount(ComponentType type) {
    	Integer count = 0;
    	if(componentCounts.get(type) != null) {
    		count = componentCounts.get(type);
    	}
    	return count;
    }
    
    /**
     * Increments number of components with specfied type and returns incremented count
     * If that components type is not found in count map, it is added with count set to 1
     */
    public Integer incrementCount(ComponentType type) {
    	Integer count = componentCounts.get(type);
    	if(count != null) {
    		count++;
    		componentCounts.put(type, count);
    		return count;
    	}else {
    		componentCounts.put(type, 1);
    		return 1;
    	}
    }
    
    public void decrementCount(ComponentType type) {
    	Integer count = componentCounts.get(type);
    	if(count > 0) {
    		count--;
    		componentCounts.put(type, count);
    	}
    }
    /*******************************************/
    /*Implementirane metode interfejsa UmlClass*/
    /*******************************************/
    public boolean isAbstract() {
        return isAbstract;
    }

    /**VraÄ‡a listu svih atrubuta klase koji implmentiraju interfejs UmlProperty*/
    public List<UmlProperty> ownedAttribute() {
        List<UmlProperty> umlPropertyList = new ArrayList<UmlProperty>();
        for (int i = 0; i < visibleElementList.size(); i++) {
            VisibleElement visibleElement = visibleElementList.get(i);
            if (visibleElement instanceof UmlProperty) {
                umlPropertyList.add((UmlProperty) visibleElement);
            }
        }
        return umlPropertyList;
    }

    /**VraÄ‡a listu svih operacija klase koji implmentiraju interfejs UmlOperation*/
    public List<UmlOperation> ownedOperation() {
        List<UmlOperation> umlOperationList = new ArrayList<UmlOperation>();
        for (int i = 0; i < visibleElementList.size(); i++) {
            VisibleElement visibleElement = visibleElementList.get(i);
            if (visibleElement instanceof UmlOperation) {
                umlOperationList.add((UmlOperation) visibleElement);
            }
        }
        return umlOperationList;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public List<UmlClass> superClass() {
        return null;
    }

    public UmlPackage umlPackage() {
        return umlPackage;
    }

    public void setUmlPackage(UmlPackage umlPackage) {
        this.umlPackage = umlPackage;
    }

    /*****************/
    /*GETERI I SETERI*/
    /*****************/
    public boolean isModal() {
        return modal;
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public List<VisibleElement> getVisibleElementList() {
        return visibleElementList;
    }

    public void setVisibleElementList(List<VisibleElement> visibleElementList) {
        this.visibleElementList = visibleElementList;
    }

    @Override
    public String toString() {
        return label;
    }
}