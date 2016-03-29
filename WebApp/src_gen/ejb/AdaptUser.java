package ejb;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Adapt_User")
public class AdaptUser implements Serializable {  

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
		@Column(name="username", unique = false, nullable = false)
		private String username;
         
		@Column(name="password", unique = false, nullable = false)
		private String password;
         
    	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
  		private Set<AdaptUserRoles> useringroup = new HashSet<AdaptUserRoles>();
         

	public AdaptUser(){
	}

    	public void addUseringroup(AdaptUserRoles entity) {
    		if(entity != null) {
    			if(!useringroup.contains(entity)) {
    				entity.setUser(this);
    				useringroup.add(entity);
    			}
    		}
    	}
    	
    	public void removeUseringroup(AdaptUserRoles entity) {
    		if(entity != null) {
    			if(useringroup.contains(entity)) {
					entity.setUser(null);
    				useringroup.remove(entity);
    			}
    		}
    	}

      public String getUsername(){
           return username;
      }
      
      public void setUsername(String username){
           this.username = username;
      }
      
      public String getPassword(){
           return password;
      }
      
      public void setPassword(String password){
           this.password = password;
      }
      
      public Set<AdaptUserRoles> getUseringroup(){
           return useringroup;
      }
      
      public void setUseringroup( Set<AdaptUserRoles> useringroup){
           this.useringroup = useringroup;
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
		list.add(username.toString());
		list.add(password.toString());
		 
		 return list.toArray();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append(username + " ");
		result.append(password + " ");
		
		return result.toString();
	}

}
