package graphedit.command;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.components.Class;
import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Package;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.elements.AbstractLinkElement;
import graphedit.model.elements.GraphEditElement;
import graphedit.model.elements.UIClassElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kroki.uml_core_basic.UmlNamedElement;

public class ChangeElementCommand extends Command {

	/*
	 * Kada se uradi i postavljanje pozicije i velicine
	 *  obezbediti da se radi bas za shortcut...
	 */
	
	
	private GraphElement element;
	private String oldName, newName;
	private GraphElementProperties property;
	private GraphEditElement graphEditElement;
	private UmlNamedElement umlElement;
	private List<Link> links;
	private  List<LinkPainter> linkPainters;
	private Map<Connector, GraphElement> removedMappings;
	private List<AbstractLinkElement> sourceList = new ArrayList<AbstractLinkElement>(), destinationList = new ArrayList<AbstractLinkElement>();

	public ChangeElementCommand(GraphEditView view, GraphElement element, String newName, GraphElementProperties property) {
		this.view = view;
		this.oldName = (String)element.getProperty(property);
		this.newName = newName;
		this.property = property;
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
		
		graphEditElement = this.element.getRepresentedElement();
		umlElement = graphEditElement.getUmlElement();
		this.links =view.getModel().getAssociatedLinks(element);
		this.linkPainters = view.getLinkPainters(links);
		
		if (MainFrame.getInstance().getAppMode() != ApplicationMode.PERSISTENT && links!=null)
			for (Link link : links){
				sourceList.add(((UIClassElement)link.getSourceConnector().getRepresentedElement()).getCurrentElement(link.getSourceConnector()));
				destinationList.add(((UIClassElement)link.getDestinationConnector().getRepresentedElement()).getCurrentElement(link.getDestinationConnector()));
			}

	}

	@Override
	public void execute() {
		element.setProperty(property, newName);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
		}
		if (property == GraphElementProperties.NAME){
			graphEditElement.setName(newName);
			if (graphEditElement.element() instanceof graphedit.model.components.Package){
				view.getModel().removeGraphEditPackage((Package) graphEditElement.element());
				view.getModel().sortedInsertPackage((Package) graphEditElement.element());
			}
			else{
				view.getModel().removeDiagramElement(graphEditElement.element());
				view.getModel().sortedInsert(graphEditElement.element());
			}
		}
		else if (property == GraphElementProperties.STEREOTYPE){
			if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE 
					|| MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_MIXED){
				for (Link link : links){
					view.getModel().removeLinks(links);
					removedMappings = view.getModel().removeFromElementByConnectorStructure(links);
					view.removeLinkPainters(linkPainters);
					GraphEditElement sourceElement = link.getSourceConnector().getRepresentedElement();
					GraphEditElement destinationElement = link.getDestinationConnector().getRepresentedElement();
					sourceElement.unlink(link, true);
					destinationElement.unlink(link, false);
				}
				((UIClassElement)graphEditElement).changeClassStereotype(newName);
			}
		}
		view.getModel().fireUpdates();
	}

	@Override
	public void undo() {
		element.setProperty(property, oldName);
		
		if (property == GraphElementProperties.NAME){
			graphEditElement.setName(oldName);
			graphEditElement.setName(newName);
			if (graphEditElement.element() instanceof graphedit.model.components.Package){
				view.getModel().removeGraphEditPackage((Package) graphEditElement.element());
				view.getModel().sortedInsertPackage((Package) graphEditElement.element());
			}
			else{
				view.getModel().removeDiagramElement(graphEditElement.element());
				view.getModel().sortedInsert(graphEditElement.element());
			}
		}
		
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
		}
		
		if (property == GraphElementProperties.STEREOTYPE){
			graphEditElement.setUmlElement(umlElement);
			if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE 
					|| MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_MIXED){
				view.getModel().addLinks(links);
				if (removedMappings != null && removedMappings.size() > 0)
					view.getModel().addToElementByConnectorStructure(removedMappings);
				if (linkPainters != null && linkPainters.size() > 0)
					view.addLinkPainters(linkPainters);
				Link link;
				for (int i=0; i<links.size();i++){
					link = links.get(i);
					GraphEditElement sourceElement = link.getSourceConnector().getRepresentedElement();
					GraphEditElement destinationElement = link.getDestinationConnector().getRepresentedElement();
					if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE 
							|| MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_MIXED){
						sourceElement.setOldLink(link, sourceList.get(i), true);
						destinationElement.setOldLink(link, destinationList.get(i), false);
					}
				}
			}

		}
		view.getModel().fireUpdates();
	}

}
