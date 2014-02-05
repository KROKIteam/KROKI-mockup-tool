package graphedit.actions.file;

import graphedit.app.MainFrame;
import graphedit.model.GraphEditWorkspace;
import graphedit.util.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import kroki.uml_core_basic.UmlPackage;

public class ExitAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public ExitAction() {
		putValue(NAME, "Exit");
		putValue(MNEMONIC_KEY, KeyEvent.VK_E);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("exit.png"));
		putValue(SHORT_DESCRIPTION, "Exit...");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		boolean thereAreChanges = false;
	/*	for (UmlPackage UmlPackage : GraphEditWorkspace.getInstance().getUmlPackages())
			if (UmlPackage.isChanged()) {
				thereAreChanges = true;
				break;
			}
		if (thereAreChanges) {
			Object[] options = { "Yes, please", "No, thanks", "Cancel" };
			int n = JOptionPane.showOptionDialog(MainFrame.getInstance(),
					"Save any unsaved data?",
					"GraphEdit v1.1", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null,
					options,
					options[0]); 
			if (n != JOptionPane.CANCEL_OPTION) {
				if (n == JOptionPane.YES_OPTION) {
					for (UmlPackage p : GraphEditWorkspace.getInstance().getUmlPackages())
						WorkspaceUtility.saveUmlPackage(p);
				}
			//	System.exit(0);
				MainFrame.getInstance().setVisible(false);
			}
		} else {
			int n = JOptionPane.showOptionDialog(MainFrame.getInstance(),
					"Terminate the application?",
					"GraphEdit v1.1", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null); 
			if (n == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
			
		}*/
		MainFrame.getInstance().setVisible(false);
		//System.exit(0);
	}

}
