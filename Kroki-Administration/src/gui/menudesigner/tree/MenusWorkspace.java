package gui.menudesigner.tree;

import java.util.List;

import gui.menudesigner.model.Submenu;


public class MenusWorkspace {
	
	private String name;
	private List<Submenu> menus;
	
	public MenusWorkspace(){
		name = "Menus";
	}
	
	 public void addMenu(Submenu menu) {
	        if (!menus.contains(menu)) {
	        	menus.add(menu);
	        }
	    }

	    public void removeMenu(Submenu menu) {
	        if (menus.contains(menu)) {
	        	menus.remove(menu);
	        }
	    }

	    public int getIndexOf(Submenu menu) {
	        return menus.indexOf(menu);
	    }

	    public int getMenuCount() {
	        return menus.size();
	    }

	    public Submenu getMenuAt(int index) {
	        return menus.get(index);
	    }

		public List<Submenu> getMenus() {
			return menus;
		}

		public void setMenus(List<Submenu> menus) {
			this.menus = menus;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	    
		@Override
		public String toString() {
			return name;
		}
	    

}
