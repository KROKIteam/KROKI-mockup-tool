package kroki.profil.utils;

import java.util.ArrayList;
import java.util.List;

import kroki.profil.VisibleElement;
import kroki.profil.panel.ContainerPanel;
import kroki.profil.panel.VisibleClass;

public class ContainerPanelUtil {

	public static List<VisibleClass> containedPanels(ContainerPanel panel) {
		List<VisibleClass> containedPanels = new ArrayList<VisibleClass>();
		for (VisibleElement visibleElement : panel.getVisibleElementList()) {
			if (visibleElement instanceof VisibleClass) {
				containedPanels.add((VisibleClass) visibleElement);
			}
		}
		return containedPanels;
	}

	public static List<ContainerPanel> containedContainers(ContainerPanel panel) {
		List<ContainerPanel> containedContainers = new ArrayList<ContainerPanel>();
		for (VisibleElement visibleElement : panel.getVisibleElementList()) {
			if (visibleElement instanceof VisibleClass) {
				containedContainers.add((ContainerPanel) visibleElement);
			}
		}
		return containedContainers;
	}
}
