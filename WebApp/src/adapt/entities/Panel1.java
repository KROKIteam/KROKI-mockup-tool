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
   @Author KROKI Team 
   Creation date: 07.07.2014  9:31:19h
   **/

@Entity
@Table(name = "TES_PANEL1")
public class Panel1 implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "PAN_PROPERTY1", unique = false, nullable = false)
	private java.lang.String property1;
	@Column(name = "PAN_PROPERTY2", unique = false, nullable = false)
	private java.lang.String property2;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "panel2_panel1")
	private Set<Panel2> Panel2Set;
	
	public Panel1(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getProperty1() {
		return this.property1;
	}
	
	public void setProperty1(java.lang.String property1) {
		this.property1 = property1;
	}
	
	public java.lang.String getProperty2() {
		return this.property2;
	}
	
	public void setProperty2(java.lang.String property2) {
		this.property2 = property2;
	}
	
	public Set<Panel2> getPanel2Set() {
		return this.Panel2Set;
	}
	
	public void setPanel2Set(Set<Panel2> Panel2Set) {
		this.Panel2Set = Panel2Set;
	}
	
}