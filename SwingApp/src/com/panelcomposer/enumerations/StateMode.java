package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 21.06.2013  12:55:55h
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
