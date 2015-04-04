package adapt.resources;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.core.AdaptApplication;
import adapt.util.staticnames.Settings;

public class HomeResource extends BaseResource {

	public HomeResource(Context context, Request request, Response response) {
		super(context, request, response);
	}

	@Override
	public void handleGet() {
		prepareContent();
		super.handleGet();
	}

	public void prepareContent() {
		dataModel.put("title", Settings.APP_TITLE);
		dataModel.put("page_description", Settings.APP_DESCRIPTION);
	}
	
	@Override
    public Representation represent(Variant variant) throws ResourceException {
        return getHTMLTemplateRepresentation("homepage.html", dataModel);
    }
}