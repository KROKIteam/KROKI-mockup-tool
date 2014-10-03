package adapt.model.menu;

import java.util.ArrayList;
import java.util.List;

import adapt.model.AbstractElement;

public class AdaptMenu extends AbstractElement {

	protected List<AdaptMenu> menus = new ArrayList<AdaptMenu>();
	protected List<AdaptSubMenu> submenus = new ArrayList<AdaptSubMenu>();
	
	public void addMenu(AdaptMenu menu) {
		menus.add(menu);
	}
	
	public void addSubMenu(AdaptSubMenu submenu) {
		submenus.add(submenu);
	}

	public List<AdaptMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<AdaptMenu> menus) {
		this.menus = menus;
	}

	public List<AdaptSubMenu> getSubmenus() {
		return submenus;
	}

	public void setSubmenus(List<AdaptSubMenu> submenus) {
		this.submenus = submenus;
	}
}
