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
@Table(name = "USERRIGHTS")
public class UserRights implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "UR_ALLOWED", unique = false, nullable = false)
	private Boolean allowed;
	
	
	@ManyToOne
	@JoinColumn(name="UR_USER", referencedColumnName="ID", nullable=false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="UR_ACTION", referencedColumnName="ID", nullable=false)
	private Action action;
	
	@ManyToOne
	@JoinColumn(name="UR_RESOURCE", referencedColumnName="ID", nullable=false)
	private Resource resource;
	
	
	public UserRights(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Boolean getAllowed() {
		return this.allowed;
	}
	
	public void setAllowed(Boolean allowed) {
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