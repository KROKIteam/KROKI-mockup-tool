package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 14.06.2013  13:02:07h
   **/

public enum OrderStatusOrderEnum {
	PROCESSING("Processing"),
	SHIPPED("Shipped"),
	DISPATCHED("Dispatched"),
	CANCELED("Canceled");
	
	String label;
	
	OrderStatusOrderEnum() {
	}
	
	OrderStatusOrderEnum(String label) {
		this.label = label;
	}
	
}
