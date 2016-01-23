package ${class.classPackage};

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

${doc}

@Entity
@Table(name = "${class.tableName}")
public class ${class.name} implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	protected java.lang.Long id;

	<#if class.attributes?has_content>
	<#list class.attributes as attr>
	<#list attr.annotations as annotation>
	${annotation}
	</#list>
	protected ${attr.type?split(":")[0]} ${attr.name};
	</#list>
	</#if>
	
	public ${class.name}(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	<#if class.attributes?has_content>
	<#list class.attributes as attr>
	public ${attr.type?split(":")[0]} get${attr.name?cap_first}() {
		return this.${attr.name};
	}
	
	public void set${attr.name?cap_first}(${attr.type?split(":")[0]} ${attr.name}) {
		this.${attr.name} = ${attr.name};
	}
	
	</#list>
	</#if>
}