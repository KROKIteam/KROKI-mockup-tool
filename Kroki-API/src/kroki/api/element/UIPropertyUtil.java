package kroki.api.element;


import kroki.commons.camelcase.NamingUtil;
import kroki.mockup.model.Composite;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.VisibleProperty;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlProperty;

public class UIPropertyUtil {

	public static final int STANDARD_PANEL_PROPERTIES = 1;
	public static final int PARENTCHILD_PANEL_PROPERTIES = 0;

	/**
	 * Creates a new visible property and adds in to the given panel
	 * @param label Label of visible property
	 * @param visible Is property visible
	 * @param type Component type (text field, text area, check boc etc.)
	 * @param panel Visible panel to which the property is being added
	 * @param indexClass Class index of the property (-1 if the visible property is being created for the first time, old index in case of undo operation)
	 * @param indexGroup Group index of the property (-1 if the visible property is being created for the first time, old index in case of undo operation)
	 * @return Created visible property
	 */
	public static VisibleProperty makeVisiblePropertyAt(String label, boolean visible, ComponentType type, VisibleClass panel, int indexClass, int indexGroup) {
		NamingUtil namer = new NamingUtil();
		int propertiesGroup = getPropertiesGroupIndex(panel);
		if (propertiesGroup == -1)
			return null;
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(propertiesGroup);
		VisibleProperty property = new VisibleProperty(label, visible, type);	
		property.setName(label);
		if(type == ComponentType.TEXT_FIELD) 
			property.setDataType("String");
		property.setColumnLabel(namer.toDatabaseFormat(panel.getLabel(), label));

		if (indexGroup != -1)
			gr.addVisibleElement(indexGroup, property);
		else
			gr.addVisibleElement(property);
		if (indexClass != -1)
			addVisibleElement(panel, indexClass, property);
		else
			addVisibleElement(panel, property);
		return property;
	}


	/**
	 * Creates visible property by calling {@link #makeVisiblePropertyAt()} and passing -1 as class and group index
	 * @param label Label of visible property
	 * @param visible Is property visible
	 * @param type Component type (text field, text area, check boc etc.)
	 * @param panel Visible panel to which the property is being added
	 * @return Created visible property
	 */
	public static VisibleProperty makeVisibleProperty(String label, boolean visible, ComponentType type, VisibleClass panel){
		return makeVisiblePropertyAt(label, visible, type, panel, -1, -1);
	}


	/**
	 * Dodaje novi vidljivi element u listu svih vidljivih elemenata koje poseduje klasa.
	 * @param visibleElement vidljivi element
	 */
	public static void addVisibleElement(VisibleClass panel, VisibleElement visibleElement) {
		if (!panel.getVisibleElementList().contains(visibleElement)) {
			if (visibleElement instanceof UmlProperty) {
				((UmlProperty) visibleElement).setUmlClass(panel);
				panel.getVisibleElementList().add(visibleElement);
			} else if (visibleElement instanceof UmlOperation) {
				((UmlOperation) visibleElement).setUmlClass(panel);
				panel.getVisibleElementList().add(visibleElement);
			}
		}
	}

	public static void addVisibleElement(VisibleClass panel, int index, VisibleElement visibleElement) {
		if (!panel.getVisibleElementList().contains(visibleElement)) {
			//visibleElement.setParentPanel(this);
			if (visibleElement instanceof UmlProperty) {
				((UmlProperty) visibleElement).setUmlClass(panel);
				panel.getVisibleElementList().add(index, visibleElement);
			} else if (visibleElement instanceof UmlOperation) {
				((UmlOperation) visibleElement).setUmlClass(panel);
				panel.getVisibleElementList().add(index, visibleElement);
			} else {
				return;
			}
		}
	}

	/**
	 * BriÅ¡e vidljivi element iz liste svih vidljivih elemenata koje poseduje klasa.
	 * @param visibleElement vidljivi element
	 */
	public static void removeVisibleElement(VisibleClass panel, VisibleElement visibleElement) {
		if (panel.getVisibleElementList().contains(visibleElement)) {
			panel.getVisibleElementList().remove(visibleElement);
			((Composite) panel.getComponent()).removeChild(visibleElement.getComponent());
		}
	}

	/**
	 * BriÅ¡e vidljivi element sa odreÄ‘ene pozicije u listi svih vidljivih elemenata klase.
	 * @param index pozicija vidljivog elementa u listi
	 * @return vidljivi element koji je obrisan. U sluÄ�aju da je <code>index</code> van opsega povratna vrednost ove metode je <code>null</code>
	 */
	public static VisibleElement removeVisibleElement(VisibleClass panel, int index) {
		VisibleElement removed = null;
		if (index >= 0 || index < panel.getVisibleElementList().size()) {
			removed = panel.getVisibleElementList().remove(index);
			((Composite) panel.getComponent()).removeChild(removed.getComponent());
		}
		return removed;
	}

	/**
	 * VraÄ‡a vidljivi element iz liste svih vidljivih elemenata sa pozicije odreÄ‘ene prosleÄ‘enim indeksom.
	 * @param index indeks
	 * @return vidljivi element
	 */
	public static VisibleElement getVisibleElementAt(VisibleClass panel, int index) {
		return panel.getVisibleElementList().get(index);
	}

	/**
	 * VraÄ‡a broj vidljivih elemenata
	 * @return broj vidljivih elemenata
	 */
	public static  int getVisibleElementNum(VisibleClass panel) {
		return panel.getVisibleElementList().size();
	}

	public static void addVisibleElement(ParentChild panel, VisibleElement visibleElement) {
		if (visibleElement instanceof Hierarchy) {
			Hierarchy hierarchy = (Hierarchy) visibleElement;
			setHierarchhyAttributes(panel, hierarchy);
		}
		addVisibleElement((VisibleClass)panel, visibleElement);
	}


	public static void addVisibleElement(ParentChild panel, int index, VisibleElement visibleElement) {
		if (visibleElement instanceof Hierarchy) {
			Hierarchy hierarchy = (Hierarchy) visibleElement;
			setHierarchhyAttributes(panel, hierarchy);
		}
		addVisibleElement((VisibleClass)panel, index, visibleElement);
	}

	private static void setHierarchhyAttributes(ParentChild panel, Hierarchy hierarchy){
		if (hierarchy.getLevel() != 0)
			return;
		int count = panel.getHierarchyCount();
		if (count == 0) {
			hierarchy.setLevel(1);
			hierarchy.setHierarchyParent(null);
		} else if (count == 1) {
			hierarchy.setLevel(2);
			hierarchy.setHierarchyParent(panel.getHierarchyRoot());
		} else {
			//ako ima dva ili vise elemenata
			//u pocetku ce biti nedefinisan dok se ne izabere targetPanel 
			//(prikikom cega ce se odrediti level na osnovu uzajamnih veza izmedju elemenata hijerarhije
			hierarchy.setLevel(-1);
		}
	}


	private static int getPropertiesGroupIndex(VisibleClass panel){
		if (panel instanceof StandardPanel)
			return STANDARD_PANEL_PROPERTIES;
		if (panel instanceof ParentChild)
			return PARENTCHILD_PANEL_PROPERTIES;
		return -1; 
	}


}
