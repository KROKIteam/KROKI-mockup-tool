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
@Table(name = "Adapt_Operation")
public class AdaptOperation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "operation")
	private Set<AdaptPermission> userGroup = new HashSet<AdaptPermission>();

	@Column(name = "name", unique = false, nullable = false)
	private String name;

	public AdaptOperation() {
	}

	public void addUserGroup(AdaptPermission entity) {
		if (entity != null) {
			if (!userGroup.contains(entity)) {
				entity.setOperation(this);
				userGroup.add(entity);
			}
		}
	}

	public void removeUserGroup(AdaptPermission entity) {
		if (entity != null) {
			if (userGroup.contains(entity)) {
				entity.setOperation(null);
				userGroup.remove(entity);
			}
		}
	}

	public Set<AdaptPermission> getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Set<AdaptPermission> userGroup) {
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
