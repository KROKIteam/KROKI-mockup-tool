package graphedit.model.diagram;

import graphedit.command.CommandManager;
import graphedit.model.components.AssociationLink;
import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Package;
import graphedit.model.elements.GraphEditPackage;
import graphedit.model.interfaces.GraphEditTreeNode;
import graphedit.model.properties.Properties;
import graphedit.model.properties.PropertyEnums.DiagramProperties;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.state.AddElementState;
import graphedit.state.LassoSelectionState;
import graphedit.state.LassoZoomState;
import graphedit.state.LinkState;
import graphedit.state.MoveElementState;
import graphedit.state.ResizeState;
import graphedit.state.SelectionState;
import graphedit.state.StateFactory;
import graphedit.state.WaitToBeReleasedState;
import graphedit.state.ZoomInState;
import graphedit.state.ZoomOutState;
import graphedit.util.Calculate;
import graphedit.view.ClassPainter;
import graphedit.view.ElementPainter;
import graphedit.view.LinkPainter;
import graphedit.view.PackagePainter;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

@SuppressWarnings({"unused"})
public class GraphEditModel extends Observable implements Serializable, GraphEditTreeNode {

	private static final long serialVersionUID = 1L;
	private Point2D pointClicked;
	private Point2D pointReleased;

	private GraphEditPackage parentPackage;
	private List<Package> containedPackages;

	private File file;

	private transient CommandManager commandManager;	

	protected List<GraphElement> diagramElements;
	private List<Link> links;
	private Properties<DiagramProperties> properties;
	private Map<Connector, GraphElement> elementByConnector;

	//state facotry for this model
	private transient StateFactory stateFactory;

	private int linkCounter, classCounter, packageCounter , interfaceCounter;

	public GraphEditModel(String name) {
		commandManager = new CommandManager();
		links = new ArrayList<Link>();
		diagramElements = new ArrayList<GraphElement>();
		containedPackages = new ArrayList<Package>();
		properties = new Properties<DiagramProperties>();
		elementByConnector = new HashMap<Connector, GraphElement>();
		properties.set(DiagramProperties.NAME, name);
		stateFactory = new StateFactory(this);
	}

	/**
	 * Copy constructor is being called whenever the object gets deserialized.
	 * @param model represents an instance of <code>GraphEditModel</code> which is to be copied.
	 * @author specijalac
	 */
	public GraphEditModel(GraphEditModel model) {
		commandManager = new CommandManager();
		links = model.getLinks();
		diagramElements = model.getDiagramElements();
		properties = model.getProperties();
		elementByConnector = model.getElementByConnector();
		parentPackage = model.getParentPackage();
		file = model.getFile();
		classCounter = model.getClassCounter();
		linkCounter = model.getLinkCounter();
		packageCounter = model.getPackageCounter();
		stateFactory = new StateFactory(this);
	}

	public void initModel(String name){
		commandManager = new CommandManager();
		stateFactory = new StateFactory(this);
		setProperty(DiagramProperties.NAME,name);
	}


	public void fireUpdates() {
		this.setChanged();
		this.notifyObservers();
	}


	@Override
	public Object getNodeAt(int index) {
		if (index < diagramElements.size()) {
			return diagramElements.get(index);
		} else {
			return links.get(index - diagramElements.size());
		}
	}

	@Override
	public int getNodeCount() {
		return diagramElements.size() + links.size();
	}

	@Override
	public int getNodeIndex(Object node) {
		if (node instanceof GraphElement) {
			if (diagramElements.contains(node)) {
				return diagramElements.indexOf(node);
			}
		} else if (node instanceof Link) {
			if (links.contains(node)) {
				return links.indexOf(node);
			}
		}
		return -1;
	}

	@Override
	public String toString() {
		return (String) properties.get(DiagramProperties.NAME);
	}


	/**
	 * Method removes all of the provided instances from diagram elements.
	 * @param elements represent a list of <code>GraphElement</code> instances which are to be removed.
	 * @author specijalac
	 */
	public void removeDiagramElements(List<GraphElement> elements) {
		for (GraphElement element : elements){
			if (element instanceof Package)
				containedPackages.remove(element);
			else
				diagramElements.remove(element);
		}
		fireUpdates();
	}


