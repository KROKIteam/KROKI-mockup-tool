package graphedit.command;

import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.GraphEditElement;
import graphedit.model.elements.GraphEditPackage;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

import java.util.List;
import java.util.Map;

import kroki.uml_core_basic.UmlPackage;

@SuppressWarnings("unused")
public class RemoveElementCommand extends Command {

	private GraphElement element;
	private ElementPainter elementPainter;
	private final List<Link> links;
	private final List<LinkPainter> linkPainters;
	private Map<Connector, GraphElement> removedMappings;
	private GraphEditModel model;
	private ClassElement classElement;
	private UmlPackage umlPackage;

	public RemoveElementCommand(GraphEditView view, GraphElement element) {
		this.model = view.getModel();
		this.view = view;
		this.element = element;
		this.links = model.getAssociatedLinks(element);
		this.linkPainters = view.getLinkPainters(links);
		umlPackage = model.getParentPackage().getUmlPackage();
	}

	@Override
	public void execute() {
		//smodel.removeDiagramElement(element);
		model.removeLinks(links);

		/* update mappings - connector - element */
		removedMappings = model.removeFromElementByConnectorStructure(links);

		elementPainter = view.removeElementPainter(element);
		view.removeLinkPainters(linkPainters);
		view.getSelectionModel().removeAllSelectedElements();

		for (Link link : links){
			GraphEditElement sourceElement = link.getSourceConnector().getRepresentedElement();
			GraphEditElement destinationElement = link.getDestinationConnector().getRepresentedElement();
			sourceElement.unlink(link);
			destinationElement.unlink(link);
		}

		GraphEditElement gElement = element.getRepresentedElement();
		umlPackage.removeOwnedType(((ClassElement)gElement).getUmlType());

	}

	@Override
	public void undo() {
		//model.addDiagramElement(element);
		model.addLinks(links);

		//model.addToElementByConnectorStructure(removedMappings);
		model.updateHashStructure(element);

		view.addElementPainter(elementPainter);
		view.addLinkPainters(linkPainters);
		// ponisti selekciju
		view.getSelectionModel().removeAllSelectedElements();

	}
}
