package graphedit.command;

import graphedit.app.MainFrame;
import graphedit.model.GraphEditWorkspace;
import graphedit.model.elements.GraphEditPackage;
import kroki.profil.ComponentType;
import kroki.profil.subsystem.BussinesSubsystem;

public class NewProjectCommand extends Command {

	private GraphEditWorkspace workspace;
	private GraphEditPackage pack;
	private String name;

	public NewProjectCommand(String name) {
		this.workspace = GraphEditWorkspace.getInstance();
		this.name = name;
	}

	@Override
	public void execute() {
		BussinesSubsystem buss = new BussinesSubsystem(name, true, ComponentType.MENU, null);
		pack = new GraphEditPackage(buss);
		workspace.addProject(pack);
		MainFrame.getInstance().showDiagram(pack.getDiagram());
	}

	@Override
	public void undo() {
		//MainFrame.getInstance().closeDiagrams(project.getAllDiagrams());
		//workspace.removeProject(project);
	}

}