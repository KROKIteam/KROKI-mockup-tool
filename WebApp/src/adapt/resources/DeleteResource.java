package adapt.resources;

import javax.persistence.EntityManager;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.core.AppCache;
import adapt.enumerations.OpenedAs;
import adapt.enumerations.PanelType;
import adapt.model.panel.AdaptStandardPanel;
import adapt.util.ejb.PersisenceHelper;
import adapt.util.xml_readers.PanelReader;

public class DeleteResource extends BaseResource {

	public DeleteResource(Context context, Request request, Response response) {
		super(context, request, response);
	}

	@Override
	public void handleGet() {
		String panelName = (String)getRequest().getAttributes().get("panelName");
		String delId = (String)getRequest().getAttributes().get("delid");
		
		if(panelName != null && delId != null) {
			AdaptStandardPanel panel = (AdaptStandardPanel) PanelReader.loadPanel(panelName, PanelType.STANDARDPANEL, null, OpenedAs.DEFAULT);
			if(panel != null) {
				remove(panel.getEntityBean().getEntityClass().getName(), Long.parseLong(delId));
			}else {
				addToDataModel("css", "messageError");
				addToDataModel("message", "Unable to delete entry. Panel NULL");
			}
		}else {
			//TODO Handle errors
		}
		
		super.handleGet();
	}

	private void remove(String entityName, Long id) {
		EntityManager em = PersisenceHelper.createEntityManager();
		em.getTransaction().begin();
		String q = "FROM " + entityName + " x WHERE x.id=:did";
		Object o = em.createQuery(q).setParameter("did", id).getSingleResult();
		em.remove(o);
		try {
			em.getTransaction().commit();
			em.close();
			addToDataModel("css", "messageOk");
			addToDataModel("message", "Row has been successfully deleted!");
		} catch (Exception e) {
			addToDataModel("css", "messageError");
			addToDataModel("message", "Unable to delete entry.");
		}
	}
	
	
	@Override
	public void handlePost() {
		handleGet();
	}
	
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		return getHTMLTemplateRepresentation("popupTemplate.html", dataModel);
	}
}
