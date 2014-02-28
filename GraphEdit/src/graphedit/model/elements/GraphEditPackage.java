package graphedit.model.elements;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.components.AssociationLink;
import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Package;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.interfaces.GraphEditTreeNode;
import graphedit.model.properties.Properties;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.model.properties.PropertyEnums.PackageProperties;
import graphedit.util.NameTransformUtil;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlNamedElement;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;
public class GraphEditPackage extends Observable implements GraphEditElement, GraphEditTreeNode, Serializable {

	private static final long serialVersionUID = 1L;

	private Properties<PackageProperties> properties = new Properties<PackageProperties>();

	private GraphEditPackage parentPackage;

	//contained uml package
	private UmlPackage umlPackage;

	//Diagram representing contents of the package
	private GraphEditModel diagram;

	//GraphElement
	private Package packageElement;

	public GraphEditPackage(UmlPackage umlPackage){
		this.umlPackage = umlPackage;
		String name = umlPackage.name();
		if (umlPackage instanceof BussinesSubsystem)
			name = ((BussinesSubsystem)umlPackage).getLabel();
		properties.set(PackageProperties.NAME, name);
		diagram = new GraphEditModel(name);
		diagram.setParentPackage(this);
	}

	public GraphEditPackage(UmlPackage umlPackage, GraphEditPackage parent){
		this.umlPackage = umlPackage;
		String name = umlPackage.name();
		if (umlPackage instanceof BussinesSubsystem)
			name = ((BussinesSubsystem)umlPackage).getLabel();
		properties.set(PackageProperties.NAME, name);
		diagram = new GraphEditModel(name);
		diagram.setParentPackage(this);

		//nije na vrhu hijerarhije
		if (parent != null){
			packageElement = new Package(new Point2D.Double(0,0), MainFrame.getInstance().incrementPackageCounter());
			packageElement.setProperty(GraphElementProperties.NAME, NameTransformUtil.labelToCamelCase(((BussinesSubsystem)umlPackage).getLabel(),true));
			packageElement.setRepresentedElement(this);
			packageElement.setRepresentedElement(this);
			this.parentPackage = parent;
			parent.getDiagram().addGraphEditPackage(packageElement);
		}
		//ako package ima nested
		if (umlPackage.nestedPackage().size() > 0){

			for (UmlPackage innerUml : umlPackage.nestedPackage()){
				new GraphEditPackage(innerUml, this);

			}
		}
		if (umlPackage.ownedType().size()>0){
			UIClassElement classElement;

			HashMap<VisibleClass, UIClassElement> classes = new HashMap<VisibleClass, UIClassElement>();
			int xPosition = 0;
			for (UmlType type : umlPackage.ownedType())
				if (type instanceof VisibleClass){

					VisibleClass visibleClass = (VisibleClass)type;
					classElement = new UIClassElement(visibleClass,new Point2D.Double(xPosition, xPosition));
					classes.put(visibleClass, classElement);

					xPosition = 200;

					diagram.addDiagramElement(classElement.element());

				}

			for (VisibleClass visibleClass : classes.keySet()){
				for (Zoom zoom : visibleClass.containedZooms()){

					//onaj koji sadrzi zoom je destination
					//onaj koji sadrzi next je source


					UIClassElement targetElement = classes.get(zoom.getTargetPanel());
					if (targetElement == null)
						continue;
					UIClassElement thisElement = classes.get(visibleClass);

					LinkableElement destinationElement = (LinkableElement) thisElement.element();
					LinkableElement sourceElement = (LinkableElement) targetElement.element();

					Point  p1 = new Point(0,0);
					Point p2 = new Point(200,200);
					Connector c1 = new Connector(p1, sourceElement);
					Connector c2 = new Connector(p2, destinationElement);
					c2.setRepresentedElement(thisElement);
					c1.setRepresentedElement(targetElement);

					int classIndex = visibleClass.getVisibleElementList().indexOf(zoom);
					int groupIndex = ((ElementsGroup) visibleClass.getVisibleElementList().get(UIClassElement.STANDARD_PANEL_PROPERTIES)).getVisibleElementList().indexOf(zoom);
					NextZoomElement zoomElement = new NextZoomElement(targetElement, classIndex, groupIndex,zoom.getLabel(),"1..1",zoom);
					thisElement.getZoomMap().put(c2, zoomElement);


					Next next = null;
					if (zoom.opposite() != null && zoom.opposite() instanceof Next){
						
						next = (Next) zoom.opposite();
						UIClassElement targetElement2 = classes.get(visibleClass);
						UIClassElement thisElement2 = classes.get(zoom.getTargetPanel());
						classIndex = zoom.getTargetPanel().getVisibleElementList().indexOf(next);
						groupIndex = ((ElementsGroup) zoom.getTargetPanel().getVisibleElementList().get(UIClassElement.STANDARD_PANEL_OPERATIONS)).getVisibleElementList().indexOf(next);
						NextZoomElement nextElement = new NextZoomElement(targetElement2, classIndex, groupIndex,next.getLabel(),"*",next);
						thisElement2.getNextMap().put(c1, nextElement);

					}

					ArrayList<LinkNode> nodes = new ArrayList<LinkNode>();
					nodes.add(c1);
					nodes.add(c2);
					Link link;
					if (next != null)
						link = new AssociationLink(nodes, "1..1", "*", zoom.getLabel(),next.getLabel(),"",true,true, MainFrame.getInstance().incrementLinkCounter());
					else
						link = new AssociationLink(nodes, "1..1", "*", zoom.getLabel(),"","",true,false, MainFrame.getInstance().incrementLinkCounter());
					
					link.setProperty(LinkProperties.STEREOTYPE, "zoom");
					c1.setLink(link);
					c2.setLink(link);


					sourceElement.addConnectors(link.getSourceConnector());
					destinationElement.addConnectors(link.getDestinationConnector());


					diagram.insertIntoElementByConnectorStructure(link.getSourceConnector(), sourceElement);
					diagram.insertIntoElementByConnectorStructure(link.getDestinationConnector(), destinationElement);
					diagram.addLink(link);

				}
				
				for (Hierarchy hierarchy : visibleClass.containedHierarchies()){

					UIClassElement targetElement = classes.get(hierarchy.getTargetPanel());
					if (targetElement == null)
						continue;

					UIClassElement thisElement = classes.get(visibleClass);

					LinkableElement sourceElement = (LinkableElement) thisElement.element();
					LinkableElement destinationElement = (LinkableElement) targetElement.element();

					Point  p1 = new Point(0,0);
					Point p2 = new Point(200,200);
					Connector c1 = new Connector(p1, sourceElement);
					Connector c2 = new Connector(p2, destinationElement);
					c2.setRepresentedElement(thisElement);
					c1.setRepresentedElement(targetElement);


					ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(1);
					int classIndex = visibleClass.getVisibleElementList().indexOf(hierarchy);
					int groupIndex = gr.getVisibleElementList().indexOf(hierarchy);
					thisElement.getHierarchyMap().put(c1, new HierarchyElement(hierarchy, classIndex, groupIndex));


					ArrayList<LinkNode> nodes = new ArrayList<LinkNode>();
					nodes.add(c1);
					nodes.add(c2);
					Link link = new AssociationLink(nodes, "1..1", "1..1", hierarchy.getLabel(),hierarchy.getLabel(),"",false,true, MainFrame.getInstance().incrementLinkCounter());
					link.setProperty(LinkProperties.STEREOTYPE, "Hierarchy level = " + hierarchy.getLevel());
					c1.setLink(link);
					c2.setLink(link);


					sourceElement.addConnectors(link.getSourceConnector());
					destinationElement.addConnectors(link.getDestinationConnector());


					diagram.insertIntoElementByConnectorStructure(link.getSourceConnector(), sourceElement);
					diagram.insertIntoElementByConnectorStructure(link.getDestinationConnector(), destinationElement);
					diagram.addLink(link);
				}
			}
		}
	}


