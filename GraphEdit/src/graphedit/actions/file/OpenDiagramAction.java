package graphedit.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import graphedit.app.MainFrame;
import graphedit.model.elements.GraphEditPackage;
import graphedit.util.ResourceLoader;

public class OpenDiagramAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public OpenDiagramAction() {
		putValue(NAME, "Open Diagram");
		putValue(MNEMONIC_KEY, KeyEvent.VK_O);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("open.png"));
		putValue(SHORT_DESCRIPTION, "Open selected diagram...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object o = MainFrame.getInstance().getMainTree().getSelectionPath().getLastPathComponent();
		if (o instanceof GraphEditPackage)
			MainFrame.getInstance().showDiagram(((GraphEditPackage)o).getDiagram());
	}

}
