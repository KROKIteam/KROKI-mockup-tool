package graphedit.actions.file;

import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.NewPackageCommand;
import graphedit.gui.utils.Dialogs;
import graphedit.model.elements.GraphEditPackage;
import graphedit.model.interfaces.GraphEditTreeNode;
import graphedit.util.ResourceLoader;
import graphedit.util.Validator;
import graphedit.view.GraphEditView;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class NewPackageAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public NewPackageAction() {
		putValue(NAME, "New Package");
		putValue(MNEMONIC_KEY, KeyEvent.VK_N);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("new.png"));
		putValue(SHORT_DESCRIPTION, "Create new package...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object selected = MainFrame.getInstance().getMainTree().getSelectionPath().getLastPathComponent();
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
		
		if (selected instanceof GraphEditPackage) {
			command = new NewPackageCommand((GraphEditPackage) selected, name);
		}
		
	
		GraphEditView view = ((NewPackageCommand)command).getView();
		if (view!= null){
			view.getModel().getCommandManager().executeCommand(command);
			view.getSelectionModel().removeAllSelectedElements();
		}
		else
			command.execute();
	}

}
