package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author KROKIteam 
   **/

public enum OperationType {
	BUSSINESTRANSACTION("BussinesTransaction"),
	VIEWREPORT("ViewReport"),
	JAVAOPERATION("JavaOperation");
	
	String label;
	
	OperationType() {
	}
	
	OperationType(String label) {
		this.label = label;
	}
	
}
