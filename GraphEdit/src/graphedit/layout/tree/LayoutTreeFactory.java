package graphedit.layout.tree;


import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LayoutTreeFactory {
	
	private GraphEditModel model;
	private List<GraphElement> elements;
	private HashMap<GraphElement, LayoutTreeNode> elementNodeMap;
	
	
	private enum ConnectorType {SOURCE, DESTINATION};

	public LayoutTreeFactory(GraphEditModel model) {
		this.model = model;
		elements = new ArrayList<GraphElement>();
		elements.addAll(model.getDiagramElements());
		elements.addAll(model.getContainedPackages());
		elementNodeMap = new HashMap<GraphElement, LayoutTreeNode>();
	}
	
	public List<LayoutTree> createTrees(){
		List<LayoutTree> trees = new ArrayList<LayoutTree>();
		for (GraphElement element : elements){
			if (isOnTop((LinkableElement) element)){
				trees.add(new LayoutTree(formRootNode(null, (LinkableElement) element)));
			}
		}
		if (elementNodeMap.size() < elements.size())
			return null;
		return trees;
	}
	
	
	private LayoutTreeNode formRootNode(LayoutTreeNode parentNode, LinkableElement element){
		LayoutTreeNode currentNode;
		if (elementNodeMap.containsKey(element)){
			return elementNodeMap.get(element);
		}
		
		currentNode = new LayoutTreeNode(element, parentNode);
		elementNodeMap.put(element, currentNode);
		Connector otherConnector;
		for (Connector conn : element.getConnectors()){
			if (getConnectorType(conn) == ConnectorType.SOURCE){
				otherConnector = conn.getLink().getDestinationConnector();
				currentNode.addChildNode(formRootNode(currentNode, (LinkableElement) model.getFromElementByConnectorStructure(otherConnector)));
			}
		}
		return currentNode;
	}
	
	private boolean isOnTop(LinkableElement element){
		for (Connector conn : element.getConnectors())
			if (getConnectorType(conn) == ConnectorType.DESTINATION)
				return false;
		return true;
	}
	
	private ConnectorType getConnectorType(Connector connector){
		if (connector.getLink().getSourceConnector() == connector)
			return ConnectorType.SOURCE;
		return ConnectorType.DESTINATION;
	}
	
	
}