	/**
	 * Method adds all of the provided instances to diagram elements.
	 * @param elements represent a list of <code>GraphElement</code> instances which are to be added.
	 * @author specijalac
	 */
	public void addDiagramElements(List<GraphElement> elements) {
		this.diagramElements.addAll(elements);
		fireUpdates();
	}

	/**
	 * Method removes all of the provided instances from links.
	 * @param links represent a list of <code>Link</code> instances which are to be removed.
	 * @author specijalac
	 */
	public void removeLinks(List<Link> links) {
		this.links.removeAll(links);
		fireUpdates();
	}

	/**
	 * Method adds all of the provided instances to links.
	 * @param links represent a list of <code>Link</code> instances which are to be added.
	 * @author specijalac
	 */
	public void addLinks(List<Link> links) {
		this.links.addAll(links);
		fireUpdates();
	}

	public List<GraphElement> pasteDiagramElements(List<ElementPainter> elementPainters, double xDiff, double yDiff) {
		// here we're using cloned element painter list 
		List<GraphElement> addedElements = new ArrayList<GraphElement>();
		List<Package> addedPackages = new ArrayList<Package>();
		GraphElement clonedElement;
		for (ElementPainter elementPainter : elementPainters) {
			clonedElement = ((LinkableElement)elementPainter.getElement());
			if (this.containsElement((String)clonedElement.getProperty(GraphElementProperties.NAME))) {
				clonedElement.setProperty(GraphElementProperties.NAME, "CopyOf" + clonedElement.getProperty(GraphElementProperties.NAME));
				if (elementPainter instanceof ClassPainter){
					((ClassPainter) elementPainter).setUpdated(true);
				}
				else{
					((PackagePainter)elementPainter).setUpdateMeasures(true);

				}
				Point2D point = (Point2D) clonedElement.getProperty(GraphElementProperties.POSITION);
				point.setLocation(point.getX() + xDiff, point.getY() + yDiff);
				elementPainter.setElement(clonedElement);
			}

			if (clonedElement instanceof Package)
				addedPackages.add((Package) clonedElement);
			else
				addedElements.add(clonedElement);	 
		}


		diagramElements.addAll(addedElements);
		containedPackages.addAll(addedPackages);
		addedElements.addAll(addedPackages);

		return addedElements;
	}



	/**
	 * Method pastes provided list of <code>GraphElement</code>, taking care of duplicates.
	 * @param elementPainters represent a list of <code>ElementPainter</code> instances which 
	 * encapsulate <code>GraphElement</code> instances, which are to be pasted.
	 * @return a list of <code>GraphElement</code> instances which were successfully pasted.
	 * @author specijalac
	 */
	public List<GraphElement> pasteDiagramElements(List<ElementPainter> elementPainters, Point location) {
		// here we're using cloned element painter list 
		List<GraphElement> addedElements = new ArrayList<GraphElement>();
		List<Package> addedPackages = new ArrayList<Package>();
		GraphElement clonedElement;
		double minDist = Double.MAX_VALUE;
		GraphElement minDistElement = null;
		for (ElementPainter elementPainter : elementPainters) {
			clonedElement = ((LinkableElement)elementPainter.getElement());
			if (this.containsElement((String)clonedElement.getProperty(GraphElementProperties.NAME))) {
				clonedElement.setProperty(GraphElementProperties.NAME, "CopyOf" + clonedElement.getProperty(GraphElementProperties.NAME));
				if (elementPainter instanceof ClassPainter){
					((ClassPainter) elementPainter).setUpdated(true);
				}
				else{
					((PackagePainter)elementPainter).setUpdateMeasures(true);

				}
				Point2D point = (Point2D) clonedElement.getProperty(GraphElementProperties.POSITION);
				if (location == null){
					point.setLocation(point.getX() + 20, point.getY() + 20);
					elementPainter.setElement(clonedElement);
				}
				else{
					//nadji najblizi, stavi ga na tu poziciju, a onda pomeri sve ostale za rastojanje od pomerenog
					double dist = Calculate.positionDiff(location, point);
					if (dist < minDist){
						minDist = dist;
						minDistElement = clonedElement;
					}
				}

			}
			if (clonedElement instanceof Package)
				addedPackages.add((Package) clonedElement);
			else
				addedElements.add(clonedElement);	 
		}

		if (location != null){
			Point2D point = (Point2D) minDistElement.getProperty(GraphElementProperties.POSITION); 
			Point2D oldLocation = new Point2D.Double(point.getX(),point.getY());
			for (ElementPainter elementPainter : elementPainters){
				clonedElement = ((LinkableElement)elementPainter.getElement());
				point = (Point2D) clonedElement.getProperty(GraphElementProperties.POSITION);
				if (clonedElement == minDistElement){
					point.setLocation(location);
				}
				else{
					double xDiff = oldLocation.getX() - point.getX();
					double yDiff = oldLocation.getY() - point.getY();
					point.setLocation(location.getX() - xDiff, location.getY() - yDiff);

				}
				elementPainter.setElement(clonedElement);
			}
		}
		diagramElements.addAll(addedElements);
		containedPackages.addAll(addedPackages);
		addedElements.addAll(addedPackages);

		return addedElements;
	}

