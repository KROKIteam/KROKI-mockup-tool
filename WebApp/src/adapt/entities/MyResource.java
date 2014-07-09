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
   Creation date: 03.07.2014  19:20:58h
   **/

@Entity
@Table(name = "ADAPT_MY_RESOURCE")
public class MyResource implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "MYRES_ENT_ID", unique = false, nullable = false)
	private java.lang.Long entityId;
	@Column(name = "MYRES_TABLE", unique = false, nullable = false)
	private java.lang.String table;
	@Column(name = "MYRES_ENT_LABEL", unique = false, nullable = false)
	private java.lang.String entityLabel;
	@Column(name = "MYRES_TABLE_LABEL", unique = false, nullable = false)
	private java.lang.String tableLabel;
	@Column(name = "MYRES_RESLINK", unique = false, nullable = false)
	private java.lang.String resLink;
	@ManyToOne
	@JoinColumn(name="user", referencedColumnName="ID",  nullable = false)
	private User user;
	
	public MyResource(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.Long getEntityId() {
		return this.entityId;
	}
	
	public void setEntityId(java.lang.Long entityId) {
		this.entityId = entityId;
	}
	
	public java.lang.String getTable() {
		return this.table;
	}
	
	public void setTable(java.lang.String table) {
		this.table = table;
	}
	
	public java.lang.String getEntityLabel() {
		return this.entityLabel;
	}
	
	public void setEntityLabel(java.lang.String entityLabel) {
		this.entityLabel = entityLabel;
	}
	
	public java.lang.String getTableLabel() {
		return this.tableLabel;
	}
	
	public void setTableLabel(java.lang.String tableLabel) {
		this.tableLabel = tableLabel;
	}
	
	public java.lang.String getResLink() {
		return this.resLink;
	}
	
	public void setResLink(java.lang.String resLink) {
		this.resLink = resLink;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
}