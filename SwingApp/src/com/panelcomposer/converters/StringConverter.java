package com.panelcomposer.converters;

import com.panelcomposer.model.attribute.ColumnAttribute;

public class StringConverter extends AbstractConverter {

	@Override
	public Object convert(String value, ColumnAttribute column) {
		return value;
	}

	@Override
	public Object convert(String value) {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public String convertBack(Object value) {
		// TODO Auto-generated method stub
		if(value != null) {
			return value.toString();
		} else
			return "";
	}

	@Override
	public String convertForViewing(Object value) {
		if(value != null) {
			return value.toString();
		} else
			return "";
	}

	@Override
	public String convertForSQL(String value, ColumnAttribute column) {
		return value;
	}

}
