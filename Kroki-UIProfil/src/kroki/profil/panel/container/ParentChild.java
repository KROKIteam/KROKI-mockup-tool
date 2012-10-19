/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel.container;

import java.util.ArrayList;
import java.util.List;
import kroki.mockup.model.Composite;
import kroki.mockup.model.components.TitledContainer;
import kroki.mockup.model.layout.BorderLayoutManager;
import kroki.mockup.model.layout.FlowLayoutManager;
import kroki.mockup.model.layout.FreeLayoutManager;
import kroki.mockup.model.layout.LayoutManager;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupAlignment;
import kroki.profil.group.GroupLocation;
import kroki.profil.group.GroupOrientation;
import kroki.profil.panel.ContainerPanel;
import kroki.profil.utils.settings.SettingsPanel;
import kroki.profil.utils.settings.VisibleClassSettings;

/**
 * <code>ParentChild</code> modeluje složeni panel  čiji su
 * sastavni paneli organizovani u stablo, na način definisan HCI standardom. 
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SettingsPanel(VisibleClassSettings.class)
public class ParentChild extends ContainerPanel {

    private ElementsGroup propertiesPanel;
    private ElementsGroup operationsPanel;

    public ParentChild() {
        super();
        component = new TitledContainer("Parent child");
        component.getRelativePosition().setLocation(5, 5);
        component.getAbsolutePosition().setLocation(5, 5);
        component.getDimension().setSize(800, 500);
        defaultGuiSettings();
    }

    private void defaultGuiSettings() {
        Composite root = ((Composite) component);
        root.setLayoutManager(new BorderLayoutManager());
        propertiesPanel = new ElementsGroup("properties", ComponentType.PANEL);
        propertiesPanel.setGroupLocation(GroupLocation.componentPanel);
        propertiesPanel.setGroupOrientation(GroupOrientation.area);
        LayoutManager propertiesLayout = new FreeLayoutManager();
        ((Composite) propertiesPanel.getComponent()).setLayoutManager(propertiesLayout);
        ((Composite) propertiesPanel.getComponent()).setLocked(true);
        operationsPanel = new ElementsGroup("operations", ComponentType.PANEL);
        operationsPanel.setGroupLocation(GroupLocation.operationPanel);
        operationsPanel.setGroupOrientation(GroupOrientation.horizontal);
        operationsPanel.setGroupAlignment(GroupAlignment.left);
        LayoutManager operationsLayout = new FlowLayoutManager();
        operationsLayout.setAlign(LayoutManager.LEFT);
        ((Composite) operationsPanel.getComponent()).setLayoutManager(operationsLayout);
        ((Composite) operationsPanel.getComponent()).setLocked(true);
        addVisibleElement(propertiesPanel);
        addVisibleElement(operationsPanel);
        root.addChild(propertiesPanel.getComponent(), BorderLayoutManager.CENTER);
        root.addChild(operationsPanel.getComponent(), BorderLayoutManager.SOUTH);
        update();
    }

    @Override
    public void update() {
        component.updateComponent();
        ((Composite) component).layout();
    }

    @Override
    public String toString() {
        return label;
    }

    /**************/
    /*JAVNE METODE*/
    /**************/
    /**Vraća sve elemente koji predstavljaju deo hijerarhijske strukture*/
    public List<Hierarchy> allContainedHierarchies() {
        List<Hierarchy> allContainedHierarchies = new ArrayList<Hierarchy>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof Hierarchy) {
                allContainedHierarchies.add((Hierarchy) visibleElement);
            }
        }
        return allContainedHierarchies;
    }

    /**Vraća broj elemenata hijerarhijske strukture*/
    public int getHierarchyCount() {
        int i = 0;
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof Hierarchy) {
                i++;
            }
        }
        return i;
    }

    /**Vraća koren hijerarhije. Ukoliko ga nema vraća null.*/
    public Hierarchy getHierarchyRoot() {
        Hierarchy root = null;
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof Hierarchy) {
                if (((Hierarchy) visibleElement).getLevel() == 1) {
                    root = (Hierarchy) visibleElement;
                    break;
                }
            }
        }
        return root;
    }

    /**Pronalazi sve hijerarhije čiji nivo je jednak prosleđenom parametru <code>level</code>*/
    public List<Hierarchy> allHierarchiesByLevel(int level) {
        List<Hierarchy> allHierarchiesByLevel = new ArrayList<Hierarchy>();
        for (VisibleElement visibleElement : visibleElementList) {
            if (visibleElement instanceof Hierarchy) {
                Hierarchy hierarchy = (Hierarchy) visibleElement;
                if (hierarchy.getLevel() == level) {
                    allHierarchiesByLevel.add((Hierarchy) visibleElement);
                }
            }
        }
        return allHierarchiesByLevel;
    }

    @Override
    public void addVisibleElement(VisibleElement visibleElement) {
        if (visibleElement instanceof Hierarchy) {
            Hierarchy hierarchy = (Hierarchy) visibleElement;
            int count = getHierarchyCount();
            if (count == 0) {
                hierarchy.setLevel(1);
                hierarchy.setHierarchyParent(null);
            } else if (count == 1) {
                hierarchy.setLevel(2);
                hierarchy.setHierarchyParent(getHierarchyRoot());
            } else {
                hierarchy.setLevel(-1);
            }
        }
        super.addVisibleElement(visibleElement);
    }

    @Override
    public void addVisibleElement(int index, VisibleElement visibleElement) {
        if (visibleElement instanceof Hierarchy) {
            Hierarchy hierarchy = (Hierarchy) visibleElement;
            int count = getHierarchyCount();
            if (count == 0) {
                hierarchy.setLevel(1);
            } else if (count == 1) {
                hierarchy.setLevel(2);
            } else {
                //ako ima dva ili vise elemenata
                //u pocetku ce biti nedefinisan dok se ne izabere targetPanel (prikikom cega ce se odrediti level na osnovu uzajamnih veza izmedju elemenata hijerarhije
                hierarchy.setLevel(-1);
            }
        }
        super.addVisibleElement(index, visibleElement);
    }

    @Override
    public void removeVisibleElement(VisibleElement visibleElement) {
        super.removeVisibleElement(visibleElement);
    }

    @Override
    public VisibleElement removeVisibleElement(int index) {
        return super.removeVisibleElement(index);
    }

    /*******************/
    /**GETERI I SETERI**/
    /*******************/
    public ElementsGroup getOperationsPanel() {
        return operationsPanel;
    }

    public void setOperationsPanel(ElementsGroup operationsPanel) {
        this.operationsPanel = operationsPanel;
    }

    public ElementsGroup getPropertiesPanel() {
        return propertiesPanel;
    }

    public void setPropertiesPanel(ElementsGroup propertiesPanel) {
        this.propertiesPanel = propertiesPanel;
    }
}
