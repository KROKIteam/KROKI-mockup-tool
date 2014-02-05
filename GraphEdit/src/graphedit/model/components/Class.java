package graphedit.model.components;

import graphedit.app.MainFrame;
import graphedit.model.components.Link.LinkType;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.properties.ApplicationModeProperties;
import graphedit.util.Validator;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Class extends LinkableElement {

	private static final long serialVersionUID = 1L;
	private ApplicationModeProperties appModeProperties;

	public Class() {}

	public Class(Point2D position) {

		appModeProperties = ApplicationModeProperties.getInstance();

		properties.set(GraphElementProperties.POSITION, position);

		properties.set(GraphElementProperties.ATTRIBUTES, new ArrayList<Attribute>());
		properties.set(GraphElementProperties.METHODS, new ArrayList<Method>());

		String name;
		do{
			name = (String) appModeProperties.getPropertyValue("className") + MainFrame.getInstance().incrementClassCounter();
		}while(Validator.classHasName(name));


		properties.set(GraphElementProperties.NAME, name);
		properties.set(GraphElementProperties.STEREOTYPE, (String)appModeProperties.getPropertyValue("classStereotype"));
	}

	public Class (Point2D position, boolean setName){
		appModeProperties = ApplicationModeProperties.getInstance();

		properties.set(GraphElementProperties.POSITION, position);

		properties.set(GraphElementProperties.ATTRIBUTES, new ArrayList<Attribute>());
		properties.set(GraphElementProperties.METHODS, new ArrayList<Method>());

		if (setName){
			String name;
			do{
				name = (String) appModeProperties.getPropertyValue("className") + MainFrame.getInstance().incrementClassCounter();
			}while(Validator.classHasName(name));
			properties.set(GraphElementProperties.NAME, name);
		}
		properties.set(GraphElementProperties.STEREOTYPE, (String)appModeProperties.getPropertyValue("classStereotype"));
	}



	@Override
	public boolean linkingCanStart(LinkType linkType) {
		return true;
	}

	@Override
	public boolean linkingCanEnd(LinkType linkType, LinkableElement sourceElement) {
		if (linkType==Link.LinkType.REALIZATION ||linkType==Link.LinkType.REQUIRE)
			return false;
		if (linkType==Link.LinkType.GENERALIZATION && (sourceElement instanceof Interface))
			return false;
		return true;
	}

}