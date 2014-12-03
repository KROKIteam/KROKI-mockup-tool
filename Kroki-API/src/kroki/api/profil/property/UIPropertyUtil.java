package kroki.api.profil.property;


import kroki.mockup.model.Composite;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlProperty;

public class UIPropertyUtil {

	


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
			HierarchyUtil.setHierarchhyAttributes(panel, hierarchy);
		}
		addVisibleElement((VisibleClass)panel, visibleElement);
	}


	public static void addVisibleElement(ParentChild panel, int index, VisibleElement visibleElement) {
		if (visibleElement instanceof Hierarchy) {
			Hierarchy hierarchy = (Hierarchy) visibleElement;
			HierarchyUtil.setHierarchhyAttributes(panel, hierarchy);
		}
		addVisibleElement((VisibleClass)panel, index, visibleElement);
	}

	

}
