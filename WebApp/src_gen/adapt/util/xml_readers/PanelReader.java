package adapt.util.xml_readers;

import java.awt.Label;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import adapt.core.AppCache;
import adapt.enumerations.OpenedAs;
import adapt.enumerations.OperationType;
import adapt.enumerations.PanelType;
import adapt.enumerations.ViewMode;
import adapt.exceptions.OperationNotFoundException;
import adapt.model.ejb.AbstractAttribute;
import adapt.model.ejb.ColumnAttribute;
import adapt.model.ejb.EntityBean;
import adapt.model.ejb.JoinColumnAttribute;
import adapt.model.panel.AdaptManyToManyPanel;
import adapt.model.panel.AdaptPanel;
import adapt.model.panel.AdaptParentChildPanel;
import adapt.model.panel.AdaptStandardPanel;
import adapt.model.panel.configuration.DataSettings;
import adapt.model.panel.configuration.Next;
import adapt.model.panel.configuration.PanelSettings;
import adapt.model.panel.configuration.Zoom;
import adapt.model.panel.configuration.operation.Operation;
import adapt.model.panel.configuration.operation.Parameter;
import adapt.model.panel.configuration.operation.ParameterType;
import adapt.model.panel.configuration.operation.SpecificOperations;
import adapt.util.converters.ConverterUtil;
import adapt.util.ejb.EntityHelper;
import adapt.util.repository_utils.RepositoryPathsUtil;
import adapt.util.resolvers.PanelTypeResolver;
import adapt.util.staticnames.Tags;
import adapt.util.xml_utils.XMLParserUtils;
import adapt.util.xml_utils.XMLTypesConverter;

public class PanelReader {

	protected static String generatedRepoPath 	= RepositoryPathsUtil.getGeneratedModelPath();
	protected static String generatedModelPath 	= RepositoryPathsUtil.getGeneratedModelPath();
	protected static String panelsDirectoryPath	= generatedModelPath + File.separator + "panel" + File.separator;
	protected static String mappingFileName 	= "panel-map.xml";
	protected static String panelsFileName		= "panel.xml";

	private static String logPrefix = "PANEL READER: ";

	/**
	 * Parse panel-to-ejb mapping from XML file
	 */
	public static void loadMappings() {
		try {
			AppCache.displayTextOnMainFrame(logPrefix + "Reading mapping file: " + mappingFileName, 0);
			Document document = XMLParserUtils.parseXml(panelsDirectoryPath + mappingFileName);
			NodeList panelNodes = document.getElementsByTagName(Tags.PANEL);
			for(int i=0; i<panelNodes.getLength(); i++) {
				Element panelElement = (Element)panelNodes.item(i);
				String id = panelElement.getAttribute(Tags.ID);
				String ejb_ref = panelElement.getAttribute(Tags.EJB_REF);
				AppCache.getInstance().addToCachePanelClassMapping(ejb_ref, id);
				AppCache.displayTextOnMainFrame(logPrefix + "Mapping " + ejb_ref + ".java --> " + id, 0);
			}
		} catch (Exception e) {
			AppCache.displayTextOnMainFrame(logPrefix +  "Error parsing mapping file " + mappingFileName, 1);
		}
	}

	/**
	 * Load panel data from XML file based on panel ID
	 */
	public static AdaptPanel loadPanel(String panelId, PanelType type, String openedId, OpenedAs openedAs) {
//		System.out.println(logPrefix + " Loading data for: " + panelId);
		AdaptPanel panel = AppCache.getInstance().getPanelByName(panelId);
		if(panel != null) {
			return panel;
		}else {
			try {
				Document document = XMLParserUtils.parseXml(panelsDirectoryPath + panelsFileName);
				
				switch (type) {
				case STANDARDPANEL:
					AppCache.displayTextOnMainFrame(logPrefix + " Fetching standard panel data for: " + panelId, 0);
					if(openedAs.equals(OpenedAs.NEXT)) {
						panel = findNextPanel(document, panelId, type, openedId);
					}else if(openedAs.equals(OpenedAs.ZOOM)) {
						panel = findZoomPanel(document, panelId, type, openedId);
					}else {
						panel = findStandardPanel(document, panelId);
					}
					break;
				case PARENTCHILDPANEL:
					AppCache.displayTextOnMainFrame(logPrefix + " Fetching parent-child panel data for: " + panelId, 0);
					panel = findParentChildPanel(document, panelId);
					break;
				case MANYTOMANYPANEL:
					AppCache.displayTextOnMainFrame(logPrefix + " Fetching many to many panel data for: " + panelId, 0);
					panel = findManyToManyPanel(document, panelId);
					break;
				}
			} catch (Exception e) {
				AppCache.displayTextOnMainFrame("Error reading panel data for name: " + panelId, 1);
				AppCache.displayStackTraceOnMainFrame(e);
			}
		}
		return panel;
	}

