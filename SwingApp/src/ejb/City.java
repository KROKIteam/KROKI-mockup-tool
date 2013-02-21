package ejb;

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
   Creation date: 21.02.2013  15:40:51h
   **/

@Entity
@Table(name = "WS_CITY")
public class City implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "CIT_ZIP_CODE", unique = false, nullable = false)
	private java.lang.String zipCode;
	
	@Column(name = "CIT_NAME", unique = false, nullable = false)
	private java.lang.String name;
	
	
	@ManyToOne
	@JoinColumn(name="state", referencedColumnName="ID", nullable=true)
	private State state;
	
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "city")
	private Set<Enterprise> EnterpriseSet = new HashSet<Enterprise>();
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "city")
	private Set<Department> DepartmentSet = new HashSet<Department>();
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "city")
	private Set<Customer> CustomerSet = new HashSet<Customer>();
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "city")
	private Set<Vendor> VendorSet = new HashSet<Vendor>();
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "city")
	private Set<Orders> OrdersSet = new HashSet<Orders>();
	
	public City(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getZipCode() {
		return this.zipCode;
	}
	
	public void setZipCode(java.lang.String zipCode) {
		this.zipCode = zipCode;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	public State getState() {
		return this.state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public Set<Enterprise> getEnterpriseSet() {
		return this.EnterpriseSet;
	}

	public void setEnterpriseSet(Set<Enterprise> EnterpriseSet) {
		this.EnterpriseSet = EnterpriseSet;
	}
	
	public Set<Department> getDepartmentSet() {
		return this.DepartmentSet;
	}

	public void setDepartmentSet(Set<Department> DepartmentSet) {
		this.DepartmentSet = DepartmentSet;
	}
	
	public Set<Customer> getCustomerSet() {
		return this.CustomerSet;
	}

	public void setCustomerSet(Set<Customer> CustomerSet) {
		this.CustomerSet = CustomerSet;
	}
	
	public Set<Vendor> getVendorSet() {
		return this.VendorSet;
	}

	public void setVendorSet(Set<Vendor> VendorSet) {
		this.VendorSet = VendorSet;
	}
	
	public Set<Orders> getOrdersSet() {
		return this.OrdersSet;
	}

	public void setOrdersSet(Set<Orders> OrdersSet) {
		this.OrdersSet = OrdersSet;
	}
	
}