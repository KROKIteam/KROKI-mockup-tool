package ejb_generated;

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
   @Author KROKI Team 
   Creation date: 28.10.2014  14:37:33h
   **/

@Entity
@Table(name = "IA_CITY")
public class City implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "CIT_NAME", unique = false, nullable = false)
	private java.lang.String a_name;
	@Column(name = "CIT_ZIP_CODE", unique = false, nullable = false)
	private java.lang.String a_zip_code;
	@ManyToOne
	@JoinColumn(name="city_state", referencedColumnName="ID",  nullable = true)
	private State city_state;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "department_city")
	private Set<Department> department_citySet;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "enterprise_city")
	private Set<Enterprise> enterprise_citySet;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "vendor_city")
	private Set<Vendor> vendor_citySet;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "order_city")
	private Set<Order> order_citySet;
	
	public City(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getA_name() {
		return this.a_name;
	}
	
	public void setA_name(java.lang.String a_name) {
		this.a_name = a_name;
	}
	
	public java.lang.String getA_zip_code() {
		return this.a_zip_code;
	}
	
	public void setA_zip_code(java.lang.String a_zip_code) {
		this.a_zip_code = a_zip_code;
	}
	
	public State getCity_state() {
		return this.city_state;
	}
	
	public void setCity_state(State city_state) {
		this.city_state = city_state;
	}
	
	public Set<Department> getDepartment_citySet() {
		return this.department_citySet;
	}
	
	public void setDepartment_citySet(Set<Department> department_citySet) {
		this.department_citySet = department_citySet;
	}
	
	public Set<Enterprise> getEnterprise_citySet() {
		return this.enterprise_citySet;
	}
	
	public void setEnterprise_citySet(Set<Enterprise> enterprise_citySet) {
		this.enterprise_citySet = enterprise_citySet;
	}
	
	public Set<Vendor> getVendor_citySet() {
		return this.vendor_citySet;
	}
	
	public void setVendor_citySet(Set<Vendor> vendor_citySet) {
		this.vendor_citySet = vendor_citySet;
	}
	
	public Set<Order> getOrder_citySet() {
		return this.order_citySet;
	}
	
	public void setOrder_citySet(Set<Order> order_citySet) {
		this.order_citySet = order_citySet;
	}
	
}