	/**
	 * Method pastes provided list of <code>GraphElement</code>, taking care of duplicates.
	 * @param linkPainters represent a list of <code>LinkPainter</code> instances which 
	 * encapsulate <code>Link</code> instances, which are to be pasted.
	 * @return a list of <code>Link</code> instances which were successfully pasted.
	 * @author specijalac
	 */
	public List<Link> pasteLinks(List<LinkPainter> linkPainters) {
		// here we're using cloned element painter list 
		List<Link> addedLinks = new ArrayList<Link>();
		Link clonedLink;
		for (LinkPainter linkPainter : linkPainters) {
			clonedLink = linkPainter.getLink();
			if (this.containsLink((String)clonedLink.getProperty(LinkProperties.NAME))) {
				clonedLink.setProperty(LinkProperties.NAME, "Copy of " + clonedLink.getProperty(LinkProperties.NAME));
				for (LinkNode node : clonedLink.getNodes()) {
					Point2D point = (Point2D) node.getProperty(LinkNodeProperties.POSITION);
					point.setLocation(point.getX() + 20, point.getY() + 20);
				}
			}
			addedLinks.add(clonedLink); 
		}
		links.addAll(addedLinks);
		fireUpdates();
		return addedLinks;
	}

	/**
	 * Method checks whether this diagram contains an element with provided name. 
	 * @param name represents a unique identifier for <code>GraphElement</code>
	 * @return true - if this diagram already contains such an element, false - otherwise.
	 * @author specijalac
	 */
	public boolean containsElement(String name) {
		for (GraphElement element : diagramElements)
			if (name.equals(element.getProperty(GraphElementProperties.NAME))) 
				return true;
		for (GraphElement element : containedPackages)
			if (name.equals(element.getProperty(GraphElementProperties.NAME))) 
				return true;
		return false;
	}

	/**
	 * Method checks whether this diagram contains a link with provided name. 
	 * @param name represents a unique identifier for <code>Link</code>
	 * @return true - if this diagram already contains such a link, false - otherwise.
	 * @author specijalac
	 */
	public boolean containsLink(String name) {
		for (Link link : links) 
			if (name.equals((String)link.getProperty(LinkProperties.NAME)))
				return true;
		return false;
	}

	/**
	 * Method adds provided mappings to existing hash structure.
	 * @param map represents a map of <code>Connector</code>, <code>GraphElement</code> mappings,
	 * which are to be added to existing hash structure.
	 * @author specijalac
	 */
	public void addToElementByConnectorStructure(Map<Connector, GraphElement> map) {
		elementByConnector.putAll(map);
		//printStructure();
	}

	/**
	 * Method adds a single entry to the hash structure determined by <code>Connector</code> 
	 * and <code>GraphElement</code> instances.
	 * @param connector represents the key.
	 * @param element represents the value.
	 * @author specijalac
	 */
	public void insertIntoElementByConnectorStructure(Connector connector, GraphElement element) {
		if (elementByConnector instanceof Map)
			elementByConnector.put(connector, element);
		//printStructure();
	}

	/**
	 * Method retrieves the <code>GraphElement</code> instance determined by the <code>Connector</code>
	 * instance.
	 * @param connector represents the key of hash structure.
	 * @return an instance of <code>GraphElement</code> determined by the key.
	 * @author specijalac
	 */
	public GraphElement getFromElementByConnectorStructure(Connector connector) {
		if (elementByConnector instanceof Map)
			return elementByConnector.get(connector);
		return null;
	}

