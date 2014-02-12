package adapt.utils;

import java.util.ArrayList;

public class XMLResource {
	
	private String name;
	private String label;
	private String link;
	private Boolean routed;
	private ArrayList<XMLForm> forms;
	private ArrayList<XMLAttribute> attributes;
	private ArrayList<XMLManyToOneAttribute> manyToOneAttributes;
	private ArrayList<XMLOneToManyAttribute> oneToManyAttributes;
	private ArrayList<XMLManyToManyAttribute> manyToManyAttributes;
	
	public XMLResource(String name, String label, String link, Boolean routed,
			ArrayList<XMLForm> forms, ArrayList<XMLAttribute> attributes,
			ArrayList<XMLManyToOneAttribute> manyToOneAttributes,
			ArrayList<XMLOneToManyAttribute> oneToManyAttributes,
			ArrayList<XMLManyToManyAttribute> manyToManyAttributes) {
		super();
		this.name = name;
		this.label = label;
		this.link = link;
		this.routed = routed;
		this.forms = forms;
		this.attributes = attributes;
		this.manyToOneAttributes = manyToOneAttributes;
		this.oneToManyAttributes = oneToManyAttributes;
		this.manyToManyAttributes = manyToManyAttributes;
	}

	public ArrayList<XMLAttribute> getRepresentativeAttributes() {
		ArrayList<XMLAttribute> repAttributes = new ArrayList<XMLAttribute>();
		
		for (XMLAttribute attribute : attributes) {
			if(attribute.getRepresentative() || attribute.getName().equalsIgnoreCase("name")) {
				repAttributes.add(attribute);
			}
		}
		
		return repAttributes;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Boolean getRouted() {
		return routed;
	}

	public void setRouted(Boolean routed) {
		this.routed = routed;
	}

	public ArrayList<XMLForm> getForms() {
		return forms;
	}

	public void setForms(ArrayList<XMLForm> forms) {
		this.forms = forms;
	}

	public ArrayList<XMLAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<XMLAttribute> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<XMLManyToOneAttribute> getManyToOneAttributes() {
		return manyToOneAttributes;
	}

	public void setManyToOneAttributes(
			ArrayList<XMLManyToOneAttribute> manyToOneAttributes) {
		this.manyToOneAttributes = manyToOneAttributes;
	}

	public ArrayList<XMLOneToManyAttribute> getOneToManyAttributes() {
		return oneToManyAttributes;
	}

	public void setOneToManyAttributes(
			ArrayList<XMLOneToManyAttribute> oneToManyAttributes) {
		this.oneToManyAttributes = oneToManyAttributes;
	}

	public ArrayList<XMLManyToManyAttribute> getManyToManyAttributes() {
		return manyToManyAttributes;
	}

	public void setManyToManyAttributes(
			ArrayList<XMLManyToManyAttribute> manyToManyAttributes) {
		this.manyToManyAttributes = manyToManyAttributes;
	}
}
