package graphedit.util;

import java.util.ArrayList;
import java.util.List;

public class Utility {
	
	/**
	 * Forms enumeration string form list of possible values
	 * @param possibleValues possible values that a property can have
	 * @return string in the appropriate format (value1;value2;....)
	 */
	public static String parsePossibleEnumValues(List<String> possibleValues){
		StringBuilder builder = new StringBuilder();
		for (String value : possibleValues)
			builder.append(value).append(";");
		return builder.toString();
	}
	
	/**
	 * Parses string containing possible values and forms list of values
	 * @param enumeraion possible values joined in one string
	 * @return list of possible values
	 */
	public static List<String> formPossibleValues(String enumeraion){
		List<String> ret = new ArrayList<String>();
		for (String value : enumeraion.split(";"))
			ret.add(value);
		
		return ret;
	}

}
