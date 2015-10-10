package graphedit.command;

import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

public class RemoveLinkNodeCommand extends Command{

	private LinkNode linkNode;
	private Link link;
	private LinkStrategy strategy;
	private int index;
	
	
	public RemoveLinkNodeCommand(LinkNode linkNode, Link link,
		 GraphEditView view, LinkStrategy strategy) {
		super();
		this.linkNode = linkNode;
		this.link = link;
		this.view = view;
		this.strategy = strategy;
		index = link.getNodes().indexOf(linkNode);
	}
	
	
	@Override
	public void execute() {
		link.getNodes().remove(linkNode);
		link.setNodes(strategy.setLinkNodes(link.getNodes()));
		view.setLinkNodePainters(link);
		view.repaint();
		
	}

	@Override
	public void undo() {
		link.getNodes().add(index, linkNode);
		link.setNodes(strategy.setLinkNodes(link.getNodes()));
		view.setLinkNodePainters(link);
		view.repaint();
		
	}

}