	/**
	 * Convenience method for elements removal. Keeps consistency between connectors and elements.
	 * @param links represents a list of <code>Link</code> instances, which are to be removed
	 * @return a map of removed connector, element pairs.
	 * @author specijalac
	 */
	public Map<Connector, GraphElement> removeFromElementByConnectorStructure(List<Link> links) {
		Map<Connector, GraphElement> removed = new HashMap<Connector, GraphElement>();
		for (Link link : links) {
			Map<Connector, GraphElement> removedLink = removeFromElementByConnectorStructure(link);
			for (Connector conn : removedLink.keySet()){
				removed.put(conn, removedLink.get(conn));
			}
		}
		return removed;
	}

	/**
	 * Convenience method for link removal. Keeps consistency between connectors and elements.
	 * @param link represents an instance of <code>Link</code> class, which is to be removed
	 * @return a map of removed connector, element pairs.
	 * @author specijalac
	 */
	public Map<Connector, GraphElement> removeFromElementByConnectorStructure(Link link) {
		Map<Connector, GraphElement> removed = new HashMap<Connector, GraphElement>();
		// detach graph element from its connector
		LinkableElement sourceElement = (LinkableElement)elementByConnector.remove(link.getSourceConnector());
		LinkableElement destinationElement = (LinkableElement)elementByConnector.remove(link.getDestinationConnector());
		Connector sourceConnector = link.getSourceConnector();
		Connector destinationConnector = link.getDestinationConnector();
		// remove mappings
		elementByConnector.remove(sourceConnector);
		elementByConnector.remove(destinationConnector);
		// remove connections
		sourceElement.getConnectors().remove(sourceConnector);
		destinationElement.getConnectors().remove(destinationConnector);
		// backup mappings (for undo)
		removed.put(sourceConnector, sourceElement);
		removed.put(destinationConnector, destinationElement);

		return removed;
	}

	/**
	 * Method returns the list of <code>Link</code> instances contained by the provided 
	 * <code>GraphElement</code> instances. Makes use in Copy command. 
	 * @param elements represent a list of <code>GraphElement</code> instances which 
	 * potentially are to be copied.
	 * @return a list of <code>Link</code> instances which are contained by <code>Connector</code>
	 * instances of provided elements.
	 * @author specijalac
	 */
	public List<Link> getContainedLinks(List<GraphElement> elements) {
		List<Link> result = new ArrayList<Link>();
		for (Link link : links) {
			if (elements.contains(elementByConnector.get(link.getSourceConnector())) && 
					elements.contains(elementByConnector.get(link.getDestinationConnector())))
				result.add(link);
		}
		return result;
	}

	/**
	 * Method returns the list of <code>Link</code> instances associated by the provided 
	 * <code>GraphElement</code> instances. Makes use in Cut command. 
	 * @param elements represent a list of <code>GraphElement</code> instances which 
	 * potentially are to be cut.
	 * @return a list of <code>Link</code> instances which are associated by <code>Connector</code>
	 * instances of provided elements.
	 * @author specijalac
	 */
	public List<Link> getAssociatedLinks(List<GraphElement> elements) {
		List<Link> result = new ArrayList<Link>();
		for (Link link : links) {
			if (elements.contains(elementByConnector.get(link.getSourceConnector())) || 
					elements.contains(elementByConnector.get(link.getDestinationConnector())))
				result.add(link);
		}
		return result;
	}

	/**
	 * Method returns the list of <code>Link</code> instances associated by a single 
	 * <code>GraphElement</code> instance. Makes use in JTree related actions.
	 * @param element represents a single <code>GraphElement</code> instance which
	 * is being removed (Cut/Copy/Delete).
	 * @return a list of <code>Link</code> instances which are associated by <code>Connector</code>
	 * instances of provided elements.
	 * @author specijalac
	 */
	public List<Link> getAssociatedLinks(GraphElement element) {
		List<Link> result = new ArrayList<Link>();
		for (Link link : links) {
			if (element.equals(elementByConnector.get(link.getSourceConnector())) || 
					element.equals(elementByConnector.get(link.getDestinationConnector())))
				result.add(link);
		}
		return result;
	}

	/**
	 * Method updates the <code>Connector</code>, <code>GraphElement</code> hash structure
	 * with respect to provided list of added elements.
	 * @param addedElements represent a list of <code>GraphElement</code> instances which
	 * potentially affect hash stucture consistency.
	 * @author specijalac
	 */
	public void updateHashStructure(List<GraphElement> addedElements) {
		System.out.println("Updating structure:");
		for (GraphElement element : addedElements)
			updateHashStructure(element);
		//printStructure();
	}

