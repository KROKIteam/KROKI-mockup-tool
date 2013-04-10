package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 10.04.2013  15:26:53h
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
