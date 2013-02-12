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
@Table(name = "NASELJENOMESTO")
public class NaseljenoMesto implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "NM_PTT_OZNAKA", unique = false, nullable = false)
	private java.lang.String pttOznaka;
	
	@Column(name = "NM_NAZIV", unique = false, nullable = false)
	private java.lang.String naziv;
	
	
	@ManyToOne
	@JoinColumn(name="drzava", referencedColumnName="ID", nullable=true)
	private Drzava drzava;
	
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "naseljenoMesto")
	private Set<Preduzece> preduzeceSet = new HashSet<Preduzece>();
	
	public NaseljenoMesto(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getPttOznaka() {
		return this.pttOznaka;
	}
	
	public void setPttOznaka(java.lang.String pttOznaka) {
		this.pttOznaka = pttOznaka;
	}
	
	public java.lang.String getNaziv() {
		return this.naziv;
	}
	
	public void setNaziv(java.lang.String naziv) {
		this.naziv = naziv;
	}
	
	public Drzava getDrzava() {
		return this.drzava;
	}
	
	public void setDrzava(Drzava drzava) {
		this.drzava = drzava;
	}
	
	public Set<Preduzece> getPreduzeceSet() {
		return this.preduzeceSet;
	}

	public void setPreduzeceSet(Set<Preduzece> preduzeceSet) {
		this.preduzeceSet = preduzeceSet;
	}
	
}