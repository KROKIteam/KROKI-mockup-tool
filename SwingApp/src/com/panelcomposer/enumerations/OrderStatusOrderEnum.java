package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 14.06.2013  10:03:23h
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
