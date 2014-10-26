package adapt.resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.core.AppCache;
import adapt.enumerations.OpenedAs;
import adapt.enumerations.PanelType;
import adapt.exceptions.EntityAttributeNotFoundException;
import adapt.model.ejb.AbstractAttribute;
import adapt.model.ejb.ColumnAttribute;
import adapt.model.ejb.EntityBean;
import adapt.model.ejb.JoinColumnAttribute;
import adapt.model.panel.AdaptPanel;
import adapt.model.panel.AdaptStandardPanel;
import adapt.util.converters.ConverterUtil;
import adapt.util.ejb.EntityHelper;
import adapt.util.ejb.PersisenceHelper;
import adapt.util.xml_readers.PanelReader;

/**
 * Restlet resource that fetches list of representative attributes for given EJB bean
 * and passes it to HTML page as JSON list that populates zoom fileds when form is zoomed-out
 * @author Milorad Filipovic
 */
public class GetZoomsResource extends BaseResource {

	public GetZoomsResource(Context context, Request request, Response response) {
		super(context, request, response);
	}

	@Override
	public void handleGet() {
		String panelName = (String) getRequest().getAttributes().get("panelName");
		String id = (String) getRequest().getAttributes().get("zid");
		String zoomName = (String) getRequest().getAttributes().get("zoomName");
		if(panelName != null && id != null && zoomName != null) {
			AdaptPanel panel = PanelReader.loadPanel(panelName, PanelType.STANDARDPANEL, null, OpenedAs.DEFAULT);
			AdaptStandardPanel stdPanel = (AdaptStandardPanel)panel;
			EntityBean bean = stdPanel.getEntityBean();
			try {
				JoinColumnAttribute joinColumn = getJoinByFieldName(bean, zoomName);
				ArrayList<String> columns = getZoomValues(joinColumn, id);
				addToDataModel("zoomValues", columns);
			} catch (Exception e) {
				AppCache.displayStackTraceOnMainFrame(e);
			}
		}
		super.handleGet();
	}

	@Override
	public void handlePost() {
		handleGet();
	}

	// Get attribute values for given EJB bean using reflection
	private ArrayList<String> getZoomValues(JoinColumnAttribute joinColumn, String id) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
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
				Field columnField = ejbClass.getDeclaredField(column.getFieldName());
				columnField.setAccessible(true);
				String columnValue = ConverterUtil.convertForViewing(columnField.get(result), column);
				// FORMAT: "name": "State-id", "value": 1
				values.add("\"name\": \"" + ejbClass.getSimpleName() + "-" + column.getFieldName() + "\", \"value\": \"" + columnValue + "\"");
			}
		}
		em.getTransaction().commit();
		em.close();
		return values;
	}

	private JoinColumnAttribute getJoinByFieldName(EntityBean bean, String fieldName) throws EntityAttributeNotFoundException {
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
	
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		return getHTMLTemplateRepresentation("zoomValues.JSON", dataModel);
	}

}
