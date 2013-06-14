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
@Table(name = "WS_VENDOR")
public class Vendor implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "VEN_NAME", unique = false, nullable = false)
	private java.lang.String name;
	@Column(name = "VEN_ADDRESS", unique = false, nullable = false)
	private java.lang.String address;
	@ManyToOne
	@JoinColumn(name="vendor_city", referencedColumnName="ID",  nullable = true)
	private City vendor_city;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "product_vendor")
	private Set<Product> ProductSet;
	
	public Vendor(){
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
	
	public City getVendor_city() {
		return this.vendor_city;
	}
	
	public void setVendor_city(City vendor_city) {
		this.vendor_city = vendor_city;
	}
	
	public Set<Product> getProductSet() {
		return this.ProductSet;
	}
	
	public void setProductSet(Set<Product> ProductSet) {
		this.ProductSet = ProductSet;
	}
	
}