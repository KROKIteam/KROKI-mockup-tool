package adapt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapt.model.ejb.EntityBean;
import adapt.model.enumeration.Enumeration;
import adapt.model.menu.AdaptMenu;
import adapt.model.menu.AdaptSubMenu;
import adapt.model.panel.AdaptPanel;

/**
 * Application model that holds object representations of
 * xml entities obtained from application repository.
 * @author Milorad Filipovic
 */
public class Model {

	//--------------------------------------------------------------------| OBJECTS LISTS
	protected List<EntityBean> entityBeans = new ArrayList<EntityBean>();
	protected List<AdaptMenu> menus = new ArrayList<AdaptMenu>();
	protected Map<String, AdaptPanel> panels = new HashMap<String, AdaptPanel>();
	protected Map<String, Enumeration> enumerations = new HashMap<String, Enumeration>();
	protected AdaptSubMenu defaultMenu = new AdaptSubMenu();
	
	//--------------------------------------------------------------------| MAPPING DATA
	protected Map<String, String> xmlMappings = new HashMap<String, String>();
	protected Map<String, String> panelClassMap = new HashMap<String, String>();
	protected Map<String, String> componentTypeMap = new HashMap<String, String>();
	
	//--------------------------------------------------------------------| ADD METHODS
	/**
	 * Adds XML-to-EJB mapping to model
	 * @param className EJB class name
	 * @param xmlFile xml specification of EJB class
	 */
	public void addEJBMapping(String className, String xmlFile) {
		xmlMappings.put(className, xmlFile);
	}
	
	public void add(EntityBean ejb) {
		entityBeans.add(ejb);
	}

	public void add(AdaptMenu menu) {
		menus.add(menu);
	}
	
	public void add(ArrayList<AdaptSubMenu> allMenus) {
		menus.addAll(allMenus);
	}
	
	public void add(String panelId, AdaptPanel panel) {
		panels.put(panelId, panel);
	}
	
	public void add(String name, Enumeration enumeration) {
		enumerations.put(name, enumeration);
	}
	
	public void addPanelClassMapping(String className, String panelId) {
		panelClassMap.put(className, panelId);
	}
	
	public void addComponentTypeMapping(String languageType, String component) {
		componentTypeMap.put(languageType, component);
	}
	
	//--------------------------------------------------------------------| GETTERS AND SETTERS
	public Map<String, String> getXmlMappings() {
		return xmlMappings;
	}

	public List<EntityBean> getEntityBeans() {
		return entityBeans;
	}

	public List<AdaptMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<AdaptMenu> menus) {
		this.menus = menus;
	}

	public Map<String, AdaptPanel> getPanels() {
		return panels;
	}

	public void setPanels(Map<String, AdaptPanel> panels) {
		this.panels = panels;
	}

	public Map<String, Enumeration> getEnumerations() {
		return enumerations;
	}

	public void setEnumerations(Map<String, Enumeration> enumerations) {
		this.enumerations = enumerations;
	}

	public Map<String, String> getPanelCLassMap() {
		return panelClassMap;
	}

	public void setPanelCLassMap(Map<String, String> panelCLassMap) {
		this.panelClassMap = panelCLassMap;
	}

	public Map<String, String> getComponentTypeMap() {
		return componentTypeMap;
	}

	public void setComponentTypeMap(Map<String, String> componentTypeMap) {
		this.componentTypeMap = componentTypeMap;
	}

	public void setXmlMappings(Map<String, String> xmlMappings) {
		this.xmlMappings = xmlMappings;
	}

	public AdaptSubMenu getDefaultMenu() {
		return defaultMenu;
	}

	public void setDefaultMenu(AdaptSubMenu defaultMenu) {
		this.defaultMenu = defaultMenu;
	}
}
