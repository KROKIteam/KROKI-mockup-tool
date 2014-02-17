package graphedit.command;

import graphedit.model.components.Connector;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

import java.awt.geom.Point2D;
import java.util.List;


public class MoveLinkNodeCommand extends Command {
	private GraphEditModel model;
	private Link link;
	private LinkStrategy strategy;
	private List<LinkNode> nodes;
	private List<Point2D> oldPositions, newPositions;


	public MoveLinkNodeCommand(GraphEditView view, List<LinkNode> nodes, Link link, List<Point2D> newPositions, List<Point2D> oldPositions, LinkStrategy strategy) {
		this.view = view;
		this.nodes = nodes;
		this.model = view.getModel();
		this.newPositions = newPositions;
		this.link = link;
		this.strategy=strategy;
		this.oldPositions = oldPositions;

	}

	public void execute() {
		Point2D position;
		LinkNode node;
		for (int i = 0; i < nodes.size(); i++){
			node = nodes.get(i);
			position = (Point2D) node.getProperty(LinkNodeProperties.POSITION);
			position.setLocation(newPositions.get(i));
			if (node instanceof Connector){
				((Connector) node).setRelativePositions(position);
				((Connector) node).setPercents(position);
			}
		}
		link.setNodes(strategy.setLinkNodes(link.getNodes()));
		view.setLinkNodePainters(link);
		model.fireUpdates();
	}

	public void undo() {
		Point2D position;
		LinkNode node;
		for (int i = 0; i < nodes.size(); i++){
			node = nodes.get(i);
			position = (Point2D) node.getProperty(LinkNodeProperties.POSITION);
			position.setLocation(oldPositions.get(i));
			if (node instanceof Connector){
				((Connector) node).setRelativePositions(position);
				((Connector) node).setPercents(position);
			}
		}
		link.setNodes(strategy.setLinkNodes(link.getNodes()));
		view.setLinkNodePainters(link);
		model.fireUpdates();
	}

}