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
   Creation date: 19.03.2013  11:05:54h
   **/

@Entity
@Table(name = "ADAPT_ACTION")
public class Action implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "ACT_NAME", unique = true, nullable = false)
	private java.lang.String name;
	
	@Column(name = "ACT_LINK", unique = false, nullable = false)
	private java.lang.String link;
	
	@Column(name = "ACT_IMG_PATH", unique = false, nullable = false)
	private java.lang.String imagePath;
	
	@Column(name = "ACT_TYPE", unique = false, nullable = false)
	private java.lang.String type;
	
	@Column(name = "ACT_TIP", unique = false, nullable = false)
	private java.lang.String tip;
	
	
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "action")
	private Set<UserRights> UserRightsSet = new HashSet<UserRights>();
	
	public Action(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	public java.lang.String getLink() {
		return this.link;
	}
	
	public void setLink(java.lang.String link) {
		this.link = link;
	}
	
	public java.lang.String getImagePath() {
		return this.imagePath;
	}
	
	public void setImagePath(java.lang.String imagePath) {
		this.imagePath = imagePath;
	}
	
	public java.lang.String getType() {
		return this.type;
	}
	
	public void setType(java.lang.String type) {
		this.type = type;
	}
	
	public java.lang.String getTip() {
		return this.tip;
	}
	
	public void setTip(java.lang.String tip) {
		this.tip = tip;
	}
	
	public Set<UserRights> getUserRightsSet() {
		return this.UserRightsSet;
	}

	public void setUserRightsSet(Set<UserRights> UserRightsSet) {
		this.UserRightsSet = UserRightsSet;
	}
	
}