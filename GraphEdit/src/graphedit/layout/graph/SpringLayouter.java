package graphedit.layout.graph;

import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
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

public class SpringLayouter extends AbstractLayouter{

	private GraphEditView view;
	private GraphEditModel model;
	private LinkStrategy strategy;
	private int xOffset = 0, yOffset = 0;

	public SpringLayouter (GraphEditView view){
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
			
			SpringLayout2<GraphElement, Link> springLayout = new SpringLayout2<GraphElement, Link>(graph);
			springLayout.setForceMultiplier(5);
			
			//triggers layouting
			new DefaultVisualizationModel<GraphElement, Link>(springLayout);

			for (GraphElement element : graph.getVertices()){
				Point2D p = springLayout.transform(element);
				if (maxWidthPoint == null || (p.getX() > maxWidthPoint.getX()))
						maxWidthPoint = p;
				setPosition(strategy, view, (LinkableElement) element, (int) p.getX() + xOffset, (int) p.getY() + yOffset);
			}
			
			xOffset += maxWidthPoint.getX() + 200;
		}
	}
}