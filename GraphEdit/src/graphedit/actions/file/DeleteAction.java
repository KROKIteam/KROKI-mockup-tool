package graphedit.actions.file;

import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.CutElementsCommand;
import graphedit.command.CutLinkCommand;
import graphedit.command.DeletePackagesCommand;
import graphedit.command.RemoveElementCommand;
import graphedit.gui.utils.Dialogs;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.GraphEditPackage;
import graphedit.util.ResourceLoader;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

public class DeleteAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public DeleteAction() {
		putValue(NAME, "Delete");
		putValue(MNEMONIC_KEY, KeyEvent.VK_D);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("delete.png"));
		putValue(SHORT_DESCRIPTION, "Delete...");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	
		
		TreePath selectedPath = MainFrame.getInstance().getMainTree().getSelectionPath();
		Object selected = selectedPath.getLastPathComponent();
		Object parent = selectedPath.getParentPath().getLastPathComponent();
		
		if (Dialogs.showYesNoDialog("Are you sure you want to delete " + selected + "?", "Delete") != JOptionPane.YES_OPTION) {
			return;
		}
		
		if (selected instanceof GraphEditPackage) {
			GraphEditPackage selectedPackage = (GraphEditPackage) selected;
			
			
			GraphEditView parentsView= MainFrame.getInstance().getOpenDiagram(selectedPackage.getDiagram());
			
			
			List<GraphEditPackage> deleted = new ArrayList<GraphEditPackage>();
			deleted.add(selectedPackage);
			Command command = new DeletePackagesCommand(deleted, parentsView);
			if (parentsView != null)
				parentsView.getModel().getCommandManager().executeCommand(command);
			else
				command.execute();
			
			//WorkspaceUtility.removeDirectoryFromFileSystem(((GraphEditPackage)selected).getFile());
		
		} else if (selected instanceof Link) {
				GraphEditView view = MainFrame.getInstance().getCurrentView();
				Command command = new CutLinkCommand(view, (Link)selected);
				view.getModel().getCommandManager().executeCommand(command);
		} else if (selected instanceof GraphElement) {
				// command pattern
				GraphEditView view = MainFrame.getInstance().getCurrentView();
				
				List<GraphElement> elements = new ArrayList<GraphElement>();
				elements.add((GraphElement) selected);
				List<ElementPainter> painters = new ArrayList<ElementPainter>();
				painters.add(view.getElementPainter((GraphElement) selected));
				
				Command command = new CutElementsCommand(view, elements, painters);
				view.getModel().getCommandManager().executeCommand(command);
			}
		} 

}
