package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 14.06.2013  10:03:23h
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
