package graphedit.layout;

import graphedit.view.GraphEditView;

import java.util.List;

public abstract class CompositeLayouter extends AbstractLayouter{
	
	protected List<AbstractLayouter> layouters;
	
	public CompositeLayouter(GraphEditView view) {
		super(view);
	}



}
