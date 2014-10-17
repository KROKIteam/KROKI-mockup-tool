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
@Table(name = "SIA_EMPLOYEE")
public class Employee implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "EMP_FIRST_NAME", unique = false, nullable = false)
	private java.lang.String a_first_name;
	@Column(name = "EMP_LAST_NAME", unique = false, nullable = false)
	private java.lang.String a_last_name;
	@Column(name = "EMP_ADDRESS", unique = false, nullable = false)
	private java.lang.String a_address;
	@ManyToOne
	@JoinColumn(name="employee_city", referencedColumnName="ID",  nullable = true)
	private City employee_city;
	@Column(name = "EMP_GENDER", unique = false, nullable = false)
	private java.lang.String a_gender;
	
	public Employee(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getA_first_name() {
		return this.a_first_name;
	}
	
	public void setA_first_name(java.lang.String a_first_name) {
		this.a_first_name = a_first_name;
	}
	
	public java.lang.String getA_last_name() {
		return this.a_last_name;
	}
	
	public void setA_last_name(java.lang.String a_last_name) {
		this.a_last_name = a_last_name;
	}
	
	public java.lang.String getA_address() {
		return this.a_address;
	}
	
	public void setA_address(java.lang.String a_address) {
		this.a_address = a_address;
	}
	
	public City getEmployee_city() {
		return this.employee_city;
	}
	
	public void setEmployee_city(City employee_city) {
		this.employee_city = employee_city;
	}
	
	public java.lang.String getA_gender() {
		return this.a_gender;
	}
	
	public void setA_gender(java.lang.String a_gender) {
		this.a_gender = a_gender;
	}
	
}