package adapt.model.ejb;

import java.util.ArrayList;
import java.util.List;

import adapt.model.AbstractElement;

public class EntityBean extends AbstractElement {
	
	protected List<AbstractAttribute> attributes = new ArrayList<AbstractAttribute>();
	protected Class<?> entityClass;

	public EntityBean() {
	}

	public EntityBean(String name, String label) {
		super(name, label);
	}

	public void add(AbstractAttribute attribute) {
		attributes.add(attribute);
	}

	public void remove(AbstractAttribute attribute) {
		attributes.remove(attribute);
	}

	public List<AbstractAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AbstractAttribute> attributes) {
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
