package com.panelcomposer.model.enumeration;

import java.util.ArrayList;
import java.util.List;

import com.panelcomposer.model.AbstractElement;


public class Enumeration extends AbstractElement {
	
	protected List<String> labels;
	
	public Enumeration() {
		labels = new ArrayList<String>();
	}
	
	public Enumeration(String name, String label) {
		super(name, label);
		labels = new ArrayList<String>();
	}
	
	public void add(String label) {
		labels.add(label);
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

}
