package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 05.06.2013  14:39:54h
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
