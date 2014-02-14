package graphedit.model.elements;

import graphedit.app.MainFrame;
import graphedit.model.components.Attribute;
import graphedit.model.components.Class;
import graphedit.model.components.ClassStereotypeUI;
import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.Method;
import graphedit.model.components.MethodStereotypeUI;
import graphedit.model.components.Parameter;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.util.NameTransformUtil;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;

import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.BussinessOperation;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.VisibleProperty;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlNamedElement;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlProperty;
import kroki.uml_core_basic.UmlType;

public class UIClassElement extends ClassElement{

	private static final long serialVersionUID = 1L;

	private VisibleClass visibleClass;

	public static final int STANDARD_PANEL_PROPERTIES = 1;
	public static final int STANDARD_PANEL_OPERATIONS = 2;

	public static final int PARENTCHILD_PANEL_PROPERTIES = 0;
	public static final int PARENTCHILD_PANEL_OPERATIONS = 1;

	private  int propertiesGroup = STANDARD_PANEL_PROPERTIES;
	private  int operationsGroup = STANDARD_PANEL_OPERATIONS;

	private int zoomCounter = 0;
	private int linkCounter = 0;

	private HashMap<Connector, NextZoomElement> zoomMap  = new HashMap<Connector, NextZoomElement>();
	private HashMap<Connector, NextZoomElement> nextMap  = new HashMap<Connector, NextZoomElement>();
	private HashMap<Connector, HierarchyElement> hierarchyMap = new HashMap<Connector, HierarchyElement>();


	private enum LinkEnd {ZOOM, NEXT};



	public UIClassElement(GraphElement element, ClassStereotypeUI stereotype){
		this.element = element;

		if (stereotype == ClassStereotypeUI.STANDARD_PANEL){
			this.umlClass = new StandardPanel();
			visibleClass = (VisibleClass)umlClass;
			((StandardPanel)visibleClass).setLabel((String) element.getProperty(GraphElementProperties.NAME));
			((StandardPanel) visibleClass).getPersistentClass().setName(NameTransformUtil.labelToCamelCase((String) element.getProperty(GraphElementProperties.NAME), true));
		}
		else{
			this.umlClass = new ParentChild();
			visibleClass = (VisibleClass)umlClass;
			((ParentChild)visibleClass).setLabel((String) element.getProperty(GraphElementProperties.NAME));
		}
	}

	public UIClassElement(VisibleClass visibleClass, Point2D position){
		umlClass = visibleClass;
		this.visibleClass = visibleClass;
		initElement(position);
	}

	/**
	 *  Creates panel instance based upon content of the diagram's class element
	 */
	@SuppressWarnings("unchecked")
	public void formPanel(){

		if (element.getProperty(GraphElementProperties.STEREOTYPE).equals("StandardPanel")){
			umlClass = new StandardPanel();
			((StandardPanel)umlClass).setLabel((String) element.getProperty(GraphElementProperties.NAME));
		}
		else {
			umlClass = new ParentChild();
			((ParentChild)umlClass).setLabel((String) element.getProperty(GraphElementProperties.NAME));
		}
		visibleClass = (VisibleClass) umlClass;
		visibleClass.setName((String) element.getProperty(GraphElementProperties.NAME));


		//create properties
		for (Attribute attribute : (List<Attribute>)element.getProperty(GraphElementProperties.ATTRIBUTES))
			addAttribute(attribute, -1,-1);
		for (Method method : (List<Method>)element.getProperty(GraphElementProperties.METHODS))
			addMethod(method, -1, -1);

	}

