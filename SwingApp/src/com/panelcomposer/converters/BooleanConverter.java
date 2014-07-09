package com.panelcomposer.converters;

import java.util.ResourceBundle;

import com.panelcomposer.model.attribute.ColumnAttribute;


public class BooleanConverter extends AbstractConverter {

	public static final String BOOLEAN_TRUE = read("boolean.true");
	public static final String BOOLEAN_FALSE = read("boolean.false");
	
	@Override
	public Object convert(String value, ColumnAttribute column) {
		return convert(value);
	}

	@Override
	public String convertBack(Object value) {
		try{
			return ((Boolean)value).toString();
		} catch (Exception e) {
			return "#error#";
		}
	}

	@Override
	public String convertForViewing(Object value) {
		try{
			if(((Boolean)value).booleanValue() == true)
				return BOOLEAN_TRUE;
			else 
				return BOOLEAN_FALSE;
		} catch (Exception e) {
			return "#error#";
		}
	}
	
	public static String read(String name) {
		return ResourceBundle.getBundle("props.main").getString(name);
	}

	@Override
	public Object convert(String value) {
		try{
			return new Boolean(value);
		} catch (Exception e) {
			return null;
		}
	}


	@Override
	public String convertForSQL(String value, ColumnAttribute column) {
		return value;
	}
}