	/**
	 * Method updates the <code>Connector</code>, <code>GraphElement</code> hash structure
	 * with respect to provided element.
	 * @param element represents a single instance of <code>GraphElement</code> which
	 * potentially affects hash stucture consistency.
	 * @author specijalac
	 */
	public void updateHashStructure(GraphElement element) {
		for (Connector c : ((LinkableElement)element).getConnectors())
			elementByConnector.put(c, element);
	}

	/**
	 * Convenience method used for console debugging. Displays hash structure contents.
	 * @author specijalac
	 */
	private void printStructure() {
		Set<Entry<Connector, GraphElement>> entrySet = elementByConnector.entrySet();
		System.out.println("HashStructure contents:");
		for (Entry<Connector, GraphElement> e : entrySet) {
			System.out.println("Entry: connector " + e.getKey().toString() + ", element " + e.getValue().toString());
		}
	}

	/**
	 * Method used to test compatibility between source and destination elements in some cases
	 * If there is already a link with the listed source and destination elements, we cannot create a new link
	 * @param sourceElement
	 * @param destinationElement
	 * @param linkType
	 * @author xxx
	 */
	public boolean linkOfThatTypeAlreadyEstablished(LinkableElement sourceElement, LinkableElement destinationElement, Link.LinkType linkType){
		Link link;
		for (Connector connector:sourceElement.getConnectors()){
			link=connector.getLink();
			if (link.getLinkType()==linkType && sourceElement.equals(elementByConnector.get(link.getSourceConnector())) 
					&& destinationElement.equals(elementByConnector.get(link.getDestinationConnector())))
				return true;
		}
		return false;
	}

	/**
	 * Method used to test compatibility between source and destination elements
	 * If there is already a link with the listed source and destination elements, in any direction, we cannot create a new link
	 * @param sourceElement
	 * @param destinationElement
	 * @param linkType
	 * @author xxx
	 */
	public boolean linkOfThatTypeAlreadyEstablishedInAnyDirection(LinkableElement sourceElement, LinkableElement destinationElement, Link.LinkType linkType){
		Link link;
		for (Connector connector:sourceElement.getConnectors()){
			link=connector.getLink();
			if (link.getLinkType()==linkType && ((sourceElement.equals(elementByConnector.get(link.getSourceConnector())) 
					&& destinationElement.equals(elementByConnector.get(link.getDestinationConnector())))|| 
					(destinationElement.equals(elementByConnector.get(link.getSourceConnector())) 
							&& sourceElement.equals(elementByConnector.get(link.getDestinationConnector())))))
				return true;
		}
		return false;
	}
	/**
	 * @param element
	 * @param linkType
	 * @return LinkableElements linked with element, but only if type of link between them is linkType
	 * @author xxx
	 */
	public ArrayList<LinkableElement> getElementsLinkedWithElement(LinkableElement element, Link.LinkType linkType){
		ArrayList<LinkableElement> retVal=new ArrayList<LinkableElement>();
		Link link;
		for (Connector connector:element.getConnectors()){
			link=connector.getLink();
			if (link.getLinkType()==linkType)
				retVal.add((LinkableElement) elementByConnector.get(link.getSourceConnector()));
		}
		return retVal;
	}
	/**
	 * Method used to update properties of an association link
	 * @param newSourceCardinality
	 * @param newDestinationCardinality
	 * @param newSourceRole
	 * @param newDestinationRole
	 * @param link
	 * @author xxx
	 */
	public void changeAssociationLinkProperties(String newSourceCardinality,
			String newDestinationCardinality, String newSourceRole,
			String newDestinationRole,boolean newSourceNavigable, boolean newDestinationNavigable, AssociationLink link){
		link.setProperty(LinkProperties.SOURCE_CARDINALITY,newSourceCardinality);
		link.setProperty(LinkProperties.DESTINATION_CARDINALITY,newDestinationCardinality);
		link.setProperty(LinkProperties.SOURCE_ROLE,newSourceRole);
		link.setProperty(LinkProperties.DESTINATION_ROLE, newDestinationRole);
		link.setProperty(LinkProperties.SOURCE_NAVIGABLE,newSourceNavigable);
		link.setProperty(LinkProperties.DESTINATION_NAVIGABLE, newDestinationNavigable);
		fireUpdates();
	}



	/*
	 * Getters and setters and other methods
	 */

