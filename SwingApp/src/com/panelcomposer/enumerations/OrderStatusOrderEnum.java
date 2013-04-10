package com.panelcomposer.enumerations;

   /** 
   File generated using Kroki EnumGenerator 
   @Author MiloradFilipovic 
   Creation date: 10.04.2013  15:26:53h
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
