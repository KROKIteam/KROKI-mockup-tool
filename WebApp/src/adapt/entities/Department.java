package adapt.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.CascadeType;

import java.util.HashSet;
import java.util.Set;

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
   Creation date: 04.04.2013  14:40:58h
   **/

@Entity
@Table(name = "WS_DEPARTMENT")
public class Department implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "DEP_DEPARTMENT_NAME", unique = false, nullable = false)
	private java.lang.String departmentName;
	
	@Column(name = "DEP_DEPARTMENT_ADDRESS", unique = false, nullable = false)
	private java.lang.String departmentAddress;
	
	
	@ManyToOne
	@JoinColumn(name="city", referencedColumnName="ID",  nullable = false)
	private City city;
	
	
	public Department(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getDepartmentName() {
		return this.departmentName;
	}
	
	public void setDepartmentName(java.lang.String departmentName) {
		this.departmentName = departmentName;
	}
	
	public java.lang.String getDepartmentAddress() {
		return this.departmentAddress;
	}
	
	public void setDepartmentAddress(java.lang.String departmentAddress) {
		this.departmentAddress = departmentAddress;
	}
	
	public City getCity() {
		return this.city;
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	
}