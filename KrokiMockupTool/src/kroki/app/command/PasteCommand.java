package kroki.app.command;

import graphedit.util.SerializationUtility;

import java.awt.Point;
import java.util.List;

import kroki.app.KrokiMockupToolApp;
import kroki.app.model.SelectionModel;
import kroki.app.view.Canvas;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.VisibleElement;
import kroki.profil.association.Next;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;

public class PasteCommand implements Command {

	private List<VisibleElement> elements;
	private VisibleClass visibleClass;
    private ElementsGroup elementsGroup;
    private Point point;
    private int classIndex, groupIndex;
    private boolean cutAction;
	
    @SuppressWarnings("unchecked")
	public PasteCommand(VisibleClass visibleClass, ElementsGroup elementsGroup, List<VisibleElement> elements, Point point, boolean cutAction) {
    	Object deserializedObjects[] = SerializationUtility.deepCopy(elements, elements);
        this.visibleClass = visibleClass;
        this.elementsGroup = elementsGroup;
        this.elements = (List<VisibleElement>)deserializedObjects[0];
        this.point = point;
        this.cutAction = cutAction;
        classIndex = visibleClass.getVisibleElementList().size();
        groupIndex = elementsGroup.getVisibleElementList().size();
    }
	
	@Override
	public void doCommand() {
		for (VisibleElement element : elements) {
			visibleClass.addVisibleElement(classIndex, element);
	        elementsGroup.addVisibleElement(groupIndex, element);
	        
	        if (!cutAction) {
	        	element.changeUuid();
	        }
	        
	        if (element instanceof VisibleElement) {
				element.setParentGroup(elementsGroup);
				elementsGroup.addVisibleElement(element);
				element.update();
			}
	        
	        /*
	        if (element instanceof VisibleProperty) {
	            VisibleProperty prop = (VisibleProperty) element;
	            NamingUtil namer = new NamingUtil();
	            prop.setColumnLabel(namer.toDatabaseFormat(visibleClass.getLabel(), element.getLabel()));
	        } else if (element instanceof Next) {
				Next next = (Next)element;
				next.setParentGroup(elementsGroup);
				next.setActivationPanel(visibleClass);
				next.getActivationPanel().update();
				elementsGroup.addVisibleElement(element);
			} 
	        PREVIOUS ITERATION VISIBLE ELEMENT IF WAS LAST
	        */
	        elementsGroup.update();
	        visibleClass.update();
	        KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent().repaint();
	        
            if (element.getComponentType() != null){
				visibleClass.incrementCount(element.getComponentType());
			}
		}
	}

	@Override
	public void undoCommand() {
		for (VisibleElement element : elements) {
	        Canvas c = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
	        SelectionModel selectionModel = c.getSelectionModel();
	        if (selectionModel.isSelected(element)) {
	            selectionModel.removeFromSelection(element);
	        }
	        visibleClass.removeVisibleElement(element);
	        elementsGroup.removeVisibleElement(element);
	        elementsGroup.update();
	        visibleClass.update();
	        
            if (element.getComponentType() != null){
				visibleClass.decrementCount(element.getComponentType());
			}
		}
	}

}
