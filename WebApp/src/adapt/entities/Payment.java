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
   Creation date: 02.04.2013  15:17:20h
   **/

@Entity
@Table(name = "WS_PAYMENT")
public class Payment implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "PAY_PAYMENT_METHOD", unique = false, nullable = true)
	private java.lang.String name;
	
	@Column(name = "PAY_DATE_RECIEVED", unique = false, nullable = true)
	private java.util.Date dateRecieved;
	
	@Column(name = "PAY_AMOUNT_RECIEVED", unique = false, nullable = true)
	private java.math.BigDecimal amountRecieved;
	
	
	
	public Payment(){
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
	
}