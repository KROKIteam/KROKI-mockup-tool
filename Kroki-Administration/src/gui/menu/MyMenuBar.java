package gui.menu;

import gui.actions.administration.OperationAction;
import gui.actions.administration.PermissionAction;
import gui.actions.administration.ResourceAction;
import gui.actions.administration.ResourceHierarchyAction;
import gui.actions.administration.RoleAction;
import gui.actions.administration.RolePermissionAction;
import gui.actions.administration.UserAction;
import gui.actions.administration.UserRolesAction;
import gui.actions.menu.SketchedMenusBarAction;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyMenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JMenu Stocks;
	private JMenu Menu;
	
	public MyMenuBar() {
		super();
		createGUI();
	}
	
	private void createGUI() {
		Stocks = new MenuStocks();
		Menu = new MenuBar();
		add(Stocks);
		add(Menu);
		
	}
	
		class MenuStocks extends JMenu {
			public MenuStocks() {
				setText("Administration");
					add(new JMenuItem(new ResourceHierarchyAction()));	
					add(new JMenuItem(new ResourceAction()));	
					add(new JMenuItem(new OperationAction()));	
					add(new JMenuItem(new UserAction()));	
					add(new JMenuItem(new RoleAction()));	
					add(new JMenuItem(new UserRolesAction()));	
					add(new JMenuItem(new PermissionAction()));	
					add(new JMenuItem(new RolePermissionAction()));	
			}
		}
		
		class MenuBar extends JMenu {
			public MenuBar() {
				setText("Menu bar");
				add (new JMenuItem(new SketchedMenusBarAction()));
			}
		}

}