package graphedit.command;

import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.GraphEditElement;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

public class LinkElementsCommand extends Command {

	private GraphEditModel model;
	private Link link;
	private LinkPainter linkPainter;
	private LinkableElement sourceElement,destinationElement;
	private GraphEditElement sourceGraphEditElement, destinationGraphEditElement;


	public LinkElementsCommand(GraphEditView view, Link link, LinkPainter linkPainter,LinkableElement sourceElement, LinkableElement destinationElement) {
		this.model = view.getModel();
		this.view = view;
		this.link=link;
		this.linkPainter = linkPainter;
		this.sourceElement=sourceElement;
		this.destinationElement=destinationElement;
		
		if (sourceElement instanceof Shortcut)
			sourceGraphEditElement = ((Shortcut)sourceElement).shortcutTo().getRepresentedElement();
		else
			sourceGraphEditElement = sourceElement.getRepresentedElement();
		
		if (destinationElement instanceof Shortcut)
			destinationGraphEditElement = ((Shortcut)destinationElement).shortcutTo().getRepresentedElement();
		else
			destinationGraphEditElement = destinationElement.getRepresentedElement();
		
		link.getSourceConnector().setRepresentedElement(sourceGraphEditElement);
		link.getDestinationConnector().setRepresentedElement(destinationGraphEditElement);
		
		
	}


	public void execute() {
		sourceElement.addConnectors(link.getSourceConnector());
		destinationElement.addConnectors(link.getDestinationConnector());
		model.insertIntoElementByConnectorStructure(link.getSourceConnector(), sourceElement);
		model.insertIntoElementByConnectorStructure(link.getDestinationConnector(), destinationElement);
		view.addLinkPainter(linkPainter);
		destinationGraphEditElement.link(link, false);
		sourceGraphEditElement.link(link, true);
		model.addLink(link);
	}

	public void undo() {
		sourceGraphEditElement.unlink(link, true);
		destinationGraphEditElement.unlink(link, false);
		model.removeFromElementByConnectorStructure(link);
		model.removeLink(link);
		view.removeLinkPainters(linkPainter);
		view.getSelectionModel().removeSelecredLink(link);
	}

	public GraphEditModel getModel() {
		return model;
	}

	public void setModel(GraphEditModel newGraphEditModel) {
		this.model = newGraphEditModel;
	}


}