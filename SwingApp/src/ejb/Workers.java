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
   Creation date: 12.02.2013  15:53:42h
   **/

@Entity
@Table(name = "WORKERS")
public class Workers implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "WOR_FIRST_NAME", unique = false, nullable = false)
	private java.lang.String firstName;
	
	@Column(name = "WOR_LAST_NAME", unique = false, nullable = false)
	private java.lang.String lastName;
	
	@Column(name = "WOR_ADDRESS", unique = false, nullable = false)
	private java.lang.String address;
	
	@Column(name = "WOR_MARRIED", unique = false, nullable = false)
	private java.lang.Boolean married;
	
	
	@ManyToOne
	@JoinColumn(name="cities", referencedColumnName="ID", nullable=true)
	private Cities cities;
	
	
	public Workers(){
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
	
	public java.lang.Boolean getMarried() {
		return this.married;
	}
	
	public void setMarried(java.lang.Boolean married) {
		this.married = married;
	}
	
	public Cities getCities() {
		return this.cities;
	}
	
	public void setCities(Cities cities) {
		this.cities = cities;
	}
	
}