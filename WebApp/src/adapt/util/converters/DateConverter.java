package adapt.util.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import adapt.model.ejb.ColumnAttribute;

public class DateConverter extends AbstractConverter {

	@Override
	public Object convert(String value, ColumnAttribute column) {
		return convert(value);
	}

	@Override
	public String convertBack(Object value) {
		String format = "dd-MM-yyyy";
		Date date = (Date) value;
		DateFormat dateFormat = new SimpleDateFormat(format);
		String strDate = null;
		strDate = dateFormat.format(date);
		return strDate;
	}

	@Override
	public String convertForViewing(Object value) {
		String format = "dd.MM.yyyy.";
		Date date = (Date) value;
		DateFormat dateFormat = new SimpleDateFormat(format);
		String strDate = null;
		strDate = dateFormat.format(date);
		return strDate;
	}

	@Override
	public Object convert(String value) {
		Date date = null;
		String format = "dd.MM.yyyy.";
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			date = dateFormat.parse(value);
		} catch (ParseException e) {
			return null;
		}
		return date;
	}


	@Override
	public String convertForSQL(String value, ColumnAttribute column) {
		String strDate = null;
		Date date = (Date) convert(value);
		String format = "yyyy-MM-dd";
		DateFormat dateFormat = new SimpleDateFormat(format);
		if(date != null) {
			strDate = dateFormat.format(date);
		}
		return strDate;
	}
}
