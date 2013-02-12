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
   Creation date: 12.02.2013  13:09:01h
   **/

@Entity
@Table(name = "DRZAVA")
public class Drzava implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "DR_NAZIV", unique = false, nullable = false)
	private java.lang.String naziv;
	
	@Column(name = "DR_VAZECA", unique = false, nullable = false)
	private java.lang.Boolean vazeca;
	
	
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "drzava")
	private Set<NaseljenoMesto> naseljenoMestoSet = new HashSet<NaseljenoMesto>();
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "drzava")
	private Set<Preduzece> preduzeceSet = new HashSet<Preduzece>();
	
	public Drzava(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getNaziv() {
		return this.naziv;
	}
	
	public void setNaziv(java.lang.String naziv) {
		this.naziv = naziv;
	}
	
	public java.lang.Boolean getVazeca() {
		return this.vazeca;
	}
	
	public void setVazeca(java.lang.Boolean vazeca) {
		this.vazeca = vazeca;
	}
	
	public Set<NaseljenoMesto> getNaseljenoMestoSet() {
		return this.naseljenoMestoSet;
	}

	public void setNaseljenoMestoSet(Set<NaseljenoMesto> naseljenoMestoSet) {
		this.naseljenoMestoSet = naseljenoMestoSet;
	}
	
	public Set<Preduzece> getPreduzeceSet() {
		return this.preduzeceSet;
	}

	public void setPreduzeceSet(Set<Preduzece> preduzeceSet) {
		this.preduzeceSet = preduzeceSet;
	}
	
}