package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 21.06.2013  12:55:55h
   **/

public enum PanelType {
	STANDARDPANEL("StandardPanel"),
	PARENTCHILDPANEL("ParentChildPanel"),
	MANYTOMANYPANEL("ManyToManyPanel");
	
	String label;
	
	PanelType() {
	}
	
	PanelType(String label) {
		this.label = label;
	}
	
}
