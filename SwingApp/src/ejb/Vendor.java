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
   Creation date: 19.02.2013  10:44:27h
   **/

@Entity
@Table(name = "TC_VENDOR")
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
	@JoinColumn(name="city", referencedColumnName="ID", nullable=true)
	private City city;
	
	
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
	
	public City getCity() {
		return this.city;
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	
}