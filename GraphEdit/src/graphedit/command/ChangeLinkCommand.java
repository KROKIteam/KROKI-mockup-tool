package graphedit.command;

import graphedit.model.components.Link;
import graphedit.model.elements.AbstractLinkElement;
import graphedit.model.elements.UIClassElement;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.view.GraphEditView;

public class ChangeLinkCommand extends Command {

	private Link link;
	private String oldName, newName;
	private LinkProperties property;
	private AbstractLinkElement nextZoom;
	
	public ChangeLinkCommand(GraphEditView view, Link link, String newName, LinkProperties property) {
		this.view = view;
		this.link = link;
	

		switch (property) {
		case DESTINATION_CARDINALITY : 
			if (link.getDestinationConnector().getRepresentedElement() instanceof UIClassElement){
				nextZoom = ((UIClassElement)link.getSourceConnector().getRepresentedElement()).getCurrentElement(link.getSourceConnector());
			}
			this.oldName = (String)link.getProperty(LinkProperties.DESTINATION_CARDINALITY);
			break;
		case SOURCE_CARDINALITY : {
			if (link.getSourceConnector().getRepresentedElement() instanceof UIClassElement){
				nextZoom = ((UIClassElement)link.getDestinationConnector().getRepresentedElement()).getCurrentElement(link.getDestinationConnector());
			}
			this.oldName = (String)link.getProperty(LinkProperties.SOURCE_CARDINALITY);
			break;
		}
		case DESTINATION_ROLE : 
			this.oldName = (String)link.getProperty(LinkProperties.DESTINATION_ROLE);
			break;
		case SOURCE_ROLE : 
			this.oldName = (String)link.getProperty(LinkProperties.SOURCE_ROLE);
			break;
		case STEREOTYPE : 
			this.oldName = (String)link.getProperty(LinkProperties.STEREOTYPE);
			break;
		default : 
			this.oldName = (String)link.getProperty(LinkProperties.NAME);
			break;
		}
		this.newName = newName;
		this.property = property;
	}

	@Override
	public void execute() {
		link.setProperty(property, newName);
		link.getSourceConnector().getRepresentedElement().changeLinkProperty(link, property, newName);
		link.getDestinationConnector().getRepresentedElement().changeLinkProperty(link, property, newName);
		view.getModel().fireUpdates();
	}

	@Override
	public void undo() {
		link.setProperty(property, oldName);
		
		if (nextZoom!=null){
			if (property == LinkProperties.SOURCE_CARDINALITY)
				link.getDestinationConnector().getRepresentedElement().setOldLink(link, nextZoom);
			else
				link.getSourceConnector().getRepresentedElement().setOldLink(link, nextZoom);
		}
		else{
		link.getSourceConnector().getRepresentedElement().changeLinkProperty(link, property, oldName);
		link.getDestinationConnector().getRepresentedElement().changeLinkProperty(link, property, oldName);
		}
		view.getModel().fireUpdates();
	}

}
