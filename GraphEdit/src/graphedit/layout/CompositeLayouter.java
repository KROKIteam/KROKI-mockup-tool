package graphedit.layout;

import java.util.List;

import graphedit.view.GraphEditView;

public abstract class CompositeLayouter extends AbstractLayouter{
	
	protected List<AbstractLayouter> layouters;
	
	public CompositeLayouter(GraphEditView view) {
		super(view);
	}



}
