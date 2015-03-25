package kroki.app.action;

import java.util.HashMap;
import javax.swing.AbstractAction;
import kroki.app.action.mockup.AddButtonAction;
import kroki.app.action.mockup.AddCheckBoxAction;
import kroki.app.action.mockup.AddCheckBoxesAction;
import kroki.app.action.mockup.AddComboBoxAction;
import kroki.app.action.mockup.AddGroupBoxAction;
import kroki.app.action.mockup.AddLabelAction;
import kroki.app.action.mockup.AddRadioButtonAction;
import kroki.app.action.mockup.AddRadioGroupAction;
import kroki.app.action.mockup.AddTextAreaAction;
import kroki.app.action.mockup.AddTextFieldAction;

/**
 * Action handler
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Actions {

    public static final String NEW_FILE = "NEW_FILE";
    public static final String NEW_PROJECT = "NEW_PROJECT";
    public static final String OPEN_PROJECT = "OPEN_PROJECT";
    public static final String REDO = "REDO";
    public static final String UNDO = "UNDO";
    public static final String SAVE = "SAVE";
    public static final String SAVE_AS = "SAVE_AS";
    public static final String SAVE_ALL = "SAVE_ALL";
    public static final String EXIT = "EXIT";
    public static final String ADD_BUTTON = "ADD_BUTTON";
    public static final String ADD_CHECKBOXES = "ADD_CHECKBOXES";
    public static final String ADD_COMBOBOX = "ADD_COMBOBOX";
    public static final String ADD_GROUPBOX = "ADD_GROUPBOX";
    public static final String ADD_LABEL = "ADD_LABEL";
    public static final String ADD_RADIOGROUP = "ADD_RADIOGROUP";
    public static final String ADD_TEXTAREA = "ADD_TEXTAREA";
    public static final String ADD_TEXTFIELD = "ADD_TEXTFIELD";
    public static final String ADD_CHECKBOX = "ADD_CHECKBOX";
    public static final String ADD_RADIOBUTTON = "ADD_RADIOBUTTON";
    /**Actions map*/
    private HashMap<String, AbstractAction> actionMap;

    public Actions() {
        actionMap = new HashMap<String, AbstractAction>();
        actionMap.put(NEW_FILE, new NewFileAction());
        actionMap.put(NEW_PROJECT, new NewProjectAction());
        actionMap.put(OPEN_PROJECT, new OpenProjectAction());
        actionMap.put(REDO, new RedoAction());
        actionMap.put(UNDO, new UndoAction());
        actionMap.put(SAVE, new SaveAction());
        actionMap.put(SAVE_AS, new SaveAsAction());
        actionMap.put(SAVE_ALL, new SaveAllAction());
        actionMap.put(EXIT, new ExitAction());
        //actions located of the components palette
        actionMap.put(ADD_BUTTON, new AddButtonAction());
        actionMap.put(ADD_CHECKBOXES, new AddCheckBoxesAction());
        actionMap.put(ADD_COMBOBOX, new AddComboBoxAction());
        actionMap.put(ADD_GROUPBOX, new AddGroupBoxAction());
        actionMap.put(ADD_LABEL, new AddLabelAction());
        actionMap.put(ADD_RADIOGROUP, new AddRadioGroupAction());
        actionMap.put(ADD_TEXTAREA, new AddTextAreaAction());
        actionMap.put(ADD_TEXTFIELD, new AddTextFieldAction());
        actionMap.put(ADD_RADIOBUTTON, new AddRadioButtonAction());
        actionMap.put(ADD_CHECKBOX, new AddCheckBoxAction());
    }

    /**
     * Finds action with the specified key
     * @param key Key
     * @return Action with the specified key
     */
    public AbstractAction getAction(String key) {
        return actionMap.get(key);
    }

    /**
     * Registers a new action
     * @param key Key
     * @param action Action
     */
    public void registerAction(String key, AbstractAction action) {
        actionMap.put(key, action);
    }

    /**
     * Unregisters the action with the specified key
     * @param key Key
     */
    public void unregisterAction(String key) {
        actionMap.remove(key);
    }
}
