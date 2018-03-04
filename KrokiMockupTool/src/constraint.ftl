package  ${class.classPackage};
<#if class.importedPackages?has_content>
<#list class.importedPackages as paket>
${paket}
</#list>
</#if> 

public class ${class.name}Constraints extends ${class.name}{

<#list class.constraints as con>
<#if con.kind=="invariant" || con.kind=="postcondition" || con.kind=="precondition">
	public void ${con.operation.header} throws InvariantException{
		boolean result=false;
<#list con.declarations as dec>
		${dec}
</#list>
		try {
		<#if con.thenPart?? == false>
			result = ${con.condition};
		<#else>
			<#include "/ifElse.ftl">	
		</#if> 	
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ( ! result ) {
			String message = "invariant ${con.operation.name} "
			+ "is broken in object '" + this.getObjectName() + "' of type '" + this.getClass().getName() + "'";
			throw new InvariantException(message);
		}
	}
</#if> 

<#if con.kind=="body" || con.kind=="definition">
	public ${con.returnType} ${con.operation.header}{
		<#list con.declarations as dec>
		${dec}
		</#list>
		return ${con.condition};
	}
</#if> 

<#if con.kind=="unsupported">
	public void ${con.operation.header}{
		// ${con.body}
	}
</#if> 

<#list con.setOperations as metoda>

<#if metoda.name=="count">
<#include "/setOperations/count.ftl">

</#if>
<#if metoda.name=="convertToList">
<#include "/setOperations/convertToList.ftl">

</#if>
<#if metoda.name=="convertToSet">
<#include "/setOperations/convertToSet.ftl">

</#if>
<#if metoda.name=="symmetricDifference">
<#include "/setOperations/symmetricDifference.ftl">

</#if>
<#if metoda.name=="flatten">
<#include "/setOperations/flatten.ftl">

</#if>
</#list>
</#list>
<#if class.operations?has_content>
<#list class.operations as metoda>

<#if metoda.name=="implies">
	public Boolean implies(Boolean b1, Boolean b2){
		if(b1==true && b2==false)
			return false;
		else
			return true;
	}
</#if>
<#if metoda.name?starts_with("sum")>	
<#include "/setOperations/sum.ftl">
</#if> 	
<#if metoda.name?starts_with("forAll")>	
<#include "/setOperations/forAll.ftl">
</#if> 	
<#if metoda.name?starts_with("exists")>	
<#include "/setOperations/exists.ftl">
</#if> 	
<#if metoda.name?starts_with("one")>	
<#include "/setOperations/one.ftl">
</#if> 	
<#if metoda.name?starts_with("isUnique")>	
<#include "/setOperations/isUnique.ftl">
</#if> 
<#if metoda.name?starts_with("any")>	
<#include "/setOperations/any.ftl">
</#if> 
<#if metoda.name?starts_with("select") || metoda.name?starts_with("reject")>	
<#include "/setOperations/select.ftl">
</#if>
<#if metoda.name?starts_with("collect")>	
<#include "/setOperations/collect.ftl">
</#if> 
</#list>
</#if>
	private String objectName;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
}