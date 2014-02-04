package graphedit.command;

import graphedit.app.MainFrame;
import graphedit.model.elements.GraphEditPackage;
import graphedit.model.properties.PropertyEnums.PackageProperties;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeletePackagesCommand extends Command {

	private List<GraphEditPackage> packages;
	private GraphEditView parentsView;
	private List<ElementPainter> painters;
	private Map<String, Boolean> wasOpen;

	public DeletePackagesCommand (List<GraphEditPackage> packages, GraphEditView parentsView){
		this.packages = packages;
		this.parentsView = parentsView;
		painters = new ArrayList<ElementPainter>();
		wasOpen = new HashMap<String, Boolean>();
		ElementPainter painter;
		if (parentsView != null)
			for (GraphEditPackage pack : packages){
				painter = parentsView.getElementPainter(pack.getPackageElement());
				painters.add(painter);
			}

	}

	@Override
	public void execute() {

		GraphEditView packView;
		for (GraphEditPackage pack : packages){
			packView = MainFrame.getInstance().getOpenDiagram(pack.getDiagram());
			wasOpen.put((String) pack.getProperty(PackageProperties.NAME), packView != null);
			if (packView != null)
				MainFrame.getInstance().closeDiagram(pack.getDiagram(), false);

			if (pack.getParentPackage()!=null){
				pack.getParentPackage().getUmlPackage().removeNestedPackage(pack.getUmlPackage());
				pack.getParentPackage().getDiagram().removeGraphEditPackage(pack.getPackageElement());
			}
		}
		if (parentsView != null)
			for (ElementPainter painter : painters)
				parentsView.removeElementPainter(painter);
	}

	@Override
	public void undo() {
		for (GraphEditPackage pack : packages){
			if (wasOpen.get(pack.getProperty(PackageProperties.NAME)))
				MainFrame.getInstance().showDiagram(pack.getDiagram());
			if (pack.getParentPackage()!=null){
				pack.getParentPackage().getUmlPackage().addNestedPackage(pack.getUmlPackage());
				pack.getParentPackage().getDiagram().addGraphEditPackage(pack.getPackageElement());

			}
		}
		if (parentsView != null)
			for (ElementPainter painter : painters)
				parentsView.addElementPainter(painter);
	}

}
