package kroki.api.util;

import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;

/**
 * Class containing some util methods
 * @author KROKI Team
 *
 */
public class Util {

	
	public static final int STANDARD_PANEL_PROPERTIES = 1;
	public static final int PARENTCHILD_PANEL_PROPERTIES = 0;
	public static final int STANDARD_PANEL_OPERATIONS = 2;
	public static final int PARENTCHILD_PANEL_OPERATIONS = 1;
	
	/**
	 * Returns index of properties group for panel (different for standard and parent-child)
	 * @param panel Panel
	 * @return Index of properties group
	 */
	public static int getPropertiesGroupIndex(VisibleClass panel){
		if (panel instanceof StandardPanel)
			return STANDARD_PANEL_PROPERTIES;
		if (panel instanceof ParentChild)
			return PARENTCHILD_PANEL_PROPERTIES;
		return -1; 
	}
	
	/**
	 * Returns index of operations group for panel (different for standard and parent-child)
	 * @param panel
	 * @return Index of operations group
	 */
	public static int getOperationGroupIndex(VisibleClass panel){
		if (panel instanceof StandardPanel)
			return STANDARD_PANEL_OPERATIONS;
		if (panel instanceof ParentChild)
			return PARENTCHILD_PANEL_OPERATIONS;
		return -1; 
	}
}
