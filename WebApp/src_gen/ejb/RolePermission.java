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
@Table(name = "RolePermission")
public class RolePermission implements Serializable {  

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
		@ManyToOne
		@JoinColumn(name="role", referencedColumnName="id", nullable = false)
		private Role role;
         
		@ManyToOne
		@JoinColumn(name="permission", referencedColumnName="id", nullable = false)
		private Permission permission;
         

	public RolePermission(){
	}


      public Role getRole(){
           return role;
      }
      
      public void setRole(Role role){
           this.role = role;
      }
      
      public Permission getPermission(){
           return permission;
      }
      
      public void setPermission(Permission permission){
           this.permission = permission;
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
		if(role != null){
			list.add(role.toString());
		}else{
			list.add("");
		}
		if(permission != null){
			list.add(permission.toString());
		}else{
			list.add("");
		}
		 
		 return list.toArray();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		
		return result.toString();
	}

}
