package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 05.06.2013  14:39:54h
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