	public Point2D getPointClicked() {
		return pointClicked;
	}

	public void setPointClicked(Point2D newPointClicked) {
		pointClicked = newPointClicked;
	}

	public Point2D getPointReleased() {
		return pointReleased;
	}

	public void setPointReleased(Point2D newPointReleased) {
		pointReleased = newPointReleased;
	}

	public void addElement(GraphElement element) {
	}

	public void removeElements() {
	}

	public void linkElements(GraphElement sourceElement,
			GraphElement destinationElement) {
	}

	public void moveElements() {
	}

	public void resizeElements(double scaleFactor) {
	}

	public boolean resizeElement() {
		return false;
	}

	public void rotateElementsByAngle(double angle) {
	}

	public void saveDiagram() {
	}

	public void closeDiagram() {
	}

	public List<GraphElement> getDiagramElements() {
		if (diagramElements == null)
			diagramElements = new ArrayList<GraphElement>();
		return diagramElements;
	}

	public List<GraphElement> getAllElements(){
		List<GraphElement> ret = new ArrayList<GraphElement>();
		ret.addAll(diagramElements);
		ret.addAll(containedPackages);
		ret.addAll(links);
		return ret;
	}

	public Iterator<GraphElement> getIteratorDiagramElements() {
		if (diagramElements == null)
			diagramElements = new ArrayList<GraphElement>();
		return diagramElements.iterator();
	}

	public void setDiagramElements(List<GraphElement> newDiagramElements) {
		removeAllDiagramElements();
		for (Iterator<GraphElement> iter = newDiagramElements.iterator(); iter
				.hasNext();)
			addDiagramElement(iter.next());
	}

	public void addDiagramElement(GraphElement	newGraphElement) {
		if (newGraphElement == null)
			return;
		if (this.diagramElements == null)
			this.diagramElements = new ArrayList<GraphElement>();
		if (!this.diagramElements.contains(newGraphElement))
			this.diagramElements.add(newGraphElement);

		// fire-uj izmene
		fireUpdates();
	}

	public void removeDiagramElement(GraphElement oldGraphElement) {
		if (oldGraphElement == null)
			return;
		if (this.diagramElements != null) {
			if (this.diagramElements.contains(oldGraphElement)) {
				this.diagramElements.remove(oldGraphElement);
			}
		}

		// fire-uj izmene
		fireUpdates();
	}

	public void removeAllDiagramElements() {
		if (diagramElements != null)
			diagramElements.clear();
	}

	public List<Link> getLinks() {
		if (links == null)
			links = new ArrayList<Link>();
		return links;
	}

	public Iterator<Link> getIteratorLinks() {
		if (links == null)
			links = new ArrayList<Link>();
		return links.iterator();
	}

	public void setLinks(List<Link> newLinks) {
		removeAllLinks();
		for (Iterator<Link> iter = newLinks.iterator(); iter.hasNext();)
			addLink((Link) iter.next());
	}

	public void addLink(Link newLink) {
		if (newLink == null)
			return;
		if (this.links == null)
			this.links = new ArrayList<Link>();
		if (!this.links.contains(newLink))
			this.links.add(newLink);
		fireUpdates();
	}

	public void removeLink(Link oldLink) {
		if (oldLink == null)
			return;
		if (this.links != null)
			if (this.links.contains(oldLink))
				this.links.remove(oldLink);
		fireUpdates();
	}

	public void removeAllLinks() {
		if (links != null)
			links.clear();
	}


	public List<Package> getGraphEditPackages() {
		if (containedPackages == null)
			containedPackages = new ArrayList<Package>();
		return containedPackages;
	}

	public Iterator<Package> getIteratorGraphEditPackages() {
		if (containedPackages == null)
			containedPackages = new ArrayList<Package>();
		return containedPackages.iterator();
	}

	public void setGraphEditPackages(List<Package> newGraphEditPackages) {
		removeAllGraphEditPackages();
		for (Iterator<Package> iter = newGraphEditPackages.iterator(); iter.hasNext();)
			addGraphEditPackage((Package) iter.next());
	}

	public void addGraphEditPackage(Package newGraphEditPackage) {
		if (newGraphEditPackage == null)
			return;
		if (this.containedPackages == null)
			this.containedPackages = new ArrayList<Package>();
		if (!this.containedPackages.contains(newGraphEditPackage))
			this.containedPackages.add(newGraphEditPackage);
		fireUpdates();
	}

