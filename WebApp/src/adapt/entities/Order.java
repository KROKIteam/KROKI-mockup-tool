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
   Creation date: 14.06.2013  11:57:05h
   **/

@Entity
@Table(name = "WS_ORDER")
public class Order implements java.io.Serializable {

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
	private java.math.BigDecimal orderTotal;
	@Column(name = "ORD_ORDER_STATUS", unique = false, nullable = false)
	private java.lang.String orderStatus;
	@ManyToOne
	@JoinColumn(name="order_customer", referencedColumnName="ID",  nullable = true)
	private Customer order_customer;
	@ManyToOne
	@JoinColumn(name="order_city", referencedColumnName="ID",  nullable = true)
	private City order_city;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "orderitem_order")
	private Set<OrderItem> OrderItemSet;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "invoice_order")
	private Set<Invoice> InvoiceSet;
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "payment_order")
	private Set<Payment> PaymentSet;
	
	public Order(){
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
	
	public java.math.BigDecimal getOrderTotal() {
		return this.orderTotal;
	}
	
	public void setOrderTotal(java.math.BigDecimal orderTotal) {
		this.orderTotal = orderTotal;
	}
	
	public java.lang.String getOrderStatus() {
		return this.orderStatus;
	}
	
	public void setOrderStatus(java.lang.String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public Customer getOrder_customer() {
		return this.order_customer;
	}
	
	public void setOrder_customer(Customer order_customer) {
		this.order_customer = order_customer;
	}
	
	public City getOrder_city() {
		return this.order_city;
	}
	
	public void setOrder_city(City order_city) {
		this.order_city = order_city;
	}
	
	public Set<OrderItem> getOrderItemSet() {
		return this.OrderItemSet;
	}
	
	public void setOrderItemSet(Set<OrderItem> OrderItemSet) {
		this.OrderItemSet = OrderItemSet;
	}
	
	public Set<Invoice> getInvoiceSet() {
		return this.InvoiceSet;
	}
	
	public void setInvoiceSet(Set<Invoice> InvoiceSet) {
		this.InvoiceSet = InvoiceSet;
	}
	
	public Set<Payment> getPaymentSet() {
		return this.PaymentSet;
	}
	
	public void setPaymentSet(Set<Payment> PaymentSet) {
		this.PaymentSet = PaymentSet;
	}
	
}