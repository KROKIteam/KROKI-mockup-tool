package adapt.entities.generated;

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
   Creation date: 14.04.2016  16:46:13h
   **/

@Entity
@Table(name = "TRE_YRTYTR")
public class Yrtytr implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "YRT_TEXT_FIELD_1", unique = false, nullable = false)
	private java.math.BigDecimal a_text_field_1;
	
	public Yrtytr(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.math.BigDecimal getA_text_field_1() {
		return this.a_text_field_1;
	}
	
	public void setA_text_field_1(java.math.BigDecimal a_text_field_1) {
		this.a_text_field_1 = a_text_field_1;
	}
	
}