	public void removeGraphEditPackage(Package oldGraphEditPackage) {
		if (oldGraphEditPackage == null)
			return;
		if (this.containedPackages != null)
			if (this.containedPackages.contains(oldGraphEditPackage))
				this.containedPackages.remove(oldGraphEditPackage);
		fireUpdates();
	}

	public void removeAllGraphEditPackages() {
		if (containedPackages != null)
			containedPackages.clear();
	}

	/**
	 * Copies all elements form given model and initializes counters
	 * @param model
	 */
	public void copyElements(GraphEditModel model){
		diagramElements.addAll(model.getDiagramElements());
		containedPackages.addAll(model.getContainedPackages());
		links.addAll(model.getLinks());
		linkCounter = model.getLinkCounter();
		classCounter = model.getPackageCounter();
		packageCounter = model.getPackageCounter();

	}



	public List<Package> getContainedPackages() {
		return containedPackages;
	}

	public void setContainedPackages(List<Package> containedPackages) {
		this.containedPackages = containedPackages;
	}

	public void setParentPackage(GraphEditPackage parentPackage) {
		this.parentPackage = parentPackage;
	}
	public Object getProperty(DiagramProperties key) {
		return properties.get(key);
	}

	public Object setProperty(DiagramProperties key, Object value) {
		Object result = properties.set(key, value);

		// fire-uj izmene
		fireUpdates();

		return result;
	}

	public CommandManager getCommandManager() {
		if (commandManager == null)
			commandManager = new CommandManager();
		return commandManager;
	}

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}	

	public AddElementState getAddElementState() {
		return (AddElementState) stateFactory.getState("graphedit.state.AddElementState");
	}

	public LassoSelectionState getLassoSelectionState() {
		return (LassoSelectionState) stateFactory.getState("graphedit.state.LassoSelectionState");
	}

	public LassoZoomState getLassoZoomState() {
		return (LassoZoomState) stateFactory.getState("graphedit.state.LassoZoomState");
	}

	public LinkState getLinkState() {
		return (LinkState) stateFactory.getState("graphedit.state.LinkState");
	}


	public MoveElementState getMoveElementState() {
		return (MoveElementState) stateFactory.getState("graphedit.state.MoveElementState");
	}

	public ResizeState getResizeState() {
		return (ResizeState) stateFactory.getState("graphedit.state.ResizeState");
	}

	public SelectionState getSelectionState() {
		if (stateFactory == null) 
			stateFactory = new StateFactory(this);
		return (SelectionState) stateFactory.getState("graphedit.state.SelectionState");
	}

	public ZoomInState getZoomInState() {
		return (ZoomInState) stateFactory.getState("graphedit.state.ZoomInState");
	}

	public ZoomOutState getZoomOutState() {
		return (ZoomOutState) stateFactory.getState("graphedit.state.ZoomOutState");
	}


	public WaitToBeReleasedState getWaitToBeReleasedState() {
		return (WaitToBeReleasedState) stateFactory.getState("graphedit.state.WaitToBeReleasedState");
	}
	public Map<Connector, GraphElement> getElementByConnector() {
		return elementByConnector;
	}

	public void setElementByConnector(
			Map<Connector, GraphElement> elementByConnector) {
		this.elementByConnector = elementByConnector;
	}

	public Properties<DiagramProperties> getProperties() {
		return properties;
	}

	public void setProperties(Properties<DiagramProperties> properties) {
		this.properties = properties;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		System.out.println("Project: " + file.getAbsolutePath());
	}

	public GraphEditPackage getParentPackage() {
		return parentPackage;
	}

	public void setParentProject(GraphEditPackage parentPackage) {
		this.parentPackage = parentPackage;
	}

	public int getLinkCounter() {
		return linkCounter;
	}

	public void setLinkCounter(int linkCounter) {
		this.linkCounter = linkCounter;
	}

	public int getClassCounter() {
		return classCounter;
	}

	public void setClassCounter(int classCounter) {
		this.classCounter = classCounter;
	}

	public int getPackageCounter() {
		return packageCounter;
	}

	public void setPackageCounter(int packageCounter) {
		this.packageCounter = packageCounter;
	}

	public int getInterfaceCounter() {
		return interfaceCounter;
	}

	public void setInterfaceCounter(int interfaceCounter) {
		this.interfaceCounter = interfaceCounter;
	}


}