package adapt.util.converters;

import adapt.model.ejb.ColumnAttribute;


public class IntegerConverter extends AbstractConverter {

	@Override
	public Object convert(String value, ColumnAttribute column) {
		return convert(value);
	}

	@Override
	public String convertBack(Object value) {
		try {
			return ((Integer) value).toString();
		} catch (Exception e) {
			System.out.println("integer  exception " + value);
			return "0";
		}
	}
	
	@Override
	public String convertForViewing(Object value) {
		return convertBack(value);
	}

	@Override
	public Object convert(String value) {
		try {
			return new Integer(value.trim());
		} catch (Exception e) {
			return 0;
		}
	}
	

	@Override
	public String convertForSQL(String value, ColumnAttribute column) {
		return value;
	}
}
