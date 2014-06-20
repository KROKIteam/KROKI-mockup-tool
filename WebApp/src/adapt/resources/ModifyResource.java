package adapt.resources;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.application.AdaptApplication;
import adapt.utils.EntityClass;
import adapt.utils.EntityCreator;
import adapt.utils.EntityProperty;
import adapt.utils.Settings;
import adapt.utils.XMLAttribute;
import adapt.utils.XMLManyToOneAttribute;
import adapt.utils.XMLResource;

public class ModifyResource extends BaseResource {

	Map<String, Object> dataModel = new TreeMap<String, Object>();
	XMLResource resource;
	EntityCreator creator;
	
	public ModifyResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_HTML));
	}

	@Override
	public void prepareContent(Map<String, Object> model, EntityManager em) {
		super.prepareContent(model, em);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleGet() {
		String resName = (String)getRequest().getAttributes().get("aresName");
		String modId = (String)getRequest().getAttributes().get("mid");
		AdaptApplication application = (AdaptApplication) getApplication();
		creator = new EntityCreator(application);
		if(resName != null &&  modId != null) {
			AdaptApplication app = (AdaptApplication) getApplication();
			resource = app.getXMLResource(resName);
			dataModel.put("resource", resource);
			Long id = Long.parseLong(modId);
			EntityManager em = app.getEmf().createEntityManager();
			EntityTransaction tx = em.getTransaction();
			ArrayList<Object> obejcts = new ArrayList<Object>();
			tx.begin();
			Object o = em.createQuery("FROM " + resName + " o WHERE o.id=:oid").setParameter("oid", id).getSingleResult();
			tx.commit();
			obejcts.add(o);
			EntityClass entity = null;
			try {
				entity = creator.getEntities(obejcts).get(0);
			} catch (NoSuchFieldException e) {
//				try {
//					entity = EntityCreator.getEntities(obejcts, "id").get(0);
//				} catch (NoSuchFieldException e1) {
//					e1.printStackTrace();
//				}
				e.printStackTrace();
			}
			if(entity != null) {
				//za obicne atribute kreiramo liste sa labelama i vrednostima
				ArrayList<String> attributeLabels = new ArrayList<String>();
				ArrayList<String> attributeValues = new ArrayList<String>();
				//za child atribute kreiramo mapu
				LinkedHashMap<String, Map<String, String>> childFormMap = new LinkedHashMap<String, Map<String,String>>();
				
				for(int i=0; i<entity.getProperties().size();i++) {
					EntityProperty prop = entity.getProperties().get(i);
					for(int j=0; j<resource.getAttributes().size(); j++) {
						XMLAttribute attr = resource.getAttributes().get(j);
						if(prop.getName().equals(attr.getName())) {
							attributeLabels.add(attr.getLabel());
							attributeValues.add(prop.getValue().toString());
						}
					}
					//za child atribute
					for(int k=0; k<resource.getManyToOneAttributes().size(); k++) {
						XMLManyToOneAttribute mattr = resource.getManyToOneAttributes().get(k);
						XMLResource ress = app.getXMLResource(mattr.getType());
						if(prop.getName().equals(mattr.getName())) {
							EntityTransaction t = em.getTransaction();
							t.begin();
							//pokupimo sve entitete iz baze
							ArrayList<Object> objs = (ArrayList<Object>) em.createQuery("FROM " + mattr.getType()).getResultList(); 
							ArrayList<EntityClass> entities;
							try {
								entities = creator.getEntities(objs);
								Map<String, String> childMap = new TreeMap<String, String>();
								if(!mattr.getMandatory()) {
									childMap.put("null", "-- None --");
								}
								for(int j=0; j<entities.size(); j++) {
									EntityClass ecl = entities.get(j);
									String Id = creator.getEntityPropertyValue(ecl, "id");
									String name = "";
									
									for (XMLAttribute attr : ress.getRepresentativeAttributes()) {
										name += creator.getEntityPropertyValue(ecl, attr.getName()) + ", ";
									}
									
									name = name.substring(0, name.length()-2);
									//objekte iz baze pretvorimo u EntityClass objekte
									//i spremimo u mapu sa vrednostima za combo box
									childMap.put(Id, name);
								}
								childFormMap.put(mattr.getLabel(), childMap);
							} catch (NoSuchFieldException e) {
//								try {
//									entities = EntityCreator.getEntities(objs, "id");
//									Map<String, String> childMap = new TreeMap<String, String>();
//									for(int j=0; j<entities.size(); j++) {
//										EntityClass ecl = entities.get(j);
//										String Id = EntityCreator.getEntityPropertyValue(ecl, "id");
//										childMap.put(Id, Id);
//									}
//									childFormMap.put(mattr.getLabel(), childMap);
//								} catch (NoSuchFieldException e1) {
//									e1.printStackTrace();
//								}
								e.printStackTrace();
							}
							t.commit();
						}
					}
				}
				
				dataModel.put("attributeLabels", attributeLabels);
				dataModel.put("attributeValues", attributeValues);
				dataModel.put("childFormMap", childFormMap);
				dataModel.put("modid", creator.getEntityPropertyValue(entity, "id"));
			}
			em.close();
		}
		super.handleGet();
	}

	@Override
	public void handlePost() {
		handleGet();
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		AdaptApplication app = (AdaptApplication) getApplication();
		EntityManager em = app.getEmf().createEntityManager();
		prepareContent(dataModel, em);
		dataModel.put("title", Settings.APP_TITLE);
		return getHTMLTemplateRepresentation("editFormTemplate.html", dataModel);
	}
}