	private static AdaptStandardPanel findStandardPanel(Document document, String panelId) {
		NodeList stdPanelNodes = document.getElementsByTagName(Tags.STANDARD_PANEL);
		String id = null;
		for(int i=0; i<stdPanelNodes.getLength(); i++) {
			Element stdPanelElement = (Element)stdPanelNodes.item(i);
			id = stdPanelElement.getAttribute(Tags.ID);
			if(id.equals(panelId)) {
				String ejbRef = stdPanelElement.getAttribute(Tags.EJB_REF);
				EntityBean bean = EntityReader.load(ejbRef) ;
				if(bean == null) {
					return null;
				}
				AdaptStandardPanel stdPanel = new AdaptStandardPanel();
				bean = getEntityRestrictions(bean, stdPanelElement);
				stdPanel = new AdaptStandardPanel();
				stdPanel.setName(id);
				stdPanel.setEntityBean(bean);
				stdPanel.setLabel(bean.getLabel());
				stdPanel.setPanelSettings(getSettings(stdPanelElement, new PanelSettings()));
				stdPanel.setDataSettings(new DataSettings());
				stdPanel.setStandardOperations(getStandardOperations(stdPanelElement, new SpecificOperations()));
				stdPanel.setNextPanels(getNexts(document, stdPanelElement));
				stdPanel.setZoomPanels(getZooms(document, stdPanelElement));
				return stdPanel;
			}
		}
		return null;
	}

	private static AdaptPanel findNextPanel(Document doc,String panelId, PanelType panelType, String openedId) {
		AdaptPanel panel = null;
		NodeList nodeList = doc.getElementsByTagName(Tags.NEXT);
		String id = null;
		for(int i=0; i<nodeList.getLength(); i++) {
			Element elem = (Element) nodeList.item(i);
			id = elem.getAttribute(Tags.NAME);
			if(id.equals(openedId)) {
				panel = loadPanel(panelId, panelType, openedId, OpenedAs.DEFAULT);
				String label = elem.getAttribute(Tags.LABEL);
				String name = elem.getAttribute(Tags.NAME);
				panel.setName(name);
				panel.setLabel(label);
				if(panel instanceof AdaptStandardPanel) {
					AdaptStandardPanel stdPanel = (AdaptStandardPanel) panel;
					PanelSettings settings = stdPanel.getPanelSettings();
					stdPanel.setPanelSettings(getSettings(elem, settings));
					stdPanel.setStandardOperations(getStandardOperations(elem, stdPanel.getStandardOperations()));
				}
				return panel;
			}
		}
		return panel;
	}

	private static AdaptPanel findZoomPanel(Document doc, String panelId, PanelType panelType, String openedId) {
		AdaptPanel panel = null;
		NodeList nodeList = doc.getElementsByTagName(Tags.ZOOM);
		String id = null;
		panel = loadPanel(panelId, panelType, openedId, OpenedAs.DEFAULT);
		for(int i=0; i<nodeList.getLength(); i++) {
			Element elem = (Element)nodeList.item(i);
			id = elem.getAttribute(Tags.NAME);
			if(id.equals(openedId)) {
				if(panel instanceof AdaptStandardPanel) {
					AdaptStandardPanel stdPanel = (AdaptStandardPanel) panel;
					PanelSettings settings = stdPanel.getPanelSettings();
					stdPanel.setPanelSettings(getSettings(elem, settings));
					stdPanel.setStandardOperations(getStandardOperations(elem, stdPanel.getStandardOperations()));
				}
				return panel;
			}
		}
		return panel;
	}

