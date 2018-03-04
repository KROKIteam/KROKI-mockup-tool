package graphedit.layout.graph;

import java.awt.geom.Point2D;
import java.util.List;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import graphedit.layout.AbstractLayouter;
import graphedit.layout.LayouterException;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.view.GraphEditView;

public class KKLayouter extends AbstractLayouter{

	private int xOffset = 0, yOffset = 0;

	public KKLayouter (GraphEditView view){
		super(view);
	}
	

	@Override
	public void layout() throws LayouterException {

		Point2D maxWidthPoint = null;
		
		GraphFactory fact = new GraphFactory(model);

		List<Graph<GraphElement, Link>> graphs = fact.createGraphs();


		for (Graph<GraphElement, Link> graph : graphs){
			
			KKLayout<GraphElement, Link> kkLayout = new KKLayout<>(graph);
			kkLayout.setAdjustForGravity(true);
			
			if (graph.getVertexCount() < 4){
				kkLayout.setLengthFactor(0.9);
				kkLayout.setDisconnectedDistanceMultiplier(0.8);
			}
			else if (graph.getVertexCount() < 10){
				kkLayout.setLengthFactor(1.5);
				kkLayout.setDisconnectedDistanceMultiplier(3);
			}
			else if (graph.getVertexCount() < 20){
				kkLayout.setLengthFactor(2);
				kkLayout.setDisconnectedDistanceMultiplier(5);
			}
			else {
				kkLayout.setLengthFactor(3);
				kkLayout.setDisconnectedDistanceMultiplier(10);
			}
			
			
			//triggers layouting
			new DefaultVisualizationModel<GraphElement, Link>(kkLayout);

			for (GraphElement element : graph.getVertices()){
				Point2D p = kkLayout.transform(element);
				if (maxWidthPoint == null || (p.getX() > maxWidthPoint.getX()))
						maxWidthPoint = p;
				setPosition(strategy, view, (LinkableElement) element, (int) p.getX() + xOffset, (int) p.getY() + yOffset);
			}
			
			xOffset += maxWidthPoint.getX() + 200;
		}
	}
}
