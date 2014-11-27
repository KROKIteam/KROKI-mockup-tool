package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Operation")
public class Operation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "operation")
	private Set<Permission> userGroup = new HashSet<Permission>();

	@Column(name = "name", unique = false, nullable = false)
	private String name;

	public Operation() {
	}

	public void addUserGroup(Permission entity) {
		if (entity != null) {
			if (!userGroup.contains(entity)) {
				entity.setOperation(this);
				userGroup.add(entity);
			}
		}
	}

	public void removeUserGroup(Permission entity) {
		if (entity != null) {
			if (userGroup.contains(entity)) {
				entity.setOperation(null);
				userGroup.remove(entity);
			}
		}
	}

	public Set<Permission> getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Set<Permission> userGroup) {
		this.userGroup = userGroup;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		return result.toString();
	}

}
