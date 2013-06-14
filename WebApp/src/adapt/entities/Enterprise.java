package adapt.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.CascadeType;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;

   /** 
   Class generated using Kroki EJBGenerator 
   @Author mrd 
   Creation date: 14.06.2013  11:57:05h
   **/

@Entity
@Table(name = "WS_ENTERPRISE")
public class Enterprise implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "ENT_NAME", unique = false, nullable = false)
	private java.lang.String name;
	@Column(name = "ENT_ADDRESS", unique = false, nullable = false)
	private java.lang.String address;
	@ManyToOne
	@JoinColumn(name="enterprise_city", referencedColumnName="ID",  nullable = true)
	private City enterprise_city;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "department_enterprise")
	private Set<Department> DepartmentSet;
	
	public Enterprise(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	public java.lang.String getAddress() {
		return this.address;
	}
	
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	
	public City getEnterprise_city() {
		return this.enterprise_city;
	}
	
	public void setEnterprise_city(City enterprise_city) {
		this.enterprise_city = enterprise_city;
	}
	
	public Set<Department> getDepartmentSet() {
		return this.DepartmentSet;
	}
	
	public void setDepartmentSet(Set<Department> DepartmentSet) {
		this.DepartmentSet = DepartmentSet;
	}
	
}