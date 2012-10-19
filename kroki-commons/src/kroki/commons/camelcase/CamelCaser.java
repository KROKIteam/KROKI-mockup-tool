package kroki.commons.camelcase;

public class CamelCaser {

	/**
	 * Klasa koja od prosledjenog stringa  pravi string u camel case notaciji.
	 * @param s string koji treba biti preveden u camel case
	 * @param cap ako je true, rezultujuci string ce da pocinje malim slovom (notacija za promenjive i metode), 
	 * ako je false, pocinjace velikm slovom (imena klasa)
	 * @return
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
}
