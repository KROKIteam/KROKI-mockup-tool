package adapt.resources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.smartcardio.ATR;
import javax.swing.text.DateFormatter;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.application.AdaptApplication;
import adapt.exception.RightAlreadyDefinedException;
import adapt.utils.EntityClass;
import adapt.utils.EntityCreator;
import adapt.utils.Settings;
import adapt.utils.XMLAttribute;
import adapt.utils.XMLManyToManyAttribute;
import adapt.utils.XMLManyToOneAttribute;
import adapt.utils.XMLResource;
import freemarker.template.Configuration;

public class ViewResource extends Resource {

	Map<String, Object> dataModel = new TreeMap<String, Object>();
	XMLResource resource;
	XMLResource childResource;
	EntityCreator creator;
	
	public ViewResource(Context context, Request request, Response response) {
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

	@SuppressWarnings("rawtypes")
	@Override
	public void handleGet() {
		AdaptApplication application = (AdaptApplication) getApplication();
			String resName	= (String)getRequest().getAttributes().get("resName");
			String delid	= (String)getRequest().getAttributes().get("delid");
			String modid	= (String)getRequest().getAttributes().get("modid");
			String aresName = (String)getRequest().getAttributes().get("arName");
			String cresName = (String)getRequest().getAttributes().get("cresName");
			String mcresName = (String)getRequest().getAttributes().get("mcresName");
			String mtmResName = (String)getRequest().getAttributes().get("mtmResName");
			
			creator = new EntityCreator(application);
			
			if(resName != null) {
				resource = application.getXMLResource(resName);
			}
			if (delid != null) {//brisanje
				String dresName	= (String)getRequest().getAttributes().get("dresName");
				resource = application.getXMLResource(dresName);
				Long delIdLong = Long.parseLong(delid);
				remove(dresName, delIdLong);
			}
			if (modid != null) {//izmena
				System.out.println("[VIEW RESOURCE] handleGet");
				String mresName = (String) getRequest().getAttributes().get("mresName");
				resource = application.getXMLResource(mresName);
				Form form = getRequest().getEntityAsForm();
				ArrayList<Object> values = getFormData(form);
				Long id = Long.parseLong(modid);
				try {
					modify(values, id);
				} catch (RightAlreadyDefinedException e) {
					e.printStackTrace();
				}
			}
			if(aresName != null) {//dodavanje
				resource = application.getXMLResource(aresName);
				Form form = getRequest().getEntityAsForm();
				ArrayList<Object> values = getFormData(form);
				try {
					modify(values, null);
				} catch (RightAlreadyDefinedException e) {
				}
			}
			if(cresName != null) {//child forma
				resource = application.getXMLResource(cresName);
				Form form = getRequest().getEntityAsForm();
				String child = form.getFirstValue("childern");
				String id = form.getFirstValue("selectChild");
				prepareChildern(application, cresName, id, child);
			}
			if(mcresName != null) {//many-to-many forma
				resource = application.getXMLResource(mcresName);
				Form form = getRequest().getEntityAsForm();
				String child = form.getFirstValue("MTMchildern");
				String id = form.getFirstValue("selectMTMChild");
				prepareMTMChildern(application, mcresName, id, child);
				dataModel.put("id", id);
				dataModel.put("childname", child);
			}
			if(mtmResName != null) {
				String reff = getRequest().getResourceRef().toString();
				if(reff.contains("dod")) {//many-to-many dodavanje
					String mtmcresName = (String)getRequest().getAttributes().get("mtmcresName");
					Form form = getRequest().getEntityAsForm();
					//id entiteta koji je izabran iz combo-boxa
					String sid = "mtmSelect" + mtmcresName;
					String cid = form.getFirstValue(sid);
					//id entiteta u koji se dodaje, prosledjuje se u zahtevu
					String mtmResId = (String)getRequest().getAttributes().get("mtmResId");
					
					XMLResource resXML = application.getXMLResource(mtmResName);
					XMLResource childXML = application.getXMLResource(mtmcresName);
					if(resXML != null && childXML != null) {
						resource = resXML;
						try {
							Class recClass = Class.forName("adapt.entities." + resXML.getName());
							Class childClass = Class.forName("adapt.entities." + childXML.getName());
							Long cidl = Long.parseLong(cid);
							Long resIdl = Long.parseLong(mtmResId);
							EntityManager em = application.getEmf().createEntityManager();
							EntityTransaction tx = em.getTransaction();
							tx.begin();
							//nadjemo entitet koji se dodaje
							Object childObj = em.find(childClass, cidl);
							//i entitet u koji se dodaje
							Object resObj = em.find(recClass, resIdl);
							if(childObj != null && resObj != null) {
								String mtmatt = "";
								XMLManyToManyAttribute mattr = null;
								//nadjemo naziv mtm atributa
								for(int i=0; i<resXML.getManyToManyAttributes().size(); i++) {
									XMLManyToManyAttribute mtmattr = resXML.getManyToManyAttributes().get(i);
									if(mtmattr.getType().equals(childXML.getName())) {
										mattr = mtmattr;
									}
								}
								//pozovemo add metodu
								String addName = "add" + Character.toUpperCase(mattr.getType().charAt(0)) + mattr.getType().substring(1);
								Method add = recClass.getDeclaredMethod(addName, childObj.getClass());
								add.invoke(resObj, childObj);
							}
							em.flush();
							tx.commit();
							em.close();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}else { //many-to-many brisanje
					String mtmResId = (String)getRequest().getAttributes().get("mtmResId");
					String mtmcresName = (String)getRequest().getAttributes().get("mtmcresName");
					String mtmChId = (String)getRequest().getAttributes().get("mtmChId");
					
					XMLResource resXML = application.getXMLResource(mtmResName);
					XMLResource childXML = application.getXMLResource(mtmcresName);
					if(resXML != null && childXML != null) {
						resource = resXML;
						try {
							Class recClass = Class.forName("adapt.entities." + resXML.getName());
							Class childClass = Class.forName("adapt.entities." + childXML.getName());
							Long cidl = Long.parseLong(mtmChId);
							Long resIdl = Long.parseLong(mtmResId);
							EntityManager em = application.getEmf().createEntityManager();
							EntityTransaction tx = em.getTransaction();
							tx.begin();
							Object childObj = em.find(childClass, cidl);
							Object resObj = em.find(recClass, resIdl);
							if(childObj != null && resObj != null) {
								String mtmatt = "";
								XMLManyToManyAttribute mattr = null;
								for(int i=0; i<resXML.getManyToManyAttributes().size(); i++) {
									XMLManyToManyAttribute mtmattr = resXML.getManyToManyAttributes().get(i);
									if(mtmattr.getType().equals(childXML.getName())) {
										mattr = mtmattr;
									}
								}
								
								String remName = "remove" + Character.toUpperCase(mattr.getType().charAt(0)) + mattr.getType().substring(1);
								Method rem = recClass.getDeclaredMethod(remName, childObj.getClass());
								rem.invoke(resObj, childObj);
							}
							em.flush();
							tx.commit();
							em.close();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
			prepareContent();
		super.handleGet();
	}

	@SuppressWarnings("unchecked")
	public void prepareContent() {
		AdaptApplication application = (AdaptApplication) getApplication();
		EntityManager em =application.getEmf().createEntityManager();
		if (resource != null) {
			//smestimo resurs u data model
			dataModel.put("resource", resource);
			//smestimo i listu sa ostalim resursima u data model
			//za moguce koristenje na nekim formama
			ArrayList<XMLResource> XMLresources = application.XMLResources;
			dataModel.put("XMLresources", XMLresources);
			//pokupimo labele atributa resursa koje sluze kao zaglavlja tabele
			ArrayList<String> headers = new ArrayList<String>();
			//headers.add("ID");
			for(int i=0; i<resource.getAttributes().size(); i++) {
				headers.add(resource.getAttributes().get(i).getLabel());
			}
			for(int i=0; i<resource.getManyToOneAttributes().size(); i++) {
				headers.add(resource.getManyToOneAttributes().get(i).getLabel());
			}
			dataModel.put("mainFormHeaders", headers);
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			//iz baze se ucitaju svi entiteti koji pripadaju trezenom resursu
			Query q = em.createQuery("FROM " + resource.getName());
			ArrayList<Object> ress = (ArrayList<Object>) q.getResultList();
			tx.commit();
			em.close();
			if(ress != null) {
				ArrayList<EntityClass> entities;
				try {
					//objekte koje nam vrati upit pomocu klase EntityCreator
					//pretvorimo u objekte klase EntityClass
					//i smestimo u data model
					entities = creator.getEntities(ress);
					if(!entities.isEmpty()) {
						Map<String, String> childMap = new LinkedHashMap<String, String>();
						
						for(int j=0; j<entities.size(); j++) {
							EntityClass ecl = entities.get(j);
							String Id = creator.getEntityPropertyValue(ecl, "id");
							String name = "";
							
							for (XMLAttribute attr : resource.getRepresentativeAttributes()) {
								name += creator.getEntityPropertyValue(ecl, attr.getName()) + ", ";
							}
							
							if(!name.equals("")) {
								name = name.substring(0, name.length()-2);
							}
							
							childMap.put(Id, name);
						}
						System.out.println("[ENTITIES] " + entities.size());
						dataModel.put("entities", entities);
						dataModel.put("childMap", childMap);
						
					}else {
						dataModel.put("msg", "No entries in the database for requested resource!");
					}
				} catch (NoSuchFieldException e) {
//					try {
//						//ako klasa nema name polje, ispisuje se ID
//						entities = EntityCreator.getEntities(ress, "id");
//						System.err.println("[INFO]Klasa nema 'name' polje, ispisujem ID");
//						if(!entities.isEmpty()) {
//							dataModel.put("entities", entities);
//						}else {
//							dataModel.put("msg", "No entries in the database for requested resource!");
//						}
//					} catch (NoSuchFieldException e1) {
//						//ovo ne bi trebalo nikada da se desi
//						System.err.println("[ERROR]Klasa nema ID polje :(");
//					}
					e.printStackTrace();
				}
			}
		}
	}
	
	public void prepareChildern(AdaptApplication application, String cresName, String id, String child) {
		EntityManager em = application.getEmf().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		childResource = application.getXMLResource(child);
		if(childResource != null) {
			for(int i=0; i<childResource.getManyToOneAttributes().size(); i++) {
				XMLManyToOneAttribute mattr = childResource.getManyToOneAttributes().get(i);
				XMLResource childRes = application.getXMLResource(mattr.getType());
				if(mattr.getType().equals(resource.getName())) {
					String q = "FROM " + child + " o WHERE o." + mattr.getName() + ".id = " + id;
					Query query = em.createQuery(q);
					ArrayList<Object> objs = (ArrayList<Object>) query.getResultList();
					try {
						ArrayList<EntityClass> childEntities = creator.getEntities(objs);
						dataModel.put("childEntities", childEntities);
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}
				}
			}
			for(int k=0;k<resource.getManyToManyAttributes().size();k++) {
				XMLManyToManyAttribute mtmattr = resource.getManyToManyAttributes().get(k);
				if(mtmattr.getType().equals(child)) {
					System.out.println("mtm atribut: " + mtmattr.getName());
					Class rClass;
					try {
						rClass = Class.forName("adapt.entities." + resource.getName());
						System.out.println("klasa: " + rClass.getSimpleName());
						Object rObj = em.find(rClass, Long.parseLong(id));
						String getName = "get" + Character.toUpperCase(mtmattr.getName().charAt(0)) + mtmattr.getName().substring(1);
						System.out.println("metoda: " + getName);
						Method get = rClass.getDeclaredMethod(getName);
						get.setAccessible(true);
						ArrayList<Object> objs =  new ArrayList<Object>((Collection<Object>) get.invoke(rObj));
						System.out.println("objekata: " + objs.size());
						try {
							ArrayList<EntityClass> childEntities = creator.getEntities(objs);
							dataModel.put("childEntities", childEntities);
						} catch (NoSuchFieldException e) {
//							try {
//								ArrayList<EntityClass> childEntities = EntityCreator.getEntities(objs, "id");
//								dataModel.put("childEntities", childEntities);
//							} catch (NoSuchFieldException e1) {
//								e1.printStackTrace();
//							}
							e.printStackTrace();
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			ArrayList<String> childFormHeaders = new ArrayList<String>();
			//childFormHeaders.add("ID");
			for(int j=0; j<childResource.getAttributes().size(); j++) {
				XMLAttribute attr = childResource.getAttributes().get(j);
				childFormHeaders.add(attr.getLabel());
			}
			for(int k=0; k<childResource.getManyToOneAttributes().size(); k++) {
				XMLManyToOneAttribute mattr = childResource.getManyToOneAttributes().get(k);
				childFormHeaders.add(mattr.getLabel());
			}
			dataModel.put("childFormHeaders", childFormHeaders);
			dataModel.put("childResource", childResource);
		}
	}
	
	public void prepareMTMChildern(AdaptApplication application, String mcresName, String id, String child) {
		EntityManager em = application.getEmf().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		String mtmattrName = "";
		childResource = application.getXMLResource(child);
		if(childResource != null) {
			for(int i=0; i<resource.getManyToManyAttributes().size(); i++) {
				XMLManyToManyAttribute mtmattr = resource.getManyToManyAttributes().get(i);
				if(mtmattr.getType().equals(childResource.getName())) {
					mtmattrName = mtmattr.getName();
				}
			}
			try {
				Class rClass = Class.forName("adapt.entities." + resource.getName());
				Object rObj = em.find(rClass, Long.parseLong(id));
				String getName = "get" + Character.toUpperCase(mtmattrName.charAt(0)) + mtmattrName.substring(1);
				Method get = rClass.getDeclaredMethod(getName);
				get.setAccessible(true);
				ArrayList<Object> objs =  new ArrayList<Object>((Collection<Object>) get.invoke(rObj));
				ArrayList<EntityClass> ents = creator.getEntities(objs);
				dataModel.put("mtmchildEntities", ents);
				ArrayList<String> childFormHeaders = new ArrayList<String>();
				childFormHeaders.add("ID");
				for(int j=0; j<childResource.getAttributes().size(); j++) {
					XMLAttribute attr = childResource.getAttributes().get(j);
					childFormHeaders.add(attr.getLabel());
				}
				for(int k=0; k<childResource.getManyToOneAttributes().size(); k++) {
					XMLManyToOneAttribute mattr = childResource.getManyToOneAttributes().get(k);
					childFormHeaders.add(mattr.getLabel());
				}
				dataModel.put("mtmheaders", childFormHeaders);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void handlePost() {
		handleGet();
	}

//---------------------------------------CITANJE FORME-----------------------------------------------
	public ArrayList<Object> getFormData(Form form) {
		ArrayList<Object> values = new ArrayList<Object>();
		AdaptApplication application = (AdaptApplication) getApplication();
		
		Map<String, String> vals = form.getValuesMap();
		Iterator i = vals.entrySet().iterator();
		while(i.hasNext()) {
			Map.Entry pairs = (Map.Entry)i.next();
			if(!pairs.getKey().toString().equals("submit")) {
				//atributi
				if(pairs.getKey().toString().startsWith("attr")) {
					if(pairs.getKey().toString().startsWith("attrSelectBool")) { //ako ima combobox, onda je boolean
						Boolean b = Boolean.parseBoolean(pairs.getValue().toString());
						values.add(b);
					}else {
						values.add(pairs.getValue().toString());
					}
				}else if(pairs.getKey().toString().startsWith("mattr")) { //manyToOne atributi
					int ind = pairs.getKey().toString().length()-1;
					System.out.println("pairs.getKey() = " + pairs.getKey().toString());
					System.out.println("charAt(" + ind + ")");
					String index = Character.toString( pairs.getKey().toString().charAt(ind));
					System.out.println("index = " + index);
					XMLManyToOneAttribute mattr = resource.getManyToOneAttributes().get(Integer.parseInt(index));
					EntityManager e = application.getEmf().createEntityManager();
					EntityTransaction tx = e.getTransaction();
					tx.begin();
					String q = "FROM " + mattr.getType() + " o WHERE o.id=:oid";
					Object o = null;
					if(!pairs.getValue().toString().equals("null")) {
						o = e.createQuery(q).setParameter("oid", Long.parseLong(pairs.getValue().toString())).getSingleResult();
					}
					tx.commit();
					values.add(o);
				}
			}
		}
		return values;
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
		} catch (Exception e1) {
			dataModel.put("msg", "Unable to delete entity. Delete child entries first!");
		}
	}
	
//------------------------------DODAVANJE I IZMENA-------------------------------
	public Object modify(ArrayList<Object> values, Long id) throws RightAlreadyDefinedException {
		String table = resource.getName();
		AdaptApplication application = (AdaptApplication) getApplication();
		EntityManager em = application.getEmf().createEntityManager();
		EntityTransaction t = em.getTransaction();
		t.begin();
		try {
			Class s = Class.forName("adapt.entities." + table);
			Object o;
			if(id == null) {//if no ID is passed, add operation is executed
				//new object
				o = s.newInstance();
			}else {//if ID is not null, entity with that id gets modified
				//get object from database
				o = em.createQuery("FROM " + table + " o WHERE o.id=:oid").setParameter("oid", id).getSingleResult();
			}
				//prvo setujem collumn atribute
				for(int j=0; j<resource.getAttributes().size(); j++) {
					XMLAttribute attr = resource.getAttributes().get(j);
					String setName = "set" + Character.toUpperCase(attr.getName().charAt(0)) + attr.getName().substring(1);
					try {
						//ignore suffix on type
						Class aClass = Class.forName(attr.getType().split(":")[0]);
						Method setter = s.getMethod(setName, aClass);
						setter.setAccessible(true);
						try {
							Object value = values.get(j);
							if(attr.getType().equals("java.lang.Boolean")) {
								value = Boolean.parseBoolean(value.toString());
							}else if (attr.getType().equals("java.util.Date")) {
								SimpleDateFormat formatter = new SimpleDateFormat("dd.MMM.yyyy.", Locale.JAPAN);
								value = (Date)formatter.parse(value.toString().replaceAll("\\p{Cntrl}", "").replaceAll(",", "."));
							}else if (attr.getType().equals("java.math.BigDecimal")) {
								value = new BigDecimal(value.toString().replaceAll(",", "."));
							}
//							else if (attr.getType().equals("java.lang.String") && attr.getValues() != null) {
//								System.out.println("TREBA COMBOBOX ZA " + attr.getName());
//							}
							setter.invoke(o, value);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						System.out.println("nema klase za atribut: " + attr.getName());
					}
				}
				//zatim manyToOne atribute
				for(int k=resource.getAttributes().size(); k<resource.getManyToOneAttributes().size() + resource.getAttributes().size(); k++) {
					int mattrIndex = k-resource.getAttributes().size();
					XMLManyToOneAttribute mattr = resource.getManyToOneAttributes().get(mattrIndex);
					String setName = "set" + Character.toUpperCase(mattr.getName().charAt(0)) + mattr.getName().substring(1);
					Class mClass = Class.forName("adapt.entities." + mattr.getType());
					Method mSetter = s.getMethod(setName, mClass);
					mSetter.setAccessible(true);
					mSetter.invoke(o, values.get(k));
				}
			if(id == null) {
				em.persist(o);
			}else {
				em.flush();
			}
			t.commit();
			em.close();
			return o;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
//---------------------------------------------------------------------------
	
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

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		dataModel.put("title", Settings.APP_TITLE);
		return getHTMLTemplateRepresentation("formTemplate.html", dataModel);
	}
}
