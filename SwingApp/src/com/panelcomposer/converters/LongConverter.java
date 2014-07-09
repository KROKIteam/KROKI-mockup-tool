package com.panelcomposer.converters;

import com.panelcomposer.model.attribute.ColumnAttribute;

public class LongConverter extends AbstractConverter {

	@Override
	public Object convert(String value, ColumnAttribute column) {
		Long longVal = null;
		longVal = Long.parseLong(value);
		return longVal;
	}

	@Override
	public Object convert(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convertBack(Object value) {
		if(value instanceof Long) {
			return value.toString();
		}
		else {
			return null;
		}
	}

	@Override
	public String convertForViewing(Object value) {
		return convertBack(value);
	}

	@Override
	public String convertForSQL(String value, ColumnAttribute column) {
		return value;
	}

}
