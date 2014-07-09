package com.panelcomposer.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.panelcomposer.model.attribute.ColumnAttribute;


public class BigDecimalConverter extends AbstractConverter {

	@Override
	public Object convert(String value, ColumnAttribute column) {
		BigDecimal bd = null;
		Integer scale = column.getScale();
		try {
			bd = new BigDecimal(value);
			bd = bd.setScale(scale, RoundingMode.HALF_UP);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bd;
	}

	@Override
	public String convertBack(Object value) {
		String str = null;
		if(value != null && value instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) value;
			str = bd.toString();
		}
		return str;
	}

	@Override
	public String convertForViewing(Object value) {
		return convertBack(value);
	}

	@Override
	public Object convert(String value) {
		try {
			return new BigDecimal(value);
		} catch (Exception e) {
			return new BigDecimal(0.0);
		}
	}

	@Override
	public String convertForSQL(String value, ColumnAttribute column) {
		return value;
	}
}
