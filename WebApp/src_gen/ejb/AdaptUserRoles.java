package ejb;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "Adapt_UserRoles")
public class AdaptUserRoles implements Serializable {  

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
		@ManyToOne
		@JoinColumn(name="user", referencedColumnName="id", nullable = false)
		private AdaptUser user;
         
		@ManyToOne
		@JoinColumn(name="role", referencedColumnName="id", nullable = false)
		private AdaptRole role;
         
		//izbaciti
		@Deprecated 
		@Column(name="valid", unique = false, nullable = true)
		private Boolean valid;
         

	public AdaptUserRoles(){
	}


      public AdaptUser getUser(){
           return user;
      }
      
      public void setUser(AdaptUser user){
           this.user = user;
      }
      
      public AdaptRole getRole(){
           return role;
      }
      
      public void setRole(AdaptRole role){
           this.role = role;
      }
      
      public Boolean getValid(){
           return valid;
      }
      
      public void setValid(Boolean valid){
           this.valid = valid;
      }
      

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Object[] getValues() {	
		List<Object> list = new ArrayList<Object>();
		
		list.add(id);		
		if(user != null){
			list.add(user.toString());
		}else{
			list.add("");
		}
		if(role != null){
			list.add(role.toString());
		}else{
			list.add("");
		}
		list.add(valid.toString());
		 
		 return list.toArray();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append(valid + " ");
		
		return result.toString();
	}

}
