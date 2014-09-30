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
@Table(name = "TES_PANEL2")
public class Panel2 implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@ManyToOne
	@JoinColumn(name="panel2_panel1", referencedColumnName="ID",  nullable = true)
	private Panel1 panel2_panel1;
	@Column(name = "PAN_PROPERTY3", unique = false, nullable = false)
	private java.lang.String property3;
	@Column(name = "PAN_PROPERTY4", unique = false, nullable = false)
	private java.lang.String property4;
	
	public Panel2(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Panel1 getPanel2_panel1() {
		return this.panel2_panel1;
	}
	
	public void setPanel2_panel1(Panel1 panel2_panel1) {
		this.panel2_panel1 = panel2_panel1;
	}
	
	public java.lang.String getProperty3() {
		return this.property3;
	}
	
	public void setProperty3(java.lang.String property3) {
		this.property3 = property3;
	}
	
	public java.lang.String getProperty4() {
		return this.property4;
	}
	
	public void setProperty4(java.lang.String property4) {
		this.property4 = property4;
	}
	
}