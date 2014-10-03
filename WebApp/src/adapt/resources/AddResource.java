package adapt.resources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.model.ejb.AbstractAttribute;
import adapt.model.ejb.ColumnAttribute;
import adapt.model.ejb.EntityBean;
import adapt.model.ejb.JoinColumnAttribute;
import adapt.util.converters.ConverterUtil;
import adapt.util.ejb.PersisenceHelper;
import adapt.util.xml_readers.EntityReader;

public class AddResource extends BaseResource {

	EntityBean bean;

	public AddResource(Context context, Request request, Response response) {
		super(context, request, response);
	}

	@Override
	public void handleGet() {
		String entityName = (String)getRequest().getAttributes().get("entityName");
		String modifyId =(String)getRequest().getAttributes().get("modid");
		bean = EntityReader.load(entityName);
		if(bean != null) {
			Form addForm = getRequest().getEntityAsForm();
			try {
				add(addForm, modifyId);
				addToDataModel("css", "messageOk");
				String message = "Row has been successfuly added to table \"" + bean.getLabel() + "\"";
				if(modifyId != null) {
					message = "Row has been successfuly modified.";
				}
				addToDataModel("message", message);
			} catch (Exception e) {
				addToDataModel("css", "messageError");
				addToDataModel("message", e.getMessage());
				e.printStackTrace();
			} 
		}else {
			addToDataModel("css", "messageError");
			addToDataModel("message", "Cannot find corresponding entity class in database.");
		}
		super.handleGet();
	}

	@Override
	public void handlePost() {
		handleGet();
	}

	/**
	 * Persists data from form to database
	 * @param form
	 */
	public void add(Form form, String modifyId) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, String> formValues = form.getValuesMap();
		Class entityClass = bean.getEntityClass();
		Object instance;
		if(modifyId != null) {
			instance = getObjectFromDB(entityClass.getName(), modifyId);
		}else {
			instance = entityClass.newInstance();
		}
		Iterator<Entry<String, String>> i = formValues.entrySet().iterator();
		while(i.hasNext()) {
			Entry<String, String> pair = i.next();
			// Extract name-value pairs from submited form
			String name = pair.getKey();
			String value = pair.getValue();
			if(!name.equals("null") && !value.equals("null")) {
				String nameCap = Character.toUpperCase(name.charAt(0)) + name.substring(1);
				for (AbstractAttribute attribute : bean.getAttributes()) {
					// get corresponding sttribute info based on fieldName
					if(attribute.getFieldName().equals(name)) {
						if(attribute instanceof ColumnAttribute) {
							ColumnAttribute column = (ColumnAttribute) attribute;
							Class paramClass  = Class.forName(column.getDataType().split(":")[0]);
							Method setter = entityClass.getMethod("set" + nameCap, paramClass);
							setter.setAccessible(true);
							Object val = ConverterUtil.convert(value, column);
							setter.invoke(instance, val);
						}else if(attribute instanceof JoinColumnAttribute) {
							JoinColumnAttribute jcAttribute = (JoinColumnAttribute) attribute;
							Class paramClass = jcAttribute.getLookupClass();
							Method setter = entityClass.getDeclaredMethod("set" + nameCap, paramClass);
							setter.setAccessible(true);
							System.out.println("[ADD] joinColumn=" + jcAttribute.getFieldName() + ", paramClass=" + paramClass.getName() + ", setter" + "set" + nameCap +
									", mozda id=" + value);
							Object zoomObject = getObjectFromDB(paramClass.getName(), value);
							if(zoomObject != null) {
								setter.invoke(instance, zoomObject);
							}
						}
					}
				}
			}
		}
		EntityManager em = PersisenceHelper.createEntityManager();
		em.getTransaction().begin();
		if(modifyId == null) {
			em.persist(instance);
		}else {
			em.merge(instance);
		}
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		return getHTMLTemplateRepresentation("popupTemplate.html", dataModel);
	}

}
