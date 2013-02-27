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
   Creation date: 27.02.2013  14:52:36h
   **/

@Entity
@Table(name = "TC_ORDERS")
public class Orders implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "ORD_ORDER_NUMBER", unique = false, nullable = false)
	private java.lang.String orderNumber;
	
	@Column(name = "ORD_ORDER_DATE", unique = false, nullable = false)
	private java.util.Date orderDate;
	
	@Column(name = "ORD_SHIPMENT_ADDRESS", unique = false, nullable = false)
	private java.lang.String shipmentAddress;
	
	@Column(name = "ORD_ORDER_TOTAL", unique = false, nullable = false)
	private java.lang.String orderTotal;
	
	@Column(name = "ORD_ORDER_STATUS", unique = false, nullable = false)
	private java.lang.String orderStatus;
	
	
	@ManyToOne
	@JoinColumn(name="customer", referencedColumnName="ID", nullable=true)
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="city", referencedColumnName="ID", nullable=true)
	private City city;
	
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "orders")
	private Set<OrderItem> orderItemSet = new HashSet<OrderItem>();
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "orders")
	private Set<Invoice> invoiceSet = new HashSet<Invoice>();
	
	public Orders(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getOrderNumber() {
		return this.orderNumber;
	}
	
	public void setOrderNumber(java.lang.String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public java.util.Date getOrderDate() {
		return this.orderDate;
	}
	
	public void setOrderDate(java.util.Date orderDate) {
		this.orderDate = orderDate;
	}
	
	public java.lang.String getShipmentAddress() {
		return this.shipmentAddress;
	}
	
	public void setShipmentAddress(java.lang.String shipmentAddress) {
		this.shipmentAddress = shipmentAddress;
	}
	
	public java.lang.String getOrderTotal() {
		return this.orderTotal;
	}
	
	public void setOrderTotal(java.lang.String orderTotal) {
		this.orderTotal = orderTotal;
	}
	
	public java.lang.String getOrderStatus() {
		return this.orderStatus;
	}
	
	public void setOrderStatus(java.lang.String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public Customer getCustomer() {
		return this.customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public City getCity() {
		return this.city;
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	
	public Set<OrderItem> getOrderItemSet() {
		return this.orderItemSet;
	}

	public void setOrderItemSet(Set<OrderItem> orderItemSet) {
		this.orderItemSet = orderItemSet;
	}
	
	public Set<Invoice> getInvoiceSet() {
		return this.invoiceSet;
	}

	public void setInvoiceSet(Set<Invoice> invoiceSet) {
		this.invoiceSet = invoiceSet;
	}
	
}