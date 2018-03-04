package graphedit.layout.graph;

import java.awt.geom.Point2D;
import java.util.List;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import graphedit.layout.AbstractLayouter;
import graphedit.layout.LayouterException;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.view.GraphEditView;

public class CircleLayouter  extends AbstractLayouter{


	public CircleLayouter (GraphEditView view){
		super(view);
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