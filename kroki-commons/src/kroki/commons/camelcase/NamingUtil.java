package kroki.commons.camelcase;

/**
 * Class that provides methods for naming various KROKI elements and name generation
 * @author KROKI Team
 */
public class NamingUtil {

	/**
	 * Converts given string to camel case.
	 * Used to generate names for classes and properties.
	 * @param s string to be converted
	 * @param cap indicates if converted string should be capitalized (methods and properties names) or not (class names)
	 * @return string in camel case notion
	 */
	public String toCamelCase(String s, boolean cap) {
		if(!s.equals("")) {
			s = s.replace('ž', 'z');
			s = s.replace('Ž', 'z');
			s = s.replace('ć', 'c');
			s = s.replace('Ć', 'c');
			s = s.replace('č', 'c');
			s = s.replace('Č', 'c');
			s = s.replace('š', 's');
			s = s.replace('Š', 's');
			s = s.replaceAll("đ", "dj");
			s = s.replaceAll("Đ", "dj");
			
		    StringBuffer sb = new StringBuffer();
		    String[] x = s.replaceAll("[^A-Za-z]", " ").replaceAll("\\s+", " ")
		            .trim().split(" ");

		    for (int i = 0; i < x.length; i++) {
		        if (i == 0) {
		            x[i] = x[i].toLowerCase();
		        } else {
		            String r = x[i].substring(1);
		            x[i] = String.valueOf(x[i].charAt(0)).toUpperCase() + r;

		        }
		        sb.append(x[i]);
		    }
		    if(cap) {
		    	return sb.toString().substring(0, 1).toLowerCase() + sb.toString().substring(1);
		    }else {
		    	return sb.toString().substring(0, 1).toUpperCase() + sb.toString().substring(1);
		    }
		}else {
			return "";
		}
	}
	
	/**
	 * Converts given string to format suitable for naming database columns.
	 * Column names consist of column label in upper case with words separated by underscore and prefix which is based on table name.
	 * If table name has only one word, column prefix is made from first three letters of that word in upper case.
	 * Otherwise, column prefix is acronym for table name.
	 * Method is used for generating default column names based on labels of corresponding GUI components.
	 * @param tableName name of database table used to generate prefix for column name
	 * @param columnName label of GUI component used to generate database column name
	 * @return generated database column name with corresponding prefix (i.e. EMP_FIRST_NAME for column 'First name' in 'Employee' table)
	 */
	public String toDatabaseFormat(String tableName, String columnName) {
		String prefix = "";
		
		String[] prefixElements = tableName.split(" ");
		if(prefixElements.length == 1) {
			String tabName = prefixElements[0];
			if(tabName.length() < 3) {
				prefix = tabName.toUpperCase();
			}else {
				prefix = tabName.substring(0, 3).toUpperCase();
			}
		}else {
			for(int i=0; i<prefixElements.length; i++) {
				prefix += prefixElements[i].charAt(0);
			}
		}
		return prefix.toUpperCase() + "_" + columnName.replace(" ", "_").toUpperCase();
	}
}
