package com.panelcomposer.model.menu;

import java.util.ArrayList;
import java.util.List;

import com.panelcomposer.model.AbstractElement;

public class MyMenu extends AbstractElement {

	protected List<MySubMenu> submenus = new ArrayList<MySubMenu>();
	protected List<MyMenu> menus = new ArrayList<MyMenu>();
	
	public void addSubmenu(MySubMenu msm) {
		submenus.add(msm);
	}

	public void addMenu(MyMenu menu) {
		menus.add(menu);
	}
	
	public List<MyMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<MyMenu> menus) {
		this.menus = menus;
	}

	public List<MySubMenu> getSubmenus() {
		return submenus;
	}

	public void setSubmenus(List<MySubMenu> submenus) {
		this.submenus = submenus;
	}
}
