package adapt.resources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
import adapt.utils.EntityCreator;
import adapt.utils.XMLAttribute;
import adapt.utils.XMLManyToOneAttribute;
import adapt.utils.XMLResource;
import freemarker.template.Configuration;

public class AddResource extends Resource {
	
	Map<String, Object> dataModel = new TreeMap<String, Object>();
	XMLResource resource;
	XMLResource childResource;
	EntityCreator creator;
	
	public AddResource(Context context, Request request, Response response) {
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
		String resName = (String)getRequest().getAttributes().get("resName");
		String modid	= (String)getRequest().getAttributes().get("modid");
		if(resName != null) {//dodavanje
			resource = application.getXMLResource(resName);
			Form form = getRequest().getEntityAsForm();
			ArrayList<Object> values = getFormData(form);
			System.out.println("DODAJEM: " + resName);
			try {
				Object o = modify(values, null);
				if(o != null) {
					System.out.println("OK");
					dataModel.put("css", "messageOk");
					dataModel.put("message", "Row has been successfuly added to table \"" + resource.getLabel() + "\"");
				}else {
					System.out.println("ERROR");
					dataModel.put("css", "messageError");
					dataModel.put("message", "Error occured while adding row to \"" + resource.getLabel() + "\". Please check your data.");
				}
			} catch (RightAlreadyDefinedException e) {
			}
		}else if (modid != null) {//izmena
			String mresName = (String) getRequest().getAttributes().get("mresName");
			resource = application.getXMLResource(mresName);
			Form form = getRequest().getEntityAsForm();
			ArrayList<Object> values = getFormData(form);
			Long id = Long.parseLong(modid);
			try {
				Object o = modify(values, id);
				if(o != null) {
					dataModel.put("css", "messageOk");
					dataModel.put("message", "Row has been successfuly modified");
				}else {
					dataModel.put("message", "Error occured while row. Please check your data.");
				}
			} catch (RightAlreadyDefinedException e) {
				e.printStackTrace();
			}
		}else {
			dataModel.put("css", "messageError");
			dataModel.put("message", "Error getting data from the server.");
		}
		super.handleGet();
	}
	
	@Override
	public void handlePost() {
		handleGet();
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
//								else if (attr.getType().equals("java.lang.String") && attr.getValues() != null) {
//									System.out.println("TREBA COMBOBOX ZA " + attr.getName());
//								}
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
						if(pairs.getKey().toString().startsWith("attrSelectBool")) { //ako ima checkBox, onda je boolean
							values.add(pairs.getValue().toString());
						}else {
							System.out.println("[SNIMAM] " + pairs.getValue().toString());
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

		@Override
		public Representation represent(Variant variant) throws ResourceException {
			return getHTMLTemplateRepresentation("popupTemplate.html", getDataModel());
		}
		
		
}