	public void formPanels(){
		if (MainFrame.getInstance().getAppMode() != ApplicationMode.USER_INTERFACE)
			return;
		for (GraphElement diagramElement : diagram.getDiagramElements()){
			UIClassElement clazz = (UIClassElement) diagramElement.getRepresentedElement();
			clazz.formPanel();
			umlPackage.addOwnedType(clazz.getUmlType());
		}
		for (Package contained : diagram.getContainedPackages()){
			contained.getHierarchyPackage().formPanels();
		}
	}


	public Object getProperty(PackageProperties key) {
		return properties.get(key);
	}

	public Object setProperty(PackageProperties key, Object value) {
		Object result = properties.set(key, value);
		fireChanges();
		return result;
	}

	//**********************************************************************
	/*
	 * GraphEditTreeNode's methods
	 * Show inner packages first, then other diagram elements
	 */

	public void fireChanges(){
		// fire-uj izmene
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public Object getNodeAt(int index) {

		if (index < diagram.getContainedPackages().size())
			return ((Package)diagram.getContainedPackages().get(index)).getHierarchyPackage();

		else if (index < diagram.getContainedPackages().size() + diagram.getDiagramElements().size())
			return (((GraphElement) diagram.getDiagramElements().get(index - diagram.getContainedPackages().size())));
		else 
			return diagram.getLinks().get(index - diagram.getContainedPackages().size() - diagram.getDiagramElements().size());
	}

	@Override
	public int getNodeCount() {
		return diagram.getDiagramElements().size() + diagram.getContainedPackages().size() + diagram.getLinks().size();
	}

	@Override
	public int getNodeIndex(Object node){
		if (node instanceof GraphEditPackage){
			if (diagram.getContainedPackages().contains(node))
				return diagram.getContainedPackages().indexOf(node);
		}
		else if (node instanceof Link) {
			if (diagram.getLinks().contains(node)) 
				return diagram.getLinks().indexOf(node);
		}
		else if (node instanceof GraphElement){
			if (diagram.getDiagramElements().contains(node))
				return diagram.getDiagramElements().indexOf(node);
		}
		return -1;
	}



	@Override
	public String toString() {
		return (String) properties.get(PackageProperties.NAME);
	}

	public GraphEditPackage getParentPackage() {
		return parentPackage;
	}

	public void setParentPackage(GraphEditPackage parentPackage) {
		this.parentPackage = parentPackage;
	}


	public UmlPackage getUmlPackage() {
		return umlPackage;
	}

	public void setUmlPackage(UmlPackage umlPackage) {
		this.umlPackage = umlPackage;
	}


	public GraphEditModel getDiagram() {
		return diagram;
	}

	public void setDiagram(GraphEditModel diagram) {
		this.diagram = diagram;
	}

	public Package getPackageElement() {
		return packageElement;
	}

	public void setPackageElement(Package packageElement) {
		this.packageElement = packageElement;
	}


	@Override
	public GraphElement element() {
		return packageElement;
	}

	@Override
	public void setElement(GraphElement element) {
		packageElement = (Package)element;

	}


	@Override
	public void unlink(Link link) {
		// TODO Auto-generated method stub

	}

	@Override
	public void link(Link link) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeLinkProperty(Link link, LinkProperties property, Object newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOldLink(Link link, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	public UmlNamedElement getUmlElement() {
		return umlPackage;
	}

	@Override
	public void setUmlElement(UmlNamedElement umlElement) {
		umlPackage = (UmlPackage)umlElement;

	}

	@Override
	public void setName(String newName) {
		umlPackage.setName(newName);
		if (umlPackage instanceof BussinesSubsystem)
			((BussinesSubsystem)umlPackage).setLabel(newName);

	}


}