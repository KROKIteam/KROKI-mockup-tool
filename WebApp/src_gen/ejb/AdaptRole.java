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
@Table(name = "Adapt_Role")
public class AdaptRole implements Serializable {  

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
		@Column(name="name", unique = false, nullable = false)
		private String name;
         
    	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
  		private Set<AdaptUserRoles> userRoles = new HashSet<AdaptUserRoles>();
         
    	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "role")
  		private Set<AdaptRolePermission> rolePermission = new HashSet<AdaptRolePermission>();
         

	public AdaptRole(){
	}

    	public void addUserRoles(AdaptUserRoles entity) {
    		if(entity != null) {
    			if(!userRoles.contains(entity)) {
    				entity.setRole(this);
    				userRoles.add(entity);
    			}
    		}
    	}
    	
    	public void removeUserRoles(AdaptUserRoles entity) {
    		if(entity != null) {
    			if(userRoles.contains(entity)) {
					entity.setRole(null);
    				userRoles.remove(entity);
    			}
    		}
    	}
    	public void addRolePermission(AdaptRolePermission entity) {
    		if(entity != null) {
    			if(!rolePermission.contains(entity)) {
    				entity.setRole(this);
    				rolePermission.add(entity);
    			}
    		}
    	}
    	
    	public void removeRolePermission(AdaptRolePermission entity) {
    		if(entity != null) {
    			if(rolePermission.contains(entity)) {
					entity.setRole(null);
    				rolePermission.remove(entity);
    			}
    		}
    	}

      public String getName(){
           return name;
      }
      
      public void setName(String name){
           this.name = name;
      }
      
      public Set<AdaptUserRoles> getUserRoles(){
           return userRoles;
      }
      
      public void setUserRoles( Set<AdaptUserRoles> userRoles){
           this.userRoles = userRoles;
      }
      
      public Set<AdaptRolePermission> getRolePermission(){
           return rolePermission;
      }
      
      public void setRolePermission( Set<AdaptRolePermission> rolePermission){
           this.rolePermission = rolePermission;
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
		list.add(name.toString());
		 
		 return list.toArray();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append(name + " ");
		
		return result.toString();
	}

}

