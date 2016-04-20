package graphedit.model.elements;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.layout.LayoutStrategy;
import graphedit.layout.LayoutUtil;
import graphedit.model.GraphEditWorkspace;
import graphedit.model.components.AggregationLink;
import graphedit.model.components.AssociationLink;
import graphedit.model.components.Class;
import graphedit.model.components.CompositionLink;
import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Package;
import graphedit.model.components.shortcuts.ClassShortcut;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.interfaces.GraphEditTreeNode;
import graphedit.model.properties.Properties;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.model.properties.PropertyEnums.PackageProperties;
import graphedit.util.WorkspaceUtility;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import kroki.commons.camelcase.NamingUtil;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.VisibleClassUtil;
import kroki.uml_core_basic.UmlNamedElement;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

public class GraphEditPackage extends Observable implements GraphEditElement, GraphEditTreeNode, Serializable {

	private static final long serialVersionUID = 1L;

	private Properties<PackageProperties> properties = new Properties<PackageProperties>();

	private GraphEditPackage parentPackage;

	/**
	 * contained uml package
	 */
	private UmlPackage umlPackage;

	/**
	 * Diagram representing contents of the package
	 */
	private GraphEditModel diagram;

	/**
	 * GraphElement
	 */
	private Package packageElement;

	/**
	 * Where the project is saved
	 */
	private File file;

	private Map<VisibleClass, UIClassElement> classElementsByVisibleClassesMap = new HashMap<VisibleClass, UIClassElement>();

	private Map<VisibleClass, UIClassElement> subClassesMap = new HashMap<VisibleClass, UIClassElement>();

	private List<GraphEditPackage> subPackages = new ArrayList<GraphEditPackage>();

	private boolean changed = false;

	private boolean loaded;

	private transient NamingUtil namer = new NamingUtil();


	public GraphEditPackage(UmlPackage umlPackage){
		this.umlPackage = umlPackage;
		String name = umlPackage.name();
		if (umlPackage instanceof BussinesSubsystem)
			name = namer.transformLabelToJavaName(((BussinesSubsystem)umlPackage).getLabel());
		properties.set(PackageProperties.NAME, name);
		diagram = new GraphEditModel(name);
		diagram.setParentPackage(this);
	}

	public GraphEditPackage(UmlPackage umlPackage, GraphEditPackage parent, GraphEditPackage loadedElement){
		this.umlPackage = umlPackage;
		String name = umlPackage.name();
		if (umlPackage instanceof BussinesSubsystem)
			name = namer.transformLabelToJavaName(((BussinesSubsystem)umlPackage).getLabel());
		properties.set(PackageProperties.NAME, name);
		diagram = new GraphEditModel(name);
		diagram.setParentPackage(this);

		if (loadedElement != null)
			if (loadedElement.getClassElementsByVisibleClassesMap().size() > 0 || loadedElement.getSubPackages().size() > 0){
				loaded = true;
				diagram.setLayout(loadedElement.getDiagram().isLayout());
			}

		//nije na vrhu hijerarhije
		if (parent != null){
			packageElement = new Package(new Point2D.Double(0,0), MainFrame.getInstance().incrementPackageCounter());
			if (loadedElement != null){
				Package loadedPackage = loadedElement.getPackageElement();
				//preuzmi sta treba iz njega
				Point2D position = (Point2D) loadedPackage.getProperty(GraphElementProperties.POSITION);
				Dimension dim = (Dimension) loadedPackage.getProperty(GraphElementProperties.SIZE);
				String labelOld = ((BussinesSubsystem)loadedElement.getUmlPackage()).getLabel();
				String labelNew = ((BussinesSubsystem)umlPackage).getLabel();
				packageElement.setLoaded(true);
				packageElement.setProperty(GraphElementProperties.POSITION, position);
				packageElement.setProperty(GraphElementProperties.SIZE, dim);
				if (labelOld.equals(labelNew))
					packageElement.setProperty(GraphElementProperties.NAME, loadedPackage.getProperty(GraphElementProperties.NAME));
				else
					packageElement.setProperty(GraphElementProperties.NAME, namer.transformLabelToJavaName(((BussinesSubsystem)umlPackage).getLabel()));
			}

			else
				packageElement.setProperty(GraphElementProperties.NAME, namer.transformLabelToJavaName(((BussinesSubsystem)umlPackage).getLabel()));

			packageElement.setRepresentedElement(this);
			packageElement.setRepresentedElement(this);
			this.parentPackage = parent;
			parent.getDiagram().addGraphEditPackage(packageElement);
		}
		//ako package ima nested
		if (umlPackage.nestedPackage().size() > 0){

			GraphEditPackage subPackage;

			for (UmlPackage innerUml : umlPackage.nestedPackage()){
				subPackage = new GraphEditPackage(innerUml, this, savedPackage(innerUml, loadedElement));
				subPackages.add(subPackage);
				subPackages.addAll(subPackage.getSubPackages());

				subClassesMap.putAll(subPackage.getSubClassesMap());


			}
		}
		generateClasses(loadedElement);
	}

