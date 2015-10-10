package graphedit.command;

import graphedit.app.MainFrame;
import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.view.ClassPainter;
import graphedit.view.GraphEditView;

public abstract class Command {

	protected GraphEditView view;

	public abstract void execute();

	public abstract void undo();


	protected void updatePainters(LinkableElement element){
		GraphEditView containingView = getContainingView(element);

		if (containingView !=null){
			((ClassPainter)containingView.getElementPainter(element)).setUpdated(true);
			((ClassPainter)containingView.getElementPainter(element)).setAttributesOrMethodsUpdated(true);
			((ClassPainter)containingView.getElementPainter(element)).setAttributesOrMethodsUpdated(true);
		}
		Class c;
		for (Shortcut s : element.getShortcuts()){
			if (s instanceof Class){
				c = (Class) s;
				containingView = getContainingView(c);
				if (containingView != null){
					((ClassPainter)containingView.getElementPainter(c)).setUpdated(true);
					((ClassPainter)containingView.getElementPainter(c)).setAttributesOrMethodsUpdated(true);
					((ClassPainter)containingView.getElementPainter(c)).setAttributesOrMethodsUpdated(true);

				}
			}
		}
	}

	private GraphEditView getContainingView(GraphElement element){
		if (view.getModel().getDiagramElements().contains(element))
			return view;
		else
			return MainFrame.getInstance().getViewContaining(element);
	}
}