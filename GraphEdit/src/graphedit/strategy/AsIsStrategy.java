package graphedit.strategy;

import java.util.ArrayList;

import graphedit.model.components.LinkNode;

public class AsIsStrategy implements LinkStrategy{

	@Override
	public ArrayList<LinkNode> setLinkNodes(ArrayList<LinkNode> nodes) {
		return nodes;
	}

}
