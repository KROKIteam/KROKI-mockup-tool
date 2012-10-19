/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel;

import java.util.ArrayList;
import java.util.List;
import kroki.profil.VisibleElement;

/**
 * <code>ContainerPanel</code> označava složeni panel
 * koji može posedovati druge panele (jednostavne i/ili složene), kao i
 * proizvoljan broj sopstvenih obeležja i metoda. Definiše pomoćne atribute,
 * metode i ograničenja za potrebe svojih naslednika
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ContainerPanel extends VisibleClass {

    @Override
    public void update() {
    }

    public List<VisibleClass> containedPanels() {
        List<VisibleClass> containedPanels = new ArrayList<VisibleClass>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof VisibleClass) {
                containedPanels.add((VisibleClass) visibleElement);
            }
        }
        return containedPanels;
    }

    public List<ContainerPanel> containedContainers() {
        List<ContainerPanel> containedContainers = new ArrayList<ContainerPanel>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof VisibleClass) {
                containedContainers.add((ContainerPanel) visibleElement);
            }
        }
        return containedContainers;
    }
}
