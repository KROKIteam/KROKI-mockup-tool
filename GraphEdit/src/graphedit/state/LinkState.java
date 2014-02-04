package graphedit.state;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.app.MainFrame.ToolSelected;
import graphedit.command.Command;
import graphedit.command.LinkElementsCommand;
import graphedit.model.components.AggregationLink;
import graphedit.model.components.AssociationLink;
import graphedit.model.components.AssociationLink.AssociationType;
import graphedit.model.components.ClassStereotypeUI;
import graphedit.model.components.CompositionLink;
import graphedit.model.components.Connector;
import graphedit.model.components.DependencyLink;
import graphedit.model.components.GeneralizationLink;
import graphedit.model.components.InnerLink;
import graphedit.model.components.Link;
import graphedit.model.components.Link.LinkType;
import graphedit.model.components.LinkNode;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.RealizationLink;
import graphedit.model.components.RequireLink;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.elements.ClassElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.properties.ApplicationModeProperties;
import graphedit.properties.Preferences;
import graphedit.strategy.AsIsStrategy;
import graphedit.strategy.LinkStrategy;
import graphedit.strategy.RightAngledStrategy;
import graphedit.view.AggregationLinkPainter;
import graphedit.view.AssociationLinkPainter;
import graphedit.view.CompositionLinkPainter;
import graphedit.view.DependencyLinkPainter;
import graphedit.view.GeneralizationLinkPainter;
import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;
import graphedit.view.InnerLinkPainter;
import graphedit.view.LinkPainter;
import graphedit.view.RealizationLinkPainter;
import graphedit.view.RequireLinkPainter;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.container.ParentChild;

public class LinkState extends State {

	private LinkableElement sourceElement;
	private LinkableElement destinationElement;
	private ArrayList<LinkNode> nodes;	
	private Point firstPoint;	
	private LinkNode ln;
	private Connector sourceConnector;
	private Connector destinationConnector;
	private Link link;
	private boolean linkingInProgress=false;
	private boolean rightButtonNotReleased=false;
	private Link.LinkType linkType;
	private AssociationLink.AssociationType associationType;
	private LinkStrategy strategy;
	private Preferences prefs = Preferences.getInstance();
	private ApplicationModeProperties appModeProperties = ApplicationModeProperties.getInstance();

	public LinkState(GraphEditView view, GraphEditController controller) {
		super(controller);
		nodes=new ArrayList<LinkNode>();
	}

