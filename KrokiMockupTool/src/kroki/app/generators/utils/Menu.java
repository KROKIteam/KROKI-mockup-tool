package kroki.app.generators.utils;

import java.util.ArrayList;

public class Menu {
	
	String name;
	String label;
	ArrayList<Submenu> submenus;
	
	public Menu() {
	}

	public Menu(String name, String label, ArrayList<Submenu> submenus) {
		this.name = name;
		this.label = label;
		this.submenus = submenus;
	}

	public boolean add(Submenu sub) {
		return submenus.add(sub);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ArrayList<Submenu> getSubmenus() {
		return submenus;
	}

	public void setSubmenus(ArrayList<Submenu> submenus) {
		this.submenus = submenus;
	}
	
}
