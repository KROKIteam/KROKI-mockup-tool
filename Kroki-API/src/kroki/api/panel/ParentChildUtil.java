package kroki.api.panel;

import kroki.api.element.UIPropertyUtil;
import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.BorderLayoutManager;
import kroki.mockup.model.layout.FlowLayoutManager;
import kroki.mockup.model.layout.FreeLayoutManager;
import kroki.mockup.model.layout.LayoutManager;
import kroki.profil.ComponentType;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupAlignment;
import kroki.profil.group.GroupLocation;
import kroki.profil.group.GroupOrientation;
import kroki.profil.panel.container.ParentChild;

public class ParentChildUtil {


	public static void defaultGuiSettings(ParentChild panel) {
		Composite root = ((Composite) panel.getComponent());
		root.setLayoutManager(new BorderLayoutManager());
		panel.setPropertiesPanel(new ElementsGroup("properties", ComponentType.PANEL));
		panel.getPropertiesPanel().setGroupLocation(GroupLocation.componentPanel);
		panel.getPropertiesPanel().setGroupOrientation(GroupOrientation.area);
		LayoutManager propertiesLayout = new FreeLayoutManager();
		((Composite) panel.getPropertiesPanel().getComponent()).setLayoutManager(propertiesLayout);
		((Composite) panel.getPropertiesPanel().getComponent()).setLocked(true);
		panel.setOperationsPanel(new ElementsGroup("operations", ComponentType.PANEL));
		panel.getOperationsPanel().setGroupLocation(GroupLocation.operationPanel);
		panel.getOperationsPanel().setGroupOrientation(GroupOrientation.horizontal);
		panel.getOperationsPanel().setGroupAlignment(GroupAlignment.left);
		LayoutManager operationsLayout = new FlowLayoutManager();
		operationsLayout.setAlign(LayoutManager.LEFT);
		((Composite) panel.getOperationsPanel().getComponent()).setLayoutManager(operationsLayout);
		((Composite) panel.getOperationsPanel().getComponent()).setLocked(true);
		UIPropertyUtil.addVisibleElement(panel, panel.getPropertiesPanel());
		UIPropertyUtil.addVisibleElement(panel, panel.getPropertiesPanel());
		root.addChild(panel.getPropertiesPanel().getComponent(), BorderLayoutManager.CENTER);
		root.addChild(panel.getPropertiesPanel().getComponent(), BorderLayoutManager.SOUTH);
		panel.update();
	}

}