	public void formPanel(String name, String label, List<VisibleElement> visibleElements, String stereotype){
		if (stereotype.equals(ClassStereotypeUI.STANDARD_PANEL.toString()))
			umlClass = new StandardPanel();
		else if (stereotype.equals(ClassStereotypeUI.PARENT_CHILD.toString()))
			umlClass = new ParentChild();
		visibleClass = (VisibleClass)umlClass;
		visibleClass.setLabel(label);
		visibleClass.setName(name);
		for (int i=0; i<visibleElements.size();i++){
			VisibleElement element = visibleElements.get(i);
			if (element.getParentGroup() == null)
				continue;
			if (element instanceof VisibleAssociationEnd)
				continue;
			if (element instanceof UmlProperty)
				makeVisiblePropertyAt(element.getLabel(), element.isVisible(), element.getComponentType(), visibleClass, -1, -1);
			else if (element instanceof UmlOperation){
				String opStereotype;
				if (element instanceof Transaction)
					opStereotype = "Transaction";
				else 
					opStereotype = "Report";
				makeVisibleOperation(element.getLabel(), element.isVisible(), element.getComponentType(), visibleClass, opStereotype, -1, -1);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void initElement(Point2D position){
		Attribute attribute;
		Method method;
		element = new Class(position);
		element.setProperty(GraphElementProperties.NAME, NameTransformUtil.labelToCamelCase(visibleClass.getLabel(),true));
		element.setRepresentedElement(this);
		if (visibleClass instanceof StandardPanel)
			element.setProperty(GraphElementProperties.STEREOTYPE,ClassStereotypeUI.STANDARD_PANEL.toString());
		else
			element.setProperty(GraphElementProperties.STEREOTYPE, ClassStereotypeUI.PARENT_CHILD.toString());

		for (VisibleElement type : visibleClass.getVisibleElementList()){
			if (type instanceof Zoom || type instanceof Next )
				continue;
			if (type instanceof VisibleProperty){
				attribute = new Attribute(NameTransformUtil.labelToCamelCase(type.getLabel(), true), NameTransformUtil.transformUppercaseWithoutSpaces(type.getComponentType().toString()));
				((List<Attribute>) element.getProperty(GraphElementProperties.ATTRIBUTES)).add(attribute);
				attribute.setUmlProperty((VisibleProperty)type);
			}
			else if (type instanceof BussinessOperation){
				if (type instanceof Report)
					method = new Method(NameTransformUtil.labelToCamelCase(((BussinessOperation) type).getLabel(), false),"void","Report"); 
				else
					method = new Method(NameTransformUtil.labelToCamelCase(((BussinessOperation) type).getLabel(), false),"void", "Transaction");
				method.setUmlOperation(( BussinessOperation)type);
				((List<Method>) element.getProperty(GraphElementProperties.METHODS)).add(method);
			}
		}
	}



	public void addAttribute(Attribute attribute, int ... args) {
		int classIndex = args[0];
		int groupIndex = args[1];

		String propLabel = attribute.getName();
		String type = attribute.getType();
		ComponentType componentType = getComponentType(type);
		VisibleProperty prop = makeVisiblePropertyAt(propLabel, true, componentType, visibleClass, classIndex, groupIndex);
		attribute.setUmlProperty(prop);
	}

	public void addMethod(Method method, int ... args){
		int classIndex = args[0];
		int groupIndex = args[1];

		String propLabel = method.getName();
		ComponentType type = getComponentType(method.getStereotype());
		VisibleOperation oper = makeVisibleOperation(propLabel, true, type, visibleClass, method.getStereotype(), classIndex, groupIndex);
		method.setUmlOperation(oper);
	}


	public void removeAttribute(int classIndex){
		removeProperty(classIndex);
	}

	public void removeMethod(int classIndex){
		removeOperation(classIndex);
	}


	public VisibleOperation makeVisibleOperation(String label, boolean visible, ComponentType componentType, VisibleClass panel, String operationType, int indexClass, int indexGroup){
		VisibleOperation operation;
		if (operationType.equals(MethodStereotypeUI.REPORT.toString()))
			operation = new Report(label, visible, componentType);
		else
			operation = new Transaction(label, visible, componentType);
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(operationsGroup);
		if (indexGroup != -1)
			gr.addVisibleElement(indexGroup, operation);
		else
			gr.addVisibleElement(operation);
		if (indexClass != -1)
			panel.addVisibleElement(indexClass, operation);
		else
			panel.addVisibleElement(operation);
		return operation;

	}

	public VisibleProperty makeVisiblePropertyAt(String label, boolean visible, ComponentType type, VisibleClass panel, int indexClass, int indexGroup) {
		NamingUtil namer = new NamingUtil();
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(propertiesGroup);
		VisibleProperty property = new VisibleProperty(label, visible, type);	
		if(type == ComponentType.TEXT_FIELD) 
			property.setDataType("String");
		property.setColumnLabel(namer.toDatabaseFormat(panel.getLabel(), label));

		if (indexGroup != -1)
			gr.addVisibleElement(indexGroup, property);
		else
			gr.addVisibleElement(property);
		if (indexClass != -1)
			panel.addVisibleElement(indexClass, property);
		else
			panel.addVisibleElement(property);
		return property;
	}

	private ComponentType getComponentType(String type){
		if(type.equalsIgnoreCase("textfield")) {
			return ComponentType.TEXT_FIELD;
		}else if(type.equalsIgnoreCase("textarea")) {
			return ComponentType.TEXT_AREA;
		}else if(type.equalsIgnoreCase("combobox")) {
			return ComponentType.COMBO_BOX;
		}else if(type.equalsIgnoreCase("radiobutton")) {
			return ComponentType.RADIO_BUTTON;
		}else if(type.equalsIgnoreCase("checkbox")) {
			return ComponentType.CHECK_BOX;
		}else if(type.equalsIgnoreCase("report") || type.equalsIgnoreCase("transaction")) {
			return ComponentType.BUTTON;
		}
		return ComponentType.TEXT_FIELD;
	}

	private void removeProperty(int classIndex){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
		gr.removeVisibleElement(visibleClass.getVisibleElementAt(classIndex));
		visibleClass.removeVisibleElement(classIndex);
	}

	private void removeOperation(int classIndex){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
		gr.removeVisibleElement(visibleClass.getVisibleElementAt(classIndex));
		visibleClass.removeVisibleElement(classIndex);
	}


	public void changeOperationStereotype(Method method, String newStereotype, int ...args){
		int classIndex = args[0];
		int groupIndex = args[1];

		ComponentType componentType = getComponentType(newStereotype);
		VisibleOperation operation = (VisibleOperation) method.getUmlOperation();
		removeOperation(classIndex);
		operation = makeVisibleOperation(operation.getLabel(), true, componentType, visibleClass, newStereotype, classIndex, groupIndex);
		method.setUmlOperation(operation);
	}

	public void changeAttributeType(Attribute attribute, String newType, int ...args){
		int classIndex = args[0];
		int groupIndex = args[1];


		ComponentType componentType = getComponentType(newType);
		VisibleProperty property = (VisibleProperty) attribute.getUmlProperty();
		removeProperty(classIndex);
		VisibleProperty prop = makeVisiblePropertyAt(property.getLabel(), true, componentType, visibleClass, classIndex, groupIndex);
		attribute.setUmlProperty(prop);
	}

	public void setOldProperty(Attribute attribute, UmlProperty oldProperty, int ...args){
		int classIndex = args[0];
		int groupIndex = args[1];

		VisibleProperty property = (VisibleProperty) attribute.getUmlProperty();
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);

		gr.removeVisibleElement(property);
		visibleClass.removeVisibleElement(property);

		visibleClass.addVisibleElement(classIndex, (VisibleProperty)oldProperty);
		gr.addVisibleElement(groupIndex, (VisibleProperty)oldProperty);
		attribute.setUmlProperty(oldProperty);
	}

	public void setOldOperation(Method method, UmlOperation oldOperation, int ...args){
		int classIndex = args[0];
		int groupIndex = args[1];

		VisibleOperation operation = (VisibleOperation) method.getUmlOperation();
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);

		gr.removeVisibleElement(operation);
		visibleClass.removeVisibleElement(operation);

		visibleClass.addVisibleElement(classIndex, (VisibleOperation)oldOperation);
		gr.addVisibleElement(groupIndex, (VisibleOperation)oldOperation);
		method.setUmlOperation(oldOperation);
	}


	public void renameAttribute(Attribute attribute, String newName) {
		VisibleProperty property = (VisibleProperty) attribute.getUmlProperty();
		property.setLabel(NameTransformUtil.transformClassName(newName));
		NamingUtil namer = new NamingUtil();
		property.setColumnLabel(namer.toDatabaseFormat(visibleClass.getLabel(),newName));
	}


	public int getClassIndexForAttribute(Attribute attribute){
		VisibleProperty prop = (VisibleProperty) attribute.getUmlProperty();
		int index = visibleClass.getVisibleElementList().indexOf(prop);
		if (index == -1)
			index = visibleClass.getVisibleElementList().size();
		return index;
	}


	public int getSourceLinkIndex(Link link){
		NextZoomElement next = nextMap.get(link.getSourceConnector());
		if (next == null)
			return visibleClass.getVisibleElementNum() - 1;
		else 
			return next.getClassIndex();
	}

	public int getDestinationLinkIndex(Link link){
		NextZoomElement zoom = zoomMap.get(link.getSourceConnector());
		if (zoom == null)
			return visibleClass.getVisibleElementNum() - 1;
		else 
			return zoom.getClassIndex();
	}


	public int getGroupIndexForAttribute(Attribute attribute){
		VisibleProperty prop = (VisibleProperty) attribute.getUmlProperty();
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
		int index = gr.getVisibleElementList().indexOf(prop);
		if (index == -1)
			index = gr.getVisibleElementList().size();
		return index;
	}

	public int getClassIndexForMethod(Method method){
		VisibleOperation operation = (VisibleOperation) method.getUmlOperation();
		int index = visibleClass.getVisibleElementList().indexOf(operation);
		if (index == -1)
			index = visibleClass.getVisibleElementList().size();
		return index;
	}

	public int getGroupIndexForMethod(Method method){
		VisibleOperation operation = (VisibleOperation) method.getUmlOperation();
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
		int index = gr.getVisibleElementList().indexOf(operation);
		//if (index == -1)
		//index = gr.getVisibleElementList().size();
		return index;
	}


	@Override
	public UmlType getUmlType() {
		return visibleClass;
	}

	/*
	 * Inicijalno veza 1..1 - *
	 * 1..1 strana ima next ka *
	 * * zooma ka 1..1
	 * @see graphedit.model.elements.GraphEditElement#linkAsSource(graphedit.model.elements.GraphEditElement)
	 */


	public void addZoomElement(String label, UIClassElement targetElement, Connector connector, int classIndex, int groupIndex){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
		if (classIndex == -1)
			classIndex = visibleClass.getVisibleElementNum();
		if (groupIndex == -1)
			groupIndex = gr.getVisibleElementsNum();

		VisibleProperty property = new VisibleProperty(label, true,  ComponentType.COMBO_BOX);
		Zoom zoom = new Zoom(property);
		zoom.setActivationPanel(visibleClass);
		zoom.setTargetPanel((VisibleClass) targetElement.getUmlType());

		gr.addVisibleElement(groupIndex, zoom);
		visibleClass.addVisibleElement(classIndex, zoom);

		NextZoomElement nextElement = new NextZoomElement(targetElement, classIndex, groupIndex, label, "*", zoom);
		zoomMap.put(connector, nextElement);
	}

	public void addNextElement(String label, UIClassElement targetElement, Connector connector, int classIndex, int groupIndex){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
		if (classIndex == -1)
			classIndex = visibleClass.getVisibleElementNum();
		if (groupIndex == -1)
			groupIndex = gr.getVisibleElementsNum();

		Next next = new Next(label);
		next.setTargetPanel((VisibleClass) targetElement.getUmlType());
		next.setActivationPanel(visibleClass);
		visibleClass.addVisibleElement(classIndex, next);
		gr.addVisibleElement(groupIndex, next);
		NextZoomElement nextElement = new NextZoomElement(targetElement, visibleClass.getVisibleElementNum()-1, gr.getVisibleElementsNum()-1, label, "1..1", next);
		nextMap.put(connector, nextElement);
	}

	public void addNextElement(NextZoomElement next, Connector connector){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
		visibleClass.addVisibleElement(next.getClassIndex(), next.getVisibleElement());
		gr.addVisibleElement(next.getGroupIndex(), next.getVisibleElement());
		nextMap.put(connector, next);
	}

	public void addZoomElement(NextZoomElement zoom, Connector connector){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
		visibleClass.addVisibleElement(zoom.getClassIndex(), zoom.getVisibleElement());
		gr.addVisibleElement(zoom.getGroupIndex(), zoom.getVisibleElement());
		zoomMap.put(connector, zoom);
	}

	public void addHierarchyElement(Connector connector, HierarchyElement hierarchyElement){
		int level = hierarchyElement.getHierarchy().getLevel();
		hierarchyMap.put(connector, hierarchyElement);
		visibleClass.addVisibleElement(hierarchyElement.getHierarchy());
		hierarchyElement.getHierarchy().getParentGroup().addVisibleElement(hierarchyElement.getHierarchy());
		hierarchyElement.getHierarchy().setLevel(level);
	}


	public void addHierarchyElement(Hierarchy hierarchy, VisibleClass targetPanel, Connector connector){
		hierarchy.setActivationPanel(visibleClass);
		visibleClass.addVisibleElement(hierarchy);
		hierarchy.setTargetPanel(targetPanel);


		if (!(targetPanel instanceof ParentChild)){
			List<Hierarchy> possibleParents = ((ParentChild)visibleClass).possibleParents(hierarchy, hierarchy.getLevel());
			if (possibleParents != null && possibleParents.size() == 1){
				hierarchy.setHierarchyParent(possibleParents.get(0));
				hierarchy.setLevel(possibleParents.get(0).getLevel() + 1);
				List<VisibleAssociationEnd> possibleEnds = ((ParentChild)visibleClass).possibleAssociationEnds(hierarchy);
				if (possibleEnds != null && possibleEnds.size() == 1)
					hierarchy.setViaAssociationEnd(possibleEnds.get(0));
			}
		}

		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
		gr.addVisibleElement(hierarchy);
		hierarchyMap.put(connector, new HierarchyElement(hierarchy, visibleClass.getVisibleElementNum()-1, gr.getVisibleElementsNum() -1));
		
		hierarchy.setParentGroup(gr);


		gr.update();
		visibleClass.update();
		hierarchy.forceUpdateComponent();
	}



	@Override
	public void link(Link link){

		UIClassElement otherElement = null;
		LinkEnd linkEnd;
		Boolean navigable = false;
		String cardinality = "";
		String roleName="";
		String label;
		Connector connector = null;

		if (this == link.getSourceConnector().getRepresentedElement()){
			otherElement = (UIClassElement) link.getDestinationConnector().getRepresentedElement();
			//gledamo da li je zoom ili next, zavisi od kardinaliteta i da li je navigabilno
			cardinality = (String) link.getProperty(LinkProperties.DESTINATION_CARDINALITY);
			navigable = (Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE);
			roleName =  (String) link.getProperty(LinkProperties.DESTINATION_ROLE);
			connector = link.getSourceConnector();
		}
		else if (this == link.getDestinationConnector().getRepresentedElement()){
			otherElement = (UIClassElement) link.getSourceConnector().getRepresentedElement();
			//gledamo da li je zoom ili next, zavisi od kardinaliteta i da li je navigabilno
			cardinality = (String) link.getProperty(LinkProperties.SOURCE_CARDINALITY);
			navigable = (Boolean) link.getProperty(LinkProperties.SOURCE_NAVIGABLE);
			roleName =  (String) link.getProperty(LinkProperties.SOURCE_ROLE);
			connector = link.getDestinationConnector();
		}

		//proveri da li je standardni ili parent-child panel
		LinkType linkType;
		if (umlClass instanceof ParentChild || otherElement.getUmlType() instanceof ParentChild)
			linkType = LinkType.HIERARCHY;
		else
			linkType = LinkType.NEXT_ZOOM;

		if (linkType == LinkType.NEXT_ZOOM){

			if (navigable){
				linkEnd = zoomOrNext(cardinality);
				label = roleName;
				if (linkEnd == LinkEnd.ZOOM){
					if (roleName.equals("")){
						label = "Zoom" + zoomCounter;
						zoomCounter++;
					}
					link.setProperty(LinkProperties.SOURCE_ROLE, label);
					addZoomElement(label, otherElement, connector, -1, -1);
				}
				else{
					if (roleName.equals("")){
						label = "Link" + linkCounter;
						linkCounter++;
					}
					link.setProperty(LinkProperties.DESTINATION_ROLE, label);
					addNextElement(label, otherElement, connector, -1, -1);
				}
			}
		}
		else{
			//ako je ovo ParentChild koji sadrzi drugi, dodaj
			if (navigable){
				System.out.println(getUmlType().name());
				Hierarchy hierarchy = new Hierarchy();
				addHierarchyElement(hierarchy, (VisibleClass) otherElement.getUmlType(), connector);
				int level = hierarchy.getLevel();
				link.setProperty(LinkProperties.STEREOTYPE, link.getProperty(LinkProperties.STEREOTYPE)+" level = " + level);
			}
		}
	}


	@SuppressWarnings("incomplete-switch")
	@Override
	public void changeLinkProperty(Link link, LinkProperties property, Object newValue) {
		if (property.toString().startsWith("DESTINATION") && !(this.equals(link.getSourceConnector().getRepresentedElement())))
			return;
		if (property.toString().startsWith("SOURCE") && !(this.equals(link.getDestinationConnector().getRepresentedElement())))
			return;
		switch (property) {
		case DESTINATION_CARDINALITY :
			changeCardinality(link, (String) newValue);
			break;
		case SOURCE_CARDINALITY : 
			changeCardinality(link, (String) newValue);
			break;
		case DESTINATION_ROLE : 
			changeRole(link, (String) newValue);
			break;
		case SOURCE_ROLE : 
			changeRole(link, (String) newValue);
			break;
		}
	}

	public AbstractLinkElement getCurrentElement(Connector connector){
		if (nextMap.containsKey(connector))
			return nextMap.get(connector);
		else if (zoomMap.containsKey(connector))
			return zoomMap.get(connector);
		return hierarchyMap.get(connector);
	}

	private void changeCardinality(Link link, String newCardinality){
		UIClassElement otherElement = null;

		Connector connector;
		LinkProperties property = LinkProperties.DESTINATION_ROLE;
		if (this == link.getSourceConnector().getRepresentedElement()){
			connector = link.getSourceConnector();
			otherElement = (UIClassElement) link.getDestinationConnector().getRepresentedElement();
		}
		else{
			property = LinkProperties.SOURCE_ROLE;
			connector = link.getDestinationConnector();
			otherElement = (UIClassElement) link.getSourceConnector().getRepresentedElement();
		}

		LinkType linkType = getLinkType((UmlClass) otherElement.getUmlType());
		if (linkType == LinkType.HIERARCHY)
			return;

		LinkEnd currenLinkEnd = LinkEnd.ZOOM;
		NextZoomElement nextZoom = zoomMap.get(connector);
		if (nextZoom == null){
			nextZoom = nextMap.get(connector);
			currenLinkEnd = LinkEnd.NEXT;
		}

		LinkEnd linkEnd = zoomOrNext(newCardinality);
		if (linkEnd != currenLinkEnd){
			String label;
			if (linkEnd == LinkEnd.NEXT){
				//izbaci zoom
				ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
				gr.removeVisibleElement(visibleClass.getVisibleElementAt(nextZoom.getClassIndex()));
				visibleClass.removeVisibleElement(nextZoom.getClassIndex());
				zoomMap.remove(connector);
				label = nextZoom.getLabel();
				if (label.toLowerCase().startsWith("zoom")){
					label = "Link" + linkCounter;
					linkCounter++;
				}
				addNextElement(label, nextZoom.getTargetElement(), connector, -1, -1);
			}
			else{
				//izbaci next
				ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
				gr.removeVisibleElement(visibleClass.getVisibleElementAt(nextZoom.getClassIndex()));
				visibleClass.removeVisibleElement(nextZoom.getClassIndex());
				nextMap.remove(connector);
				label = nextZoom.getLabel();
				if (label.toLowerCase().startsWith("link")){
					label = "Zoom" + zoomCounter;
					zoomCounter++;	
				}
				addZoomElement(label, nextZoom.getTargetElement(), connector, -1, -1);
			}
			link.setProperty(property, label);
		}
	}

	public LinkEnd zoomOrNext(String cardinality){
		if (cardinality.endsWith("1"))
			return LinkEnd.ZOOM;
		else
			return LinkEnd.NEXT;
	}

	public void changeRole(Link link, String newRole) {
		Connector connector;
		if (this == link.getSourceConnector().getRepresentedElement())
			connector = link.getSourceConnector();
		else
			connector = link.getDestinationConnector();

		NextZoomElement nextZoom = zoomMap.get(connector);
		if (nextZoom == null){
			nextZoom = nextMap.get(connector);
		}

		nextZoom.setLabel(newRole);
		visibleClass.getVisibleElementList().get(nextZoom.getClassIndex()).setLabel(newRole);
	}

	@Override
	public void setOldLink(Link link, Object...args){
		if (args[0] == null)
			return;
		boolean navigable;
		Connector connector;
		LinkProperties role;
		String cardinality;
		UIClassElement otherElement;

		if (this == link.getSourceConnector().getRepresentedElement()){
			navigable = (Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE);
			connector = link.getSourceConnector();
			role = LinkProperties.DESTINATION_ROLE;
			cardinality = (String) link.getProperty(LinkProperties.DESTINATION_CARDINALITY);
			otherElement = (UIClassElement) link.getDestinationConnector().getRepresentedElement();
		}
		else{
			navigable = (Boolean) link.getProperty(LinkProperties.SOURCE_NAVIGABLE);
			connector = link.getDestinationConnector();
			role = LinkProperties.SOURCE_ROLE;
			cardinality = (String) link.getProperty(LinkProperties.SOURCE_CARDINALITY);
			otherElement = (UIClassElement) link.getSourceConnector().getRepresentedElement();
		}
		if (!navigable)
			return;

		LinkType linkType = getLinkType((UmlClass) otherElement.getUmlType());

		if (linkType == LinkType.NEXT_ZOOM){
			NextZoomElement nextZoomElement = (NextZoomElement) args[0];
			if (nextZoomElement == null)
				return;
			LinkEnd linkEnd = zoomOrNext(cardinality);
			String label = nextZoomElement.getLabel();
			if (navigable){
				if (zoomMap.containsKey(connector)){
					//izbaci zoom
					NextZoomElement zoomElement = zoomMap.get(connector);
					if (visibleClass.getVisibleElementList().contains(zoomElement.getVisibleElement())){
						ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
						gr.removeVisibleElement(visibleClass.getVisibleElementAt(zoomElement.getClassIndex()));
						visibleClass.removeVisibleElement(zoomElement .getClassIndex());
					}
					zoomMap.remove(connector);
				}
				else if (nextMap.containsKey(connector)){
					//izbaci next
					NextZoomElement nextElement = nextMap.get(connector);
					if (visibleClass.getVisibleElementList().contains(nextElement.getVisibleElement())){
						ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
						gr.removeVisibleElement(visibleClass.getVisibleElementAt(nextElement.getClassIndex()));
						visibleClass.removeVisibleElement(nextElement.getClassIndex());
					}
					nextMap.remove(connector);

				}
				if (linkEnd == LinkEnd.NEXT)
					addNextElement(nextZoomElement, connector);
				else
					addZoomElement(nextZoomElement, connector);

				link.setProperty(role, label);
			}
		}
		else{
			HierarchyElement hierarchy = (HierarchyElement) args[0];
			addHierarchyElement(connector, hierarchy);
		}
	}


	@Override
	public void unlink(Link link) {
		//proveri da li je standardni ili parent-child panel

		UIClassElement otherElement = null;
		boolean navigable = false;
		Connector connector = null;

		if (this == link.getSourceConnector().getRepresentedElement()){
			otherElement = (UIClassElement) link.getDestinationConnector().getRepresentedElement();
			navigable = (Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE);
			connector = link.getSourceConnector();
		}
		else if (this == link.getDestinationConnector().getRepresentedElement()){
			otherElement = (UIClassElement) link.getSourceConnector().getRepresentedElement();
			navigable = (Boolean) link.getProperty(LinkProperties.SOURCE_NAVIGABLE);
			connector = link.getDestinationConnector();
		}

		if (!navigable)
			return;

		//proveri da li je standardni ili parent-child panel
		LinkType linkType;
		if (umlClass instanceof ParentChild || otherElement.getUmlType() instanceof ParentChild)
			linkType = LinkType.HIERARCHY;
		else
			linkType = LinkType.NEXT_ZOOM;

		if (linkType == LinkType.HIERARCHY){
			if (navigable){
				HierarchyElement h = hierarchyMap.get(connector);
				if (h == null)
					return;
				hierarchyMap.remove(connector);
				visibleClass.removeVisibleElement(h.getHierarchy());
				h.getHierarchy().getParentGroup().removeVisibleElement(h.getHierarchy());
			}
		}
		else{
			if (this == link.getSourceConnector().getRepresentedElement()){
				NextZoomElement next = nextMap.get(link.getSourceConnector());
				ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
				gr.removeVisibleElement(visibleClass.getVisibleElementAt(next.getClassIndex()));
				visibleClass.removeVisibleElement(next.getClassIndex());
				nextMap.remove(link.getSourceConnector());
			}
			else if (this == link.getDestinationConnector().getRepresentedElement()){
				NextZoomElement zoom = zoomMap.get(link.getDestinationConnector());
				ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
				gr.removeVisibleElement(visibleClass.getVisibleElementAt(zoom.getClassIndex()));
				visibleClass.removeVisibleElement(zoom.getClassIndex());
			}
		}
	}

	@Override
	public void addParameter(Method m, Parameter parameter) {
		if (m.getParameters().size() == 0){
			BussinessOperation operation =  (BussinessOperation) m.getUmlOperation();
			operation.setHasParametersForm(true);
		}
	}

	@Override
	public void removeParameter(Method m, Parameter parameter) {
		if (m.getParameters().size() == 1){
			BussinessOperation operation =  (BussinessOperation) m.getUmlOperation();
			operation.setHasParametersForm(false);
		}
	}

	@Override
	public void changeClassStereotype(String stereotype) {
		List<VisibleElement> visibleElements = visibleClass.getVisibleElementList();
		//Izbaci toolbar i ostale pocetne elemente

		String label = visibleClass.getLabel();
		String name = visibleClass.name();

		UmlPackage umlPackage = MainFrame.getInstance().getCurrentView().getModel().getParentPackage().getUmlPackage();
		umlPackage.removeOwnedType(visibleClass);

		if (stereotype.equals(ClassStereotypeUI.STANDARD_PANEL.toString())){
			propertiesGroup = STANDARD_PANEL_PROPERTIES;
			operationsGroup = STANDARD_PANEL_OPERATIONS;
		}
		else{
			propertiesGroup = PARENTCHILD_PANEL_PROPERTIES;
			operationsGroup = PARENTCHILD_PANEL_OPERATIONS;
		}
		formPanel(name, label, visibleElements, stereotype);
		umlPackage.addOwnedType(umlClass);
	}

	public void setOldClass(UmlClass oldClass){
		if (oldClass instanceof StandardPanel){
			propertiesGroup = STANDARD_PANEL_PROPERTIES;
			operationsGroup = STANDARD_PANEL_OPERATIONS;
		}
		else{
			propertiesGroup = PARENTCHILD_PANEL_PROPERTIES;
			operationsGroup = PARENTCHILD_PANEL_OPERATIONS;
		}
		UmlPackage umlPackage = MainFrame.getInstance().getCurrentView().getModel().getParentPackage().getUmlPackage();
		umlPackage.removeOwnedType(visibleClass);
		umlClass = oldClass;
		visibleClass = (VisibleClass) oldClass;
		umlPackage.addOwnedType(umlClass);
	}

	private LinkType getLinkType(UmlClass otherClass){
		//proveri da li je standardni ili parent-child panel
		if (umlClass instanceof ParentChild ||otherClass instanceof ParentChild)
			return LinkType.HIERARCHY;
		else
			return LinkType.NEXT_ZOOM;
	}

	public HashMap<Connector, NextZoomElement> getZoomMap() {
		return zoomMap;
	}

	public HashMap<Connector, NextZoomElement> getNextMap() {
		return nextMap;
	}

	public HashMap<Connector, HierarchyElement> getHierarchyMap() {
		return hierarchyMap;
	}

	@Override
	public UmlNamedElement getUmlElement() {
		return visibleClass;
	}

	@Override
	public void setUmlElement(UmlNamedElement umlElement) {
		setOldClass((VisibleClass)umlElement);

	}

	@Override
	public void setName(String newName) {
		visibleClass.setName(newName);
		umlClass.setName(newName);
		visibleClass.setLabel(NameTransformUtil.transformClassName(newName));

	}

	@Override
	public void renameMathod(Method method, String newName) {
		VisibleOperation operation = (VisibleOperation) method.getUmlOperation();
		operation.setLabel(NameTransformUtil.transformClassName(newName));
	}


}
