package graphedit.command;

import graphedit.model.components.Link;
import graphedit.model.elements.AbstractLinkElement;
import graphedit.model.elements.UIClassElement;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.view.GraphEditView;

public class ChangeLinkCommand extends Command {

	private Link link;
	private Object oldName, newName;
	private LinkProperties property;
	private AbstractLinkElement nextZoom;
	private boolean changeNavigable;
	private LinkProperties navigableProperty;

	public ChangeLinkCommand(GraphEditView view, Link link, String newName, LinkProperties property) {
		this.view = view;
		this.link = link;
		this.newName = newName;
		this.property = property;

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
		case SOURCE_NAVIGABLE :{
			this.newName = 	Boolean.valueOf(newName);
			if (link.getSourceConnector().getRepresentedElement() instanceof UIClassElement){
				nextZoom = ((UIClassElement)link.getDestinationConnector().getRepresentedElement()).getCurrentElement(link.getDestinationConnector());
			}
			this.oldName = (Boolean)link.getProperty(LinkProperties.SOURCE_NAVIGABLE);
			break;
		}
		case DESTINATION_NAVIGABLE :{
			this.newName = 	Boolean.valueOf(newName);
			if (link.getSourceConnector().getRepresentedElement() instanceof UIClassElement){
				nextZoom = ((UIClassElement)link.getSourceConnector().getRepresentedElement()).getCurrentElement(link.getSourceConnector());
			}
			this.oldName = (Boolean)link.getProperty(LinkProperties.DESTINATION_NAVIGABLE);
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
		case SHOW_DESTINATION_ROLE:
			this.oldName = (Boolean)link.getProperty(LinkProperties.SHOW_DESTINATION_ROLE);
			this.newName = 	Boolean.valueOf(newName);
			break;
		case SHOW_SOURCE_ROLE:
			this.oldName = (Boolean)link.getProperty(LinkProperties.SHOW_SOURCE_ROLE);
			this.newName = 	Boolean.valueOf(newName);
			break;
		default : 
			this.oldName = (String)link.getProperty(LinkProperties.NAME);
			break;
		}
		if (property == LinkProperties.DESTINATION_CARDINALITY || property == LinkProperties.SOURCE_CARDINALITY){
			changeNavigable = !(Boolean)link.getProperty(LinkProperties.DESTINATION_NAVIGABLE) || 
					!(Boolean)link.getProperty(LinkProperties.SOURCE_NAVIGABLE);
			if (property == LinkProperties.DESTINATION_CARDINALITY)
				navigableProperty = LinkProperties.DESTINATION_NAVIGABLE;
			else
				navigableProperty = LinkProperties.SOURCE_NAVIGABLE;
		}
	}

	@Override
	public void execute() {

		link.getSourceConnector().getRepresentedElement().changeLinkProperty(link, property, newName, true);
		link.getDestinationConnector().getRepresentedElement().changeLinkProperty(link, property, newName, false);
		link.setProperty(property, newName);
		if (changeNavigable){
			link.changeNaviglable(navigableProperty);
		}
		view.getModel().fireUpdates();
		
	}

	@Override
	public void undo() {
		link.setProperty(property, oldName);
		if (property == LinkProperties.SOURCE_CARDINALITY || property == LinkProperties.SOURCE_NAVIGABLE)
			link.getDestinationConnector().getRepresentedElement().setOldLink(link, nextZoom, false);
		else if (property == LinkProperties.DESTINATION_CARDINALITY || property == LinkProperties.DESTINATION_NAVIGABLE)
			link.getSourceConnector().getRepresentedElement().setOldLink(link, nextZoom, true);
		else{
			link.getSourceConnector().getRepresentedElement().changeLinkProperty(link, property, oldName, true);
			link.getDestinationConnector().getRepresentedElement().changeLinkProperty(link, property, oldName, false);
		}
		if (changeNavigable){
			link.changeNaviglable(navigableProperty);
		}
		view.getModel().fireUpdates();
	}

}
