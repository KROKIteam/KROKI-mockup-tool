package graphedit.actions.edit;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;
import graphedit.view.GraphEditView;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class SelectInverseAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public SelectInverseAction() {
		putValue(NAME, "Inverse selection");
		putValue(MNEMONIC_KEY, KeyEvent.VK_I);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("inverse.png"));
		putValue(SHORT_DESCRIPTION, "Select inverse elements");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		GraphEditView view = MainFrame.getInstance().getCurrentView();
		view.selectInverse();
		view.repaint();
	}

}
