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
@Table(name = "Adapt_ResourceHierarchy")
public class AdaptResourceHierarchy implements Serializable {  

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
		@ManyToOne
		@JoinColumn(name="resource", referencedColumnName="id", nullable = false)
		private AdaptResource resource;
         
		@ManyToOne
		@JoinColumn(name="resource2", referencedColumnName="id", nullable = false)
		private AdaptResource resource2;
         

	public AdaptResourceHierarchy(){
	}


      public AdaptResource getResource(){
           return resource;
      }
      
      public void setResource(AdaptResource resource){
           this.resource = resource;
      }
      
      public AdaptResource getResource2(){
           return resource2;
      }
      
      public void setResource2(AdaptResource resource2){
           this.resource2 = resource2;
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
		if(resource != null){
			list.add(resource.toString());
		}else{
			list.add("");
		}
		if(resource2 != null){
			list.add(resource2.toString());
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
