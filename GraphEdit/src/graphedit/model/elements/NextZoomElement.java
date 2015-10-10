package graphedit.model.elements;

import kroki.profil.VisibleElement;

public class NextZoomElement extends AbstractLinkElement{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UIClassElement targetElement;
	private int classIndex;
	private int groupIndex;
	private String label;
	private String cardinality;
	private VisibleElement visibleElement;
	
	
	public NextZoomElement(UIClassElement targetElement, int classIndex,
			int groupIndex, String label, String cardinality,  VisibleElement visibleElement) {
		super();
		this.targetElement = targetElement;
		this.classIndex = classIndex;
		this.groupIndex = groupIndex;
		this.label = label;
		this.cardinality = cardinality;
		this.visibleElement = visibleElement;
	}


	public UIClassElement getTargetElement() {
		return targetElement;
	}


	public void setTargetElement(UIClassElement targetElement) {
		this.targetElement = targetElement;
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


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getCardinality() {
		return cardinality;
	}


	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}


	public VisibleElement getVisibleElement() {
		return visibleElement;
	}


	public void setVisibleElement(VisibleElement visibleElement) {
		this.visibleElement = visibleElement;
	}
	
	
	
	
	
	

}
