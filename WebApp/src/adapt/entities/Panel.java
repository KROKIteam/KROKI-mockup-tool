package adapt.entities;

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
   Creation date: 03.06.2013  14:03:14h
   **/

@Entity
@Table(name = "PRO_PANEL")
public class Panel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "PAN_BOJA", unique = false, nullable = false)
	private java.lang.String boja;
	
	@Column(name = "PAN_IME", unique = false, nullable = false)
	private java.lang.String ime;
	
	
	
	public Panel(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getBoja() {
		return this.boja;
	}
	
	public void setBoja(java.lang.String boja) {
		this.boja = boja;
	}
	
	public java.lang.String getIme() {
		return this.ime;
	}
	
	public void setIme(java.lang.String ime) {
		this.ime = ime;
	}
	
}