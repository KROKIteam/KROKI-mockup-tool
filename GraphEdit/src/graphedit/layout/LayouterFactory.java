package graphedit.layout;

import java.awt.Graphics;

import graphedit.layout.adding.AddingLayouter;
import graphedit.layout.random.RandomLayouter;
import graphedit.layout.tree.TreeLayouter;
import graphedit.view.GraphEditView;

public class LayouterFactory {
	
	public static Layouter createLayouter(LayoutStrategy strategy, GraphEditView view, Graphics g){
		Layouter layouter = null;
		
		if (strategy == LayoutStrategy.ADDING)
			layouter = new AddingLayouter(view);
		else if (strategy == LayoutStrategy.RANDOM)
			layouter = new RandomLayouter(view);
		else if (strategy == LayoutStrategy.TREE)
			layouter = new TreeLayouter(view, g);
		
		return layouter;
	}

}
