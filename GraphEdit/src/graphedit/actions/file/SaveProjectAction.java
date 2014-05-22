package graphedit.actions.file;

import graphedit.app.MainFrame;
import graphedit.model.elements.GraphEditPackage;
import graphedit.util.ResourceLoader;
import graphedit.util.WorkspaceUtility;
import graphedit.view.GraphEditView;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class SaveProjectAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public SaveProjectAction() {
		putValue(NAME, "Save UmlPackage");
		putValue(MNEMONIC_KEY, KeyEvent.VK_P);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("save.png"));
		putValue(SHORT_DESCRIPTION, "Save current project...");
		setEnabled(false);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (MainFrame.getInstance().getCurrentView() instanceof GraphEditView) {
			GraphEditPackage topPackage = WorkspaceUtility.getTopPackage(MainFrame.getInstance().getCurrentView().getModel().getParentPackage());
			WorkspaceUtility.saveProject(topPackage);
			
			
			//WorkspaceUtility.saveUmlPackage(parentUmlPackage);
			//parentUmlPackage.setChanged(false);
			MainFrame.getInstance().removeAsteriskFromAllTabsContainingToProject(topPackage);
			MainFrame.getInstance().getSaveProjectAction().setEnabled(false);
			MainFrame.getInstance().getCurrentView().getSelectionModel().removeAllSelectedElements();
			setEnabled(false);
			
		}
		
	}

}
