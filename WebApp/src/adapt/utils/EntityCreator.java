package adapt.utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.h2.constant.SysProperties;

import adapt.application.AdaptApplication;

public class EntityCreator {

	private AdaptApplication application;
	
	public EntityCreator(AdaptApplication application) {
		this.application = application;
	}
	
	/**
	 * Na osnovu liste objekata iz baze podataka kreira listu EntityClass objekata.
	 * @param objects lista objekata dobijena upitom u bazu, moze biti lista objekata bilo koje klase
	 * @return listu objekata EntityClass klase koji se prikazuju na templejtu
	 * @throws NoSuchFieldException 
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<EntityClass> getEntities(ArrayList<Object> objects, ArrayList<String> headers, XMLResource resource) throws NoSuchFieldException {
		ArrayList<EntityClass> entities = new ArrayList<EntityClass>();
		if(!objects.isEmpty()) {
			for(int i=0; i<objects.size(); i++) {
				Object o = objects.get(i);
				//za svaki objekat saznamo koje je klase
				Class oClass = o.getClass();
				ArrayList<EntityProperty> props = new ArrayList<EntityProperty>();
				EntityClass entity = new EntityClass(oClass.getName(), props);
				//na osnovu klase dobijemo niz sa njenim atributima
				Field[] fields = oClass.getDeclaredFields();
				for (int j = 0; j < fields.length; j++) {
					//za svaki atribut se postavi modifikator pristupa na public
					fields[j].setAccessible(true);
					try {
						//ocitamo ime i vrednost atributa
						String fname = fields[j].getName();
						Object value = fields[j].get(o);
						
						if (value == null) {
							value = new String("None");
							EntityProperty pr = new EntityProperty(fname,value.toString());
							entity.getProperties().add(pr);
						}else {
							//ako je kolekcija ne prikazuje se na formi i ne treba nam
							if (!value.getClass().getSimpleName().equals("PersistentSet")) {
								if(!value.getClass().getSimpleName().equals("PersistentBag")) {
									if(!fname.equals("serialVersionUID")) {
										if(headers != null && resource != null) {
											if(!fname.equals("id")) {
												String label = getAttributeLabel(resource, fname);
												if(!headers.contains(label)) {
													headers.add(label);
												}
											}
										}
										//ako ima referenca na neku drugu klasu
										//treba dovuci representativna obelezja
										if (value.toString().startsWith("adapt")) {
											XMLResource ress = application.getXMLResource(value.getClass().getSimpleName());
											String values = "";
											for (XMLAttribute xmlAttribute : ress.getRepresentativeAttributes()) {
												Field f = value.getClass().getDeclaredField(xmlAttribute.getName());
												f.setAccessible(true);
												values += f.get(value).toString() + ", ";
												values = values.substring(0, values.length()-2);
											}
											if(values.equals("")) {
												Field id = value.getClass().getDeclaredField("id");
												id.setAccessible(true);
												values = id.get(value).toString();
											}
											EntityProperty pr = new EntityProperty(fname, values);
											entity.getProperties().add(pr);
											//ako je Boolean, pretvorim u da ili ne
										} else if (value.getClass().getSimpleName().equals("Boolean")) {
											if (value.toString().equals("true")) {
												EntityProperty pr = new EntityProperty(
														fname, "YES");
												entity.getProperties().add(pr);
											} else {
												EntityProperty pr = new EntityProperty(
														fname, "NO");
												entity.getProperties().add(pr);
											}
											//ako je Date, formatiram ga lepo
										}else if (value.getClass().getSimpleName().equals("Timestamp")) {
											SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.JAPAN);
											SimpleDateFormat niceFormatter = new SimpleDateFormat("dd.MMM.yyyy.", Locale.JAPAN);
											Date dateValue = inputFormat.parse(value.toString());
											EntityProperty prop = new EntityProperty(fname, niceFormatter.format(dateValue).toString());
											entity.getProperties().add(prop);
										}else {
											System.out.println("[IMAM] " + fname + " = " + value.toString());
											EntityProperty pr = new EntityProperty(fname,value.toString());
											entity.getProperties().add(pr);
										}
									}
								}
							}
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				entities.add(entity);
			}
		}
		return entities;
	}

	/**
	 * vraca string vrednost atributa na osnovu prosledjenog imena
	 * @param entity entitet kojem trazeni atribut pripada
	 * @param name ime atributa cija se vrednost trazi
	 * @return String vrednost trazenog atributa ili null ukoliko atribut sa trazenim imenom ne postoji
	 */
	public String getEntityPropertyValue(EntityClass entity, String name) {
		String val = null;
		for(int i=0; i<entity.getProperties().size(); i++) {
			EntityProperty prop = entity.getProperties().get(i);
			if(prop.getName().equalsIgnoreCase(name)) {
				val = prop.getValue().toString();
			}
		}
		return val;
	}
	
	public String getAttributeLabel(XMLResource resource, String name) {
		for (XMLAttribute attribute : resource.getAttributes()) {
			if(attribute.getName().equals(name)) {
				return attribute.getLabel();
			}
		}
		
		for(XMLOneToManyAttribute attribute : resource.getOneToManyAttributes()) {
			if(attribute.getName().equals(name)) {
				return attribute.getLabel();
			}
		}
		
		for(XMLManyToOneAttribute attribute : resource.getManyToOneAttributes()) {
			if(attribute.getName().equals(name)) {
				return attribute.getLabel();
			}
		}
		
		for (XMLManyToManyAttribute attribute : resource.getManyToManyAttributes()) {
			if(attribute.getName().equals(name)) {
				return attribute.getLabel();
			}
		}
		return "";
	}

}