	private static AdaptPanel findManyToManyPanel(Document doc, String panelId) {
		NodeList nodeList = doc.getElementsByTagName(Tags.MANY_TO_MANY);
		String id = null;
		for(int i=0; i<nodeList.getLength(); i++) {
			Element elem = (Element)nodeList.item(i);
			id = elem.getAttribute(Tags.ID);
			if(id.equals(panelId)) {
				AdaptManyToManyPanel mtmPanel = new AdaptManyToManyPanel();
				mtmPanel.setName(id);
				String label = elem.getAttribute(Tags.LABEL);
				mtmPanel.setLabel(label);
				NodeList childPanelNodes = elem.getElementsByTagName(Tags.PANEL);
				Element childPanelElement = null;
				for(int j=0; j<childPanelNodes.getLength(); j++) {
					childPanelElement = (Element)childPanelNodes.item(j);
					mtmPanel.add(getSubPanel(childPanelElement));
				}
				return mtmPanel;
			}
		}
		return null;
	}

	private static AdaptParentChildPanel findParentChildPanel(Document doc, String panelId) {
		NodeList nodeList = doc.getElementsByTagName(Tags.PARENT_CHILD);
		String id = null;
		for(int i=0;i <nodeList.getLength(); i++) {
			Element elem = (Element) nodeList.item(i);
			id =  elem.getAttribute(Tags.ID);
			if(id.equals(panelId)) {
				AdaptParentChildPanel pcPanel = new AdaptParentChildPanel();
				pcPanel.setName(id);
				String label = elem.getAttribute(Tags.LABEL);
				pcPanel.setLabel(label);
				NodeList childPanelNodes = elem.getElementsByTagName(Tags.PANEL);
				Element  subElem = null;
				for(int j=0; j<childPanelNodes.getLength(); j++) {
					subElem = (Element) childPanelNodes.item(j);
					pcPanel.add(getSubPanel(subElem));
				}
				return pcPanel;
			}
		}
		return null;
	}

	// Returns JSON representation of parent-child panels' panels
	// This method is used to quickly fetch parent-child panel information from XML file
	public static ArrayList<String> getJSONPanelList(String pcPanelName) {
		ArrayList<String> panels = new ArrayList<String>();
		Document document = XMLParserUtils.parseXml(panelsDirectoryPath + panelsFileName);
		NodeList nodeList = document.getElementsByTagName(Tags.PARENT_CHILD);
		for(int i=0;i <nodeList.getLength(); i++) {
			Element elem = (Element) nodeList.item(i);
			String id =  elem.getAttribute(Tags.ID);
			if(id.equals(pcPanelName)) {
				NodeList childPanelNodes = elem.getElementsByTagName(Tags.PANEL);
				for(int j=0; j<childPanelNodes.getLength(); j++) {
					Element subElem = (Element) childPanelNodes.item(j);
					String panelRef = subElem.getAttribute(Tags.PANEL_REF);
					String asocEnd = subElem.getAttribute(Tags.ASSOCIATION_END);
					/*
					 * FORMAT:
					 * 	"activate"          : "${panel.name}"<#if (panel.associationEnd??)>,
            			"assoiciation_end"  : "${panel.associationEnd}"</#if>
					 */
					String jsonEntry = "\"activate\":	\"" + panelRef + "\"";
					if(asocEnd != null && !asocEnd.equals("")) {
						jsonEntry += ", \"assoiciation_end\":	\"" + asocEnd + "\"";
					}
					panels.add(jsonEntry);
				}
			}
		}
		return panels;
	}

	private static AdaptStandardPanel getSubPanel(Element elem) {
		AdaptStandardPanel stdPanel = null;
		String panelRef = elem.getAttribute(Tags.PANEL_REF);
		stdPanel = (AdaptStandardPanel) loadPanel(panelRef, PanelType.STANDARDPANEL, null, OpenedAs.DEFAULT);
		String id = elem.getAttribute(Tags.ID);
		String level = elem.getAttribute(Tags.LEVEL);
		stdPanel.setName(panelRef);
		stdPanel.setLabel(id);
		Integer lev = XMLTypesConverter.resolveInteger(level);
		stdPanel.setLevel(lev);
		if(lev > 1) {
			stdPanel.setAssociationEnd(elem.getAttribute(Tags.ASSOCIATION_END));
		}
		stdPanel.setPanelSettings(getSettings(elem, stdPanel.getPanelSettings()));
		stdPanel.setStandardOperations(getStandardOperations(elem, stdPanel.getStandardOperations()));
		stdPanel.setEntityBean(getEntityRestrictions(stdPanel.getEntityBean(), elem));
		return stdPanel;
	}

