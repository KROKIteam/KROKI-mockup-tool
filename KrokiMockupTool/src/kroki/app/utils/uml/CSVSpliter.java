package kroki.app.utils.uml;

import java.util.ArrayList;

/**
 * Utility class that contains methods for splitting string values that contain comma separators.
 * @author Zeljko Ivkovic {ivkovicszeljko@gmail.com}
 *
 */
public class CSVSpliter {

	/**
	 * Received string will be split if it contains comma separators.
	 * Two trailing commas while produce an empty string.
	 * Commas in quoted elements are ignored.
	 * @param text String with comma separators to split
	 * @return String array of separated values
	 */
	public static String[] split(String text){
		ArrayList<String> niz=new ArrayList<String>();
		if(text==null)
			return null;
		if(text.isEmpty())
			return null;
		int index1,index2,brojac=0;
		boolean pronasao=false;//U slucaju kada je pronasao ," treba da pronadje kraj tog dela ", i tek onda moze
		// da nastavi sa ponovnim trazenjem ," ili ,
		do{
			if(pronasao)
			{
				index1=text.indexOf("\",",brojac);
				if(index1==-1)
				{
					niz.add(text.substring(brojac));
					break;
				}else
				{
					niz.add(text.substring(brojac,index1));
					brojac=index1+2;
					pronasao=false;
				}
				
			}else{
				index1=text.indexOf(",\"",brojac);
				
				index2=text.indexOf(",",brojac);
				if(index1==-1)
				{
					if(index2>=0)
					{
						if(brojac==index2)
							niz.add("");
						else
							niz.add(text.substring(brojac,index2));
						brojac=index2+1;
					}else
					{
						niz.add(text.substring(brojac));
						break;
					}
				}
				else 
				{
					if(index1==index2)
					{
						if(brojac==index2)
							niz.add("");
						else
							niz.add(text.substring(brojac,index2));
						brojac=index2+2;
						pronasao=true;
					}else{
						if(brojac==index2)
							niz.add("");
						else
							niz.add(text.substring(brojac,index2));
						brojac=index2+1;
					}
				}
			}
		}while(brojac<text.length());
		String[] stringNiz=new String[1];
		return niz.toArray(stringNiz);
	}
	
	
}
