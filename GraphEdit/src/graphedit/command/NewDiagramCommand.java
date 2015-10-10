package graphedit.command;

import graphedit.app.MainFrame;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.GraphEditPackage;

public class NewDiagramCommand extends Command {

	@SuppressWarnings("unused")
	private GraphEditPackage parentPackage;
	private GraphEditModel diagram;
	private String name;
	
	public static final String FILE_EXTENSION = ".dgm";

	public NewDiagramCommand(GraphEditPackage parentPackage, String name) {
		this.parentPackage =parentPackage;
		this.name = name;
	}
	

	@Override
	public void execute() {
		diagram = new GraphEditModel(name);
		//parentPackage.getUmlPackage().addOwnedType(diagram);
		
		MainFrame.getInstance().showDiagram(diagram);
	}

	@Override
	public void undo() {
		MainFrame.getInstance().closeDiagram(diagram, false);

		/*if (parentProject != null) {
			parentProject.removeDiagram(diagram);
		} else {
			parentPackage.removeDiagram(diagram);
		}*/
	}

}