	public static SpecificOperations getStandardOperations(Element elem, SpecificOperations operations) {
		// Adding new operations for panel
		NodeList nodeListOperations = elem.getElementsByTagName(Tags.OPERATION);
		for (int i = 0; i < nodeListOperations.getLength(); i++) {
			Element elemOperation = (Element) nodeListOperations.item(i);
			Operation oper = new Operation();
			oper.setName(elemOperation.getAttribute(Tags.NAME));
			oper.setLabel(elemOperation.getAttribute(Tags.LABEL));
			oper.setTarget(elemOperation.getAttribute(Tags.TARGET));
			String allowedStr = elemOperation.getAttribute(Tags.ALLOWED);
			boolean allowed = new Boolean(allowedStr);
			oper.setAllowed(allowed);
			oper.setParentGroup(elemOperation.getAttribute(Tags.OPERATION_GROUP));
			
			String type = elemOperation.getAttribute(Tags.DATA_TYPE);
			if (type.equals("report"))
				oper.setType(OperationType.VIEWREPORT);
			else if (type.equals("transaction")) {
				oper.setType(OperationType.BUSINESSTRANSACTION);
			}

			NodeList nodeListParameters = elemOperation
					.getElementsByTagName(Tags.PARAMETER);
			for (int j = 0; j < nodeListParameters.getLength(); j++) {
				Element elemParam = (Element) nodeListParameters.item(j);
				Parameter param = new Parameter();
				param.setName(elemParam.getAttribute(Tags.NAME));
				param.setLabel(elemParam.getAttribute(Tags.LABEL));
				String typeClass = elemParam.getAttribute(Tags.DATA_TYPE);
				try {
					param.setType(Class.forName(typeClass));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				String parameterType = elemParam
						.getAttribute(Tags.PARAMETER_TYPE);
				if (parameterType.equals("in")) {
					param.setParameterType(ParameterType.IN);
				} else if (parameterType.equals("inout")) {
					param.setParameterType(ParameterType.INOUT);
				} else if (parameterType.equals("out")) {
					param.setParameterType(ParameterType.OUT);
				}
				oper.add(param);
				String defaultValue = elemParam.getAttribute(Tags.DEFAULT);
				if (defaultValue != null && !defaultValue.trim().equals("")) {
					Object obj = ConverterUtil.convert(defaultValue,
							param.getType());
					param.setDefaultValue(obj);
				}
			}
			operations.add(oper);
		}

		// operation references
		nodeListOperations = elem
				.getElementsByTagName(Tags.OPERATION_REFERENCE);
		for (int i = 0; i < nodeListOperations.getLength(); i++) {
			Element elemOperationRef = (Element) nodeListOperations.item(i);
			String name = elemOperationRef.getAttribute(Tags.NAME);
			try {
				Operation operation = operations.findByName(name);
				String allowedStr = elemOperationRef.getAttribute(Tags.ALLOWED);
				boolean allowed = new Boolean(allowedStr);
				// restrictive rights only
				if (operation.getAllowed() != false) {
					operation.setAllowed(allowed);
				}
			} catch (OperationNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}

		return operations;
	}

	public static EntityBean getEntityRestrictions(EntityBean bean, Element elem) {
		bean = setRestrictionOnColumn(bean, elem, Tags.HIDDEN, "setHidden", true);
		bean = setRestrictionOnColumn(bean, elem, Tags.DISABLED, "setDisabled", true);
		bean = setRestrictionOnColumn(bean, elem, Tags.EDITABLE, "setEditable", true);
		return bean;
	}

	public static EntityBean setRestrictionOnColumn(EntityBean bean, Element elem, String tagName, String methodName, Boolean argValue) {
		NodeList nodeList = elem.getElementsByTagName(tagName);
		Element el = null;
		String value = null;
		AbstractAttribute attr = null;
		Method method = null;

		for(int i=0; i<nodeList.getLength(); i++) {
			el = (Element)nodeList.item(i);
			value = el.getAttribute(Tags.VALUE);
			try {
				attr = (AbstractAttribute) EntityHelper.getAttribute(bean, value);
				if(attr instanceof JoinColumnAttribute) {
					JoinColumnAttribute jca = (JoinColumnAttribute)attr;
					for (ColumnAttribute ca : jca.getColumns()) {
						method = ColumnAttribute.class.getMethod(methodName, Boolean.class);
						method.invoke(ca, argValue);
					}
				}else {
					method = ColumnAttribute.class.getMethod(methodName, Boolean.class);
					method.invoke(attr, argValue);
				}
			} catch (Exception e) {
				AppCache.displayTextOnMainFrame("Error setting column restrictions.", 1);
				AppCache.displayStackTraceOnMainFrame(e);
			}
		}
		return bean;
	}

	public static PanelSettings getSettings(Element elem, PanelSettings settings) {
		Node settingsNode = elem.getElementsByTagName(Tags.SETTINGS).item(0);
		Element el = (Element) settingsNode;
		if(el == null) {
			return settings;
		}
		settings = setOneSetting(Tags.ADD, "Add", el, settings);
		settings = setOneSetting(Tags.DELETE, "Delete", el, settings);
		settings = setOneSetting(Tags.UPDATE, "Update", el, settings);
		settings = setOneSetting(Tags.COPY, "Copy", el, settings);
		settings = setOneSetting(Tags.CHANGE_MODE, "ChangeMode", el, settings);
		settings = setOneSetting(Tags.NAVIGATION, "DataNavigation", el, settings);

		String val = el.getAttribute(Tags.ADD);
		val = el.getAttribute(Tags.VIEW_MODE);
		if(val != null && !val.trim().equals("")) {
			if(val.equals("table")) {
				settings.setViewMode(ViewMode.TABLEVIEW);
			}else if(val.equals("panel")) {
				settings.setViewMode(ViewMode.INPUTPANELVIEW);
			}
		}
		return settings;
	}

	private static PanelSettings setOneSetting(String tag, String methodName, Element elem, PanelSettings settings) {
		String val = elem.getAttribute(tag);
		Method method = null;
		if(val != null && !val.trim().equals("")) {
			try {
				method = PanelSettings.class.getMethod("get" + methodName);
				if((Boolean) method.invoke(settings) != false) {
					method = PanelSettings.class.getMethod("set" + methodName, Boolean.class);
					method.invoke(settings, new Boolean(val));
				}
			} catch (Exception e) {
				AppCache.displayTextOnMainFrame("Error apllying settings do panel", 1);
				AppCache.displayStackTraceOnMainFrame(e);
			}
		}
		return settings;
	}

	private static List<Next> getNexts(Document doc, Element elem) {
		System.out.println("GET NEXTS");
		List<Next> nexts = new ArrayList<Next>();
		Next next = null;
		NodeList nextNodes = elem.getElementsByTagName(Tags.NEXT);
		for(int i=0; i<nextNodes.getLength(); i++) {
			Element nextElement = (Element)nextNodes.item(i);
			System.out.println("NEXT ELEMENT: " + nextElement.toString());
			try {
				next = new Next();
				next.setLabel(nextElement.getAttribute(Tags.LABEL));
				next.setName(nextElement.getAttribute(Tags.NAME));
				next.setPanelId(nextElement.getAttribute(Tags.PANEL_REF));
				next.setPanelType(PanelTypeResolver.getType(Tags.STANDARD_PANEL));
				next.setParentGroup(nextElement.getAttribute(Tags.OPERATION_GROUP));
				next.setOpposite(nextElement.getAttribute(Tags.OPPOSITE));
				nexts.add(next);
			} catch (Exception e) {
				AppCache.displayTextOnMainFrame("Error reading next data from file", 1);
				AppCache.displayStackTraceOnMainFrame(e);
			}
		}
		return nexts;
	}

	private static List<Zoom> getZooms(Document doc, Element elem) {
		List<Zoom> zooms = new ArrayList<Zoom>();
		Zoom zoom = null;
		NodeList zoomNodes = elem.getElementsByTagName(Tags.ZOOM);
		for(int i=0; i<zoomNodes.getLength(); i++) {
			Element zoomElement = (Element)zoomNodes.item(i);
			zoom = new Zoom();
			zoom.setName(zoomElement.getAttribute(Tags.NAME));
			zoom.setPanelId(zoomElement.getAttribute(Tags.PANEL_REF));
			zooms.add(zoom);
		}
		return zooms;
	}
}
