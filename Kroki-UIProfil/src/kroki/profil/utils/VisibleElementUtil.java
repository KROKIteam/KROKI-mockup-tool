package kroki.profil.utils;

import kroki.mockup.model.Component;
import kroki.mockup.model.components.Button;
import kroki.mockup.model.components.CheckBox;
import kroki.mockup.model.components.ComboBox;
import kroki.mockup.model.components.Link;
import kroki.mockup.model.components.Panel;
import kroki.mockup.model.components.RadioButton;
import kroki.mockup.model.components.TextArea;
import kroki.mockup.model.components.TextField;
import kroki.profil.VisibleElement;


/**
 * Class contains <code>VisibleElement</code> util methods 
 * @author Kroki Team
 */
public class VisibleElementUtil {

    /**
     * Creates default user interface component based on the 
     * component type specified for the given element (text field, text area etc.)
     * Component's type is specified with enumerated type {@link  Input}
     * @param element Visible element
     */
    public static void createDefaultComponent(VisibleElement element) {
    	
    	String label = element.getLabel();
    	Component component = null;
    	
        switch (element.getComponentType()) {
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
        element.setComponent(component);
    }
}
