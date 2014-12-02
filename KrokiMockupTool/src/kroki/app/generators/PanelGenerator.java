package kroki.app.generators;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import kroki.api.panel.ParentChildUtil;
import kroki.api.panel.VisibleClassUtil;
import kroki.app.generators.utils.XMLWriter;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.BussinessOperation;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.panel.mode.ViewMode;
import kroki.profil.panel.std.StdPanelSettings;
import kroki.uml_core_basic.UmlParameter;

public class PanelGenerator {
	
	public void generate(ArrayList<VisibleElement> elements, Object repo) {
		
		XMLWriter writer = new XMLWriter();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		NamingUtil cc = new NamingUtil();
		
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			  /***********************************/
			 /*           panel.xml             */
			/***********************************/
			Document doc = docBuilder.newDocument();
			//korenski element <menus>
			Element root = doc.createElement("panels");
			doc.appendChild(root);
			
			//korenski element za standardne panele
			Element stdPanelsRoot = doc.createElement("standard-panels");
			//korenski element za parent-child panele
			Element parentChildRoot = doc.createElement("parent-child-panels");
			//korenski element za many-to-many panele
			Element mtmPanelsRoot = doc.createElement("many-to-many-panels");
			
			root.appendChild(stdPanelsRoot);
			root.appendChild(parentChildRoot);
			root.appendChild(mtmPanelsRoot);
			
			
			  /************************************/
			 /*         panel-map.xml            */
			/************************************/
			Document mapDoc = docBuilder.newDocument();
			//korenski element <map>
			Element mapRoot = mapDoc.createElement("map");
			mapDoc.appendChild(mapRoot);
			
			for(int i=0; i<elements.size(); i++) {
				VisibleElement element = elements.get(i);
				VisibleClass vClass = (VisibleClass)element;
				
				  /***********************************/
				 /*           panel.xml             */
				/***********************************/
				if(element instanceof StandardPanel) {
					StandardPanel panel = (StandardPanel)element;
					StdPanelSettings settings = panel.getStdPanelSettings();
					
					//za svaki standardi panel ide tag <standard-panel>
					Element stdPanel = doc.createElement("standard-panel");
					
					String id = panel.getPersistentClass().name().toLowerCase() + "_st";
					String ejbRef = "ejb." + panel.getPersistentClass().name();
					if(repo != null) {
						ejbRef = "ejb_generated." + panel.getPersistentClass().name();
					}
						
					
					//atribut "id"
					Attr idAttr = doc.createAttribute("id");
					idAttr.setValue(id);
					stdPanel.setAttributeNode(idAttr);
					
					//atribut "ejb-ref"
					Attr ejbRefAttr = doc.createAttribute("ejb-ref");
					ejbRefAttr.setValue(ejbRef);
					stdPanel.setAttributeNode(ejbRefAttr);
					
					//generisanje <settings> taga za standardni panel
					Element eSettings = doc.createElement("settings");
					
					//atribut add
					Attr addAttr = doc.createAttribute("add");
					addAttr.setValue(String.valueOf(panel.isAdd()));
					eSettings.setAttributeNode(addAttr);
					//atribut delete
					Attr deleteAttr = doc.createAttribute("delete");
					deleteAttr.setValue(String.valueOf(panel.isDelete()));
					eSettings.setAttributeNode(deleteAttr);
					//atribut view-mode
					Attr viewModeAttr = doc.createAttribute("view-mode");
					if(settings.getDefaultViewMode() == ViewMode.INPUT_PANEL_MODE) {
						viewModeAttr.setValue("panel");
					}else {
						viewModeAttr.setValue("table");
					}
					
					eSettings.setAttributeNode(viewModeAttr);
					//atribut change-mode
					Attr changeModeAttr = doc.createAttribute("change-mode");
					changeModeAttr.setValue(String.valueOf(panel.isChangeMode()));
					eSettings.setAttributeNode(changeModeAttr);
					//atribut data-navigation
					Attr dataNavAttr = doc.createAttribute("data-navigation");
					dataNavAttr.setValue(String.valueOf(panel.isDataNavigation()));
					eSettings.setAttributeNode(dataNavAttr);
					
					stdPanel.appendChild(eSettings);
					
					//linkovi
					if(!VisibleClassUtil.containedNexts(vClass).isEmpty()) {
						Element linksTag = doc.createElement("nexts");
						stdPanel.appendChild(linksTag);
						
						for (Next next : VisibleClassUtil.containedNexts(vClass)) {
							Element linkTag = doc.createElement("next");
							
							ElementsGroup elemGr = next.getParentGroup();
							String groupName = "operations";
							if(elemGr != null) {
								groupName = elemGr.getLabel();
							}
							
							//atribut label
							String label = next.getLabel();
							Attr linkLabelAttr = doc.createAttribute("label");
							linkLabelAttr.setValue(label);
							linkTag.setAttributeNode(linkLabelAttr);
							
							//atribut activate
							VisibleClass vclTarget = next.getTargetPanel();
							if(vclTarget instanceof StandardPanel) {
								StandardPanel stdPanelTarget = (StandardPanel)vclTarget;
								String panelName = stdPanelTarget.getPersistentClass().name().toLowerCase() + "_st";
								
								Attr linkNameAttr = doc.createAttribute("name");
								linkNameAttr.setNodeValue(label.toLowerCase().replaceAll(" ", ""));
								linkTag.setAttributeNode(linkNameAttr);
								
								Attr linkPanelTypeAttr = doc.createAttribute("panel-type");
								linkPanelTypeAttr.setNodeValue("STANDARDPANEL");
								linkTag.setAttributeNode(linkPanelTypeAttr);
								
								Attr linkPanelAttr = doc.createAttribute("panel-ref");
								linkPanelAttr.setNodeValue(panelName);
								linkTag.setAttributeNode(linkPanelAttr);

								//atribut opposite NESTO NECE
								if(next.opposite() != null) {
									Attr linkOppositeAttr = doc.createAttribute("opposite");
									linkOppositeAttr.setValue(next.opposite().name());
									linkTag.setAttributeNode(linkOppositeAttr);
								}
							}
							
							//atribut "elementgroup"
							if(!groupName.equals("operations")) {
								Attr opGroupAttr = doc.createAttribute("operationgroup");
								opGroupAttr.setValue(groupName);
								linkTag.setAttributeNode(opGroupAttr);
							}
							
							linksTag.appendChild(linkTag);
						}
					}
					
					//<operations> tag
					if(!VisibleClassUtil.containedOperations(vClass).isEmpty()) {
						Element operationsTag = doc.createElement("operations");
						stdPanel.appendChild(operationsTag);
						
						for(int k=0; k < VisibleClassUtil.containedOperations(vClass).size(); k++) {
							VisibleOperation vo = VisibleClassUtil.containedOperations(vClass).get(k);
							if(vo instanceof BussinessOperation) {
								Element opTag = doc.createElement("operation");
								ElementsGroup elemGroup = vo.getParentGroup();
								String groupName = "operations";
								if(elemGroup != null) {
									groupName = elemGroup.getLabel();
								}
								
								//atribut "name"
								Attr opNameAttr = doc.createAttribute("name");
								opNameAttr.setValue(vo.name());
								opTag.setAttributeNode(opNameAttr);
								
								//atribut "label"
								Attr opLabelAttr = doc.createAttribute("label");
								opLabelAttr.setValue(vo.getLabel());
								opTag.setAttributeNode(opLabelAttr);
								
								//atribut "elementgroup"
								if(!groupName.equals("operations")) {
									Attr opGroupAttr = doc.createAttribute("operationgroup");
									opGroupAttr.setValue(groupName);
									opTag.setAttributeNode(opGroupAttr);
								}
								
								//atribut "type"
								Attr opTypeAttr = doc.createAttribute("type");
								
								//ako je transakcija ide type="report"
								if(vo instanceof Report) {
									opTypeAttr.setValue("report");
								//ako je transakcija ide type="transaction"
								}else if (vo instanceof Transaction) {
									opTypeAttr.setValue("transaction");
								}
								
								opTag.setAttributeNode(opTypeAttr);
								
								//atribut "target"
								Attr opTargetAttr = doc.createAttribute("target");
								//nije implementirano u krokiju pa je null
								if(((BussinessOperation) vo).getPersistentOperation() == null) {
									opTargetAttr.setValue("null");
								}else {
									opTargetAttr.setValue(((BussinessOperation) vo).getPersistentOperation().name());
								}
								opTag.setAttributeNode(opTargetAttr);
								
								//atribut "allowed"
								//za sada samo true
								Attr opAllowedAttr = doc.createAttribute("allowed");
								opAllowedAttr.setValue("true");
								opTag.setAttributeNode(opAllowedAttr);
								
								//za svaki paraterar ide 
								//<parameter name="sifra" label="�ifra" type="java.lang.String" parameter-type="in" /> tag
								if(vo.ownedParameter() != null) {
									if(!vo.ownedParameter().isEmpty()) {
										for(int l=0;l<vo.ownedParameter().size();l++) {
											UmlParameter param = vo.ownedParameter().get(l);
											
											Element paramTag = doc.createElement("parameter");
											
											//atribut "name"
											Attr paramNameAttr = doc.createAttribute("name");
											paramNameAttr.setValue(param.name());
											paramTag.setAttributeNode(paramNameAttr);
											
											//atribut "label"
											//nema :(
											Attr paramLabelAttr = doc.createAttribute("label");
											paramLabelAttr.setValue(param.name());
											paramTag.setAttributeNode(paramLabelAttr);
											
											//atribut "type"
											Attr paramTypeAttr = doc.createAttribute("type");
											paramTypeAttr.setValue(param.type().toString());
											paramTag.setAttributeNode(paramTypeAttr);
											
											//atribut "parameter-type"
											//za sada samo in 
											Attr paramPTypeAttr = doc.createAttribute("parameter-type");
											paramPTypeAttr.setValue("in");
											paramTag.setAttributeNode(paramPTypeAttr);
											
											opTag.appendChild(paramTag);
										}
									}
								}
								operationsTag.appendChild(opTag);
							}
						}
						
						
					}
					
					stdPanelsRoot.appendChild(stdPanel);
					
					  /************************************/
					 /*         panel-map.xml            */
					/************************************/
					//tag <panel>
					Element mapPanel = mapDoc.createElement("panel");
					//atribut "id"
					Attr mapIdAttr = mapDoc.createAttribute("id");
					mapIdAttr.setValue(id);
					mapPanel.setAttributeNode(mapIdAttr);
					//atribut "ejb-ref"
					Attr mapEjbRefAttr = mapDoc.createAttribute("ejb-ref");
					mapEjbRefAttr.setValue(ejbRef);
					mapPanel.setAttributeNode(mapEjbRefAttr);
					
					mapRoot.appendChild(mapPanel);
					
					//ako je parent-child panel> tag
				}else if (element instanceof ParentChild) {
					ParentChild pcPanel = (ParentChild)element;
					//System.out.println("[PARENT CHILD PANEL] id = " + cc.toCamelCase(pcPanel.name() + "_pc", false) + ", label = " + pcPanel.getLabel() );
					
					Element pcTag = doc.createElement("parent-child");
					parentChildRoot.appendChild(pcTag);
				
					//atribut id
					Attr pcIdAttr = doc.createAttribute("id");
					pcIdAttr.setValue(cc.toCamelCase(pcPanel.name(), false) +  "_pc");
					pcTag.setAttributeNode(pcIdAttr);
					
					//atribut label
					Attr pcLabelAttr = doc.createAttribute("label");
					pcLabelAttr.setValue(pcPanel.getLabel());
					pcTag.setAttributeNode(pcLabelAttr);
					
					//za svaki panel u hijerarhiji ide <panel> tag
					//<panel id="dnmp_sk" level="4" panel-ref="sektor_st" />
					for(int m=0; m < ParentChildUtil.allContainedHierarchies(pcPanel).size(); m++) {
						Hierarchy h = ParentChildUtil.allContainedHierarchies(pcPanel).get(m);
						StandardPanel hPanel = (StandardPanel) h.getTargetPanel();
						System.out.println("Hierarhija: id = " + h.name() + ", panel-ref = " + hPanel.getPersistentClass().name().toLowerCase() + "_st, level = " + h.getLevel());
						
						Element hPanelTag = doc.createElement("panel");
						
						//atribut id
						Attr hPanelIdAttr = doc.createAttribute("id");
						hPanelIdAttr.setValue(hPanel.name());
						hPanelTag.setAttributeNode(hPanelIdAttr);
						
						//atribut panel-ref
						Attr hPanelRefAttr = doc.createAttribute("panel-ref");
						hPanelRefAttr.setValue(hPanel.getPersistentClass().name().toLowerCase() + "_st");
						hPanelTag.setAttributeNode(hPanelRefAttr);
						
						//atribut level
						Attr hPanelLevel = doc.createAttribute("level");
						hPanelLevel.setValue(String.valueOf(h.getLevel()));
						hPanelTag.setAttributeNode(hPanelLevel);
						
						//association end
						if(h.getLevel() > 1) {
							Attr hPanelAssociationEnd = doc.createAttribute("association-end");
							hPanelAssociationEnd.setValue(h.getViaAssociationEnd().name());
							hPanelTag.setAttributeNode(hPanelAssociationEnd);
						}
						
						pcTag.appendChild(hPanelTag);
						
					}
				}
			}
			
			if(repo == null) {
				writer.write(doc, "panel" + File.separator + "panel", true);
				writer.write(mapDoc, "panel" + File.separator + "panel-map", true);
			}else {
				writer.write(doc, "panel" + File.separator + "panel", false);
				writer.write(mapDoc, "panel" + File.separator + "panel-map", false);
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

};