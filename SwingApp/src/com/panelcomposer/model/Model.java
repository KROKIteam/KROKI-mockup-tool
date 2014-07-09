package com.panelcomposer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.model.enumeration.Enumeration;
import com.panelcomposer.model.menu.MyMenu;
import com.panelcomposer.model.panel.MPanel;


public class Model {
	
	/***
	 * Map that contains XML file paths as values and keys that are name of the entity classes
	 */
	protected Map<String, String> mappingXmlMap = new HashMap<String, String>();
	/***
	 * List that contains entity beans
	 */
	protected List<EntityBean> entityBeans = new ArrayList<EntityBean>();
	
	protected Map<String, MPanel> panels = new HashMap<String, MPanel>();
	protected List<MyMenu> menus = new ArrayList<MyMenu>();
	protected Map<String, String> panelClassMap = new HashMap<String, String>();
	protected Map<String, String> componentTypeMap = new HashMap<String, String>();
	protected Map<String, Enumeration> enumsMap = new HashMap<String, Enumeration>();
	
	/**
	 * Adds new entry to map of entity classes' names and coresponding XML file paths
	 * @param className Name of the entity class
	 * @param xmlFile Path to the XML file describing the entity  
	 */
	public void addXML(String className, String xmlFile) {
		mappingXmlMap.put(className, xmlFile);
	}

	public void addType(String modelType, String languageType) {
		mappingXmlMap.put(modelType, languageType);
	}

	public void add(EntityBean ejb) {
		entityBeans.add(ejb);
	}

	public void add(String panelId, MPanel mpanel) {
		panels.put(panelId, mpanel);
	}

	public void addPanelClass(String className, String panelId) {
		panelClassMap.put(className, panelId);
	}
	
	public void addComponentType(String languageType, String componentType) {
		componentTypeMap.put(languageType, componentType);
	}

	public void add(MyMenu menu) {
		menus.add(menu);
	}

	public void add(String name, Enumeration enumeration) {
		enumsMap.put(name, enumeration);
	}

	public Map<String, String> getMappingXmlMap() {
		return mappingXmlMap;
	}

	public void setMappingXmlMap(Map<String, String> mappingXmlMap) {
		this.mappingXmlMap = mappingXmlMap;
	}

	public List<EntityBean> getEntityBeans() {
		return entityBeans;
	}

	public void setEntityBeans(List<EntityBean> entityBeans) {
		this.entityBeans = entityBeans;
	}

	public Map<String, MPanel> getPanels() {
		return panels;
	}

	public void setPanels(Map<String, MPanel> panels) {
		this.panels = panels;
	}

	public List<MyMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<MyMenu> menus) {
		this.menus = menus;
	}

	public Map<String, String> getPanelClassMap() {
		return panelClassMap;
	}

	public void setPanelClassMap(Map<String, String> panelClassMap) {
		this.panelClassMap = panelClassMap;
	}

	public Map<String, String> getComponentTypeMap() {
		return componentTypeMap;
	}

	public void setComponentTypeMap(Map<String, String> componentTypeMap) {
		this.componentTypeMap = componentTypeMap;
	}

	public Map<String, Enumeration> getEnumsMap() {
		return enumsMap;
	}

	public void setEnumsMap(Map<String, Enumeration> enumsMap) {
		this.enumsMap = enumsMap;
	}

}
