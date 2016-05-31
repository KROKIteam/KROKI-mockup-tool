package util;

import java.awt.Color;
import java.util.Locale;
import java.util.Locale.Builder;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Resources {
	
	private static Border invalidBorder = BorderFactory.createLineBorder(new Color(237, 81, 135));
	private static Border originalBorder = new JTextField().getBorder();
	private static Border nimbusBorder = new NimbusFocusBorder();
	private static Locale srLatinLocale = new Builder().setLanguage("sr").setScript("Latn").setRegion("RS").build();
	
	
	public static Border getInvalidBorder(){
		return invalidBorder;
	}
	
	public static Border getOriginalBorder(){
		return originalBorder;
	}
	
	public static Locale getLocale(){
		return srLatinLocale;
	}

	public static Border getNimbusBorder() {
		return nimbusBorder;
	}
	

}
