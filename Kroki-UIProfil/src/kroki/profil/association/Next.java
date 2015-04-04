package kroki.profil.association;

import kroki.profil.ComponentType;

/**
 * Stereotype Next specifies that the target panel is the next form for
 * the activation panel. Follows the HCI standard.
 * Stereotip Next oznaÄ�ava da odrediÅ¡ni panel ima ulogu next forme za
 * potrebe aktivacionog panela, na naÄ�in definisan HCI standardom
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Next extends VisibleAssociationEnd {

	private static final long serialVersionUID = 1L;
	
    /**Indicates that the next form is automatically
     * activated right after a new row is inserted
     * in the activation panel 
     */
    private boolean autoActivate = false;
    /**
     * Indicates that identificator's value (key)
     * of the current row of the activation panel
     * is shown in the header of the next form
     */
    private boolean displayIdentifier = true;
    /**
     * Indicates that representative  value
     * of the current row of the activation panel
     * is shown in the header of the next form
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
