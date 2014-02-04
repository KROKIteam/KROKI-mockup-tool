package graphedit.command;

import graphedit.app.MainFrame;
import graphedit.model.components.Package;
import graphedit.model.elements.GraphEditPackage;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import graphedit.view.PackagePainter;

import java.awt.Component;
import java.awt.geom.Point2D;

import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;

public class NewPackageCommand extends Command {

	private GraphEditPackage pack, parentPackage;
	private GraphEditView view;
	private String name;
	private UmlPackage umlPackage;
	private Package packageElement;
	private ElementPainter painter;

	public NewPackageCommand(GraphEditPackage parentPackage, String name) {
		this.parentPackage = parentPackage;
		this.name = name;
		//this.view = view;
		packageElement = new Package(new Point2D.Double(0,0));
		packageElement.setProperty(GraphElementProperties.NAME, name);
		//nije naveden view, probaj da nadjes meju otvorenima
		for (Component c : MainFrame.getInstance().getMainTabbedPane().getComponents()) 
			if (c instanceof GraphEditView) 
				if (((GraphEditView) c).getModel() == parentPackage.getDiagram())
					view = (GraphEditView)c;
	}

	public NewPackageCommand(GraphEditPackage parentPackage, Package packageElement, GraphEditView view) {
		this.parentPackage = parentPackage;
		this.packageElement = packageElement;
		name = (String) packageElement.getProperty(GraphElementProperties.NAME);
		this.view = view;

	}
	
	public NewPackageCommand(){
		
	}


	@Override
	public void execute() {
		
		umlPackage = new BussinesSubsystem((BussinesSubsystem) parentPackage.getUmlPackage());
		((BussinesSubsystem)umlPackage).setLabel(name);
		parentPackage.getUmlPackage().addNestedPackage(umlPackage);
		pack = new GraphEditPackage(umlPackage);
		packageElement.setRepresentedElement(pack);
		pack.setParentPackage(parentPackage);
		pack.setPackageElement(packageElement);
		parentPackage.getDiagram().addGraphEditPackage(pack.getPackageElement());
		pack.getDiagram().setParentPackage(pack);


		if (view != null){
			painter = new PackagePainter(packageElement);
			view.addElementPainter(painter);
		}


	}

	@Override
	public void undo() {
		parentPackage.getUmlPackage().removeNestedPackage(pack.getUmlPackage());
		parentPackage.getDiagram().removeGraphEditPackage(pack.getPackageElement());
		if (view != null)
			view.removeElementPainter(packageElement);
		MainFrame.getInstance().closeDiagram(pack.getDiagram(), false);

	}

	public GraphEditView getView() {
		return view;
	}
	
	public void initCommand(GraphEditPackage parentPackage, Package packageElement, GraphEditView view){
		this.parentPackage = parentPackage;
		this.packageElement = packageElement;
		name = (String) packageElement.getProperty(GraphElementProperties.NAME);
		this.view = view;
	}

}