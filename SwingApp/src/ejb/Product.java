package ejb;

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
   Creation date: 14.06.2013  13:02:06h
   **/

@Entity
@Table(name = "WS_PRODUCT")
public class Product implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "PRO_NAME", unique = false, nullable = false)
	private java.lang.String name;
	@Column(name = "PRO_DESCRIPTION", unique = false, nullable = false)
	private java.lang.String description;
	@ManyToOne
	@JoinColumn(name="product_category", referencedColumnName="ID",  nullable = true)
	private Category product_category;
	@ManyToOne
	@JoinColumn(name="product_vendor", referencedColumnName="ID",  nullable = true)
	private Vendor product_vendor;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "orderitem_product")
	private Set<OrderItem> OrderItemSet;
	
	public Product(){
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
	
	public Category getProduct_category() {
		return this.product_category;
	}
	
	public void setProduct_category(Category product_category) {
		this.product_category = product_category;
	}
	
	public Vendor getProduct_vendor() {
		return this.product_vendor;
	}
	
	public void setProduct_vendor(Vendor product_vendor) {
		this.product_vendor = product_vendor;
	}
	
	public Set<OrderItem> getOrderItemSet() {
		return this.OrderItemSet;
	}
	
	public void setOrderItemSet(Set<OrderItem> OrderItemSet) {
		this.OrderItemSet = OrderItemSet;
	}
	
}