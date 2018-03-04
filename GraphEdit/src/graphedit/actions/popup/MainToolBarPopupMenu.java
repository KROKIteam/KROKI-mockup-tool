package graphedit.actions.popup;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import graphedit.actions.view.StandardToolbarAction;

public class MainToolBarPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	private JCheckBoxMenuItem standardToolBar;
	public static final String ACTION_COMMAND = " ";
	
	public MainToolBarPopupMenu() {
		standardToolBar = new JCheckBoxMenuItem(new StandardToolbarAction());
		standardToolBar.setSelected(true);
		standardToolBar.setActionCommand(ACTION_COMMAND);
		add(standardToolBar);
	}
	
	public JCheckBoxMenuItem getStandardToolBar() {
		return standardToolBar;
	}
}
