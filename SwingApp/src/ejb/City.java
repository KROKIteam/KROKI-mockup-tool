package ejb;

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
   Creation date: 14.06.2013  13:02:06h
   **/

@Entity
@Table(name = "WS_CITY")
public class City implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "CIT_NAME", unique = false, nullable = false)
	private java.lang.String name;
	@Column(name = "CIT_ZIP_CODE", unique = false, nullable = false)
	private java.lang.String zipCode;
	@ManyToOne
	@JoinColumn(name="city_state", referencedColumnName="ID",  nullable = true)
	private State city_state;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "enterprise_city")
	private Set<Enterprise> EnterpriseSet;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "customer_city")
	private Set<Customer> CustomerSet;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "vendor_city")
	private Set<Vendor> VendorSet;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "order_city")
	private Set<Order> OrderSet;
	
	public City(){
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
	
	public java.lang.String getZipCode() {
		return this.zipCode;
	}
	
	public void setZipCode(java.lang.String zipCode) {
		this.zipCode = zipCode;
	}
	
	public State getCity_state() {
		return this.city_state;
	}
	
	public void setCity_state(State city_state) {
		this.city_state = city_state;
	}
	
	public Set<Enterprise> getEnterpriseSet() {
		return this.EnterpriseSet;
	}
	
	public void setEnterpriseSet(Set<Enterprise> EnterpriseSet) {
		this.EnterpriseSet = EnterpriseSet;
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
	
	public Set<Order> getOrderSet() {
		return this.OrderSet;
	}
	
	public void setOrderSet(Set<Order> OrderSet) {
		this.OrderSet = OrderSet;
	}
	
}