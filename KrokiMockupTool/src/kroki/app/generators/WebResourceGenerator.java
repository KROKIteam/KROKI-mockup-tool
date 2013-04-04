package kroki.app.generators;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import kroki.app.generators.utils.XMLWriter;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Zoom;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.VisibleProperty;

public class WebResourceGenerator {
	
	public void generate(ArrayList<VisibleElement> elements) {
		
		NamingUtil cc = new NamingUtil();
		XMLWriter writer = new XMLWriter();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
		
			//korenski tag <resources>
			Element resourcesRoot = doc.createElement("resources");
			doc.appendChild(resourcesRoot);
			
			//za svaki panel u modelu generise se jedan <resource> tag
			for(int i=0; i<elements.size(); i++) {
				VisibleElement element = elements.get(i);
				VisibleClass vClass = (VisibleClass)element;
				
				//<resource> tag
				Element resourceTag = doc.createElement("resource");
				resourcesRoot.appendChild(resourceTag);
				
				//atributi za <resounce> tag
				String name = cc.toCamelCase(element.name(), false);
				String label = element.getLabel();
				String link = "/resources/" + name;
				Boolean routed = element.isVisible();
				String forms = "StandardForm";
				
				if(element instanceof ParentChild) {
					forms += ",ParentChildForm";
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
				
				//tagovi za atribute
				if(!vClass.containedProperties().isEmpty()) {
					
					//prvo korenski tag <Attributes>
					Element attributesTag = doc.createElement("Attributes");
					resourceTag.appendChild(attributesTag);
					
					//za svaki atribut klase ide <attribute> tag
					for(int j=0; j<vClass.containedProperties().size();j++) {
						VisibleProperty prop = vClass.containedProperties().get(j);
						
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
						} 
						
						Boolean unique = false;
						Boolean mandatory = prop.lower() != 0;
						Boolean representative = prop.isRepresentative();
						
						//<Name>
						Element attrNameTag = doc.createElement("Name");
						attrNameTag.setTextContent(attrName);
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
						
						Element attrRepresentativeTag = doc.createElement("Representative");
						attrRepresentativeTag.setTextContent(Boolean.toString(representative));
						attributeTag.appendChild(attrRepresentativeTag);
						//System.out.println("\t[ATTRIBUTE] \n\t\tName: " + attrName + "\n\t\tLabel: " + attrLabel + "\n\t\tType: " + type + "\n\t\tUnique: " + unique + "\n\t \tMandatory: " + mandatory);
					}
				}
				
				//many-to-one atributi (zoom)
				if(!vClass.containedZooms().isEmpty()) {
					
					//korenski tag <ManyToOneAttributes>
					Element zoomsTag = doc.createElement("ManyToOneAttributes");
					resourceTag.appendChild(zoomsTag);
					
					//za svaki zoom atribut ide <manyToOne> tag
					for(int k=0; k<vClass.containedZooms().size(); k++) {
						Zoom zoom = vClass.containedZooms().get(k);
						StandardPanel zoomPanel = (StandardPanel) zoom.getTargetPanel();
						
						Element zoomElement = doc.createElement("manyToOne");
						zoomsTag.appendChild(zoomElement);
						
						//<Name>
						Element zoomName = doc.createElement("Name");
						zoomName.setTextContent(cc.toCamelCase(zoom.getTargetPanel().getComponent().getName(), true));
						zoomElement.appendChild(zoomName);
						
						//<DatabaseName>
						Element zoomDBName = doc.createElement("DatabaseName");
						zoomDBName.setTextContent(zoom.name());
						zoomElement.appendChild(zoomDBName);
						
						//<Label>
						Element zoomLabel = doc.createElement("Label");
						zoomLabel.setTextContent(zoom.getLabel());
						zoomElement.appendChild(zoomLabel);
						
						//<Type>
						String zoomType =cc.toCamelCase(zoom.getTargetPanel().getComponent().getName(), false);
						Element zoomTypeTag = doc.createElement("Type");
						zoomTypeTag.setTextContent(zoomType);
						zoomElement.appendChild(zoomTypeTag);
						
						//<Mandatory>
						Boolean zoomMandatory = zoom.lower() != 0;
						Element zoomMandatoryTag = doc.createElement("Mandatory");
						zoomMandatoryTag.setTextContent(Boolean.toString(zoomMandatory));
						zoomElement.appendChild(zoomMandatoryTag);
						
						//treba naci <resource> tag sa imenom  koje je isto kao i <Type> tag i dodati mu parent-child formu u listu formi
						for(int l=0; l<doc.getChildNodes().getLength(); l++) {
							Node node = doc.getChildNodes().item(l);

							NodeList resourceNodes = node.getChildNodes();
							
							for(int m=0; m<resourceNodes.getLength(); m++) {
								Node resNode = resourceNodes.item(m);

								NodeList resChildNodes = resNode.getChildNodes();
								
								for(int n=0; n<resChildNodes.getLength(); n++) {
									Node resChildNode = resChildNodes.item(n);
									if(resChildNode.getNodeName().equals("Name")) {
										if(resChildNode.getTextContent().equals(zoomType)) {
											for(int o=0; o<resChildNodes.getLength(); o++) {
												Node rresChildNode = resChildNodes.item(o);
												if(rresChildNode.getNodeName().equals("Forms")) {
													rresChildNode.setTextContent("StandardForm,ParentChildForm");
												}
											}
										}
									}
								}
								
							}
							
						}
						
					}
					
				}
				
			}
			
			writer.write(doc, "resources-generated", false);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
