package graphedit.view;

import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.components.Package;
import graphedit.model.components.shortcuts.Shortcut;
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
	private List<GraphElement> packageList, shortcutsList;


	public SelectionModel(GraphEditModel model) {
		this.model = model;
		selectedElements = new ArrayList<GraphElement>();
		packageList = new ArrayList<GraphElement>();
		shortcutsList = new ArrayList<GraphElement>();
	}

	public void inverseSelection() {
		List<GraphElement> allElements = new ArrayList<GraphElement>();
		allElements.addAll(model.getDiagramElements());
		allElements.addAll(model.getContainedPackages());
		if (model != null && allElements.size() > 0) {
			for (GraphElement element : allElements) {
				if (selectedElements.contains(element)) {
					selectedElements.remove(element);
				} else {
					if(element instanceof Package)
						packageList.add(element);
					else if (element instanceof Shortcut)
						shortcutsList.add(element);
					selectedElements.add(element);
				}
			}
		}
		fireUpdates();
	}

	public void selectAllElements() {
		List<GraphElement> allElements = new ArrayList<GraphElement>();
		allElements.addAll(model.getDiagramElements());
		allElements.addAll(model.getContainedPackages());
		if (model != null && allElements.size() > 0) {
			for (GraphElement element : allElements) {
				if (!selectedElements.contains(element)) {
					if(element instanceof Package)
						packageList.add(element);
					else if (element instanceof Shortcut)
						shortcutsList.add(element);
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
		if (!this.selectedElements.contains(newGraphElement)){
			if(newGraphElement instanceof Package)
				packageList.add(newGraphElement);
			else if (newGraphElement instanceof Shortcut)
				shortcutsList.add(newGraphElement);
			this.selectedElements.add(newGraphElement);
		}
		fireUpdates();
	}

	public void removeSelectedElement(GraphElement oldGraphElement) {
		if (oldGraphElement == null)
			return;
		if (this.selectedElements != null)
			if (this.selectedElements.contains(oldGraphElement)){
				if(oldGraphElement instanceof Package)
					packageList.add(oldGraphElement);
				else if (oldGraphElement instanceof Shortcut)
					shortcutsList.add(oldGraphElement);
				this.selectedElements.remove(oldGraphElement);
			}
		fireUpdates();
	}

	public void clearSelection(){
		removeAllSelectedElements();
		removeSelecredLink(selectedLink);
	}
	
	public void removeAllSelectedElements() {
		if (selectedElements != null)
			selectedElements.clear();
		packageList.clear();
		shortcutsList.clear();
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
		this.setChanged();
		this.notifyObservers();
	}



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


	public boolean hasPackage(){
		return packageList.size() > 0;
	}

	public boolean hasShortcut(){
		return shortcutsList.size() > 0;
	}
}