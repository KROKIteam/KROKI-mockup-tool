package util.xml_readers;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.EntityHelper;
import util.TypesConverterFromXML;
import util.resolvers.PanelTypeResolver;
import util.staticnames.ReadersPathConst;
import util.staticnames.Tags;

import com.panelcomposer.converters.ConverterUtil;
import com.panelcomposer.core.AppCache;
import com.panelcomposer.enumerations.OpenedAs;
import com.panelcomposer.enumerations.OperationType;
import com.panelcomposer.enumerations.PanelType;
import com.panelcomposer.enumerations.ViewMode;
import com.panelcomposer.exceptions.OperationNotFoundException;
import com.panelcomposer.exceptions.PanelTypeParsingException;
import com.panelcomposer.model.attribute.AbsAttribute;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.model.panel.MManyToManyPanel;
import com.panelcomposer.model.panel.MPanel;
import com.panelcomposer.model.panel.MParentChildPanel;
import com.panelcomposer.model.panel.MStandardPanel;
import com.panelcomposer.model.panel.configuration.DataSettings;
import com.panelcomposer.model.panel.configuration.Next;
import com.panelcomposer.model.panel.configuration.PanelSettings;
import com.panelcomposer.model.panel.configuration.Zoom;
import com.panelcomposer.model.panel.configuration.operation.Operation;
import com.panelcomposer.model.panel.configuration.operation.Parameter;
import com.panelcomposer.model.panel.configuration.operation.ParameterType;
import com.panelcomposer.model.panel.configuration.operation.SpecificOperations;

public class PanelReader {

	protected static String modelDir = ReadersPathConst.MODEL_DIR_PATH;
	protected static String panelDir = ReadersPathConst.PANELS_DIR;
	protected static String panelsFile = ReadersPathConst.PANELS_FILE_NAME;
	protected static String panelsMapFile = ReadersPathConst.PANELS_MAP_FILE_NAME;
	
