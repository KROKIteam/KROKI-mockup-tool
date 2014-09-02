package graphedit.layout.graph;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import graphedit.layout.AbstractLayouter;
import graphedit.layout.LayouterException;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.strategy.AsIsStrategy;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

import java.awt.geom.Point2D;
import java.util.List;

public class CircleLayouter  extends AbstractLayouter{

	private GraphEditView view;
	private GraphEditModel model;
	private LinkStrategy strategy;
	private int xOffset = 0, yOffset = 0;

	public CircleLayouter (GraphEditView view){
		this.view = view;
		this.model = view.getModel();
		strategy = new AsIsStrategy();
	}
	

	@Override
	public void layout() throws LayouterException {

		Point2D maxWidthPoint = null;
		
		GraphFactory fact = new GraphFactory(model);

		List<Graph<GraphElement, Link>> graphs = fact.createGraphs();

		for (Graph<GraphElement, Link> graph : graphs){
			
			CircleLayout<GraphElement, Link> circleLayout = new CircleLayout<GraphElement, Link>(graph);
			
			//triggers layouting
			new DefaultVisualizationModel<GraphElement, Link>(circleLayout);

			for (GraphElement element : graph.getVertices()){
				Point2D p = circleLayout.transform(element);
				if (maxWidthPoint == null || (p.getX() > maxWidthPoint.getX()))
						maxWidthPoint = p;
				setPosition(strategy, view, (LinkableElement) element, (int) p.getX() + xOffset, (int) p.getY() + yOffset);
			}
			
			xOffset += maxWidthPoint.getX() + 200;
		}
	}
}