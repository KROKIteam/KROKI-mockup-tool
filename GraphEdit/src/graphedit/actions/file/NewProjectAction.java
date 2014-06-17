package graphedit.actions.file;

import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.NewProjectCommand;
import graphedit.gui.utils.Dialogs;
import graphedit.model.GraphEditWorkspace;
import graphedit.model.interfaces.GraphEditTreeNode;
import graphedit.util.ResourceLoader;
import graphedit.util.Validator;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

public class NewProjectAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public NewProjectAction() {
		putValue(NAME, "New Project");
		putValue(MNEMONIC_KEY, KeyEvent.VK_N);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("new.png"));
		putValue(SHORT_DESCRIPTION, "Create new project...");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		TreePath path =  MainFrame.getInstance().getMainTree().getSelectionPath();
		Object selected;
		if (path == null)
			selected = MainFrame.getInstance().getMainTree().getModel().getRoot();
		else
		  selected = path.getLastPathComponent();
		Command command = null;
		
		String name = null;
		for(;;) {
			name = Dialogs.showInputDialog("Name:", (String) getValue(NAME));
			if (name == null) {
				return;
			} else if (!Validator.isValidName(name)) {
				Dialogs.showErrorMessage("\"" + name + "\" is not a valid name.", "Error");
			} else if (Validator.hasChildWithName((GraphEditTreeNode) selected, name)) {
				Dialogs.showErrorMessage("\"" + name + "\" already exists.", "Error");
			} else {
				break;
			}
		}
		
		if (selected instanceof GraphEditWorkspace) {
			command = new NewProjectCommand(name);
		}
		
		// it is not according to command pattern, but it also shouldn't be, 
		// since it's not diagram related command as such it is not undoable
		command.execute();
	}

}
