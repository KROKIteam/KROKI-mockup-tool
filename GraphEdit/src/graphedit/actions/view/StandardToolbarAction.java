package graphedit.actions.view;

import graphedit.actions.popup.MainToolBarPopupMenu;
import graphedit.app.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class StandardToolbarAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public StandardToolbarAction() {
		putValue(NAME, "Standard Toolbar");
		putValue(MNEMONIC_KEY, KeyEvent.VK_S);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		putValue(SHORT_DESCRIPTION, "Toggle standard toolbar...");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		boolean selected = true;
		if (arg0.getActionCommand().equals(MainToolBarPopupMenu.ACTION_COMMAND)) {
			selected = MainFrame.getInstance().getMainToolBarPopupMenu().getStandardToolBar().isSelected();
			MainFrame.getInstance().getStandardToolBar().setSelected(selected);
		} else {
			selected = MainFrame.getInstance().getStandardToolBar().isSelected();
			MainFrame.getInstance().getMainToolBarPopupMenu().getStandardToolBar().setSelected(selected);
		}
		MainFrame.getInstance().getMainToolBar().setVisible(selected);
		
		if (selected) {
			MainFrame.getInstance().getFullScreenAction().setFullScreenIcon();
		} else {
			MainFrame.getInstance().getFullScreenAction().setStandardScreenIcon();
		}
	}

}
