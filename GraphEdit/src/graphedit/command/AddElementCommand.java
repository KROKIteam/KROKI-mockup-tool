package graphedit.command;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.components.GraphElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.PersistentClassElement;
import graphedit.model.elements.UIClassElement;
import graphedit.model.enums.ClassStereotypeUI;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.VisibleElement;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;

public class AddElementCommand extends Command {

	private GraphElement component;
	private ElementPainter elementPainter;
	private GraphEditModel model;
	private ClassElement element;

	public AddElementCommand(GraphEditView view, GraphElement component,
			ElementPainter elementPainter) {
		this.model = view.getModel();
		this.view = view;
		this.component = component;
		this.elementPainter = elementPainter;
		
		if (component instanceof graphedit.model.components.Class){
			if (MainFrame.getInstance().getAppMode() != ApplicationMode.PERSISTENT)
				element = new UIClassElement(component, ClassStereotypeUI.STANDARD_PANEL);
			else 
				element = new PersistentClassElement(component);
		}
		element.setElement(component);
		this.component.setRepresentedElement(element);
		
	}

	@Override
	public void execute() {
	
		if (element.getUmlType() instanceof VisibleElement){
			model.getParentPackage().getUmlPackage().addOwnedType(element.getUmlType());
			
			element.getUmlType().setUmlPackage(model.getParentPackage().getUmlPackage());
			if (element instanceof UIClassElement)
				model.getParentPackage().getClassElementsByVisibleClassesMap().put((VisibleClass)element.getUmlType(), (UIClassElement) element);
			
			((StandardPanel) element.getUmlType()).getPersistentClass().setTableName(new NamingUtil().toDatabaseFormat(((StandardPanel) element.getUmlType()).project().getLabel(), ((StandardPanel) element.getUmlType()).getLabel()));
			((StandardPanel) element.getUmlType()).getPersistentClass().setLabelToCode(((StandardPanel) element.getUmlType()).project().isLabelToCode());
		}
		model.addDiagramElement(component);
		view.addElementPainter(elementPainter);
	}

	@Override
	public void undo() {
		model.getParentPackage().getUmlPackage().removeOwnedType(element.getUmlType());
		model.removeDiagramElement(component);
		view.removeElementPainter(component);
		view.getSelectionModel().removeAllSelectedElements();
		
		if (element instanceof UIClassElement)
			model.getParentPackage().getClassElementsByVisibleClassesMap().remove((VisibleClass)element.getUmlType());
	}

}
