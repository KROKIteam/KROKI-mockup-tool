package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 10.04.2013  15:26:53h
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
