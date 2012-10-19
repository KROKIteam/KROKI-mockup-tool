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
   Creation date: 25.04.2012  18:37:17:306h
   **/

@Entity
@Table(name = "MYRESOURCE")
public class MyResource implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "entId", unique = false, nullable = false)
	private Long entId;
	
	@Column(name = "MR_TABLE", unique = false, nullable = false)
	private String table;
	
	@Column(name = "MR_ENTLABEL", unique = false, nullable = false)
	private String entLabel;
	
	@Column(name = "MR_TABLABEL", unique = false, nullable = false)
	private String tableLabel;
	
	@Column(name = "MR_RESLINK", unique = false, nullable = false)
	private String resLink;
	
	
	@ManyToOne
	@JoinColumn(name="MR_USER", referencedColumnName="ID", nullable=false)
	private User user;
	
	
	public MyResource(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getEntId() {
		return this.entId;
	}
	
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	
	public String getTable() {
		return this.table;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public String getEntLabel() {
		return this.entLabel;
	}
	
	public void setEntLabel(String entLabel) {
		this.entLabel = entLabel;
	}
	
	public String getTableLabel() {
		return this.tableLabel;
	}
	
	public void setTableLabel(String tableLabel) {
		this.tableLabel = tableLabel;
	}
	
	public String getResLink() {
		return this.resLink;
	}
	
	public void setResLink(String resLink) {
		this.resLink = resLink;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
}