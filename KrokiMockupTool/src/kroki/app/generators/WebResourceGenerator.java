package kroki.app.generators;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kroki.app.generators.utils.XMLWriter;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Zoom;
import kroki.profil.operation.BussinessOperation;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.VisibleProperty;
import kroki.profil.utils.VisibleClassUtil;
import kroki.uml_core_basic.UmlParameter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import devHub.AppType;

public class WebResourceGenerator {
	
	public void generate(ArrayList<VisibleElement> elements) {
		
		NamingUtil cc = new NamingUtil();
		XMLWriter writer = new XMLWriter();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
		
			//root tag tag <resources>
			Element resourcesRoot = doc.createElement("resources");
			doc.appendChild(resourcesRoot);
			
			//z <resource> tag
			for(int i=0; i<elements.size(); i++) {
				VisibleElement element = elements.get(i);
				VisibleClass vClass = (VisibleClass)element;
				
				//<resource> tag
				Element resourceTag = doc.createElement("resource");
				resourcesRoot.appendChild(resourceTag);
				
				//attribute of the <resounce> tag
				String name = cc.toCamelCase(vClass.getComponent().getName(), false);
				String label = element.getLabel();
				String link = "/resources/" + name;
				Boolean routed = element.isVisible();
				String forms = "StandardForm";
				
				if(element instanceof ParentChild) {
					forms = "ParentChildForm";
				}
				
				//System.out.println("\n[RESOURCE]" + "\n\tName: " + name + "\n\tLabel: " + label + "\n\tLink: " + link + "\n\tRouted: " + routed + "\n\tForms: " + forms + "\n");
			
				//<Name>
				Element nameTag = doc.createElement("Name");
				nameTag.setTextContent(name);
				resourceTag.appendChild(nameTag);
				
				//<Label>
				Element labelTag = doc.createElement("Label");
				labelTag.setTextContent(label);
				resourceTag.appendChild(labelTag);
				
				//<Link>
				Element linkTag = doc.createElement("Link");
				linkTag.setTextContent(link);
				resourceTag.appendChild(linkTag);
				
				//<Routed>
				Element routedTag = doc.createElement("Routed");
				routedTag.setTextContent(String.valueOf(routed));
				resourceTag.appendChild(routedTag);
				
				//<Forms>
				Element formsTag = doc.createElement("Forms");
				formsTag.setTextContent(forms);
				resourceTag.appendChild(formsTag);
				
				//attribute tags
				if(!VisibleClassUtil.containedProperties(vClass).isEmpty()) {
					
					//firstly, the root tag <Attributes>
					Element attributesTag = doc.createElement("Attributes");
					resourceTag.appendChild(attributesTag);
					
					//for every class attribute put <attribute> tag
					for(int j=0; j < VisibleClassUtil.containedProperties(vClass).size();j++) {
						VisibleProperty prop = VisibleClassUtil.containedProperties(vClass).get(j);
						
						Element attributeTag = doc.createElement("attribute");
						attributesTag.appendChild(attributeTag);
						
						String attrName = cc.toCamelCase(prop.name(), true);
						String attrLabel = prop.getLabel();
						
						String type = "java.lang.String";
						if(prop.getComponentType() == ComponentType.TEXT_FIELD) {
							if(prop.getDataType().equals("BigDecimal")) {
								type = "java.math.BigDecimal";
							}else if(prop.getDataType().equals("Date")) {
								type = "java.util.Date";
							}
						}else if(prop.getComponentType() == ComponentType.CHECK_BOX) {
							type =  "java.lang.Boolean";
						}else if(prop.getComponentType() == ComponentType.TEXT_AREA) {
							type = "java.lang.String:TextArea";
						}
						
						Boolean unique = false;
						Boolean mandatory = prop.lower() != 0;
						Boolean representative = prop.isRepresentative();
						
						//<Name>
						Element attrNameTag = doc.createElement("Name");
						attrNameTag.setTextContent("a_" + attrName);
						attributeTag.appendChild(attrNameTag);
						
						//<DatabaseName>
						Element attrDBName = doc.createElement("DatabaseName");
						attrDBName.setTextContent(prop.getColumnLabel());
						attributeTag.appendChild(attrDBName);
						
						//<Label>
						Element attrLabelTag = doc.createElement("Label");
						attrLabelTag.setTextContent(attrLabel);
						attributeTag.appendChild(attrLabelTag);
						
						//<Type>
						Element attrTypeTag = doc.createElement("Type");
						attrTypeTag.setTextContent(type);
						attributeTag.appendChild(attrTypeTag);
						
						//<Unique>
						Element attrUniqueTag = doc.createElement("Unique");
						attrUniqueTag.setTextContent(Boolean.toString(unique));
						attributeTag.appendChild(attrUniqueTag);
						
						//<Mandatory>
						Element attrMandatoryTag = doc.createElement("Mandatory");
						attrMandatoryTag.setTextContent(Boolean.toString(mandatory));
						attributeTag.appendChild(attrMandatoryTag);
						
						//<Representative>
						Element attrRepresentativeTag = doc.createElement("Representative");
						attrRepresentativeTag.setTextContent(Boolean.toString(representative));
						attributeTag.appendChild(attrRepresentativeTag);
						
						if(prop.getComponentType() == ComponentType.COMBO_BOX) {
							Element attrValuesTag = doc.createElement("Values");
							if(prop.getEnumeration() != null) {
								attrValuesTag.setTextContent(prop.getEnumeration());
							}else {
								attrValuesTag.setTextContent("-- None --");
							}
							attributeTag.appendChild(attrValuesTag);
						}
						//System.out.println("\t[ATTRIBUTE] \n\t\tName: " + attrName + "\n\t\tLabel: " + attrLabel + "\n\t\tType: " + type + "\n\t\tUnique: " + unique + "\n\t \tMandatory: " + mandatory);
					}
				}
				
				//many-to-one attributes (zoom)
				if(!VisibleClassUtil.containedZooms(vClass).isEmpty()) {
					
					//root tag <ManyToOneAttributes>
					Element zoomsTag = doc.createElement("ManyToOneAttributes");
					resourceTag.appendChild(zoomsTag);
					
					//for every attribute put <manyToOne> tag
					for(int k=0; k < VisibleClassUtil.containedZooms(vClass).size(); k++) {
						Zoom zoom = VisibleClassUtil.containedZooms(vClass).get(k);
						//StandardPanel zoomPanel = (StandardPanel) zoom.getTargetPanel();
						
						Element zoomElement = doc.createElement("manyToOne");
						zoomsTag.appendChild(zoomElement);
						
						//<Name>
						Element zoomName = doc.createElement("Name");
						String zName = cc.toCamelCase(zoom.getComponent().getName(), true);
						zName = zName.replaceAll("_", "");
						String cName = cc.toCamelCase(vClass.name(), true);
						zoomName.setTextContent(cName + "_" + zName);
						zoomElement.appendChild(zoomName);
						
						//<DatabaseName>
						Element zoomDBName = doc.createElement("DatabaseName");
						zoomDBName.setTextContent(zoom.name());
						if(zoom.name() == null) {
							zoomDBName.setTextContent(cc.toCamelCase(zoom.getTargetPanel().getComponent().getName(), false)); 
						}
						zoomElement.appendChild(zoomDBName);
						
						//<Label>
						Element zoomLabel = doc.createElement("Label");
						zoomLabel.setTextContent(zoom.getLabel());
						zoomElement.appendChild(zoomLabel);
						
						//<Type>
						String zoomType = cc.toCamelCase(zoom.getTargetPanel().getComponent().getName(), false);
						Element zoomTypeTag = doc.createElement("Type");
						zoomTypeTag.setTextContent(zoomType);
						zoomElement.appendChild(zoomTypeTag);
						
						//<Mandatory>
						Boolean zoomMandatory = zoom.lower() != 0;
						Element zoomMandatoryTag = doc.createElement("Mandatory");
						zoomMandatoryTag.setTextContent(Boolean.toString(zoomMandatory));
						zoomElement.appendChild(zoomMandatoryTag);
					}
					
				}
				
				//<operations> tag
				if(!VisibleClassUtil.containedOperations(vClass).isEmpty()) {
					Element operationsTag = doc.createElement("Operations");
					resourceTag.appendChild(operationsTag);
					
					for(int k=0; k < VisibleClassUtil.containedOperations(vClass).size(); k++) {
						VisibleOperation vo = VisibleClassUtil.containedOperations(vClass).get(k);
						System.out.println("[GENERISEM OPERACIJU] " + vo.getLabel());
						if(vo instanceof BussinessOperation) {
							
							Element opTag = doc.createElement("operation");
							
							//attribute "name"
							Attr opNameAttr = doc.createAttribute("name");
							opNameAttr.setValue(vo.name());
							opTag.setAttributeNode(opNameAttr);
							
							//attribute "label"
							Attr opLabelAttr = doc.createAttribute("label");
							opLabelAttr.setValue(vo.getLabel());
							opTag.setAttributeNode(opLabelAttr);
							
							//attribute "type"
							Attr opTypeAttr = doc.createAttribute("type");
							
							//if the visible operation is a report, put type="report"
							if(vo instanceof Report) {
								opTypeAttr.setValue("report");
							//if the visible operation is a transaction, put type="transaction"
							}else if (vo instanceof Transaction) {
								opTypeAttr.setValue("transaction");
							}
							
							opTag.setAttributeNode(opTypeAttr);
							
							//if the visible operation is a transaction, put type="report" "target"
							Attr opTargetAttr = doc.createAttribute("target");
							//not yet implemented, so put null
							if(((BussinessOperation) vo).getPersistentOperation() == null) {
								opTargetAttr.setValue("null");
							}else {
								opTargetAttr.setValue(((BussinessOperation) vo).getPersistentOperation().name());
							}
							opTag.setAttributeNode(opTargetAttr);
							
							//attribute "allowed"
							//for now, always set to true
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
										//TODO currently not implemented in Kroki :(
										Attr paramLabelAttr = doc.createAttribute("label");
										paramLabelAttr.setValue(param.name());
										paramTag.setAttributeNode(paramLabelAttr);
										
										//attribute "type"
										Attr paramTypeAttr = doc.createAttribute("type");
										paramTypeAttr.setValue(param.type().toString());
										paramTag.setAttributeNode(paramTypeAttr);
										
										//attribute "parameter-type"
										//for now, always set to in
										Attr paramPTypeAttr = doc.createAttribute("parameter-type");
										paramPTypeAttr.setValue("in");
										paramTag.setAttributeNode(paramPTypeAttr);
										
										opTag.appendChild(paramTag);
									}
								}
							}
							operationsTag.appendChild(opTag);
						}else {
							System.out.println("[ELSE]");
						}
					}
					
					
				}
				
			}
			
			writer.write(doc, "resources-generated", AppType.WEB);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
