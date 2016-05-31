package com.panelcomposer.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Patterns {
	
	
	static Pattern emailPattern = Pattern
			.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[a-zA-Z0-9](?:[\\w-]*[\\w])?");
	
	private static Map<String, Pattern> patterns = new HashMap<String, Pattern>();
	
	static {
		patterns.put("email", emailPattern);
	}
	
	public static Pattern getPattern(String key){
		return patterns.get(key);
	}

}
