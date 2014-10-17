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
   Creation date: 17.10.2014  13:40:18h
   **/

@Entity
@Table(name = "SIA_CITY")
public class City implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "CIT_NAME", unique = false, nullable = false)
	private java.lang.String a_name;
	@ManyToOne
	@JoinColumn(name="city_state", referencedColumnName="ID",  nullable = true)
	private State city_state;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "employee_city")
	private Set<Employee> employee_citySet;
	
	public City(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getA_name() {
		return this.a_name;
	}
	
	public void setA_name(java.lang.String a_name) {
		this.a_name = a_name;
	}
	
	public State getCity_state() {
		return this.city_state;
	}
	
	public void setCity_state(State city_state) {
		this.city_state = city_state;
	}
	
	public Set<Employee> getEmployee_citySet() {
		return this.employee_citySet;
	}
	
	public void setEmployee_citySet(Set<Employee> employee_citySet) {
		this.employee_citySet = employee_citySet;
	}
	
}