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
   Creation date: 14.06.2013  10:03:22h
   **/

@Entity
@Table(name = "WS_PRICE_LIST_ITEM")
public class PriceListItem implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "PLI_PRICE", unique = false, nullable = false)
	private java.math.BigDecimal price;
	
	@Column(name = "PLI_TAX", unique = false, nullable = false)
	private java.math.BigDecimal tax;
	
	@ManyToOne
	@JoinColumn(name="priceList", referencedColumnName="ID",  nullable = true)
	private PriceList priceList;
	
	
	public PriceListItem(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.math.BigDecimal getPrice() {
		return this.price;
	}
	
	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}
	
	public java.math.BigDecimal getTax() {
		return this.tax;
	}
	
	public void setTax(java.math.BigDecimal tax) {
		this.tax = tax;
	}
	
	public PriceList getPriceList() {
		return this.priceList;
	}
	
	public void setPriceList(PriceList priceList) {
		this.priceList = priceList;
	}
	
}