	public LinkState() {

		nodes=new ArrayList<LinkNode>();

	}


	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode()== KeyEvent.VK_ESCAPE){
			view.clearLinkLines();
			firstPoint=null;
			sourceElement=null;
			nodes.clear();
			view.repaint();
		}
	}


	@Override
	public void mousePressed(MouseEvent e) {
		if (nodes==null)
			nodes=new ArrayList<LinkNode>();
		if (e.getButton()==1 && !rightButtonNotReleased){ //promenjeno zbog testa
			if (nodes.size()==0){
				firstPoint=e.getPoint();	
				sourceElement=controller.getMousePressedElement();
				if (sourceElement!=null && sourceElement.linkingCanStart(linkType)){
					sourceConnector=new Connector(firstPoint,sourceElement);
					nodes.add(sourceConnector);
					linkingInProgress=true;
				}
				else
					sourceElement=null;
			}
		}
		else if (e.getButton()==3 && linkingInProgress){
			clearEverything();
			rightButtonNotReleased=true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (e.getButton()==1){
			if (sourceElement!=null){   //zapoceto je povezivanje
				destinationElement=controller.getMouseReleasedElement();

				if (destinationElement!=null)
				{
					//povezivanje elementa sa samim sobom samo ako se izaslo van okvira elementa
					if (destinationElement.linkingCanEnd(linkType, sourceElement) && ((sourceElement!=destinationElement) || nodes.size()>1) 
							&& linkCanBeCreated(sourceElement, destinationElement, linkType)){
						LinkPainter linkPainter=null;
						linkingInProgress=false;
						link=null;
						view.clearLinkLines();
						destinationConnector=new Connector(e.getPoint(),destinationElement);
						nodes.add(destinationConnector);


						if (linkType==Link.LinkType.ASSOCIATION ){ 
							String stereotype = (String) appModeProperties.getPropertyValue("associationOneToManyStereotype");
							String sourceStereotype = (String) sourceElement.getProperty(GraphElementProperties.STEREOTYPE);
							String destinationStereotype = (String) destinationElement.getProperty(GraphElementProperties.STEREOTYPE);
							String sourceCardinality = "1..1", destinationCardinality = "1..*";
							boolean sourceNavigable = false, destinationNavigable = true;
							//Uspostavi hijerarhiju ako je neki parent child
							if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE){
								if (sourceStereotype.equals(ClassStereotypeUI.PARENT_CHILD.toString()) || destinationStereotype.equals(ClassStereotypeUI.PARENT_CHILD.toString())){
									sourceCardinality = "1..1";
									destinationCardinality = "1..1";
									stereotype = (String) appModeProperties.getPropertyValue("associationOneToOneStereotype");
								}
								else{
									sourceNavigable = true;
									sourceCardinality = "1..1";
									destinationCardinality = "*";
								}
							}
							if (associationType==AssociationType.REGULAR){  	
								link = new AssociationLink(nodes, sourceCardinality, destinationCardinality, "", "", stereotype,sourceNavigable, destinationNavigable);
								linkPainter = new AssociationLinkPainter(link);
							}
							else if (associationType==AssociationType.AGGREGATION) {
								link=new AggregationLink(nodes, sourceCardinality, destinationCardinality, "", "", stereotype,sourceNavigable, destinationNavigable);
								linkPainter = new AggregationLinkPainter(link);	
							}
							else if(associationType==AssociationType.COMPOSITION) {
								link=new CompositionLink(nodes, sourceCardinality, destinationCardinality, "", "", stereotype,sourceNavigable, destinationNavigable);
								linkPainter = new CompositionLinkPainter(link);	
							}
							link.setProperty(LinkProperties.STEREOTYPE, stereotype);
						}
						else if (view.getSelectedTool() == ToolSelected.DEPENDENCY) {
							link=new DependencyLink(nodes);
							linkPainter = new DependencyLinkPainter(link);	
						}
						else if (linkType==Link.LinkType.GENERALIZATION) {
							link=new GeneralizationLink(nodes);
							linkPainter = new GeneralizationLinkPainter(link);	
						}
						else if (linkType==Link.LinkType.INNERLINK) {
							link=new InnerLink(nodes);
							linkPainter = new InnerLinkPainter(link);	
						}
						else if (linkType==Link.LinkType.REALIZATION) {
							link=new RealizationLink(nodes);
							linkPainter = new RealizationLinkPainter(link);	
						}
						else if (linkType==LinkType.REQUIRE) {
							link=new RequireLink(nodes);
							linkPainter = new RequireLinkPainter(link);	
						}
						sourceConnector.setLink(link);
						destinationConnector.setLink(link);

						Command command = new LinkElementsCommand(view, link,linkPainter,sourceElement,destinationElement);
						view.getModel().getCommandManager().executeCommand(command);		

						//Strategy
						if (prefs.getProperty(Preferences.RIGHT_ANGLE).equalsIgnoreCase("true")) 
							strategy = new RightAngledStrategy();
						else 
							strategy = new AsIsStrategy();
						nodes=strategy.setLinkNodes(nodes);
						link.setNodes(nodes);

						firstPoint=null;
						nodes=new ArrayList<LinkNode>();
					}
					else
						clearEverything();
				} 
				else {
					ln =new LinkNode(e.getPoint());
					nodes.add(ln);
					view.addLinkLine(firstPoint, e.getPoint());
					firstPoint=e.getPoint();
					view.repaint();
				}

			}
		}
		else if (SwingUtilities.isRightMouseButton(e)){
			if (linkingInProgress){ //odustanak od povezivanje, ali ostanak u stanju
				clearEverything();
				rightButtonNotReleased=false;
			}
			else if (rightButtonNotReleased)
				rightButtonNotReleased=false;
			else
				switchToDefaultState(); //prelazak u stanje Select

		}	
		view.repaint();
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		MainFrame.getInstance().setPositionTrack(e.getX(), e.getY());
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (sourceElement!=null)
			{	
				view.temporarelyAddLinkLine(firstPoint,e.getPoint());
				view.repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// status bar
		MainFrame.getInstance().setPositionTrack(e.getX(), e.getY());
		if (nodes!=null && nodes.size()>0){
			view.temporarelyAddLinkLine(firstPoint,e.getPoint());
			view.repaint();
		}		
	}

	private boolean linkCanBeCreated(LinkableElement sourceElement, LinkableElement destinationElement, Link.LinkType linkType){
		if (MainFrame.getInstance().getAppMode() ==  ApplicationMode.USER_INTERFACE){
			if (sourceElement instanceof Shortcut)
				sourceElement = (LinkableElement) ((Shortcut)sourceElement).shortcutTo();
			
			if (destinationElement instanceof Shortcut)
				destinationElement = (LinkableElement) ((Shortcut)destinationElement).shortcutTo();
			
			if ((((ClassElement)sourceElement.getRepresentedElement()).getUmlType() instanceof StandardPanel) &&
			(((ClassElement)destinationElement.getRepresentedElement()).getUmlType() instanceof ParentChild))
			return false;
			
		}
		if (linkType==LinkType.DEPENDENCY || linkType==LinkType.ASSOCIATION)
			return true;
		if (linkType==LinkType.GENERALIZATION){
			if (sourceElement.equals(destinationElement))
				return false;
			if (model.linkOfThatTypeAlreadyEstablishedInAnyDirection(sourceElement, destinationElement, linkType) ||
					MainFrame.getInstance().getCurrentView().getModel().linkOfThatTypeAlreadyEstablished(sourceElement, destinationElement, LinkType.INNERLINK))
				return false;
			return true;
		}
		if (linkType==LinkType.INNERLINK){
			if (sourceElement.equals(destinationElement))
				return false;
			return !model.linkOfThatTypeAlreadyEstablishedInAnyDirection(sourceElement, destinationElement, linkType);
		}
		return !model.linkOfThatTypeAlreadyEstablished(sourceElement, destinationElement, linkType);
	}

	@Override
	public boolean isAutoScrollOnDragEnabled() {
		return linkingInProgress;
	}

	@Override
	public boolean isAutoScrollOnMoveEnabled() {
		return linkingInProgress;
	}

	public void clearEverything() {
		sourceElement=null;
		firstPoint=null;
		linkingInProgress = false;
		nodes.clear();
		view.clearLinkLines();
		view.repaint();
	}

	public LinkableElement getSourceElement() {
		return sourceElement;
	}

	public LinkableElement getDestinationElement() {
		return destinationElement;
	}

	public ArrayList<LinkNode> getNodes() {
		return nodes;
	}

	public Point getFirstPoint() {
		return firstPoint;
	}

	public LinkNode getLn() {
		return ln;
	}

	public Connector getSourceConnector() {
		return sourceConnector;
	}

	public Connector getDestinationConnector() {
		return destinationConnector;
	}

	public boolean isLinkingInProgress() {
		return linkingInProgress;
	}

	public Link getLink() {
		return link;
	}

	public void setLinkType(Link.LinkType linkType) {
		this.linkType = linkType;
	}

	public void setAssociationType(AssociationLink.AssociationType associationType) {
		this.associationType = associationType;
	}

	public LinkStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(LinkStrategy strategy) {
		this.strategy = strategy;
	}
}