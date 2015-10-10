package kroki.profil.association;


import kroki.mockup.model.components.ComboBox;
import kroki.profil.property.VisibleProperty;

/**
 * Stereotype Zoom specifies that the target panel is a zoom form for
 * the activation panel. Follows the HCI standard.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Zoom extends VisibleAssociationEnd {

	private static final long serialVersionUID = 1L;
	
    /**
     * Indicates that the zoom is implemented as 
     * combozoom filled with representative values
     * of the target class
     */
    protected boolean combozoom = true;

    public Zoom(VisibleProperty visibleProperty) {
        this.label = visibleProperty.getLabel();
        this.name = visibleProperty.name();
        this.visible = visibleProperty.isVisible();
        this.componentType = visibleProperty.getComponentType();
        this.component = visibleProperty.getComponent();
        this.component.setParent(null);
        ((ComboBox) this.component).setZoom(true);
    }

    /*****************/
    /*GETTERS AND SETTERS*/
    /*****************/
    public boolean isCombozoom() {
        return combozoom;
    }

    public void setCombozoom(boolean combozoom) {
        this.combozoom = combozoom;
    }
}
