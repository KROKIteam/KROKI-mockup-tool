package graphedit.command;

import graphedit.model.diagram.GraphEditModel;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;

import java.util.List;

public class CreateShortcutCommand extends Command{

	private List<ElementPainter> shortcutPainters;
	private GraphEditModel model;
	private GraphEditView view;
	
	
	public CreateShortcutCommand(GraphEditView view, List<ElementPainter> shortcutPainters) {
		this.model = view.getModel();
		this.view = view;
		this.shortcutPainters = shortcutPainters;
		
	}

	@Override
	public void execute() {
		System.out.println("pravi shortcut");
		for (ElementPainter shortcutPainter : shortcutPainters){
			view.addElementPainter(shortcutPainter);
			model.addDiagramElement(shortcutPainter.getElement());
		}
		
	}

	@Override
	public void undo() {
		for (ElementPainter shortcutPainter : shortcutPainters){
			view.removeElementPainter(shortcutPainter);
			model.removeDiagramElement(shortcutPainter.getElement());
		}
		view.getSelectionModel().removeAllSelectedElements();
	}

	
	

}