	private GraphEditPackage savedPackage(UmlPackage testPackage, GraphEditPackage loadedPackage){
		if (loadedPackage == null)
			return null;
		if (loadedPackage.getUmlPackage().equals(testPackage))
			return loadedPackage;
		for (GraphEditPackage pack : loadedPackage.subPackages){
			if (pack.getUmlPackage().equals(testPackage))
				return pack;
		}
		return null;
	}

	private UIClassElement savedClass(VisibleClass testClass, GraphEditPackage loadedPackage){
		if (loadedPackage == null)
			return null;
		for (UIClassElement clazz : loadedPackage.getClassElementsByVisibleClassesMap().values()){
			if (clazz.getUmlElement().equals(testClass))
				return clazz;
		}
		for (GraphEditPackage pack : loadedPackage.getSubPackages()){
			UIClassElement found = savedClass(testClass, pack);
			if (found != null)
				return found;
		}
		return null;
	}

	private void generateClasses(GraphEditPackage loadedElement){

		if (umlPackage.ownedType().size()>0){

			UIClassElement classElement;

			for (UmlType type : umlPackage.ownedType())
				if (type instanceof VisibleClass){

					VisibleClass visibleClass = (VisibleClass)type;
					UIClassElement loadedClass = savedClass(visibleClass, loadedElement);
					classElement = new UIClassElement(visibleClass, loadedClass);
					classElementsByVisibleClassesMap.put(visibleClass, classElement);

					diagram.addDiagramElement((Class)classElement.element());

				}
		}
		subClassesMap.putAll(classElementsByVisibleClassesMap);
	}

	private UIClassElement shortcutElement(Shortcut shortcut){
		GraphEditPackage topPackage = WorkspaceUtility.getTopPackage(this);

		VisibleClass loadedClass = (VisibleClass)shortcut.shortcutTo().getRepresentedElement().getUmlElement();
		for (VisibleClass visibleClass : topPackage.getSubClassesMap().keySet())
			if (visibleClass.equals(loadedClass))
				return topPackage.getSubClassesMap().get(visibleClass);
		return null;
	}

	public void generateShortcuts(GraphEditPackage loadedElement){
		if (loadedElement == null)
			return;
		GraphEditPackage loadedPackage = savedPackage(umlPackage, loadedElement);
		for (GraphElement diagramElement : loadedPackage.getDiagram().getDiagramElements()){
			if (diagramElement instanceof Shortcut){
				Shortcut shortcut = (Shortcut) diagramElement;
				UIClassElement shortcutTo = shortcutElement(shortcut);
				if (shortcutTo == null)
					continue;
				if (shortcut.shortcutTo() instanceof Class){
					ClassShortcut newShortcut = new ClassShortcut((Point2D)((ClassShortcut)shortcut).getProperty(GraphElementProperties.POSITION),
							(Class) shortcutTo.element(), diagram, shortcut.shortcutId());
					newShortcut.setProperty(GraphElementProperties.SIZE, ((ClassShortcut)shortcut).getProperty(GraphElementProperties.SIZE));
					newShortcut.setLoaded(true);
					newShortcut.setLoadedDimension((Dimension)((ClassShortcut)shortcut).getProperty(GraphElementProperties.SIZE));
					diagram.addDiagramElement(newShortcut);
					if (!loaded)
						loaded = true;
				}
			}
		}
	}

	private Link savedLink(Zoom testZoom, UIClassElement loadedClass){
		if (loadedClass == null)
			return null;
		for (NextZoomElement el : loadedClass.getZoomMap().values()){
			if (el.getVisibleElement().equals(testZoom)){
				//nadji konektor 
				for (Connector c : loadedClass.getZoomMap().keySet()){
					if (loadedClass.getZoomMap().get(c) == el){
						return c.getLink();
					}
				}
			}

		}
		return null;
	}

