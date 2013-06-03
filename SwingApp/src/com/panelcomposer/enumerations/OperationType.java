package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 03.06.2013  12:24:43h
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
