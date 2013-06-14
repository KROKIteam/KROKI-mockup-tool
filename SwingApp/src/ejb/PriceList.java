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
@Table(name = "WS_PRICE_LIST")
public class PriceList implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "PL_PRICE_LIST_NUMBER", unique = false, nullable = false)
	private java.lang.String priceListNumber;
	
	@Column(name = "PL_ACTIVE_FROM_DATE", unique = false, nullable = false)
	private java.util.Date activeFromDate;
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "priceList")
	private Set<PriceListItem> PriceListItemSet;
	
	
	public PriceList(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getPriceListNumber() {
		return this.priceListNumber;
	}
	
	public void setPriceListNumber(java.lang.String priceListNumber) {
		this.priceListNumber = priceListNumber;
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