package graphedit.util;

public class NameTransformUtil {

	
	public static String labelToCamelCase(String s, boolean firstCap){
		String[] split = s.split(" ");
		StringBuilder builder = new StringBuilder();
		if (firstCap)
			builder.append(toProperCase(split[0]));
		else
			builder.append(split[0].toLowerCase());
		if (split.length > 1)
			for (int i=1; i<split.length; i++)
				builder.append(toProperCase(split[i]));
		return builder.toString();
	}
	

	private static String toProperCase(String s) {
		if (s.length() < 2)
			return s; 
		return s.substring(0, 1).toUpperCase() +
				s.substring(1).toLowerCase();
	}

	public static String transformUppercase(String name){
		String[] split = name.split("_");
		StringBuilder builder = new StringBuilder();
		for (String s : split){
			builder.append(s.charAt(0));
			if (s.length()>1)
				builder.append(s.toLowerCase().substring(1));
			builder.append(" ");
		}
		return builder.toString().trim();
	}

	public static String transformUppercaseWithoutSpaces(String name){
		return transformUppercase(name).replace(" ", "");
	}


}
