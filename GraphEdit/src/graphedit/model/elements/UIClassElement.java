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
import graphedit.util.Utility;

import java.awt.Dimension;
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


	private HashMap<Connector, NextZoomElement> zoomMap  = new HashMap<Connector, NextZoomElement>();
	private HashMap<Connector, NextZoomElement> nextMap  = new HashMap<Connector, NextZoomElement>();
	private HashMap<Connector, HierarchyElement> hierarchyMap = new HashMap<Connector, HierarchyElement>();

	private transient NamingUtil namer = new NamingUtil();

	private enum LinkEnd {ZOOM, NEXT};



	public UIClassElement(GraphElement element, ClassStereotypeUI stereotype){
		this.element = element;

		if (stereotype == ClassStereotypeUI.STANDARD_PANEL){
			this.umlClass = new StandardPanel();
			visibleClass = (VisibleClass)umlClass;
			((StandardPanel)visibleClass).setLabel((String) element.getProperty(GraphElementProperties.NAME));
			((StandardPanel) visibleClass).getPersistentClass().setName(namer.toCamelCase((String) element.getProperty(GraphElementProperties.NAME), false).trim());
		}
		else{
			this.umlClass = new ParentChild();
			visibleClass = (VisibleClass)umlClass;
			((ParentChild)visibleClass).setLabel((String) element.getProperty(GraphElementProperties.NAME));
		}
	}

	public UIClassElement(VisibleClass visibleClass, UIClassElement loadedElement){
		umlClass = visibleClass;
		this.visibleClass = visibleClass;
		initElement(loadedElement);
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
		if (visibleClass instanceof StandardPanel)
			((StandardPanel) visibleClass).getPersistentClass().setName(namer.toCamelCase((String) element.getProperty(GraphElementProperties.NAME), false).trim());

		//create properties
		for (Attribute attribute : (List<Attribute>)element.getProperty(GraphElementProperties.ATTRIBUTES))
			addAttribute(attribute, -1,-1);
		for (Method method : (List<Method>)element.getProperty(GraphElementProperties.METHODS))
			addMethod(method, -1, -1);

	}

	public void formPanel(String name, String label, List<VisibleElement> visibleElements, String stereotype){
		if (stereotype.equals(ClassStereotypeUI.STANDARD_PANEL.toString())){
			umlClass = new StandardPanel();
			((StandardPanel) umlClass).getPersistentClass().setName(namer.toCamelCase(name, false).trim());
		}
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
	public void initElement(UIClassElement loadedElement){
		Attribute attribute;
		Method method;
		element = new Class(new Point2D.Double(0,0), MainFrame.getInstance().incrementClassCounter());


		//ako je klasa sacuvana, izvuci sta moze
		if (loadedElement != null){
			//preuzmi sta treba iz njega
			GraphElement loadedClass = loadedElement.element();
			Point2D position = (Point2D) loadedClass.getProperty(GraphElementProperties.POSITION);
			Dimension dim = (Dimension) loadedClass.getProperty(GraphElementProperties.SIZE);
			String labelOld = ((VisibleClass)loadedElement.getUmlElement()).getLabel();
			String labelNew = visibleClass.getLabel();

			element.setLoaded(true);		
			element.setProperty(GraphElementProperties.POSITION, position);
			element.setLoadedDimension(dim);

			if (labelOld.equals(labelNew))
				element.setProperty(GraphElementProperties.NAME, loadedClass.getProperty(GraphElementProperties.NAME));
			else
				element.setProperty(GraphElementProperties.NAME, NameTransformUtil.labelToCamelCase(visibleClass.getLabel(),true));
		}

		else
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
				VisibleProperty visibleProperty = (VisibleProperty) type;
				Attribute loadedAttribute = savedAttribute((VisibleProperty) type, loadedElement);
				attribute = new Attribute(NameTransformUtil.labelToCamelCase(type.getLabel(), true), NameTransformUtil.transformUppercaseWithoutSpaces(type.getComponentType().toString()));
				((List<Attribute>) element.getProperty(GraphElementProperties.ATTRIBUTES)).add(attribute);
				attribute.setUmlProperty((VisibleProperty)type);
				if (loadedAttribute != null){
					attribute.setVisible(loadedAttribute.isVisible());
					if (((VisibleProperty)attribute.getUmlProperty()).getLabel().equals(
							(((VisibleProperty)loadedAttribute.getUmlProperty()).getLabel())))
						attribute.setName(loadedAttribute.getName());
				}
				if (visibleProperty.getEnumeration() != null && visibleProperty.getEnumeration().length() > 0)
					attribute.setPossibleValues(Utility.formPossibleValues(visibleProperty.getEnumeration()));
			}
			else if (type instanceof BussinessOperation){
				if (type instanceof Report)
					method = new Method(NameTransformUtil.labelToCamelCase(((BussinessOperation) type).getLabel(), false),"void","Report"); 
				else
					method = new Method(NameTransformUtil.labelToCamelCase(((BussinessOperation) type).getLabel(), false),"void", "Transaction");
				Method loadedMethod = savedMethod((BussinessOperation) type, loadedElement);

				method.setUmlOperation(( BussinessOperation)type);
				((List<Method>) element.getProperty(GraphElementProperties.METHODS)).add(method);
				if (loadedMethod != null){
					method.setParameters(loadedMethod.getParameters());
					method.setVisible(loadedMethod.isVisible());
					if (((BussinessOperation)method.getUmlOperation()).getLabel().equals(
							(((BussinessOperation)loadedMethod.getUmlOperation()).getLabel())))
						method.setName(loadedMethod.getName());
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	private Attribute savedAttribute(VisibleProperty prop, UIClassElement loadedClass){
		if (loadedClass == null)
			return null;
		List<Attribute> attributes = (List<Attribute>) loadedClass.element().getProperty(GraphElementProperties.ATTRIBUTES);
		for (Attribute a : attributes)
			if (((VisibleProperty)a.getUmlProperty()).equals(prop))
				return a;
		return  null;
	}

	@SuppressWarnings("unchecked")
	private Method savedMethod(BussinessOperation operation, UIClassElement loadedClass){
		if (loadedClass == null)
			return null;
		List<Method> methods = (List<Method>) loadedClass.element().getProperty(GraphElementProperties.METHODS);
		for (Method m : methods)
			if (((BussinessOperation)m.getUmlOperation()).equals(operation))
				return m;
		return  null;
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
		property.setName(label);
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
		property.setLabel(namer.transformClassName(newName));
		NamingUtil namer = new NamingUtil();
		property.setName(newName);
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


	public Zoom addZoomElement(String label, UIClassElement targetElement, Connector connector, int classIndex, int groupIndex){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
		if (classIndex == -1)
			classIndex = visibleClass.getVisibleElementNum();
		if (groupIndex == -1)
			groupIndex = gr.getVisibleElementsNum();

		VisibleProperty property = new VisibleProperty(namer.transformClassName(label), true,  ComponentType.COMBO_BOX);
		Zoom zoom = new Zoom(property);
		zoom.setActivationPanel(visibleClass);
		zoom.setTargetPanel((VisibleClass) targetElement.getUmlType());

		gr.addVisibleElement(groupIndex, zoom);
		visibleClass.addVisibleElement(classIndex, zoom);

		NextZoomElement nextElement = new NextZoomElement(targetElement, classIndex, groupIndex, label, "*", zoom);
		zoomMap.put(connector, nextElement);

		return zoom;
	}

	public Next addNextElement(String label, UIClassElement targetElement, Connector connector, int classIndex, int groupIndex){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
		if (classIndex == -1)
			classIndex = visibleClass.getVisibleElementNum();
		if (groupIndex == -1)
			groupIndex = gr.getVisibleElementsNum();

		Next next = new Next(namer.transformClassName(label));
		next.setTargetPanel((VisibleClass) targetElement.getUmlType());
		next.setActivationPanel(visibleClass);
		visibleClass.addVisibleElement(classIndex, next);
		gr.addVisibleElement(groupIndex, next);
		NextZoomElement nextElement = new NextZoomElement(targetElement, visibleClass.getVisibleElementNum()-1, gr.getVisibleElementsNum()-1, label, "1..1", next);
		nextMap.put(connector, nextElement);
		return next;
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
	public void link(Link link, Object...args){

		UIClassElement otherElement = null;
		LinkEnd linkEnd;
		Boolean navigable = false;
		String cardinality = "";
		String roleName="";
		String label;
		Connector connector = null;
		Connector otherConnector = null;
		Boolean source = (Boolean) args[0];

		if (source){
			otherElement = (UIClassElement) link.getDestinationConnector().getRepresentedElement();
			//gledamo da li je zoom ili next, zavisi od kardinaliteta i da li je navigabilno
			cardinality = (String) link.getProperty(LinkProperties.DESTINATION_CARDINALITY);
			navigable = (Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE);
			roleName =  (String) link.getProperty(LinkProperties.DESTINATION_ROLE);
			connector = link.getSourceConnector();
			otherConnector = link.getDestinationConnector();
		}
		else {
			otherElement = (UIClassElement) link.getSourceConnector().getRepresentedElement();
			//gledamo da li je zoom ili next, zavisi od kardinaliteta i da li je navigabilno
			cardinality = (String) link.getProperty(LinkProperties.SOURCE_CARDINALITY);
			navigable = (Boolean) link.getProperty(LinkProperties.SOURCE_NAVIGABLE);
			roleName =  (String) link.getProperty(LinkProperties.SOURCE_ROLE);
			connector = link.getDestinationConnector();
			otherConnector = link.getSourceConnector();
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
				if (label.equals("")){
					String otherName = (String) otherElement.element().getProperty(GraphElementProperties.NAME);
					label = namer.lowerFirstLetter(otherName);
				}
				if (linkEnd == LinkEnd.ZOOM){
					link.setProperty(LinkProperties.SOURCE_ROLE, label);
					Zoom zoom = addZoomElement(label, otherElement, connector, -1, -1);
					setOpposite(zoom, otherConnector, otherElement);
				}
				else{
					link.setProperty(LinkProperties.DESTINATION_ROLE, label);
					Next next = addNextElement(label, otherElement, connector, -1, -1);
					setOpposite(next, otherConnector, otherElement);
				}
			}
		}
		else{
			//ako je ovo ParentChild koji sadrzi drugi, dodaj
			if (navigable){
				Hierarchy hierarchy = new Hierarchy();
				addHierarchyElement(hierarchy, (VisibleClass) otherElement.getUmlType(), connector);
				int level = hierarchy.getLevel();
				link.setProperty(LinkProperties.STEREOTYPE, link.getProperty(LinkProperties.STEREOTYPE)+" level = " + level);
			}
		}
	}


	@Override
	public void changeLinkProperty(Link link, LinkProperties property, Object newValue, Object...args) {

		boolean source = (Boolean)args[0];

		if (property.toString().startsWith("DESTINATION") && !source)
			return;
		if (property.toString().startsWith("SOURCE") && source)
			return;

		switch (property) {
		case DESTINATION_CARDINALITY :
			changeCardinality(link, (String) newValue, property, source);
			break;
		case SOURCE_CARDINALITY : 
			changeCardinality(link, (String) newValue, property, source);
			break;
		case DESTINATION_ROLE : 
			changeRole(link, (String) newValue, source);
			break;
		case SOURCE_ROLE : 
			changeRole(link, (String) newValue, source);
			break;
		case DESTINATION_NAVIGABLE :
			changeNavigable(link, property, (Boolean) newValue, source);
			break;
		case SOURCE_NAVIGABLE :
			changeNavigable(link, property, (Boolean) newValue, source);
			break;
		default:
			break;
		}

	}

	private void changeNavigable(Link link, LinkProperties property, boolean navigable, boolean sourceOrDestination){

		if (navigable == (Boolean) link.getProperty(property))
			return;

		UIClassElement otherElement = null;

		Connector connector, otherConnector;


		if (!sourceOrDestination && this == link.getDestinationConnector().getRepresentedElement() && property == LinkProperties.DESTINATION_NAVIGABLE)
			return;
		if (sourceOrDestination && this == link.getSourceConnector().getRepresentedElement() && property == LinkProperties.SOURCE_NAVIGABLE)
			return;

		boolean source;

		if (property == LinkProperties.DESTINATION_NAVIGABLE){
			connector = link.getSourceConnector();
			otherConnector = link.getDestinationConnector();

			otherElement = (UIClassElement) link.getDestinationConnector().getRepresentedElement();
			source = false;
		}
		else{
			connector = link.getDestinationConnector();
			otherConnector = link.getSourceConnector();
			otherElement = (UIClassElement) link.getSourceConnector().getRepresentedElement();
			source = true;
		}
		NextZoomElement nextEl = nextMap.get(connector);

		if (!navigable){
			//izbaci next
			ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
			gr.removeVisibleElement(visibleClass.getVisibleElementAt(nextEl.getClassIndex()));
			visibleClass.removeVisibleElement(nextEl.getClassIndex());

			Next next = (Next) nextMap.get(connector).getVisibleElement();
			if (next.opposite() != null)
				next.opposite().setOpposite(null);

			nextMap.remove(connector);
			if (source)
				link.setProperty(LinkProperties.SOURCE_ROLE, "");
			else
				link.setProperty(LinkProperties.DESTINATION_ROLE, "");

		}
		else{
			//dodaj next
			String otherName = (String) otherElement.element().getProperty(GraphElementProperties.NAME);
			String label = namer.lowerFirstLetter(otherName);
			Next next = addNextElement(label, otherElement, connector, -1, -1);
			setOpposite(next, otherConnector, otherElement);
			if (source)
				link.setProperty(LinkProperties.SOURCE_ROLE, label);
			else
				link.setProperty(LinkProperties.DESTINATION_ROLE, label);

		}
	}

	public AbstractLinkElement getCurrentElement(Connector connector){
		if (nextMap.containsKey(connector))
			return nextMap.get(connector);
		else if (zoomMap.containsKey(connector))
			return zoomMap.get(connector);
		return hierarchyMap.get(connector);
	}

	private void changeCardinality(Link link, String newCardinality, LinkProperties cardProperty, boolean source){


		UIClassElement otherElement = null;

		Connector connector, otherConnector;
		LinkProperties property = LinkProperties.DESTINATION_ROLE;
		boolean otherNavigable;

		if (source){
			connector = link.getSourceConnector();
			if (cardProperty == LinkProperties.SOURCE_CARDINALITY)
				return;
			otherConnector = link.getDestinationConnector();
			otherElement = (UIClassElement) link.getDestinationConnector().getRepresentedElement();
			otherNavigable = (Boolean) link.getProperty(LinkProperties.SOURCE_NAVIGABLE);
		}
		else{
			property = LinkProperties.SOURCE_ROLE;
			connector = link.getDestinationConnector();
			if (cardProperty == LinkProperties.DESTINATION_CARDINALITY)
				return;
			otherConnector = link.getSourceConnector();
			otherElement = (UIClassElement) link.getSourceConnector().getRepresentedElement();
			otherNavigable = (Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE);
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
			String label = nextZoom.getLabel();
			if (linkEnd == LinkEnd.NEXT){
				//izbaci zoom
				ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
				gr.removeVisibleElement(visibleClass.getVisibleElementAt(nextZoom.getClassIndex()));
				visibleClass.removeVisibleElement(nextZoom.getClassIndex());
				zoomMap.remove(connector);

				//				label = nextZoom.getLabel();
				//				if (label.toLowerCase().startsWith("zoom")){
				//					label = "Link" + linkCounter;
				//					linkCounter++;
				//				}
				if (otherNavigable){
					Next next = addNextElement(label, otherElement, connector, -1, -1);
					setOpposite(next, otherConnector, otherElement);
					link.setProperty(property, label);
				}
				else{
					link.setProperty(property, "");
				}
			}
			else{
				//izbaci next
				if (nextZoom != null){
					ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
					gr.removeVisibleElement(visibleClass.getVisibleElementAt(nextZoom.getClassIndex()));
					visibleClass.removeVisibleElement(nextZoom.getClassIndex());
					nextMap.remove(connector);
					//	label = nextZoom.getLabel();
				}
				//				if (label == null || label.toLowerCase().startsWith("link")){
				//					label = "Zoom" + zoomCounter;
				//					zoomCounter++;	
				//				}
				Zoom zoom = addZoomElement(label, otherElement, connector, -1, -1);
				setOpposite(zoom, otherConnector, otherElement);
				link.setProperty(property, label);
			}

		}
	}

	public LinkEnd zoomOrNext(String cardinality){
		if (cardinality.endsWith("1"))
			return LinkEnd.ZOOM;
		else
			return LinkEnd.NEXT;
	}

	public void changeRole(Link link, String newRole, boolean source) {

		Connector connector;
		if (source)
			connector = link.getSourceConnector();
		else
			connector = link.getDestinationConnector();

		NextZoomElement nextZoom = zoomMap.get(connector);
		if (nextZoom == null){
			nextZoom = nextMap.get(connector);
		}

		nextZoom.setLabel(newRole);
		newRole = namer.transformClassName(newRole);
		visibleClass.getVisibleElementList().get(nextZoom.getClassIndex()).setLabel(newRole);
	}

	@Override
	public void setOldLink(Link link, Object...args){

		Connector connector, otherConnector;
		LinkProperties role;
		String cardinality;
		UIClassElement otherElement;
		Boolean source = (Boolean) args[1];

		if (source){
			connector = link.getSourceConnector();
			otherConnector = link.getDestinationConnector();
			role = LinkProperties.DESTINATION_ROLE;
			cardinality = (String) link.getProperty(LinkProperties.DESTINATION_CARDINALITY);
			otherElement = (UIClassElement) link.getDestinationConnector().getRepresentedElement();
		}
		else{
			connector = link.getDestinationConnector();
			otherConnector = link.getSourceConnector();
			role = LinkProperties.SOURCE_ROLE;
			cardinality = (String) link.getProperty(LinkProperties.SOURCE_CARDINALITY);
			otherElement = (UIClassElement) link.getSourceConnector().getRepresentedElement();
		}

		LinkType linkType = getLinkType((UmlClass) otherElement.getUmlType());

		if (linkType == LinkType.NEXT_ZOOM){
			NextZoomElement nextZoomElement = (NextZoomElement) args[0];
			LinkEnd linkEnd = zoomOrNext(cardinality);
			String label = "";
			if (nextZoomElement != null)
				label = nextZoomElement.getLabel();
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

			if (nextZoomElement != null){
				if (linkEnd == LinkEnd.NEXT){
					addNextElement(nextZoomElement, connector);
					if (nextZoomElement != null)
						setOpposite((VisibleAssociationEnd) nextZoomElement.getVisibleElement(), otherConnector, otherElement);

				}
				else{
					addZoomElement(nextZoomElement, connector);
					if (nextZoomElement != null)
						setOpposite((VisibleAssociationEnd) nextZoomElement.getVisibleElement(), otherConnector, otherElement);
				}
			}
			link.setProperty(role, label);
		}
		else{
			HierarchyElement hierarchy = (HierarchyElement) args[0];
			if (hierarchy == null)
				return;
			addHierarchyElement(connector, hierarchy);
		}
	}


	private void setOpposite(VisibleAssociationEnd  nextZoom, Connector otherConnector, UIClassElement otherElement){
		NextZoomElement other = otherElement.getNextMap().get(otherConnector);
		if (other == null)
			other = otherElement.getZoomMap().get(otherConnector);
		if (other != null){
			VisibleAssociationEnd otherEnd = (VisibleAssociationEnd) other.getVisibleElement();
			otherEnd.setOpposite(nextZoom);
			nextZoom.setOpposite(otherEnd);
		}
	}
	@Override
	public void unlink(Link link, Object...args) {
		//proveri da li je standardni ili parent-child panel

		UIClassElement otherElement = null;
		boolean navigable = false;
		Connector connector = null;
		Boolean source = (Boolean) args[0];

		if (source){
			otherElement = (UIClassElement) link.getDestinationConnector().getRepresentedElement();
			navigable = (Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE);
			connector = link.getSourceConnector();
		}
		else{
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
			if (source){
				NextZoomElement next = nextMap.get(link.getSourceConnector());
				ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
				gr.removeVisibleElement(visibleClass.getVisibleElementAt(next.getClassIndex()));
				visibleClass.removeVisibleElement(next.getClassIndex());
				nextMap.remove(link.getSourceConnector());
			}
			else{
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

	//	/**
	//	 * Return name of the association role and updates association map
	//	 * @param otherName
	//	 * @return
	//	 */
	//	private String getAssociationNameFormMap(String otherName){
	//		Integer count = associationNameMap.get(otherName);
	//		if (count == null)
	//			count = 0;
	//		associationNameMap.put(otherName, ++count);
	//		return otherName + "_" + count;
	//	}

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
		visibleClass.setLabel(namer.transformClassName(newName));
		if (visibleClass instanceof StandardPanel){
			if (namer == null)
				namer = new NamingUtil();
			((StandardPanel) visibleClass).getPersistentClass().setName(namer.toCamelCase(newName, false).trim());
		}

	}

	@Override
	public void renameMathod(Method method, String newName) {
		VisibleOperation operation = (VisibleOperation) method.getUmlOperation();
		operation.setLabel(namer.transformClassName(newName));
	}

	/**
	 * args[0] class index
	 * args[1] group index
	 * args[2] type - 1 for attributes, 2 for methods
	 */
	@Override
	public void moveElementUp(int... args) {
		int  classIndex = args[0];
		int groupIndex = args[1];
		int type = args[2];

		ElementsGroup gr;
		if (type == 1)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
		else if (type == 2)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
		else 
			return;

		//izbaci oba i dodaj na suprotne pozicije
		VisibleElement thisProp = (VisibleElement) gr.getVisibleElementAt(groupIndex);
		VisibleElement otherProp = (VisibleElement) gr.getVisibleElementAt(groupIndex - 1);

		swapProperties(thisProp, otherProp, groupIndex - 1, groupIndex, classIndex - 1, classIndex, gr);

	}

	@Override
	public void moveElementDown(int... args) {
		int  classIndex = args[0];
		int groupIndex = args[1];
		int type = args[2];

		ElementsGroup gr;
		if (type == 1)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(propertiesGroup);
		else if (type == 2)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(operationsGroup);
		else 
			return;

		//izbaci oba i dodaj na suprotne pozicije
		VisibleElement thisProp = (VisibleElement) gr.getVisibleElementAt(groupIndex);
		VisibleElement otherProp = (VisibleElement) gr.getVisibleElementAt(groupIndex + 1);

		swapProperties(thisProp, otherProp, groupIndex + 1, groupIndex, classIndex + 1, classIndex, gr);

	}

	private void swapProperties(VisibleElement p1, VisibleElement p2, int firstIndexGr, int secondIndexGr,
			int firstIndexCl, int secondIndexCl, ElementsGroup gr){


		gr.removeVisibleElement(p1);
		gr.removeVisibleElement(p2);
		if (firstIndexGr < secondIndexGr){
			gr.addVisibleElement(firstIndexGr, p1);
			gr.addVisibleElement(secondIndexGr, p2);
		}
		else{
			gr.addVisibleElement(secondIndexGr, p2);
			gr.addVisibleElement(firstIndexGr, p1);
		}

		visibleClass.removeVisibleElement(p1);
		visibleClass.removeVisibleElement(p2);
		if (firstIndexCl < secondIndexCl){
			visibleClass.addVisibleElement(firstIndexCl, p1);
			visibleClass.addVisibleElement(secondIndexCl, p2);
		}
		else{
			visibleClass.addVisibleElement(secondIndexCl, p2);
			visibleClass.addVisibleElement(firstIndexCl, p1);
		}
	}



}
