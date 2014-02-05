package graphedit.strategy;

import graphedit.model.components.LinkNode;

import java.util.ArrayList;

public class AsIsStrategy implements LinkStrategy{

	@Override
	public ArrayList<LinkNode> setLinkNodes(ArrayList<LinkNode> nodes) {
		return nodes;
	}

}
