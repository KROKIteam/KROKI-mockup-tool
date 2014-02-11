import static org.junit.Assert.*;

import org.junit.Test;

import kroki.commons.camelcase.NamingUtil;


public class CamelCaseTest {

	@Test
	public void transferBetweenCamelCase() {
		String text="ZIP code RGID fa",kreiran;
		
		NamingUtil na=new NamingUtil();
		
		kreiran=na.toCamelCaseIE(text, false);
		assertEquals(kreiran,"zipCodeRgidFa");
		
		kreiran=na.fromCamelCase(kreiran);
		assertEquals(kreiran,"Zip code rgid fa");
		
		text="A BeaUtifuL dAY";
        kreiran=na.toCamelCaseIE(text, false);
        assertEquals(kreiran,"aBeautifulDay");
        
        kreiran=na.fromCamelCase(kreiran);
		assertEquals(kreiran,"A beautiful day");
		
		text="A wonderful day";
        kreiran=na.toCamelCaseIE(text, true);
        assertEquals(kreiran,"AWonderfulDay");
        
        kreiran=na.fromCamelCase(kreiran);
		assertEquals(kreiran,"A wonderful day");
	}
}
