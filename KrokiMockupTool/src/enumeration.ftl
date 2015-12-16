package com.panelcomposer.enumerations;

${doc}

public enum ${enum.name} {
	<#list enum.values as value>
	${value?replace(" ", "_")?upper_case}("${value}")<#if value_has_next>,<#else>;</#if>
	</#list>
	
	String label;
	
	${enum.name}() {
	}
	
	${enum.name}(String label) {
		this.label = label;
	}
	
}
