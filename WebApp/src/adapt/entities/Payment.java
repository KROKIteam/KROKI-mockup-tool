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
   Creation date: 03.06.2013  16:13:59h
   **/

@Entity
@Table(name = "WS_PAYMENT")
public class Payment implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "PAY_PAYMENT_METHOD", unique = false, nullable = false)
	private java.lang.String paymentMethod;
	
	@Column(name = "PAY_DATE_RECIEVED", unique = false, nullable = false)
	private java.util.Date dateRecieved;
	
	@Column(name = "PAY_AMOUNT_RECIEVED", unique = false, nullable = false)
	private java.math.BigDecimal amountRecieved;
	
	
	@ManyToOne
	@JoinColumn(name="order", referencedColumnName="ID",  nullable = true)
	private Order order;
	
	
	public Payment(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getPaymentMethod() {
		return this.paymentMethod;
	}
	
	public void setPaymentMethod(java.lang.String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public java.util.Date getDateRecieved() {
		return this.dateRecieved;
	}
	
	public void setDateRecieved(java.util.Date dateRecieved) {
		this.dateRecieved = dateRecieved;
	}
	
	public java.math.BigDecimal getAmountRecieved() {
		return this.amountRecieved;
	}
	
	public void setAmountRecieved(java.math.BigDecimal amountRecieved) {
		this.amountRecieved = amountRecieved;
	}
	
	public Order getOrder() {
		return this.order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
}