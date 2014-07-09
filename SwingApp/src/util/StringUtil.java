package util;

public class StringUtil {

	public static String capitalize(String str) {
		if(str == null || str.equals(""))
			return "";
		String capital = str.substring(0,1).toUpperCase();
		String rest = str.substring(1);
		str = capital + rest;
		return str;
	}
	
}
