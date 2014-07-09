package adapt.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.restlet.Context;
import org.restlet.data.CharacterSet;
import org.restlet.data.Encoding;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;

import adapt.application.AdaptApplication;
import freemarker.template.Configuration;

public class BaseResource extends Resource {

	
	protected Representation getTemplateRepresentation(String templateName,
            Map<String, Object> dataModel, MediaType mt) {
        // The template representation is based on Freemarker.
        return new TemplateRepresentation(templateName, getFmcConfiguration(),
                dataModel, mt);
    }
    
	public Representation getHTMLTemplateRepresentation(String templateName,
			Map<String, Object> dataModel) {
			TemplateRepresentation represntation = new TemplateRepresentation(templateName, getFmcConfiguration(),
					dataModel, MediaType.TEXT_HTML);
			Encoding enc = new Encoding("UTF-8");
			List<Encoding> encodings = new ArrayList<Encoding>();
			encodings.add(enc);
			represntation.setCharacterSet(CharacterSet.UTF_8);
			represntation.setEncodings(encodings);
		// The template representation is based on Freemarker.
		return represntation; 
	}
	
    private Configuration getFmcConfiguration() {
        final AdaptApplication application = (AdaptApplication) getApplication();
    	return application.getFmc();
    }

	public BaseResource(Context context, Request request, Response response) {
		super(context, request, response);
	}
	
	public void prepareContent(Map<String, Object> model, EntityManager em) {
	}
	
}
