/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil;

import java.io.Serializable;
import java.util.UUID;

import kroki.mockup.model.Component;
import kroki.profil.group.ElementsGroup;
import kroki.profil.utils.NamingUtils;
import kroki.profil.utils.VisibleElementUtil;
import kroki.uml_core_basic.UmlNamedElement;

/**
 * Klasa predstavlja element modela koji se preslikava na element korisniÄ�kog interfejsa.
 * @author Vladan MarseniÄ‡ (vladan.marsenic@gmail.com)
 */
public class VisibleElement implements UmlNamedElement, Serializable {

	private static final long serialVersionUID = 1L;
	 
	/**Labela*/
    protected String label;
    /**Indikator vidljivosti elementa*/
    protected boolean visible = true;
    /**Komponenta koju referencira*/
    protected Component component;
    /**Tip komponente*/
    protected ComponentType componentType;
    /**Grupa kojoj pripada*/
    protected ElementsGroup parentGroup;
    //OBELEÅ½JA METAKLASE NAMEDELEMENT
    protected String name;
    protected String qualifiedName;
    
	private UUID uuid;
	
	public VisibleElement(){
		uuid = UUID.randomUUID();
	    this.visible = true;
	}
	
    public VisibleElement(String label, boolean visible, ComponentType componentType) {
        this.label = label;
        this.visible = visible;
        this.componentType = componentType;
    	uuid = UUID.randomUUID();
        //nakon ovoga je potrebno kreirati komponentu korisniÄ�kog interfejsa na koju se ovaj element mapira.
        //to se vrÅ¡i na osnovu nabrojanog tipa: Input
    	VisibleElementUtil.createDefaultComponent(this);
    }

    public void update() {
    	if (component != null)
    		component.updateComponent();
    }

    
	@Override
	public boolean equals(Object  other){
		if (other == null)
			return false;
		if (!(other instanceof VisibleElement))
			return false;
		if (((VisibleElement)other).getUuid() == null)
			return false;
		return (uuid.compareTo(((VisibleElement)other).getUuid())== 0);
	}

	public UUID getUuid() {
		return uuid;
	}
	
	public void changeUuid() {
		this.uuid = UUID.randomUUID();
	}
	
    /**************************************************/
    /*IMPLEMENTIRANE METODE INTERFEJSA UmlNamedElement*/
    /**************************************************/
    public String name() {
        return name;
    }

    public String qualifiedName() {
        return qualifiedName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (this.component != null) {
            this.component.setName(label);
        }
        this.label = label;
        this.name = NamingUtils.slugify(label);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public ElementsGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(ElementsGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

}
