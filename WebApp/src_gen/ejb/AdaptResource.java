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
@Table(name = "Adapt_Resource")
public class AdaptResource implements Serializable {  

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
		@Column(name="name", unique = false, nullable = false)
		private String name;
         
		@Column(name="link", unique = false, nullable = false)
		private String link;
         
    	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "resource")
  		private Set<AdaptPermission> permission = new HashSet<AdaptPermission>();
         

	public AdaptResource(){
	}

    	public void addPermission(AdaptPermission entity) {
    		if(entity != null) {
    			if(!permission.contains(entity)) {
    				entity.setResource(this);
    				permission.add(entity);
    			}
    		}
    	}
    	
    	public void removePermission(AdaptPermission entity) {
    		if(entity != null) {
    			if(permission.contains(entity)) {
					entity.setResource(null);
    				permission.remove(entity);
    			}
    		}
    	}

      public String getName(){
           return name;
      }
      
      public void setName(String name){
           this.name = name;
      }
      
      public String getLink(){
           return link;
      }
      
      public void setLink(String link){
           this.link = link;
      }
      
      public Set<AdaptPermission> getPermission(){
           return permission;
      }
      
      public void setPermission( Set<AdaptPermission> permission){
           this.permission = permission;
      }
      

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append(name + " ");
		result.append(link + " ");
		
		return result.toString();
	}

}
