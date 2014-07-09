package util;

public class TypesConverterFromXML {
	
	public static Integer resolveInteger(String s) {
		Integer i = null;
		if(s == null || s.equals("")) {
			return 0;
		} else {
			try {
				i = new Integer(s);
			} catch (NumberFormatException e) {
				System.err.println("Greska u parsiranju broja iz stringa: " + s);
				return 0;
			}
		}
		return i;
	}
}
