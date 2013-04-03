package adapt.resources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
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

import adapt.utils.EntityClass;
import adapt.utils.EntityCreator;
import adapt.utils.Settings;
import adapt.utils.XMLAttribute;
import adapt.utils.XMLManyToManyAttribute;
import adapt.utils.XMLResource;
import adapt.utils.XMLManyToOneAttribute;
import adapt.application.AdaptApplication;

public class AddResource extends BaseResource {

	Map<String, Object> dataModel = new TreeMap<String, Object>();
	XMLResource resource;
	EntityCreator creator;
	
	public AddResource(Context context, Request request, Response response) {
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
		String amtmresName = (String)getRequest().getAttributes().get("amtmresName");
		AdaptApplication app = (AdaptApplication) getApplication();
		creator = new EntityCreator(app);
		//obicno dodavanje
		if(resName != null) {
			prepareAdd(resName);
		}
		//many-to-many dodavanje
		if(amtmresName != null) {
			String mtmid = (String)getRequest().getAttributes().get("mtmid");
			String mtmchild = (String)getRequest().getAttributes().get("mtmchild");
			System.out.println("mtm dodavanje u " + amtmresName + " sa id " + mtmid + " childova: " + mtmchild);
			Long id = Long.parseLong(mtmid);
			prepareMTMAdd(amtmresName, id, mtmchild);
		}
		super.handleGet();
	}

	public void prepareAdd(String resName) {
		AdaptApplication app = (AdaptApplication) getApplication();
		EntityManager em = app.getEmf().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		resource = app.getXMLResource(resName);
		dataModel.put("resource", resource);
		//mapa child elemenata
		//kljuc je labela a vrednost opet mapa kojoj je kluc ID entiteta a vrednost ime, ukoliko child klasa ima 'name' atribut, inace je ID
		//na osnovu tih mapa se generise sadrzaj drop box-ova
		LinkedHashMap<String, Map<String, String>> childFormMap = new LinkedHashMap<String, Map<String,String>>();
		//svaki child atribut pokupim iz baze i pretvorim u EntityClass objekat
		for(int i=0; i<resource.getManyToOneAttributes().size(); i++) {
			XMLManyToOneAttribute mattr = resource.getManyToOneAttributes().get(i);
			XMLResource ress = app.getXMLResource(mattr.getType());
			ArrayList<Object> objects = (ArrayList<Object>) em.createQuery("FROM " + mattr.getType()).getResultList();
			ArrayList<EntityClass> entities;
			try {
				entities = creator.getEntities(objects);
				Map<String, String> childMap = new LinkedHashMap<String, String>();
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
					
					childMap.put(Id, name);
				}
				childFormMap.put(mattr.getLabel(), childMap);
			} catch (NoSuchFieldException e) {
//				try {
//					entities = EntityCreator.getEntities(objects, "id");
//					Map<String, String> childMap = new TreeMap<String, String>();
//					for(int j=0; j<entities.size(); j++) {
//						EntityClass ecl = entities.get(j);
//						String Id = EntityCreator.getEntityPropertyValue(ecl, "id");
//						childMap.put(Id, Id);
//					}
//					childFormMap.put(mattr.getLabel(), childMap);
//				} catch (NoSuchFieldException e1) {
//					e1.printStackTrace();
//				}
				e.printStackTrace();
			}
		}
		tx.commit();
		em.close();
		dataModel.put("childFormMap", childFormMap);
	}
	
	@SuppressWarnings("unchecked")
	public void prepareMTMAdd(String resName, Long id, String childName) {
		AdaptApplication application = (AdaptApplication) getApplication();
		EntityManager em = application.getEmf().createEntityManager();
		//resurs u koji se dodaje
		XMLResource ress = application.getXMLResource(resName);
		//resurs joji se dodaje
		XMLResource cress = application.getXMLResource(childName);
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Class rClass;
		try {
			//naziv mtm atributa u resursu u koji se dodaje
			String mtmattrName = "";
			for(int i=0; i<ress.getManyToManyAttributes().size(); i++) {
				XMLManyToManyAttribute mtmattr = ress.getManyToManyAttributes().get(i);
				if(mtmattr.getType().equals(cress.getName())) {
					mtmattrName = mtmattr.getName();
				}
			}
			rClass = Class.forName("adapt.entities." + resName);
			//na osnovu id-a nalazimo konkretan entitet u koji dodajemo
			Object res = em.find(rClass, id);
			if(res != null) {
				//citamo iz baze sve entitete resursa koji se dodaje
				String q = "FROM " + childName;
				ArrayList<Object> childern = (ArrayList<Object>) em.createQuery(q).getResultList();
				dataModel.put("resource", ress);
				dataModel.put("chresource", cress);
				dataModel.put("resObj", res);
				dataModel.put("mtm", "nja");
				//na osnovu imena atributa pozivamo get metodu
				//i dobijamo listu svih enititeta koji su vec dodani
				String getName = "get" + Character.toUpperCase(mtmattrName.charAt(0)) + mtmattrName.substring(1);
				Method get = rClass.getDeclaredMethod(getName);
				get.setAccessible(true);
				ArrayList<Object> objs =  new ArrayList<Object>((Collection<Object>) get.invoke(res));
				//napravimo presek liste svih entiteta i liste vec dodatih
				//kako bi dobili listu samo onih entiteta koji jos nisu dodati
				childern.removeAll(objs);
				ArrayList<EntityClass> chEnt = creator.getEntities(childern);
				dataModel.put("childern", chEnt);//spisak za combo box
				ArrayList<EntityClass> ents = creator.getEntities(objs);
				dataModel.put("mtmchildEntities", ents);//spisak za 'Vec u...' tabelu
				ArrayList<String> childFormHeaders = new ArrayList<String>();
				childFormHeaders.add("ID");
				for(int j=0; j<cress.getAttributes().size(); j++) {
					XMLAttribute attr = cress.getAttributes().get(j);
					childFormHeaders.add(attr.getLabel());
				}
				for(int k=0; k<cress.getManyToOneAttributes().size(); k++) {
					XMLManyToOneAttribute mattr = cress.getManyToOneAttributes().get(k);
					childFormHeaders.add(mattr.getLabel());
				}
				dataModel.put("childFormHeaders", childFormHeaders);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
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
		return getHTMLTemplateRepresentation("addTemplate.html", dataModel);
	}
}
