package graphedit.command;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Package;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.AbstractLinkElement;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.GraphEditElement;
import graphedit.model.elements.GraphEditPackage;
import graphedit.model.elements.UIClassElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kroki.uml_core_basic.UmlPackage;

public class CutElementsCommand extends Command {

	private  List<GraphElement> elements;
	private  List<ElementPainter> elementPainters;
	private  List<Link> links;
	private  List<LinkPainter> linkPainters;
	private Map<Connector, GraphElement> removedMappings;
	private GraphEditModel model;
	private List<GraphEditPackage> packages;
	private Command deletePackages;
	private UmlPackage umlPackage;
	private List<AbstractLinkElement> sourceList = new ArrayList<AbstractLinkElement>(), destinationList = new ArrayList<AbstractLinkElement>();

	private Map<Shortcut, GraphElement> replacements = new HashMap<Shortcut, GraphElement>();
	private List<Shortcut> unlinked = new ArrayList<Shortcut>();
	private Map<Connector, Point2D> oldPositions = new HashMap<Connector, Point2D>();


	/*
	 * WIP - nakon UNDO i REDO srediti
	 */
	public CutElementsCommand(GraphEditView view, List<GraphElement> elements, List<ElementPainter> elementPainters) {
		packages = new ArrayList<GraphEditPackage>();
		this.elementPainters = new ArrayList<ElementPainter>();
		this.elements = new ArrayList<GraphElement>();
		for (GraphElement element : elements){
			if (element instanceof Package)
				packages.add( ((Package)element).getHierarchyPackage());
			else{
				this.elements.add(element);
				this.elementPainters.add(view.getElementPainter(element));
			}
		}
		this.model = view.getModel();
		this.view = view;
		this.elements = new ArrayList<GraphElement>(elements);
		this.elementPainters = new ArrayList<ElementPainter>(elementPainters);
		this.links = model.getAssociatedLinks(elements);
		this.linkPainters = view.getLinkPainters(links);
		deletePackages = new DeletePackagesCommand(packages, view);
		umlPackage = model.getParentPackage().getUmlPackage();

		if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE && links!=null)
			for (Link link : links){
				sourceList.add(((UIClassElement)link.getSourceConnector().getRepresentedElement()).getCurrentElement(link.getSourceConnector()));
				destinationList.add(((UIClassElement)link.getDestinationConnector().getRepresentedElement()).getCurrentElement(link.getDestinationConnector()));
			}


