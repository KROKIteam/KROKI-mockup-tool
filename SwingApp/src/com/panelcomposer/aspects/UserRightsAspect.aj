package com.panelcomposer.aspects;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import util.staticnames.ReadersPathConst;
import util.staticnames.Tags;
import util.xml_readers.MenuReader;
import util.xml_readers.PanelReader;
import util.xml_readers.XMLUtil;

import com.panelcomposer.converters.ConverterUtil;
import com.panelcomposer.core.AppCache;
import com.panelcomposer.enumerations.OpenedAs;
import com.panelcomposer.model.menu.MySubMenu;
import com.panelcomposer.model.panel.MStandardPanel;
import com.panelcomposer.model.panel.configuration.Next;
import com.panelcomposer.model.panel.configuration.Zoom;

public aspect UserRightsAspect {
	protected static String modelDir = ReadersPathConst.MODEL_DIR_PATH;
	protected static String usersDirName = modelDir + File.separator + ReadersPathConst.USERS_FILE_NAME + File.separator;
	
	public pointcut userMPanelRights() : 
		( call (* PanelReader.findStandardPanel(..)) || call (* PanelReader.findNextPanel(..)) 
				|| call (* PanelReader.findZoomPanel(..)) || call (* PanelReader.getSubPanel(..)) );
	
	MStandardPanel around() : userMPanelRights() {
		String signature = thisJoinPoint.getSignature().getName();
		MStandardPanel msp = proceed();
		OpenedAs openedAs = null;
		String openedId = null;
		if (signature.equals("findStandardPanel") || signature.equals("getSubPanel")) {
			openedAs = OpenedAs.DEFAULT;
		} else if (signature.equals("findNextPanel")) {
			openedId = (String) thisJoinPoint.getArgs()[3];
			openedAs = OpenedAs.NEXT;
		} else if (signature.equals("findZoomPanel")) {
			openedId = (String) thisJoinPoint.getArgs()[3];
			openedAs = OpenedAs.ZOOM;
		}
		if(msp != null) {
			msp = getUserRestriction(msp, openedAs, openedId);
		}
		return msp;
	}
	
	public pointcut userRightsZooms() : call (* PanelReader.getZooms(..));
	
	List<Zoom> around() : userRightsZooms() {
		List<Zoom> zooms = proceed();
		if(!zooms.isEmpty()) {
			Iterator<Zoom> it = zooms.iterator();
			while(it.hasNext()) {
				Zoom zoom = it.next();
				String username = AppCache.getInstance().getUsername();
				String fileName = usersDirName + username + ".xml";
				Document doc = XMLUtil.getDocumentFromXML(fileName, null);
				//System.out.println(zoom.getName());
				String panelId = zoom.getName();
				Element elem = getElementByPanelId(doc, panelId, OpenedAs.ZOOM);
				if(elem != null) {
					String value = elem.getAttribute(Tags.DISABLED);
					Boolean hide = (Boolean) ConverterUtil.convert(value, Boolean.class);
					if(hide != null && hide == true) {
						it.remove();
					}
				}
			}
		}
		return zooms;
	}
	
	public pointcut userRightsNexts() : execution (* PanelReader.getNexts(..));
	
	List<Next> around() : userRightsNexts() {
		List<Next> nexts = proceed();
		if(!nexts.isEmpty()) {
			Iterator<Next> it = nexts.iterator();
			while(it.hasNext()) {
				Next next = it.next();
				String username = AppCache.getInstance().getUsername();
				String fileName = usersDirName + username + ".xml";
				Document doc = XMLUtil.getDocumentFromXML(fileName, null);
				String panelId = next.getName();
				Element elem = getElementByPanelId(doc, panelId, OpenedAs.NEXT);
				if(elem != null) {
					String value = elem.getAttribute(Tags.DISABLED);
					Boolean hide = (Boolean) ConverterUtil.convert(value, Boolean.class);
					if(hide != null && hide == true) {
						it.remove();
					}
				}
			}
		}
		return nexts;
	}
	
	public pointcut userRightsMenu() : call (* MenuReader.createSubMenu(..));
	
	MySubMenu around() : userRightsMenu() {
		MySubMenu msm = proceed();
		if(msm != null) {
			String username = AppCache.getInstance().getUsername();
			String fileName = usersDirName + username + ".xml";
			Document doc = XMLUtil.getDocumentFromXML(fileName, null);
			//System.out.println("UR ASPECT FILENAME: " + doc.getDocumentURI());
			String panelId = msm.getActivate();
			Element elem = getElementByPanelId(doc, panelId, OpenedAs.DEFAULT);
			if(elem != null) {
				String value = elem.getAttribute(Tags.DISABLED);
				Boolean hide = (Boolean) ConverterUtil.convert(value, Boolean.class);
				if(hide != null && hide == true) {
					msm = null;
				}
			}
		}
		return msm;
	}

	private static MStandardPanel getUserRestriction(MStandardPanel mspanel,
			OpenedAs openedAs, String openedId) {
		String fileName = null;
		try {
			String username = AppCache.getInstance().getUsername();
			fileName = usersDirName + username + ".xml";
			Document doc = XMLUtil.getDocumentFromXML(fileName, null);
			Element elemPanel = getElementByPanelId(doc, mspanel.getName(),
					openedAs);
			if (elemPanel != null) {
				mspanel.setEntityBean(PanelReader.getEntityRestrictions(
						mspanel.getEntityBean(), elemPanel));
				mspanel.setPanelSettings(PanelReader.getSettings(elemPanel,
						mspanel.getPanelSettings()));
				mspanel.setStandardOperations(PanelReader.getStandardOperations(elemPanel,
						mspanel.getStandardOperations()));
			}
		} catch (Exception e) {
			System.out.println("No user's rights XML file: " + fileName);
		}
		return mspanel;
	}
	
	private static Element getElementByPanelId(Document doc, String panelId,
			OpenedAs openedAs) {
		String tag = null;
		String attr = null;
		if (openedAs.equals(OpenedAs.DEFAULT)) {
			tag = Tags.PANEL;
			attr = Tags.PANEL_REF;
		} else if (openedAs.equals(OpenedAs.NEXT)) {
			tag = Tags.NEXT;
			attr = Tags.NAME;
		} else if (openedAs.equals(OpenedAs.ZOOM)) {
			tag = Tags.ZOOM;
			attr = Tags.NAME;
		}
		NodeList nodeList = doc.getElementsByTagName(tag);
		String panelRefId = null;
		Element elemPanel = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			elemPanel = (Element) nodeList.item(i);
			panelRefId = elemPanel.getAttribute(attr);
			if (panelRefId.equals(panelId)) {
				return elemPanel;
			}
		}
		return null;
	}
}
