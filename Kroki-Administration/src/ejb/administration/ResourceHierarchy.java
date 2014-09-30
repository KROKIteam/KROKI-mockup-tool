package ejb.administration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.AbstractEntity;

@Entity
@Table(name = "ResourceHierarchy")
public class ResourceHierarchy extends AbstractEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "resource", referencedColumnName = "id", nullable = false)
	private Resource resource;

	@ManyToOne
	@JoinColumn(name = "subResource", referencedColumnName = "id", nullable = true)
	private Resource subResource;

	@ManyToOne
	@JoinColumn(name = "superResource", referencedColumnName = "id", nullable = true)
	private Resource superResource;

	public ResourceHierarchy() {
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Resource getSubResource() {
		return subResource;
	}

	public void setSubResource(Resource subResource) {
		this.subResource = subResource;
	}

	public Resource getSuperResource() {
		return superResource;
	}

	public void setSuperResource(Resource superResource) {
		this.superResource = superResource;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Object[] getValues() {
		List<Object> list = new ArrayList<Object>();

		list.add(id);
		if (resource != null) {
			list.add(resource.toString());
		} else {
			list.add("");
		}
		if (subResource != null) {
			list.add(subResource.toString());
		} else {
			list.add("");
		}
		if (superResource != null) {
			list.add(superResource.toString());
		} else {
			list.add("");
		}

		return list.toArray();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		return result.toString();
	}

}
