package graphedit.util;

import kroki.profil.panel.container.ParentChild;
import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.components.Link;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.LinkType;
import graphedit.model.enums.ClassStereotypeUI;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkProperties;

public class LinkingUtil {


	public static String otherCardinality(String cardinality, Link link, boolean source){
		if (MainFrame.getInstance().getAppMode() != ApplicationMode.USER_INTERFACE)
			return "";
		if (link.getSourceConnector().getRepresentedElement().element().getProperty(GraphElementProperties.STEREOTYPE).equals(ClassStereotypeUI.PARENT_CHILD.toString()) || link.getDestinationConnector().getRepresentedElement().element().getProperty(GraphElementProperties.STEREOTYPE).equals(ClassStereotypeUI.PARENT_CHILD.toString()))
			return "";
		String otherCardinality;
		if (source)
			otherCardinality = (String) link.getProperty(LinkProperties.DESTINATION_CARDINALITY);
		else
			otherCardinality = (String) link.getProperty(LinkProperties.SOURCE_CARDINALITY);

		if (otherCardinality.charAt(otherCardinality.length()-1) != cardinality.charAt(cardinality.length()-1))
			return "";

		if (cardinality.endsWith("1"))
			return "*";
		return "1..1";
	}

	public static LinkType checkLinkType(Link link){
		if ( ((ClassElement)link.getSourceConnector().getRepresentedElement()).getUmlType()  instanceof ParentChild || ((ClassElement)link.getSourceConnector().getRepresentedElement()).getUmlType() instanceof ParentChild)
			return LinkType.HIERARCHY;
		return  LinkType.NEXT_ZOOM;
	}

}
