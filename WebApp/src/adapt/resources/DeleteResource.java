package adapt.resources;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.application.AdaptApplication;
import adapt.utils.XMLResource;
import freemarker.template.Configuration;

public class DeleteResource extends Resource {

	Map<String, Object> dataModel = new TreeMap<String, Object>();
	XMLResource resource;
	
	public DeleteResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_HTML));
	}
	
	public Representation getTemplateRepresentation(String templateName,
            Map<String, Object> dataModel, MediaType mt) {
        // The template representation is based on Freemarker.
        return new TemplateRepresentation(templateName, getFmcConfiguration(),
                dataModel, mt);
    }
    
	public Representation getHTMLTemplateRepresentation(String templateName,
            Map<String, Object> dataModel) {
        // The template representation is based on Freemarker.
        return new TemplateRepresentation(templateName, getFmcConfiguration(),
                dataModel, MediaType.TEXT_HTML);
    }
	
    private Configuration getFmcConfiguration() {
        final AdaptApplication application = (AdaptApplication) getApplication();
    	return application.getFmc();
    }
    
    public void handleGet() {
		AdaptApplication application = (AdaptApplication) getApplication();
		String delid	= (String)getRequest().getAttributes().get("delid");
		
		if (delid != null) {//brisanje
			String dresName	= (String)getRequest().getAttributes().get("dresName");
			resource = application.getXMLResource(dresName);
			Long delIdLong = Long.parseLong(delid);
			remove(dresName, delIdLong);
		}
		super.handleGet();
    }
	
    @Override
	public Representation represent(Variant variant) throws ResourceException {
		return getHTMLTemplateRepresentation("popupTemplate.html", getDataModel());
	}
    
    public Map<String, Object> getDataModel() {
		return dataModel;
	}

	public void setDataModel(Map<String, Object> dataModel) {
		this.dataModel = dataModel;
	}

	public XMLResource getResource() {
		return resource;
	}

	public void setResource(XMLResource resource) {
		this.resource = resource;
	}
    
  //------------------------------------BRISANJE------------------------------
  	public void remove(String table, Long id) {
  		AdaptApplication application = (AdaptApplication) getApplication();
  		EntityManager em = application.getEmf().createEntityManager();
  		EntityTransaction t = em.getTransaction();
  		t.begin();
  		String q = "FROM " + table + " t WHERE t.id=:did";
  		Object o = em.createQuery(q).setParameter("did", id).getSingleResult();
  		em.remove(o);
  		try {
  			t.commit();
  			dataModel.put("css", "messageOk");
			dataModel.put("message", "Row has been successfuly deleted");
  		} catch (Exception e1) {
  			dataModel.put("css", "messageError");
			dataModel.put("message", "Unable to delete entity. Delete child entries first.");
  		}
  	}
}
