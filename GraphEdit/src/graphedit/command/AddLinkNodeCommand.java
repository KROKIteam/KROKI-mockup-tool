package graphedit.command;

import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

public class AddLinkNodeCommand extends Command{

	private LinkNode linkNode;
	private Link link;
	private int index;
	private LinkStrategy strategy;
	
	
	public AddLinkNodeCommand(LinkNode linkNode, Link link, int index,
		 GraphEditView view, LinkStrategy strategy) {
		super();
		this.linkNode = linkNode;
		this.link = link;
		this.view = view;
		this.index = index;
		this.strategy = strategy;
	}

	@Override
	public void execute() {
		link.getNodes().add(index, linkNode);
		link.setNodes(strategy.setLinkNodes(link.getNodes()));
		view.setLinkNodePainters(link);
		view.repaint();
		
	}

	@Override
	public void undo() {
		link.getNodes().remove(linkNode);
		link.setNodes(strategy.setLinkNodes(link.getNodes()));
		view.setLinkNodePainters(link);
		view.repaint();
	}

}
