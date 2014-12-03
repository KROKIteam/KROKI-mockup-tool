package kroki.api.commons.property;

import kroki.api.profil.group.ElementsGroupUtil;
import kroki.api.profil.property.UIPropertyUtil;
import kroki.api.util.Util;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;

public class UIPropertyCommons {

	
	

	/**
	 * Creates a new visible property and adds in to the given panel
	 * @param label Label of visible property
	 * @param visible Is property visible
	 * @param type Component type (text field, text area, check box etc.)
	 * @param panel Visible panel to which the property is being added
	 * @param indexClass Class index of the property (-1 if the visible property is being created for the first time, old index in case of undo operation)
	 * @param indexGroup Group index of the property (-1 if the visible property is being created for the first time, old index in case of undo operation)
	 * @return Created visible property
	 */
	public static VisibleProperty makeVisiblePropertyAt(String label, boolean visible, ComponentType type, VisibleClass panel, int indexClass, int indexGroup) {
		NamingUtil namer = new NamingUtil();
		int propertiesGroup = Util.getPropertiesGroupIndex(panel);
		if (propertiesGroup == -1)
			return null;
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(propertiesGroup);
		VisibleProperty property = new VisibleProperty(label, visible, type);	
		property.setName(label);
		if(type == ComponentType.TEXT_FIELD) 
			property.setDataType("String");
		property.setColumnLabel(namer.toDatabaseFormat(panel.getLabel(), label));

		if (indexGroup != -1)
			ElementsGroupUtil.addVisibleElement(gr, indexGroup, property);
		else
			ElementsGroupUtil.addVisibleElement(gr, property);
		if (indexClass != -1)
			UIPropertyUtil.addVisibleElement(panel, indexClass, property);
		else
			UIPropertyUtil.addVisibleElement(panel, property);
		return property;
	}


	/**
	 * Creates visible property by calling {@link #makeVisiblePropertyAt()} and passing -1 as class and group index
	 * @param label Label of visible property
	 * @param visible Is property visible
	 * @param type Component type (text field, text area, check boc etc.)
	 * @param panel Visible panel to which the property is being added
	 * @return Created visible property
	 */
	public static VisibleProperty makeVisibleProperty(String label, boolean visible, ComponentType type, VisibleClass panel){
		return makeVisiblePropertyAt(label, visible, type, panel, -1, -1);
	}
	
	
	/**
	 * Removes property with given class index from visible class
	 * @param visibleClass Visible class countaining the property
	 * @param classIndex Class index of the property
	 */
	public static void removeProperty(VisibleClass visibleClass, int classIndex){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getPropertiesGroupIndex(visibleClass));
		ElementsGroupUtil.removeVisibleElement(gr, UIPropertyUtil.getVisibleElementAt(visibleClass, classIndex));
		UIPropertyUtil.removeVisibleElement(visibleClass, classIndex);
	}
	
	/**
	 * Moves element down
	 * @param visibleClass Visible class containing the element
	 * @param classIndex Class index of the element
	 * @param groupIndex Group index of the element
	 * @param type 1 for swapping properties, 2 for swapping operations, 
	 */
	public static void moveElementDown(VisibleClass visibleClass, int classIndex, int groupIndex, int type) {

		ElementsGroup gr;
		if (type == 1)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getPropertiesGroupIndex(visibleClass));
		else if (type == 2)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getOperationGroupIndex(visibleClass));
		else 
			return;

		//izbaci oba i dodaj na suprotne pozicije
		VisibleElement thisProp = (VisibleElement) ElementsGroupUtil.getVisibleElementAt(gr, groupIndex);
		VisibleElement otherProp = (VisibleElement) ElementsGroupUtil.getVisibleElementAt(gr, groupIndex + 1);

		UIPropertyCommons.swapProperties(visibleClass, thisProp, otherProp, groupIndex + 1, groupIndex, classIndex + 1, classIndex, gr);

	}
	
	/**
	 * Moves element up 
	 * @param visibleClass Visible class containing the element
	 * @param classIndex Class index of the element
	 * @param groupIndex Group index of the element
	 * @param type 1 for swapping properties, 2 for swapping operations, 
	 */
	public static void moveElementUp(VisibleClass visibleClass, int classIndex, int groupIndex, int type) {
		
		ElementsGroup gr;
		if (type == 1)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getPropertiesGroupIndex(visibleClass));
		else if (type == 2)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getPropertiesGroupIndex(visibleClass));
		else 
			return;

		//izbaci oba i dodaj na suprotne pozicije
		VisibleElement thisProp = (VisibleElement) ElementsGroupUtil.getVisibleElementAt(gr, groupIndex);
		VisibleElement otherProp = (VisibleElement) ElementsGroupUtil.getVisibleElementAt(gr, groupIndex - 1);

		UIPropertyCommons.swapProperties(visibleClass, thisProp, otherProp, groupIndex - 1, groupIndex, classIndex - 1, classIndex, gr);

	}
	
	/**
	 * Swaps positions of two visible elements
	 * @param visibleClass Class containing the elements
	 * @param p1 The first visible element
	 * @param p2 The second visible element
	 * @param firstIndexGr First element's group index
	 * @param secondIndexGr Second element's group index
	 * @param firstIndexCl First element's class index
	 * @param secondIndexCl Second element's class index
	 * @param gr Element group containing the elements
	 */
	public static void swapProperties(VisibleClass visibleClass, VisibleElement p1, VisibleElement p2, int firstIndexGr, int secondIndexGr,
			int firstIndexCl, int secondIndexCl, ElementsGroup gr){


		ElementsGroupUtil.removeVisibleElement(gr, p1);
		ElementsGroupUtil.removeVisibleElement(gr, p2);
		if (firstIndexGr < secondIndexGr){
			ElementsGroupUtil.addVisibleElement(gr, firstIndexGr, p1);
			ElementsGroupUtil.addVisibleElement(gr, secondIndexGr, p2);
		}
		else{
			ElementsGroupUtil.addVisibleElement(gr, secondIndexGr, p2);
			ElementsGroupUtil.addVisibleElement(gr, firstIndexGr, p1);
		}

		UIPropertyUtil.removeVisibleElement(visibleClass, p1);
		UIPropertyUtil.removeVisibleElement(visibleClass, p2);
		if (firstIndexCl < secondIndexCl){
			UIPropertyUtil.addVisibleElement(visibleClass, firstIndexCl, p1);
			UIPropertyUtil.addVisibleElement(visibleClass, secondIndexCl, p2);
		}
		else{
			UIPropertyUtil.addVisibleElement(visibleClass, secondIndexCl, p2);
			UIPropertyUtil.addVisibleElement(visibleClass, firstIndexCl, p1);
		}
	}

}
