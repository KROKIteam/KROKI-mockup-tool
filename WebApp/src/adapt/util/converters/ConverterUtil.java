package adapt.util.converters;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import adapt.model.ejb.ColumnAttribute;

/****
 * Class that handles all converting issues between strings and objects.
 * Includes BigDecimalConverter, IntegerConverter, BooleanConverter,
 * DateConverter and StringConverter
 * @author Darko
 * 
 */
public class ConverterUtil {

	/***
	 * BigDecimal converter
	 */
	private static BigDecimalConverter bigDecimalConverter = new BigDecimalConverter();
	/***
	 * Integer converter
	 */
	private static IntegerConverter integerConverter = new IntegerConverter();
	/***
	 * Boolean converter
	 */
	private static BooleanConverter booleanConverter = new BooleanConverter();
	/***
	 * Date converter
	 */
	private static DateConverter dateConverter = new DateConverter();
	/***
	 * String converter
	 */
	private static StringConverter stringConverter = new StringConverter();
	/***
	 * Long converter
	 */
	private static LongConverter longConverter = new LongConverter();
	
	@SuppressWarnings("serial")
	/***
	 * Static map which contains all the converters
	 */
	private static Map<Class<?>, AbstractConverter> converters = new HashMap<Class<?>, AbstractConverter>() {
		{
			put(BigDecimal.class, bigDecimalConverter);
			put(Integer.class, integerConverter);
			put(Boolean.class, booleanConverter);
			put(Date.class, dateConverter);
			put(String.class, stringConverter);
			put(Long.class, longConverter);
		}
	};

	/***
	 * Converts string value of an object to type that is contained in
	 * ColumnAttribute object.
	 * 
	 * @param value
	 *            String value
	 * @param column
	 *            ColumnAttribute that has given value
	 * @return Object form from value
	 */
	public static Object convert(String value, ColumnAttribute column) {
		System.out.println("konvertujem " + value + " u " + column.getDataType());
		value = value.trim();
		Class<?> classType = getType(column);
		if(classType == null) {
			System.out.println("classType = null");
		}else {
			System.out.println("nije null: " + classType.getName());
		}
		try {
			return converters.get(classType).convert(value);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * Converts string value into object for the given ColumnAttribute column.
	 * 
	 * @param value
	 *           String for conversion
	 * @param column
	 *            ColumnAttribute column that contains the class type
	 * @return Object value of the string
	 */
	public static Object convert(String value, Class<?> classType) {
		value = value.trim();
		try {
			return converters.get(classType).convert(value);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public static String convertBack(Object value, Class<?> classType) {
		try {
			return converters.get(classType).convertBack(value);
		} catch (NullPointerException e) {
			return null;
		}
	}

	/***
	 * Converts object into it's string value
	 * 
	 * @param value
	 *            Object for conversion
	 * @param column
	 *            ColumnAttribute column that contains the class type
	 * @return String value of the object
	 */
	public static String convertBack(Object value, ColumnAttribute column) {
		Class<?> classType = getType(column);
		try {
			return converters.get(classType).convertBack(value);
		} catch (NullPointerException e) {
			return "";
		}
	}

	/***
	 * Converts object's value into string for panel's component viewing.
	 * 
	 * @param value
	 *            Object for conversion
	 * @param column
	 *            ColumnAttribute column that contains the class type
	 * @return String value of the object
	 */
	public static String convertForViewing(Object value, ColumnAttribute column) {
		Class<?> classType = getType(column);
		try {
			converters.get(converters.get(classType));
			return converters.get(classType).convertForViewing(value);
		} catch (NullPointerException e) {
			return "";
		}
	}

	/***
	 * Converts object's value into string for SQL query.
	 * 
	 * @param value
	 *            Object for conversion
	 * @param column
	 *            ColumnAttribute column that contains the class type
	 * @return String value of the object
	 */
	public static String convertForSQL(String value, ColumnAttribute column) {
		Class<?> classType = getType(column);
		try {
			System.out.println("ConverterUtil.convertForSQL()");
			return converters.get(classType).convertForSQL(value, column);
		} catch (NullPointerException e) {
			return "";
		}
	}

	/***
	 * Prepares the type of class from column. If it has no type and it is an
	 * enumeration, then it returns as type class Integer.
	 * 
	 * @param column
	 *            ColumnAttribute
	 * @return
	 */
	private static Class<?> getType(ColumnAttribute column) {
		Class<?> classType = null;
		if (column.getDataType() != null && !column.getDataType().equals("")) {
			try {
				classType = Class.forName(column.getDataType().split(":")[0]);
			} catch (Exception e) {
				return null;
			}
		} else if (column.getEnumeration() != null
				&& !column.getEnumeration().equals("")) {
			classType = Integer.class;
		} else {
			return null;
		}
		return classType;
	}

	public static String convertForViewing(Object value, Class<?> classType) {
		try {
			return converters.get(classType).convertForViewing(value);
		} catch (NullPointerException e) {
			return "";
		}
	}

}
