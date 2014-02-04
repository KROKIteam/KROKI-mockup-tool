package graphedit.layout.tree;

import graphedit.model.components.GraphElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class LayoutTreeNode {
	
	private GraphElement element;
	private LayoutTreeNode parentNode;
	private List<LayoutTreeNode> children;
	
	public LayoutTreeNode(GraphElement element, LayoutTreeNode parentNode) {
		super();
		this.element = element;
		this.parentNode = parentNode;
		children = new ArrayList<LayoutTreeNode>();
	}

	public int getWidth(){
		return (int) ((Dimension)element.getProperty(GraphElementProperties.SIZE)).getWidth();
	}
	
	public int getHeight(){
		return (int) ((Dimension)element.getProperty(GraphElementProperties.SIZE)).getHeight();
	}

	public LayoutTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(LayoutTreeNode parentNode) {
		this.parentNode = parentNode;
	}
	
	public void addChildNode(LayoutTreeNode child){
		children.add(child);
	}
	
	public List<LayoutTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<LayoutTreeNode> children) {
		this.children = children;
	}

	public GraphElement getElement() {
		return element;
	}

	public void setElement(GraphElement element) {
		this.element = element;
	}

}
