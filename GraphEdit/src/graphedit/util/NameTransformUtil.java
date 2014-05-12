package graphedit.util;

public class NameTransformUtil {

	/**
	 * 
	 * @return
	 */
	public static String transformClassName(String className){
		final StringBuilder result = new StringBuilder();
		result.append(Character.toUpperCase(className.charAt(0)));
		for (int i = 1; i < className.length(); i++) {
			boolean lower = false;
			if (Character.isUpperCase(className.charAt(i))){
				result.append(' ');
				lower = true;
			}
			if (lower)
				result.append(Character.toLowerCase(className.charAt(i)));
			else
				result.append(className.charAt(i));
		}
		return result.toString();
	}	

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
