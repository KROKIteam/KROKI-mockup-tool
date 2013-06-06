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
   Creation date: 06.06.2013  15:10:54h
   **/

@Entity
@Table(name = "WS_INVOICE")
public class Invoice implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "INV_INVOICE_NUMBER", unique = false, nullable = false)
	private java.lang.String invoiceNumber;
	
	@Column(name = "INV_INVOICE_DATE", unique = false, nullable = false)
	private java.util.Date invoiceDate;
	
	@Column(name = "INV_INVOICE_TOTAL", unique = false, nullable = false)
	private java.math.BigDecimal invoiceTotal;
	
	@ManyToOne
	@JoinColumn(name="order", referencedColumnName="ID",  nullable = true)
	private Order order;
	
	
	public Invoice(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getInvoiceNumber() {
		return this.invoiceNumber;
	}
	
	public void setInvoiceNumber(java.lang.String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public java.util.Date getInvoiceDate() {
		return this.invoiceDate;
	}
	
	public void setInvoiceDate(java.util.Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	public java.math.BigDecimal getInvoiceTotal() {
		return this.invoiceTotal;
	}
	
	public void setInvoiceTotal(java.math.BigDecimal invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}
	
	public Order getOrder() {
		return this.order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
}