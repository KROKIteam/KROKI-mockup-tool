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
   Creation date: 05.06.2013  14:39:53h
   **/

@Entity
@Table(name = "PRO_CAR")
public class Car implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "CAR_MODEL", unique = false, nullable = false)
	private java.lang.String model;
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "car")
	private Set<Owner> OwnerSet;
	
	
	public Car(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getModel() {
		return this.model;
	}
	
	public void setModel(java.lang.String model) {
		this.model = model;
	}
	
	public Set<Owner> getOwnerSet() {
		return this.OwnerSet;
	}
	
	public void setOwnerSet(Set<Owner> OwnerSet) {
		this.OwnerSet = OwnerSet;
	}
	
}