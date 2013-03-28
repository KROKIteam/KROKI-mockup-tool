package adapt.entities;

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
   Creation date: 28.03.2013  12:21:09h
   **/

@Entity
@Table(name = "ADAPT_USER_RIGHTS")
public class UserRights implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "UR_ALLOWED", unique = false, nullable = false)
	private java.lang.Boolean allowed;
	
	
	@ManyToOne
	@JoinColumn(name="user", referencedColumnName="ID", nullable=true)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="action", referencedColumnName="ID", nullable=true)
	private Action action;
	
	@ManyToOne
	@JoinColumn(name="resource", referencedColumnName="ID", nullable=true)
	private Resource resource;
	
	
	public UserRights(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.Boolean getAllowed() {
		return this.allowed;
	}
	
	public void setAllowed(java.lang.Boolean allowed) {
		this.allowed = allowed;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public Resource getResource() {
		return this.resource;
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
}