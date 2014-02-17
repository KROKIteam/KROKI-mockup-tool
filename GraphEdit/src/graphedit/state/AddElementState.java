package graphedit.state;

import graphedit.app.MainFrame;
import graphedit.app.MainFrame.ToolSelected;
import graphedit.command.AddElementCommand;
import graphedit.command.Command;
import graphedit.command.NewPackageCommand;
import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Interface;
import graphedit.model.components.Package;
import graphedit.view.ClassPainter;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;
import graphedit.view.InterfacePainter;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public class AddElementState extends State {

	public AddElementState(GraphEditController controller, GraphEditView view) {
		super(controller);
		this.model = view.getModel();
		this.view = view;
	}
	
	public AddElementState() { }


	@Override
	public void mouseReleased(MouseEvent e) {
		boolean packageSelected = false;
		GraphElement element = null;
		ElementPainter elementPainter = null;
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (view.getSelectedTool() == ToolSelected.INTERFACE_SELECTED) {
				element = new Interface(e.getPoint());
				elementPainter = new InterfacePainter(element);
			} else if (view.getSelectedTool() == ToolSelected.CLASS_SELECTED) {
				element = new Class(e.getPoint());
				elementPainter = new ClassPainter(element);
			}else if (view.getSelectedTool() == ToolSelected.PACKAGE_SELECTED){
				element = new Package(e.getPoint());
				packageSelected = true;
			}
			
			
			// command pattern
			Command command;
			if (!packageSelected)
				command = new AddElementCommand(view, element, elementPainter);
			else{
				command = new NewPackageCommand(model.getParentPackage(), (Package) element, view);
			}
			
			view.getModel().getCommandManager().executeCommand(command);
			view.getSelectionModel().removeAllSelectedElements();
		}
		else if (SwingUtilities.isRightMouseButton(e)) {
			switchToDefaultState();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// azuriranje koordinata
		MainFrame.getInstance().setPositionTrack(e.getX(), e.getY());
	}


	@Override
	public boolean isAutoScrollOnDragEnabled() {
		return false;
	}

	@Override
	public boolean isAutoScrollOnMoveEnabled() {
		return false;
	}
}
