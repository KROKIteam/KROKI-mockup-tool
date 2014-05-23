package graphedit.actions.file;

import graphedit.app.MainFrame;
import graphedit.model.GraphEditWorkspace;
import graphedit.model.elements.GraphEditPackage;
import graphedit.util.ResourceLoader;
import graphedit.util.WorkspaceUtility;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

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
		for (GraphEditPackage pack : GraphEditWorkspace.getInstance().getpackageList()){
			if (pack.isChanged()){
				thereAreChanges = true;
				break;
			}
		}
		
		if (!thereAreChanges){
			MainFrame.getInstance().setVisible(false);
			return;
		}
		else{
			Object[] options = { "Yes, please", "No, thanks"};
			int n = JOptionPane.showOptionDialog(MainFrame.getInstance(),
					"Save any unsaved changes ?",
					"GraphEdit v1.2", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null,
					options,
					options[0]); 
			
			
			if (n == JOptionPane.YES_OPTION){
				for (GraphEditPackage pack : GraphEditWorkspace.getInstance().getpackageList()){
					if (pack.isChanged()){
						WorkspaceUtility.saveProject(pack);
					}
				}
			}
				
		}

		MainFrame.getInstance().setVisible(false);
	}

}
