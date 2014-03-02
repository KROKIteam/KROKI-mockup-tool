package graphedit.model.components.shortcuts;

import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums.DiagramProperties;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.PackageProperties;

import java.awt.geom.Point2D;

public class ClassShortcut extends Class implements Shortcut{

	private static final long serialVersionUID = 1L;
	private Class shortcutTo;
	private GraphEditModel shortcutToModel;
	private String shortcutInfo ="";
	
	public ClassShortcut(Point2D position, Class shortcutTo, GraphEditModel shortcutToModel){
		//sve isto samo naziv sa Shortcut to...
		super(position, false, 0);
		this.shortcutTo = (Class) shortcutTo;
		setProperty(GraphElementProperties.NAME, shortcutTo.getProperty(GraphElementProperties.NAME));
		this.shortcutToModel = shortcutToModel;
		setShortcutInfo(shortcutToModel);
		
	}

	public Class getShortcutTo() {
		return shortcutTo;
	}

	public void setShortcutTo(Class shortcutTo) {
		this.shortcutTo = shortcutTo;
	}

	@Override
	public GraphElement shortcutTo() {
		return shortcutTo;
	}

	@Override
	public void setShortcutTo(LinkableElement shortcutTo) {
		this.shortcutTo = (Class) shortcutTo;
		setProperty(GraphElementProperties.NAME, "Shortcut to " + shortcutTo.getProperty(GraphElementProperties.NAME));
		
	}

	@Override
	public GraphEditModel shortcutToModel() {
		return shortcutToModel;
	}

	@Override
	public void setShortcutToModel(GraphEditModel model) {
		this.shortcutToModel = model;
		
	}
	
	@Override
	public void setShortcutInfo(GraphEditModel currentModel){
			if (!currentModel.getDiagramElements().contains(shortcutTo))
				shortcutInfo = "( " + (String)shortcutToModel.getParentPackage().getProperty(PackageProperties.NAME) + " )";
	}

	@Override
	public String shortcutInfo() {
		return shortcutInfo;
	}

}
