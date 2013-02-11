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
   Creation date: 11.02.2013  15:14:04h
   **/

@Entity
@Table(name = "NJANJALICA")
public class Njanjalica implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "NJA_NAZIV", unique = false, nullable = false)
	private java.lang.String naziv;
	
	@Column(name = "NJA_MOZE", unique = false, nullable = false)
	private java.lang.Boolean moze;
	
	
	
	public Njanjalica(){
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
	
	public java.lang.Boolean getMoze() {
		return this.moze;
	}
	
	public void setMoze(java.lang.Boolean moze) {
		this.moze = moze;
	}
	
}