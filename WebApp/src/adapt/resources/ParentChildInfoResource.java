package adapt.resources;

import java.util.ArrayList;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.enumerations.OpenedAs;
import adapt.enumerations.PanelType;
import adapt.model.panel.AdaptPanel;
import adapt.model.panel.AdaptParentChildPanel;
import adapt.util.xml_readers.PanelReader;

/**
 * Restlet resource that fetches list of standard panels contained in parent-child panel
 * and passes it to HTML page as JSON list
 * @author Milorad Filipovic
 */
public class ParentChildInfoResource extends BaseResource {

	public ParentChildInfoResource(Context context, Request request,Response response) {
		super(context, request, response);
	}

	@Override
	public void handleGet() {
		String panelName = (String) getRequest().getAttributes().get("pcPanel");
		if(panelName != null) {
			//AdaptPanel panel = PanelReader.loadPanel(panelName, PanelType.PARENTCHILDPANEL, null, OpenedAs.DEFAULT);
			//AdaptParentChildPanel pcPanel = (AdaptParentChildPanel)panel;
			ArrayList<String> panles = PanelReader.getJSONPanelList(panelName);
			addToDataModel("panels", panles);
		}
		super.handleGet();
	}

	@Override
	public void handlePost() {
		handleGet();
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		return getHTMLTemplateRepresentation("panels.JSON", dataModel);
	}

}
