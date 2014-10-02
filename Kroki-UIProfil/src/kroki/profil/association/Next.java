/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.association;

import kroki.profil.ComponentType;
import kroki.profil.utils.settings.NextSettings;
import kroki.profil.utils.settings.SettingsPanel;

/**
 * Stereotip Next označava da odredišni panel ima ulogu next forme za
 * potrebe aktivacionog panela, na način definisan HCI standardom
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SettingsPanel(NextSettings.class)
public class Next extends VisibleAssociationEnd {

	private static final long serialVersionUID = 1L;
	
    /**
     * Indikator da se  next forma automatski pokreće
     * odmah posle unosa reda u okviru aktivacionog
     * panela.
     */
    private boolean autoActivate = false;
    /**
     * Indikator da se u okviru  next forme u njenom
     * zaglavlju prikazuje vrednost identifikatora (ključa)
     * tekućeg reda aktivacionog panela
     */
    private boolean displayIdentifier = true;
    /**
     * indikator da se u okviru  next forme u njenom
     * zaglavlju prikazuje reprezentativno obeležje iz
     * tekućeg reda aktivacionog panela.
     */
    private boolean displayRepresentative = true;

    public Next() {
        super("link", true, ComponentType.LINK);
    }

    public Next(String label, boolean visible) {
        super(label, visible, ComponentType.LINK);
    }

    public Next(String label) {
        super(label, true, ComponentType.LINK);
    }

    public boolean isAutoActivate() {
        return autoActivate;
    }

    public void setAutoActivate(boolean autoActivate) {
        this.autoActivate = autoActivate;
    }

    public boolean isDisplayIdentifier() {
        return displayIdentifier;
    }

    public void setDisplayIdentifier(boolean displayIdentifier) {
        this.displayIdentifier = displayIdentifier;
    }

    public boolean isDisplayRepresentative() {
        return displayRepresentative;
    }

    public void setDisplayRepresentative(boolean displayRepresentative) {
        this.displayRepresentative = displayRepresentative;
    }
}
