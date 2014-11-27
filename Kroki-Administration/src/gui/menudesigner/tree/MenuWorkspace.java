package gui.menudesigner.tree;

import gui.menudesigner.model.Menu;
import gui.menudesigner.model.Submenu;

import java.util.List;


public class MenuWorkspace {
	
	private String name;
	private Submenu root;
	
	public MenuWorkspace(Submenu root){
		this.root = root;
		this.name = root.getName();
	}
	
	 public void addMenu(Menu menu) {
	        if (!root.getChildren().contains(menu)) {
	        	root.getChildren().add(menu);
	        }
	    }

	    public void removeMenu(Menu menu) {
	        if (root.getChildren().contains(menu)) {
	        	root.getChildren().remove(menu);
	        }
	    }

	    public int getIndexOf(Submenu menu) {
	        return root.getChildren().indexOf(menu);
	    }

	    public int getMenuCount() {
	        return root.getChildren().size();
	    }

	    public Menu getMenuAt(int index) {
	        return root.getChildren().get(index);
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

		public Submenu getRoot() {
			return root;
		}

		public void setRoot(Submenu root) {
			this.root = root;
		}
	    

}
