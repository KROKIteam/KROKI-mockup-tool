package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 10.04.2013  15:26:53h
   **/

public enum StateMode {
	UPDATE("UPDATE"),
	ADD("ADD"),
	SEARCH("SEARCH");
	
	String label;
	
	StateMode() {
	}
	
	StateMode(String label) {
		this.label = label;
	}
	
}
