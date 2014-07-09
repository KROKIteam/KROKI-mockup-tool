package adapt.resources;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.application.AdaptApplication;
import adapt.utils.Settings;

public class ProfilIzmenaResource extends BaseResource {

	Map<String, Object> dataModel = new TreeMap<String, Object>();
	
	public ProfilIzmenaResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_HTML));
	}

	@Override
	public void prepareContent(Map<String, Object> model, EntityManager em) {
		super.prepareContent(model, em);
	}

	@Override
	public void handleGet() {
		AdaptApplication app = (AdaptApplication) getApplication();
		EntityManager em = app.getEmf().createEntityManager();
		prepareContent(dataModel, em);
	}

	@Override
	public void handlePost() {
		handleGet();
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		dataModel.put("title", Settings.APP_TITLE);
		return getHTMLTemplateRepresentation("profilIzmena.html", dataModel);
	}
}
