package adapt.resources;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.util.staticnames.Settings;

public class IndexResource extends BaseResource {

	public IndexResource(Context context, Request request, Response response) {
		super(context, request, response);
	}
	
	@Override
	public void handleGet() {
		prepareContent();
		super.handleGet();
	}

	public void prepareContent() {
		dataModel.put("page_title", Settings.APP_TITLE);
		dataModel.put("page_description", Settings.APP_DESCRIPTION);
		dataModel.put("username_label", Settings.LOGIN_USERNAME);
		dataModel.put("password_label", Settings.LOGIN_PASSWORD);
	}
	
	@Override
    public Representation represent(Variant variant) throws ResourceException {
        return getHTMLTemplateRepresentation("login.html", dataModel);
    }
}
