package kroki.api.util;

import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;

public class Util {

	
	public static final int STANDARD_PANEL_PROPERTIES = 1;
	public static final int PARENTCHILD_PANEL_PROPERTIES = 0;
	public static final int STANDARD_PANEL_OPERATIONS = 2;
	public static final int PARENTCHILD_PANEL_OPERATIONS = 1;
	
	public static int getPropertiesGroupIndex(VisibleClass panel){
		if (panel instanceof StandardPanel)
			return STANDARD_PANEL_PROPERTIES;
		if (panel instanceof ParentChild)
			return PARENTCHILD_PANEL_PROPERTIES;
		return -1; 
	}
	
	public static int getOperationGroupIndex(VisibleClass panel){
		if (panel instanceof StandardPanel)
			return STANDARD_PANEL_OPERATIONS;
		if (panel instanceof ParentChild)
			return PARENTCHILD_PANEL_OPERATIONS;
		return -1; 
	}
}
