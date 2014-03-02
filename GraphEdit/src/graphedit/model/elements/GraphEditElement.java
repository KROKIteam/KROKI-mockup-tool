package graphedit.model.elements;

import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import kroki.uml_core_basic.UmlNamedElement;

public interface GraphEditElement {


	public UmlNamedElement getUmlElement();
	public GraphElement element();
	public void setUmlElement(UmlNamedElement umlElement);
	public void setElement(GraphElement element);
	public void setName(String newName);
	public void link(Link link, Object...args);
	public void unlink(Link link, Object...args);
	public void changeLinkProperty(Link link, LinkProperties property, Object newValue, Object...args);
	public void setOldLink(Link link, Object...args);


}
