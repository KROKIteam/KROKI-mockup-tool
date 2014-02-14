package graphedit.model.elements;

import java.io.Serializable;

import kroki.profil.association.Hierarchy;

public class HierarchyElement extends AbstractLinkElement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hierarchy hierarchy;
	private int classIndex;
	private int groupIndex;
	
	public HierarchyElement(Hierarchy hierarchy, int classIndex, int groupIndex) {
		super();
		this.hierarchy = hierarchy;
		this.classIndex = classIndex;
		this.groupIndex = groupIndex;
	}


	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public int getClassIndex() {
		return classIndex;
	}

	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}


	public int getGroupIndex() {
		return groupIndex;
	}


	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}
	
	
}
	
