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
   Creation date: 28.01.2013  12:53:58h
   **/

@Entity
@Table(name = "WORKERS")
public class Workers implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "WRK_FIRST_NAME", unique = false, nullable = false)
	private java.lang.String name;
	
	@Column(name = "WRK_LAST_NAME", unique = false, nullable = false)
	private java.lang.String lastName;
	
	@Column(name = "WRK_ADDRESS", unique = false, nullable = false)
	private java.lang.String address;
	
	@Column(name = "WRK_MARRIED", unique = false, nullable = false)
	private java.lang.Boolean married;
	
	
	
	public Workers(){
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
	
}