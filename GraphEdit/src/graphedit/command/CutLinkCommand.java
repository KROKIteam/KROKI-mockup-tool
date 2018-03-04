package graphedit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.AbstractLinkElement;
import graphedit.model.elements.GraphEditElement;
import graphedit.model.elements.HierarchyElement;
import graphedit.model.elements.UIClassElement;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;
import kroki.profil.association.Hierarchy;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.utils.HierarchyUtil;

public class CutLinkCommand extends Command {


	private Map<Connector, GraphElement> removedMappings;

	private GraphEditModel model;

	private List<Link> links;

	private List<LinkPainter> linkPainters;
	private List<AbstractLinkElement> sourceLinkElements, destinationLinkElements;

	private GraphEditElement sourceElement, destinationElement;


	public CutLinkCommand (GraphEditView view, GraphEditModel model, Link link){
		this(view, link);
		this.model = model;
	}


	public CutLinkCommand(GraphEditView view, Link link) {
		this.view = view;
		if (view != null)
			this.model = view.getModel();


		links = new ArrayList<Link>();
		linkPainters = new ArrayList<LinkPainter>();
		sourceLinkElements = new ArrayList<AbstractLinkElement>();
		destinationLinkElements = new ArrayList<AbstractLinkElement>();

		if (ApplicationMode.PERSISTENT != MainFrame.getInstance().getAppMode()){

			
			sourceElement = link.getSourceConnector().getRepresentedElement();
			destinationElement = link.getDestinationConnector().getRepresentedElement();
			

			//if link represents a hierarchy, delete all child hierarchies as well
			if (sourceElement.getUmlElement() instanceof ParentChild){
				HierarchyElement hierarchyElement = ((UIClassElement)sourceElement).getHierarchyMap().get(link.getSourceConnector());
				Hierarchy hierarchy = hierarchyElement.getHierarchy();
				
				List<Hierarchy> successors = HierarchyUtil.allSuccessors(hierarchy);
				successors.add(hierarchy);


				for (Connector conn : ((UIClassElement)sourceElement).getHierarchyMap().keySet()){
					hierarchyElement = ((UIClassElement)sourceElement).getHierarchyMap().get(conn);
					if (successors.contains(hierarchyElement.getHierarchy())){
						links.add(conn.getLink());
						if (view != null)
							linkPainters.add(view.getLinkPainter(conn.getLink()));
						sourceElement = link.getSourceConnector().getRepresentedElement();
						destinationElement = link.getDestinationConnector().getRepresentedElement();
						hierarchyElement = ((UIClassElement)sourceElement).getHierarchyMap().get(conn.getLink().getSourceConnector());
						sourceLinkElements.add(hierarchyElement);
						destinationLinkElements.add(null);
					}
				}

			}
			else{
				//zoom and next
				links.add(link);
				if (view != null)
					linkPainters.add(view.getLinkPainter(link));
				sourceLinkElements.add(((UIClassElement) sourceElement).getCurrentElement(link.getSourceConnector()));
				destinationLinkElements.add(((UIClassElement) destinationElement).getCurrentElement(link.getDestinationConnector()));
			}

		}
	}

	@Override
	public void execute() {

		Link link;
		LinkPainter linkPainter = null;
		for (int i = 0; i < links.size(); i++){

			link = links.get(i);
			if (view != null)
				linkPainter = linkPainters.get(i);
			sourceElement = link.getSourceConnector().getRepresentedElement();
			
			destinationElement = link.getDestinationConnector().getRepresentedElement();
			sourceElement.unlink(link, true);
			destinationElement.unlink(link, false);
			model.removeLink(link);
			if (view != null)
				view.removeLinkPainters(linkPainter);
		}
		removedMappings = model.removeFromElementByConnectorStructure(links);
		if (view != null){
			view.getSelectionModel().removeAllSelectedElements();
			view.getSelectionModel().setSelectedLink(null);
		}
	}

	@Override
	public void undo() {

		for (Connector conn : removedMappings.keySet()){
			LinkableElement element = (LinkableElement) removedMappings.get(conn);
			element.getConnectors().add(conn);
		}
		model.addToElementByConnectorStructure(removedMappings);

		Link link;
		LinkPainter linkPainter = null;
		AbstractLinkElement sourceLinkElement, destinationLinkElement;

		for (int i = 0; i < links.size(); i++){
			link = links.get(i);
			
			if (view != null)
				linkPainter = linkPainters.get(i);
			sourceElement = link.getSourceConnector().getRepresentedElement();
			destinationElement = link.getDestinationConnector().getRepresentedElement();

			model.addLink(link);
			if (view != null)
				view.addLinkPainter(linkPainter);

			sourceLinkElement = sourceLinkElements.get(i);
			destinationLinkElement = destinationLinkElements.get(i);
			
			
			if (sourceLinkElement != null)
				sourceElement.setOldLink(link, sourceLinkElement, true);
			if (destinationLinkElement != null)
				destinationElement.setOldLink(link, destinationLinkElement, false);
		}
		if (view != null)
			view.getSelectionModel().setSelectedLink(links.get(links.size()-1));
	}

}
