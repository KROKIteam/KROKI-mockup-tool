package util.xml_readers;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.EntityHelper;
import util.TypesConverterFromXML;
import util.resolvers.ComponentResolver;
import util.staticnames.ReadersPathConst;
import util.staticnames.Tags;

import com.panelcomposer.converters.ConverterUtil;
import com.panelcomposer.core.AppCache;
import com.panelcomposer.exceptions.EntityAttributeNotFoundException;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;

public class EntityReader {

	protected static String mappingFile = ReadersPathConst.XML_MAPPINGS_FILE;
	protected static String dirName = ReadersPathConst.MODEL_DIR_PATH;

	/***
	 * Loads mappings for Entity Java Bean classes and XML file paths
	 */
	public static void loadMappings() {
		try {
			File f = new File(".");
			String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
			System.out.println("ENTITY READER: " + appPath + dirName + File.separator + mappingFile);
			Document doc = XMLUtil.getDocumentFromXML(appPath + dirName + File.separator + mappingFile, null);
			System.out.println("ENTITY READER DOC: " + doc.getBaseURI());
			NodeList nodeLst = doc.getElementsByTagName(Tags.PROPERTY);
			for (int i = 0; i < nodeLst.getLength(); i++) {
				Element elem = (Element) nodeLst.item(i);
				String className = elem.getAttribute(Tags.CLASS_NAME);
				String xmlFile = elem.getAttribute(Tags.XML_FILE);
				AppCache.getInstance().addToCache(className, xmlFile + ".xml");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static EntityBean load(String className) {
		EntityBean ejb = null;
		AppCache appCache = AppCache.getInstance();
		if ((ejb = appCache.findEJBByClassName(className)) != null) {
			return ejb;
		}
		try {
			String xmlFileName = appCache.getXMLFileName(className); 
			if (xmlFileName == null)
				return null;
			Document doc = XMLUtil.getDocumentFromXML(dirName + File.separator + xmlFileName, 
					null);
			ejb = getEntityBeanInfo(doc);
			ejb = getAttributesInfo(ejb, doc);
			return ejb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static EntityBean getEntityBeanInfo(Document doc)
			throws ClassNotFoundException {
		EntityBean ejb = new EntityBean();
		NodeList nodeLstEntities = doc.getElementsByTagName(Tags.EJB);
		Node node = nodeLstEntities.item(0);
		Element elem = (Element) node;
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			ejb.setName(elem.getAttribute(Tags.NAME));
			ejb.setLabel(elem.getAttribute(Tags.LABEL));
			ejb.setEntityClass(Class.forName(elem.getAttribute(Tags.CLASS_NAME)));
		}
		return ejb;
	}

	private static EntityBean getAttributesInfo(EntityBean ejb, Document doc) {
		NodeList nodeLisAttributes = doc.getElementsByTagName(Tags.ATTRIBUTES);
		Node node = nodeLisAttributes.item(0);
		NodeList nodeListAbsAttributes = node.getChildNodes();
		for (int j = 0; j < nodeListAbsAttributes.getLength(); j++) {
			Node nodeAttribute = nodeListAbsAttributes.item(j);
			if (nodeAttribute.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) nodeAttribute;
				String attributeType = nodeAttribute.getNodeName();
				if (attributeType.equals(Tags.COLUMN_ATTRIBUTE)) {
					ColumnAttribute ca = null;
					ca = getColumnAttributeValues(elem);
					if(ca.getFieldName().equals("id")) {
						ca.setHidden(true);
					}
					ejb.add(ca);
				} else if (attributeType.equals(Tags.JOIN_COLUMN_ATTRIBUTE)) {
					JoinColumnAttribute jca = null;
					jca = getJoinColumnAttributeValues(elem);
					ejb.add(jca);
				}
			}
		}
		return ejb;
	}

	private static ColumnAttribute getColumnAttributeValues(Element elem) {
		ColumnAttribute ca = new ColumnAttribute();
		ca.setName(elem.getAttribute(Tags.NAME));
		ca.setLabel(elem.getAttribute(Tags.LABEL));
		ca.setFieldName(elem.getAttribute(Tags.FIELD_NAME));
		String dataType = elem.getAttribute(Tags.DATA_TYPE);
		ca.setDataType(dataType);
		Attr enumAttr = elem.getAttributeNode(Tags.ENUM);
		if(enumAttr != null) {
			String enumName = elem.getAttribute(Tags.ENUM);
			ca.setEnumeration(AppCache.getInstance().getEnumeration(enumName));
		}
		// ca.setDataType(AppCache.getInstance().getLanguageType(dataType));
		ca.setLength(new Integer(TypesConverterFromXML.resolveInteger(elem
				.getAttribute(Tags.LENGTH))));
		ca.setScale(new Integer(TypesConverterFromXML.resolveInteger(elem
				.getAttribute(Tags.SCALE))));
		String keyStr = elem.getAttribute(Tags.KEY);
		if (keyStr != null && (keyStr.equals("true") || keyStr.equals("false"))) {
			Boolean key = new Boolean(keyStr);
			if (key != null)
				ca.setKey(key);
		}
		String derivedStr = elem.getAttribute(Tags.DERIVED);
		if (derivedStr != null
				&& (derivedStr.equals("true") || derivedStr.equals("false"))) {
			Boolean derived = new Boolean(derivedStr);
			if (derived != null) {
				ca.setDerived(derived);
				if (derived == true) {
					String formula = elem.getAttribute(Tags.FORMULA);
					ca.setFormula(formula);
				}
			}
		}
		String defaultValue = elem.getAttribute(Tags.DEFAULT);
		if (defaultValue != null && !defaultValue.equals("")) {
			ca.setDefaultValue(ConverterUtil.convert(defaultValue, ca));
		}
		
		JComponent component = ComponentResolver.getComponent(ca);
		ca.setComponent(component);
		return ca;
	}

	private static JoinColumnAttribute getJoinColumnAttributeValues(Element elem) {
		JoinColumnAttribute jca = new JoinColumnAttribute();
		jca.setZoomedBy(elem.getAttribute(Tags.ZOOMED_BY));
		jca.setName(elem.getAttribute(Tags.NAME));
		jca.setLabel(elem.getAttribute(Tags.LABEL));
		String lookupName = elem.getAttribute(Tags.CLASS_NAME);
		jca.setFieldName(elem.getAttribute(Tags.FIELD_NAME));
		if (lookupName != null) {
			try {
				jca.setLookupClass(Class.forName(lookupName));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		NodeList nodeColumnRefList = elem.getElementsByTagName(Tags.COLUMN_REF);
		for (int k = 0; k < nodeColumnRefList.getLength(); k++) {
			Node nodeRef = nodeColumnRefList.item(k);
			elem = (Element) nodeRef;
			ColumnAttribute colAttr = null;
			String attrName = elem.getAttribute(Tags.NAME);
			colAttr = lookupColumnAttribute(attrName, jca.getLookupClass());
			colAttr.setLabel(elem.getAttribute(Tags.LABEL));
			jca.add(colAttr);
		}
		return jca;
	}

	private static ColumnAttribute lookupColumnAttribute(String attrName,
			Class<?> lookupClass) {
		ColumnAttribute ca = null;
		AppCache appCache = AppCache.getInstance();
		EntityBean ejb = null;
		if ((ejb = appCache.findEJBByClassName(lookupClass.getName())) != null) {
			try {
				return (ColumnAttribute) EntityHelper.getAttribute(ejb,
						attrName);
			} catch (EntityAttributeNotFoundException e) {
				return null;
			}
		}
		String xmlFileName = appCache.getXMLFileName(lookupClass.getName());
		if (xmlFileName == null)
			return null;
		try {
			Document doc = XMLUtil.getDocumentFromXML(dirName+ File.separator + xmlFileName, null);
			ejb = getEntityBeanInfo(doc);
			// TODO: izvuci samo potreban columnAttribute
			NodeList nodeList = doc.getElementsByTagName(Tags.COLUMN_ATTRIBUTE);
			Node node = null;
			for (int i = 0; i < nodeList.getLength(); i++) {
				node = nodeList.item(i);
				Element el = (Element) node;
				String val = el.getAttribute(Tags.NAME);
				if (val.equals(attrName)) {
					return getColumnAttributeValues(el);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ca;
	}

}
