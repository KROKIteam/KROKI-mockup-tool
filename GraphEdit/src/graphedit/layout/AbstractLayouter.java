package graphedit.layout;

import graphedit.model.components.Connector;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

import java.awt.geom.Point2D;

public abstract class AbstractLayouter implements Layouter{

	
	protected void setPosition (LinkStrategy strategy, GraphEditView view, LinkableElement graphElement, int xPosition, int yPosition){
		Point2D conPosition;
		Point2D elemPoistion = (Point2D) graphElement.getProperty(GraphElementProperties.POSITION);
		elemPoistion.setLocation(xPosition, yPosition);
		Link link;
		view.getElementPainter(graphElement).formShape();		
		if (graphElement instanceof LinkableElement) {
			LinkableElement linkableElement = (LinkableElement) graphElement;
			for (Connector conn : linkableElement.getConnectors()) {
				conPosition = (Point2D) conn.getProperty(LinkNodeProperties.POSITION);
				conPosition.setLocation(xPosition, yPosition);
				conn.setRelativePositions((Point2D) conn.getProperty(LinkNodeProperties.POSITION));
				conn.setRelativePositions((Point2D) conn.getProperty(LinkNodeProperties.POSITION));
				link=conn.getLink();
				link.setNodes(strategy.setLinkNodes(link.getNodes()));
			}
		}
	}


}
