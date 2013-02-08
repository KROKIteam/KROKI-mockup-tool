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
   Creation date: 08.02.2013  15:31:10h
   **/

@Entity
@Table(name = "PREDUZECE")
public class Preduzece implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "PR_PIB", unique = false, nullable = false)
	private java.lang.String pib;
	
	@Column(name = "PR_NAZIV", unique = false, nullable = false)
	private java.lang.String naziv;
	
	@Column(name = "PR_ADRESA", unique = false, nullable = false)
	private java.lang.String adresa;
	
	@Column(name = "PR_REG_BROJ", unique = false, nullable = false)
	private java.lang.String registarskiBroj;
	
	
	@ManyToOne
	@JoinColumn(name="drzava", referencedColumnName="ID", nullable=false)
	private Drzava drzava;
	
	@ManyToOne
	@JoinColumn(name="naseljenoMesto", referencedColumnName="ID", nullable=false)
	private NaseljenoMesto naseljenoMesto;
	
	
	public Preduzece(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getPib() {
		return this.pib;
	}
	
	public void setPib(java.lang.String pib) {
		this.pib = pib;
	}
	
	public java.lang.String getNaziv() {
		return this.naziv;
	}
	
	public void setNaziv(java.lang.String naziv) {
		this.naziv = naziv;
	}
	
	public java.lang.String getAdresa() {
		return this.adresa;
	}
	
	public void setAdresa(java.lang.String adresa) {
		this.adresa = adresa;
	}
	
	public java.lang.String getRegistarskiBroj() {
		return this.registarskiBroj;
	}
	
	public void setRegistarskiBroj(java.lang.String registarskiBroj) {
		this.registarskiBroj = registarskiBroj;
	}
	
	public Drzava getDrzava() {
		return this.drzava;
	}
	
	public void setDrzava(Drzava drzava) {
		this.drzava = drzava;
	}
	
	public NaseljenoMesto getNaseljenoMesto() {
		return this.naseljenoMesto;
	}
	
	public void setNaseljenoMesto(NaseljenoMesto naseljenoMesto) {
		this.naseljenoMesto = naseljenoMesto;
	}
	
}