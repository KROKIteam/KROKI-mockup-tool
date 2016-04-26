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
import kroki.profil.utils.ParentChildUtil;
import kroki.profil.utils.VisibleClassUtil;
import kroki.uml_core_basic.UmlParameter;

/**
 * Generates panels, standard and parent-child
 * @author Kroki Team
 *
 */
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
			//root element <menus>
			Element root = doc.createElement("panels");
			doc.appendChild(root);
			
			//root element for standard panel
			Element stdPanelsRoot = doc.createElement("standard-panels");
			//root element for parent-child panels
			Element parentChildRoot = doc.createElement("parent-child-panels");
			//root element for many-to-many panels
			Element mtmPanelsRoot = doc.createElement("many-to-many-panels");
			
			root.appendChild(stdPanelsRoot);
			root.appendChild(parentChildRoot);
			root.appendChild(mtmPanelsRoot);
			
			
			  /************************************/
			 /*         panel-map.xml            */
			/************************************/
			Document mapDoc = docBuilder.newDocument();
			//root element <map>
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
					
					//every standard panel should have tag <standard-panel>
					Element stdPanel = doc.createElement("standard-panel");
					
					String id = panel.getPersistentClass().name().toLowerCase() + "_st";
					String ejbRef = "ejb." + panel.getPersistentClass().name();
					if(repo != null) {
						ejbRef = "ejb_generated." + panel.getPersistentClass().name();
					}
						
					
					//attribute "id"
					Attr idAttr = doc.createAttribute("id");
					idAttr.setValue(id);
					stdPanel.setAttributeNode(idAttr);
					
					//attribute "ejb-ref"
					Attr ejbRefAttr = doc.createAttribute("ejb-ref");
					ejbRefAttr.setValue(ejbRef);
					stdPanel.setAttributeNode(ejbRefAttr);
					
					//generisanje <settings> taga for standardni panel
					Element eSettings = doc.createElement("settings");
					
					//attribute add
					Attr addAttr = doc.createAttribute("add");
					addAttr.setValue(String.valueOf(panel.isAdd()));
					eSettings.setAttributeNode(addAttr);
					//attribute delete
					Attr deleteAttr = doc.createAttribute("delete");
					deleteAttr.setValue(String.valueOf(panel.isDelete()));
					eSettings.setAttributeNode(deleteAttr);
					//attribute view-mode
					Attr viewModeAttr = doc.createAttribute("view-mode");
					if(settings.getDefaultViewMode() == ViewMode.INPUT_PANEL_MODE) {
						viewModeAttr.setValue("panel");
					}else {
						viewModeAttr.setValue("table");
					}
					
					eSettings.setAttributeNode(viewModeAttr);
					//attribute change-mode
					Attr changeModeAttr = doc.createAttribute("change-mode");
					changeModeAttr.setValue(String.valueOf(panel.isChangeMode()));
					eSettings.setAttributeNode(changeModeAttr);
					//attribute data-navigation
					Attr dataNavAttr = doc.createAttribute("data-navigation");
					dataNavAttr.setValue(String.valueOf(panel.isDataNavigation()));
					eSettings.setAttributeNode(dataNavAttr);
					
					stdPanel.appendChild(eSettings);
					
					//links
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
							
							//attribute label
							String label = next.getLabel();
							Attr linkLabelAttr = doc.createAttribute("label");
							linkLabelAttr.setValue(label);
							linkTag.setAttributeNode(linkLabelAttr);
							
							//attribute activate
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

								//opposite attribute 
								//TODO NESTO NECE
								if(next.opposite() != null) {
									Attr linkOppositeAttr = doc.createAttribute("opposite");
									linkOppositeAttr.setValue(next.opposite().name());
									linkTag.setAttributeNode(linkOppositeAttr);
								}
							}
							
							//attribute "elementgroup"
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
								
								//attribute "name"
								Attr opNameAttr = doc.createAttribute("name");
								opNameAttr.setValue(vo.name());
								opTag.setAttributeNode(opNameAttr);
								
								//attribute "label"
								Attr opLabelAttr = doc.createAttribute("label");
								opLabelAttr.setValue(vo.getLabel());
								opTag.setAttributeNode(opLabelAttr);
								
								//attribute "elementgroup"
								if(!groupName.equals("operations")) {
									Attr opGroupAttr = doc.createAttribute("operationgroup");
									opGroupAttr.setValue(groupName);
									opTag.setAttributeNode(opGroupAttr);
								}
								
								//attribute "type"
								Attr opTypeAttr = doc.createAttribute("type");
								
								//if the operation is a report put type="report"
								if(vo instanceof Report) {
									opTypeAttr.setValue("report");
								//if the operation is a transaction put type="transaction"
								}else if (vo instanceof Transaction) {
									opTypeAttr.setValue("transaction");
								}
								
								opTag.setAttributeNode(opTypeAttr);
								
								//attribute "target"
								Attr opTargetAttr = doc.createAttribute("target");
								//cuurently not implemented, so put null
								if(((BussinessOperation) vo).getPersistentOperation() == null) {
									opTargetAttr.setValue("null");
								}else {
									opTargetAttr.setValue(((BussinessOperation) vo).getPersistentOperation().name());
								}
								opTag.setAttributeNode(opTargetAttr);
								
								//attribute "allowed"
								//for now always set to true
								Attr opAllowedAttr = doc.createAttribute("allowed");
								opAllowedAttr.setValue("true");
								opTag.setAttributeNode(opAllowedAttr);
								
								//for every parameter: 
								//<parameter name="name" label="label" type="java.lang.String" parameter-type="in" /> tag
								if(vo.ownedParameter() != null) {
									if(!vo.ownedParameter().isEmpty()) {
										for(int l=0;l<vo.ownedParameter().size();l++) {
											UmlParameter param = vo.ownedParameter().get(l);
											
											Element paramTag = doc.createElement("parameter");
											
											//attribute "name"
											Attr paramNameAttr = doc.createAttribute("name");
											paramNameAttr.setValue(param.name());
											paramTag.setAttributeNode(paramNameAttr);
											
											//attribute "label"
											//TODO
											//not implemented yet  :(
											Attr paramLabelAttr = doc.createAttribute("label");
											paramLabelAttr.setValue(param.name());
											paramTag.setAttributeNode(paramLabelAttr);
											
											//attribute "type"
											Attr paramTypeAttr = doc.createAttribute("type");
											paramTypeAttr.setValue(param.type().toString());
											paramTag.setAttributeNode(paramTypeAttr);
											
											//attribute "parameter-type"
											//always set to in for now
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
					//attribute "id"
					Attr mapIdAttr = mapDoc.createAttribute("id");
					mapIdAttr.setValue(id);
					mapPanel.setAttributeNode(mapIdAttr);
					//attribute "ejb-ref"
					Attr mapEjbRefAttr = mapDoc.createAttribute("ejb-ref");
					mapEjbRefAttr.setValue(ejbRef);
					mapPanel.setAttributeNode(mapEjbRefAttr);
					
					mapRoot.appendChild(mapPanel);
					
					//if it is a parent-child panel put panel> tag
				}else if (element instanceof ParentChild) {
					ParentChild pcPanel = (ParentChild)element;
					//System.out.println("[PARENT CHILD PANEL] id = " + cc.toCamelCase(pcPanel.name() + "_pc", false) + ", label = " + pcPanel.getLabel() );
					
					Element pcTag = doc.createElement("parent-child");
					parentChildRoot.appendChild(pcTag);
				
					//attribute id
					Attr pcIdAttr = doc.createAttribute("id");
					pcIdAttr.setValue(cc.toCamelCase(pcPanel.name(), false) +  "_pc");
					pcTag.setAttributeNode(pcIdAttr);
					
					//attribute label
					Attr pcLabelAttr = doc.createAttribute("label");
					pcLabelAttr.setValue(pcPanel.getLabel());
					pcTag.setAttributeNode(pcLabelAttr);
					
					//for every panelin the hierarchy put  <panel> tag
					//<panel id="dnmp_sk" level="4" panel-ref="sektor_st" />
					for(int m=0; m < ParentChildUtil.allContainedHierarchies(pcPanel).size(); m++) {
						Hierarchy h = ParentChildUtil.allContainedHierarchies(pcPanel).get(m);
						StandardPanel hPanel = (StandardPanel) h.getTargetPanel();
						System.out.println("Hierarhija: id = " + h.name() + ", panel-ref = " + hPanel.getPersistentClass().name().toLowerCase() + "_st, level = " + h.getLevel());
						
						Element hPanelTag = doc.createElement("panel");
						
						//attribute id
						Attr hPanelIdAttr = doc.createAttribute("id");
						hPanelIdAttr.setValue(hPanel.name());
						hPanelTag.setAttributeNode(hPanelIdAttr);
						
						//attribute panel-ref
						Attr hPanelRefAttr = doc.createAttribute("panel-ref");
						hPanelRefAttr.setValue(hPanel.getPersistentClass().name().toLowerCase() + "_st");
						hPanelTag.setAttributeNode(hPanelRefAttr);
						
						//attribute level
						Attr hPanelLevel = doc.createAttribute("level");
						hPanelLevel.setValue(String.valueOf(h.getLevel()));
						hPanelTag.setAttributeNode(hPanelLevel);
						
						//association end
						if(h.getLevel() > 0) {
							System.out.println(h.getLabel() +  " [LEVEL = "  + h.getLevel() + "]");
							Attr hPanelAssociationEnd = doc.createAttribute("association-end");
							hPanelAssociationEnd.setValue(h.getViaAssociationEnd().name());
							hPanelTag.setAttributeNode(hPanelAssociationEnd);
							System.out.println("ASSOCIATION END: " + h.getViaAssociationEnd().name());
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