		for (GraphElement el : elements){
			if (el instanceof Shortcut){
				Shortcut s = (Shortcut) el;
				if (unlinkAfterDeletingShortcut(s))
					unlinked.add(s);
				else
					replacements.put(s, getReplacementElement(s));
			}
		}

	}


	@Override
	public void execute() {
		model.removeDiagramElements(elements);
		//model.removeLinks(links);

		/* update mappings - connector - element */
		removedMappings = model.removeFromElementByConnectorStructure(links);

		view.removeElementPainters(elementPainters);
		//	view.removeLinkPainters(linkPainters);
		view.getSelectionModel().removeAllSelectedElements();
		deletePackages.execute();



		boolean unlink;
		for (Link link : links){

			unlink = true;
			GraphElement source = removedMappings.get(link.getSourceConnector());
			GraphElement destination = removedMappings.get(link.getDestinationConnector());
			
			if (source instanceof Shortcut || destination instanceof Shortcut)
				unlink = unlinked.contains(source) || unlinked.contains(destination)
					|| (elements.contains(source) && elements.contains(destination));
			
			if (source instanceof Shortcut){
				if (!unlink){
					LinkableElement replacement = (LinkableElement) replacements.get(source);
					replacement.addConnectors(link.getSourceConnector());
					Point2D oldPosition = new Point2D.Double(((Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION)).getX(),
							((Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION)).getY());

					oldPositions.put(link.getSourceConnector(), oldPosition);
					Point2D newPosition = new Point2D.Double(((Point2D)replacement.getProperty(GraphElementProperties.POSITION)).getX(),
							((Point2D)replacement.getProperty(GraphElementProperties.POSITION)).getY());
					link.getSourceConnector().setProperty(LinkNodeProperties.POSITION, newPosition);
					model.insertIntoElementByConnectorStructure(link.getSourceConnector(), replacement);
				}
			}
			
			if (destination instanceof Shortcut){
				if (!unlink){
					LinkableElement replacement = (LinkableElement) replacements.get(destination);
					replacement.addConnectors(link.getDestinationConnector());
					Point2D oldPosition = new Point2D.Double(((Point2D)link.getDestinationConnector().getProperty(LinkNodeProperties.POSITION)).getX(),
							((Point2D)link.getDestinationConnector().getProperty(LinkNodeProperties.POSITION)).getY());

					oldPositions.put(link.getDestinationConnector(), oldPosition);
					Point2D newPosition = new Point2D.Double(((Point2D)replacement.getProperty(GraphElementProperties.POSITION)).getX(),
							((Point2D)replacement.getProperty(GraphElementProperties.POSITION)).getY());
					link.getDestinationConnector().setProperty(LinkNodeProperties.POSITION, newPosition);
					model.insertIntoElementByConnectorStructure(link.getDestinationConnector(), replacement);
				}
			}
			
			if (unlink){

				model.removeLink(link);
				view.removeLinkPainters(view.getLinkPainter(link));

				GraphEditElement sourceElement = link.getSourceConnector().getRepresentedElement();
				GraphEditElement destinationElement = link.getDestinationConnector().getRepresentedElement();
				sourceElement.unlink(link);
				destinationElement.unlink(link);

				for (GraphElement element : elements){
					GraphElement nonShortcut = element;
					if (element instanceof Shortcut)
						nonShortcut = ((Shortcut)element).shortcutTo();

					GraphEditElement gElement = nonShortcut.getRepresentedElement();
					umlPackage.removeOwnedType(((ClassElement)gElement).getUmlType());

				}
			}

		}
	}
	@Override
	public void undo() {
		model.addDiagramElements(elements);
		model.addLinks(links);
		model.addToElementByConnectorStructure(removedMappings);
		//model.updateHashStructure(elements);

		view.addElementPainters(elementPainters);
		view.addLinkPainters(linkPainters);

		view.getSelectionModel().addSelectedElements(elements);
		deletePackages.undo();

		Link link;
		boolean unlink;

		for (int i=0; i<links.size();i++){
			link = links.get(i);

			unlink = true;
			GraphElement source = removedMappings.get(link.getSourceConnector());
			GraphElement destination = removedMappings.get(link.getDestinationConnector());
			if (source instanceof Shortcut || destination instanceof Shortcut)
				unlink = unlinked.contains(source) || unlinked.contains(destination)
					|| (elements.contains(source) && elements.contains(destination));

			
			
			if (source instanceof Shortcut){

				if (!unlink){

					//nije unlinkovano, samo je premesteno
					LinkableElement replacement = (LinkableElement) replacements.get(source);
					replacement.removeConnectors(link.getSourceConnector());
					((LinkableElement)source).addConnectors(link.getSourceConnector());
					link.getSourceConnector().setProperty(LinkNodeProperties.POSITION, oldPositions.get(link.getSourceConnector()));
					model.insertIntoElementByConnectorStructure(link.getSourceConnector(), source);
				}
			}

			if (destination instanceof Shortcut){

				if (!unlink){

					//nije unlinkovano, samo je premesteno
					LinkableElement replacement = (LinkableElement) replacements.get(destination);
					replacement.removeConnectors(link.getDestinationConnector());
					((LinkableElement)destination).addConnectors(link.getDestinationConnector());
					link.getDestinationConnector().setProperty(LinkNodeProperties.POSITION, oldPositions.get(link.getDestinationConnector()));
					model.insertIntoElementByConnectorStructure(link.getDestinationConnector(), source);
				}
			}

			if (unlink){
				GraphEditElement sourceElement = link.getSourceConnector().getRepresentedElement();
				GraphEditElement destinationElement = link.getDestinationConnector().getRepresentedElement();
				//sourceElement.link(link);
				//destinationElement.link(link);
				if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE){
					sourceElement.setOldLink(link, sourceList.get(i));
					destinationElement.setOldLink(link, destinationList.get(i));
				}
				for (GraphElement element : elements){
					GraphElement nonShortcut = element;
					if (element instanceof Shortcut)
						nonShortcut = ((Shortcut)element).shortcutTo();

					GraphEditElement gElement = nonShortcut.getRepresentedElement();
					umlPackage.addOwnedType(((ClassElement)gElement).getUmlType());

				}
			}


		}
	}


	private boolean unlinkAfterDeletingShortcut(Shortcut shortcut){
		//pogledaj da li ima jos neki shortcut u tom paketu/view-u
		LinkableElement element = (LinkableElement) shortcut.shortcutTo();
		for (Shortcut s : element.getShortcuts()){
			if (s == shortcut)
				continue;
			if (s.shortcutToModel() == shortcut.shortcutToModel())
				return false;
		}
		return !shortcut.shortcutToModel().getDiagramElements().contains(element);
	}

	private GraphElement getReplacementElement(Shortcut shortcut){

		LinkableElement element = (LinkableElement) shortcut.shortcutTo();
		double minDiff = -1;
		GraphElement replacement = null;
		Point2D shortcutLocation = (Point2D) ((GraphElement) shortcut).getProperty(GraphElementProperties.POSITION);
		for (Shortcut s : element.getShortcuts()){
			if (s == shortcut)
				continue;
			if (s.shortcutToModel() == shortcut.shortcutToModel()){
				double diff = positionDiff( (Point2D) ((GraphElement)s).getProperty(GraphElementProperties.POSITION), shortcutLocation);
				if (minDiff == -1 || diff < minDiff){
					minDiff = diff;
					replacement = (GraphElement)s;
				}
			}
		}

		if (shortcut.shortcutToModel().getDiagramElements().contains(element)){
			double diff = positionDiff( (Point2D) element.getProperty(GraphElementProperties.POSITION), shortcutLocation);
			if (minDiff == -1 || diff < minDiff){
				replacement = element;
			}

		}
		return replacement;
	}



	private double positionDiff(Point2D p1, Point2D p2){
		return Math.hypot(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
	}

}