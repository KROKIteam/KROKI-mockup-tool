package graphedit.command;

import graphedit.model.components.Connector;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public class MoveLinkNodeCommand extends Command {
	private LinkNode linkNode;
	private GraphEditModel model;
	private Point2D newPosition,oldPosition,position;
	private Link link;
	private ArrayList<LinkNode> newLinkNodes, oldLinkNodes;
	private LinkStrategy strategy;

	
	public MoveLinkNodeCommand(GraphEditView view, Point2D newPosition,Point2D oldPosition, LinkStrategy strategy) {
		this.view = view;
		this.model = view.getModel();
		this.linkNode = view.getSelectionModel().getSelectedNode();
		this.newPosition=new Point2D.Double();
		this.oldPosition=new Point2D.Double();
		this.newPosition.setLocation(newPosition);
		this.oldPosition.setLocation(oldPosition);
		link=view.getSelectionModel().getSelectedLink();
		oldLinkNodes=link.getNodes();
		this.strategy=strategy;
		position = (Point2D) linkNode.getProperty(LinkNodeProperties.POSITION);
	}
	
	public void execute() {
		position.setLocation(newPosition);
		newLinkNodes=strategy.setLinkNodes(link.getNodes());
		link.setNodes(newLinkNodes);
		if (linkNode instanceof Connector){
			((Connector) linkNode).setRelativePositions(newPosition);
			((Connector) linkNode).setPercents(newPosition);
		}
		view.setLinkNodePainters(link);
		model.fireUpdates();
	}

	public void undo() {
		position.setLocation(oldPosition);
		if (linkNode instanceof Connector){
			((Connector) linkNode).setRelativePositions(oldPosition);
			((Connector) linkNode).setPercents(oldPosition);
		}
		link.setNodes(oldLinkNodes);
		view.setLinkNodePainters(link);
		model.fireUpdates();
	}

}