package graphedit.model.components;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.Set;

import graphedit.model.elements.GraphEditElement;
import graphedit.model.properties.Properties;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;

public abstract class GraphElement implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Properties<GraphElementProperties> properties;
	protected GraphEditElement representedElement;
	protected boolean shadowElement = false;
	private boolean loaded = false;
	private Dimension loadedDimension;

	public GraphElement() {
		properties = new Properties<GraphElementProperties>();
	}

	public Object getProperty(GraphElementProperties key) {
		return properties.get(key);
	}
	
	
	public Set<?> getEntrySet() { return null; }
	
	public Object setProperty(GraphElementProperties key, Object value) {
		Object result = properties.set(key, value);
		// uradi nesto
		return result;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public GraphEditElement getRepresentedElement() {
		return representedElement;
	}

	public void setRepresentedElement(GraphEditElement representedElement) {
		this.representedElement = representedElement;
	}

	public boolean isShadowElement() {
		return shadowElement;
	}

	public void setShadowElement(boolean shadowElement) {
		this.shadowElement = shadowElement;
	}
	
	@Override
	public String toString() {
		return (String) properties.get(GraphElementProperties.NAME);
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public Dimension getLoadedDimension() {
		return loadedDimension;
	}

	public void setLoadedDimension(Dimension loadedDimension) {
		this.loadedDimension = loadedDimension;
	}

}