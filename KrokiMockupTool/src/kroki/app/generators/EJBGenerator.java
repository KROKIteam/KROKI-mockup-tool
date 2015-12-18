package kroki.app.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kroki.app.generators.utils.Attribute;
import kroki.app.generators.utils.EJBAttribute;
import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.Enumeration;
import kroki.app.generators.utils.ManyToOneAttribute;
import kroki.app.generators.utils.XMLWriter;
import kroki.commons.camelcase.NamingUtil;

import org.apache.commons.io.FileDeleteStrategy;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class EJBGenerator {


	NamingUtil cc;
	XMLWriter writer = new XMLWriter();
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

	/***********************************************/
	/*           EJB CLASS GENERATION              */
	/***********************************************/
	public void generateEJBClasses(ArrayList<EJBClass> classes, Boolean swing) {
		cc = new NamingUtil();
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  H:mm:ss");
		String d = formatter.format(now);
		System.out.println("[" + d + "]" + " generating JPA Entity classes...");
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);

		File dir = new File(appPath.substring(0, appPath.length()-16) +  "SwingApp" + File.separator + "src" + File.separator + "ejb");
		if(!swing) {
			dir = new File(appPath.substring(0, appPath.length()-16) +  "WebApp" + File.separator + "src" + File.separator + "adapt" + File.separator + "entities" + File.separator + "generated");
		}
		deleteFiles(dir);

		for(int i=0; i<classes.size(); i++) {
			EJBClass cl = classes.get(i);
			Configuration cfg = new Configuration();
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			FileTemplateLoader templateLoader;
			try {
				templateLoader = new FileTemplateLoader(new File(appPath + "src/kroki/app/generators/templates"));
				cfg.setTemplateLoader(templateLoader);
				Template tpl = cfg.getTemplate("EJBClass.ftl");


				File fout = new File(appPath.substring(0, appPath.length()-16) +  "SwingApp" + File.separator + "src" + File.separator + "ejb" + File.separator + cl.getName() + ".java");
				//ako je swing false onda se generisu ejb klase u web projekat
				if(!swing) {
					fout = new File(appPath.substring(0, appPath.length()-16) +  "WebApp" +  File.separator + "src" + File.separator + "adapt" + File.separator + "entities" + File.separator + "generated" + File.separator + cl.getName() + ".java");
				}

				//JOptionPane.showMessageDialog(null, "EJB GENERATOR: generisem u " + fout.getAbsolutePath());

				if (!fout.getParentFile().exists()) 
					if (!fout.getParentFile().mkdirs()) {
						throw new IOException("Greska pri kreiranju izlaznog direktorijuma ");
					}
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fout));

				Map model = new TreeMap();
				String doc = "   /** " +
						"\n   Class generated using Kroki EJBGenerator " +
						"\n   @Author KROKI Team " +
						"\n   Creation date: " + d + "h" +
						"\n   **/";

				model.put("class", cl);
				model.put("doc", doc);

				tpl.process(model, writer);
			}catch (IOException ioe) {
				//				JOptionPane.showMessageDialog(null, "EJB GENERATOR: IOException");
				ioe.printStackTrace();
			} catch (TemplateException te) {
				//				JOptionPane.showMessageDialog(null, "EJB GENERATOR: TemplateException");
				te.printStackTrace();
			}
		}
		System.out.println("[" + d + "] " + classes.size() + " JPA classes successfully generated.");
	}


	/***********************************************/
	/*        EJB XML FILES GENERATION             */
	/***********************************************/
	public void generateEJBXmlFiles(ArrayList<EJBClass> classes) {

		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);

		File dir = new File(appPath.substring(0, appPath.length()-16) +  "SwingApp" + File.separator + "model" + File.separator + "ejb");
		deleteFiles(dir);

		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			for(int i=0; i<classes.size(); i++) {
				EJBClass clas = classes.get(i);
				Document doc = docBuilder.newDocument();

				//korenski tag <entity> za svaku klasu
				Element entityRoot = doc.createElement("entity");
				doc.appendChild(entityRoot);

				//atribut "name"
				Attr nameAttr = doc.createAttribute("name");
				nameAttr.setValue(clas.getName().toUpperCase());
				entityRoot.setAttributeNode(nameAttr);

				//atribut "label"
				Attr labelAttr = doc.createAttribute("label");
				labelAttr.setValue(clas.getLabel());
				entityRoot.setAttributeNode(labelAttr);

				//atribut "class-name"
				Attr clasNameAttr = doc.createAttribute("class-name");
				clasNameAttr.setValue("ejb." + clas.getName());
				entityRoot.setAttributeNode(clasNameAttr);

				//tag <attributes>
				Element attributes = doc.createElement("attributes");
				entityRoot.appendChild(attributes);

				//---------------------------------ID COLUMN FOR EVERY CLASS
				
				Element idColumn = doc.createElement("column-attribute");

				//atribut "name"
				Attr idNameAttr = doc.createAttribute("name");
				idNameAttr.setValue("id");
				idColumn.setAttributeNode(idNameAttr);

				//atribut "label"
				Attr idLabelAttr = doc.createAttribute("label");
				idLabelAttr.setValue("ID");
				idColumn.setAttributeNode(idLabelAttr);

				//atribut "field-name"
				Attr idFieldNameAttr = doc.createAttribute("field-name");
				idFieldNameAttr.setValue("id");
				idColumn.setAttributeNode(idFieldNameAttr);

				//atribut "type"
				Attr idType = doc.createAttribute("type");
				idType.setValue("java.lang.Long");
				idColumn.setAttributeNode(idType);

				//atribut "length"
				Attr idLength = doc.createAttribute("length");
				idLength.setValue("50");
				idColumn.setAttributeNode(idLength);

				//atribut "key"
				Attr idKeyAttr = doc.createAttribute("key");
				idKeyAttr.setValue("true");
				idColumn.setAttributeNode(idKeyAttr);
				
				Attr idVisibleAttr = doc.createAttribute("visible");
				idVisibleAttr.setValue("false");
				idColumn.setAttributeNode(idVisibleAttr);

				attributes.appendChild(idColumn);

				//-----------------------------------------------------------
				
				//tag <column-attribute> za svaki atribut klase
				if(!clas.getAttributes().isEmpty()) {
					for (EJBAttribute attribute : clas.getAttributes()) {
						if(getAttributeType(attribute).equals("Column")) {
							Element columnAttr = doc.createElement("column-attribute");

							//atribut "name"
							Attr colNameAttr = doc.createAttribute("name");
							colNameAttr.setValue(attribute.getName());
							columnAttr.setAttributeNode(colNameAttr);

							//atribut "label"
							Attr colLabelAttr = doc.createAttribute("label");
							colLabelAttr.setValue(attribute.getLabel());
							columnAttr.setAttributeNode(colLabelAttr);

							//atribut "field-name"
							Attr colFieldNameAttr = doc.createAttribute("field-name");
							colFieldNameAttr.setValue(attribute.getName());
							columnAttr.setAttributeNode(colFieldNameAttr);

							//atribut "type"
							Attr colType = doc.createAttribute("type");
							colType.setValue(attribute.getType());
							
							//atribut "visible"
							Attr colVisibleAttr = doc.createAttribute("visible");
							colVisibleAttr.setValue(attribute.getVisible().toString());
							columnAttr.setAttributeNode(colVisibleAttr);
							
							//atribut "readOnly"
							Attr colReadOnlyAttr = doc.createAttribute("readOnly");
							colReadOnlyAttr.setValue(attribute.getReadOnly().toString());
							columnAttr.setAttributeNode(colReadOnlyAttr);
							
							//atribut "autoGo"
							Attr colAutoGoAttr = doc.createAttribute("autoGo");
							colAutoGoAttr.setValue(attribute.getAutoGo().toString());
							columnAttr.setAttributeNode(colAutoGoAttr);
							
							//atribut "foregroundColor"
							Attr colForegroundColorAttr = doc.createAttribute("foreground");
							colForegroundColorAttr.setValue(attribute.getForegraundRGB().toString());
							columnAttr.setAttributeNode(colForegroundColorAttr);
							
							//atribut "backgroundColor"
							Attr colBackgroundColorAttr = doc.createAttribute("background");
							colBackgroundColorAttr.setValue(attribute.getBackgroundRGB().toString());
							columnAttr.setAttributeNode(colBackgroundColorAttr);

							Enumeration enumeration = attribute.getEnumeration();
							if(enumeration != null) {
								//colType.setValue("");
								Attr colEnum = doc.createAttribute("enum");
								colEnum.setValue(attribute.getEnumeration().getName());
								columnAttr.setAttributeNode(colEnum);
							}

							columnAttr.setAttributeNode(colType);

							//TODO ovde naci pravu duzine, ne da se postavi 50
							//staviti wrap atribut - da li da se predje u novi red nakon
							//ove komponente (pod uslovom da je horizontalni layout)
							//Pogledati pozicije komponente - imamo metode getRelativePosition
							//i getAbsolutePosition
							//snimiti te apsolutne pozicije za free layout
							//za horizontalni pogledati da li se menja y koordinata nakon
							//tekuce komponente - da li je sledeca u novom redu
							//i tako postaviti wrap
							
							//atribut "length"
							Attr colLength = doc.createAttribute("length");
							colLength.setValue(attribute.getLenght().toString());
							columnAttr.setAttributeNode(colLength);
							
							//atribut "wrap"
//							Attr colWrap = doc.createAttribute("wrap");
//							colWrap.setValue(attribute.getWrap().toString());
//							columnAttr.setAttributeNode(colWrap);

							//atribut "key"
							Attr colKeyAttr = doc.createAttribute("key");
							colKeyAttr.setValue("false");
							columnAttr.setAttributeNode(colKeyAttr);

							attributes.appendChild(columnAttr);
						}else if(getAttributeType(attribute).equals("ManyToOne")) {
							Element zoomTag = doc.createElement("zoom-attribute");

							//atribut "name"
							Attr zoomNameAttr = doc.createAttribute("name");
							zoomNameAttr.setValue(attribute.getName());
							zoomTag.setAttributeNode(zoomNameAttr);

							//atribut "label"
							Attr zoomLabelAttr = doc.createAttribute("label");
							zoomLabelAttr.setValue(attribute.getLabel());
							zoomTag.setAttributeNode(zoomLabelAttr);

							//atribut "field-name"
							Attr fieldNameAttr = doc.createAttribute("field-name");
							fieldNameAttr.setValue(attribute.getName());
							zoomTag.setAttributeNode(fieldNameAttr);

							//atribut "class-name"
							Attr classNameAttr = doc.createAttribute("class-name");
							classNameAttr.setValue("ejb." + attribute.getType());
							zoomTag.setAttributeNode(classNameAttr);

							//atribut "zoomed-by"
							Attr zoomedByAttr = doc.createAttribute("zoomed-by");
							zoomedByAttr.setValue("id");
							zoomTag.setAttributeNode(zoomedByAttr);

							//tag <column-ref> za id (ako nema ni jedan drugi)
							Element columnRef = doc.createElement("column-ref");

							//atribut "name"
							Attr colRefNameAttr = doc.createAttribute("name");
							colRefNameAttr.setValue("id");
							columnRef.setAttributeNode(colRefNameAttr);

							//atribut "label"
							Attr colRefLabelAttr = doc.createAttribute("label");
							colRefLabelAttr.setValue(attribute.getLabel() + " ID");
							columnRef.setAttributeNode(colRefLabelAttr);

							zoomTag.appendChild(columnRef);
							
							if(!attribute.getColumnRefs().isEmpty()) {
								//ako ima referenci u zoomu, ide column-ref tag
								for(int k=0;k<attribute.getColumnRefs().size(); k++) {
									EJBAttribute a = attribute.getColumnRefs().get(k);

									Element cr = doc.createElement("column-ref");

									Attr attrNm = doc.createAttribute("name");
									attrNm.setValue(a.getName());
									cr.setAttributeNode(attrNm);

									Attr attrLbl = doc.createAttribute("label");
									attrLbl.setValue(attribute.getLabel() + " " + a.getLabel());
									cr.setAttributeNode(attrLbl);

									zoomTag.appendChild(cr);

								}
							}
							
							attributes.appendChild(zoomTag);
						}
					}
				}

				writer.write(doc, "ejb" + File.separator + clas.getName(), true);

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/***********************************************/
	/*     XML-MAPPING FILE GENERATION             */
	/***********************************************/
	public void generateXMLMappingFile(ArrayList<EJBClass> classes) {

		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			//korenski tag <map>
			Element mapRoot = doc.createElement("map");
			doc.appendChild(mapRoot);

			for(int i=0; i<classes.size(); i++) {
				EJBClass ejb = classes.get(i);

				//<property class-name="ejb.organizaciona_struktura.Drzava" xml-file="ejb/drzava" />
				Element property = doc.createElement("property");

				//atribut "class-name"
				Attr nameAttr = doc.createAttribute("class-name");
				nameAttr.setValue("ejb." + ejb.getName());
				property.setAttributeNode(nameAttr);

				//atribut "xml-file"
				Attr fileAttr = doc.createAttribute("xml-file");
				//fileAttr.setValue("D:" + File.separator + "workspace" + File.separator + "kroki-integracija-clone" + File.separator + "SwingApp" + File.separator + "model" + File.separator + "ejb" + File.separator + ejb.getName());
				fileAttr.setValue("ejb/" + ejb.getName());
				property.setAttributeNode(fileAttr);

				mapRoot.appendChild(property);
			}

			writer.write(doc, "xml-mapping", true);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Determines attribute type based on it's annotation
	 * @param attribute
	 * @return Column, OneToMany or ManyToOne
	 */
	public String getAttributeType(EJBAttribute attribute) {
		String annotation = attribute.getAnnotations().get(0);
		if(annotation.startsWith("@Column")) {
			return "Column";
		}else if (annotation.startsWith("@ManyToOne")) {
			return "ManyToOne";
		}else {
			return "OneToMany";
		}

	}

	public boolean deleteFiles(File directory) {
		boolean success = false;

		if (!directory.exists()) {
			return false;
		}
		if (!directory.canWrite()) {
			return false;
		}

		File[] files = directory.listFiles();
		for(int i=0; i<files.length; i++) {
			File file = files[i];
			if(file.isDirectory()) {
				deleteFiles(file);
			}
			try {
				FileDeleteStrategy.FORCE.delete(file);
				success =  !file.delete();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return success;
	}

}
