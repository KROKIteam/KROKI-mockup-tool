package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 06.06.2013  15:10:17h
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
