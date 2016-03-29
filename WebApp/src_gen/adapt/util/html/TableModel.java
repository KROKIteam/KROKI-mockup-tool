package adapt.util.html;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import adapt.core.AppCache;
import adapt.model.ejb.AbstractAttribute;
import adapt.model.ejb.ColumnAttribute;
import adapt.model.ejb.EntityBean;
import adapt.model.ejb.JoinColumnAttribute;
import adapt.util.converters.ConverterUtil;
import adapt.util.staticnames.Settings;

/**
 * Holds data that needs to be displayed in HTML table
 * Table rows are stored in {@code ArrayList} while each cell is represented as {@code LinkedHashMap}
 * with corresponding field name as key and field value as map value.
 * @author Milorad Filipovic
 *
 */
public class TableModel {

	String NULL = Settings.NULL_VALUE;
	private EntityBean bean;
	
	public TableModel(EntityBean ejb) {
		this.bean = ejb;
	}
	
	/**
	 * Stores database query result list into table model
	 * @param objects list of objects obtained from database
	 * @return list of values suitable for HTML representation 
	 */
	public ArrayList<LinkedHashMap<String, String>> getModel(List<Object> resultSet) {
		ArrayList<LinkedHashMap<String, String>> model = new ArrayList<LinkedHashMap<String,String>>();
		Class oCLass = bean.getEntityClass();
		
		for (Object	object : resultSet) {
			LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
			for(AbstractAttribute attribute : bean.getAttributes()) {
				if(attribute instanceof ColumnAttribute) {
					ColumnAttribute columnAttr = (ColumnAttribute)attribute;
					try {
						Field columnField = oCLass.getDeclaredField(columnAttr.getFieldName());
						columnField.setAccessible(true);
						String columnValue = ConverterUtil.convertForViewing(columnField.get(object), columnAttr);
						row.put(columnAttr.getFieldName(), columnValue);
					} catch (Exception e) {
						AppCache.displayTextOnMainFrame("Error getting values for " + columnAttr.getLabel(), 1);
						e.printStackTrace();
					}
				}else if(attribute instanceof JoinColumnAttribute) {
					JoinColumnAttribute jcAttribute = (JoinColumnAttribute)attribute;
					ArrayList<ColumnAttribute> columnRefs = (ArrayList<ColumnAttribute>) jcAttribute.getColumns();
					Class zoomClass = jcAttribute.getLookupClass();
					String zoomVals = "";
					try {
						Field f = oCLass.getDeclaredField(jcAttribute.getFieldName());
						f.setAccessible(true);
						Object o = f.get(object);
						for (ColumnAttribute columnAttribute : columnRefs) {
							try {
								Field field = zoomClass.getDeclaredField(columnAttribute.getFieldName());
								field.setAccessible(true);
								zoomVals += field.get(o) + ", ";
								
							} catch (Exception e) {
								AppCache.displayTextOnMainFrame("Error getting zoom values for " + jcAttribute.getLabel(), 1);
								e.printStackTrace();
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					if(zoomVals.equals("")) {
						zoomVals = NULL;
					}else {
						if(columnRefs.size() > 1) {
							zoomVals = zoomVals.substring(zoomVals.indexOf(',') + 1);
						}
						zoomVals = zoomVals.substring(0, zoomVals.length() -2);
					}
					row.put(jcAttribute.getFieldName(), zoomVals);
				}
			}
			model.add(row);
		}
		
		return model;
	}

	public EntityBean getBean() {
		return bean;
	}
}
