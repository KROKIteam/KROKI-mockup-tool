/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.association;

import kroki.mockup.model.components.ComboBox;
import kroki.profil.property.VisibleProperty;
import kroki.profil.utils.settings.SettingsPanel;
import kroki.profil.utils.settings.ZoomSettings;

/**
 * Stereotip Zoom označava da odredišni panel ima ulogu zoom forme za
 * potrebe aktivacionog panela, na način definisan HCI standardom
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SettingsPanel(ZoomSettings.class)
public class Zoom extends VisibleAssociationEnd {

	private static final long serialVersionUID = 1L;
	
    /**
     * Indikator da se dati  zoom implementira kao
     * combobox  napunjen vrednostima reprezentativnog
     * obeležja odredišne klase.
     */
    protected boolean combozoom = true;

    public Zoom(VisibleProperty visibleProperty) {
        this.label = visibleProperty.getLabel();
        this.visible = visibleProperty.isVisible();
        this.componentType = visibleProperty.getComponentType();
        this.component = visibleProperty.getComponent();
        this.component.setParent(null);
        ((ComboBox) this.component).setZoom(true);
    }

    /*****************/
    /*GETERI I SETERI*/
    /*****************/
    public boolean isCombozoom() {
        return combozoom;
    }

    public void setCombozoom(boolean combozoom) {
        this.combozoom = combozoom;
    }
}
