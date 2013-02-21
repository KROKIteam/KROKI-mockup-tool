package ejb;

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
   Creation date: 21.02.2013  15:40:51h
   **/

@Entity
@Table(name = "WS_CATEGORY")
public class Category implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "CAT_NAME", unique = false, nullable = false)
	private java.lang.String name;
	
	@Column(name = "CAT_DESCRIPTION", unique = false, nullable = false)
	private java.lang.String description;
	
	
	@ManyToOne
	@JoinColumn(name="category", referencedColumnName="ID", nullable=true)
	private Category category;
	
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "category")
	private Set<Category> CategorySet = new HashSet<Category>();
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "category")
	private Set<Product> ProductSet = new HashSet<Product>();
	
	public Category(){
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
	
	public java.lang.String getDescription() {
		return this.description;
	}
	
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	
	public Category getCategory() {
		return this.category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Set<Category> getCategorySet() {
		return this.CategorySet;
	}

	public void setCategorySet(Set<Category> CategorySet) {
		this.CategorySet = CategorySet;
	}
	
	public Set<Product> getProductSet() {
		return this.ProductSet;
	}

	public void setProductSet(Set<Product> ProductSet) {
		this.ProductSet = ProductSet;
	}
	
}