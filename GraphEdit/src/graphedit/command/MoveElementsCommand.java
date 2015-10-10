package graphedit.command;

import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MoveElementsCommand extends Command {
	private List<GraphElement> elements;
	private int deltaX;
	private int deltaY;
	private LinkStrategy strategy;
	private Link link;
	private ArrayList<ArrayList<Point2D>> oldLinkNodePositions;
	private GraphEditModel model;
	private Point2D position, conPosition;
	private int xPos,yPos;
	private List<Link> associatedLinks;
	
	public MoveElementsCommand(GraphEditView view, int deltaX, int deltaY, LinkStrategy strategy) {
		this.view = view;
		this.model = view.getModel();
		this.elements = new ArrayList<GraphElement>(view.getSelectionModel().getSelectedElements());
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.strategy=strategy;
		oldLinkNodePositions=new ArrayList<ArrayList<Point2D>>();
		associatedLinks = view.getModel().getAssociatedLinks(elements);
		for (Link link:associatedLinks){
			ArrayList<Point2D> oldList=new ArrayList<Point2D>();
			for (LinkNode ln:link.getNodes())
				oldList.add((Point2D)ln.getProperty(LinkNodeProperties.POSITION));
			oldLinkNodePositions.add(oldList);
		}
	}

	public void execute() {
		
		List<Link> links = view.getModel().getContainedLinks(elements);
		for (Link link : links) {
			link.moveNodes(deltaX, deltaY);
		}

		for(GraphElement graphElement : elements){
			position = (Point2D) graphElement.getProperty(GraphElementProperties.POSITION);
			xPos = (int)position.getX() + deltaX;
			yPos = (int)position.getY() + deltaY;
			position.setLocation(xPos, yPos);
			view.getElementPainter(graphElement).formShape();		
			if (graphElement instanceof LinkableElement) {
				LinkableElement linkableElement = (LinkableElement) graphElement;
				for (Connector conn : linkableElement.getConnectors()) {
					conPosition = (Point2D) conn.getProperty(LinkNodeProperties.POSITION);
					conPosition.setLocation(conPosition.getX() + deltaX, conPosition.getY()
							+ deltaY);
					link=conn.getLink();
					if (!isContainedLink(link))
						link.setNodes(strategy.setLinkNodes(link.getNodes()));
				}
		}
		}

		model.fireUpdates();
	}

	public void undo() {
		Connector conn;

		for(GraphElement graphElement : elements){
			position = (Point2D) graphElement.getProperty(GraphElementProperties.POSITION);
			xPos = (int)position.getX() - deltaX;
			yPos = (int)position.getY() - deltaY;
			position.setLocation(xPos, yPos);
			view.getElementPainter(graphElement).formShape();
			if (graphElement instanceof LinkableElement) {
				LinkableElement linkableElement = (LinkableElement) graphElement;
				for (int i=0;i<linkableElement.getConnectors().size();i++){
					conn=linkableElement.getConnectors().get(i);
					conPosition = (Point2D) conn.getProperty(LinkNodeProperties.POSITION);
					conPosition.setLocation(conPosition.getX() - deltaX, conPosition.getY()
							- deltaY);
				}
				for (int i=0;i<associatedLinks.size();i++){
					link=associatedLinks.get(i);
					if (isContainedLink(link)){
						for (int j=0;j<oldLinkNodePositions.get(i).size();j++)
							link.getNodes().get(j).setProperty(LinkNodeProperties.POSITION, oldLinkNodePositions.get(i).get(j));
					}
					else
						link.setNodesWithPositions(oldLinkNodePositions.get(i));
				}
			}
		}
		model.fireUpdates();
	}
		
		private boolean isContainedLink(Link link){
			List<Link> links = view.getModel().getContainedLinks(elements);
			for (Link l:links)
				if(link==l)
					return true;
			return false;
		}

}