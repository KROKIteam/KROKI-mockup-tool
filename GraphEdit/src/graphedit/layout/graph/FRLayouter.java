package graphedit.layout.graph;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import graphedit.layout.AbstractLayouter;
import graphedit.layout.LayouterException;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.view.GraphEditView;

import java.awt.geom.Point2D;
import java.util.List;

public class FRLayouter extends AbstractLayouter{

	private int xOffset = 0, yOffset = 0;

	public FRLayouter (GraphEditView view){
		super(view);
	}
	

	@Override
	public void layout() throws LayouterException {

		Point2D maxWidthPoint = null;
		
		GraphFactory fact = new GraphFactory(model);

		List<Graph<GraphElement, Link>> graphs = fact.createGraphs();

		for (Graph<GraphElement, Link> graph : graphs){
			
			FRLayout<GraphElement, Link> frLayout = new FRLayout<GraphElement, Link>(graph);
			frLayout.setRepulsionMultiplier(3);
			
			//triggers layouting
			new DefaultVisualizationModel<GraphElement, Link>(frLayout);

			for (GraphElement element : graph.getVertices()){
				Point2D p = frLayout.transform(element);
				if (maxWidthPoint == null || (p.getX() > maxWidthPoint.getX()))
					maxWidthPoint = p;
				setPosition(strategy, view, (LinkableElement) element, (int) p.getX() + xOffset, (int) p.getY() + yOffset);
			}
			
			xOffset += maxWidthPoint.getX() + 200;
		}
	}
}