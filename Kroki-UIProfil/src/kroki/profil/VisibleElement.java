/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil;

import java.io.Serializable;
import java.util.UUID;

import kroki.mockup.model.Component;
import kroki.mockup.model.components.Button;
import kroki.mockup.model.components.CheckBox;
import kroki.mockup.model.components.ComboBox;
import kroki.mockup.model.components.Link;
import kroki.mockup.model.components.Panel;
import kroki.mockup.model.components.RadioButton;
import kroki.mockup.model.components.TextArea;
import kroki.mockup.model.components.TextField;
import kroki.profil.group.ElementsGroup;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.NamingUtils;
import kroki.profil.utils.settings.SettingsPanel;
import kroki.profil.utils.settings.VisibleElementSettings;
import kroki.uml_core_basic.UmlNamedElement;
import kroki.uml_core_basic.UmlPackage;

import com.sun.corba.se.spi.orbutil.fsm.Input;

/**
 * Klasa predstavlja element modela koji se preslikava na element korisniÄ�kog interfejsa.
 * @author Vladan MarseniÄ‡ (vladan.marsenic@gmail.com)
 */
@SettingsPanel(VisibleElementSettings.class)
public class VisibleElement implements UmlNamedElement, Serializable {

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
        createDefaultComponent();
    }

    /*****************/
    /*PRIVATNE METODE*/
    /*****************/
    /**
     * Kreira podrazumevanu komponentu korisniÄ�kog interfejsa
     * u zavisnosti tipa komponente koja mu je pridruÅ¾ena.
     * Tip komponente oznaÄ�en je nabrojanim tipom {@link  Input}
     */
    private void createDefaultComponent() {
        switch (componentType) {
            case TEXT_FIELD: {
                component = new TextField(label, 10);
            }
            break;
            case TEXT_AREA: {
                component = new TextArea(label, 10, 4);
            }
            break;
            case COLUMN: {
                //TODO:
            }
            break;
            case CHECK_BOX: {
                component = new CheckBox(label, true);
            }
            break;
            case COMBO_BOX: {
                component = new ComboBox(label, 10);
            }
            break;
            case SELECTION_LIST: {
                //TODO: 
            }
            break;
            case RADIO_BUTTON: {
                component = new RadioButton(label, true);
            }
            break;
            case LABEL: {
                //TODO:
            }
            break;
            case IMAGE: {
                //TODO:
            }
            break;
            case TABBED_PANE: {
                //TODO:
            }
            break;
            case PANEL: {
                component = new Panel(label);
            }
            break;
            case GRID: {
                //TODO:
            }
            break;
            case BORDER: {
                //TODO:
            }
            break;
            case MENU: {
                //TODO:
            }
            break;
            case MENU_ITEM: {
                //TODO:
            }
            break;
            case BUTTON: {
                component = new Button(label);
            }
            break;
            case LINK: {
                component = new Link(label);
            }
        }
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
