package adapt.util.xml_utils;

import adapt.core.AppCache;

public class XMLTypesConverter {

	public static Integer resolveInteger(String s) {
		Integer i = null;
		if(s == null || s.equals("")) {
			return null;
		}else {
			try {
				i = new Integer(s);
			} catch (Exception e) {
				AppCache.displayTextOnMainFrame("Error converting '" + s + "' to number. Assigning default value.", 1);
				return 0;
			}
		}
		return i;
	}
	
}
