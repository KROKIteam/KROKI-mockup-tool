package kroki.commons.camelcase;

/**
 * Class that provides methods for naming various KROKI elements and name generation
 * @author Milorad Filipovic
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
			s = s.replaceAll("đ‘", "dj");
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
	 * Converts given string to camel case while preserving Serbian unique letters (Ž,Č,Ć,Đ,Š).
	 * Used to generate camel case names for elements of UML diagrams during import and export functions.
	 * @param s String to be converted.
	 * @param cap Indicates if converted string should be capitalized (methods and properties names) or not (class names).
	 * @return String in camel case notion.
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
					if(Character.isLowerCase(p)&&prazno)
					{
						builder.append(Character.toUpperCase(p));
						prazno=false;
					}
					else if(Character.isWhitespace(p))
						prazno=true;
					else
					{
						builder.append(p);
						prazno=false;
					}
				}
				
				return builder.toString();
			}
		return "";
		/**/
	}
	
	/**
	 * Converts given string to format suitable for naming database columns.
	 * Column names consist of column label in upper case with words separated by underscore and prefix which is based on table name.
	 * If table name has only one word, column prefix is made from first three letters of that word in upper case,
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
			if(prefixElements[0].length()>=3)
				prefix = prefixElements[0].substring(0, 3).toUpperCase();
			else
				prefix = prefixElements[0].substring(0, prefixElements[0].length()).toUpperCase();
		}else {
			for(int i=0; i<prefixElements.length; i++) {
				prefix += prefixElements[i].charAt(0);
			}
		}
		
		return prefix.toUpperCase() + "_" + columnName.replace(" ", "_").toUpperCase();
	}

	/**
	 * Enumeration that is used while transforming Camel case sentences to sentences in which words are
	 * separated with empty spaces. Enumeration constants are used as states for the process of transforming
	 * names from camel case to human readable notation. 
	 * @author Zeljko Ivkovic
	 *
	 */
	protected enum ESTANJA{PRVO_SLOVO,SLEDECE_SLOVO_POSLE_PRVOG,VELIKO_SLOVO,MALO_POSLE_VELIKOG,MALO_SLOVO,VELIKO_POSLE_MALOG};
	
	/**
	 * Method that transforms Camel case sentences to sentences in which words are separated with empty spaces.
	 * For example:<br/>
	 * "ZIPCode" is transformed to "ZIP code" and "FirstSecondThrid" is transformed to "First second third". 
	 * @param text String that is in camel case notation.
	 * @return Text that is in human readable notation.
	 */
	public String fromCamelCase(String text){
		if(text!=null)
			if(!text.isEmpty())
			{			
				StringBuilder builder=new StringBuilder();
				ESTANJA stanje=ESTANJA.PRVO_SLOVO;
				int i=0;
				char lastCharacter = 0,currentCharacter;
				while(i<text.length()){
					switch(stanje){
					case PRVO_SLOVO:
						currentCharacter=text.charAt(0);
						i=1;
						stanje=ESTANJA.SLEDECE_SLOVO_POSLE_PRVOG;
						currentCharacter=Character.toUpperCase(currentCharacter);
						lastCharacter=currentCharacter;
						break;
					case SLEDECE_SLOVO_POSLE_PRVOG:
						builder.append(lastCharacter);
						currentCharacter=text.charAt(i);
						if(Character.isUpperCase(currentCharacter))
							stanje=ESTANJA.VELIKO_SLOVO;
						else
							stanje=ESTANJA.MALO_SLOVO;
						i++;
						lastCharacter=currentCharacter;
						break;
					case VELIKO_SLOVO:
						currentCharacter=text.charAt(i);
						if(Character.isUpperCase(currentCharacter))
						{
							stanje=ESTANJA.VELIKO_SLOVO;
							builder.append(lastCharacter);
							lastCharacter=currentCharacter;
							i++;
						}
						else
						{
							stanje=ESTANJA.MALO_POSLE_VELIKOG;
							lastCharacter=Character.toLowerCase(lastCharacter);
						}
						break;
					case MALO_POSLE_VELIKOG:
						currentCharacter=text.charAt(i);
						builder.append(" "+lastCharacter+currentCharacter);
						if(++i<text.length())
						{
							currentCharacter=text.charAt(i);
							if(Character.isUpperCase(currentCharacter))
								stanje=ESTANJA.VELIKO_POSLE_MALOG;
							else
								stanje=ESTANJA.MALO_SLOVO;						
							lastCharacter=currentCharacter;
							i++;
						}
						break;
					case MALO_SLOVO:
						builder.append(lastCharacter);
						currentCharacter=text.charAt(i);
						if(Character.isUpperCase(currentCharacter))
							stanje=ESTANJA.VELIKO_POSLE_MALOG;
						else
							stanje=ESTANJA.MALO_SLOVO;
						i++;
						lastCharacter=currentCharacter;
						break;
					case VELIKO_POSLE_MALOG:
						currentCharacter=text.charAt(i);
						if(Character.isUpperCase(currentCharacter))
						{
							builder.append(" "+lastCharacter);
							stanje=ESTANJA.VELIKO_SLOVO;
							lastCharacter=currentCharacter;
						}
						else
						{
							lastCharacter=Character.toLowerCase(lastCharacter);
							stanje=ESTANJA.MALO_POSLE_VELIKOG;
						}
						break;
					}
				}
				builder.append(lastCharacter);
				return builder.toString();
			}
		return "";
	}
}
