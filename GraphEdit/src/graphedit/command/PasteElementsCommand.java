package graphedit.command;

import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.Package;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.GraphEditElement;
import graphedit.model.elements.GraphEditPackage;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.PackageProperties;
import graphedit.util.SerializationUtility;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

import java.awt.Point;
import java.util.List;

import kroki.uml_core_basic.UmlPackage;


public class PasteElementsCommand extends Command {

	private List<GraphElement> elements;
	private List<ElementPainter> elementPainters;
	private List<Link> links;
	private List<LinkPainter> linkPainters;
	private GraphEditModel model;
	private Point location;
	private Double xDiff, yDiff;

	@SuppressWarnings("unchecked")
	public PasteElementsCommand(GraphEditView view, List<LinkPainter> linkPainters, List<ElementPainter> elementPainters) {
		this.model = view.getModel();
		this.view = view;
		// obtain deep copies... shallow ones, just don't do well :( 
		Object deserializedObjects[] = SerializationUtility.deepCopy(elementPainters, linkPainters);
		this.elementPainters = (List<ElementPainter>) deserializedObjects[1];
		this.linkPainters = (List<LinkPainter>) deserializedObjects[0];
		this.location = null;

	}
	
	@SuppressWarnings("unchecked")
	public PasteElementsCommand(GraphEditView view, List<LinkPainter> linkPainters, List<ElementPainter> elementPainters, Point location) {
		this.model = view.getModel();
		this.view = view;
		Object deserializedObjects[] = SerializationUtility.deepCopy(elementPainters, linkPainters);
		this.elementPainters = (List<ElementPainter>) deserializedObjects[1];
		this.linkPainters = (List<LinkPainter>) deserializedObjects[0];
		this.location = location;
	}
	
	@SuppressWarnings("unchecked")
	public PasteElementsCommand(GraphEditView view, List<LinkPainter> linkPainters, List<ElementPainter> elementPainters, Double xDiff, Double yDiff) {
		this.model = view.getModel();
		this.view = view;
		Object deserializedObjects[] = SerializationUtility.deepCopy(elementPainters, linkPainters);
		this.elementPainters = (List<ElementPainter>) deserializedObjects[1];
		this.linkPainters = (List<LinkPainter>) deserializedObjects[0];
		this.xDiff = xDiff;
		this.yDiff = yDiff;
	}

	
	@Override
	public void execute() {
		view.getSelectionModel().removeAllSelectedElements();
		view.addElementPainters(elementPainters);
		
		if (xDiff != null && yDiff != null)
			elements = model.pasteDiagramElements(elementPainters, xDiff, yDiff);
		else
			elements = model.pasteDiagramElements(elementPainters, location);

		view.addLinkPainters(linkPainters);
		links = model.pasteLinks(linkPainters);

		view.getSelectionModel().addSelectedElements(elements);


		for (GraphElement element : elements){
			String name = (String) element.getProperty(GraphElementProperties.NAME);
			if (element instanceof Class){
				ClassElement classElement = (ClassElement) element.getRepresentedElement();
				model.getParentPackage().getUmlPackage().addOwnedType(classElement.getUmlType());
				classElement.getUmlType().setUmlPackage(model.getParentPackage().getUmlPackage());
				classElement.setName(name);
			}
			else if (element instanceof Package){
				GraphEditPackage packElement = (GraphEditPackage) element.getRepresentedElement();
				packElement.getDiagram().initModel(name);
				model.getParentPackage().getUmlPackage().addNestedPackage(packElement.getUmlPackage());
				packElement.getUmlPackage().setNestingPackage(model.getParentPackage().getUmlPackage());
				packElement.setName(name);
				packElement.setProperty(PackageProperties.NAME, name);
			}
		}

	}

	@Override
	public void undo() {
		model.removeDiagramElements(elements);
		view.removeElementPainters(elementPainters);
		model.removeLinks(links);
		view.removeLinkPainters(linkPainters);

		view.getSelectionModel().removeSelectedElements(elements);

		for (GraphElement element : elements){
			GraphEditElement gElement = element.getRepresentedElement();
			if (element instanceof Class){
				model.getParentPackage().getUmlPackage().removeOwnedType(((ClassElement)gElement).getUmlType());
			}
			else if (element instanceof Package){
				model.getParentPackage().getUmlPackage().removeNestedPackage((UmlPackage) (gElement.getUmlElement()));
			}
		}


	}

}