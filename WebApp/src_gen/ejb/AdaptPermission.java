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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Adapt_Permission")
public class AdaptPermission implements Serializable {  

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
		@Column(name="name", unique = false, nullable = false)
		private String name;
         
		@ManyToOne
		@JoinColumn(name="operation", referencedColumnName="id", nullable = true)
		private AdaptOperation operation;
         
    	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "permission")
  		private Set<AdaptRolePermission> rolePermission = new HashSet<AdaptRolePermission>();
         
		@ManyToOne
		@JoinColumn(name="resource", referencedColumnName="id", nullable = false)
		private AdaptResource resource;
         

	public AdaptPermission(){
	}

    	public void addRolePermission(AdaptRolePermission entity) {
    		if(entity != null) {
    			if(!rolePermission.contains(entity)) {
    				entity.setPermission(this);
    				rolePermission.add(entity);
    			}
    		}
    	}
    	
    	public void removeRolePermission(AdaptRolePermission entity) {
    		if(entity != null) {
    			if(rolePermission.contains(entity)) {
					entity.setPermission(null);
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
      
      public AdaptOperation getOperation(){
           return operation;
      }
      
      public void setOperation(AdaptOperation operation){
           this.operation = operation;
      }
      
      public Set<AdaptRolePermission> getRolePermission(){
           return rolePermission;
      }
      
      public void setRolePermission( Set<AdaptRolePermission> rolePermission){
           this.rolePermission = rolePermission;
      }
      
      public AdaptResource getResource(){
           return resource;
      }
      
      public void setResource(AdaptResource resource){
           this.resource = resource;
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
		if(operation != null){
			list.add(operation.toString());
		}else{
			list.add("");
		}
		if(resource != null){
			list.add(resource.toString());
		}else{
			list.add("");
		}
		 
		 return list.toArray();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append(name + " ");
		
		return result.toString();
	}

}
