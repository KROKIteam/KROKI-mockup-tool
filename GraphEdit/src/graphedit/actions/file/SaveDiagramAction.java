package graphedit.actions.file;

import graphedit.app.MainFrame;
import graphedit.model.properties.PropertyEnums.DiagramProperties;
import graphedit.util.ResourceLoader;
import graphedit.util.WorkspaceUtility;
import graphedit.view.GraphEditView;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class SaveDiagramAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public SaveDiagramAction() {
		putValue(NAME, "Save Diagram");
		putValue(MNEMONIC_KEY, KeyEvent.VK_S);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("save.png"));
		putValue(SHORT_DESCRIPTION, "Save current diagram...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (MainFrame.getInstance().getCurrentView() instanceof GraphEditView) {
			WorkspaceUtility.save(MainFrame.getInstance().getCurrentView().getModel());
			MainFrame.getInstance().getCurrentView().getSelectionModel().removeAllSelectedElements();
			MainFrame.getInstance().removeAsteriskFromTab();
			setEnabled(false);
			JOptionPane.showMessageDialog(MainFrame.getInstance(), 
					"Diagram: " + MainFrame.getInstance().getCurrentView().getModel().getProperty(DiagramProperties.NAME) + " saved successfully!");
		}
		
	}

}
