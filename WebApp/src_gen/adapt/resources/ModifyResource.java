package adapt.resources;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.core.AppCache;
import adapt.enumerations.OpenedAs;
import adapt.enumerations.PanelType;
import adapt.model.ejb.AbstractAttribute;
import adapt.model.ejb.ColumnAttribute;
import adapt.model.ejb.EntityBean;
import adapt.model.ejb.JoinColumnAttribute;
import adapt.model.panel.AdaptStandardPanel;
import adapt.util.converters.ConverterUtil;
import adapt.util.ejb.EntityHelper;
import adapt.util.xml_readers.PanelReader;

/**
 * Restlet resource that prepares edit form
 * @author Milorad Filipovic
 */
public class ModifyResource extends BaseResource{

	public ModifyResource(Context context, Request request, Response response) {
		super(context, request, response);
	}

	@Override
	public void handlePost() {
		handleGet();
	}

	@Override
	public void handleGet() {
		String panelName = (String)getRequest().getAttributes().get("panelName");
		String modifyID =  (String)getRequest().getAttributes().get("mid");
		String parentID =  (String)getRequest().getAttributes().get("pid");

		if(panelName != null && modifyID != null) {
			AdaptStandardPanel stdPanel = (AdaptStandardPanel) PanelReader.loadPanel(panelName, PanelType.STANDARDPANEL, null, OpenedAs.DEFAULT);
			if(stdPanel != null) {
				Object o = getObjectFromDB(stdPanel.getEntityBean().getEntityClass().getName(), modifyID);
				EntityBean bean = stdPanel.getEntityBean();
				addToDataModel("panel", stdPanel);
				addToDataModel("entityClassName", stdPanel.getEntityBean().getEntityClass().getName());
				prepareInputForm(stdPanel);
				prepareEditMap(bean, o);
				addToDataModel("modid", modifyID);
			}else {
				addToDataModel("css", "messageError");
				addToDataModel("message", "Unable to modify entry. Panel NULL");
			}
		}else {
			// TODO Handle errors
		}

		super.handleGet();
	}

	private LinkedHashMap<String, String> prepareEditMap(EntityBean bean, Object object) {
		LinkedHashMap<String, String> editMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, LinkedHashMap<String, String>> zoomEditMap = new LinkedHashMap<String, LinkedHashMap<String,String>>();
		Class objectClass = object.getClass();

		for (AbstractAttribute attribute : bean.getAttributes()) {
			String getName = "get" + Character.toUpperCase(attribute.getFieldName().charAt(0)) + attribute.getFieldName().substring(1);
			try {
				if(!attribute.getHidden()) {
					if(attribute instanceof ColumnAttribute) {
						ColumnAttribute columnAttribute = (ColumnAttribute)attribute;
						String className = columnAttribute.getDataType().split(":")[0];
						Class attributeClass = Class.forName(className);
						Field field = objectClass.getDeclaredField(columnAttribute.getFieldName());
						field.setAccessible(true);
						String value = ConverterUtil.convertForViewing(field.get(object), columnAttribute);
						System.out.println("[ADDING TO EDIT MAP] " + attribute.getFieldName() + ", " + value);
						editMap.put(attribute.getFieldName(), value);
					}else if(attribute instanceof JoinColumnAttribute) {
						JoinColumnAttribute jcAttribute = (JoinColumnAttribute)attribute;
						Class jcClass = jcAttribute.getLookupClass();
						Field field = objectClass.getDeclaredField(jcAttribute.getFieldName());
						field.setAccessible(true);
						Object value = field.get(object);
						//Join column field returns the whole lookup object, so we need to extract just the referenced values
						LinkedHashMap<String, String> lookupMap = new LinkedHashMap<String, String>();
						for (ColumnAttribute column : jcAttribute.getColumns()) {
							try {
								Field columnField = jcClass.getDeclaredField(column.getFieldName());
								columnField.setAccessible(true);
								String columnValue = ConverterUtil.convertForViewing(columnField.get(value), column);
								lookupMap.put(column.getFieldName(), columnValue);
							} catch (NullPointerException e) {
								// e.printStackTrace();
							} catch (NoSuchFieldError nsfe) {
								// nsfe.printStackTrace();
							}
						}
						zoomEditMap.put(attribute.getFieldName(), lookupMap);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				AppCache.displayStackTraceOnMainFrame(e);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				AppCache.displayStackTraceOnMainFrame(e);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				AppCache.displayStackTraceOnMainFrame(e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				AppCache.displayStackTraceOnMainFrame(e);
			}
		}
		addToDataModel("editMap", editMap);
		addToDataModel("zoomEditMap", zoomEditMap);
		return editMap;
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		return getHTMLTemplateRepresentation("editFormTemplate.html", dataModel);
	}

}
