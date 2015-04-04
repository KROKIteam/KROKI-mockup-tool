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
@Table(name = "Permission")
public class Permission implements Serializable {  

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
		@Column(name="name", unique = false, nullable = false)
		private String name;
         
		@ManyToOne
		@JoinColumn(name="operation", referencedColumnName="id", nullable = true)
		private Operation operation;
         
    	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "permission")
  		private Set<RolePermission> rolePermission = new HashSet<RolePermission>();
         
		@ManyToOne
		@JoinColumn(name="resource", referencedColumnName="id", nullable = false)
		private Resource resource;
         

	public Permission(){
	}

    	public void addRolePermission(RolePermission entity) {
    		if(entity != null) {
    			if(!rolePermission.contains(entity)) {
    				entity.setPermission(this);
    				rolePermission.add(entity);
    			}
    		}
    	}
    	
    	public void removeRolePermission(RolePermission entity) {
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
      
      public Operation getOperation(){
           return operation;
      }
      
      public void setOperation(Operation operation){
           this.operation = operation;
      }
      
      public Set<RolePermission> getRolePermission(){
           return rolePermission;
      }
      
      public void setRolePermission( Set<RolePermission> rolePermission){
           this.rolePermission = rolePermission;
      }
      
      public Resource getResource(){
           return resource;
      }
      
      public void setResource(Resource resource){
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
