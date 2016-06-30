package kroki.profil.utils;


import kroki.mockup.model.Composite;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlProperty;

/**
* Class contains <code>UIProperty</code> util methods 
* @author Kroki Team
*/
public class UIPropertyUtil {

	


	/**
	 * Adds new visible element to the visible elements list of the given visible class
	 * @param visibleElement Visible element
	 * @param panel Panel
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

	/**
	 * Adds new visible element to the visible elements list of the given visible class
	 * at the position specified with index
	 * @param visibleElement Visible element
	 * @param panel Panel
	 * @param index Position
	 */
	public static void addVisibleElement(VisibleClass panel, int index, VisibleElement visibleElement) {
		if (!panel.getVisibleElementList().contains(visibleElement)) {
			//visibleElement.setParentPanel(this);
			if (visibleElement instanceof UmlProperty) {
				((UmlProperty) visibleElement).setUmlClass(panel);
				if (index >= panel.getVisibleElementList().size())
					panel.getVisibleElementList().add(visibleElement);
				else
					panel.getVisibleElementList().add(index, visibleElement);
				
			} else if (visibleElement instanceof UmlOperation) {
				((UmlOperation) visibleElement).setUmlClass(panel);
				if (panel.getVisibleElementList().size() >= panel.getVisibleElementList().size())
					panel.getVisibleElementList().add(visibleElement);
				else
					panel.getVisibleElementList().add(index, visibleElement);
			} else {
				return;
			}
		}
	}

	/**
	 * Removes the visible element from the visible elements list of the given visible class
	 * @param panel Panel
	 * @param visibleElement Visible element
	 */
	public static void removeVisibleElement(VisibleClass panel, VisibleElement visibleElement) {
		if (panel.getVisibleElementList().contains(visibleElement)) {
			panel.getVisibleElementList().remove(visibleElement);
			((Composite) panel.getComponent()).removeChild(visibleElement.getComponent());
		}
	}

	/**
	 * Removes the visible element with the given index from the panel's list of visible elements
	 * and returns the element 
	 * @param index Element's position
	 * @param panel Panel
	 * @return removed element
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
	 * Finds the visible element with the given index in the panel's list of visible elements
	 * @param index Element's position
	 * @param panel Panel
	 * @return visible element
	 */
	public static VisibleElement getVisibleElementAt(VisibleClass panel, int index) {
		return panel.getVisibleElementList().get(index);
	}

	/**
	 * Returns the number of visible elements contained by the panel
	 * @return Number of visible elements
	 */
	public static  int getVisibleElementNum(VisibleClass panel) {
		return panel.getVisibleElementList().size();
	}

	/**
	 * Adds visible element to the panel's list of visible elements
	 * Sets hierarchy properties if the element is a hierarchy
	 * @param panel Panel
	 * @param visibleElement Visible element
	 */
	public static void addVisibleElement(ParentChild panel, VisibleElement visibleElement) {
		if (visibleElement instanceof Hierarchy) {
			Hierarchy hierarchy = (Hierarchy) visibleElement;
			HierarchyUtil.setHierarchhyAttributes(panel, hierarchy);
		}
		addVisibleElement((VisibleClass)panel, visibleElement);
	}


	/**
	 * Adds visible element to the panel's list of visible elements at the position
	 * specified with <code>index</code>
	 * Sets hierarchy properties if the element is a hierarchy
	 * @param panel Panel
	 * @param index Position
	 * @param visibleElement Visible element
	 */
	public static void addVisibleElement(ParentChild panel, int index, VisibleElement visibleElement) {
		if (visibleElement instanceof Hierarchy) {
			Hierarchy hierarchy = (Hierarchy) visibleElement;
			HierarchyUtil.setHierarchhyAttributes(panel, hierarchy);
		}
		addVisibleElement((VisibleClass)panel, index, visibleElement);
	}

	

}
