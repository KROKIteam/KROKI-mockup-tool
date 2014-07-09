package com.panelcomposer.core;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import util.PersistenceHelper;

import com.panelcomposer.model.Model;
import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.model.enumeration.Enumeration;
import com.panelcomposer.model.menu.MyMenu;
import com.panelcomposer.model.panel.MPanel;

import ejb.User;


public class AppCache {

	private static AppCache appCache = null;
	protected User user;
	protected Model model = new Model();

	public static AppCache getInstance() {
		if (appCache != null) {
			return appCache;
		} else {
			appCache = new AppCache();
			return appCache;
		}
	}

	public void addToCache(String className, String xmlFile) {
		model.addXML(className, xmlFile);
	}

	public void addToCache(EntityBean ejb) {
		if (findEJBByClassName(ejb.getName()) == null) {
			model.add(ejb);
		}
	}
	
	public void addToCache(String className, MPanel mpanel) {
		model.add(className, mpanel);
	}
	
	public void addToCachePanelClassMap(String className, String id) {
		model.addPanelClass(className, id);
		
	}
	
	public void addToCache(MyMenu menu) {
		model.add(menu);
	}
	
	
	public void addToCache(String name, Enumeration enumeration) {
		model.add(name, enumeration);
	}

	public EntityBean findEJBByClassName(String className) {
		Iterator<EntityBean> it = model.getEntityBeans().iterator();
		EntityBean ejb = null;
		while (it.hasNext()) {
			ejb = it.next();
			if (ejb.getEntityClass().getName().equals(className)) {
				return ejb;
			}
		}
		return null;
	}

	public String getXMLFileName(String className) {
		return model.getMappingXmlMap().get(className);
	}

	public void addToTypeMaping(String modelType, String languageType) {
		model.addType(modelType, languageType);
	}
	
	public void addToCacheTypes(String languageType, String componentType) {
		model.addComponentType(languageType, componentType);
	}

	public MPanel getMPanel(String className) {
		return model.getPanels().get(className);
	}
	
	public List<MyMenu> getMenus() {
		return model.getMenus();
	}

	public String getPanelId(String className) {
		return model.getPanelClassMap().get(className);
	}
	
	public String getComponentType(String languageType) {
		return model.getComponentTypeMap().get(languageType);
	}
	
	public Enumeration getEnumeration(String name) {
		return model.getEnumsMap().get(name);
	}

	/***
	 * Authorizes the user for given username and password
	 * @param username User's username
	 * @param password User's password
	 * @return On successful login returns true, otherwise returns false.
	 */
	public boolean logIn(String username, char[] password) {
		EntityManager em = PersistenceHelper.createEntityManager();
		Query q = em.createQuery(
				"SELECT x FROM User x WHERE x.username = ? AND x.password = ?");
		q.setParameter(1, username);
		q.setParameter(2, new String(password));
		try {
			User user1 = (User) q.getSingleResult();
			if(user1 != null) {
				user = new User();
				user.setUsername(user1.getUsername());
				user.setPassword(user1.getPassword());
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public String getUsername() {
		return user.getUsername();
	}
}