	public static void loadMappings() {
		try {
			File f = new File(".");
			String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
			System.out.println("PANEL READER PANEL MAP: " + appPath + modelDir + File.separator + panelDir + File.separator + panelsMapFile);
			Document doc = XMLUtil.getDocumentFromXML(appPath + modelDir + File.separator + panelDir + File.separator + panelsMapFile, null);
			NodeList nodeLst = doc.getElementsByTagName(Tags.PANEL);
			System.out.println("PANELS: " + doc.getDocumentURI());
			for (int i = 0; i < nodeLst.getLength(); i++) {
				Element elem = (Element) nodeLst.item(i);
				String id = elem.getAttribute(Tags.ID);
				String className = elem.getAttribute(Tags.EJB_REF);
				AppCache.getInstance().addToCachePanelClassMap(className, id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	/***
	 * Loads panel's model for given parameters
	 * 
	 * @param panelId
	 *            Panel id (name)
	 * @param panelType
	 *            Panel's type
	 * @param openedId
	 *            Id for next or zoom panel
	 * @param openedAs
	 *            Opened as default, next or zoom
	 * @return Panel's model
	 */
	public static MPanel loadPanel(String panelId, PanelType panelType,
			String openedId, OpenedAs openedAs) {
		try {
			Document doc = XMLUtil.getDocumentFromXML(modelDir + File.separator + panelDir + File.separator + panelsFile, null);
			MPanel mpanel = null;
			switch (panelType) {
			case STANDARDPANEL:
				if (openedAs.equals(OpenedAs.NEXT)) {
					mpanel = findNextPanel(doc, panelId, panelType, openedId);
				} else if (openedAs.equals(OpenedAs.ZOOM)) {
					mpanel = findZoomPanel(doc, panelId, panelType, openedId);
				} else {
					mpanel = findStandardPanel(doc, panelId);
				}
				break;
			case PARENTCHILDPANEL:
				mpanel = findParentChildPanel(doc, panelId);
				break;
			case MANYTOMANYPANEL:
				mpanel = findManyToManyPanel(doc, panelId);
				break;
			}
			return mpanel;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static MStandardPanel findStandardPanel(Document doc, String panelId) {
		NodeList nodeLst = doc.getElementsByTagName(Tags.STANDARD_PANEL);
		String id = null;
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Element elem = (Element) nodeLst.item(i);
			id = elem.getAttribute(Tags.ID);
			if (id.equals(panelId)) {
				String ejbRef = elem.getAttribute(Tags.EJB_REF);
				// Loading ejb either from cache or xml
				EntityBean ejb = EntityReader.load(ejbRef);
				if (ejb == null) {
					return null;
				}
				MStandardPanel msp = new MStandardPanel();
				ejb = getEntityRestrictions(ejb, elem);
				msp = new MStandardPanel();
				msp.setName(id);
				msp.setEntityBean(ejb);
				msp.setPanelSettings(getSettings(elem, new PanelSettings()));
				msp.setDataSettings(new DataSettings());
				msp.setStandardOperations(getStandardOperations(elem,
						new SpecificOperations()));
				msp.setNextPanels(getNexts(doc, elem));
				msp.setZoomPanels(getZooms(doc, elem));
				return msp;
			}
		}
		return null;
	}

	private static MPanel findNextPanel(Document doc, String panelId,
			PanelType panelType, String openedId) {
		MPanel mpanel = null;
		NodeList nodeList = doc.getElementsByTagName(Tags.NEXT);
		String id = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element elem = (Element) nodeList.item(i);
			id = elem.getAttribute(Tags.NAME);
			if (id.equals(openedId)) {
				mpanel = loadPanel(panelId, panelType, openedId,
						OpenedAs.DEFAULT);
				String label = elem.getAttribute(Tags.LABEL);
				String name = elem.getAttribute(Tags.NAME);
				mpanel.setName(name);
				mpanel.setLabel(label);
				if (mpanel instanceof MStandardPanel) {
					MStandardPanel msp = (MStandardPanel) mpanel;
					PanelSettings ps = msp.getPanelSettings();
					msp.setPanelSettings(getSettings(elem, ps));
					msp.setStandardOperations(getStandardOperations(elem,
							msp.getStandardOperations()));
				}
				return mpanel;
			}
		}
		return null;
	}

	private static MPanel findZoomPanel(Document doc, String panelId,
			PanelType panelType, String openedId) {
		MPanel mpanel = null;
		NodeList nodeList = doc.getElementsByTagName(Tags.ZOOM);
		String id = null;
		mpanel = loadPanel(panelId, panelType, openedId, OpenedAs.DEFAULT);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element elem = (Element) nodeList.item(i);
			id = elem.getAttribute(Tags.NAME);
			if (id.equals(openedId)) {
				// load with basic panel
				if (mpanel instanceof MStandardPanel) {
					MStandardPanel msp = (MStandardPanel) mpanel;
					PanelSettings ps = msp.getPanelSettings();
					msp.setPanelSettings(getSettings(elem, ps));
					msp.setStandardOperations(getStandardOperations(elem,
							msp.getStandardOperations()));
				}
				return mpanel;
			}
		}
		return mpanel;
	}

	private static MPanel findManyToManyPanel(Document doc, String panelId) {
		NodeList nodeList = doc.getElementsByTagName(Tags.MANY_TO_MANY);
		String id = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element elem = (Element) nodeList.item(i);
			id = elem.getAttribute(Tags.ID);
			if (id.equals(panelId)) {
				MManyToManyPanel mtmp = new MManyToManyPanel();
				mtmp.setName(id);
				String label = elem.getAttribute(Tags.LABEL);
				mtmp.setLabel(label);
				NodeList nodeListChildren = elem
						.getElementsByTagName(Tags.PANEL);
				Element elemSub = null;
				for (int j = 0; j < nodeListChildren.getLength(); j++) {
					elemSub = (Element) nodeListChildren.item(j);
					mtmp.add(getSubPanel(elemSub));
				}
				return mtmp;
			}
		}
		return null;
	}

	private static MParentChildPanel findParentChildPanel(Document doc,
			String panelId) {
		NodeList nodeList = doc.getElementsByTagName(Tags.PARENT_CHILD);
		String id = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element elem = (Element) nodeList.item(i);
			id = elem.getAttribute(Tags.ID);
			if (id.equals(panelId)) {
				MParentChildPanel mcpc = new MParentChildPanel();
				mcpc.setName(id);
				String label = elem.getAttribute(Tags.LABEL);
				mcpc.setLabel(label);
				NodeList nodeListChildren = elem
						.getElementsByTagName(Tags.PANEL);
				Element elemSub = null;
				for (int j = 0; j < nodeListChildren.getLength(); j++) {
					elemSub = (Element) nodeListChildren.item(j);
					mcpc.add(getSubPanel(elemSub));
				}
				return mcpc;
			}
		}
		return null;
	}

	/**
	 * Get subpanel's model from composite panel.
	 * 
	 * @param elem
	 *            parent or child Element in XML
	 * @param panelRef
	 *            Referenced panel's id (name)
	 * @return Subpanel's model
	 */
	private static MStandardPanel getSubPanel(Element elem) {
		MStandardPanel mpanel = null;
		String panelRef = elem.getAttribute(Tags.PANEL_REF);
		mpanel = (MStandardPanel) loadPanel(panelRef, PanelType.STANDARDPANEL, null, OpenedAs.DEFAULT);
		String id = elem.getAttribute(Tags.ID);
		String level = elem.getAttribute(Tags.LEVEL);
		mpanel.setName(id);
		mpanel.setLevel(TypesConverterFromXML.resolveInteger(level));
		mpanel.setPanelSettings(getSettings(elem, mpanel.getPanelSettings()));
		mpanel.setStandardOperations(getStandardOperations(elem,
				mpanel.getStandardOperations()));
		mpanel.setEntityBean(getEntityRestrictions(mpanel.getEntityBean(), elem));
		return mpanel;
	}

	/***
	 * Method for parsing the standard operations
	 * 
	 * @param elem
	 * @param operations
	 * @return
	 */
	public static SpecificOperations getStandardOperations(Element elem,
			SpecificOperations operations) {

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

			String type = elemOperation.getAttribute(Tags.DATA_TYPE);
			if (type.equals("report"))
				oper.setType(OperationType.VIEWREPORT);
			else if (type.equals("transaction")) {
				oper.setType(OperationType.BUSSINESTRANSACTION);
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

	public static EntityBean getEntityRestrictions(EntityBean ejb, Element elem) {
		ejb = setRestrictionOnColumn(ejb, elem, Tags.HIDDEN, 
				"setHidden", true);
		ejb = setRestrictionOnColumn(ejb, elem, Tags.DISABLED, 
				"setDisabled",true);
		ejb = setRestrictionOnColumn(ejb, elem, Tags.EDITABLE,
				"setEditableInTable", true);
		return ejb;
	}

	public static EntityBean setRestrictionOnColumn(EntityBean ejb,
			Element elem, String tagName, String methodName, Boolean argValue) {
		NodeList nodeList = elem.getElementsByTagName(tagName);
		Element el = null;
		String value = null;
		AbsAttribute attr = null;
		Method method = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			el = (Element) nodeList.item(i);
			value = el.getAttribute(Tags.VALUE);
			try {
				attr = (AbsAttribute) EntityHelper.getAttribute(ejb, value);
				if(attr instanceof JoinColumnAttribute) {
					JoinColumnAttribute jca = (JoinColumnAttribute) attr;
					for(ColumnAttribute ca : jca.getColumns()) {
						method = ColumnAttribute.class.getMethod(methodName, Boolean.class);
						method.invoke(ca, argValue);
					}
				} else {
					System.out.println(attr.getName() + " . " + methodName);
					method = ColumnAttribute.class.getMethod(methodName, Boolean.class);
					method.invoke(attr, argValue);
				}
			} catch (Exception e) {
				// TODO
				e.printStackTrace();
			}
		}
		return ejb;
	}

	/****
	 * 
	 * @param elem
	 * @param settings
	 * @return
	 */
	public static PanelSettings getSettings(Element elem,
			PanelSettings settings) {
		Node nodeSettings = elem.getElementsByTagName(Tags.SETTINGS).item(0);
		Element el = (Element) nodeSettings;
		if (el == null) {
			return settings;
		}
		settings = setOneSetting(Tags.ADD, "Add" , el, settings);
		settings = setOneSetting(Tags.DELETE, "Delete" , el, settings);
		settings = setOneSetting(Tags.UPDATE, "Update" , el, settings);
		settings = setOneSetting(Tags.COPY, "Copy" , el, settings);
		settings = setOneSetting(Tags.CHANGE_MODE, "ChangeMode" , el, settings);
		settings = setOneSetting(Tags.NAVIGATION, "DataNavigation" , el, settings);
		
		String val = el.getAttribute(Tags.ADD);
		val = el.getAttribute(Tags.VIEW_MODE);
		if (val != null && !val.trim().equals("")) {
			if (val.equals("table")) {
				settings.setViewMode(ViewMode.TABLEVIEW);
			} else if (val.equals("panel")) {
				settings.setViewMode(ViewMode.INPUTPANELVIEW);
			}
		}
		return settings;
	}
	
	private static PanelSettings setOneSetting(String tag, String methodName, 
			Element elem, PanelSettings settings) {
		String val = elem.getAttribute(tag);
		Method method = null;
		if (val != null && !val.trim().equals("")) {
			try {
				method = PanelSettings.class.getMethod("get" + methodName);
				if ((Boolean) method.invoke(settings) != false) {
					method = PanelSettings.class.getMethod("set" + methodName, Boolean.class);
					method.invoke(settings, new Boolean(val));
				}
			} catch (Exception e) {
				// TODO
				e.printStackTrace();
			}
		}
		return settings;
	}

	private static List<Next> getNexts(Document doc, Element elem) {
		List<Next> nexts = new ArrayList<Next>();
		Next next = null;
		NodeList nodeNexts = elem.getElementsByTagName(Tags.NEXT);
		for (int i = 0; i < nodeNexts.getLength(); i++) {
			Element elNext = (Element) nodeNexts.item(i);
			try {
				next = new Next();
				next.setLabel(elNext.getAttribute(Tags.LABEL));
				next.setName(elNext.getAttribute(Tags.NAME));
				next.setPanelId(elNext.getAttribute(Tags.PANEL_REF));
				String panelType = elNext.getAttribute(Tags.PANEL_TYPE);
				next.setPanelType(PanelTypeResolver.getType(panelType));
				nexts.add(next);
			} catch (PanelTypeParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return nexts;
	}

	private static List<Zoom> getZooms(Document doc, Element elem) {
		List<Zoom> zooms = new ArrayList<Zoom>();
		Zoom zoom = null;
		NodeList nodeZooms = elem.getElementsByTagName(Tags.ZOOM);
		for (int i = 0; i < nodeZooms.getLength(); i++) {
			Element elZoom = (Element) nodeZooms.item(i);
			zoom = new Zoom();
			zoom.setName(elZoom.getAttribute(Tags.NAME));
			zoom.setPanelId(elZoom.getAttribute(Tags.PANEL_REF));
			zooms.add(zoom);
		}
		return zooms;
	}

}
