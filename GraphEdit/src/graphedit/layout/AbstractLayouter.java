package graphedit.layout;

import graphedit.model.components.Connector;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.components.LinkableElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLayouter implements Layouter{

	protected final int xConnOffset = 0, yConnOffset = 20, yNodeOffset = 0, xNodeOffset =  75; 

	protected void setPosition (LinkStrategy strategy, GraphEditView view, LinkableElement graphElement, int xPosition, int yPosition){
		Point2D conPosition;
		Point2D elemPoistion = (Point2D) graphElement.getProperty(GraphElementProperties.POSITION);
		elemPoistion.setLocation(xPosition, yPosition);
		Link link;
		view.getElementPainter(graphElement).formShape();		
		if (graphElement instanceof LinkableElement) {
			LinkableElement linkableElement = (LinkableElement) graphElement;
			List<Connector> resursiveConnectors = new ArrayList<Connector>();
			for (Connector conn : linkableElement.getConnectors()) {
				if (resursiveConnectors.contains(conn))
					continue;

				link=conn.getLink();
				if (recursiveLink(graphElement, link)){


					Connector sourceConnector = link.getSourceConnector();
					Connector destinationConnector = link.getDestinationConnector();

					if (!sourceConnector.isLoaded() || !destinationConnector.isLoaded()){
						resursiveConnectors.add(sourceConnector);
						resursiveConnectors.add(destinationConnector);


						conPosition = (Point2D) sourceConnector.getProperty(LinkNodeProperties.POSITION);
						conPosition.setLocation(xPosition + xConnOffset, yPosition  - yConnOffset);
						conPosition = (Point2D) destinationConnector.getProperty(LinkNodeProperties.POSITION);
						conPosition.setLocation(xPosition + xConnOffset, yPosition  + yConnOffset);

						int xDim = ((Dimension)graphElement.getProperty(GraphElementProperties.SIZE)).width;
						Point2D nodeLocation1 = new Point2D.Double(xPosition - xDim/2 - xNodeOffset, yPosition - yConnOffset);
						LinkNode newNode1 = new LinkNode(nodeLocation1);
						link.getNodes().add(1, newNode1);
						Point2D nodeLocation2 = new Point2D.Double(xPosition - xDim/2 - xNodeOffset, yPosition + yConnOffset);
						LinkNode newNode2 = new LinkNode(nodeLocation2);
						link.getNodes().add(2, newNode2);

						sourceConnector.setRelativePositions((Point2D) sourceConnector.getProperty(LinkNodeProperties.POSITION));
						destinationConnector.setRelativePositions((Point2D) destinationConnector.getProperty(LinkNodeProperties.POSITION));
					}

				}
				else{

					if (!conn.isLoaded()){
						conPosition = (Point2D) conn.getProperty(LinkNodeProperties.POSITION);
						conPosition.setLocation(xPosition, yPosition);
						conn.setRelativePositions((Point2D) conn.getProperty(LinkNodeProperties.POSITION));
						conn.setPercents((Point2D) conn.getProperty(LinkNodeProperties.POSITION));
					}
				}
				link.setNodes(strategy.setLinkNodes(link.getNodes()));
			}
		}
	}

	protected boolean recursiveLink(LinkableElement graphElement, Link link){
		return graphElement.getConnectors().contains(link.getSourceConnector()) && 
				graphElement.getConnectors().contains(link.getDestinationConnector());



	}


}
