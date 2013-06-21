package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 14.06.2013  13:02:07h
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
