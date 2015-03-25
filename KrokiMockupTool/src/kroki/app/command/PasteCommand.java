package kroki.app.command;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kroki.app.KrokiMockupToolApp;
import kroki.app.model.SelectionModel;
import kroki.app.view.Canvas;
import kroki.common.copy.DeepCopy;
import kroki.profil.VisibleElement;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;
import kroki.profil.utils.ElementsGroupUtil;
import kroki.profil.utils.UIPropertyUtil;
import kroki.profil.utils.VisibleClassUtil;
import kroki.uml_core_basic.UmlProperty;
import kroki.uml_core_basic.UmlType;
import kroki.uml_core_basic.UmlTypedElement;

/**
 * Command for pasting elements
 * @author Kroki Team
 */
public class PasteCommand implements Command {

	private List<VisibleElement> elements;
	private VisibleClass visibleClass;
    private ElementsGroup elementsGroup;
   // private Point point;
    private int classIndex, groupIndex;
    private boolean cutAction;
    private Map<UmlProperty, UmlProperty> oppositeMap = new HashMap<UmlProperty, UmlProperty>();
    private Map<UmlTypedElement, UmlType> typeMap = new HashMap<UmlTypedElement, UmlType>();
    private Map<VisibleElement, VisibleElement> copies = new HashMap<VisibleElement, VisibleElement>();
    private Map<VisibleAssociationEnd, VisibleClass> targetPanelsMap = new HashMap<VisibleAssociationEnd, VisibleClass>();
	
	public PasteCommand(VisibleClass visibleClass, ElementsGroup elementsGroup, List<VisibleElement> elements, Point point, boolean cutAction) {
        this.visibleClass = visibleClass;
        this.elementsGroup = elementsGroup;
        for (VisibleElement el : elements){
        	saveAndRemoveAttributes(el);
        	copies.put((VisibleElement)DeepCopy.copy(el), el);
        	restoreAttributes(el, el);
        }
        	
       // this.point = point;
        this.cutAction = cutAction;
        classIndex = visibleClass.getVisibleElementList().size();
        groupIndex = elementsGroup.getVisibleElementList().size();
    }
	
	@Override
	public void doCommand() {
		for (VisibleElement element : copies.keySet()) {
			restoreAttributes(copies.get(element), element);
			UIPropertyUtil.addVisibleElement(visibleClass,classIndex, element);
	        ElementsGroupUtil.addVisibleElement(elementsGroup, groupIndex, element);
	        
	        if (!cutAction) {
	        	element.changeUuid();
	        }
	        
	        if (element instanceof VisibleElement) {
				element.setParentGroup(elementsGroup);
				ElementsGroupUtil.addVisibleElement(elementsGroup, element);
				element.update();
			}
	        
	        elementsGroup.update();
	        visibleClass.update();
	        KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent().repaint();
	        
            if (element.getComponentType() != null){
            	VisibleClassUtil.incrementCount(visibleClass, element.getComponentType());
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
	        UIPropertyUtil.removeVisibleElement(visibleClass, element);
	        ElementsGroupUtil.removeVisibleElement(elementsGroup, element);
	        elementsGroup.update();
	        visibleClass.update();
	        
            if (element.getComponentType() != null){
				VisibleClassUtil.decrementCount(visibleClass, element.getComponentType());
			}
		}
	}
	
	
	private void saveAndRemoveAttributes(VisibleElement el){
		
		if (el instanceof UmlProperty){
			UmlProperty prop = (UmlProperty)el;
			oppositeMap.put(prop, prop.opposite());
			prop.setOpposite(null);
		}
		
		if (el instanceof UmlTypedElement){
			UmlTypedElement typed = (UmlTypedElement) el;
			typeMap.put(typed, typed.type());
			typed.setType(null);
		}
		
		if (el instanceof VisibleAssociationEnd){
			VisibleAssociationEnd end = (VisibleAssociationEnd) el;
			targetPanelsMap.put(end, end.getTargetPanel());
			end.setTargetPanel(null);
			end.setActivationPanel(null);
		}
		
		if (el instanceof VisibleProperty){
			VisibleProperty prop = (VisibleProperty) el;
			prop.setUmlClass(null);
		}
	}
	
	private void restoreAttributes(VisibleElement keyEl, VisibleElement targetEl){
		
		if (targetEl instanceof UmlProperty){
			UmlProperty prop = (UmlProperty)targetEl;
			prop.setOpposite(oppositeMap.get(keyEl));
		}
		
		if (targetEl instanceof UmlTypedElement){
			UmlTypedElement typed = (UmlTypedElement) targetEl;
			typed.setType(typeMap.get(keyEl));
		}
		
		if (targetEl instanceof VisibleAssociationEnd){
			VisibleAssociationEnd end = (VisibleAssociationEnd) targetEl;
			end.setTargetPanel(targetPanelsMap.get(keyEl));
			end.setActivationPanel(visibleClass);
		}
		
		if (targetEl instanceof VisibleProperty){
			VisibleProperty prop = (VisibleProperty) targetEl;
			if (prop.umlClass() != null)
				prop.setUmlClass(visibleClass);
		}
	}

}
