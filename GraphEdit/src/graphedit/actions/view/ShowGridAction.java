package graphedit.actions.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import graphedit.app.MainFrame;
import graphedit.view.GraphEditView;

public class ShowGridAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public ShowGridAction() {
		putValue(NAME, "Show Grid");
		putValue(MNEMONIC_KEY, KeyEvent.VK_G);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		putValue(SHORT_DESCRIPTION, "Toggle grid...");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		boolean selected;
		if (event.getSource().equals(MainFrame.getInstance().getViewPopupMenu().getShowGridCheckBox())) {
			selected = MainFrame.getInstance().getViewPopupMenu().getShowGridCheckBox().isSelected();
			MainFrame.getInstance().getShowGridMenuItem().setSelected(selected);
		} else {
			selected = MainFrame.getInstance().getShowGridMenuItem().isSelected();
			MainFrame.getInstance().getViewPopupMenu().getShowGridCheckBox().setSelected(selected);
		}
		if (MainFrame.getInstance().getCurrentView() instanceof GraphEditView)
			MainFrame.getInstance().getCurrentView().toggleGrid();
	}

}
