package graphedit.actions.file;

import graphedit.util.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

public class SwitchWorkspaceAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private JFileChooser jFileChooser;
	
	private File selectedFile;
	
	public SwitchWorkspaceAction() {
		putValue(NAME, "Switch Workspace");
		putValue(MNEMONIC_KEY, KeyEvent.VK_W);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("switch_workspace.png"));
		putValue(SHORT_DESCRIPTION, "Switch to another workspace...");
		jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jFileChooser.setMultiSelectionEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		/*jFileChooser.setCurrentDirectory(GraphEditWorkspace.getInstance().getFile());
		if (jFileChooser.showDialog(MainFrame.getInstance(), "Switch workspace") == JFileChooser.APPROVE_OPTION) {
			selectedFile = jFileChooser.getSelectedFile();
			for (UmlPackage p : GraphEditWorkspace.getInstance().getUmlPackages())
				WorkspaceUtility.saveUmlPackage(p);

			// do not save them while closing, we've already done that part
			MainFrame.getInstance().closeAllDiagrams(false, false);
			GraphEditWorkspace.getInstance().switchWorkspace(selectedFile);
			WorkspaceUtility.load();
		}*/
		
	}

}
