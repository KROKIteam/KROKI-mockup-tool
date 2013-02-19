package ${class.classPackage};

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

${doc}

@Entity
@Table(name = "${class.tableName}")
public class ${class.name} implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	<#if class.attributes?has_content>
	<#list class.attributes as attr>
	@Column(name = "${attr.databaseName}", unique = ${attr.unique?string}, nullable = false)
	private ${attr.type} ${attr.name};
	
	</#list>
	</#if>
	
	<#if class.manyToOneAttributes?has_content>
	<#list class.manyToOneAttributes as mattr>
	@ManyToOne
	@JoinColumn(name="${mattr.name}", referencedColumnName="ID", nullable=true)
	private ${mattr.type} ${mattr.name};
	
	</#list>
	</#if>
	
	<#if class.oneToManyAttributes?has_content>
	<#list class.oneToManyAttributes as oattr>
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "${oattr.mappedBy}")
	private Set<${oattr.refferencedTable}> ${oattr.name} = new HashSet<${oattr.refferencedTable}>();
	
	</#list>
	</#if>
	<#if class.manyToManyAttributes?has_content>
	<#list class.manyToManyAttributes as mtmattr>
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "${mtmattr.joinTable}",
	joinColumns = {
	@JoinColumn(name="${mtmattr.joinColumns}") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="${mtmattr.inverseJoinColumns}")
	}
	)
	private Set<${mtmattr.type}> ${mtmattr.name};
	
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
	public ${attr.type} get${attr.name?cap_first}() {
		return this.${attr.name};
	}
	
	public void set${attr.name?cap_first}(${attr.type} ${attr.name}) {
		this.${attr.name} = ${attr.name};
	}
	
	</#list>
	</#if>
	<#if class.manyToOneAttributes?has_content>
	<#list class.manyToOneAttributes as mattr>
	public ${mattr.type} get${mattr.name?cap_first}() {
		return this.${mattr.name};
	}
	
	public void set${mattr.name?cap_first}(${mattr.type} ${mattr.name}) {
		this.${mattr.name} = ${mattr.name};
	}
	
	</#list>
	</#if>
	<#if class.oneToManyAttributes?has_content>
	<#list class.oneToManyAttributes as oattr>
	public Set<${oattr.refferencedTable}> get${oattr.name?cap_first}() {
		return this.${oattr.name};
	}

	public void set${oattr.name?cap_first}(Set<${oattr.refferencedTable}> ${oattr.name}) {
		this.${oattr.name} = ${oattr.name};
	}
	
	</#list>
	</#if>
	<#if class.manyToManyAttributes?has_content>
	<#list class.manyToManyAttributes as mtmattr>
	public Set<${mtmattr.type}> get${mtmattr.name?cap_first}() {
		return this.${mtmattr.name};
	}

	public void set${mtmattr.name?cap_first}(Set<${mtmattr.type}> ${mtmattr.name}) {
		this.${mtmattr.name} = ${mtmattr.name};
	}
	
	public void add${mtmattr.type?cap_first}(${mtmattr.type} o) {
		this.${mtmattr.name}.add(o);
	}
	
	public void remove${mtmattr.type?cap_first}(${mtmattr.type} o) {
		this.${mtmattr.name}.remove(o);
	}
	</#list>
	</#if>
}