package com.panelcomposer.model.ejb;

import java.util.ArrayList;
import java.util.List;

import com.panelcomposer.model.AbstractElement;
import com.panelcomposer.model.attribute.AbsAttribute;


public class EntityBean extends AbstractElement {

	protected List<AbsAttribute> attributes = new ArrayList<AbsAttribute>();
	protected Class<?> entityClass;

	public EntityBean() {
	}

	public EntityBean(String name, String label) {
		super(name, label);
	}

	public void add(AbsAttribute attribute) {
		attributes.add(attribute);
	}

	public void remove(AbsAttribute attribute) {
		attributes.remove(attribute);
	}

	public List<AbsAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AbsAttribute> attributes) {
		this.attributes = attributes;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> ejbClass) {
		this.entityClass = ejbClass;
	}

	@Override
	public String toString() {
		return "EntityBean [entityClass=" + entityClass + "]";
	}

}
