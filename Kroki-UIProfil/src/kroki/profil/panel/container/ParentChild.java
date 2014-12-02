/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel.container;

import kroki.mockup.model.Composite;
import kroki.mockup.model.components.TitledContainer;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.ContainerPanel;

/**
 * <code>ParentChild</code> modeluje slozeni panel  ciji su
 * sastavni paneli organizovani u stablo, na nacin definisan HCI standardom. 
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 * @author Renata
 */
public class ParentChild extends ContainerPanel {
	
	
	private static final long serialVersionUID = 1L;

	private ElementsGroup propertiesPanel;
	private ElementsGroup operationsPanel;

	public ParentChild() {
		super();
		component = new TitledContainer("Parent child");
		component.getRelativePosition().setLocation(5, 5);
		component.getAbsolutePosition().setLocation(5, 5);
		component.getDimension().setSize(800, 500);
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
