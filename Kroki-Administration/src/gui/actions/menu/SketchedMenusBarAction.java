package gui.actions.menu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import gui.menudesigner.MenuSketchDialog;

public class SketchedMenusBarAction extends AbstractAction {

	
	public SketchedMenusBarAction() {
		putValue(NAME, "Sketched menus");
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_MASK)); To be decided
		//putValue(SMALL_ICON, new ImageIcon("images/ukidanje_racuna.jpg"));	To be decided
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		MenuSketchDialog msd = new MenuSketchDialog();
		msd.setVisible(true);
	}

}
