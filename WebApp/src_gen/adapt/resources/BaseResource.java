package adapt.resources;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.restlet.Context;
import org.restlet.data.CharacterSet;
import org.restlet.data.Encoding;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;

import adapt.core.AdaptApplication;
import adapt.core.AppCache;
import adapt.exceptions.EntityAttributeNotFoundException;
import adapt.model.ejb.AbstractAttribute;
import adapt.model.ejb.ColumnAttribute;
import adapt.model.ejb.EntityBean;
import adapt.model.ejb.JoinColumnAttribute;
import adapt.model.panel.AdaptStandardPanel;
import adapt.util.converters.ConverterUtil;
import adapt.util.ejb.EntityHelper;
import adapt.util.ejb.PersisenceHelper;
import adapt.util.resolvers.ComponentTypeResolver;
import adapt.util.staticnames.Settings;
import adapt.util.xml_readers.PanelReader;
import freemarker.template.Configuration;

public class BaseResource extends Resource {

	protected Map<String, Object> dataModel = new LinkedHashMap<String, Object>();
	String NULL = Settings.NULL_VALUE;
	
	public BaseResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_HTML));
	}
	
	protected Representation getTemplateRepresentation(String templateName,
			Map<String, Object> dataModel, MediaType mt) {
		// The template representation is based on Freemarker.
		return new TemplateRepresentation(templateName, getFmcConfiguration(), dataModel, mt);
	}

	public Representation getHTMLTemplateRepresentation(String templateName,
			Map<String, Object> dataModel) {
		TemplateRepresentation represntation = new TemplateRepresentation(File.separator + "pages" + File.separator + templateName, getFmcConfiguration(), dataModel, MediaType.TEXT_HTML);
		Encoding enc = new Encoding("UTF-8");
		List<Encoding> encodings = new ArrayList<Encoding>();
		encodings.add(enc);
		represntation.setCharacterSet(CharacterSet.UTF_8);
		represntation.setEncodings(encodings);
		return represntation; 
	}

	private Configuration getFmcConfiguration() {
		final AdaptApplication application = (AdaptApplication) getApplication();
		return application.getFmc();
	}

	@Override
	public void handlePost() {
		handleGet();
	}
	
	// -------------------------------------------------------------------------| COMMON UTIL METHODS
	
	public void addToDataModel(String key, Object val) {
		dataModel.put(key, val);
	}
	
	protected Object getObjectFromDB(String className, String id) {
		EntityManager em = PersisenceHelper.createEntityManager();
		try {
			Class claz = Class.forName(className);
			return em.find(claz, Long.parseLong(id));
		} catch (Exception e) {
			return null;
		}
	}
	
	public void prepareInputForm(AdaptStandardPanel panel) {
		Map<String, String> inputForm = new LinkedHashMap<String, String>();
		EntityBean bean = panel.getEntityBean();
		LinkedHashMap<String, String> zoomMap = new LinkedHashMap<String, String>();
		
		for (AbstractAttribute attribute : bean.getAttributes()) {
			String fieldName = attribute.getFieldName();
			if(attribute instanceof ColumnAttribute) {
				if(!attribute.getHidden()) {
					ColumnAttribute colAttribute = (ColumnAttribute)attribute;
					String type = colAttribute.getDataType();
					if(colAttribute.getEnumeration() != null) {
						type += ":ComboBox";
					}
					String componentTemplate = ComponentTypeResolver.getTemplate(type);
					inputForm.put(fieldName, componentTemplate);
				}
			}else if(attribute instanceof JoinColumnAttribute){
				JoinColumnAttribute jcAttribute = (JoinColumnAttribute)attribute;
				String type = "kroki.joinColumn";
				String componentTemplate = ComponentTypeResolver.getTemplate(type);
				// For join columns first assign zoom filed template
				inputForm.put(fieldName, componentTemplate);
				// Then get data from database to fill the combo box
				// TODO Substitute combo box zoom with classic lookup fields
				/*LinkedHashMap<String, String> zoomValues = getZoomMap(jcAttribute);
				if(!zoomValues.isEmpty()) {
					zoomMap.put(fieldName, zoomValues);
				}else {
					AppCache.displayTextOnMainFrame("Error getting zoom values for: " + bean.getName() + "." + fieldName, 1);
				}*/
				Class<?> zoomClass = jcAttribute.getLookupClass();
				String panelId = AppCache.getInstance().getPanelId(zoomClass.getName());
				zoomMap.put(fieldName, panelId);
				
			}else {
				System.out.println("ELSE!");
			}
		}
		addToDataModel("inputForm", inputForm);
		addToDataModel("zoomMap", zoomMap);
	}
	
	/*
	 * Gets data for zoom UI component from database
	 * Data is feched in map which keys represent referenced entities ids, and values are refferenced columns values separated by comma
	 */
	protected LinkedHashMap<String, String> getZoomMap(JoinColumnAttribute jcAttribute) {
		LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
		ArrayList<ColumnAttribute> columnRefs = (ArrayList<ColumnAttribute>) jcAttribute.getColumns();
		String q = "FROM " + jcAttribute.getLookupClass().getSimpleName();
		EntityManager em = PersisenceHelper.createEntityManager();
		em.getTransaction().begin();
		Query query = em.createQuery(q);
		List<Object> results = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
		
		// TODO check if zoom attribute is required before adding the NULL value
		values.put("null", "-- " + NULL + "--");
		
		// Get referenced entity class
		Class oClass = jcAttribute.getLookupClass();
		// For each returned object from database, get referenced attribute values using reflection
		for (Object object : results) {
			try {
				Field idField = oClass.getDeclaredField("id");
				idField.setAccessible(true);
				String idVal = idField.get(object).toString();
				String refValues = getZoomValues(columnRefs, oClass, object);
				values.put(idVal.toString(), refValues);
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return values;
	}
	
	private String getZoomValues(ArrayList<ColumnAttribute> columnRefs, Class oClass, Object object) {
		String colRefs = "";
		for (ColumnAttribute columnAttribute : columnRefs) {
			try {
				Field field = oClass.getDeclaredField(columnAttribute.getFieldName());
				field.setAccessible(true);
				String fieldVal = field.get(object).toString();
				colRefs += fieldVal.toString() + ", ";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		String stripComma = colRefs.substring(0, colRefs.length() -2);
		return stripComma;
	}
	
	// Get attribute values for given EJB bean using reflection
	protected ArrayList<String> getLookupValuesJSON(JoinColumnAttribute joinColumn, String id) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		ArrayList<String> values = new ArrayList<String>();
		Class ejbClass = joinColumn.getLookupClass();
		
		String query = "FROM " + ejbClass.getSimpleName() + " x WHERE x.id = " + id;
		EntityManager em = PersisenceHelper.createEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery(query);
		Object result = q.getSingleResult();

		if(result != null) {
			//go trough referenced columns and get their values
			for (ColumnAttribute column : joinColumn.getColumns()) {
				try {
					Field columnField = ejbClass.getDeclaredField(column.getFieldName());
					columnField.setAccessible(true);
					String columnValue = ConverterUtil.convertForViewing(columnField.get(result), column);
					// FORMAT: "name": "State-id", "value": 1
					values.add("\"name\": \"" + joinColumn.getFieldName() + "-" + column.getFieldName() + "\", \"value\": \"" + columnValue + "\"");
				} catch (Exception e) {
					System.out.println("[getLookupValuesJSON] " + e.getClass().getSimpleName() + ": " + e.getMessage());
				}
			}
		}
		em.getTransaction().commit();
		em.close();
		return values;
	}

	protected JoinColumnAttribute getJoinByFieldName(EntityBean bean, String fieldName) throws EntityAttributeNotFoundException {
		Iterator<AbstractAttribute> it = bean.getAttributes().iterator();
		AbstractAttribute attr = null;
		while (it.hasNext()) {
			attr = it.next();
			if (attr instanceof JoinColumnAttribute) {
				if (((JoinColumnAttribute) attr).getFieldName().equals(fieldName)) {
					return (JoinColumnAttribute) attr;
				}
			}
		}
		throw new EntityAttributeNotFoundException(
				"Entity attribute not found with the field name '"
						+ fieldName + "' in entity class '"
						+ bean.getEntityClass().getName() + "'");
	}

	public Map<String, Object> getDataModel() {
		return dataModel;
	}

	public void setDataModel(Map<String, Object> dataModel) {
		this.dataModel = dataModel;
	}
}
