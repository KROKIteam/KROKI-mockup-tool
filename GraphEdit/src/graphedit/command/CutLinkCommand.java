package graphedit.command;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.AbstractLinkElement;
import graphedit.model.elements.GraphEditElement;
import graphedit.model.elements.UIClassElement;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CutLinkCommand extends Command {

	private Link link;

	private LinkPainter linkPainter;

	private Map<Connector, GraphElement> removedMappings;

	private GraphEditModel model;

	private GraphEditElement sourceElement, destinationElement;
	private AbstractLinkElement sourceLinkElement, destinationLinkElement;

	public CutLinkCommand(GraphEditView view, Link link) {
		this.view = view;
		this.model = view.getModel();
		this.link = link;
		this.linkPainter = view.getLinkPainter(link);
		sourceElement = link.getSourceConnector().getRepresentedElement();
		destinationElement = link.getDestinationConnector().getRepresentedElement();
		if (ApplicationMode.USER_INTERFACE == MainFrame.getInstance().getAppMode()){
			sourceLinkElement= ((UIClassElement) sourceElement).getCurrentElement(link.getSourceConnector());
			destinationLinkElement = ((UIClassElement) destinationElement).getCurrentElement(link.getDestinationConnector());
		}
	}

	@Override
	public void execute() {
		sourceElement.unlink(link);
		destinationElement.unlink(link);
		view.getSelectionModel().removeAllSelectedElements();
		view.getSelectionModel().setSelectedLink(null);
		model.removeLink(link);
		view.removeLinkPainters(linkPainter);
		removedMappings = model.removeFromElementByConnectorStructure(link);
	}

	@Override
	public void undo() {

		model.addLink(link);
		view.addLinkPainter(linkPainter);
		model.addToElementByConnectorStructure(removedMappings);
		view.getSelectionModel().setSelectedLink(link);
		Set<Entry<Connector, GraphElement>> entrySet = removedMappings.entrySet();
		for (Entry<Connector, GraphElement> e : entrySet) {
			LinkableElement element = (LinkableElement) e.getValue();
			Connector conn = e.getKey();
			element.getConnectors().add(conn);
		}
		sourceElement.setOldLink(link, sourceLinkElement);
		destinationElement.setOldLink(link, destinationLinkElement);
	}

}
