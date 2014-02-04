package graphedit.view;

import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.diagram.GraphEditModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class SelectionModel extends Observable {

	private GraphEditModel model;
	private List<GraphElement> selectedElements;
	private Link selectedLink;
	private LinkNode selectedNode;

	public SelectionModel(GraphEditModel model) {
		this.model = model;
		selectedElements = new ArrayList<GraphElement>();
	}

	public void inverseSelection() {
		if (model != null && model.getDiagramElements().size() > 0) {
			for (GraphElement element : model.getDiagramElements()) {
				if (selectedElements.contains(element)) {
					selectedElements.remove(element);
				} else {
					selectedElements.add(element);
				}
			}
		}
		fireUpdates();
	}

	public void selectAllElements() {
		if (model != null && model.getDiagramElements().size() > 0) {
			for (GraphElement element : model.getDiagramElements()) {
				if (!selectedElements.contains(element)) {
					selectedElements.add(element);
				}
			}
		}
		fireUpdates();
	}

	public List<GraphElement> getSelectedElements() {
		if (selectedElements == null)
			selectedElements = new ArrayList<GraphElement>();
		return selectedElements;
	}

	public Iterator<GraphElement> getIteratorSelectedElements() {
		if (selectedElements == null)
			selectedElements = new ArrayList<GraphElement>();
		return selectedElements.iterator();
	}

	public void setSelectedElements(List<GraphElement> newSelectedElements) {
		removeAllSelectedElements();
		for (Iterator<GraphElement> iter = newSelectedElements.iterator(); iter
				.hasNext();)
			addSelectedElement(iter.next());
		fireUpdates();
	}

	public GraphEditModel getModel() {
		return model;
	}

	public void addSelectedElement(GraphElement newGraphElement) {
		if (newGraphElement == null)
			return;
		if (this.selectedElements == null)
			this.selectedElements = new ArrayList<GraphElement>();
		if (!this.selectedElements.contains(newGraphElement))
			this.selectedElements.add(newGraphElement);
		fireUpdates();
	}

	public void removeSelectedElement(GraphElement oldGraphElement) {
		if (oldGraphElement == null)
			return;
		if (this.selectedElements != null)
			if (this.selectedElements.contains(oldGraphElement))
				this.selectedElements.remove(oldGraphElement);
		fireUpdates();
	}

	public void removeAllSelectedElements() {
		if (selectedElements != null)
			selectedElements.clear();
		fireUpdates();
	}

	public Link getSelectedLink() {
		return selectedLink;
	}

	public void setSelectedLink(Link selectedLink) {
		this.selectedLink = selectedLink;
		fireUpdates();
	}

	public void removeSelecredLink(Link link){
		if (link==selectedLink)
			selectedLink=null;
		fireUpdates();
	}
	public void fireUpdates() {
		//System.out.println("Fire updates!");
		this.setChanged();
		this.notifyObservers();
	}

	// Igor: Aded new convenience method in order to ease Clipboard management
	public void removeSelectedElements(List<GraphElement> elements) {
		if (elements!=null){
			selectedElements.removeAll(elements);
			fireUpdates();	
		}
	}

	public void addSelectedElements(List<GraphElement> elements) {
		if (elements!=null){
			selectedElements.addAll(elements);
			fireUpdates();
		}
	}

	public LinkNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(LinkNode selectedNode) {
		this.selectedNode = selectedNode;
	}

}