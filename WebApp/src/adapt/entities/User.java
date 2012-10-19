package adapt.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.CascadeType;

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
   Class generated using EJBGeneratorAspect 
   @Author mrd 
   Creation date: 20.06.2012  15:13:11:993h
   **/

@Entity
@Table(name = "USER")
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "US_USERNAME", unique = true, nullable = false)
	private String name;
	
	@Column(name = "US_PASSWORD", unique = false, nullable = false)
	private String password;
	
	
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "user")
	private Set<UserRights> rights;
	
	public User(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Set<UserRights> getRights() {
		return this.rights;
	}

	public void setRights(Set<UserRights> rights) {
		this.rights = rights;
	}
	
}