package com.panelcomposer.model.menu;

import java.util.ArrayList;
import java.util.List;

import com.panelcomposer.model.AbstractElement;

public class MyMenu extends AbstractElement {

	protected List<MySubMenu> submenus = new ArrayList<MySubMenu>();

	public void add(MySubMenu msm) {
		submenus.add(msm);
	}

	public List<MySubMenu> getSubmenus() {
		return submenus;
	}

	public void setSubmenus(List<MySubMenu> submenus) {
		this.submenus = submenus;
	}

}
