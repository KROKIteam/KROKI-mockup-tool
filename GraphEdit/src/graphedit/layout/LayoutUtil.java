package graphedit.layout;

import graphedit.model.diagram.GraphEditModel;

public class LayoutUtil {
	
	public static LayoutStrategy getBestLayoutingStrategy(GraphEditModel model){
		if (model.getLinks().size() == 0)
			return LayoutStrategy.BOX;
		return LayoutStrategy.KKGRAPH;
		
	}

}
