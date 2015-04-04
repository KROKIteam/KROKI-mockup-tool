package adapt.util.html;

import java.util.ArrayList;

import adapt.model.AbstractElement;

public class OperationGroup {
	
	private String name;
	private ArrayList<AbstractElement> elements;
	
	public OperationGroup(String name, ArrayList<AbstractElement> elements) {
		super();
		this.name = name;
		this.elements = elements;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<AbstractElement> getElements() {
		return elements;
	}

	public void setElements(ArrayList<AbstractElement> elements) {
		this.elements = elements;
	}
}
