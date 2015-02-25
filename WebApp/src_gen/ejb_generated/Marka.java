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
   Creation date: 25.02.2015  14:17:41h
   **/

@Entity
@Table(name = "PS_MARKA")
public class Marka implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "MAR_NAZIV", unique = false, nullable = false)
	private java.lang.String a_naziv;
	@Column(name = "MAR_ZEMLJA_POREKLA", unique = false, nullable = false)
	private java.lang.String a_zemlja_porekla;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "automobil_marka")
	private Set<Automobil> automobil_markaSet;
	
	public Marka(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getA_naziv() {
		return this.a_naziv;
	}
	
	public void setA_naziv(java.lang.String a_naziv) {
		this.a_naziv = a_naziv;
	}
	
	public java.lang.String getA_zemlja_porekla() {
		return this.a_zemlja_porekla;
	}
	
	public void setA_zemlja_porekla(java.lang.String a_zemlja_porekla) {
		this.a_zemlja_porekla = a_zemlja_porekla;
	}
	
	public Set<Automobil> getAutomobil_markaSet() {
		return this.automobil_markaSet;
	}
	
	public void setAutomobil_markaSet(Set<Automobil> automobil_markaSet) {
		this.automobil_markaSet = automobil_markaSet;
	}
	
}