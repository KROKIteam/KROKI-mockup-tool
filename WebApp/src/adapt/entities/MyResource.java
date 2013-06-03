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
   @Author mrd 
   Creation date: 03.06.2013  14:03:14h
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
	private java.lang.Long entId;
	
	@Column(name = "MYRES_TABLE", unique = false, nullable = false)
	private java.lang.String table;
	
	@Column(name = "MYRES_TABLE_LABEL", unique = false, nullable = false)
	private java.lang.String tableLabel;
	
	@Column(name = "MYRES_ENT_LABEL", unique = false, nullable = false)
	private java.lang.String entLabel;
	
	@Column(name = "MYRES_RESLINK", unique = false, nullable = false)
	private java.lang.String ResLink;
	
	
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
	
	public java.lang.Long getEntId() {
		return this.entId;
	}
	
	public void setEntId(java.lang.Long entId) {
		this.entId = entId;
	}
	
	public java.lang.String getTable() {
		return this.table;
	}
	
	public void setTable(java.lang.String table) {
		this.table = table;
	}
	
	public java.lang.String getTableLabel() {
		return this.tableLabel;
	}
	
	public void setTableLabel(java.lang.String tableLabel) {
		this.tableLabel = tableLabel;
	}
	
	public java.lang.String getEntLabel() {
		return this.entLabel;
	}
	
	public void setEntLabel(java.lang.String entLabel) {
		this.entLabel = entLabel;
	}
	
	public java.lang.String getResLink() {
		return this.ResLink;
	}
	
	public void setResLink(java.lang.String ResLink) {
		this.ResLink = ResLink;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
}