	private Link savedHierarchyLink(Hierarchy testHierarchy, UIClassElement loadedClass){
		if (loadedClass == null)
			return null;
		for (HierarchyElement el : loadedClass.getHierarchyMap().values()){
			if (el.getHierarchy().equals(testHierarchy)){
				//nadji konektor 
				for (Connector c : loadedClass.getHierarchyMap().keySet()){
					if (loadedClass.getHierarchyMap().get(c) == el)
						return c.getLink();
				}
			}

		}
		return null;
	}

	private ClassShortcut findShortcut(ClassShortcut shortcut){
		for (GraphElement element : diagram.getDiagramElements())
			if (shortcut.equals(element))
				return (ClassShortcut) element;
		return null;
	}


	public void generateRelationships(Map<VisibleClass, UIClassElement> allElementsMap,
			GraphEditPackage loadedElement){



		for (VisibleClass visibleClass :  classElementsByVisibleClassesMap.keySet()){
			UIClassElement loadedClass = savedClass(visibleClass, loadedElement);
			
			for (Zoom zoom : VisibleClassUtil.containedZooms(visibleClass)){


				//onaj koji sadrzi zoom je destination
				//onaj koji sadrzi next je source


				UIClassElement targetElement = allElementsMap.get(zoom.getTargetPanel());
				if (targetElement == null)
					continue;
				UIClassElement thisElement = allElementsMap.get(visibleClass);
				
				
				Link loadedLink = savedLink(zoom, loadedClass);
				LinkableElement sourceElement = null;
				LinkableElement destinationElement = null;


				//proveri da li je source ili destination shortcut

				boolean switchSourceAndDestination = false;

				if (loadedLink != null){
					String sourceCardinality = (String) loadedLink.getProperty(LinkProperties.SOURCE_CARDINALITY);

					GraphElement origSourceElement;
					GraphElement origDestinationElement;
					if (sourceCardinality.endsWith("*"))
						switchSourceAndDestination = true;

					if (!switchSourceAndDestination){
						origSourceElement = loadedElement.getDiagram().getElementByConnector().get(loadedLink.getSourceConnector());
						origDestinationElement = loadedElement.getDiagram().getElementByConnector().get(loadedLink.getDestinationConnector());
					}
					else{
						origSourceElement = loadedElement.getDiagram().getElementByConnector().get(loadedLink.getDestinationConnector());
						origDestinationElement = loadedElement.getDiagram().getElementByConnector().get(loadedLink.getSourceConnector());
					}

					if (origSourceElement instanceof ClassShortcut)
						sourceElement = findShortcut((ClassShortcut) origSourceElement);
					if (origDestinationElement instanceof ClassShortcut)
						destinationElement = findShortcut((ClassShortcut) origDestinationElement);

				}

				if (sourceElement == null)
					sourceElement = getDiagramElement(targetElement);
				if (destinationElement == null)
					destinationElement = getDiagramElement(thisElement);



				Connector c1 = null;
				Connector c2 = null;

				String zoomLabel = namer.transformLabelToJavaName(zoom.getLabel());

				int classIndex = visibleClass.getVisibleElementList().indexOf(zoom);
				int groupIndex = ((ElementsGroup) visibleClass.getVisibleElementList().get(UIClassElement.STANDARD_PANEL_PROPERTIES)).getVisibleElementList().indexOf(zoom);
				NextZoomElement zoomElement = new NextZoomElement(targetElement, classIndex, groupIndex, zoomLabel,"1..1",zoom);

				Integer count = thisElement.getRelationshipsCounterMap().get(targetElement);
				if (count == null)
					count = 0;
				count ++;
				thisElement.getRelationshipsCounterMap().put(targetElement, count);


				Next next = null;
				NextZoomElement nextElement = null;
				if (zoom.opposite() != null && zoom.opposite() instanceof Next){

					next = (Next) zoom.opposite();
					String nextLabel = namer.transformLabelToJavaName(next.getLabel());
					UIClassElement targetElement2 = allElementsMap.get(visibleClass);
					classIndex = zoom.getTargetPanel().getVisibleElementList().indexOf(next);
					groupIndex = ((ElementsGroup) zoom.getTargetPanel().getVisibleElementList().get(UIClassElement.STANDARD_PANEL_OPERATIONS)).getVisibleElementList().indexOf(next);
					nextElement = new NextZoomElement(targetElement2, classIndex, groupIndex, nextLabel,"*",next);
				}

				ArrayList<LinkNode> nodes = new ArrayList<LinkNode>();
				Link link = null;


				String nextLabel = "";
				boolean destinationNavigable = false;
				if (next != null){
					nextLabel = namer.transformLabelToJavaName(next.getLabel());
					destinationNavigable = true;

					count = targetElement.getRelationshipsCounterMap().get(thisElement);
					if (count == null)
						count = 0;
					count ++;
					targetElement.getRelationshipsCounterMap().put(thisElement, count);

				}

				String name = zoomLabel;
				if (nextLabel != null && !nextLabel.equals(""))
					name += "_" + nextLabel;

				if (loadedLink == null){
					Point  p1 = new Point(0,0);
					Point p2 = new Point(200,200);
					c1 = new Connector(p1, sourceElement);
					c2 = new Connector(p2, destinationElement);
					nodes.add(c1);
					nodes.add(c2);

					link = new AssociationLink(nodes, "1..1", "*", zoomLabel, nextLabel,name,true,destinationNavigable, true, true, MainFrame.getInstance().incrementLinkCounter());
				}
				else{


					c1 = loadedLink.getSourceConnector();
					c2 = loadedLink.getDestinationConnector();


					Point2D loadedSourcePosition = (Point2D) loadedLink.getSourceConnector().getProperty(LinkNodeProperties.POSITION);
					Point2D sourcePosition = new Point2D.Double(loadedSourcePosition.getX(), loadedSourcePosition.getY());
					Point2D loadedDestinationPosition = (Point2D) loadedLink.getDestinationConnector().getProperty(LinkNodeProperties.POSITION);
					Point2D destinationPosition = new Point2D.Double(loadedDestinationPosition.getX(), loadedDestinationPosition.getY());

					ArrayList<LinkNode> loadedNodes = new ArrayList<LinkNode>();
					loadedNodes.addAll(loadedLink.getNodes());

					boolean showSourceRole = true;
					if (loadedLink.getProperty(LinkProperties.SHOW_SOURCE_ROLE) != null)
						showSourceRole = (Boolean) loadedLink.getProperty(LinkProperties.SHOW_SOURCE_ROLE);

					boolean showDestinationRole = true;
					if (loadedLink.getProperty(LinkProperties.SHOW_DESTINATION_ROLE) != null)
						showDestinationRole = (Boolean) loadedLink.getProperty(LinkProperties.SHOW_DESTINATION_ROLE);


					if (diagram.isLayout()){
						c1.setLoadedPosition(sourcePosition);
						c2.setLoadedPosition(destinationPosition);
					}
					else{
						c1.setLoaded(true);
						c2.setLoaded(true);
						c1.setLoadedPosition(sourcePosition);
						c2.setLoadedPosition(destinationPosition);
					}


					zoomLabel = namer.transformLabelToJavaName(zoom.getLabel());


					if (loadedLink instanceof CompositionLink)
						link = new CompositionLink(loadedNodes, "1..1", "*", zoomLabel, nextLabel, name,true,destinationNavigable, showSourceRole, showDestinationRole, MainFrame.getInstance().incrementLinkCounter());
					else if (loadedLink instanceof AggregationLink)
						link = new AggregationLink(loadedNodes, "1..1", "*", zoomLabel, nextLabel, name ,true,destinationNavigable, showSourceRole, showDestinationRole, MainFrame.getInstance().incrementLinkCounter());
					else
						link = new AssociationLink(loadedNodes, "1..1", "*", zoomLabel, nextLabel, name,true,destinationNavigable, showSourceRole, showDestinationRole, MainFrame.getInstance().incrementLinkCounter());

					if (!switchSourceAndDestination){
						link.setProperty(LinkProperties.DESTINATION_CARDINALITY, loadedLink.getProperty(LinkProperties.DESTINATION_CARDINALITY));
						link.setProperty(LinkProperties.SOURCE_CARDINALITY, loadedLink.getProperty(LinkProperties.SOURCE_CARDINALITY));
					}
					else{
						link.setProperty(LinkProperties.DESTINATION_CARDINALITY, loadedLink.getProperty(LinkProperties.SOURCE_CARDINALITY));
						link.setProperty(LinkProperties.SOURCE_CARDINALITY, loadedLink.getProperty(LinkProperties.DESTINATION_CARDINALITY));
					}
					link.setProperty(LinkProperties.STEREOTYPE, loadedLink.getProperty(LinkProperties.STEREOTYPE));
				}

				link.setProperty(LinkProperties.STEREOTYPE, "zoom");

				c2.setRepresentedElement(thisElement);
				c1.setRepresentedElement(targetElement);
				link.setSourceConnector(c1);
				link.setDestinationConnector(c2);
				c1.setLink(link);
				c2.setLink(link);
				thisElement.getZoomMap().put(c2, zoomElement);
				if (nextElement != null)
					targetElement.getNextMap().put(c1, nextElement);

				sourceElement.addConnectors(link.getSourceConnector());
				destinationElement.addConnectors(link.getDestinationConnector());

				diagram.insertIntoElementByConnectorStructure(link.getSourceConnector(), sourceElement);
				diagram.insertIntoElementByConnectorStructure(link.getDestinationConnector(), destinationElement);
				diagram.addLink(link);

				//check connectors, are they inside the element (bugfix)


			}


			for (Hierarchy hierarchy : VisibleClassUtil.containedHierarchies(visibleClass)){

				UIClassElement targetElement = allElementsMap.get(hierarchy.getTargetPanel());
				if (targetElement == null)
					continue;

				UIClassElement thisElement = allElementsMap.get(visibleClass);

				Link loadedLink = savedHierarchyLink(hierarchy, loadedClass);
				LinkableElement sourceElement = null;
				LinkableElement destinationElement = null;

				//proveri da li je source ili destination shortcut

				boolean switchSourceAndDestination = false;

				if (loadedLink != null){
					String sourceCardinality = (String) loadedLink.getProperty(LinkProperties.SOURCE_CARDINALITY);

					GraphElement origSourceElement;
					GraphElement origDestinationElement;
					if (sourceCardinality.endsWith("*"))
						switchSourceAndDestination = true;

					if (!switchSourceAndDestination){
						origSourceElement = loadedElement.getDiagram().getElementByConnector().get(loadedLink.getSourceConnector());
						origDestinationElement = loadedElement.getDiagram().getElementByConnector().get(loadedLink.getDestinationConnector());
					}
					else{
						origSourceElement = loadedElement.getDiagram().getElementByConnector().get(loadedLink.getDestinationConnector());
						origDestinationElement = loadedElement.getDiagram().getElementByConnector().get(loadedLink.getSourceConnector());
					}

					if (origSourceElement instanceof ClassShortcut)
						sourceElement = findShortcut((ClassShortcut) origSourceElement);
					if (origDestinationElement instanceof ClassShortcut)
						destinationElement = findShortcut((ClassShortcut) origDestinationElement);

				}

				if (sourceElement == null)
					sourceElement = getDiagramElement(thisElement);
				if (destinationElement == null)
					destinationElement = getDiagramElement(targetElement);



				Connector c1 = null;
				Connector c2 = null;


				ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(1);
				int classIndex = visibleClass.getVisibleElementList().indexOf(hierarchy);
				int groupIndex = gr.getVisibleElementList().indexOf(hierarchy);



				ArrayList<LinkNode> nodes = new ArrayList<LinkNode>();


				Link link;

				if (loadedLink == null){
					link = new AssociationLink(nodes, "1..1", "1..1", "","","",false,true, true, true, MainFrame.getInstance().incrementLinkCounter());
					Point  p1 = new Point(0,0);
					Point p2 = new Point(200,200);
					c1 = new Connector(p1, sourceElement);
					c2 = new Connector(p2, destinationElement);
					nodes.add(c1);
					nodes.add(c2);
				}
				else{

					c1 = loadedLink.getSourceConnector();
					c2 = loadedLink.getDestinationConnector();

					Point2D loadedSourcePosition = (Point2D) loadedLink.getSourceConnector().getProperty(LinkNodeProperties.POSITION);
					Point2D sourcePosition = new Point2D.Double(loadedSourcePosition.getX(), loadedSourcePosition.getY());
					Point2D loadedDestinationPosition = (Point2D) loadedLink.getDestinationConnector().getProperty(LinkNodeProperties.POSITION);
					Point2D destinationPosition = new Point2D.Double(loadedDestinationPosition.getX(), loadedDestinationPosition.getY());

					ArrayList<LinkNode> loadedNodes = new ArrayList<LinkNode>();
					loadedNodes.addAll(loadedLink.getNodes());

					if (diagram.isLayout()){
						c1.setLoadedPosition(sourcePosition);
						c2.setLoadedPosition(destinationPosition);
					}
					else{
						c1.setLoaded(true);
						c2.setLoaded(true);
						c1.setLoadedPosition(sourcePosition);
						c2.setLoadedPosition(destinationPosition);
					}


					if (loadedLink instanceof CompositionLink)
						link = new CompositionLink(loadedNodes, "1..1", "1..1", "", "","",false, true, true, true, MainFrame.getInstance().incrementLinkCounter());
					else if (loadedLink instanceof AggregationLink)
						link = new AggregationLink(loadedNodes, "1..1", "1..1", "", "","",false, true, true, true, MainFrame.getInstance().incrementLinkCounter());
					else
						link = new AssociationLink(loadedNodes, "1..1", "1..1", "", "","",false, true, true, true, MainFrame.getInstance().incrementLinkCounter());


					if (!switchSourceAndDestination){
						link.setProperty(LinkProperties.DESTINATION_CARDINALITY, loadedLink.getProperty(LinkProperties.DESTINATION_CARDINALITY));
						link.setProperty(LinkProperties.SOURCE_CARDINALITY, loadedLink.getProperty(LinkProperties.SOURCE_CARDINALITY));
					}
					else{
						link.setProperty(LinkProperties.DESTINATION_CARDINALITY, loadedLink.getProperty(LinkProperties.SOURCE_CARDINALITY));
						link.setProperty(LinkProperties.SOURCE_CARDINALITY, loadedLink.getProperty(LinkProperties.DESTINATION_CARDINALITY));
					}
					link.setProperty(LinkProperties.STEREOTYPE, loadedLink.getProperty(LinkProperties.STEREOTYPE));
				}

				link.setProperty(LinkProperties.STEREOTYPE, "Hierarchy level = " + hierarchy.getLevel());

				c2.setRepresentedElement(thisElement);
				c1.setRepresentedElement(targetElement);
				link.setSourceConnector(c1);
				link.setDestinationConnector(c2);
				c1.setLink(link);
				c2.setLink(link);
				thisElement.getHierarchyMap().put(c1, new HierarchyElement(hierarchy, classIndex, groupIndex));

				sourceElement.addConnectors(link.getSourceConnector());
				destinationElement.addConnectors(link.getDestinationConnector());

				diagram.insertIntoElementByConnectorStructure(link.getSourceConnector(), sourceElement);
				diagram.insertIntoElementByConnectorStructure(link.getDestinationConnector(), destinationElement);
				diagram.addLink(link);
			}
		}
	}

