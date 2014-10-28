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
				ArrayList<String> columns = getLookupValuesJSON(joinColumn, id);
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

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		return getHTMLTemplateRepresentation("zoomValues.JSON", dataModel);
	}

}
