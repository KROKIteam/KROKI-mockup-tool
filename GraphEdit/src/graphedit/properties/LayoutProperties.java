package graphedit.properties;

import java.io.IOException;
import java.util.Properties;

public class LayoutProperties {

	private Properties properties;
	private static final String PATH = "/properties/layout.properties";
	
	private Double gapBetweenLevels;
	private Double gapBetweenElements;
	private Integer gapBetweenTrees;
	
	private static LayoutProperties layoutProperties;
	
	public LayoutProperties(){
		properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream(PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getIntValue(String key){
		try{
			return Integer.parseInt(properties.getProperty(key));
		}
		catch (Exception ex){
			return 0;
		}
	}
	
	public double getGapBetweenLevels(){
		if (gapBetweenLevels == null)
			gapBetweenLevels = Double.parseDouble(properties.getProperty("gapBetweenLevels"));
		return gapBetweenLevels;
	}
	
	public double getGapBetweenElements(){
		if (gapBetweenElements == null)
			gapBetweenElements = Double.parseDouble(properties.getProperty("gapBetweenElements"));
		return gapBetweenElements;
	}	
	
	public int getGapBetweenTrees(){
		if (gapBetweenTrees == null)
			gapBetweenTrees = Integer.parseInt(properties.getProperty("gapBetweenTrees"));
		return gapBetweenTrees;
	}
	
	
	public static LayoutProperties getInstance(){
		if (layoutProperties == null)
			layoutProperties = new LayoutProperties();
		return layoutProperties;
	}
	
	
	
}