	private LinkableElement getDiagramElement(ClassElement classElement){
		LinkableElement ret;

		//check if elements are from other diagram (package)
		if (!diagram.getDiagramElements().contains(classElement.element())){

			//check if a shortcut was already created
			List<GraphElement> shortcuts = diagram.getAllShortcutsToElementInDiagram(classElement.element());
			if (shortcuts.size() > 0)
				return (LinkableElement) shortcuts.get(0);

			//else create shortcut  and return it
			ret = new  ClassShortcut(new Point2D.Double(0,0), (Class)classElement.element(), 
					GraphEditWorkspace.getInstance().getDiagramContainingElement(classElement.element()));
			ret.setRepresentedElement(classElement);
			diagram.addDiagramElement((Class)ret);
			((Shortcut)ret).setShortcutToModel(diagram);
			((Shortcut)ret).setShortcutInfo(diagram);
		}
		else
			ret = (LinkableElement) classElement.element();

		return ret;
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


	public LayoutStrategy getLayoutStrategy(){
		if (loaded && !diagram.isLayout())
			return LayoutStrategy.ADDING;
		return LayoutUtil.getBestLayoutingStrategy(diagram);

	}

	public GraphEditModel findDiagramContainingElement(GraphElement element){
		//check if current diagram contains element
		if (diagram.getAllElements().contains(element))
			return diagram;

		//check any of subpackages contains diagram with the given element
		GraphEditModel diag = null;
		for (GraphEditPackage subPack : subPackages){
			diag = subPack.findDiagramContainingElement(element);
			if (diag != null)
				break;
		}

		return diag;
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
	public void changeLinkProperty(Link link, LinkProperties property, Object newValue, Object...args) {
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

	public Map<VisibleClass, UIClassElement> getSubClassesMap() {
		return subClassesMap;
	}

	public List<GraphEditPackage> getSubPackages() {
		return subPackages;
	}

	@Override
	public void link(Link link, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unlink(Link link, Object... args) {
		// TODO Auto-generated method stub

	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public Map<VisibleClass, UIClassElement> getClassElementsByVisibleClassesMap() {
		return classElementsByVisibleClassesMap;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}





}
