package util;

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
			s = s.replace('Č', 'c');
			s = s.replace('č', 'c');
			s = s.replace('Ć', 'c');
			s = s.replace('ć', 'c');
			s = s.replace('Š', 's');
			s = s.replace('š', 's');
			s = s.replaceAll("Đ", "dj");
			s = s.replaceAll("đ", "dj");
		    
			String sb = toCamelCaseIE(s, cap);
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
	
	/**
	 * Converts given string to camel case while preserving Serbian unique letters (Å½,ÄŒ,Ä†,Ä�,Å ).
	 * Used to generate camel case names for elements of UML diagrams during import and export functions.
	 * @param s    String to be converted
	 * @param cap  Indicates if converted string should be capitalized (methods and properties names) or not (class names).
	 * @return     String in camel case notion
	 */
	public String toCamelCaseIE(String s, boolean cap) {
		if(s!=null)
			if(!s.isEmpty())
			{
				StringBuffer builder=new StringBuffer();
				char c=s.charAt(0);
				if(Character.isUpperCase(c)&&!cap)
					builder.append(Character.toLowerCase(c));
				else
					builder.append(c);
				
				boolean prazno=false;
				for(char p:s.substring(1).toCharArray())
				{
					if(Character.isWhitespace(p))
						prazno=true;
					else if(prazno)
					{
						builder.append(Character.toUpperCase(p));
						prazno=false;
					}
					else
					{
						builder.append(Character.toLowerCase(p));
						prazno=false;
					}
				}
				
				return builder.toString();
			}
		return "";
		/**/
	}
	
	/**
	 * Method that transforms Camel case sentences to sentences in which words are separated with empty spaces.
	 * For example:<br/>
	 * "FirstSecondThrid" is transformed to "First second third" and "ZIPCode" will be transfered to "Z i p code". 
	 * @param text   String that is in camel case notation
	 * @return Text  String that is in human readable notation
	 */
	public String fromCamelCase(String text){
		if(text!=null)
			if(!text.isEmpty())
			{	
				StringBuilder builder=new StringBuilder();
				builder.append(Character.toUpperCase(text.charAt(0)));
				int i=1;
				char currentCharacter;
				while(i<text.length())
				{
					currentCharacter=text.charAt(i);
					if(Character.isUpperCase(currentCharacter))
					{
						builder.append(" "+Character.toLowerCase(currentCharacter));
					}else
						builder.append(currentCharacter);
					i++;
				}
				return builder.toString();
			}
		return "";
	}
	
	/**
	 * Checks if given string is java compliant name.
	 *  Java names can only start with letters
	 */
	public boolean checkName(String name) {
		return Character.isLetter(name.charAt(0));
	}
	
	/**
	 * Returns given string with the first letter transformed to lower case
	 * @param string
	 * @return
	 */
	public String lowerFirstLetter(String string){
		if (string.equals(""))
			return "";
		
		Character c1 = string.charAt(0);
		c1 = Character.toLowerCase(c1);
		if (string.length() == 1)
			return c1 + "";
		
		return c1 + string.substring(1);
	}
	
	
	/**
	 * Transforms class name into label by adding spaces before capital letters
	 * @return
	 */
	public String transformClassName(String className){
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
	
	public String transformLabelToJavaName(String name){
		String[] split = name.split(" ");
		StringBuilder builder = new StringBuilder();
		for (String s : split){
			builder.append(Character.toUpperCase(s.charAt(0)));
			if (s.length()>1)
				builder.append(s.substring(1));
		}
		return lowerFirstLetter(builder.toString().trim());
	}


	
}