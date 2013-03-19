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
@Table(name = "WS_PRICE_LIST")
public class PriceList implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "PL_PRICE_LIST_NUMBER", unique = false, nullable = false)
	private java.lang.String name;
	
	@Column(name = "PL_ACTIVE_FROM_DATE", unique = false, nullable = false)
	private java.util.Date activeFromDate;
	
	
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "priceList")
	private Set<PriceListItem> PriceListItemSet = new HashSet<PriceListItem>();
	
	public PriceList(){
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
	
	public java.util.Date getActiveFromDate() {
		return this.activeFromDate;
	}
	
	public void setActiveFromDate(java.util.Date activeFromDate) {
		this.activeFromDate = activeFromDate;
	}
	
	public Set<PriceListItem> getPriceListItemSet() {
		return this.PriceListItemSet;
	}

	public void setPriceListItemSet(Set<PriceListItem> PriceListItemSet) {
		this.PriceListItemSet = PriceListItemSet;
	}
	
}