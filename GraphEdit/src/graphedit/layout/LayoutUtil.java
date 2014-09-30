package graphedit.layout;

import graphedit.model.diagram.GraphEditModel;

public class LayoutUtil {
	
	public static LayoutStrategy getBestLayoutingStrategy(GraphEditModel model){
		if (model.getLinks().size() == 0)
			return LayoutStrategy.BOX;
		else if (model.getGraphEditPackages().size() + model.getDiagramElements().size() <=6 )
			return LayoutStrategy.CIRCLE;
		return LayoutStrategy.KKGRAPH;
		
	}

}
