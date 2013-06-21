package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 14.06.2013  13:02:07h
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
