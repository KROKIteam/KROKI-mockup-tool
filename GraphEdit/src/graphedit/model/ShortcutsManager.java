package graphedit.model;

import graphedit.app.MainFrame;
import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.shortcuts.ClassShortcut;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.view.ClassPainter;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ShortcutsManager {

	private List<LinkableElement> contents;
	
	private static ShortcutsManager shortcutsManager;
	
	
	public ShortcutsManager(){
		contents = new ArrayList<LinkableElement>();
	}
	
	public static ShortcutsManager getInsance(){
		if (shortcutsManager == null)
			shortcutsManager = new ShortcutsManager();
		return shortcutsManager;
	}
	
	public List<ElementPainter> createShortcutsAndPainters(double xDiff, double yDiff){
		List<ElementPainter> painters = new ArrayList<ElementPainter>();
		GraphEditModel currentModel = MainFrame.getInstance().getCurrentView().getModel();
		for (GraphElement element : contents){
			if (element instanceof Class){
				ClassShortcut classShortcut = new ClassShortcut(calculatePosition(
						(Point2D) element.getProperty(GraphElementProperties.POSITION), xDiff, yDiff),(Class)element, currentModel);
				((Class)element).addShortcut(classShortcut);
				ClassPainter painter = new ClassPainter(classShortcut);
				painters.add(painter);
			}
		}
		contents.clear();
		return painters;
	}
	
	public void prepareContents(){
		GraphEditView view = MainFrame.getInstance().getCurrentView();
		List<GraphElement> selectedElements = view.getSelectionModel().getSelectedElements();
		for (GraphElement el : selectedElements){
			if (el instanceof LinkableElement)
				contents.add((LinkableElement)el);
		}
	}
	
	
	private Point2D calculatePosition(Point2D elementPosition, double xDiff, double yDiff){
		return new Point2D.Double(elementPosition.getX() + xDiff, elementPosition.getY()+yDiff);
	}
	
}
