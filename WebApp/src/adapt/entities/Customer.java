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
   Creation date: 06.06.2013  15:10:54h
   **/

@Entity
@Table(name = "WS_CUSTOMER")
public class Customer implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "CUS_FIRST_NAME", unique = false, nullable = false)
	private java.lang.String firstName;
	
	@Column(name = "CUS_LAST_NAME", unique = false, nullable = false)
	private java.lang.String lastName;
	
	@Column(name = "CUS_ADDRESS", unique = false, nullable = false)
	private java.lang.String address;
	
	@ManyToOne
	@JoinColumn(name="city", referencedColumnName="ID",  nullable = true)
	private City city;
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "customer")
	private Set<Order> OrderSet;
	
	
	public Customer(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}
	
	public java.lang.String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}
	
	public java.lang.String getAddress() {
		return this.address;
	}
	
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	
	public City getCity() {
		return this.city;
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	
	public Set<Order> getOrderSet() {
		return this.OrderSet;
	}
	
	public void setOrderSet(Set<Order> OrderSet) {
		this.OrderSet = OrderSet;
	}
	
}