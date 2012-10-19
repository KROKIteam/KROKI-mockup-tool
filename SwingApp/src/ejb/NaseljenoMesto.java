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
   Creation date: 11.09.2012  12:00:02h
   **/

@Entity
@Table(name = "NASELJENOMESTO")
public class NaseljenoMesto implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "NM_NAZIV", unique = false, nullable = false)
	private java.lang.String naziv;
	
	@Column(name = "NM_PTT", unique = false, nullable = false)
	private java.lang.String pttBroj;
	
	
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "naseljenoMesto")
	private Set<Radnik> radnikSet = new HashSet<Radnik>();
	
	public NaseljenoMesto(){
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
	
	public java.lang.String getPttBroj() {
		return this.pttBroj;
	}
	
	public void setPttBroj(java.lang.String pttBroj) {
		this.pttBroj = pttBroj;
	}
	
	public Set<Radnik> getRadnikSet() {
		return this.radnikSet;
	}

	public void setRadnikSet(Set<Radnik> radnikSet) {
		this.radnikSet = radnikSet;
	}
	
}