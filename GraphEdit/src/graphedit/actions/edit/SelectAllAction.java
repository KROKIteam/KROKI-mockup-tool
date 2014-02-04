package graphedit.actions.edit;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;
import graphedit.view.GraphEditView;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class SelectAllAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public SelectAllAction() {
		putValue(NAME, "Select All");
		putValue(MNEMONIC_KEY, KeyEvent.VK_A);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("selectall.png"));
		putValue(SHORT_DESCRIPTION, "Select all elements...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		GraphEditView view = MainFrame.getInstance().getCurrentView();
		view.selectAll();
		view.repaint();
	}

}
