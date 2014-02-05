package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author KROKIteam 
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
