package graphedit.util;

import graphedit.app.MainFrame;
import graphedit.model.components.Attribute;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Method;
import graphedit.model.components.Parameter;
import graphedit.model.interfaces.GraphEditTreeNode;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;

import java.util.List;

public class Validator {

	public static boolean isValidName(String name) {
		if (name == null || name.isEmpty()) {
			return false;
		}
		char c;
		for (int i = 0; i < name.length(); i++) {
			c = name.charAt(i);
			if (!Character.isLetterOrDigit(c) && c != '_' && c != '-') {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Metoda proverava da li neko od direktnih potomaka <code>parent</code>-a ima ime <code>name</code>.
	 * @param parent
	 * @param name
	 * @return
	 */
	public static boolean hasChildWithName(GraphEditTreeNode parent, String name) {
		Object child;
		for (int i = 0; i < parent.getNodeCount(); i++) {
			child = parent.getNodeAt(i);
			if (child.toString().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if class of attribute has given name
	 * @param elements
	 * @param name
	 * @return
	 */
	public static boolean classHasName(String name){
		if (MainFrame.getInstance().getCurrentView()!=null){
		List<GraphElement> elements = MainFrame.getInstance().getCurrentView().getModel().getDiagramElements();
		for (GraphElement c : elements)
			if (((String)c.getProperty(GraphElementProperties.NAME)).equals(name))
				return true;
		}
		return false;
	}
	
	public static boolean attributeHasName(List<Attribute> attributes, String name) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean parameterHasName(List<Parameter> parameters, String name) {
		for (Parameter attribute : parameters) {
			if (attribute.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean methodHasName(List<Method> methods, String name) {
		for (Method method : methods) {
			if (method.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isJavaIdentifier(String s) {
		if (s.length() == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
			return false;
		}
		for (int i = 1; i < s.length(); i++) {
			if (!Character.isJavaIdentifierPart(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
}
