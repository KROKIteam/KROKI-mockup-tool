package adapt.util.converters;

import adapt.model.ejb.ColumnAttribute;


public abstract class AbstractConverter {
	public abstract Object convert(String value, ColumnAttribute column);
	public abstract Object convert(String value);
	public abstract String convertBack(Object value);
	public abstract String convertForViewing(Object value);
	public abstract String convertForSQL(String value, ColumnAttribute column);
}
