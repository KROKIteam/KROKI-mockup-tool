package graphedit.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import graphedit.app.MainFrame;
import graphedit.model.diagram.GraphEditModel;
import graphedit.util.ResourceLoader;
import graphedit.util.WorkspaceUtility;

public class CloseDiagramAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private Object[] options = { "Yes, please", "No, thanks", "Cancel" };
	
	private GraphEditModel model;
	
	public CloseDiagramAction() {
		putValue(NAME, "Close Diagram");
		putValue(MNEMONIC_KEY, KeyEvent.VK_C);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("close.png"));
		putValue(SHORT_DESCRIPTION, "Close selected diagram...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		model = MainFrame.getInstance().getCurrentView().getModel(); 
	
		if (MainFrame.getInstance().isMarkedWithAsterisk()) {
			int n = JOptionPane.showOptionDialog(MainFrame.getInstance(),
					"Save changes?",
					"The changes were made.", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null,
					options,
					options[0]); 
			if (n != JOptionPane.CANCEL_OPTION) {
				if (n == JOptionPane.YES_OPTION) {
					WorkspaceUtility.save(model);
					MainFrame.getInstance().closeDiagram(model, false);
				} else {
					MainFrame.getInstance().closeDiagram(model, true);
				}
			}
		} else {
			MainFrame.getInstance().closeDiagram(model, false);
		}
	}

}
