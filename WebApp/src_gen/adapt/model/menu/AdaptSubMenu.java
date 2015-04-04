package adapt.model.menu;

import java.util.ArrayList;

public class AdaptSubMenu extends AdaptMenu {
	
	private String name;
	private ArrayList<AdaptMenu> children;
	private ArrayList<String> roles;
	
	public AdaptSubMenu(String name) {
		this.name = name;
		this.children = new ArrayList<AdaptMenu>();
		this.roles = new ArrayList<String>();
	}
	
	public AdaptSubMenu() {
		this.children = new ArrayList<AdaptMenu>();
		this.roles = new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<AdaptMenu> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<AdaptMenu> children) {
		this.children = children;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	
}
