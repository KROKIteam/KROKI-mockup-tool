package kroki.api.property;

import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.VisibleProperty;

public class UIPropertyUtil {
	
	public static final int STANDARD_PANEL_PROPERTIES = 1;
	public static final int PARENTCHILD_PANEL_PROPERTIES = 0;

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
			panel.addVisibleElement(indexClass, property);
		else
			panel.addVisibleElement(property);
		return property;
	}
	
	
	public static VisibleProperty makeVisibleProperty(String label, boolean visible, ComponentType type, VisibleClass panel){
		return makeVisiblePropertyAt(label, visible, type, panel, -1, -1);
	}
	
	private static int getPropertiesGroupIndex(VisibleClass panel){
		if (panel instanceof StandardPanel)
			return STANDARD_PANEL_PROPERTIES;
		if (panel instanceof ParentChild)
			return PARENTCHILD_PANEL_PROPERTIES;
		return -1; 
	}
}
